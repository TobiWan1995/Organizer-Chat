package com.example.projekt1.activities.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekt1.R;
import com.example.projekt1.activities.authentication.AuthenticationActivity_first;
import com.example.projekt1.activities.login.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    // Selection (Login/Auth) Buttons
    Button login, auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

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
                // cant return back if finished
                // finish();
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity_first.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                // cant return back if finished
                // finish();
            }
        });
    }
}
