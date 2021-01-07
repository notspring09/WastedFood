package com.example.wastedfoodteam.seller.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.otp.VerifyPhoneFragment;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;


public class RegisterSellerPhoneFragment extends Fragment {
    EditText etPhone;
    TextInputLayout tilPhone;
    Button btnNext;
    ImageView ivSeller;
    boolean bolPhone = false;
    private String storage_location;
    CameraStorageFunction cameraStorageFunction;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller_phone, container, false);
        etPhone = view.findViewById(R.id.et_seller_register_phone);
        tilPhone = view.findViewById(R.id.textInputPhone);
        ivSeller = view.findViewById(R.id.iv_seller_register_phone);
        btnNext = view.findViewById(R.id.btn_seller_register_phone_next);
        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), ivSeller);

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (checkEmptyEditText(etPhone) && Validation.checkPhone(etPhone.getText().toString())) {
                        checkPhoneExist(etPhone.getText().toString().trim());
                    } else {
                        tilPhone.setError("Số điện thoại không hợp lệ");
                        bolPhone = false;
                    }
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    etPhone.requestFocus();
                    etPhone.clearFocus();
                } catch (Exception e) {
                    Log.e("ERROR", e + "");
                }
            }
        });
        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });
        return view;
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void checkPhoneExist(final String phone) {
        String urlGetData = Variable.IP_ADDRESS + "register/checkPhoneExist.php?phone=" + phone;
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean emailExist;
                        emailExist = response.equals("exist");
                        if (emailExist) {
                            tilPhone.setError("Số điện thoại đã tồn tại");
                            bolPhone = false;
                        } else {
                            tilPhone.setError(null);
                            bolPhone = true;
                            if(cameraStorageFunction.getImage_uri()==null){
                                storage_location = " ";
                            }else {
                                cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                                    @Override
                                    public void onSuccess(String url) {
                                        storage_location = url;
                                        if (storage_location != null) {
                                            Variable.RESISTER_SELLER.setImage(storage_location);
                                        } else {
                                            Variable.RESISTER_SELLER.setImage("");
                                        }
                                    };

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }
                            if (fragmentManager == null) {
                                String phoneNumber = "+" + 84 + etPhone.getText().toString().trim();
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNumber", phoneNumber);
                                VerifyPhoneFragment verifyPhoneFragment = new VerifyPhoneFragment();
                                verifyPhoneFragment.setArguments(bundle);
                                fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.flFragmentLayoutAM, verifyPhoneFragment);
                                fragmentTransaction.commit();
                            }
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
}