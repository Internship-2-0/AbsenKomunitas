package com.example.absenkomunitas.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.absenkomunitas.admin.LoginAdminActivity;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.modelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class mainUserActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //database
    private DocumentReference userRef;

    //model
    private modelUser userModel;

    private TextView txtNama, txtRole;
    private CardView btnQR;
    private Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //model
        userModel = new modelUser();

        txtNama = findViewById(R.id.txtNama);
        txtRole = findViewById(R.id.txtRole);

        // database
        FirebaseUser user = mAuth.getCurrentUser();
        userModel.setUid(user.getUid());

        userRef = db.collection("users").document(userModel.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    userModel.setNama(document.getString("nama"));
                    userModel.setRole(document.getString("role"));

                    txtNama.setText(userModel.getNama());
                    txtRole.setText("Role : " + userModel.getRole());
                }
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent goLogin = new Intent(mainUserActivity.this, LoginAdminActivity.class);
                startActivity(goLogin);
                finish();
            }
        });

        btnQR = findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goQRCode = new Intent(mainUserActivity.this, mainUserQRActivity.class);
                startActivity(goQRCode);
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

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null){
            Intent goLogin = new Intent(mainUserActivity.this, LoginAdminActivity.class);
            startActivity(goLogin);
            finish();
        }
    }
}