package com.example.team13.personalbest_team13_skeleton.notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseCloudMessagingAdapter implements NotificationService {
    private final String TAG = FirebaseCloudMessagingAdapter.class.getSimpleName();

    public String subscribeToNotificationsTopic(String documentKey, Context context) {
        FirebaseMessaging.getInstance().subscribeToTopic(documentKey)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                );
        return new String("In firebase");
    }
}
