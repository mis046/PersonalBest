package com.example.team13.personalbest_team13_skeleton.friendActivities;

import java.util.HashMap;
import java.util.function.Consumer;

public class MockFriendActivity implements IFriendActivityService {
    HashMap<String, int[]> data;
    public MockFriendActivity() {
        data = new HashMap<>();
    }

    public void addData(String date, int totalSteps, int plannedSteps, int goal) {
        int[] entry = new int[] {totalSteps, plannedSteps, goal};
        data.put(date, entry);
    }
    public void getActivities(String userEmail, Consumer<int[][]> listener) {
        int[][] result = new int[3][28];
        for (int i = 0; i < 28; i++) {
            int[] entry = data.getOrDefault(Integer.toString(28-i), new int[]{0, 0, 0});
            result[0][i] = entry[0];
            result[1][i] = entry[1];
            result[2][i] = entry[2];
        }
        listener.accept(result);
    }
    public void setUser(String userEmail) {
    }
}
