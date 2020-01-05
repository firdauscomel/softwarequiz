package com.daus.softwarequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.daus.softwarequiz.QuizActivity.hintCount;


public class MainMenu extends AppCompatActivity {
    private MaterialButton webBtn, oopBtn, dsBtn, sadBtn;
    private TextView userName;
    private ImageView mMenuLogoutImg;
    String name="", email="", uid="";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        userName = findViewById(R.id.user_name_txt);
        mMenuLogoutImg = findViewById(R.id.menu_logout_img);
        webBtn = findViewById(R.id.menu_web_button);
        oopBtn = findViewById(R.id.menu_oop_button);
        dsBtn = findViewById(R.id.menu_data_struct_button);
        sadBtn = findViewById(R.id.menu_sad_button);

        getUserDetails();

        userName.setText("Hello\n"+name);

        mMenuLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        if(!uid.matches("yPuVqN1ov6Sv5oXoGFhTIdhDZ712")){
            webBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, QuizActivity.class);
                    intent.putExtra("Quiz_type", "web");
                    startActivity(intent);
                }
            });

            oopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, QuizActivity.class);
                    intent.putExtra("Quiz_type", "oop");
                    startActivity(intent);
                }
            });

            dsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, QuizActivity.class);
                    intent.putExtra("Quiz_type", "ds");
                    startActivity(intent);
                }
            });

            sadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, QuizActivity.class);
                    intent.putExtra("Quiz_type", "sad");
                    startActivity(intent);
                }
            });
        }else{
            webBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, ScoreboardAll.class);
                    intent.putExtra("scoreboard_type", "web");
                    startActivity(intent);
                }
            });

            oopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, ScoreboardAll.class);
                    intent.putExtra("scoreboard_type", "oop");
                    startActivity(intent);
                }
            });

            dsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, ScoreboardAll.class);
                    intent.putExtra("scoreboard_type", "ds");
                    startActivity(intent);
                }
            });

            sadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, ScoreboardAll.class);
                    intent.putExtra("scoreboard_type", "sad");
                    startActivity(intent);
                }
            });
        }


    }

    public void open_scoreboard(View view){
        Intent intent = new Intent(MainMenu.this, ScoreboardAll.class);
        intent.putExtra("scoreboard_type", "all");
        startActivity(intent);
    }

    private void getUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            Log.d("AUTH", "Logged in User: Name: " +name + " Email: " + email + "  UID: " + uid);
        }else{
            Intent intent = new Intent(MainMenu.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}