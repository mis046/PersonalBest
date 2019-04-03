package com.example.team13.personalbest_team13_skeleton.friendlist;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MockFriendListService implements friendListService {

    HashMap<String, HashSet<String>> db = new HashMap<>();
    private static final String FRIEND_KEY = "friend";
    private String userEmail;

    public MockFriendListService(String userEmail) {
        this.userEmail = userEmail;
    }
    public void addFriend(Map<String, String> friendRequest, EditText editFriendEmail) {
        if (db.get(userEmail) == null) {
            db.put(userEmail, new HashSet<String>());
        }
        db.get(userEmail).add(friendRequest.get(FRIEND_KEY));
    }

    public void getFriends(String userEmail, Consumer<List<String>> listener) {
        ArrayList<String> realDatasets = new ArrayList<>();
        if (db.get(userEmail) != null) {
            for (String friendEmail: db.get(userEmail)) {
                if (checkFriends(userEmail, friendEmail)) {
                    realDatasets.add(friendEmail);
                }
            }
            listener.accept(realDatasets);
        }
    }

    public boolean checkFriends(String userEmail, String friendEmail) {
        if (db != null && db.get(userEmail) != null && db.get(friendEmail) != null) {
            return db.get(userEmail).contains(friendEmail) && db.get(friendEmail).contains(userEmail);
        } else {
            return false;
        }
    }

    public void setUser(String userEmail) {
        this.userEmail = userEmail;
    }
}
