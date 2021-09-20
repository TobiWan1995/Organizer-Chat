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

import com.example.projekt1.models.plugins.pluginData.PollOption;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginNotizen;
import com.example.projekt1.models.plugins.PluginPoll;
import com.example.projekt1.models.plugins.PluginToDo;
import com.example.projekt1.models.plugins.pluginData.Notiz;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.example.projekt1.models.plugins.pluginData.ToDo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PluginListElementDialog extends BottomSheetDialogFragment {
    private EditText newTaskText;
    private Button newTaskSaveButton;
    // firebase
    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private DatabaseReference pluginRefFirebase = root.getReference("Plugin");
    // current Plugin
    Plugin plugin;

    public PluginListElementDialog(){};

    public PluginListElementDialog(Plugin plugin) {
        this.plugin = plugin;
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

        View view = inflater.inflate(R.layout.new_pluginlist_element, container, false);
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
                if (plugin instanceof PluginToDo) {
                    if (finalIsUpdate) {
                        ((PluginToDo) plugin).updateToDoTaskText(bundle.getString("id"), text);
                        pluginRefFirebase.child(plugin.getId()).setValue(plugin);
                    } else {
                        ToDo task = new ToDo();
                        String key = pluginRefFirebase.child(plugin.getId()).child("pluginData").push().getKey();
                        task.setId(key);
                        task.setTask(text);
                        task.setStatus(0);
                        ((PluginToDo) plugin).addToDoTask(task);
                        pluginRefFirebase.child(plugin.getId()).setValue(plugin);
                    }
                } else if (plugin instanceof PluginNotizen){
                    if (finalIsUpdate) {
                        ((PluginNotizen) plugin).updateNotizText(bundle.getString("id"), text);
                        pluginRefFirebase.child(plugin.getId()).setValue(plugin);
                    } else {
                        String key = pluginRefFirebase.child(plugin.getId()).child("pluginData").push().getKey();
                        ((PluginNotizen) plugin).addNotiz(new Notiz(key, text));
                        pluginRefFirebase.child(plugin.getId()).setValue(plugin);
                    }
                } else if (plugin instanceof PluginPoll) {
                    if (finalIsUpdate) {
                        // implementieren wenn man den titel bearbeiten möchte
                    } else {
                        String key = pluginRefFirebase.child(plugin.getId()).child("pluginData").push().getKey();
                        ((PluginPoll) plugin).addPoll(new Poll(key, false, text, new ArrayList<PollOption>()));
                        pluginRefFirebase.child(plugin.getId()).setValue(plugin);
                    }
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
