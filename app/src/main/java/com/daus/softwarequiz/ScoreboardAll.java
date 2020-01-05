package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardAll extends AppCompatActivity {

    String scoreboardType;
    private ImageView closeBtn;
    private List<User> listData;
    private RecyclerView rv;
    private ScoreboardAllAdapter allAdapter;
    private ScoreboardWebAdapter webAdapter;
    private ScoreboardOOPAdapter oopAdapter;
    private ScoreboardDSAdapter dsAdapter;
    private ScoreboardSADAdapter sadAdapter;
    private DatabaseReference dbRef;
    private Query selectedQuery;
    private TextView scoreboardTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard_all);

        scoreboardTitle = findViewById(R.id.scoreboard_title);
        closeBtn = findViewById(R.id.scoreboard_close_button);
        rv=findViewById(R.id.sb_all_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData=new ArrayList<>();

        dbRef= FirebaseDatabase.getInstance().getReference("users-scoreboard");
        dbRef.keepSynced(true);

        scoreboardType = getIntent().getExtras().getString("scoreboard_type", "defaultKey");

        if(scoreboardType.matches("all")){
            scoreboardTitle.setText("Scoreboard:\nAll quizzes");
            selectedQuery = dbRef.orderByChild("totalScore");
        }else if(scoreboardType.matches("web")){
            scoreboardTitle.setText("Scoreboard:\nWeb Eng.");
            selectedQuery = dbRef.orderByChild("webScore");
        }else if(scoreboardType.matches("oop")){
            scoreboardTitle.setText("Scoreboard:\nO.O.P.");
            selectedQuery = dbRef.orderByChild("oopScore");
        }else if(scoreboardType.matches("ds")){
            scoreboardTitle.setText("Scoreboard:\nData Struct.");
            selectedQuery = dbRef.orderByChild("dataStrucScore");
        }else if(scoreboardType.matches("sad")){
            scoreboardTitle.setText("Scoreboard:\nS.A.D.");
            selectedQuery = dbRef.orderByChild("sadScore");
        }

        selectedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null && dataSnapshot.getValue()!=null){
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                        Log.v("SCOREBOARD",""+ childDataSnapshot.getKey()); //displays the key for the node
//                        Log.v("SCOREBOARD",""+ childDataSnapshot.getValue());   //gives the value for given keyname
                        User user = childDataSnapshot.getValue(User.class);
                        listData.add(user);
                    }
                    Collections.reverse(listData);//Solves firebase's issue of being unable to orderBy descending order
                    if(scoreboardType.matches("all")) {
                        allAdapter = new ScoreboardAllAdapter(listData);
                        rv.setAdapter(allAdapter);
                    }else if(scoreboardType.matches("web")) {
                        webAdapter = new ScoreboardWebAdapter(listData);
                        rv.setAdapter(webAdapter);
                    }else if(scoreboardType.matches("oop")) {
                        oopAdapter = new ScoreboardOOPAdapter(listData);
                        rv.setAdapter(oopAdapter);
                    }else if(scoreboardType.matches("ds")) {
                        dsAdapter = new ScoreboardDSAdapter(listData);
                        rv.setAdapter(dsAdapter);
                    }else if(scoreboardType.matches("sad")) {
                        sadAdapter = new ScoreboardSADAdapter(listData);
                        rv.setAdapter(sadAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ScoreboardAll.this, "Something went wrong.\n"+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
