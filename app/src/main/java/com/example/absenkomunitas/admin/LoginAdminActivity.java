package com.example.absenkomunitas.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absenkomunitas.LoginActivity;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.ModelLoginAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAdminActivity extends AppCompatActivity {

    //firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    //database
    private DocumentReference userRef;

    //model
    private ModelLoginAdmin login;

    private EditText txtEmail, txtPassword;
    private Button btnBack;
    private FloatingActionButton btnLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //model
        login = new ModelLoginAdmin();

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegister = new Intent(LoginAdminActivity.this, LoginActivity.class);
                startActivity(goRegister);
                finish();
            }
        });

        btnLogin = findViewById(R.id.fabLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                loading.startLoadingDialog();

                login.setEmail(txtEmail.getText().toString());
                login.setPassword(txtPassword.getText().toString().trim());

                if (login.getEmail().equals("")){
                    Toast.makeText(LoginAdminActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (login.getPassword().equals("")){
                    Toast.makeText(LoginAdminActivity.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(login.getEmail(), login.getPassword())                                 //sign in
                            .addOnCompleteListener(LoginAdminActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        userRef = db.collection("users").document(user.getUid());
                                        ambilData(userRef, user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginAdminActivity.this, "Login Gagal : Email dan Password salah",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void ambilData(DocumentReference ref, FirebaseUser user) {
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {    //ambil data dari database
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {             //terbaca sebagai admin
                        //go admin activity
                        updateUI(user);
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Error tidak ada data di akun ini", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginAdminActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser currentUser){
        if (currentUser != null){
            Intent goAdmin = new Intent(LoginAdminActivity.this, MainAdminActivity.class);
            startActivity(goAdmin);
            finish();
        } else {
            Intent goLogin = new Intent(LoginAdminActivity.this, LoginActivity.class);
            startActivity(goLogin);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI(null);
    }
}
