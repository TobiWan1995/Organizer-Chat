package com.example.projekt1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projekt1.R;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddUserToChatDialog extends AppCompatDialogFragment {

    // dialog elements
    Spinner spinner;
    Button addUserButton;

    // to pass data back to activity
    UserDialogListener userDialogListener;

    // set session
    Session session;

    // to store and pass back users
    ArraySet<String> users = new ArraySet<>();

    // Setup Firebase-Database
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    DatabaseReference userRef = root.getReference("User");

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_usertochat_dialog, null);

        // init session
        session = new Session(getActivity().getApplicationContext());
        // init spinner
        spinner = view.findViewById(R.id.spinner);

        // fetch users from firebase
        this.userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String[] arr = new String[(int) snapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot userDs : snapshot.getChildren()){
                    User user = userDs.getValue(User.class);
                    arr[index] = user.getUserName();
                    index++;
                }

                //build dropdown from all users
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_spinner_item , arr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // button
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem() == null || spinner.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(requireActivity().getApplicationContext(), "No selection", Toast.LENGTH_SHORT).show();
                    return;
                }
                users.add(spinner.getSelectedItem().toString());
                Toast.makeText(requireActivity().getApplicationContext(), "User added:" + "\n" + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // build Dialog
        builder.setView(view)
                .setTitle("Add User to Chat - Dialog")
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
        void applyData(ArraySet<String> users);
    }
}

