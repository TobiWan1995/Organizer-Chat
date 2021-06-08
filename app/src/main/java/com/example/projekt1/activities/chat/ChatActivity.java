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
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    public static Context context;
    RecyclerView recyclerView;
    ImageButton sendMessageButton;
    EditText enteredText;

    // List to hold firebase-messages - inital and update chat
    ArrayList<Message> chat_messages, chat_messages_initial;

            // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get Message-Table-Reference from FireDB
    DatabaseReference messageref = root.getReference("Message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatActivity.context = getApplicationContext();

        // get chat passed as value to activity and extract messages
        Chat chat = getIntent().getParcelableExtra("CHAT");
        chat_messages_initial = chat.getMessages();
        chat_messages = chat.getMessages();

        // init chat data with initial list
        messageref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    chat_messages_initial.add(child.getValue(Message.class));
                    chat_messages.add(child.getValue(Message.class));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ChatActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // fix async
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // init adapter for recycler-view
        ChatMessages chatMessages = new ChatMessages(chat_messages_initial);

        // get data from Firebase when changed and update chat with new list
        messageref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chat_messages = new ArrayList<Message>();
                for(DataSnapshot child : snapshot.getChildren()){
                    chat_messages.add(child.getValue(Message.class));
                    chatMessages.setMessages(chat_messages);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ChatActivity.context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // init Recycler-View with chatMessages
        recyclerView = findViewById(R.id.chat_activity_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatMessages);

        // init sendMessageButton and editText
        sendMessageButton = findViewById(R.id.sendMessageButton);
        enteredText = findViewById(R.id.enterMessageET);

        // set sendMessageButton onClickListener
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageref.child(String.valueOf(chat_messages.get(chat_messages.size() - 1).getId()+1)).setValue(new Message(chat_messages.size()+1, enteredText.getText().toString(), LauncherActivity.currentUser));
                enteredText.setText("");
            }
        });
    }
}