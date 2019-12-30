package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainMenu extends AppCompatActivity {
    private ImageView mMenuLogoutImg;
    private Button web;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mMenuLogoutImg = findViewById(R.id.menu_logout_img);
        web = findViewById(R.id.menu_web_button);

        mMenuLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void menu_web(View view){
        Intent intent = new Intent(MainMenu.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "web");
        startActivity(intent);
    }
}