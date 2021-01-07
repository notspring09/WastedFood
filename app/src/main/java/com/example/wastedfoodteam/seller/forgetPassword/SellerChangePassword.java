package com.example.wastedfoodteam.seller.forgetPassword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.login.FragmentLoginPartner;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.home.SellerHomeActivity;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.home.SellerHomeFragment;
import com.facebook.login.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.example.wastedfoodteam.utils.Encode.md5;


public class SellerChangePassword extends Fragment {

    EditText etPassword,etConfirmPassword;
    String strPassword,strConfirmPassword;
    TextInputLayout tilPassword,tilConfirmPass;
    String phone;
    Button btnConfirm;
    LoadingDialog loadingDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_change_password, container, false);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        tilPassword = view.findViewById(R.id.textInputPasswordNew);
        tilConfirmPass = view.findViewById(R.id.textInputConfirmPassword);
        btnConfirm = view.findViewById(R.id.btnChangePassword);
        loadingDialog = new LoadingDialog(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             phone = bundle.getString("phoneNumberFromVerify", ""); // Key, default value
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePassword()){
                    Log.i("Phone",phone);
                    loadingDialog.startLoadingDialog();
                    getSellerInformationByPhone(phone);

                }
            }
        });
        return view;
    }

    private void getSellerInformationByPhone(final String phone){
        String url = Variable.IP_ADDRESS + "seller/getSellerInformationByPhone.php?phone="+ phone;
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Seller seller = gson.fromJson(object.getString(0), Seller.class);
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(seller.getEmail(), seller.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            authResult.getUser().updatePassword(md5(strPassword)).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("firebase", "User password failure.");
                                    Toast.makeText(getActivity(),"Cập nhật không thành công, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateSellerPasswordByPhone(phone);
                                                Log.d("firebase", "User password updated.");
                                            }
                                        }

                                    });
                        }
                    });

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nối" , Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    //update seller account password
    private void updateSellerPasswordByPhone(final String phone){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String urlGetData = Variable.IP_ADDRESS + "seller/updatePasswordByUsername.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            loadingDialog.dismissDialog();
                            Toast.makeText(getActivity(),"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            FragmentLoginPartner fragmentLoginPartner = new FragmentLoginPartner();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flFragmentLayoutAM, fragmentLoginPartner);
                            fragmentTransaction.commit();
                        }else{
                            loadingDialog.dismissDialog();
                            Toast.makeText(getActivity(),"Lỗi cập nhật, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(getActivity(),"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("phone", phone );
                params.put("password",md5(etPassword.getText().toString().trim()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public boolean validatePassword(){
        boolean flag = true;
        strPassword = etPassword.getText().toString().trim();
        strConfirmPassword = etConfirmPassword.getText().toString().trim();
        if(!strPassword.equals(strConfirmPassword)){
            tilConfirmPass.setError("Mật khẩu xác nhận với mật khẩu không giống nhau");
            flag = false;
        }else {
            tilConfirmPass.setError(null);
        }
        if(!Validation.checkPassword(strPassword)){
            tilPassword.setError("Mật khẩu phải bao gồm ít nhất 1 số và 1 chữ cái trong khoảng từ 8 đến 16 kí tự, ví dụ: abc12345");
            flag = false;
        }else{
            tilPassword.setError(null);
        }
        return flag;
    }
}