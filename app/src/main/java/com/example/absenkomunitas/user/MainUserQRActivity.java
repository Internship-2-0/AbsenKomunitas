package com.example.absenkomunitas.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainUserQRActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private QRGEncoder qrgEncoder;

    private Button btnBack;
    private ImageView QRCode;

    private ModelUser userModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_qr_generator);
        mAuth = FirebaseAuth.getInstance();

        userModel = new ModelUser();

        QRCode = findViewById(R.id.QRCode);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goUserMainActivity = new Intent(MainUserQRActivity.this, MainUserActivity.class);
                startActivity(goUserMainActivity);
                finish();
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        userModel.setUid(user.getUid());

        qrgEncoder = new QRGEncoder(userModel.getUid() + "*" + Calendar.getInstance().getTime(), null, QRGContents.Type.TEXT, 200);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            QRCode.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goMainUser = new Intent(MainUserQRActivity.this, MainUserActivity.class);
        startActivity(goMainUser);
        finish();
    }
}
