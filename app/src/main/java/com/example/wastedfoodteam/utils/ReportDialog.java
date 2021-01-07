package com.example.wastedfoodteam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Account;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Seller;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ReportDialog {

    String url;
    String accusedId;
    final String reporterId;
    String content = "";

    private ImageView ivReport;
    private EditText etContent;
    final CameraStorageFunction cameraStorageFunction;

    final Context context;
    final LayoutInflater inflater;
    final Account accused;


    public ReportDialog(Context context, LayoutInflater inflater, Account accused, CameraStorageFunction cameraStorageFunction, String reporterId) {
        this.context = context;
        this.inflater = inflater;
        this.accused = accused;
        this.cameraStorageFunction = cameraStorageFunction;
        this.reporterId = reporterId;
    }


    public void displayReportDialog() {
        View ratingLayout = inflater.inflate(R.layout.dialog_report, null);
        TextView tvAccused = ratingLayout.findViewById(R.id.tvAccusedDR);
        etContent = ratingLayout.findViewById(R.id.etContentDR);
        ivReport = ratingLayout.findViewById(R.id.ivReport);


        if (accused.getClass().equals(Seller.class))
            tvAccused.setText(((Seller) accused).getName());
        else
            tvAccused.setText(((Buyer) accused).getName());

        url = Variable.IP_ADDRESS + "feedbackReport/report.php";
        accusedId = accused.getId() + "";

        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.setImageView(ivReport);
                cameraStorageFunction.showImagePickDialog();
            }
        });

        AlertDialog.Builder builderDialogRating = setUpDialogBuilder(ratingLayout);
        AlertDialog dialogRating = builderDialogRating.create();
        dialogRating.show();
    }

    @NotNull
    private AlertDialog.Builder setUpDialogBuilder(View ratingLayout) {
        AlertDialog.Builder builderDialogRating = new AlertDialog.Builder(context);
        builderDialogRating.setTitle("Báo cáo vi phạm");
        builderDialogRating.setView(ratingLayout);
        builderDialogRating.setCancelable(true);

        builderDialogRating.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderDialogRating.setPositiveButton("GỬI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cameraStorageFunction.getImage_uri() != null)
                    cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                        @Override
                        public void onSuccess(String imageUrl) {
                            content = etContent.getText().toString();
                            insertData(url, reporterId, accusedId, content, imageUrl);
                        };

                        @Override
                        public void onError() {

                        }
                    });
                else {
                    content = etContent.getText().toString();
                    insertData(url, reporterId, accusedId, content, "");
                }
            }
        });
        return builderDialogRating;
    }

    private void insertData(final String url, final String reporter_id, final String accused_id, final String report_text, final String report_image) {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ("ERROR".equals(response)) {
                    Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Báo cáo thành công", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reporter_id", reporter_id);
                params.put("accused_id", accused_id);
                params.put("report_text", report_text);
                params.put("report_image", report_image);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}
