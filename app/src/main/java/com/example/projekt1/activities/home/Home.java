package com.example.projekt1.activities.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.Chat;
import com.example.projekt1.models.Message;
import com.example.projekt1.models.User;

import java.util.ArrayList;

public class Home extends RecyclerView.Adapter<Home.ViewHolder> {

    ArrayList<Chat> data;
    // Context context;
    Activity context;

    public Home(){
        this.data = this.getChats();
    }

    public Home(ArrayList<Chat> data){
        this.data = data;
    }

    @NonNull
    @Override
    public Home.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_home_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Home.ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).getTitel());
        System.out.println(data.get(position).getTitel());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat(data.get(position));
            }
        });
    }

    // how much items displayed
    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public ArrayList<Chat> getChats(){
        return new ArrayList<Chat>();
    }

    public void openChat(Chat chat){
        Intent intent = new Intent(HomeActivity.context, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("CHAT", chat);
        HomeActivity.context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.home_textView_chat);
        }
    }
}

