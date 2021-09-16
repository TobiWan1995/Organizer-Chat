package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Notiz;

public class PluginPollFragment extends PluginBaseFragment {
    @Override
    public Plugin initializePlugin() {
        this.layout= R.layout.fragment_notizen_plugin;

        return new Plugin<Poll>() {
            @Override
            public void doPluginStuff() {

            }
        };
    }
}
