package com.example.team13.personalbest_team13_skeleton.friendlist;

import android.widget.EditText;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface friendListService {
    void addFriend(Map<String, String> friendRequest, EditText editFriendEmail);
    void getFriends(String userEmail, Consumer<List<String>> listener);
    void setUser(String userEmail);
}
