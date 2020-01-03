package com.daus.softwarequiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class login extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btnlogin, devLoginBtn;
    private ProgressBar progressbar;
    private ImageView mMenuLogoutImg;
    private TextView tvregister, tvforgot;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.login_email_input);
        passwordTextView = findViewById(R.id.login_password_input);
        Btnlogin = findViewById(R.id.login_button);
        progressbar = findViewById(R.id.progressBar);
        mMenuLogoutImg = findViewById(R.id.login_close_button);
        tvregister = findViewById(R.id.register_text_view);
        tvforgot = findViewById(R.id.forgot_text_view);
        devLoginBtn = findViewById(R.id.login_button2);

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(login.this, register.class);
                startActivity(register);
                finish();
            }
        });

        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot = new Intent(login.this, ForgotPassword.class);
                startActivity(forgot);
                finish();
            }
        });

        mMenuLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressbar.setVisibility(View.VISIBLE);

                final String email, password;
                email = emailTextView.getText().toString();
                password = passwordTextView.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                    emailTextView.setText("");
                    passwordTextView.setText("");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                    emailTextView.setText("");
                    passwordTextView.setText("");
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                    emailTextView.setText("");
                    passwordTextView.setText("");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                                            progressbar.setVisibility(View.GONE);

                                            emailTextView.setText("");
                                            passwordTextView.setText("");

                                            //admin login
                                            if(email.equals("test@gmail.com") && password.equals("12345678")){
                                                Toast.makeText(getApplicationContext(), "Welcome to dev page", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(login.this, MainMenu.class);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(login.this, MainMenu.class);
                                                startActivity(intent);
                                            }

                                            Intent intent = new Intent(login.this, MainMenu.class);
                                            startActivity(intent);

                                        }

                                        else {

                                            Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                                            emailTextView.setText("");
                                            passwordTextView.setText("");

                                            progressbar.setVisibility(View.GONE);
                                        }
                                    }
                                });
            }
        });

        devLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devLogin();
            }
        });
    }

    public void devLogin(){
        mAuth.signInWithEmailAndPassword("test@gmail.com", "12345678")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainMenu = new Intent(login.this,MainMenu.class);
                            startActivity(mainMenu);
                            Toast.makeText(login.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
