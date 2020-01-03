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
                startActivity(new Intent(MainMenu.this, MainActivity.class));
            }
        });


    }

    public void menu_web(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "web");
        startActivity(intent);
    }
}