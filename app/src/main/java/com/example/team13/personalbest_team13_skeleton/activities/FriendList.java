package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.DataIOStream;
import com.example.team13.personalbest_team13_skeleton.friendlist.FirestoreFriendListAdapter;
import com.example.team13.personalbest_team13_skeleton.R;
import com.example.team13.personalbest_team13_skeleton.RecyclerViewAdapter;
import com.example.team13.personalbest_team13_skeleton.friendlist.MockFriendListService;
import com.example.team13.personalbest_team13_skeleton.friendlist.friendListService;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;


public class FriendList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userEmail;    // current user's email address

    private static final String FRIEND_KEY = "friend";
    private static final String SUCCESS_KEY = "success";

    public static final String FRIENDS_LIST_SERVICE_EXTRA = "FRIEND_LIST_SERVICE";
    private static final String TAG = FriendList.class.getSimpleName();

    private String[] friendlistdata = null;
    public friendListService friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendslist);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        this.userEmail = intent.getExtras().getString("EMAIL");

        // specify an adapter (see also next example)
        // String[] myDataset = new String[]{"Mingqi", "Allen","Mingqi", "Allen","Mingqi", "Allen", "Mingqi", "Allen", "Mingqi", "Allen", "Mingqi", "Allen", "Nate"};

        // userEmail = getFromGoogleSignIn;

        String stringExtra = getIntent().getStringExtra(FRIENDS_LIST_SERVICE_EXTRA);
        // friends = friendListServiceFactory.getInstance(userEmail).getOrDefault(stringExtra, FirestoreFriendListAdapter::getInstance);
        if (stringExtra.equals("test")) {
            friends = new MockFriendListService(userEmail);
        } else {
            friends = new FirestoreFriendListAdapter(userEmail);
        }

        findViewById(R.id.add_btn).setOnClickListener(view -> addFriend());

        initFriendListUpdateListener();

        DataIOStream dataIOStream = new DataIOStream(this.getApplicationContext(), userEmail);
        dataIOStream.setHasFriend(this.friendlistdata != null && this.friendlistdata.length != 0);
        //Log.d(TAG, "this.friendlistdata length: "+this.friendlistdata.length);

    }

    // add friend
    private void addFriend() {
        EditText editFriendEmail = findViewById(R.id.addFriendInput);

        if (editFriendEmail.getText().toString() == null || editFriendEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Input email address", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> friendRequest = new HashMap<>();
        friendRequest.put(FRIEND_KEY, editFriendEmail.getText().toString());
        friendRequest.put(SUCCESS_KEY, "false");

        friends.addFriend(friendRequest, editFriendEmail);
        editFriendEmail.setText("");
    }

    public void initFriendListUpdateListener() {
        Log.e(TAG, "updating view");
        recyclerView.setAdapter(mAdapter);
        friends.getFriends(userEmail,
                realDBArr -> {
                    Log.d(TAG, "msg list size:" + realDBArr.size());
                    String[] temp = realDBArr.toArray(new String[realDBArr.size()]);
                    updateFriendListData(temp);
                    mAdapter = new RecyclerViewAdapter(temp, this);
                    recyclerView.setAdapter(mAdapter);

                    DataIOStream dataIOStream = new DataIOStream(this.getApplicationContext(), userEmail);
                    dataIOStream.setHasFriend(temp != null && temp.length != 0);
                    Log.d(TAG, "temp length: "+temp.length);
                });
    }

    public void openFriendAvtivities(String friendEmail) {
        Intent intent = new Intent(this, ViewFriendActivities.class);
        intent.putExtra("friendEmail", friendEmail);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("test", false);
        this.startActivity(intent);

        //Toast.makeText(this, friendEmail, Toast.LENGTH_LONG).show();
    }

    private void updateFriendListData(String[] friendlist) {
        this.friendlistdata = friendlist;
    }

    public String[] getFriendlistdata() {
        return this.friendlistdata;
    }
    // ...
}
