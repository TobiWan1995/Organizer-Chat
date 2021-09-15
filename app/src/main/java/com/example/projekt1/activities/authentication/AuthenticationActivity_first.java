package com.example.projekt1.activities.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AuthenticationActivity_first extends AppCompatActivity {
    // auth next Buttons
    Button next1;
    // auth Data
    String fullname, username, email, password;
    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");

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

                // Validation - fullname, username, email, password
                if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Validation - E-Mail
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(getApplicationContext(), "Invalid E-Mail", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // check if user is taken
                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){
                                User user = ds.getValue(User.class);
                                if(user.getUserName().equals(username)){
                                    Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                                }else{
                                    // next - authentication 2
                                    Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_second.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    // pass data from Stage 1
                                    intent.putExtra("username", username);
                                    intent.putExtra("fullname", fullname);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    getApplicationContext().startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}

