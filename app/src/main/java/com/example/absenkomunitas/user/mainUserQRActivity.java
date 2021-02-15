package com.example.absenkomunitas.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.modelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class mainUserQRActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private Button btnBack;
    private ImageView QRCode;

    private modelUser userModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_qr_generator);
        mAuth = FirebaseAuth.getInstance();

        userModel = new modelUser();

        QRCode = findViewById(R.id.QRCode);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goUserMainActivity = new Intent(mainUserQRActivity.this, mainUserActivity.class);
                startActivity(goUserMainActivity);
                finish();
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        userModel.setUid(user.getUid());

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(user.getUid(), BarcodeFormat.QR_CODE, 200,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            QRCode.setImageBitmap(bitmap);
            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );
        } catch (WriterException e){
            e.printStackTrace();
        }
    }
}
