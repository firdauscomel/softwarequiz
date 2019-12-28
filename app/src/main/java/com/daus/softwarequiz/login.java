package com.daus.softwarequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private EditText email, pass;
    private Button login;
    DatabaseReference reference;
    myData studentData;
    long noID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        email = findViewById(R.id.register_email_input);
        pass = findViewById(R.id.register_password_input);
        login = findViewById(R.id.login_btn);

        studentData = new myData();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("myData");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    noID = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.length()!=0 && pass.length()!=0){
                    Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
