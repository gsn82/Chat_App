package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> messages;

    public MessageAdapter() {
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        // указываем адапетру , что данные изменились
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_messages, viewGroup,false);
       return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int position) {
        Message message = messages.get(position);

        messageViewHolder.textViewAuthor.setText(message.getAuthor());
        messageViewHolder.textViewTextOfMassage.setText(message.getTextOfMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthor;
        private TextView textViewTextOfMassage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewTextOfMassage = itemView.findViewById(R.id.textViewTextOfMassage);
        }
    }
}
