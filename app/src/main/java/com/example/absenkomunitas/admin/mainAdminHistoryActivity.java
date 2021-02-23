package com.example.absenkomunitas.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenkomunitas.HistoryAdapter;
import com.example.absenkomunitas.R;
import com.example.absenkomunitas.model.modelUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.grpc.internal.LogExceptionRunnable;

public class mainAdminHistoryActivity extends AppCompatActivity {

    private static final String TAG = null;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<modelUser> userArrayList;

//    private FirestoreRecyclerAdapter adapter;

    //model
    modelUser userModel;

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mainAdminHistoryActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historyAdapter);

//        //query
//        Query query = db.collection("history");
//
//        //recylerOptions
//        FirestoreRecyclerOptions<modelUser> options = new FirestoreRecyclerOptions.Builder<modelUser>()
//                .setQuery(query, modelUser.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<modelUser, userViewHolder>(options) {
//            @NonNull
//            @Override
//            public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
//                return new userViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull userViewHolder holder, int position, @NonNull modelUser model) {
//                holder.txtNama.setText(model.getNama());
//                holder.txtTimeStamp.setText(model.getTimeStamp());
//            }
//        };
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMainAdmin = new Intent(mainAdminHistoryActivity.this, mainAdminActivity.class);
                startActivity(goMainAdmin);
                finish();
            }
        });

    }

    public void getData() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("h:mm a dd-MM-yyyy");
        userArrayList = new ArrayList<>();
        userModel = new modelUser();
        db.collection("history").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String time = timeStampFormat.format(document.getDate("timestamp"));
                        userModel.setNama(document.getString("nama"));
                        userModel.setTimeStamp(time);
                        userArrayList.add(new modelUser(document.getString("nama"), time));
                        Log.e(TAG, "onComplete: " + userModel.getNama() + " " + userModel.getTimeStamp());
                        historyAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(mainAdminHistoryActivity.this, "Tidak ada History", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goMainAdmin = new Intent(mainAdminHistoryActivity.this, mainAdminActivity.class);
        ;
        startActivity(goMainAdmin);
        finish();
    }

//    private class userViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView txtNama, txtTimeStamp;
//
//        public userViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            txtNama = itemView.findViewById(R.id.txtNama);
//            txtTimeStamp = itemView.findViewById(R.id.txtTimeStamp);
//
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
}
