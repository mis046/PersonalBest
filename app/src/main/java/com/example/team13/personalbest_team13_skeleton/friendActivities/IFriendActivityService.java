package com.example.team13.personalbest_team13_skeleton.friendActivities;

import java.util.function.Consumer;

public interface IFriendActivityService {
    void addData(String date, int totalSteps, int plannedSteps, int goal);
    void getActivities(String userEmail, Consumer<int[][]> listener);
    void setUser(String userEmail);
}
