package com.example.absenkomunitas.admin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainAdminScanActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //scanner
    private CodeScanner scanner;
    private CodeScannerView scannerView;

    //model
    private ModelUser userModel;

    private Button btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qr_scan);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //model
        userModel = new ModelUser();

        //scanner
        scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this, scannerView);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String code = result.getText();
                        String[] strResult = code.split("\\*");
                        userModel.setUid(strResult[0]);
                        Toast.makeText(MainAdminScanActivity.this, userModel.getUid(), Toast.LENGTH_SHORT).show();
                        db.collection("users").document(userModel.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    db.collection("users").document(userModel.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot data = task.getResult();
                                            userModel.setKomunitas(data.getString("komunitas"));
                                            userModel.setNama(document.getString("nama"));

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("Uid",userModel.getUid());
                                            userData.put("nama", userModel.getNama());
                                            userData.put("timestamp", Calendar.getInstance().getTime());
                                            userData.put("komunitas", userModel.getKomunitas());

                                            db.collection("history").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(MainAdminScanActivity.this, userModel.getNama() + " Berhasil di Absen", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.startPreview();
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMainAdmin = new Intent(MainAdminScanActivity.this, MainAdminActivity.class);
                startActivity(goMainAdmin);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.releaseResources();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goMainAdmin = new Intent(MainAdminScanActivity.this, MainAdminActivity.class);
        startActivity(goMainAdmin);
        finish();
    }

    private void requestForCamera() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainAdminScanActivity.this, "Membutuhkan persetujuan Kamera.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}
