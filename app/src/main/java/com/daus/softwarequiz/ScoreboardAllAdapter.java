package com.daus.softwarequiz;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ScoreboardAllAdapter extends RecyclerView.Adapter<ScoreboardAllAdapter.ViewHolder>{
    private List<User> listData;

    public ScoreboardAllAdapter(List<User> listData) {
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
        holder.txtpoints.setText(String.valueOf(scoreboard.getTotalScore()));
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