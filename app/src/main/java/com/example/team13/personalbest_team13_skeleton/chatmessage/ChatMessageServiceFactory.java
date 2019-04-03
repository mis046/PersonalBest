package com.example.team13.personalbest_team13_skeleton.chatmessage;


import com.example.team13.personalbest_team13_skeleton.friendlist.Factory;

public class ChatMessageServiceFactory extends Factory<ChatMessageService> {
    private static ChatMessageServiceFactory instance;

    public static ChatMessageServiceFactory getInstance() {
        if (instance == null) {
            instance = new ChatMessageServiceFactory();
        }
        return instance;
    }

}
