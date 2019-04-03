package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.R;
import com.example.team13.personalbest_team13_skeleton.chatmessage.ChatMessageService;
import com.example.team13.personalbest_team13_skeleton.chatmessage.FirestoreChatAdapter;
import com.example.team13.personalbest_team13_skeleton.chatmessage.MockChatMessage;
import com.google.firebase.FirebaseApp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatBoxActivity extends AppCompatActivity {
    private String userEmail;
    private String friendEmail;

    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    String TAG = ChatBoxActivity.class.getSimpleName();

    public ChatMessageService chatMessageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());


        Intent intent = getIntent();
        userEmail = intent.getExtras().getString("userEmail");
        friendEmail = intent.getExtras().getString("friendEmail");

        if (intent.getExtras().getString("test") != null) {
            chatMessageService = new MockChatMessage();
            userEmail = "test";
        } else {
            chatMessageService = new FirestoreChatAdapter(userEmail, friendEmail);
        }

        initMessageUpdateListener();

    }

    private void sendMessage() {

        EditText messageView = findViewById(R.id.text_message);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, userEmail);
        newMessage.put(TIMESTAMP_KEY, String.valueOf(new Date().getTime()));
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        Intent intent = getIntent();
        if (intent.getExtras().getString("test") != null) {
            chatMessageService.addMessage(newMessage);
        } else {
            chatMessageService.addMessage(newMessage).addOnSuccessListener(result -> {
                messageView.setText("");
                TextView chatView = findViewById(R.id.chat);
                //chatView.setText(""); // clear view first before update
                //initMessageUpdateListener();

            }).addOnFailureListener(error -> {
                Log.e(TAG, error.getLocalizedMessage());
            });
        }
    }

    private void initMessageUpdateListener() {
        TextView chatView = findViewById(R.id.chat);
        chatMessageService.addOrderedMessagesListener(
                chatMessagesList -> {
                    Log.d(TAG, "msg list size:" + chatMessagesList.size());
                    chatMessagesList.forEach(chatMessage -> {
                        chatView.append(chatMessage.toString());
                    });
                });
    }
}