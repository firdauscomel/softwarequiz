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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity implements BiometricCallback {

    private EditText emailTextView, passwordTextView;
    private Button Btnlogin, devLoginBtn, fingerPrintBtn;
    private ProgressBar progressbar;
    private ImageView mMenuLogoutImg;
    private TextView tvregister, tvforgot;
    BiometricManager mBiometricManager;

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
        fingerPrintBtn = findViewById(R.id.login_fingerprint);

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
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getApplicationContext(), "No account detected", Toast.LENGTH_LONG).show();

                } else {
                    mBiometricManager = new BiometricManager.BiometricBuilder(login.this)
                            .setTitle(getString(R.string.biometric_title))
                            .setSubtitle(getString(R.string.biometric_subtitle))
                            .setDescription(getString(R.string.biometric_description))
                            .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                            .build();

                    //start authentication
                    mBiometricManager.authenticate(login.this);
                }
            }
        });
    }

    public void devLogin(){
        mAuth.signInWithEmailAndPassword("admin@gmail.com", "12345678")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainMenu = new Intent(login.this,MainMenu.class);
                            mainMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        mBiometricManager.cancelAuthentication();
    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(login.this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }
}
