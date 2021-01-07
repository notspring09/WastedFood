package com.example.wastedfoodteam.utils.service;

import android.content.Context;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;

import java.util.HashMap;
import java.util.Map;

public class FollowVolley {
    private final RequestQueue requestQueue;
    private final ImageButton ibFollow;
    public FollowVolley(Context context,ImageButton ibFollow) {
        this.ibFollow = ibFollow;
        this.requestQueue = Volley.newRequestQueue(context);

    }

    public void setRequestGetFollow( String url, final int buyer, final  int seller){
        url = url + "?buyer_id=" + buyer + "&seller_id=" +seller;
        StringRequest getFollowRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("TRUE")) {
                    ibFollow.setImageResource(R.drawable.followed);
                    ibFollow.setTag(R.drawable.followed);
                } else {
                    ibFollow.setTag(R.drawable.not_followed);
                    ibFollow.setImageResource(R.drawable.not_followed);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(getFollowRequest);
    }

    public void setRequestUpdateFollow(final FollowResponseCallback callback,String url, final int buyer, final  int seller, final boolean isFollow){
        StringRequest updateFollowRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("buyer_id",buyer + "");
                params.put("seller_id",seller+"");
                params.put("is_follow",isFollow + "");
                return params;
            }
        };
        requestQueue.add(updateFollowRequest);
    }

    public void onIbFollowClick(){
        if (ibFollow.getTag() != null)
            if (isImageButtonIsFollowed(ibFollow.getTag())) {
                changeButtonFollowStatus(ibFollow, R.drawable.not_followed);
            } else {
                changeButtonFollowStatus(ibFollow, R.drawable.followed);
            }
    }

    private boolean isImageButtonIsFollowed(Object tag) {
        return tag.equals(R.drawable.followed);
    }

    private void changeButtonFollowStatus(ImageButton ibFollow, int resourceId) {
        ibFollow.setImageResource(resourceId);
        ibFollow.setTag(resourceId);
    }
}
