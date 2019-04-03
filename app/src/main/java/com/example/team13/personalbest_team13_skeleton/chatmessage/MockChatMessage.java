package com.example.team13.personalbest_team13_skeleton.chatmessage;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MockChatMessage implements ChatMessageService {
    Map<String, String> messageMap = new HashMap<>();
    public ArrayList<String> display = new ArrayList<>();
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    public Task<?> addMessage(Map<String, String> data) {
        messageMap = data;
        display.add(messageMap.get(TEXT_KEY));
        return null;
    }

    public void addOrderedMessagesListener(Consumer<List<ChatMessage>> listener) {
        //display.add(messageMap.get(TEXT_KEY));
    }

    public void getChatList(Consumer<List<String>> listener) {

    }

    public String getContent() {
        return display.get(0);
    }
}
