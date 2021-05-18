package com.example.projekt1.activities.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatMessages;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    public static Context context;
    RecyclerView recyclerView;
    ImageButton sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //---- Variablen zuweisen
        sendMessageButton = findViewById(R.id.sendMessageButton);

        recyclerView = findViewById(R.id.chat_activity_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatActivity.context = getApplicationContext();

        Chat chat = getIntent().getParcelableExtra("CHAT");

        chat.addUser(new User(1L, "Firstname", "Lastname", "Sheeeesh"));

        chat.sendMessage(new Message(0, "This is a chat message.", chat.getUsers().get(0)));
        chat.sendMessage(new Message(0, "This is another a chat message.", chat.getUsers().get(0)));
        chat.sendMessage(new Message(1, "This is a chat message......... asasddas  idaoasd asodason osasoh", chat.getUsers().get(0)));

        ArrayList<Message> chat_messages = chat.getMessages();

        ChatMessages chatMessages = new ChatMessages(chat_messages);
        recyclerView.setAdapter(chatMessages);
    }
}