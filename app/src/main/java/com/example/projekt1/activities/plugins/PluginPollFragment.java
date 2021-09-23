package com.example.projekt1.activities.plugins;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginPoll;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PluginPollFragment extends PluginBaseFragment {
    @Override
    public void initializePlugin() {

    }

    @Override
    public Plugin setNewPlugin(String key) {

        return new PluginPoll(key, "Mit diesem Plugin lassen sich Abstimmungen durchführen", this.chatId, this.pluginType, new ArrayList<>()) {
         @Override
         public void doPluginStuff() {

         }
        };
    }

    @Override
    protected PluginPoll castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginSpecificData) {
        ArrayList<Poll> polls = new ArrayList<>();
        PluginPoll tempPluginPoll = pluginData.getValue(PluginPoll.class);

        if(pluginSpecificData != null) {
            for( DataSnapshot pollDs : pluginSpecificData.getChildren()){
                Poll poll = pollDs.getValue(Poll.class);
                polls.add(poll);

            }
        }
        return new PluginPoll(tempPluginPoll.getId(), tempPluginPoll.getBeschreibung(), tempPluginPoll.getTyp(), tempPluginPoll.getChatRef(), polls);
    }

    @Override
    protected void setPluginLayout() { this.layout = R.layout.fragment_poll_plugin; }
}
