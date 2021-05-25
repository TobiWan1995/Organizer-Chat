package com.example.projekt1.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity //implements View.OnClickListener
{
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");
    // Get Chat-Table-Reference from FireDB
    DatabaseReference chatref = root.getReference("Chat");
    // Get Chat-Table-Reference from FireDB

    //Deklarieren von Variablen
    RecyclerView recyclerView;
    Home home;
    Button addChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // dummy-user firebase
        // Setup Firebase-Database
        userref.child(String.valueOf(LoginActivity.currentUser.getId())).setValue(LoginActivity.currentUser);

        HomeActivity.context = getApplicationContext();

        // addChatButton = findViewById(R.id.addChatButton); Muss noch im Design hinzugefügt werden
        // addChatButton.setOnClickListener(this);

        // Home - RecyclerView - Implementation
        recyclerView = findViewById(R.id.home_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        home = new Home();
        recyclerView.setAdapter(home);

    }

    /*@Override
    public void onClick(View view) {
        switch(view.getId()){
            /*case R.id.addChatButton:
                //chatHinzufügen();
                break;
        }
    }*/


}