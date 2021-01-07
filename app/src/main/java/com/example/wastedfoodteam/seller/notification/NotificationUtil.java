package com.example.wastedfoodteam.seller.notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class NotificationUtil {
    public void addNotification(final Context context , final int sender_id , final int receiver_id , final String content , final int order_id ) {
        String url = Variable.IP_ADDRESS + "/notification/addNotification.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender_id", String.valueOf(sender_id));
                params.put("receiver_id", String.valueOf(receiver_id));
                params.put("content", content);
                params.put("order_id", String.valueOf(order_id));
                params.put("modified_date" , CommonFunction.getCurrentDatetime() + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getTotalNotification(Context context, int receiver_id , final BottomNavigationView bottomNavigationView){
        String url = Variable.IP_ADDRESS + "notification/getTotalNotification.php?receiver_id=" + receiver_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Variable.TOTAL_NOTIFICATION = Integer.parseInt(response);
                            if(Variable.TOTAL_NOTIFICATION > 0) {
                                BadgeDrawable badge;
                                if(Variable.CURRENT_USER.equals("BUYER"))
                                    badge = bottomNavigationView.getOrCreateBadge(R.id.item_bottom_nav_menu_buyer_notification);
                                else
                                    badge = bottomNavigationView.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
                                badge.setVisible(true);
                                badge.setNumber(Variable.TOTAL_NOTIFICATION);
                            }
                            Log.i("Notification",Variable.TOTAL_NOTIFICATION + "");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(getProductAround);
    }

    //update notification data after user seen
    public void updateNotificationSeen(final Context context , final int receiver_id , final BottomNavigationView bottomNavigationView){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = Variable.IP_ADDRESS + "notification/editSeenNotification.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            BadgeDrawable badge;
                            if(Variable.CURRENT_USER.equals("SELLER"))
                                badge = bottomNavigationView.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
                            else
                                badge = bottomNavigationView.getOrCreateBadge(R.id.item_bottom_nav_menu_buyer_notification);
                            Variable.TOTAL_NOTIFICATION = 0;
                            badge.setVisible(false);
                            badge.clearNumber();
                        }else{
                            Toast.makeText(context,"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("receiver_id", String.valueOf(receiver_id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
