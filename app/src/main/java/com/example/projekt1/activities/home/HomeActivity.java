package com.example.projekt1.activities.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projekt1.R;
import com.example.projekt1.activities.login.LoginActivity;
import com.example.projekt1.dialog.AddChatDialog;
import com.example.projekt1.dialog.AddUserDialog;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements AddChatDialog.ChatDialogListener // AddUserDialog.UserDialogListener
{
    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");
    // Get Chat-Table-Reference from FireDB
    DatabaseReference chatref = root.getReference("Chat");
    // Deklarieren von Variablen
    RecyclerView recyclerView;
    Home home;
    ImageButton addChatButton;
    ImageButton addUsersButton;

    // Session for current-user
    Session session;

    ArrayList<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init session
        session = new Session(getApplicationContext());

        // init home elements
        addChatButton = findViewById(R.id.addChatsButton);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChatDialog addChatDialog = new AddChatDialog();
                addChatDialog.show(getSupportFragmentManager(), "Add Chat - Dialog");
            }
        });

        /*
        addUsersButton = findViewById(R.id.addUsersButton);
        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserDialog addUserDialog = new AddUserDialog();
                addUserDialog.show(getSupportFragmentManager(), "Add User - Dialog");
            }
        });
         */

        // Chats
        chats = new ArrayList<Chat>();

        // Home - RecyclerView - Implementation
        recyclerView = findViewById(R.id.home_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        home = new Home(chats, this.getApplicationContext());
        recyclerView.setAdapter(home);

        // init chat data with data from firebase
        // to get every chat for the user, we need to iterate through every chats userList and check
        // if the current user is present - because of this complex situation the approach without a Query and a ChildEventListener
        // seems better to implement, but it will later be necessary for higher scale
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    // get current chat - will always hold an empty userList (missing list support)
                    Chat currChat  = child.getValue(Chat.class);
                    // throw assertion error if currChat is null
                    assert currChat != null;
                    // get userList of current chat - no Firebase-Support for Array or List
                    // need to fetch ref and iterate through it
                    DataSnapshot userListDS = child.child("users");
                    // temp save of users - extract them in for loop - add them after
                    ArraySet<String> tempUsers = new ArraySet<>();
                    // temp flag - to check if user belongs to chat - autoReset to false 'cause of loop
                    boolean isChatFromUser = false;
                    // iterate through users of chat and check for matches
                    for (int i=0;i< userListDS.getChildrenCount() ;i++) {
                        // get current User of chat
                        String currUser = userListDS.child(String.valueOf(i)).getValue(String.class);
                        // throw assertion-error if currUser is null
                        assert currUser != null;
                        // add user to tempUserSet - save after loop
                        tempUsers.add(currUser);
                        // check if currUser belongs to chat
                        if(currUser.equals(session.getUserName())){
                            isChatFromUser = true;
                        }
                    }
                    if(isChatFromUser) {
                        currChat.addUsers(tempUsers);
                        chats.add(currChat);
                    }
                }
                home.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void applyData(String chatTitle, ArraySet<String> users) {
        // switching to arraylist for firebase compatibility
        ArrayList<String> newUsers = new ArrayList<>(users);
        // generate unique id
        String key = chatref.push().getKey();
        // save new chat to firebase
        // throw assertion-error if key is null
        assert key != null;
        chatref.child(key).setValue(new Chat(key, chatTitle, newUsers));
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void applyData(ArraySet<String> users) {
        // update User
        User updatedUser = new User(session.getId(), session.getFullname(), session.getUserName(), session.geteMail(), session.getPassword(), session.getGender(), session.getBirth(), session.getPhoneNumber());
        updatedUser.addUserCollection(users);
        updatedUser.addUserCollection(session.getUsers());
        userref.child(session.getId()).setValue(updatedUser);

        // update Session
        this.session.setUser(updatedUser);
    }
    */
}