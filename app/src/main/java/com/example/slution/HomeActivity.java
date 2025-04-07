package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatItem> chatList;
    private MaterialButton btnNewChat, btnViewFavorites;
    private ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_chats);
        btnNewChat = findViewById(R.id.btn_new_chat);
        btnViewFavorites = findViewById(R.id.btn_view_favorites);
        profileIcon = findViewById(R.id.profile_icon);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        chatList.add(new ChatItem("John", "5 min ago"));
        chatList.add(new ChatItem("Emily", "10 min ago"));
        chatList.add(new ChatItem("Daniel", "30 min ago"));
        chatList.add(new ChatItem("Sarah", "1 hour ago"));
        chatList.add(new ChatItem("Maya", "2 hours ago"));

        adapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(adapter);

        btnNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        btnViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
