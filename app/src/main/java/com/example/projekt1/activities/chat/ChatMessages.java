package com.example.projekt1.activities.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChatMessages extends RecyclerView.Adapter<ChatMessages.ViewHolder> {

    ArrayList<Message> data;
    //Context context;
    Activity context;

    public ChatMessages(){
        this.data = this.getMessages();
    }

    public ChatMessages(ArrayList<Message> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ChatMessages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.home_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessages.ViewHolder holder, final int position) {
        //holder.textView.setText(data[position].getContent());
        if(position % 2 == 0)holder.textView.setText("Text1");
        else holder.textView.setText("Text2");

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public ArrayList<Message> getMessages(){
        return new ArrayList<Message>(){};
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.home_textView_chat);
        }
    }
}

