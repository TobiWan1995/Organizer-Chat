package com.example.projekt1.activities.plugins;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.dialog.PluginListElementDialog;
import com.example.projekt1.models.Session;
import com.example.projekt1.models.plugins.pluginData.Notiz;
import com.example.projekt1.models.plugins.pluginData.PollOption;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginPoll;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.example.projekt1.models.plugins.pluginData.ToDo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PluginPollFragment extends PluginBaseFragment implements PluginListElementDialog.DialogCloseListener {

    private RecyclerView pollRecyclerView;
    private PollAdapter pollAdapter;
    private CheckBox checkBoxPoll;
    private TextView etPollOption;
    private TextView pollTitle;
    private EditText pollOptionInput;
    private Button pollSubmitButton;
    private ImageButton ibAddPoll;
    private FloatingActionButton addPollFab;
    //List for Poll
    private ArrayList<Poll> pollList;

    String zwischenspeicher;

    @Override
    public void initializePlugin() {
        //cast plugin to PollPlugin
        PluginPoll actualPlugin = (PluginPoll) plugin;
        this.pollList = this.plugin.getPluginData();

        addPollFab = view.findViewById(R.id.addPollFab);
        addPollFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment addNewToDoDialog = new PluginListElementDialog(actualPlugin);
                addNewToDoDialog.show(getActivity().getSupportFragmentManager(), "Poll hinzufügen");
            }
        });


        //Initialize RecyclerView and Adapter
        pollRecyclerView = view.findViewById(R.id.pollRecyclerView);
        pollRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        pollAdapter = new PollAdapter(this.pollList, getActivity(), actualPlugin);
        pollRecyclerView.setAdapter(pollAdapter);

        this.pluginRefFirebase.orderByChild("id").equalTo(actualPlugin.getId()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot pluginDataList = null;
                for (DataSnapshot pluginDs : snapshot.getChildren()) {
                    pluginDataList = pluginDs.hasChild("pluginData") ? pluginDs.child("pluginData") : null;
                }

                ArrayList<Poll> tempArrayList = new ArrayList<>();

                if (pluginDataList != null) {
                    for (DataSnapshot pollDs : pluginDataList.getChildren()) {
                        Poll poll = pollDs.getValue(Poll.class);
                        DataSnapshot pollOptionsDs = pollDs.child("pollOptions");
                        if (pollDs != null && pollOptionsDs != null) {
                            ArrayList<PollOption> options = new ArrayList<>();
                            for (DataSnapshot pollOptionDs : pollOptionsDs.getChildren()) {
                                PollOption pollOption = pollOptionDs.getValue(PollOption.class);
                                options.add(pollOption);
                            }
                            poll.setPollOptions(options);
                        }
                        tempArrayList.add(poll);
                    }
                }
                pollList = tempArrayList;
                pollAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public Plugin setNewPlugin(String key) {
        return new PluginPoll(key, "Mit diesem Plugin lassen sich Abstimmungen durchführen", this.pluginType, this.chatId, new ArrayList<Poll>()) {
            @Override
            public void doPluginStuff() {

            }
        };
    }

    @Override
    protected PluginPoll castToSpecifiedPlugin(DataSnapshot pluginData, DataSnapshot pluginSpecificData) {
        ArrayList<Poll> polls = new ArrayList<>();
        PluginPoll tempPluginPoll = pluginData.getValue(PluginPoll.class);

        if (pluginSpecificData != null) {
            for (DataSnapshot pollDs : pluginSpecificData.getChildren()) {
                Poll poll = pollDs.getValue(Poll.class);
                // pollOptions
                DataSnapshot pollOptionsDs = pollDs.child("pollOptions");
                ArrayList<PollOption> options = new ArrayList<>();
                for (DataSnapshot pollOptionDs : pollOptionsDs.getChildren()) {
                    PollOption pollOption = pollOptionDs.getValue(PollOption.class);
                    options.add(pollOption);
                    // userRefs of pollOption
                    DataSnapshot userRefDs = pollOptionDs.child("userRef");
                    ArrayList<String> userRefs = new ArrayList<>();
                    for(DataSnapshot userRef : userRefDs.getChildren()){
                        userRefs.add(userRef.getValue(String.class));
                    }
                    pollOption.setUserRef(userRefs);
                }
                poll.setPollOptions(options);
                // subbed users for poll
                DataSnapshot subUserDs = pollDs.child("subUser");
                ArrayList<String> subUserList = new ArrayList<>();
                for (DataSnapshot subUser : subUserDs.getChildren()){
                    subUserList.add(subUser.getValue(String.class));
                }
                poll.setSubUser(subUserList);
                // add poll to list
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
        pollAdapter.setPolls(this.pollList);
        pollAdapter.notifyDataSetChanged();
    }
}

class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {
    private ArrayList<Poll> pollList;
    private FragmentActivity activity;
    private PluginPoll pluginPoll;
    ItemTouchHelper itemTouchHelper;

    //firebase
    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private DatabaseReference pluginRefFirebase = root.getReference("Plugin");

    //constructor passing database and activity
    public PollAdapter(ArrayList<Poll> pollList, FragmentActivity activity, PluginPoll pluginPoll) {
        this.pollList = pollList;
        this.activity = activity;
        this.pluginPoll = pluginPoll;
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_recylleritem, parent, false);
        return new PollAdapter.PollViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Poll item = pollList.get(position);

        holder.pollTitle.setText(item.getTitle());

        // set nested RecyclerView
        holder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
        OptionsAdapter optionsAdapter = new OptionsAdapter(this.pollList.get(position).getPollOptions(), activity, pluginPoll,
                holder.subPollBtn, item, this, holder.optionsRecyclerView);
        holder.optionsRecyclerView.setAdapter(optionsAdapter);

        // attach itemtouchhelper
        itemTouchHelper = new ItemTouchHelper(new PollOptionRecyclerItemTouchHelper(optionsAdapter));
        itemTouchHelper.attachToRecyclerView(holder.optionsRecyclerView);

        // addPoll
        holder.addPollOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = pluginRefFirebase.child(pluginPoll.getId()).child("pluginData").child(pollList.get(position).getId()).child("pollOptions").push().getKey();
                PollOption pollOption = new PollOption(key, new ArrayList<String>());
                pollOption.setOptionTitle(holder.pollOptionEditText.getText().toString());
                holder.pollOptionEditText.setText("");

                pollList.get(position).addPollOption(pollOption);
                pluginPoll.setPolls(pollList);
                pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
                optionsAdapter.notifyDataSetChanged();
            }
        });

        holder.subPollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.deletePollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollList.remove(item);
                pluginPoll.setPolls(pollList);
                pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
            }
        });


    }

    public void openDialog() {

    }

    public void deletePoll(int position) {
        Poll item = pollList.get(position);
        pollList.remove(position);
        notifyItemRemoved(position);
        pluginPoll.setPolls(pollList);

        // firebase delete
        pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
    }

    @Override
    public int getItemCount() {
        return this.pollList.size();
    }

    public void setPolls(ArrayList<Poll> pollList) {
        this.pollList = pollList;
    }


    public class PollViewHolder extends RecyclerView.ViewHolder {
        TextView pollTitle;
        EditText pollOptionEditText;
        ImageButton addPollOption;
        RecyclerView optionsRecyclerView;
        Button subPollBtn;
        ImageButton deletePollBtn;


        PollViewHolder(View view) {
            super(view);
            pollTitle = view.findViewById(R.id.tVTitleOfPoll);
            pollOptionEditText = view.findViewById(R.id.pollOptionInput);
            addPollOption = view.findViewById(R.id.iBaddPollOptions);
            subPollBtn = view.findViewById(R.id.pollSubmitButton);
            optionsRecyclerView = view.findViewById(R.id.pollOptionRecyclerView);
            deletePollBtn = view.findViewById(R.id.deletePollBtn);

        }
    }


}

class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionViewHolder> {

    private final Button submitPollBtn;
    private ArrayList<PollOption> pollOptionList;
    private FragmentActivity activity;
    private PluginPoll pluginPoll;
    private Session session;
    private Poll poll;
    private PollAdapter pollAdapter;
    private RecyclerView recyclerView;

    //firebase
    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private DatabaseReference pluginRefFirebase = root.getReference("Plugin");

    //constructor passing database and activity
    public OptionsAdapter(ArrayList<PollOption> pollOptionList, FragmentActivity activity, PluginPoll pluginPoll,
                          Button submitPolllBtn, Poll poll, PollAdapter pollAdapter, RecyclerView recyclerView) {
        this.pollOptionList = pollOptionList;
        this.activity = activity;
        this.pluginPoll = pluginPoll;
        this.session = new Session(activity.getApplicationContext());
        this.submitPollBtn = submitPolllBtn;
        this.poll = poll;
        this.pollAdapter = pollAdapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_list, parent, false);
        return new OptionsAdapter.OptionViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final PollOption item = pollOptionList.get(position);

        holder.tVPollOption.setText(item.getOptionTitle());

        // if voted change view
        ArrayList<String> temp = poll.getSubUser().stream().filter(val -> session.getUserName().equals(val)).collect(Collectors.toCollection(ArrayList::new));
        // let the user return if already voted
        if (temp.size() > 0) {
            // get result
            PollOption currentWinner = poll.returnResult();
            // set result visible
            submitPollBtn.setText(currentWinner.getOptionTitle() + ": " + currentWinner.getUserRef().size() + "votes");
            submitPollBtn.setVisibility(View.VISIBLE);
            // set options invisible
            recyclerView.setVisibility(View.INVISIBLE);
        }

        holder.pollCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // set every other checkbox to false
                    for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                        final OptionsAdapter.OptionViewHolder optionViewHolder = (OptionViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                        if (!holder.equals(optionViewHolder))
                            optionViewHolder.pollCheckBox.setChecked(false);
                        // remove user from every other poll
                        pollOptionList = pollOptionList.stream().map(val  -> {
                            val.removeUserFromSub(session.getUserName());
                            return val;
                        }).collect(Collectors.toCollection(ArrayList::new));
                        // add user to item
                        item.addUserToSub(session.getUserName());
                        // update values
                        pollOptionList.set(position, item);
                        poll.setPollOptions(pollOptionList);
                        pluginPoll.updatePoll(poll);
                        pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
                    }

                } else {
                    item.removeUserFromSub(session.getUserName());
                }
                // update values
                pollOptionList.set(position, item);
                poll.setPollOptions(pollOptionList);
                pluginPoll.updatePoll(poll);
                // update firebase
                pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
            }
        });

        // if checked initialize view
        boolean test = item.containsUser(session.getUserName());
        if (item.containsUser(session.getUserName())) {
            holder.pollCheckBox.setChecked(true);
            submitPollBtn.setVisibility(View.VISIBLE);
        }

        submitPollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!poll.containsSubUser(session.getUserName())) {

                    poll.addUserToSub(session.getUserName());
                    // set options invisible
                    recyclerView.setVisibility(View.GONE);
                    // get result
                    PollOption currentWinner = poll.returnResult();
                    // set result visible
                    submitPollBtn.setText(currentWinner.getOptionTitle() + ": " + currentWinner.getUserRef().size() + " votes");

                }
                // update values
                pluginPoll.updatePoll(poll);
                // update firebase
                pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);

            }
        });
    }


    @Override
    public int getItemCount() {
        return this.pollOptionList.size();
    }

    public void setPolls(ArrayList<PollOption> pollOptionList) {
        this.pollOptionList = pollOptionList;
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        pollOptionList.remove(position);
        notifyItemRemoved(position);
        poll.setPollOptions(pollOptionList);
        pluginPoll.updatePoll(poll);
        // firebase delete
        pluginRefFirebase.child(pluginPoll.getId()).setValue(pluginPoll);
    }

    public void editItem(int position) {
        PollOption item = pollOptionList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId());
        bundle.putString("task", item.getOptionTitle());
        bundle.putBoolean("pollOption", true);
        bundle.putString("pollId", poll.getId());
        BottomSheetDialogFragment addNewNotizDialog = new PluginListElementDialog(pluginPoll);
        addNewNotizDialog.setArguments(bundle);
        addNewNotizDialog.show(activity.getSupportFragmentManager(), "PollOption bearbeiten");
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView tVPollOption;
        CheckBox pollCheckBox;


        OptionViewHolder(View view) {
            super(view);
            tVPollOption = view.findViewById(R.id.tVPollOption);
            pollCheckBox = view.findViewById(R.id.checkBox_Poll);
        }

    }
}

// ItemTouchHelper for RecyclerView

class PollOptionRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private OptionsAdapter adapter;

    public PollOptionRecyclerItemTouchHelper(OptionsAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Löschen");
            builder.setMessage("Wollen Sie den Inhalt wirklich löschen?");
            builder.setPositiveButton("Bestätigen",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.editItem(position);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}

