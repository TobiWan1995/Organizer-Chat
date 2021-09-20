package com.example.projekt1.activities.plugins;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.dialog.PluginListElementDialog;
import com.example.projekt1.models.PollOption;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginPoll;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PluginPollFragment extends PluginBaseFragment implements PluginListElementDialog.DialogCloseListener {

    ItemTouchHelper itemTouchHelper;
    private RecyclerView pollRecyclerView;
    private PollAdapter pollOptionAdapter;
    private CheckBox checkBoxPoll;
    private EditText etPollOption;
    private Button pollSubmitButton;

    //List for PollOptions
    private ArrayList<PollOption> pollOptionList;


    @Override
    public void initializePlugin() {

        //cast plugin to PollPlugin
        PluginPoll actualPlugin = (PluginPoll) plugin;
        this.pollOptionList = this.plugin.getPluginData();

        //Initialize RecyclerView and Adapter
        pollRecyclerView = view.findViewById(R.id.pollRecyclerView);
        pollRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        pollOptionAdapter = new PollAdapter(this.pollOptionList, getActivity(), actualPlugin);
        pollRecyclerView.setAdapter(pollOptionAdapter);


        //attach itemTouchHelper to RecyclerView
        //itemTouchHelper = new ItemTouchHelper(new PollRecyclerItemTouchHelper(pollOptionAdapter));
        //itemTouchHelper.attachToRecyclerView(pollRecyclerView);

        pollSubmitButton = view.findViewById(R.id.pollSubmitButton);

        pollSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Du hast abgestimmt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Plugin setNewPlugin(String key) {

        return new PluginPoll(key, "Mit diesem Plugin lassen sich Abstimmungen durchführen", this.chatId, this.pluginType) {
            @Override
            public void doPluginStuff() {

            }
        };
    }

    @Override
    protected PluginPoll castToSpecifiedPlugin(DataSnapshot pluginSpecificData, DataSnapshot pluginDataList) {
        ArrayList<Poll> polls = new ArrayList<>();
        PluginPoll tempPluginPoll = pluginSpecificData.getValue(PluginPoll.class);

        if (pluginSpecificData != null) {
            for (DataSnapshot pollDs : pluginSpecificData.getChildren()) {
                Poll poll = pollDs.getValue(Poll.class);
                polls.add(poll);

            }
        }
        return new PluginPoll(tempPluginPoll.getId(), tempPluginPoll.getBeschreibung(), tempPluginPoll.getTyp(), tempPluginPoll.getChatRef(), polls);
    }

    @Override
    protected void setPluginLayout() {
        this.layout = R.layout.fragment_poll_plugin;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {

    }
}

    class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {

        private ArrayList<PollOption> pollOptionList;
        private FragmentActivity activity;
        private PluginPoll pluginPoll;

        //firebase
        private FirebaseDatabase root = FirebaseDatabase.getInstance();
        private DatabaseReference pluginRefFirebase = root.getReference("plugin");

        //constructor passing database and activity
        public PollAdapter(ArrayList<PollOption> pollOptionList, FragmentActivity activity, PluginPoll pluginPoll) {
            this.pollOptionList = pollOptionList;
            this.activity = activity;
            this.pluginPoll = pluginPoll;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }


        //public static class PluginPollOptionFragment {
    //}

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText etPollOption;

        ViewHolder(View view) {
            super(view);
            etPollOption = view.findViewById(R.id.eTPollOption);
        }
    }
}



