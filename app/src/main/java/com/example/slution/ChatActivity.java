package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

        chatId = getIntent().getStringExtra("chatId");
        chatUser = getIntent().getStringExtra("chatUser");

        TextView title = findViewById(R.id.chatTitle);
        title.setText(chatUser);

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);
        messageEditText = findViewById(R.id.messageEditText);
        ImageButton sendButton = new ImageButton(this);
        sendButton.setImageResource(android.R.drawable.ic_menu_send);
        sendButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) findViewById(R.id.messageInputLayout)).addView(sendButton);

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

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
                toggleEmptyState(messages.isEmpty());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                String timestamp = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                ChatItem newMessage = new ChatItem(message, timestamp);
                messagesRef.push().setValue(newMessage);

                DatabaseReference chatInfoRef = database.getReference("chats").child(chatId);
                chatInfoRef.child("lastMessage").setValue(message);
                chatInfoRef.child("time").setValue(timestamp);

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

        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, HomeActivity.class));
            finish();
        });

        findViewById(R.id.cameraFab).setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, TranslationActivity.class));
        });
    }

    private void toggleEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
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
