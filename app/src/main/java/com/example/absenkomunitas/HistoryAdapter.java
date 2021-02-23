package com.example.absenkomunitas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenkomunitas.model.modelUser;

import java.util.ArrayList;

public class
HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<modelUser> listUser;

    public HistoryAdapter(ArrayList<modelUser> listUser){
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        holder.txtNama.setText(listUser.get(position).getNama());
        holder.txtTimeStamp.setText(listUser.get(position).getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNama, txtTimeStamp;

        public HistoryViewHolder (View view){
            super(view);
            txtNama = view.findViewById(R.id.txtNama);
            txtTimeStamp = view.findViewById(R.id.txtTimeStamp);
        }
    }
}
