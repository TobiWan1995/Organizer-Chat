package com.example.projekt1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projekt1.R;
import com.example.projekt1.models.Session;

import java.util.ArrayList;

public class AddUserToChatDialog extends AppCompatDialogFragment {

    // dialog elements
    Spinner spinner;
    Button addUserButton;

    // to pass data back to activity
    UserDialogListener userDialogListener;

    // set session
    Session session;

    // to store and pass back users
    ArrayList<String> users = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_usertochat_dialog, null);

        // init session
        session = new Session(getActivity().getApplicationContext());

        // init Dialog elements
        // set Dropdown-List with users to add
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item , new String[] {"1", "2", "3"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // button
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.add(spinner.getSelectedItem().toString());
                Toast.makeText(getActivity().getApplicationContext(), "User added.", Toast.LENGTH_SHORT).show();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userDialogListener = (UserDialogListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface UserDialogListener{
        public void applyData(ArrayList<String> users);
    }
}

