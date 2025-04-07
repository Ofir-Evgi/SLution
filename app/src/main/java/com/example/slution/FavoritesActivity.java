package com.example.slution;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private LinearLayout emptyLayout;
    private FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);

        ArrayList<ChatItem> favoriteMessages = (ArrayList<ChatItem>) getIntent().getSerializableExtra("favorites");
        if (favoriteMessages == null) favoriteMessages = new ArrayList<>();

        if (favoriteMessages.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            favoritesRecyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            adapter = new FavoritesAdapter(favoriteMessages);
            favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            favoritesRecyclerView.setAdapter(adapter);
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}
