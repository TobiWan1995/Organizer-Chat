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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.projekt1.R;
import com.example.projekt1.models.Session;


import java.util.ArrayList;
import java.util.Arrays;

public class AddChatDialog extends AppCompatDialogFragment implements AddUserToChatDialog.UserDialogListener {

    // dialog elements
    EditText chatTitleEditText;
    Button addUserButton;
    Spinner spinner;

    // to pass data back to activity
    ChatDialogListener chatDialogListener;
    ArrayList<String> users = new ArrayList<>();


    // set session
    Session session;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_chat_dialog, null);

        // init session
        session = new Session(getActivity().getApplicationContext());

        // init Dialog elements
        chatTitleEditText = view.findViewById(R.id.editTextAddUser);
        addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.add(spinner.getSelectedItem().toString());
                Toast.makeText(getActivity().getApplicationContext(), "User added.", Toast.LENGTH_SHORT).show();
            }
        });
        // set Drop
        // down-List with users to add
        spinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item , new String[] {"1", "2", "3"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // build Dialog
        builder.setView(view)
                .setTitle("Add Chat - Dialog")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String chatTitle = chatTitleEditText.getText().toString();
                        // initial user
                        users.add(session.getId());
                        chatDialogListener.applyData(chatTitle, users);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            chatDialogListener = (ChatDialogListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void applyData(ArrayList<String> users) {
        System.out.println(Arrays.toString(users.toArray()));
    }

    public interface ChatDialogListener{
        public void applyData(String chatTitle, ArrayList<String> users);
    }
}

