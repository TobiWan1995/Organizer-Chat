package com.example.projekt1.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projekt1.R;

import com.example.projekt1.models.plugins.PluginToDo;
import com.example.projekt1.models.plugins.pluginData.ToDo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewToDoDialog extends BottomSheetDialogFragment {
    private EditText newTaskText;
    private Button newTaskSaveButton;
    // firebase
    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private DatabaseReference pluginRefFirebase = root.getReference("Plugin");
    // current Plugin
    PluginToDo pluginToDo;

    public AddNewToDoDialog(){};

    public AddNewToDoDialog(PluginToDo pluginToDo) {
        this.pluginToDo = pluginToDo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task_todo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if (finalIsUpdate) {
                    pluginToDo.updateToDoTaskText(bundle.getString("id"), text);
                    pluginRefFirebase.child(pluginToDo.getId()).setValue(pluginToDo);
                } else {
                    ToDo task = new ToDo();
                    String key = pluginRefFirebase.child(pluginToDo.getId()).child("pluginData").push().getKey();
                    task.setId(key);
                    task.setTask(text);
                    task.setStatus(0);
                    pluginToDo.addToDoTask(task);
                    pluginRefFirebase.child(pluginToDo.getId()).setValue(pluginToDo);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }

    public interface DialogCloseListener {
        public void handleDialogClose(DialogInterface dialog);
    }
}
