package com.example.absenkomunitas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.admin.LoginAdminActivity;
import com.example.absenkomunitas.admin.MainAdminActivity;
import com.example.absenkomunitas.user.LoginUserActivity;
import com.example.absenkomunitas.user.MainUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.DocumentMask;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button btnAdmin, btnUser;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

    void showProgress() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        showProgress();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getString("role").equals(null)) {
                        Toast.makeText(LoginActivity.this, "Telah Login sebagai Admin", Toast.LENGTH_SHORT).show();
                        Intent goAdmin = new Intent(LoginActivity.this, MainAdminActivity.class);
                        startActivity(goAdmin);
                        finish();
                    } else if (document.getString("role").equals("user")) {
                        Toast.makeText(LoginActivity.this, "Telah Login sebagai User", Toast.LENGTH_SHORT).show();
                        Intent goUser = new Intent(LoginActivity.this, MainUserActivity.class);
                        startActivity(goUser);
                        finish();
                    } else if (document.getString("role").equals("admin")){
                        dismissProgress();
                    }
                }
            });
        } else {
            dismissProgress();
        }
    }
}
