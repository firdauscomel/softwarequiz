package com.daus.softwarequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class register extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String QUIZ_PREFS = "QuizPrefs";

    private EditText inputEmail, inputPassword, inputUsername;
    private Button register;
    private ProgressBar progressBar;
    private ImageView mMenuLogoutImg;
    private TextView tvlogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        mAuth = FirebaseAuth.getInstance();

        tvlogin =  findViewById(R.id.login_text_view);
        mMenuLogoutImg = findViewById(R.id.register_close_button);
        register =  findViewById(R.id.register_button);
        inputEmail =  findViewById(R.id.login_email_input);
        inputPassword =  findViewById(R.id.login_password_input);
        inputUsername = findViewById(R.id.register_username_input);
        progressBar =  findViewById(R.id.progressBar);;


        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(register.this, login.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                finish();
            }
        });

        mMenuLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_SHORT).show();
                    inputEmail.setText("");
                    inputPassword.setText("");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_SHORT).show();
                    inputEmail.setText("");
                    inputPassword.setText("");
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    inputEmail.setText("");
                    inputPassword.setText("");
                    return;
                }
                if(!email.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    inputEmail.setText("");
                    inputPassword.setText("");
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    inputEmail.setText("");
                                    inputPassword.setText("");
                                } else {
                                    saveUserName();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(register.this, "Register successful!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            //Added small timeout to allow the username to load properly after registering
                                            finish();
                                            startActivity(new Intent(register.this, MainMenu.class));
                                        }
                                    }, 1000);
                                }
                            }
                        });

            }
        });
    }

    private void saveUserName(){
//        String userName = inputUsername.getText().toString();
//        SharedPreferences prefs = getSharedPreferences(QUIZ_PREFS, 0);
//        prefs.edit().putString(USERNAME, userName).apply();

        //Update display name in Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(inputUsername.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "User profile updated.");
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
