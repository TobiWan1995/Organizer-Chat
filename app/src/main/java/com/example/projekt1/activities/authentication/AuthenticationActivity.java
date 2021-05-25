package com.example.projekt1.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.TimeUtils;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.activities.main.HomeActivity;
import com.example.projekt1.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");
    // auth next Buttons
    Button next1, next2, next3;
    // auth Data
    String fullname, username, email, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_stage1);

        AuthenticationActivity.context = getApplicationContext();

        // init Stage1
        EditText editTextFullname = findViewById(R.id.editTextFullname);
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextEmail = findViewById(R.id.editTextEmailAddress);
        EditText editTextPassword = findViewById(R.id.editTextPasswordAuth);

        // navigate through authentication
        next1 = findViewById(R.id.authNext1);

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set Stage 2
                setContentView(R.layout.authenticate_stage2);

                // get Data from Stage1
                fullname = editTextFullname.getText().toString();
                username = editTextUsername.getText().toString();
                email = editTextEmail.getText().toString();
                phoneNumber = editTextPassword.getText().toString();

                // init Stage 2
                next2 = findViewById(R.id.authNext2);
                System.out.println(fullname + username + email + phoneNumber);
                next2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.authenticate_stage3);

                        // init Stage 3
                        next3 = findViewById(R.id.authNext3);
                        next3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }
                        });
                    }
                });
            }
        });




    }
}