package com.example.wastedfoodteam.buyer.followseller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentListSellerFollow extends ListFragment {
    final String urlGetData = Variable.IP_ADDRESS + "information/getListSellerFollow.php?buyer_id=" + Variable.BUYER.getId();
    TextView tvEmpty;
    SellerFollowAdapter adapter;
    ArrayList<Seller> listSellers;
    ListView lvSeller;
    Bundle bundleDetail;
    FragmentSellerDetail restaurant;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_seller_follow, container, false);
        lvSeller = view.findViewById(android.R.id.list);
        tvEmpty = view.findViewById(android.R.id.empty);
        lvSeller.setEmptyView(tvEmpty);

        listSellers = new ArrayList<>();
        adapter = new SellerFollowAdapter(requireActivity().getApplicationContext(), R.layout.list_seller_follow_item, listSellers, getResources());
        lvSeller.setAdapter(adapter);
        lvSeller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
        getListFollowSellerOfCurrentUser(urlGetData);
        return view;
    }

    public void getListFollowSellerOfCurrentUser(final String urlGetData) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setUpDataForAdapter(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Lỗi kết nỗi" + urlGetData, Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(getProductAround);
    }

    private void setUpDataForAdapter(String response) {
        try {
            JSONArray jsonProducts = new JSONArray(response);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            for (int i = 0; i < jsonProducts.length(); i++) {
                listSellers.add(gson.fromJson(jsonProducts.getString(i), Seller.class));
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Seller seller = (Seller) l.getAdapter().getItem(position);

        //put bundle
        bundleDetail = new Bundle();
        bundleDetail.putSerializable("SELLER", seller);
        restaurant = new FragmentSellerDetail();
        restaurant.setArguments(bundleDetail);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, restaurant, "")
                .addToBackStack(null)
                .commit();
    }
}
