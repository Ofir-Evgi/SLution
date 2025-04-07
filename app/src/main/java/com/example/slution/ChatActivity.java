package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private EditText messageEditText;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 101;
    private ArrayList<ChatItem> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messages = new ArrayList<>();
        messages.add(new ChatItem("I need help with directions.", "Just now"));
        messages.add(new ChatItem("Can you please repeat that?", "Just now"));
        messages.add(new ChatItem("Where is the nearest restroom?", "Just now"));

        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        findViewById(R.id.backButton).setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, FavoritesActivity.class);
            intent.putExtra("favorites", new ArrayList<>(chatAdapter.getFavoriteMessages()));
            startActivity(intent);
        });

        findViewById(R.id.cameraFab).setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, TranslationActivity.class);
            startActivity(intent);
        });

        messageEditText = findViewById(R.id.messageEditText);

        ImageButton sendButton = new ImageButton(this);
        sendButton.setImageResource(android.R.drawable.ic_menu_send);
        sendButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) findViewById(R.id.messageInputLayout)).addView(sendButton);

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                messages.add(new ChatItem(message, "Just now"));
                chatAdapter.notifyItemInserted(messages.size() - 1);
                messageEditText.setText("");
            }
        });

        findViewById(R.id.voiceButton).setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
            try {
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                messageEditText.setText(results.get(0));
            }
        }
    }
}
