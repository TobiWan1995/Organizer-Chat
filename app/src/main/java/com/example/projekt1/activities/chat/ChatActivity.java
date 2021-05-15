package com.example.projekt1.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.health.SystemHealthManager;

import com.example.projekt1.R;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatActivity.context = getApplicationContext();

        Chat chat = getIntent().getParcelableExtra("CHAT");

        chat.addUser(new User(1L, "Firstname", "Lastname", "Sheeeesh"));

        System.out.println(chat.getTitel());

        ArrayList<Message> chat_messages = new ArrayList<Message>();

        for (User user : chat.getUsers()) {
            user.postMessage(new Message(1, "This is a chat message.", 1L));
            for (Message message : user.getMessages()) {
                if (chat.getId() == message.getId()) chat_messages.add(message);
            }
        }

        for (Message m : chat_messages) System.out.println(m.getContent());
    }
}