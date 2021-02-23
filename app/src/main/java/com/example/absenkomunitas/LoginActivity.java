package com.example.absenkomunitas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.admin.LoginAdminActivity;
import com.example.absenkomunitas.user.LoginUserActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnAdmin, btnUser

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goAdmin = new Intent(LoginActivity.this, LoginAdminActivity.class);
                startActivity(goAdmin);
                finish();
            }
        });

        btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goUser = new Intent(LoginActivity.this, LoginUserActivity.class);
                startActivity(goUser);
                finish();
            }
        });

    }
}
