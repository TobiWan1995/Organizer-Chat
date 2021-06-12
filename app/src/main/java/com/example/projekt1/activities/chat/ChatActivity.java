package com.example.projekt1.activities.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projekt1.R;
import com.example.projekt1.activities.home.HomeActivity;
import com.example.projekt1.activities.launcher.LauncherActivity;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.ChildEventListener;
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

    // recylcer adapter
    ChatMessages chatMessages;

    // current Chat
    Chat chat;

    // List to hold firebase-messages
    ArrayList<Message> chat_messages = new ArrayList<Message>();

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get Message-Table-Reference from FireDB
    DatabaseReference messageref = root.getReference("Message");

    // Session for current-user
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatActivity.context = getApplicationContext();

        // get Session
        session = new Session(getApplicationContext());

        // get chat passed as value to activity and extract messages
        chat = getIntent().getParcelableExtra("CHAT");

        // init adapter for recycler-view
        chatMessages = new ChatMessages(chat_messages, session.getId());

        // get data from Firebase when changed and update chat with new messageList
        messageref.orderByChild("chatId").equalTo(chat.getId()).addChildEventListener(new ChatActivity.ChildListener());

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
                // generate unique ID
                String key =  messageref.push().getKey();
                // save message to firebase
                messageref.child(key).setValue(new Message(key, enteredText.getText().toString(), session.getId(), chat.getId()));
                // reset message-input
                enteredText.setText("");
            }
        });
    }

    private class ChildListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            chat_messages.add(dataSnapshot.getValue(Message.class));
            chatMessages.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
        }

        @Override
        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
        }

        @Override
        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
        }
    }
}