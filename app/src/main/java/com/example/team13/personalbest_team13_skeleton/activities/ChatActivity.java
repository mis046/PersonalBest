package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.ChatRecyclerViewAdapter;
import com.example.team13.personalbest_team13_skeleton.R;
import com.example.team13.personalbest_team13_skeleton.RecyclerViewAdapter;
import com.example.team13.personalbest_team13_skeleton.chatmessage.ChatMessage;
import com.example.team13.personalbest_team13_skeleton.chatmessage.ChatMessageService;
import com.example.team13.personalbest_team13_skeleton.chatmessage.FirestoreChatAdapter;
import com.example.team13.personalbest_team13_skeleton.friendlist.FirestoreFriendListAdapter;
import com.example.team13.personalbest_team13_skeleton.friendlist.MockFriendListService;
import com.example.team13.personalbest_team13_skeleton.friendlist.friendListService;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userEmail;    // current user's email address

    //public static final String FRIENDS_LIST_SERVICE_EXTRA = "FRIEND_LIST_SERVICE";
    private static final String TAG = ChatActivity.class.getSimpleName();

    private String[] chatlistdata = null;
    public ChatMessageService chatMessageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "In onCreate");
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        this.userEmail = intent.getExtras().getString("EMAIL");

        //String stringExtra = getIntent().getStringExtra(FRIENDS_LIST_SERVICE_EXTRA);
        // friends = friendListServiceFactory.getInstance(userEmail).getOrDefault(stringExtra, FirestoreFriendListAdapter::getInstance);
        //if (stringExtra.equals("test")) {
        //    friends = new MockFriendListService(userEmail);
        //} else {
        chatMessageService = new FirestoreChatAdapter(userEmail);
        //}

        initFriendListUpdateListener();
    }

    public void initFriendListUpdateListener() {
        Log.e(TAG, "updating view");
        recyclerView.setAdapter(mAdapter);
        chatMessageService.getChatList(
                realDBArr -> {
                    Log.d(TAG, "msg list size:" + realDBArr.size());
                    String[] temp = realDBArr.toArray(new String[realDBArr.size()]);
                    updateFriendListData(temp);
                    Log.d(TAG, "hello: " + temp[0]);
                    mAdapter = new ChatRecyclerViewAdapter(temp, this);
                    recyclerView.setAdapter(mAdapter);
                });
    }

    public void openChatBox(String friendEmail) {
        Intent intent = new Intent(this, ChatBoxActivity.class);
        intent.putExtra("friendEmail", friendEmail);
        intent.putExtra("userEmail", userEmail);
        this.startActivity(intent);

        //Toast.makeText(this, friendEmail, Toast.LENGTH_LONG).show();
    }


    private void updateFriendListData(String[] chatList) {
        this.chatlistdata = chatList;
    }

    public String[] getFriendlistdata() {
        return this.chatlistdata;
    }
    // ...*/
}
