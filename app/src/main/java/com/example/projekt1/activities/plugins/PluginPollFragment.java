package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.google.firebase.database.DataSnapshot;

public class PluginPollFragment extends PluginBaseFragment {
    @Override
    public void initializePlugin() {
        this.layout= R.layout.fragment_notizen_plugin;
        ;
    }

    @Override
    public Plugin setNewPlugin(String key) {
        return null;
    }

    @Override
    protected Plugin castToSpecifiedPlugin(DataSnapshot pluginSpecificData, DataSnapshot pluginDataList) {
        return null;
    }

    @Override
    protected void setPluginLayout() {

    }
}
