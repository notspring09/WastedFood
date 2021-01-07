package com.example.wastedfoodteam.utils.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.buyer.followseller.SellerExtraInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SellerExtraVolley {
    private final Context context;
    private final RequestQueue requestQueue;
    private String url;

    public SellerExtraVolley(Context context, String url) {
        this.context = context;
        this.url = url;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * get seller information
     */
    public void setRequestGetSeller(final SellerResponseCallback callback, final String id) {
        url = url + "?id=" + id;
        StringRequest getBuyerRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    SellerExtraInfo seller = gson.fromJson(response, SellerExtraInfo.class);
                    callback.onSuccess(seller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        }) ;
        requestQueue.add(getBuyerRequest);
    }


}

