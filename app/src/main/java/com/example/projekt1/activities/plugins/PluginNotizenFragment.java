package com.example.projekt1.activities.plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.dialog.PluginListElementDialog;
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.PluginNotizen;
import com.example.projekt1.models.plugins.PluginNotizen;
import com.example.projekt1.models.plugins.PluginToDo;
import com.example.projekt1.models.plugins.pluginData.Notiz;
import com.example.projekt1.models.plugins.pluginData.ToDo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PluginNotizenFragment extends PluginBaseFragment implements PluginListElementDialog.DialogCloseListener {

    ItemTouchHelper itemTouchHelper;
    private RecyclerView notizenRecyclerView;
    private NotizenAdapter notizenAdapter;
    private FloatingActionButton fab;
    //List for Notizen
    private ArrayList<Notiz> notizenList;

    @Override
    public void initializePlugin() {
        // Cast Plugin to PluginNotizen
        PluginNotizen actualPlugin = (PluginNotizen) plugin;
        this.notizenList = this.plugin.getPluginData();

        // initialize RecyclerView and Adapter
        notizenRecyclerView = view.findViewById(R.id.notizenRecyclerView);
        notizenRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        notizenAdapter = new NotizenAdapter(this.notizenList, getActivity(), actualPlugin);
        notizenRecyclerView.setAdapter(notizenAdapter);
        notizenAdapter.setNotizen(notizenList);

        // attach itemTouchHelper to Recyclerview
        itemTouchHelper = new ItemTouchHelper(new NotizenRecyclerItemTouchHelper(notizenAdapter));
        itemTouchHelper.attachToRecyclerView(notizenRecyclerView);

        //fab = + Button
        fab = view.findViewById(R.id.fabNotiz);

        //if + Button is pressed call AddNewTask
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment addNewToDoDialog = new PluginListElementDialog(actualPlugin);
                addNewToDoDialog.show(getActivity().getSupportFragmentManager(), "Notiz hinzufügen");
            }
        });

        // update pluginDataList from Firebase when changed
        this.pluginRefFirebase.orderByChild("id").equalTo(actualPlugin.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot pluginDataList = null;
                for (DataSnapshot pluginDs : snapshot.getChildren()) {
                    pluginDataList = pluginDs.hasChild("pluginData") ? pluginDs.child("pluginData") : null;
                }

                ArrayList<Notiz> tempArrayList = new ArrayList<>();

                if (pluginDataList != null) {
                    for (DataSnapshot toDoDs : pluginDataList.getChildren()) {
                        Notiz notiz = toDoDs.getValue(Notiz.class);
                        tempArrayList.add(notiz);
                    }
                }
                notizenList = tempArrayList;
                notizenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public PluginNotizen setNewPlugin(String key) {
        return new PluginNotizen(key, "Plugin um Notizen zu verfassen", this.chatId, this.pluginType, new ArrayList<Notiz>()) {
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

        return new PluginNotizen(tempPlugin.getId(), tempPlugin.getBeschreibung(), pluginType, tempPlugin.getChatRef(), notizen);
    }

    @Override
    protected void setPluginLayout() {
        this.layout = R.layout.fragment_notizen_plugin;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        notizenAdapter.setNotizen(notizenList);
        notizenAdapter.notifyDataSetChanged();
    }
}

// Recycler-Adapter for NotizenFragment
class NotizenAdapter extends RecyclerView.Adapter<NotizenAdapter.ViewHolder> {

    private ArrayList<Notiz> notizenList;
    private FragmentActivity activity;
    private PluginNotizen pluginNotizen;
    // firebase
    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private DatabaseReference pluginRefFirebase = root.getReference("Plugin");

    //constructor passing database and activity
    public NotizenAdapter(ArrayList<Notiz> notizenList, FragmentActivity activity, PluginNotizen pluginNotizen) {
        this.activity = activity;
        this.notizenList = notizenList;
        this.pluginNotizen = pluginNotizen;
    }

    //RecyclerView calls onCreateViewHolder
    //onCreateViewHolder creates ItemView
    @NonNull
    @Override
    public NotizenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater takes the XML file as input and builds the view objets from it
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notiz_element, parent, false);
        return new NotizenAdapter.ViewHolder(itemView);
    }

    //ItemView calls onBindViewHolder
    //onBindViewHolder reads Item from list and fills Item View
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final NotizenAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Notiz item = notizenList.get(position);
        String date =  DateFormat.getDateTimeInstance().format(Long.valueOf(item.getDatum()));
        holder.notiz.setText(date + ":\n\n" + item.getInhalt());
    }

    //helperfunction to convert integer into boolean
    private boolean toBoolean(int n) {
        return n != 0;
    }

    //returns Listsize
    @Override
    public int getItemCount() {
        return notizenList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setNotizen(ArrayList<Notiz> notizenList) {
        this.notizenList = notizenList;
        //notify RecyclerView
        notifyDataSetChanged();
    }

    //Delete function
    public void deleteItem(int position) {
        Notiz item = notizenList.get(position);
        notizenList.remove(position);
        notifyItemRemoved(position);
        pluginNotizen.setNotizen(notizenList);
        // firebase delete
        pluginRefFirebase.child(pluginNotizen.getId()).setValue(pluginNotizen);
    }

    //Edit function
    public void editItem(int position) {
        Notiz item = notizenList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId());
        bundle.putString("task", item.getInhalt());
        BottomSheetDialogFragment addNewNotizDialog = new PluginListElementDialog(pluginNotizen);
        addNewNotizDialog.setArguments(bundle);
        addNewNotizDialog.show(activity.getSupportFragmentManager(), "Notiz bearbeiten");
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notiz;
        ViewHolder(View view) {
            super(view);
            notiz = view.findViewById(R.id.notizText);
        }
    }
}

// ItemTouchHelper for RecyclerView

class NotizenRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

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