package com.example.wastedfoodteam.seller.forgetPassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wastedfoodteam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneForgotPasswordFragment extends Fragment {

    String phoneNumber;
    private String verificationId;
    private FirebaseAuth mAuth;
    EditText editText;
    TextInputLayout tilCode;
    boolean bolCode = false;
    Button button,btnResend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        button = view.findViewById(R.id.buttonSignIn);
        editText = view.findViewById(R.id.editTextCode);
        tilCode = view.findViewById(R.id.textInputVerifyPhone);
        btnResend = view.findViewById(R.id.btnResend);
        mAuth = FirebaseAuth.getInstance();
        btnResend.setClickable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                editText.clearFocus();
                if(bolCode){
                    String code = editText.getText().toString().trim();
                    verifyCode(code);
                }else {
                    Toast.makeText(getActivity(), "Mã xác nhận không hợp lệ", Toast.LENGTH_LONG).show();
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editText.getText().toString().trim().length() < 6 || editText.getText().toString().trim().equals(null)) {
                        tilCode.setError("Mã xác nhận phải có 6 ký tự");
                        bolCode = false;
                    } else {
                        tilCode.setError(null);
                        bolCode = true;
                    }
                }
            }
        });



        phoneNumber=getArguments().getString("phone");
        sendVerificationCode("+84" + phoneNumber);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResend.setClickable(false);
                sendVerificationCode("+84" + phoneNumber);
            }
        });
        return view;
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallBack
        );
    }

    private void countDownTimer(){
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                btnResend.setText("" + millisUntilFinished/1000);
            }

            public void onFinish() {
                btnResend.setText("Gửi lại mã");
                btnResend.setClickable(true);
            }
        }.start();
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity(), "Mã xác nhận không đúng", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SellerChangePassword sellerChangePasswordFragment = new SellerChangePassword();
                            //open seller detail product fragment
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            Bundle bundle = new Bundle();
                            bundle.putString( "phoneNumberFromVerify" ,phoneNumber);
                            sellerChangePasswordFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flFragmentLayoutAM,sellerChangePasswordFragment);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            countDownTimer();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}