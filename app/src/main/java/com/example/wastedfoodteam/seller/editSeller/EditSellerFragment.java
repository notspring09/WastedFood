package com.example.wastedfoodteam.seller.editSeller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
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
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.home.SellerHomeActivity;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class EditSellerFragment extends Fragment {

    private int id;
    //ui view
    EditText editText_editSeller_name;
    EditText editText_editSeller_address;
    EditText editText_editSeller_description;
    EditText editText_editSeller_email;
    EditText editText_editSeller_phoneNumber;
    Button btn_editSeller_edit;
    TextInputLayout tilName,tilAddress,tilDescription;
    boolean bolName,bolAddress,bolDescription;
    ImageView iv_editSeller_avatar;

    //link to php file
    String urlGetData = "";

    //string get from edit text
    String string_editSeller_name;
    String string_editSeller_address;
    String string_editSeller_description;
    LoadingDialog loadingDialog;

    //camera and upload picture handle
    CameraStorageFunction cameraStorageFunction;
    private String storage_location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_edit, container, false);

        //init ui view
        editText_editSeller_name = view.findViewById(R.id.editText_editSeller_name);
        editText_editSeller_address = view.findViewById(R.id.editText_editSeller_address);
        editText_editSeller_description = view.findViewById(R.id.editText_editSeller_description);
        editText_editSeller_email = view.findViewById(R.id.editText_editSeller_email);
        editText_editSeller_phoneNumber = view.findViewById(R.id.editText_editSeller_phoneNumber);
        tilAddress = view.findViewById(R.id.textInputRestaurantAddress);
        tilDescription = view.findViewById(R.id.textInputRestaurantDescription);
        tilName = view.findViewById(R.id.textInputRestaurantName);
        bolAddress = false;
        bolDescription = false;
        bolName = false;
        btn_editSeller_edit = view.findViewById(R.id.btn_editSeller_edit1);
        iv_editSeller_avatar = view.findViewById(R.id.iv_editSeller_avatar);
        id = Variable.SELLER.getId();
        editText_editSeller_email.setEnabled(false);
        editText_editSeller_phoneNumber.setEnabled(false);
        loadingDialog = new LoadingDialog(getActivity());
        getSeller(id);
        //string get from edit text
        string_editSeller_name = editText_editSeller_name.getText().toString().trim();
        string_editSeller_address = editText_editSeller_address.getText().toString().trim();
        string_editSeller_description = editText_editSeller_description.getText().toString().trim();
        editText_editSeller_email.setText(Variable.SELLER.getEmail());
        editText_editSeller_phoneNumber.setText(Variable.SELLER.getPhone());
        cameraStorageFunction = new CameraStorageFunction(requireActivity(), getContext(), iv_editSeller_avatar);

        //for multiline EditText
        //scroll for EditText
        editText_editSeller_description.setScroller(new Scroller(requireActivity().getApplicationContext()));
        editText_editSeller_description.setVerticalScrollBarEnabled(true);



        //Edit Text Line
        editText_editSeller_description.setMinLines(2);
        editText_editSeller_description.setMaxLines(5);

        editText_editSeller_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    bolAddress = checkAndAlertEmptyEditText( editText_editSeller_address, tilAddress, "Địa chỉ nhà hàng không được quá 100 kí tự và không được để trống");
                }
            }
        });

        editText_editSeller_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    bolName = checkAndAlertEmptyEditText( editText_editSeller_name, tilName, "Tên nhà hàng không được quá 100 kí tự và không được để trống");
                }
            }
        });

        editText_editSeller_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editText_editSeller_description.getText().toString().trim().length() <= 300) {
                        bolDescription = true;
                        tilDescription.setError(null);
                    } else {
                        tilDescription.setError("Thông tin nhà hàng không được vượt quá 300 ký tự");
                        bolDescription = false;
                    }
                }
            }
        });

        //click avatar handle
        iv_editSeller_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });

        //click edit button handle
        btn_editSeller_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                editText_editSeller_address.requestFocus();
                editText_editSeller_description.requestFocus();
                editText_editSeller_name.requestFocus();
                editText_editSeller_name.clearFocus();
                if(bolAddress && bolDescription && bolName){
                    inputData();
                }else{
                    loadingDialog.dismissDialog();
                }
            }
        });
        return view;
    }

    private void getSeller(int id) {
        urlGetData = Variable.IP_ADDRESS + "getSellerById.php?id=" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonSellers = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Seller seller = gson.fromJson(jsonSellers.getString(0), Seller.class);
                            editText_editSeller_name.setText(seller.getName());
                            editText_editSeller_address.setText(seller.getAddress());
                            editText_editSeller_description.setText(seller.getDescription());
                            CommonFunction.setImageViewSrc(getActivity(), seller.getImage(), iv_editSeller_avatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(getSellerRequestString);
    }

    private Boolean checkAndAlertEmptyEditText(EditText editText , TextInputLayout textInputLayout , String errorMessage){

            String string = editText.getText().toString().trim();
            if (CommonFunction.checkEmptyEditText(editText) && string.length() <= 100) {
                textInputLayout.setError(null);
                return true;

            } else {
                textInputLayout.setError(errorMessage);
                return false;
            }

    }


    private void inputData() {
        if (cameraStorageFunction.getImage_uri() != null)
            cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                @Override
                public void onSuccess(String url) {
                    storage_location = url;
                    String urlGetData = Variable.IP_ADDRESS + "seller/sellerEdit.php";
                    updateSeller(urlGetData);
                };

                @Override
                public void onError() {

                }
            });
        else {
            String urlGetData = Variable.IP_ADDRESS + "seller/sellerEdit.php";
            updateSeller(urlGetData);
        }
    }


    //end of for camera handle

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //update seller data
    private void updateSeller(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Variable.SELLER.setName(editText_editSeller_name.getText().toString().trim());
                            Variable.SELLER.setAddress(editText_editSeller_address.getText().toString().trim());
                            Variable.SELLER.setDescription(editText_editSeller_description.getText().toString().trim());
                            if (storage_location != null)
                                Variable.SELLER.setImage(storage_location);
                            startActivity(new Intent(getActivity(), SellerHomeActivity.class));
                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(getActivity(), "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
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
                params.put("id", String.valueOf(id));
                params.put("username", editText_editSeller_name.getText().toString().trim());
                params.put("address", editText_editSeller_address.getText().toString().trim());
                params.put("description", editText_editSeller_description.getText().toString().trim());
                if(storage_location!=null) {
                    params.put("image", storage_location);
                }else {
                    params.put("image", " ");
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}