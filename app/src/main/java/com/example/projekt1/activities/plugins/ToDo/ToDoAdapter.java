package com.example.projekt1.activities.plugins.ToDo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekt1.R;
import java.util.List;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private ToDo_Main_Activity activity;

    //constructor passing database and activity
    public ToDoAdapter(DatabaseHandler db, ToDo_Main_Activity activity) {
        this.db = db;
        this.activity = activity;
    }



    //RecyclerView calls onCreateViewHolder
    //onCreateViewHolder creates ItemView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater takes the XML file as input and builds the view objets from it
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_todo, parent, false);
        return new ViewHolder(itemView);
    }

    //ItemView calls onBindViewHolder
    //onBindViewHolder reads Item from list and fills Item View
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        //assign text and checked to ToDoModel
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //if status changed, the updateStatus function from the DatabaseHandler class will be called
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    //helperfunction to convert integer into boolean
    private boolean toBoolean(int n) {
        return n != 0;
    }

    //returns Listsize
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        //notify RecyclerView
        notifyDataSetChanged();
    }

    //Delete function
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    //Edit function
    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}