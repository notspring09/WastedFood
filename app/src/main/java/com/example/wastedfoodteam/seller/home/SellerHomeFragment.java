package com.example.wastedfoodteam.seller.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;

import org.json.JSONException;
import org.json.JSONObject;

public class SellerHomeFragment extends Fragment {

    TextView tvTotalProductSelling,tvTotalProduct,tvOrderBuying,tvTotalOrderSuccess,tvTotalOrderCancel,tvTotalFollower;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        tvTotalFollower = view.findViewById(R.id.tvTotalFollower);
        tvOrderBuying = view.findViewById(R.id.tvOrderBuying);
        tvTotalOrderCancel = view.findViewById(R.id.tvTotalOrderCancel);
        tvTotalOrderSuccess = view.findViewById(R.id.tvTotalOrderSuccess);
        tvTotalProduct = view.findViewById(R.id.tvTotalProduct);
        tvTotalProductSelling = view.findViewById(R.id.tvTotalProductSelling);
        getSellerHome();
         return  view;
    }

    private void getSellerHome() {
        String urlGetData = Variable.IP_ADDRESS + "seller/getHomePageSellerData.php?seller_id=" + Variable.SELLER.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonSellerHome = new JSONObject(response);

                            tvTotalProductSelling.setText(jsonSellerHome.getString("totalProductSelling") + " sản phẩm") ;
                            tvTotalFollower.setText(jsonSellerHome.getString("totalFollower") + " người") ;
                            tvOrderBuying.setText(jsonSellerHome.getString("totalOrderBuying") + " đơn hàng") ;
                            tvTotalOrderCancel.setText(jsonSellerHome.getString("totalOrderCancel") + " đơn hàng") ;
                            tvTotalOrderSuccess.setText(jsonSellerHome.getString("totalOrderSuccess") + " đơn hàng") ;
                            tvTotalProduct.setText(jsonSellerHome.getString("totalProduct") + " sản phẩm" ) ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(getSellerRequestString);
    }
}