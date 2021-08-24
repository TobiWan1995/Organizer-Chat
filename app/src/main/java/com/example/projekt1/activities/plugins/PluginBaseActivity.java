package com.example.projekt1.activities.plugins;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class PluginBaseActivity extends AppCompatActivity {
    public abstract void initializePlugin();
    public void onCreate(Bundle S) {
        super.onCreate(S);
        this.initializePlugin();
    };
}
