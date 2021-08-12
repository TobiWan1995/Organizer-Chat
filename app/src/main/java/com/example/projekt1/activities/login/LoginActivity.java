package com.example.projekt1.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekt1.R;
import com.example.projekt1.activities.authentication.AuthenticationActivity_first;
import com.example.projekt1.activities.home.HomeActivity;
import com.example.projekt1.activities.launcher.LauncherActivity;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    public static Context context;

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");

    // LoginView - Elements
    Button loginButton;
    EditText editTexteMail, editTextPassword;

    // data for login
    String eMail, password;

    // Session for current user
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);
        LoginActivity.context = getApplicationContext();

        // initialize Login
        editTexteMail = findViewById(R.id.editTextLoginEMail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        loginButton = findViewById(R.id.loginLoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get entered Values
                eMail = editTexteMail.getText().toString();
                password = editTextPassword.getText().toString();
                // Firebase get user
                userref.orderByChild("eMail").equalTo(eMail).addChildEventListener(new ChildListener());
            }
        });
    }

    private class ChildListener implements ChildEventListener {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            if(dataSnapshot.getValue(User.class).getPassword().equals(password)){
                User currentUser = dataSnapshot.getValue(User.class);

                // get userlist of current user - no Firebase-Support for Array or List
                // need to fetch ref and iterate through it
                String userID;
                DataSnapshot userListDS = dataSnapshot.child("users");
                for (int i=0;i< userListDS.getChildrenCount() ; i++) {
                    // get current User of User
                    userID = userListDS.child(String.valueOf(i)).getValue(String.class);
                    currentUser.addUser(userID);
                }

                // set Session
                session = new Session(LoginActivity.context);
                session.setUser(currentUser);

                // redirect view to home
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Login failed - Password incorrect", Toast.LENGTH_LONG).show();
            }
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
