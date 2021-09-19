package com.example.projekt1.activities.plugins;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.dialog.PluginListElementDialog;
import com.example.projekt1.models.PollOption;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginPoll;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class PluginPollFragment extends PluginBaseFragment implements PluginListElementDialog.DialogCloseListener {

    ItemTouchHelper itemTouchHelper;
    private RecyclerView pollRecyclerView;
    private PollOptionAdapter pollOptionAdapter;
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
        pollOptionAdapter = new PollOptionAdapter(this.pollOptionList, getActivity(), actualPlugin);
        pollRecyclerView.setAdapter(pollOptionAdapter);


        //attach itemTouchHelper to RecyclerView
        itemTouchHelper = new ItemTouchHelper(new PollRecyclerItemTouchHelper(pollOptionAdapter));
        itemTouchHelper.attachToRecyclerView(pollRecyclerView);

        pollSubmitButton = view.findViewById(R.id.pollSubmitButton);

        pollSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(this, "Du hast abgestimmt", Toast.LENGTH_SHORT).show();
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

    public static class PollOptionAdapter extends RecyclerView.Adapter<PollOptionAdapter.ViewHolder>{

    }

    public static class PluginPollOptionFragment {
    }

    public static class PollRecyclerItemTouchHelper {
        private NotizenAdapter adapter;

        public NotizenRecyclerItemTouchHelper(NotizenAdapter adapter) {
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
}
