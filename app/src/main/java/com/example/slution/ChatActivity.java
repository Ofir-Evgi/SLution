package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private EditText messageEditText;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 101;
    private ArrayList<ChatItem> messages;
    private DatabaseReference messagesRef;
    private LinearLayout emptyLayout;
    private String chatId;
    private String chatUser;
    private RecyclerView chatRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        // Fix: Use content view instead of findViewById with non-existent ID
        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chatId = getIntent().getStringExtra("chatId");
        chatUser = getIntent().getStringExtra("chatUser");

        // Initialize UI components
        TextView title = findViewById(R.id.chat_title);
        title.setText(chatUser != null ? chatUser : "Chat");

        chatRecyclerView = findViewById(R.id.messages_recyclerview);
        emptyLayout = findViewById(R.id.empty_state_container);
        messageEditText = findViewById(R.id.message_input);
        ImageView sendButton = findViewById(R.id.send_button);
        ImageView micButton = findViewById(R.id.mic_button);
        ImageView cameraButton = findViewById(R.id.camera_button);
        ImageView backButton = findViewById(R.id.back_button);
        ImageView cameraNavButton = findViewById(R.id.camera_nav_button);

        // Initialize RecyclerView
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://slution-24489-default-rtdb.europe-west1.firebasedatabase.app/");
        messagesRef = database.getReference("chats").child(chatId).child("messages");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    ChatItem item = messageSnapshot.getValue(ChatItem.class);
                    if (item != null) {
                        messages.add(item);
                        Log.d("FIREBASE", "Loaded message: " + item.getUserName());
                    } else {
                        Log.e("FIREBASE", "ChatItem is null");
                    }
                }
                chatAdapter.notifyDataSetChanged();

                // After loading messages, scroll to the bottom
                if (!messages.isEmpty()) {
                    chatRecyclerView.scrollToPosition(messages.size() - 1);
                }

                toggleEmptyState(messages.isEmpty());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });

        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });

        // Microphone button click listener
        micButton.setOnClickListener(v -> {
            startVoiceRecognition();
        });

        // Camera button click listener for opening the translation
        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, TranslationActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("chatUser", chatUser);
            startActivity(intent);
        });

        // Camera nav button in toolbar
        cameraNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, TranslationActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("chatUser", chatUser);
            startActivity(intent);
        });

        // Back button click listener
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            String timestamp = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            ChatItem newMessage = new ChatItem(message, timestamp);

            // Push message to Firebase
            messagesRef.push().setValue(newMessage)
                    .addOnSuccessListener(aVoid -> {
                        // Update last message info in the chat summary
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://slution-24489-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference chatInfoRef = database.getReference("chats").child(chatId);
                        chatInfoRef.child("lastMessage").setValue(message);
                        chatInfoRef.child("time").setValue(timestamp);

                        // Clear input field and scroll to bottom
                        messageEditText.setText("");
                        chatRecyclerView.scrollToPosition(messages.size());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
            Log.e("VoiceRecognition", "Error starting speech recognition: " + e.getMessage());
        }
    }

    private void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyLayout.setVisibility(View.VISIBLE);
            chatRecyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String recognizedText = results.get(0);
                messageEditText.setText(recognizedText);
                // Position cursor at the end
                messageEditText.setSelection(recognizedText.length());
            }
        }
    }
}