package com.example.projekt1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class AddUserDialog extends AppCompatDialogFragment {

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    // Get User-Table-Reference from FireDB
    DatabaseReference userref = root.getReference("User");

    // dialog elements
    EditText addUserEditText;
    Button addUserButton;

    // to pass data back to activity
    UserDialogListener userDialogListener;

    // set session
    Session session;

    // to store and pass back users
    ArraySet<String> users = new ArraySet<>();

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user_dialog, null);

        // init session
        session = new Session(getActivity().getApplicationContext());

        // init Dialog elements
        addUserEditText = view.findViewById(R.id.editTextAddUser);

        // button
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addUserEditText.getText().toString().isEmpty()){
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Enter a Username", Toast.LENGTH_SHORT).show();
                }
                userref.orderByChild("userName").equalTo(addUserEditText.getText().toString()).addChildEventListener(new AddUserDialog.ChildListener());
            }
        });

        // build Dialog
        builder.setView(view)
                .setTitle("Add User - Dialog")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDialogListener.applyData(users);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            userDialogListener = (UserDialogListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface UserDialogListener{
        public void applyData(ArraySet<String> users);
    }

    private class ChildListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            User currUser = dataSnapshot.getValue(User.class);
            // throws assertion error when currUser is null, during runtime
            assert currUser != null;
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), ("User added: " + "\n" + currUser.getUserName()), Toast.LENGTH_SHORT).show();
            users.add(currUser.getId());
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

