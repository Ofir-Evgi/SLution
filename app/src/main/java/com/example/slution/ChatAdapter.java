package com.example.slution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatItem> chatMessages;

    public ChatAdapter(List<ChatItem> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_card, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem item = chatMessages.get(position);
        Context context = holder.itemView.getContext();

        holder.messageText.setText(item.getUserName());
        holder.messageTime.setText(item.getTime());

        updateStarIcon(holder.starIcon, context, item);

        holder.starIcon.setOnClickListener(v -> {
            boolean isFavorited = FavoriteStorage.isFavorite(context, item);
            if (isFavorited) {
                FavoriteStorage.removeFavorite(context, item);
            } else {
                FavoriteStorage.addFavorite(context, item);
            }
            updateStarIcon(holder.starIcon, context, item);
        });
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    private void updateStarIcon(ImageView starIcon, Context context, ChatItem item) {
        boolean isFavorited = FavoriteStorage.isFavorite(context, item);
        int colorRes = isFavorited ? R.color.yellow : R.color.gray;
        starIcon.setColorFilter(ContextCompat.getColor(context, colorRes));
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView messageText, messageTime;
        ImageView starIcon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.chat_name);
            messageTime = itemView.findViewById(R.id.chat_time);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }
}
