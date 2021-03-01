package com.example.absenkomunitas.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.absenkomunitas.LoginActivity;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.ModelAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainAdminActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //database
    private DocumentReference userRef;

    //model
    private ModelAdmin adminModel;

    private TextView txtNama, txtRole;
    private CardView btnScan, btnHistory;
    private Button btnLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //model
        adminModel = new ModelAdmin();

        txtNama = findViewById(R.id.txtNama);
        txtRole = findViewById(R.id.txtRole);

        //database
        FirebaseUser user = mAuth.getCurrentUser();
        adminModel.setUid(user.getUid());

        userRef = db.collection("users").document(adminModel.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    adminModel.setNama(document.getString("nama"));
                    adminModel.setRole(document.getString("role"));

                    txtNama.setText(adminModel.getNama());
                    txtRole.setText("Role : " + adminModel.getRole());
                }
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
            }
        });

        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goScan = new Intent(MainAdminActivity.this, MainAdminScanActivity.class);
                startActivity(goScan);
                finish();
            }
        });

        btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHistory = new Intent(MainAdminActivity.this, MainAdminHistoryActivity.class);
                startActivity(goHistory);
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
            Intent goLogin = new Intent(MainAdminActivity.this, LoginActivity.class);
            startActivity(goLogin);
            finish();
        }
    }
}
