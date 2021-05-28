package com.example.projekt1.activities.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    public static Context context;
    RecyclerView recyclerView;
    ImageButton sendMessageButton;
    EditText enteredText;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get Message-Table-Reference from FireDB
    DatabaseReference messageref = root.getReference("Message");

    // Testuser2
    User testuser = new User(1, "sdds", "sdds", "ssd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatActivity.context = getApplicationContext();

        // get chat passed as value to activity and extract messages
        Chat chat = getIntent().getParcelableExtra("CHAT");
        ArrayList<Message> chat_messages = chat.getMessages();

        // init chat data
        messageref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    chat_messages.add(child.getValue(Message.class));
                }
                System.out.println("first");
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(ChatActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // fix async
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // init adapter for recycler-view
        System.out.println("second");
        ChatMessages chatMessages = new ChatMessages(chat_messages);

        // get data from Firebase and also listen to changes
        messageref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    chat_messages.add(child.getValue(Message.class));
                    chatMessages.setMessages(chat_messages);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // init Recycler-View with chatMessages
        recyclerView = findViewById(R.id.chat_activity_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatMessages);

        // init dummy Messages - Firebase
        /*
        Message m1 = new Message(1, "testmessage", LoginActivity.currentUser);
        Message m2 = new Message(2, "testmessage", testuser);
        messageref.child(String.valueOf(1)).setValue(m1);
        messageref.child(String.valueOf(2)).setValue(m2); */

        // init sendMessageButton and editText
        sendMessageButton = findViewById(R.id.sendMessageButton);
        enteredText = findViewById(R.id.enterMessageET);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageref.child(String.valueOf(chat_messages.size()+1)).setValue(new Message(chat_messages.size()+1, enteredText.getText().toString(), LoginActivity.currentUser));
                enteredText.setText("");
            }
        });
    }
}