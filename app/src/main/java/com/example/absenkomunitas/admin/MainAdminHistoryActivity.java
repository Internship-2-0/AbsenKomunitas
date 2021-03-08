package com.example.absenkomunitas.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainAdminHistoryActivity extends AppCompatActivity {

    private static final String TAG = null;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<ModelUser> userArrayList;

    //model
    ModelUser userModel;

    private Button btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getData();

        recyclerView = findViewById(R.id.recycler);
        historyAdapter = new HistoryAdapter(userArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainAdminHistoryActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyAdapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMainAdmin = new Intent(MainAdminHistoryActivity.this, MainAdminActivity.class);
                startActivity(goMainAdmin);
                finish();
            }
        });

    }

    public void getData() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("h:mm a dd-MM-yyyy");
        userArrayList = new ArrayList<>();
        userModel = new ModelUser();
        db.collection("history").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String time = timeStampFormat.format(document.getDate("timestamp"));
                        userModel.setNama(document.getString("nama"));
                        userModel.setKomunitas(document.getString("komunitas"));
                        userModel.setTimeStamp(time);
                        userArrayList.add(new ModelUser(userModel.getNama(), userModel.getTimeStamp(), userModel.getKomunitas()));
                        Log.e(TAG, "onComplete: " + userModel.getNama() + " " + userModel.getTimeStamp());
                        historyAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainAdminHistoryActivity.this, "Tidak ada History", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goMainAdmin = new Intent(MainAdminHistoryActivity.this, MainAdminActivity.class);
        ;
        startActivity(goMainAdmin);
        finish();
    }
}
