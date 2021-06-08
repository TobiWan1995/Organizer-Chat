package com.example.projekt1.activities.login;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekt1.R;
import com.example.projekt1.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);


        // Login
        // Firebase get user
        // redirect view to home
    }

}
