package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Notiz;

public class PluginNotizenFragment extends PluginBaseFragment {
    @Override
    public Plugin initializePlugin() {
        this.layout = R.layout.fragment_notizen_plugin;
        System.out.println(this.chatRef);

        return new Plugin<Notiz>() {
            @Override
            public void doPluginStuff() {

            }
        };
    }

}
