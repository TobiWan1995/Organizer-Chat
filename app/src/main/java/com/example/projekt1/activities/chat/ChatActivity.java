package com.example.projekt1.activities.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        chat_messages.add(new Message(chat_messages.size()+1, "testmessage", LoginActivity.currentUser));
        chat_messages.add(new Message(chat_messages.size()+1, "testmessage2", testuser));
        chat_messages.add(new Message(chat_messages.size()+1, "testmessage3", LoginActivity.currentUser));
        chat_messages.add(new Message(chat_messages.size()+1, "testmessage4", testuser));

        // init Recycler-View with chatMessages
        recyclerView = findViewById(R.id.chat_activity_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatMessages chatMessages = new ChatMessages(chat_messages);
        recyclerView.setAdapter(chatMessages);

        // init sendMessageButton and editText
        sendMessageButton = findViewById(R.id.sendMessageButton);
        enteredText = findViewById(R.id.enterMessageET);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_messages.add(new Message(chat_messages.size()+1, enteredText.getText().toString(), LoginActivity.currentUser));
                chatMessages.setMessages(chat_messages);
            }
        });
    }
}