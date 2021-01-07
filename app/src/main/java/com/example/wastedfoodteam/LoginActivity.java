package com.example.wastedfoodteam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.login.FragmentLoginBuyer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //loadingStartApp = new LoadingStartApp(LoginActivity.this);
        //loadingStartApp.startLoadingDialog();
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingStartApp.dismissDialog();
            }
        },3000);*/
        //Variable.uri =  Uri.parse("android.resource://"+ getApplicationContext().getPackageName()+"/drawable/bell.png");
        Variable.uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.button_home)
                + '/' + getResources().getResourceTypeName(R.drawable.button_home) + '/' + getResources().getResourceEntryName(R.drawable.button_home));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragment for login
        FragmentLoginBuyer fragment = new FragmentLoginBuyer();
        fragmentTransaction.add(R.id.flFragmentLayoutAM, fragment, "login_buyer");
        fragmentTransaction.commit();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        FragmentLoginBuyer loginBuyer = (FragmentLoginBuyer) getSupportFragmentManager().findFragmentByTag("login_buyer");
        if (loginBuyer != null && loginBuyer.isVisible()) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else
            intent = new Intent(this, LoginActivity.class);
        finishAndRemoveTask();
        startActivity(intent);
    }
}