package com.example.wastedfoodteam.utils.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.model.Buyer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

public class BuyerVolley {
    private final RequestQueue requestQueue;
    private String url;


    public BuyerVolley(Context context, String url) {
        this.url = url;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void setRequestGetBuyerBy3rdId(final BuyerResponseCallback callback, String thirdPartyId) {
        url = url + "?thirdPartyID=" + thirdPartyId;
        StringRequest getBuyerRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray buyers = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    Buyer buyer = gson.fromJson(buyers.getString(0), Buyer.class);
                    callback.onSuccess(buyer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(getBuyerRequest);
    }
}
