package com.example.projekt1.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekt1.R;
import com.example.projekt1.activities.launcher.LauncherActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

    // Deklarieren von Variablen
    RecyclerView recyclerView;
    Home home;
    Button addChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Chats
        ArrayList<Chat> dummyChats = new ArrayList<Chat>();

        // dummy-user firebase
        userref.child(String.valueOf(LauncherActivity.currentUser.getId())).setValue(LauncherActivity.currentUser);

        // dummy-chat firebase
        Chat chat1 = new Chat(1, "Chat1 - Firebase", new ArrayList<User>(), new ArrayList<Message>());
        Message initialMessage = new Message( 1, "Hello World.", LauncherActivity.currentUser);
        chat1.addUser(LauncherActivity.currentUser);
        chat1.addMessage(initialMessage);
        chatref.child(String.valueOf(chat1.getId())).setValue(chat1);
        chatref.child(String.valueOf(2)).setValue(chat1);
        chatref.child(String.valueOf(3)).setValue(chat1);
        chatref.child(String.valueOf(4)).setValue(chat1);
        chatref.child(String.valueOf(5)).setValue(chat1);
        chatref.child(String.valueOf(6)).setValue(chat1);

        HomeActivity.context = getApplicationContext();

        // Home - RecyclerView - Implementation
        recyclerView = findViewById(R.id.home_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.context));

        home = new Home(dummyChats);
        recyclerView.setAdapter(home);

        // init chat data with initial list - late ValueEventListener
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    dummyChats.add(child.getValue(Chat.class));
                }

                home.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(HomeActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // addChatButton = findViewById(R.id.addChatButton); Muss noch im Design hinzugefügt werden
        // addChatButton.setOnClickListener(this);
    }
    /*@Override
    public void onClick(View view) {    // init chat data with initial list - late ValueEventListener
            /*case R.id.addChatButton:
                //chatHinzufügen();
              break;
        }
    }*/
}