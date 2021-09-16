package com.example.projekt1.activities.plugins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt1.models.plugins.Plugin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public abstract class PluginBaseFragment extends Fragment {
    // contains the ViewLayoutId for the Fragment as Int - you have to initialize this in setPluginlayout()
    Integer layout;
    // contains the View to interact with viewElements
    View view;
    // contains the reference to the current chat
    String chatId;
    // firebase
    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference pluginRefFirebase = root.getReference("Plugin");
    // plugin specific data
    Plugin plugin;
    String pluginType;

    // logic
    public abstract void initializePlugin();

    // set your layout here
    protected abstract void setPluginLayout();

    public abstract Plugin setNewPlugin(String key);

    protected abstract Plugin castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginSpefificData);

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // set PluginLayout
        setPluginLayout();
        // inflate view
        view = inflater.inflate(layout, container, false);
        // init firebase for plugin
        if (getArguments() != null) {
            this.chatId = getArguments().getString("chatId");
            this.pluginType = getArguments().getString("pluginType");
        }
        pluginRefFirebase.orderByChild("chatRef").equalTo(this.chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                DataSnapshot pluginData = null;
                DataSnapshot pluginDataList = null; // because no arraylist support
                for (DataSnapshot pluginDs : snapshot.getChildren()) {
                    String currentType = pluginDs.child("typ").getValue(String.class);
                    if (currentType != null && currentType.equals(pluginType)) {
                        pluginData = pluginDs;
                        pluginDataList = pluginDs.hasChild("pluginData") ? pluginDs.child("pluginData") : null;
                    }
                }
                if (pluginData != null) {
                    plugin = castToSpecifiedPlugin(pluginData, pluginDataList);
                } else {
                    // create plugin if there is none
                    String key = pluginRefFirebase.push().getKey();
                    plugin = setNewPlugin(key);
                    pluginRefFirebase.child(key).setValue(plugin);
                }

                // plugin specific logic
                initializePlugin();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // return the initialized fragment as view
        return view;
    }
}
