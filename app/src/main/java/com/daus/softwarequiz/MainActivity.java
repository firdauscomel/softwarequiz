package com.daus.softwarequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button mLoginButton, mLoginSubmit, register;
    ImageView mMenuLogoutImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        mMenuLogoutImg = findViewById(R.id.menu_logout_img);
        mLoginSubmit = findViewById(R.id.lohin_button);
        mLoginButton = findViewById(R.id.login_btn);
        register = findViewById(R.id.sign_up_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,register.class);
                startActivity(intent);
        }
        });
    }

    public void menu_logout(View view) {
        this.setContentView(R.layout.activity_start_page);
    }
    public void register(View view) {
        this.setContentView(R.layout.register_page);
    }
    public void start_login(View view){
        this.setContentView(R.layout.login_page);
    }
    public void login_close(View view){
        this.setContentView(R.layout.activity_start_page);
    }
    public void login_submit(View view){
        this.setContentView(R.layout.main_menu);
    }
    public void menu_web(View view){
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("Quiz_type", "web");
        startActivity(intent);
    }


}
