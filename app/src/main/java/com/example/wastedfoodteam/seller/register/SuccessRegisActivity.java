package com.example.wastedfoodteam.seller.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;

public class SuccessRegisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_regis);
        Button btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessRegisActivity.this, LoginActivity.class);
                finishAndRemoveTask();
                startActivity(intent);
            }
        });
    }
}