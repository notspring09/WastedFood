package com.example.wastedfoodteam.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RatingDialog {
    private RatingBar rbRating;
    private EditText etRating;
    final private String urlUpdateRating = Variable.IP_ADDRESS + Variable.UPDATE_RATING;
    final Context context;
    final LayoutInflater inflater;
    final Order order;

    public RatingDialog(Context context, LayoutInflater inflater, Order order) {
        this.context = context;
        this.inflater = inflater;
        this.order = order;
    }

    public void displayRatingOrderDialog() {
        @SuppressLint("InflateParams") View ratingLayout = inflater.inflate(R.layout.dialog_buyer_rating, null);
        rbRating = ratingLayout.findViewById(R.id.rbRating);
        etRating = ratingLayout.findViewById(R.id.etRating);
        AlertDialog.Builder builderDialogRating = setUpDialogBuilder(ratingLayout);
        AlertDialog dialogRating = builderDialogRating.create();
        dialogRating.show();
    }

    @NotNull
    private AlertDialog.Builder setUpDialogBuilder(View ratingLayout) {
        AlertDialog.Builder builderDialogRating = new AlertDialog.Builder(context);
        builderDialogRating.setTitle("Đánh giá");
        builderDialogRating.setView(ratingLayout);
        builderDialogRating.setCancelable(true);

        builderDialogRating.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderDialogRating.setPositiveButton("Đánh giá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRating(dialog);
            }
        });
        return builderDialogRating;
    }


    private void sendRating(final DialogInterface dialog) {
        RequestQueue requestInsertOrder = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, urlUpdateRating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(context.getApplicationContext(), "Thành công", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "Lỗi hệ thống: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", order.getId() + "");
                params.put("rating", rbRating.getRating() + "");
                params.put("buyer_comment", etRating.getText().toString() + "");
                return params;
            }
        };
        requestInsertOrder.add(stringRequestInsert);
    }


}