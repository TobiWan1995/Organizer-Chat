package com.example.projekt1.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekt1.R;
import com.example.projekt1.activities.home.HomeActivity;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

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
                userref.orderByChild("eMail").equalTo(eMail).addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            // reference to check current user
                            AtomicReference<User> currentUser = new AtomicReference<>();

                            // extract User from snapshot which is a List in ValueEventListener -> prefer ChildEventListener
                            // only used here because we need the case of no match in the query!
                            snapshot.getChildren().forEach(user -> {
                                currentUser.set(user.getValue(User.class));

                                // check if password is correct
                                if(!currentUser.get().getPassword().equals(password)){
                                    Toast.makeText(getApplicationContext(), "Login failed - Password incorrect", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // reference to extract contacts from User -> no firebase support for list
                                DataSnapshot userListDS = user.child("users");

                                if(userListDS.hasChildren()){
                                    for (int i = 0; i < userListDS.getChildrenCount() ; i++) {
                                        // get current User from userlist of User
                                        String userID = userListDS.child(String.valueOf(i)).getValue(String.class);
                                        currentUser.get().addUser(userID);
                                    }
                                }
                            });

                            // set Session
                            session = new Session(getApplicationContext());
                            session.setUser(currentUser.get());

                            // redirect view to home
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            // cant return back if finished
                            // finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed - E-Mail incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Login failed - There was an error.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
