package com.example.absenkomunitas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.admin.LoginAdminActivity;
import com.example.absenkomunitas.user.LoginUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    Button btnAdmin, btnUser, btnRegister;

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser){
        if (currentUser != null){
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            ambilData(userRef);
        }
    }

    public void ambilData(DocumentReference ref) {
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {    //ambil data dari database
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getString("role") == "admin"){
                            Intent goAdmin = new Intent(LoginActivity.this, LoginAdminActivity.class);
                            startActivity(goAdmin);
                            finish();
                        } else if (document.getString("role") == "user"){
                            Intent goUser = new Intent(LoginActivity.this, LoginUserActivity.class);
                            startActivity(goUser);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error tidak ada data di akun ini", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
