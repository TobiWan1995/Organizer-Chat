package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.PluginNotizen;
import com.example.projekt1.models.plugins.pluginData.Notiz;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class PluginNotizenFragment extends PluginBaseFragment {
    @Override
    public void initializePlugin() {
    }

    @Override
    public PluginNotizen setNewPlugin() {
        return new PluginNotizen("Mit diesem Plugin lassen sich Notizen verfassen", this.chatId) {
            @Override
            public void doPluginStuff() {

            }
        };
    }

    @Override
    protected PluginNotizen castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginDataList) {
        ArrayList<Notiz> notizen = new ArrayList<>();
        return null;
    }

    @Override
    protected void setPluginLayout() {
        this.layout = R.layout.fragment_notizen_plugin;
    }

}
