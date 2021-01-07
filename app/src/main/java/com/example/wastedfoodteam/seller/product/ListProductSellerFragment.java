package com.example.wastedfoodteam.seller.product;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ListProductSellerFragment extends ListFragment {

    ArrayList<Product> arrProduct;
    ProductSellerAdapter adapter;
    Product product;
    ListView lvProduct;
    TextView tv_total_product;
    LoadingDialog loadingDialog;
    int seller_id;
    int totalProduct;
    int page;
    boolean isEnd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ListProductSellerFragment", "Show the list view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_product_seller, container, false);
        //mapping view
        lvProduct = view.findViewById(android.R.id.list);
        loadingDialog = new LoadingDialog(getActivity());
        seller_id = Variable.SELLER.getId();
        product = Variable.PRODUCT;
        if(!isHasProduct(arrProduct)){
            refreshListProduct();
        }

        tv_total_product = view.findViewById(R.id.tv_total_product);
        adapter = new ProductSellerAdapter(getActivity().getApplicationContext(), R.layout.list_seller_product, arrProduct, getResources(),getActivity());
        lvProduct.setAdapter(adapter);
        page = 1;
        lvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (!isEnd) {
                        page++;
                        getListProductSeller();
                    }
                }
            }
        });
        getTotalProduct(Variable.IP_ADDRESS + "seller/getTotalProduct.php" + "?seller_id=" + Variable.SELLER.getId());
        /*lvProduct.setOnTouchListener(new View.OnTouchListener() {
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
        });*/
        return view;
    }

    private boolean isHasProduct(ArrayList<Product> arrProduct) {
        return arrProduct != null && arrProduct.size() > 0;
    }


    public void getListProductSeller() {
        String url =  Variable.IP_ADDRESS + "seller/getListProductSellerPaging.php?seller_id=" + seller_id + "&page=" + page;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!"end".equalsIgnoreCase(response)) {
                                isEnd = false;
                                JSONArray jsonProducts = new JSONArray(response);
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                for (int i = 0; i < jsonProducts.length(); i++) {
                                    arrProduct.add(gson.fromJson(jsonProducts.getString(i), Product.class));
                                }
                                adapter.setProductList(arrProduct);
                                adapter.notifyDataSetChanged();
                            } else {
                                isEnd = true;
                            }
                            loadingDialog.dismissDialog();
                        } catch (Exception e) {
                            Log.e("ListProduct", response);
                            e.printStackTrace();
                            loadingDialog.dismissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismissDialog();
                    }
                });
        loadingDialog.startLoadingDialog();
        requestQueue.add(jsonArrayRequest);
    }

    public void getTotalProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            totalProduct = Integer.parseInt(response);
                            tv_total_product.setText(totalProduct + " sản phẩm");
                        } catch (Exception e) {
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


    private void refreshListProduct() {
        page = 1;
        isEnd = false;
        createNewArrayProduct();
        getListProductSeller();
    }

    public void createNewArrayProduct() {
        arrProduct = new ArrayList<>();
        if (adapter != null) {
            adapter.getProductList().clear();
            adapter.setProductList(arrProduct);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("ListProductSellerFragment", "On item clicked");
        Variable.PRODUCT = (Product) l.getAdapter().getItem(position);

        ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
        //open seller detail product fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, productOrderSellerFragment, "")
                .addToBackStack(null)
                .commit();
    }
}