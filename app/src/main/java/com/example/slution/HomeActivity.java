package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recentConversationsRecycler;
    private ChatAdapter adapter;
    private List<ChatItem> chatList;
    private MaterialCardView newChatCard, favoritesCard;
    private ShapeableImageView profileButton;
    private ConstraintLayout emptyStateContainer;
    private DatabaseReference chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        recentConversationsRecycler = findViewById(R.id.recent_conversations_recycler);
        newChatCard = findViewById(R.id.new_chat_card);
        favoritesCard = findViewById(R.id.favorites_card);
        profileButton = findViewById(R.id.profile_button);
        emptyStateContainer = findViewById(R.id.empty_state_container);

        // Setup RecyclerView
        recentConversationsRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        recentConversationsRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("chatId", item.getChatId());
            intent.putExtra("chatUser", item.getUserName());
            startActivity(intent);
        });

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://slution-24489-default-rtdb.europe-west1.firebasedatabase.app/");
        chatsRef = database.getReference("chats");

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    ChatItem item = chatSnapshot.getValue(ChatItem.class);
                    if (item != null) {
                        chatList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
                toggleEmptyState(chatList.isEmpty());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Failed to load chats: " + error.getMessage());
            }
        });

        newChatCard.setOnClickListener(v -> {
            String chatId = chatsRef.push().getKey();
            if (chatId != null) {
                ChatItem newChat = new ChatItem(chatId, "Chat #" + (chatList.size() + 1), "", "");
                chatsRef.child(chatId).setValue(newChat);
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("chatUser", newChat.getUserName());
                startActivity(intent);
            }
        });

        favoritesCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            recentConversationsRecycler.setVisibility(View.GONE);
        } else {
            emptyStateContainer.setVisibility(View.GONE);
            recentConversationsRecycler.setVisibility(View.VISIBLE);
        }
    }
}