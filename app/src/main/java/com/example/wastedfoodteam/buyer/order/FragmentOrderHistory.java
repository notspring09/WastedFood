package com.example.wastedfoodteam.buyer.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentOrderHistory extends ListFragment {
    final String urlGetData = Variable.IP_ADDRESS + Variable.ORDER_HISTORY + "?buyer_id=" + Variable.BUYER.getId();
    ArrayList<BuyerOrder> orderArrayList;
    OrderAdapter adapter;
    ListView lvOrder;
    TextView tvEmpty;
    FragmentOrderDetail orderDetail;
    Bundle bundleDetail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_order_history, container, false);

        //mapping view
        lvOrder = view.findViewById(android.R.id.list);
        tvEmpty = view.findViewById(android.R.id.empty);

        //setup bundle
        bundleDetail = new Bundle();

        //set up list display
        orderArrayList = new ArrayList<>();
        adapter = new OrderAdapter(requireActivity().getApplicationContext(), R.layout.list_buyer_order_history_item, orderArrayList, getResources());
        lvOrder.setAdapter(adapter);
        lvOrder.setEmptyView(tvEmpty);
        getListOrderHistory();
        return view;
    }

    private void getListOrderHistory() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setUpAdapterItems(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Có lỗi bất thường xảy ra, vui lòng thử lại.", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(getProductAround);
    }

    private void setUpAdapterItems(String response) {
        try {
            JSONArray jsonOrders = new JSONArray(response);

            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
            Gson gson = gsonBuilder.create();

            for (int i = 0; i < jsonOrders.length(); i++) {
                orderArrayList.add(gson.fromJson(jsonOrders.getString(i), BuyerOrder.class));
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.e("StringResponse", response);
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        BuyerOrder order = (BuyerOrder) l.getAdapter().getItem(position);

        //put bundle
        bundleDetail.putSerializable("PRODUCT", order);
        orderDetail = new FragmentOrderDetail(order);//TODO check again
        orderDetail.setArguments(bundleDetail);

        //open detail order fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, orderDetail, "")
                .addToBackStack(null)
                .commit();
    }
}
