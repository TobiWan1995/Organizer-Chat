package com.example.projekt1.activities.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.launcher.LauncherActivity;
import com.example.projekt1.models.Message;

import java.util.ArrayList;

public class ChatMessages extends RecyclerView.Adapter<ChatMessages.ViewHolder> {
    // to identify Viewtype
    public static final int VIEWTYPE_MYMESSAGE = 0;
    public static final int VIEWTYPE_OTHERMESSAGE = 1;
    String userId;

    ArrayList<Message> data;
    Activity context;

    public ChatMessages(){
    }

    public ChatMessages(ArrayList<Message> data, String userId)
    {
        this.data = data;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChatMessages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if(viewType == VIEWTYPE_MYMESSAGE){
            view = layoutInflater.inflate(R.layout.message_send_layout, parent, false);
        }
        else {
            view = layoutInflater.inflate(R.layout.message_received_layout, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);;

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getUserId().equals(this.userId)) return VIEWTYPE_MYMESSAGE;
        else return VIEWTYPE_OTHERMESSAGE;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessages.ViewHolder holder, final int position) {
        if(data.get(position).getUserId().equals(this.userId)) holder.sendMessage.setText(data.get(position).getContent());
        else holder.receiveMessage.setText(data.get(position).getContent());;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setMessages(ArrayList<Message> newData){
        this.data = newData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView sendMessage;
        TextView receiveMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMessage = itemView.findViewById(R.id.textView_chat_send);
            receiveMessage = itemView.findViewById(R.id.textView_chat_received);
        }
    }
}

