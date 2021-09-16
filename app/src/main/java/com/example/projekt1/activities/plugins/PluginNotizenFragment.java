package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginNotizen;
import com.example.projekt1.models.plugins.pluginData.Notiz;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class PluginNotizenFragment extends PluginBaseFragment {
    @Override
    public void initializePlugin() {

        // PluginNotizen actualPlugin = (PluginNotizen) plugin;
        // actualPlugin.addNotiz(new Notiz("0", "hello"));
        // this.pluginRefFirebase.child(actualPlugin.getId()).setValue(actualPlugin);
    }

    @Override
    public PluginNotizen setNewPlugin(String key) {
        return new PluginNotizen(key, "Mit diesem Plugin lassen sich Notizen verfassen", this.chatId, this.pluginType) {
            @Override
            public void doPluginStuff() {

            }
        };
    }

    @Override
    protected PluginNotizen castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginSpecificData) {
        ArrayList<Notiz> notizen = new ArrayList<>();
        PluginNotizen tempPlugin = pluginData.getValue(PluginNotizen.class);

        if(pluginSpecificData != null){
            for( DataSnapshot notizDs : pluginSpecificData.getChildren()){
                Notiz notiz = notizDs.getValue(Notiz.class);
                notizen.add(notiz);
            }
        }

        return new PluginNotizen(tempPlugin.getId(), tempPlugin.getBeschreibung(), tempPlugin.getChatRef(), notizen);
    }

    @Override
    protected void setPluginLayout() {
        this.layout = R.layout.fragment_notizen_plugin;
    }

}
