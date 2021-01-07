package com.example.wastedfoodteam.buyer.followseller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.buyer.buy.FragmentDetailProduct;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.ReportDialog;
import com.example.wastedfoodteam.utils.service.FollowResponseCallback;
import com.example.wastedfoodteam.utils.service.FollowVolley;
import com.example.wastedfoodteam.utils.service.SellerExtraVolley;
import com.example.wastedfoodteam.utils.service.SellerResponseCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentSellerDetail extends ListFragment {
    String urlGetData;
    ArrayList<BuyerProduct> arrProduct;
    ProductAdapterOfSeller adapter;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    Seller seller;
    TextView tvNameSeller, tvAddress, tvDescription, tvRatingFSD, tvFollowFSD, tvProductFSD, tvDistance;
    ImageView ivPhotoSeller;
    ListView lvProduction;
    ImageButton ibReport, ibFollow;
    CameraStorageFunction cameraStorageFunction;
    private FollowVolley followVolley;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_get_seller_detail, container, false);
        mappingViewWithVariable(view);
        //set up url volley

        assert getArguments() != null;
        seller = (Seller) getArguments().get("SELLER");

        setViewContent();

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), null);

        ibReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog = new ReportDialog(getActivity(), getLayoutInflater(), seller, cameraStorageFunction, Variable.BUYER.getId() + "");
                reportDialog.displayReportDialog();
            }
        });

        //set up url
        urlGetData = Variable.IP_ADDRESS + "search/getListProductsOfSeller.php?seller_id=" + seller.getId()
                + "&lat=" + Variable.gps.getLatitude()
                + "&lng=" + Variable.gps.getLongitude();

        followVolley = new FollowVolley(requireActivity().getApplicationContext(), ibFollow);
        followVolley.setRequestGetFollow(Variable.IP_ADDRESS + Variable.GET_FOLLOW, Variable.BUYER.getId(), seller.getId());


        //mapping view
        lvProduction = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapterOfSeller(requireActivity().getApplicationContext(), R.layout.list_seller_product_item, arrProduct, getResources());
        lvProduction.setAdapter(adapter);
        getListProduct(urlGetData);
        getSellerExtraInfo();

        lvProduction.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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

        ibFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followVolley.onIbFollowClick();
            }
        });

        return view;
    }

    private void setViewContent() {
        tvRatingFSD.setText("Điểm đánh giá:" + seller.getRating() + "");
        tvNameSeller.setText(seller.getName() != null ? seller.getName() : "Chưa được đặt tên");
        tvAddress.setText(seller.getAddress() != null ? seller.getAddress() : "Chưa có địa chỉ");
        tvDescription.setText(seller.getDescription() != null ? seller.getDescription() : "Chưa có thông tin");
        tvDistance.setText(CommonFunction.getStringDistance(seller, Variable.gps));
        CommonFunction.setImageViewSrc(requireActivity().getApplicationContext(), seller.getImage(), ivPhotoSeller);
    }

    private void getSellerExtraInfo() {
        SellerExtraVolley sellerExtraVolley = new SellerExtraVolley(getActivity(), Variable.IP_ADDRESS + "follow/getSellerExtraInfo.php");
        sellerExtraVolley.setRequestGetSeller(new SellerResponseCallback() {
            @Override
            public void onSuccess(SellerExtraInfo seller) {
                tvFollowFSD.setText("Số người theo dõi: " + seller.follow_total);
                tvProductFSD.setText("Số sản phẩm đã bán: " + seller.product_total);
            }
        }, seller.getId() + "");
    }

    private void mappingViewWithVariable(View view) {
        tvNameSeller = view.findViewById(R.id.tvNameSellerFSD);
        tvAddress = view.findViewById(R.id.tvAddressFSD);
        tvDescription = view.findViewById(R.id.tvDescriptionFSD);
        tvRatingFSD = view.findViewById(R.id.tvRatingFSD);
        tvFollowFSD = view.findViewById(R.id.tvFollowFSD);
        tvProductFSD = view.findViewById(R.id.tvProductFSD);
        tvDistance = view.findViewById(R.id.tvDistance);
        ivPhotoSeller = view.findViewById(R.id.ivPhotoSellerFSD);
        ibReport = view.findViewById(R.id.ibReport);
        ibFollow = view.findViewById(R.id.iBtnFollow);
    }

    public void getListProduct(String urlGetData) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setUpItemsForAdapter(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(getProductAround);
    }

    private void setUpItemsForAdapter(String response) {
        try {
            JSONArray jsonProducts = new JSONArray(response);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            for (int i = 0; i < jsonProducts.length(); i++) {
                arrProduct.add(gson.fromJson(jsonProducts.getString(i), BuyerProduct.class));
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ResponseString", response);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        BuyerProduct product = (BuyerProduct) l.getAdapter().getItem(position);

        //put bundle
        bundleDetail.putSerializable("PRODUCT", product);
        detailProduct = new FragmentDetailProduct();
        detailProduct.setArguments(bundleDetail);

        //open detail product fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, detailProduct, "")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPause() {
        try {
            boolean isFollow = false;
            if (ibFollow.getTag().equals(R.drawable.followed)) isFollow = true;
            String UPDATE_FOLLOW_URL = Variable.IP_ADDRESS + Variable.UPDATE_FOLLOW;
            followVolley.setRequestUpdateFollow(new FollowResponseCallback() {
                @Override
                public void onSuccess(String result) {

                }
            }, UPDATE_FOLLOW_URL, Variable.BUYER.getId(), seller.getId(), isFollow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
    }
}
