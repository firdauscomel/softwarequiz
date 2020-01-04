package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.daus.softwarequiz.QuizActivity.hintCount;


public class MainMenu extends AppCompatActivity {
    private ImageView mMenuLogoutImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mMenuLogoutImg = findViewById(R.id.menu_logout_img);

        mMenuLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

    public void menu_web(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "web");
        startActivity(intent);
    }

    public void menu_data_struct(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "ds");
        startActivity(intent);
    }

    public void menu_oop(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "oop");
        startActivity(intent);
    }

    public void menu_sad(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "sad");
        startActivity(intent);
    }
}