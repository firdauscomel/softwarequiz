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

public class register extends AppCompatActivity {
    private EditText email,pass,id,name,dob;
    private Button save;
    DatabaseReference reference;
    myData studentData;
    long noID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        email = findViewById(R.id.register_email_input);
        pass = findViewById(R.id.register_password_input);
        id = findViewById(R.id.student_id_input);
        name = findViewById(R.id.student_name_input);
        dob = findViewById(R.id.student_dob_input);
        save = findViewById(R.id.register_button);
        studentData = new myData();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("myData");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    noID = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.length()!=0 && pass.length()!=0 && id.length()!=0 && name.length()!=0 && dob.length()!=0) {
                    studentData.setStdEmail(email.getText().toString());
                    studentData.setStdPass(pass.getText().toString());
                    studentData.setStdID(id.getText().toString());
                    studentData.setStdName(name.getText().toString());
                    studentData.setStdDOB(dob.getText().toString());

                    //reference.push().setValue(studentData);
                    reference.child(String.valueOf(noID + 1)).setValue(studentData);
                    email.setText("");
                    pass.setText("");
                    id.setText("");
                    name.setText("");
                    dob.setText("");

                    Toast.makeText(register.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(register.this, "Please Input Some Words!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
