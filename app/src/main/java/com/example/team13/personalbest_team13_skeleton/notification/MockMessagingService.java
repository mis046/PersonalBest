package com.example.team13.personalbest_team13_skeleton.notification;

import android.content.Context;

import com.example.team13.personalbest_team13_skeleton.MainActivity;

public class MockMessagingService implements NotificationService{
    String TAG = MainActivity.class.getSimpleName();
    String document_key;

    public String subscribeToNotificationsTopic(String documentKey, Context context) {
        document_key = documentKey;
        String msg = "Subscribed to notifications " + document_key;
        return msg;
    }
}
