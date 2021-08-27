package com.example.projekt1.activities.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.SpannableString;
import android.util.ArraySet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.projekt1.R;
import com.example.projekt1.dialog.AddUserToChatDialog;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.Session;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity implements AddUserToChatDialog.UserDialogListener {
    RecyclerView recyclerView;
    ImageButton sendMessageButton, addUser;
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
    DatabaseReference chatref = root.getReference("Chat");

    // Session for current-user
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // get Session
        session = new Session(getApplicationContext());

        // get chat passed as value to activity and extract messages
        chat = getIntent().getParcelableExtra("CHAT");
        // get user to current chat
        ArrayList<String> userList = (ArrayList<String>) getIntent().getSerializableExtra("users");
        chat.addUsers(userList);

        // init adapter for recycler-view
        chatMessages = new ChatMessages(chat_messages, session.getId());

        // get data from Firebase when changed and update chat with new messageList
        messageref.orderByChild("chatId").equalTo(chat.getId()).addChildEventListener(new ChatActivity.ChildListener());

        // init Recycler-View with chatMessages
        recyclerView = findViewById(R.id.chat_activity_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
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
                // add Username to message
                String message =  session.getUserName() + "\n\n" + enteredText.getText().toString();
                // throw assertion-error if null
                assert key != null;
                // save message to firebase
                messageref.child(key).setValue(new Message(key, message, session.getId(), chat.getId()));
                // reset message-input
                enteredText.setText("");
            }
        });

        // init addUser-Button
        addUser = findViewById(R.id.addUsersButtonChat);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserToChatDialog addUserToChatDialog = new AddUserToChatDialog();
                addUserToChatDialog.show(getSupportFragmentManager(), "Add User to Chat - Dialog");
            }
        });
    }

    @Override
    public void applyData(ArraySet<String> users) {
        this.chat.addUsers(users);
        chatref.child(this.chat.getId()).setValue(this.chat);
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