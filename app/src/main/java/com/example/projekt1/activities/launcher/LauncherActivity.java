package com.example.projekt1.activities.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.activities.authentication.AuthenticationActivity_first;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LauncherActivity extends AppCompatActivity {
    public static User currentUser = new User("0", "SuperUser", "SuperUser", "SuperUser", "SuperUser", "SuperUser", "SuperUser");
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");

    // Selection (Login/Auth) Buttons
    Button login, auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);

        // dummy-user firebase
        userref.child(String.valueOf(LauncherActivity.currentUser.getId())).setValue(LauncherActivity.currentUser);

        LauncherActivity.context = getApplicationContext();

        // init Selection Buttons
        login = findViewById(R.id.loginButton);
        auth = findViewById(R.id.authButton);

        // navigate between login and authentication
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_first.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
    // Login
    // Firebase get user
    // redirect view to home
}
