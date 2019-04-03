package com.example.team13.personalbest_team13_skeleton.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.team13.personalbest_team13_skeleton.TimeMachine;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

// https://stackoverflow.com/questions/23586031/calling-activity-class-method-from-service-class
/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * Resets the congratulations message at the end of the day.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DailyService extends Service {
    public static final String TAG = "DailyService";
    private final IBinder binder = new LocalBinder();
    private DailyCallbacks dailyCallbacks;

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public DailyService getService() {
            // Return this instance of MyService so clients can call public methods
            return DailyService.this;
        }
    }

    // We need this ctor for weird edge case?
    public DailyService() {}

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "In onBind");
        return binder;
    }

    // Connect callback to MainActivity.
    public void setCallbacks(DailyCallbacks callbacks) {
        this.dailyCallbacks = callbacks;
    }

    // Start our infinite thread.
    public void run() {
        if (dailyCallbacks != null) {
            Thread thread = new Thread(new MyThread(dailyCallbacks, 10));
            thread.start();
        }
    }

    /**
     * Determine if we need to perform some daily service to call back to MainActivity.
     */
    private boolean checkDailyService() {
        int currentTime = TimeMachine.now().toLocalTime().toSecondOfDay();
        int pastTime = TimeMachine.pastNow().toLocalTime().toSecondOfDay();

        if (currentTime < pastTime) {
            Log.d(TAG, "Trigger daily service");
            adjustPastClock();
            return true;
        }
        return false;
    }

    /**
     * Reset the past clock when we have a new day.
     */
    private void adjustPastClock() {
        LocalDateTime time = TimeMachine.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Clock newPastClock = Clock.fixed(time.atZone(zoneId).toInstant(), zoneId);
        TimeMachine.setPastTime(newPastClock);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.d(TAG, "In on start command");
        this.run();
        return super.onStartCommand(intent, flags, startID);
    }

    final class MyThread implements Runnable {
        private int startId; // Use this id so we can close the service later if we want to.
        private DailyCallbacks dailyCallbacks;
        public MyThread(DailyCallbacks dailyCallbacks, int startId) {
            this.startId = startId;
            this.dailyCallbacks = dailyCallbacks;
        }
        @Override
        public void run() {
            synchronized (this) {
                while (true) {
                    try {
                        if (dailyCallbacks == null) {
                            Log.d(TAG, "callback is null");
                            // TODO(Nate): Check if this thread is closed out properly when a callback is null.
                        } else {
                            Log.d(TAG, "Perform callback");
                            if (checkDailyService())
                                dailyCallbacks.performDailyAction();
                        }
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //stopSelf(startId);
            }
        }
    }
}
