package com.daus.softwarequiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreboardSADAdapter extends RecyclerView.Adapter<ScoreboardSADAdapter.ViewHolder>{
    private List<User> listData;

    public ScoreboardSADAdapter(List<User> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.score_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User scoreboard=listData.get(position);
        holder.txtid.setText(String.valueOf(position+1));
        holder.txtname.setText(scoreboard.getUserName());
        holder.txtpoints.setText(String.valueOf(scoreboard.getSadScore()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtid, txtname,txtpoints;
        public ViewHolder(View itemView) {
            super(itemView);
            txtid=itemView.findViewById(R.id.id_txt);
            txtname=itemView.findViewById(R.id.name_txt);
            txtpoints=itemView.findViewById(R.id.score_txt);
        }
    }
}