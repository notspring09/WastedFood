package com.example.wastedfoodteam.utils.service;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;

import java.util.HashMap;
import java.util.Map;

public class UpdateStatusForOrder {
    //update order status
    public static void updateOrderStatus(String url, final Order.Status status, final int id, final Context myContext , final FragmentActivity activity){
        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(myContext,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productOrderSellerFragment, productOrderSellerFragment.getTag()).commit();
                            //TODO move back to home
                        }else{
                            Toast.makeText( myContext,"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myContext,"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("status",  status.name());
                params.put("id" ,  String.valueOf(id) );
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
