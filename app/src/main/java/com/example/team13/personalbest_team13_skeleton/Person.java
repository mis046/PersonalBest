package com.example.team13.personalbest_team13_skeleton;

import android.util.Log;


public class Person {

    public static final String TAG = "Person";

    public static final float DEFAULT_HEIGHT_INCHES = 72;
    public static final float DEFAULT_STRIDE_INCHES = 29;
    public static final int DEFAULT_GOAL = 5000;
    public static final float HEIGHT_TO_STRIDE = 0.415f;

    private int totalSteps;
    private int plannedSteps;
    private float height;
    private float strideLen;
    private String email;
    private int goal;
    private boolean onAWalk; // Determine if we are on a walk.
    private boolean isWalker;
    private PlannedWalk plannedWalk;

    public Person(String email) {
        // set everything to default
        this.totalSteps = 0;
        this.plannedSteps = 0;
        this.height = DEFAULT_HEIGHT_INCHES;
        this.strideLen = DEFAULT_STRIDE_INCHES;
        this.email = email;
        this.goal = DEFAULT_GOAL;
        this.isWalker = true;
        this.plannedWalk = new PlannedWalk();
        this.onAWalk = false;
    }

    public Person(String email, int ts, int ps, int goal, float height, float stride, boolean isWalker) {
        // set everything to default
        this.totalSteps = ts;
        this.plannedSteps = ps;
        this.height = height;
        this.strideLen = stride;
        this.email = email;
        this.goal = goal;
        this.isWalker = isWalker;
        this.plannedWalk = new PlannedWalk(strideLen);
        this.onAWalk = false;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getPlannedSteps() {
        return plannedSteps;
    }

    public void setPlannedSteps(int plannedSteps) {
        this.plannedSteps = plannedSteps;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        updateStrideLen();
    }

    public float getStrideLen() {
        return strideLen;
    }

    public void updateStrideLen() {
        this.strideLen = heightToStride(this.height);
    }

    public String getEmailAcc() {
        return email;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public boolean isWalker() {
        return isWalker;
    }

    public void setWalker(boolean walker) {
        isWalker = walker;
    }

    public static float heightToStride(float h) {
        return HEIGHT_TO_STRIDE * h;
    }

    public void startPlannedWalk() {
        onAWalk = true;
        plannedWalk = new PlannedWalk(strideLen);
        plannedWalk.beginWalk(getTotalSteps());
    }

    public void endWalk() {
        onAWalk = false;
        plannedWalk.endWalk(getTotalSteps());
        plannedSteps += plannedWalk.getSteps();
    }

    // Get the most recent completed planned walk.
    public PlannedWalk getPlannedWalk() {
        Log.d(TAG, "getPlannedWalk");
        if (onAWalk) {
            Log.d(TAG, "Getting plannedWalk for person that is on planned walk");
            // XXX: Make sure to not call getter or get infinite recursion.
            PlannedWalk plannedWalkClone = new PlannedWalk(this.plannedWalk);
            plannedWalkClone.endWalk(getTotalSteps());
            return plannedWalkClone;
        } else {
            return plannedWalk;
        }
    }
}
