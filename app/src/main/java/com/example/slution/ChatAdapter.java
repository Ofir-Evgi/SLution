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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatItem> chatList;
    private final Set<ChatItem> favoriteMessages = new HashSet<>();

    public ChatAdapter(List<ChatItem> chatList) {
        this.chatList = chatList;
    }

    public Set<ChatItem> getFavoriteMessages() {
        return favoriteMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_card, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem item = chatList.get(position);
        Context context = holder.itemView.getContext();

        holder.nameText.setText(item.getUserName());
        holder.timeText.setText(item.getTime());

        boolean isFavorite = favoriteMessages.contains(item);
        updateStarIcon(holder.starIcon, context, isFavorite);

        holder.starIcon.setOnClickListener(v -> {
            boolean nowFavorite;
            if (favoriteMessages.contains(item)) {
                favoriteMessages.remove(item);
                nowFavorite = false;
            } else {
                favoriteMessages.add(item);
                nowFavorite = true;
            }
            updateStarIcon(holder.starIcon, context, nowFavorite);
        });
    }

    private void updateStarIcon(ImageView starIcon, Context context, boolean isFavorite) {
        int color = ContextCompat.getColor(context, isFavorite ? R.color.yellow : R.color.gray);
        starIcon.setColorFilter(color);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView nameText;
        MaterialTextView timeText;
        ImageView starIcon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.chat_name);
            timeText = itemView.findViewById(R.id.chat_time);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }
}