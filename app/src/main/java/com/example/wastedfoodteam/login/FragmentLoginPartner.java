package com.example.wastedfoodteam.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.forgetPassword.SellerForgetPasswordFragment;
import com.example.wastedfoodteam.seller.home.SellerHomeActivity;
import com.example.wastedfoodteam.seller.register.RegisterSellerFragment;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import static com.example.wastedfoodteam.utils.Encode.md5;

public class FragmentLoginPartner extends Fragment {
    Button btnSignIn, btnBuyerOption;
    EditText etSDT, etPass;
    String urlGetData = "";
    TextView tvRegisterAccount, tvForgotPassword;
    LoadingDialog loadingDialog;
    String password = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_partner, container, false);
        etSDT = view.findViewById(R.id.etSdtPartnerFLP);
        etPass = view.findViewById(R.id.etPassPartnerFLP);
        btnSignIn = view.findViewById(R.id.btnSignInPartnerFLP);
        btnBuyerOption = view.findViewById(R.id.btnBuyerOptionFLP);
        tvRegisterAccount = view.findViewById(R.id.tvRegisterSeller);
        loadingDialog = new LoadingDialog(getActivity());
        tvForgotPassword = view.findViewById(R.id.tvForgotPassPartnerFLP);
        Intent intent = new Intent(getActivity(), SellerHomeActivity.class);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                password = md5(etPass.getText().toString());
                urlGetData = Variable.IP_ADDRESS + "login/sellerLogin.php?username=" + etSDT.getText().toString().trim() + "&password=" + md5(etPass.getText().toString().trim());
                getSellerLoginStatus(urlGetData);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerForgetPasswordFragment sellerForgetPasswordFragment = new SellerForgetPasswordFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragmentLayoutAM, sellerForgetPasswordFragment);
                fragmentTransaction.commit();
            }
        });

        btnBuyerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentLoginBuyer();
            }
        });
        tvRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterSellerFragment registerSellerFragment = new RegisterSellerFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragmentLayoutAM, registerSellerFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    /**
     * get data from mySql
     *
     * @param url
     */
    private void getSellerLoginStatus(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ("account is locked".equals(response)) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getActivity(), "Tài khoản bạn đã bị khóa", Toast.LENGTH_LONG).show();
                } else  if("account is not active".equals(response)){
                    loadingDialog.dismissDialog();
                    Toast.makeText(getActivity(), "Tài khoản của bạn chưa được kích hoạt nếu có thắc mắc vui lòng liên hệ với chúng tôi " , Toast.LENGTH_LONG).show();
                }
                else if ("not exist account".equals(response)) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getActivity(), "Tên đăng nhập hoặc mật khẩu không đúng" , Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray object = new JSONArray(response);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Seller seller = gson.fromJson(object.getString(0), Seller.class);
                        final Intent intent = new Intent(getActivity(), SellerHomeActivity.class);
                        Variable.SELLER = seller;
                        loadingDialog.dismissDialog();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(seller.getEmail(), seller.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Variable.fireBaseUID = authResult.getUser().getUid();
                                Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        });
                    } catch (Exception e) {
                        loadingDialog.dismissDialog();
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                Toast.makeText(getActivity(), "Lỗi kết nối", Toast.LENGTH_LONG).show();
                Log.d("MK ", md5(etPass.getText().toString()));
            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * move to fragment buyer
     */
    public void addFragmentLoginBuyer() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentLoginBuyer fragmentLoginBuyer = new FragmentLoginBuyer();
        fragmentTransaction.replace(R.id.flFragmentLayoutAM, fragmentLoginBuyer);
        fragmentTransaction.commit();
    }

    private void openSellerHome() {
        Intent intent = new Intent(getActivity(), SellerHomeActivity.class);//TODO change to seller activity
        requireActivity().finish();
        startActivity(intent);
    }
}
