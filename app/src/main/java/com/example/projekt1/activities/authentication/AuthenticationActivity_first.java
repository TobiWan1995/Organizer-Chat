package com.example.projekt1.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.activities.home.HomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity_first extends AppCompatActivity {
    // auth next Buttons
    Button next1;
    // auth Data
    String fullname, username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate_stage1);

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
                // get Data from Stage1
                fullname = editTextFullname.getText().toString();
                username = editTextUsername.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(fullname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Alle Felder ausfüllen!", Toast.LENGTH_SHORT).show();
                } else {
                    // next - authentication 2
                    Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_second.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // pass data from Stage 1
                    intent.putExtra("username", username);
                    intent.putExtra("fullname", fullname);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }
}