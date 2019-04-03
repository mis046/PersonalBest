package com.example.team13.personalbest_team13_skeleton.notification;

import android.content.Context;

import com.google.android.gms.tasks.Task;

import java.util.function.Consumer;

public interface NotificationService {
    String subscribeToNotificationsTopic(String topic, Context context);
}
