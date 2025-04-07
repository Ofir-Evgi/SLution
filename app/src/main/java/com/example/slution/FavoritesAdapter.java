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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavViewHolder> {

    private final List<ChatItem> favoriteMessages;

    public FavoritesAdapter(List<ChatItem> favoriteMessages) {
        this.favoriteMessages = favoriteMessages;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_message, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        ChatItem item = favoriteMessages.get(position);
        Context context = holder.itemView.getContext();

        holder.messageText.setText(item.getUserName());
        holder.starIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow));

        holder.starIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                favoriteMessages.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteMessages.size();
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView messageText;
        ImageView starIcon;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.favoriteText);
            starIcon = itemView.findViewById(R.id.starIcon);
        }
    }
}