package com.example.wastedfoodteam.seller.product;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.home.SellerHomeFragment;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment {

    private int seller_id;
    private String storage_location;

    private TextInputLayout tilProductName,tilDescription,tilOriginalPrice,tilSalePrice,tilQuantity,tilOpenTime,tilCloseTime;
    private EditText etProductName,
            etOriginalPrice, etSellPrice,
            etDescription, etQuantity;
    private TextView etCloseTime,etOpenTime;
    private TextView tvCountProductName,TvCountProductDescription;
    private boolean bolProductName,bolProductDescription,bolOriginalPrice,bolSalePrice,bolQuantity,bolOpenTime,bolCloseTime;
    //for time picker
    private int mHour, mMinute;

    CameraStorageFunction cameraStorageFunction;
    LoadingDialog loadingDialog;

    final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get seller id from seller home activity
        seller_id = Variable.SELLER.getId();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_add_product, container, false);

        loadingDialog = new LoadingDialog(getActivity());
        //init ui view
        //ui view
        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        etProductName = view.findViewById(R.id.etProductName);
        etOriginalPrice = view.findViewById(R.id.etOriginalPrice);
        etSellPrice = view.findViewById(R.id.etSellPrice);
        etOpenTime = view.findViewById(R.id.etOpenTime);
        etCloseTime = view.findViewById(R.id.etCloseTime);
        etDescription = view.findViewById(R.id.etDescription);
        etQuantity = view.findViewById(R.id.etQuantity);
        tvCountProductName = view.findViewById(R.id.tvCountProductName);
        TvCountProductDescription = view.findViewById(R.id.TvCountProductDescription);
        tilOpenTime = view.findViewById(R.id.textInputOpenTime);
        tilCloseTime = view.findViewById(R.id.textInputCloseTime);
        tilDescription = view.findViewById(R.id.textInputProductDescription);
        tilProductName = view.findViewById(R.id.textInputProductName);
        tilOriginalPrice = view.findViewById(R.id.textInputOriginalPrice);
        tilSalePrice = view.findViewById(R.id.textInputSellPrice);
        tilQuantity = view.findViewById(R.id.textInputQuantity);
        final Button btnAddProductAdd = view.findViewById(R.id.btnAddProductAdd);
        setUp();
        etSellPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String sellPrice = etSellPrice.getText().toString().trim();
                    String originalPrice = etOriginalPrice.getText().toString().trim();
                    if (CommonFunction.checkEmptyEditText(etSellPrice) && sellPrice.length() <= 100) {
                        if (Integer.parseInt(sellPrice) > Integer.parseInt(originalPrice)) {
                            tilSalePrice.setError("Giá bán không được lớn hơn giá gốc");
                            bolSalePrice = false;
                        } else {
                            tilSalePrice.setError(null);
                            bolSalePrice = true;
                        }
                    } else {
                        tilSalePrice.setError("Giá bán phải có ít nhất 1 kí tự và không được quá 100 kí tự");
                        bolSalePrice = false;
                    }
                }
            }
        });

        etOriginalPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(checkAndAlertEmptyEditText(etOriginalPrice, tilOriginalPrice ,"Giá gốc phải có ít nhất 1 kí tự và không được quá 100 kí tự" )){
                        if(Double.parseDouble(etOriginalPrice.getText().toString()) == 0){
                            bolOriginalPrice = false;
                            tilOriginalPrice.setError("Giá gốc không được bằng không");
                        }else{
                            tilOriginalPrice.setError(null);
                            bolOriginalPrice = true;
                        }
                    }else{
                        bolOriginalPrice = false;
                    }
                }

            }
        });


        etProductName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    bolProductName = checkAndAlertEmptyEditText(etProductName, tilProductName ,"Tên sản phẩm phải có ít nhất 1 kí tự và không được quá 100 kí tự" );
                }
            }
        });

        etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                     if( checkAndAlertEmptyEditText(etQuantity, tilQuantity ,"Số lượng phải có ít nhất 1 kí tự và không được quá 100 kí tự" )){
                         if(Integer.parseInt(etQuantity.getText().toString().trim() ) == 0){
                             bolQuantity = false;
                             tilQuantity.setError("Số lượng không được bằng không");
                         }else{
                             tilQuantity.setError(null);
                             bolQuantity = true;
                         }
                     }
                }
            }
        });

        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String string = etDescription.getText().toString().trim();
                if (string.length() <= 300) {
                    tilDescription.setError(null);
                    bolProductDescription = true;

                } else {
                    tilDescription.setError("Thông tin chi tiết sản phẩm không được quá 300 ký tự");
                    bolProductDescription = false;
                }
            }
        });


        etProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCountProductName.setText(s.length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TvCountProductDescription.setText(s.length()+"/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Date picker handle
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), ivProduct);

        //count time for validate
        final int[] countEtOpenTime = new int[1];
        final int[] countEtCloseTime = new int[1];
        etOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar datetime = Calendar.getInstance();
                                Calendar c = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                if(datetime.getTimeInMillis() > c.getTimeInMillis()){
                                    countEtOpenTime[0] = 60*hourOfDay + minute;
                                    if(!etCloseTime.getText().toString().equals("")){
                                        if(countEtOpenTime[0] > countEtCloseTime[0]){
                                            etOpenTime.setText(etCloseTime.getText().toString());
                                        }else {
                                            etOpenTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                        }
                                    }else
                                        etOpenTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                }else {
                                    etOpenTime.setText(String.format("%02d:%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) , Calendar.getInstance().get(Calendar.MINUTE)));
                                }

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
//                showImagePickDialog();
            }
        });


        etCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        countEtCloseTime[0] = 60*hourOfDay + minute;
                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if(datetime.getTimeInMillis() > c.getTimeInMillis()){
                            if(!etOpenTime.getText().toString().equals("")){
                                if(countEtOpenTime[0] < countEtCloseTime[0]){
                                    etCloseTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                }else {
                                    etCloseTime.setText(etOpenTime.getText().toString());
                                }
                            }else
                                etCloseTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }else{
                            etCloseTime.setText(String.format("%02d:%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) , Calendar.getInstance().get(Calendar.MINUTE) ));
                        }


                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        btnAddProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCheck();
                final String urlGetData = Variable.IP_ADDRESS + Variable.ADD_PRODUCT_SELLER;
                if(bolCloseTime && bolOpenTime && bolOriginalPrice && bolProductDescription && bolProductName && bolQuantity && bolSalePrice && bolCloseTime && bolOpenTime ) {
                    loadingDialog.startLoadingDialog();
                    if(cameraStorageFunction.getImage_uri()==null){
                        storage_location = "";
                        addProduct(urlGetData);
                    }else {
                        cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                            @Override
                            public void onSuccess(String url) {
                                storage_location = url;
                                addProduct(urlGetData);
                            }

                            @Override
                            public void onError() {
                                loadingDialog.dismissDialog();
                            }
                        });
                    }
                }
            }
        });
        return view;
    }



    private boolean checkAndAlertEmptyEditText( EditText editText , TextInputLayout textInputLayout  , String errorMessage){
            String string = editText.getText().toString().trim();
            if (CommonFunction.checkEmptyEditText(editText) && string.length() <= 100) {
                textInputLayout.setError(null);
                return true;

            } else {
                textInputLayout.setError(errorMessage);
                return false;
            }
    }

    private void setUp(){
        bolCloseTime = false;
        bolOpenTime = false;
        bolOriginalPrice = false;
        bolProductDescription = false;
        bolSalePrice = false;
        bolProductName = false;
        bolQuantity = false;
    }

    private void activeCheck(){
        etQuantity.requestFocus();
        etDescription.requestFocus();
        etProductName.requestFocus();
        etSellPrice.requestFocus();
        etOriginalPrice.requestFocus();
        etOriginalPrice.clearFocus();
        if(etCloseTime.getText().toString().trim()!=""){
            tilCloseTime.setError(null);
            bolCloseTime = true;
        }else {
            tilCloseTime.setError("Thời gian đóng cửa không được để trống");
            bolCloseTime = false;
        }
        if(etOpenTime.getText().toString().trim()!=""){
            tilOpenTime.setError(null);
            bolOpenTime = true;
        }else {
            tilOpenTime.setError("Thời gian bán không được để trống");
            bolOpenTime = false;
        }
    }


    private void addProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            loadingDialog.dismissDialog();
                            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                            //open seller detail product fragment
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, sellerHomeFragment, "")
                                    .addToBackStack(null)
                                    .commit();
                            Toast.makeText(getActivity(), R.string.sucess_upload, Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(getActivity(), R.string.error_upload, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(getActivity(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", String.valueOf(seller_id));
                params.put("name", etProductName.getText().toString());
                params.put("image", storage_location);
                params.put("start_time", CommonFunction.getCurrentDate() + " " + etOpenTime.getText().toString());
                params.put("end_time", CommonFunction.getCurrentDate() + " " + etCloseTime.getText().toString());
                params.put("original_price", etOriginalPrice.getText().toString());
                params.put("sell_price", etSellPrice.getText().toString());
                params.put("original_quantity", etQuantity.getText().toString());
                params.put("remain_quantity", etQuantity.getText().toString());
                params.put("description", etDescription.getText().toString());
                params.put("status", Product.ProductStatus.SELLING + "");
                params.put("sell_date", CommonFunction.getCurrentDate() + " " + etOpenTime.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}