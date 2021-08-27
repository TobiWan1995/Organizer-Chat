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

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projekt1.R;
import com.example.projekt1.models.Session;

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

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.add_usertochat_dialog, null);

        // init session
        session = new Session(getActivity().getApplicationContext());

        // init Dialog elements
        // set Dropdown-List with users to add
        spinner = view.findViewById(R.id.spinner);
        // user-hashset to array
        String[] arr = session.getUsers().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // button
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem() == null || spinner.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "No selection", Toast.LENGTH_SHORT).show();
                    return;
                }
                users.add(spinner.getSelectedItem().toString());
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "User added:" + "\n" + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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

