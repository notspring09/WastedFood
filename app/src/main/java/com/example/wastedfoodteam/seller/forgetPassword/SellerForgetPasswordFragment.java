package com.example.wastedfoodteam.seller.forgetPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.example.wastedfoodteam.global.Variable;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;

public class SellerForgetPasswordFragment extends Fragment {
    EditText etUserName,etPhone;
    TextInputLayout tilUsername,tilPhone;
    Boolean phoneExist = false;
    Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_forget_password, container, false);
        etUserName = view.findViewById(R.id.etUserName);
        etPhone = view.findViewById(R.id.etPhoneNumber);
        tilUsername = view.findViewById(R.id.textInputUserName);
        tilPhone = view.findViewById(R.id.textInputPhoneNumber);
        btnNext = view.findViewById(R.id.btnForgotPasswordNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhoneAccountExist(etPhone.getText().toString().trim() , etUserName.getText().toString().trim() );
            }
        });
        return view;
    }

    private void checkPhoneAccountExist(final String phone , String account) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkUserNamePhoneExist.php?phone=" + phone +"&username=" + account;
        RequestQueue requestQueue = Volley.newRequestQueue( getActivity().getApplicationContext()   );
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        phoneExist = response.equals("exist");
                        if(validateInformation()){
                            if(phoneExist){
                                tilUsername.setErrorEnabled(false);
                                Bundle bundle=new Bundle();
                                bundle.putString("phone", phone);
                                VerifyPhoneForgotPasswordFragment verifyPhoneForgotPasswordFragment=new VerifyPhoneForgotPasswordFragment();
                                verifyPhoneForgotPasswordFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.flFragmentLayoutAM,verifyPhoneForgotPasswordFragment);
                                fragmentTransaction.commit();
                            }else {
                                tilUsername.setError("Tài khoản hoặc số điện thoại sai");
                                tilUsername.setErrorEnabled(true);
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



    private boolean validateInformation(){
        boolean flag = true;
        if(checkEmptyEditText(etUserName)){
            tilUsername.setErrorEnabled(false);
        }else {
            tilUsername.setError("Tên tài khoản không được để trống");
            tilUsername.setErrorEnabled(true);
            flag = false;
        }
        if(checkEmptyEditText(etPhone) || !Validation.checkPhone(etPhone.getText().toString().trim())){
            tilPhone.setErrorEnabled(false);
        }else {
            tilPhone.setError("Số điện thoại không hợp lệ");
            tilPhone.setErrorEnabled(true);
            flag = false;
        }
        return flag;
    }
}