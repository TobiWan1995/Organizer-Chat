package com.example.projekt1.activities.plugins;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.projekt1.R;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Poll;

public class PluginPollFragment extends PluginBaseFragment implements View.OnClickListener{

    TextView tV_TitleOfPoll;
    private Activity context;
    int id;

    @Override
    public Plugin initializePlugin() {
        this.layout = R.layout.plugin_poll;

        return new Plugin<Poll>() {
            @Override
            public void doPluginStuff() {

            }
        };
    }




    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iB_addPollOptions:
                //createNewPollOptions();
                //Herausfinden wie views zuweisen von anderen xml dateien
                break;
        }
    }





}
