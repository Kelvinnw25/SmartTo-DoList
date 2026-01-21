package com.example.finalproject.ui;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.model.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatList;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chat = chatList.get(position);
        holder.tvMessage.setText(chat.getMessage());

        if (chat.isUser()) {
            //position: right
            holder.chatLayout.setGravity(Gravity.END);

            //use bubble user
            holder.tvMessage.setBackgroundResource(R.drawable.bg_bubble_user);

            //text color: white
            holder.tvMessage.setTextColor(Color.WHITE);
        } else {
            //position: left
            holder.chatLayout.setGravity(Gravity.START);

            //use bubble ai
            holder.tvMessage.setBackgroundResource(R.drawable.bg_bubble_ai);

            //text color: black
            holder.tvMessage.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() { return chatList.size(); }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        LinearLayout chatLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);

            chatLayout = itemView.findViewById(R.id.chatLayout);
        }
    }
}