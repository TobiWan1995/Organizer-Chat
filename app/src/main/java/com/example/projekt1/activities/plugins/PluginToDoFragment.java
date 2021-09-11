package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.ToDo;

public class PluginToDoFragment extends PluginBaseFragment {
    @Override
    public Plugin initializePlugin() {
        this.layout = R.layout.fragment_todo_plugin;
        System.out.println(this.chatRef);

        return new Plugin<ToDo>() {
            @Override
            public void doPluginStuff() {

            }
        };
    }
}
