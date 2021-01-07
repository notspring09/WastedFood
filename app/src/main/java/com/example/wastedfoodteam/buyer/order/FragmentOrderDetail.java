package com.example.wastedfoodteam.buyer.order;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.followseller.FragmentSellerDetail;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.DirectionParser;
import com.example.wastedfoodteam.utils.RatingDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentOrderDetail extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private BuyerOrder order;
    private TextView tvProductName, tvDistance, tvPrice, tvSellerName;
    private ImageView ivProduct;

    public FragmentOrderDetail() {
    }

    public FragmentOrderDetail(BuyerOrder order) {
        this.order = order;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_order_detail, container, false);

        mappingViewWithVariables(view);
        setViewContent();

        //show dialog
        setRatingDialog();

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleDetail = new Bundle();
                bundleDetail.putSerializable("SELLER", order.getProduct().getSeller());
                FragmentSellerDetail restaurant = new FragmentSellerDetail();
                restaurant.setArguments(bundleDetail);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSearchResultAH, restaurant, "")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private void setRatingDialog() {
        if (order.isNotRatingYet()) {
            RatingDialog ratingDialog = new RatingDialog(getActivity(), getLayoutInflater(), order);
            ratingDialog.displayRatingOrderDialog();
        }
    }

    private void setViewContent() {
        CommonFunction.setImageViewSrc(getActivity().getApplicationContext(), order.getProduct().getImage(), ivProduct);
        tvProductName.setText("Sản phẩm: " + order.getProduct().getName());
        tvSellerName.setText(order.getProduct().getSeller().getName());
        tvPrice.setText(CommonFunction.getCurrency(order.getTotal_cost()));
        tvDistance.setText(CommonFunction.getStringDistance(order.getProduct().getSeller(), Variable.gps));
    }

    private void mappingViewWithVariables(@NotNull View view) {
        tvProductName = view.findViewById(R.id.tvProductName);
        tvPrice = view.findViewById(R.id.tvSellPrice);
        tvSellerName = view.findViewById(R.id.tvSellerName);
        tvDistance = view.findViewById(R.id.tvDistance);
        ivProduct = view.findViewById(R.id.ivProduct);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker in currentPlace and move the camera
        LatLng currentPlace = new LatLng(Variable.gps.getLatitude(), Variable.gps.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentPlace).title("Bạn ở đây"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(order.getProduct().getSeller().getLatitude(), order.getProduct().getSeller().getLongitude())).title(order.getProduct().getSeller().getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 16f));
        new TaskDirectionRequest().execute(buildRequestUrl(currentPlace, new LatLng(order.getProduct().getSeller().getLatitude(), order.getProduct().getSeller().getLongitude())));
    }

    @NotNull
    private String buildRequestUrl(@NotNull LatLng origin, @NotNull LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_direct_api);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + APIKEY;
        Log.d("TAG", url);
        return url;
    }


    /**
     * Request direction from Google Direction API
     *
     * @param requestedUrl see {@link #buildRequestUrl(LatLng, LatLng)}
     * @return JSON data routes/direction
     */
    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }


    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList<LatLng> points;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList<>();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15f);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }


}
