package com.example.projekt1.activities.plugins;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projekt1.models.plugins.Plugin;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public abstract class PluginBaseFragment extends Fragment {
    // contains the ViewLayoutId for the Fragment as Int - you have to initialize this
    Integer layout;
    // contains the reference to the current chat
    String chatRef;
    // firebase
    FirebaseDatabase root =  FirebaseDatabase.getInstance();
    DatabaseReference chatRefFirebase = root.getReference("Chat");
    // PluginRef to check firebase and set Data if necessary
    Plugin plugin;

    public abstract Plugin initializePlugin();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // init plugins specific code
        this.plugin = initializePlugin();
        // init firebase for plugin
        this.chatRef = getArguments().getString("chatRef");
        // return the initialized fragment as view
        return inflater.inflate(layout, container, false);
    }
}
