package com.example.projekt1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity //implements View.OnClickListener
{
    //Deklarieren von Variablen
    RecyclerView recyclerView;
    Home home;
    Button addChatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //addChatButton = findViewById(R.id.addChatButton); Muss noch im Design hinzugefügt werden

        // Home - RecyclerView - Implementation
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*
        Wie man mit der Recycler View umgeht muss noch in einer Adapterklasse definiert werden
         */
        home = new Home();
        recyclerView.setAdapter(home);


        //addChatButton.setOnClickListener(this);



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