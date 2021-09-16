package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.ToDo;
import com.google.firebase.database.DataSnapshot;

public class PluginToDoFragment extends PluginBaseFragment {
    @Override
    public void initializePlugin() {

    }

    @Override
    public Plugin<ToDo> setNewPlugin(String key) {
        return new Plugin<ToDo>() {
            @Override
            public void doPluginStuff() {

            }
        };
    }

    @Override
    protected Plugin castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginDataList) {
        return null;
    }

    @Override
    protected void setPluginLayout() {
        this.layout = R.layout.fragment_todo_plugin;
    }
}
