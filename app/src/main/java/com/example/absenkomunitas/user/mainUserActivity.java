package com.example.absenkomunitas.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.absenkomunitas.LoginActivity;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.modelMainUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class mainUserActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //database
    private DocumentReference userRef;

    //model
    private modelMainUserActivity userActivity;

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
        userActivity = new modelMainUserActivity();

        txtNama = findViewById(R.id.txtNama);
        txtRole = findViewById(R.id.txtRole);

        // database
        FirebaseUser user = mAuth.getCurrentUser();
        userRef = db.collection("users").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        userActivity.setNama(document.getString("nama"));
                        userActivity.setRole(document.getString("role"));

                        txtNama.setText(userActivity.getNama());
                        txtRole.setText("Role : " + userActivity.getRole());
                    }
                }
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent goLogin = new Intent(mainUserActivity.this, LoginActivity.class);
                startActivity(goLogin);
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
            Intent goLogin = new Intent(mainUserActivity.this, LoginActivity.class);
            startActivity(goLogin);
            finish();
        }
    }
}