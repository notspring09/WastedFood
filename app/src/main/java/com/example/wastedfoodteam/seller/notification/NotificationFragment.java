package com.example.wastedfoodteam.seller.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class NotificationFragment extends ListFragment {

    ListView lvNotification;
    TextView tvEmpty;
    ArrayList<Notification> arrNotification;
    NotificationAdapter notificationAdapter;
    final String receiver_id;

    public NotificationFragment(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        lvNotification = view.findViewById(android.R.id.list);
        tvEmpty = view.findViewById(android.R.id.empty);
        arrNotification = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(requireActivity().getApplicationContext(), R.layout.list_seller_notification, arrNotification, getResources(), getActivity());
        lvNotification.setAdapter(notificationAdapter);
        lvNotification.setEmptyView(tvEmpty);
        if (Variable.CURRENT_USER.equals("SELLER"))
            getNotificationDataSeller();
        else
            getNotificationDataBuyer(receiver_id);

        return view;

    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Notification notification = (Notification) l.getAdapter().getItem(position);
        super.onListItemClick(l, v, position, id);
    }

    public void getNotificationDataSeller() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        String urlGetData = Variable.IP_ADDRESS + "notification/getListNotificationSeller.php?receiver_id=" + Variable.SELLER.getId();//TODO missing product id???
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonOrders.length(); i++) {
                                Notification notification = gson.fromJson(jsonOrders.getString(i), Notification.class);
                                arrNotification.add(gson.fromJson(jsonOrders.getString(i), Notification.class));
                                notificationAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.e("ResponseString", response);
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

    public void getNotificationDataBuyer(String receiver_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        String urlGetData = Variable.IP_ADDRESS + "notification/getListNotificationBuyer.php?receiver_id=" + receiver_id;//TODO missing product id???
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonOrders.length(); i++) {
                                Notification notification = gson.fromJson(jsonOrders.getString(i), Notification.class);
                                arrNotification.add(gson.fromJson(jsonOrders.getString(i), Notification.class));
                                notificationAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.e("ResponseString", response);
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


}