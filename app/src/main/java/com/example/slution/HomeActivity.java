package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatItem> chatList;
    private MaterialButton btnNewChat, btnViewFavorites;
    private ImageView profileIcon;
    private LinearLayout emptyLayout;

    private DatabaseReference chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_chats);
        btnNewChat = findViewById(R.id.btn_new_chat);
        btnViewFavorites = findViewById(R.id.btn_view_favorites);
        profileIcon = findViewById(R.id.profile_icon);
        emptyLayout = findViewById(R.id.emptyLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("chatId", item.getChatId());
            intent.putExtra("chatUser", item.getUserName());
            startActivity(intent);
        });

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

        btnNewChat.setOnClickListener(v -> {
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

        btnViewFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}