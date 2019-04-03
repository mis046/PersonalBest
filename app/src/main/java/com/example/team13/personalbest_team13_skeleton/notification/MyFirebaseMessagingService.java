package com.example.team13.personalbest_team13_skeleton.notification;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        need to implement this if you want to do something when you receive a notification while app is in the foreground.

        Log.d(TAG, "In onMessageReceived");

//        String click_action = remoteMessage.getNotification().getClickAction();
//        if (click_action != null) {
//            Log.d(TAG, "click action not null: " + click_action);
//            Intent intent = new Intent(click_action);
//            startActivity(intent);
//        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, remoteMessage.getNotification().getTitle());
            Log.d(TAG, remoteMessage.getNotification().getBody());
            Log.d(TAG, remoteMessage.getData().get("click_action"));
        }
    }
}
