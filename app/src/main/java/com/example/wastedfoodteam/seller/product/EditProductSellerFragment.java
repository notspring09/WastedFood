package com.example.wastedfoodteam.seller.product;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.home.SellerHomeFragment;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;
import com.example.wastedfoodteam.seller.order.SellerOrder;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class EditProductSellerFragment extends Fragment {
    private EditText name,description;
    private EditText originalPrice;
    private EditText sellPrice;
    private EditText openTime,closeTime,quantity,remainQuantity;
    private String storageLocation;
    private Button btnCancel;
    TextInputLayout tilProductName,tilRemainQuantity;
    int ori_quantity;
    private TextView tvCountProductName,tvCountProductDescription;
    int id;

    CameraStorageFunction cameraStorageFunction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_seller_detail_product, container, false);
        //unit ui
        //ui view
        ImageView iv_detail_product_icon = view.findViewById(R.id.iv_detail_product_icon);
        name = view.findViewById(R.id.editText_detail_product_name);
        btnCancel = view.findViewById(R.id.btn_cancel_FSAP);
        originalPrice = view.findViewById(R.id.editText_detail_product_originalPrice);
        sellPrice = view.findViewById(R.id.editText_detail_product_sellPrice);
        openTime = view.findViewById(R.id.editText_detail_product_openTime);
        closeTime = view.findViewById(R.id.editText_detail_product_closeTime);
        tilProductName = view.findViewById(R.id.textInputDetailName);
        tilRemainQuantity = view.findViewById(R.id.textInputRemainQuantity);
        description = view.findViewById(R.id.etDescription);
        tvCountProductName = view.findViewById(R.id.tvCountProductName);
        tvCountProductDescription = view.findViewById(R.id.tvCountProductDescription);
        quantity = view.findViewById(R.id.etQuantity);
        remainQuantity = view.findViewById(R.id.etRemainQuantity);
        Button btn_detail_product_add = view.findViewById(R.id.btn_detail_product_add);
        //input data
        id = Variable.PRODUCT.getId();
        CommonFunction.setImageViewSrc(getContext(),Variable.PRODUCT.getImage(), iv_detail_product_icon);
        name.setText(Variable.PRODUCT.getName());
        description.setText(Variable.PRODUCT.getDescription());
        originalPrice.setText(String.valueOf(Variable.PRODUCT.getOriginal_price()));
        sellPrice.setText(String.valueOf(Variable.PRODUCT.getSell_price()));
        openTime.setText( new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Variable().PRODUCT.getStart_time()));
        closeTime.setText( new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Variable().PRODUCT.getEnd_time()));
        quantity.setText(String.valueOf(Variable.PRODUCT.getOriginal_quantity()));
        remainQuantity.setText(String.valueOf(Variable.PRODUCT.getRemain_quantity()));
        closeTime.setEnabled(false);
        openTime.setEnabled(false);
        quantity.setEnabled(false);
        originalPrice.setEnabled(false);
        sellPrice.setEnabled(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, productOrderSellerFragment, "")
                        .addToBackStack(null)
                        .commit();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvCountProductName.setText(s.length() + "/100");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCountProductName.setText(s.length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCountProductDescription.setText(s.length()+"/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), iv_detail_product_icon);
        //
        iv_detail_product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });

        btn_detail_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunction.checkEmptyEditText(name) && CommonFunction.checkEmptyEditText(remainQuantity)) {
                    ori_quantity = Integer.parseInt(remainQuantity.getText().toString().trim()) - Variable.PRODUCT.getRemain_quantity() + Variable.PRODUCT.getOriginal_quantity();
                    if (cameraStorageFunction.getImage_uri() != null) {
                        final int finalOri_quantity = ori_quantity;
                        cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                            @Override
                            public void onSuccess(String url) {
                                storageLocation = url;
                                String urlGetData = Variable.IP_ADDRESS + "seller/updateProductByID.php";
                                updateProduct(urlGetData, finalOri_quantity);
                            }

                            ;

                            @Override
                            public void onError() {

                            }
                        });
                    } else {
                        storageLocation = " ";
                        String urlGetData = Variable.IP_ADDRESS + "seller/updateProductByID.php";
                        updateProduct(urlGetData, ori_quantity);
                    }
                }else if(!CommonFunction.checkEmptyEditText(name)){
                    tilProductName.setError("Tên sản phẩm phải có ít nhất 1 ký tự");
                }else if(!CommonFunction.checkEmptyEditText(remainQuantity)){
                    tilRemainQuantity.setError("Số lượng còn lại phải có ít nhất 1 ký tự");
                }
            }
        });
        tvCountProductDescription.setText(description.getText().toString().trim().length()+"/300");
        tvCountProductName.setText(name.getText().toString().trim().length()+"/100");
        return view;
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("SellerDetailProductFragment", "handle image pick");
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    //update product data
    private void updateProduct(String url, final int ori_quantity) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Successfully update")) {
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Variable.PRODUCT.setName(name.getText().toString().trim());
                            Variable.PRODUCT.setDescription(description.getText().toString().trim());
                            Variable.PRODUCT.setRemain_quantity( Integer.parseInt(remainQuantity.getText().toString().trim()));
                            Variable.PRODUCT.setOriginal_quantity(ori_quantity);
                            ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, productOrderSellerFragment, "")
                                    .addToBackStack(null)
                                    .commit();

                            //TODO move back to home
                        } else {
                            Toast.makeText(getActivity(), "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("seller_id", String.valueOf(Variable.SELLER.getId()));
                params.put("name", name.getText().toString().trim());
                params.put("originalPrice", originalPrice.getText().toString().trim());
                params.put("sellPrice", sellPrice.getText().toString().trim());
                params.put("openTime", openTime.getText().toString().trim());
                params.put("closeTime", closeTime.getText().toString().trim());
                params.put("remainQuantity",remainQuantity.getText().toString().trim());
                params.put("quantity", ori_quantity + "");
                params.put("image",storageLocation);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}