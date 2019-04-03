package com.example.team13.personalbest_team13_skeleton.fitness;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.example.team13.personalbest_team13_skeleton.MainActivity;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, MainActivity stepCountActivity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(stepCountActivity);
    }

    public interface BluePrint {
        FitnessService create(MainActivity stepCountActivity);
    }
}
