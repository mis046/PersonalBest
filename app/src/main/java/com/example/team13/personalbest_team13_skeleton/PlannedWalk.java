package com.example.team13.personalbest_team13_skeleton;

import java.time.ZoneId;

public class PlannedWalk {
    private int firstStep;
    private int endSteps;
    private float strideLength;
    private float distance;
    private float speed;
    private long startTime;
    private long elapsedTime;
    private int walkedSteps;

    public PlannedWalk() {
    }

    public PlannedWalk(float strideLength) {
        this.strideLength = strideLength;
    }

    // Copy constructor.
    public PlannedWalk(PlannedWalk plannedWalk) {
        // We don't need all these
        this.firstStep = plannedWalk.firstStep;
        //this.endSteps = plannedWalk.endSteps;
        this.strideLength = plannedWalk.strideLength;
        //this.distance = plannedWalk.distance;
        //this.speed = plannedWalk.speed;
        this.startTime = plannedWalk.startTime;
        //this.elapsedTime = plannedWalk.elapsedTime;
        //this.walkedSteps = plannedWalk.walkedSteps;
    }

    public void beginWalk(int firstStep){
        this.firstStep = firstStep;
        startTime = TimeMachine.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void endWalk(int lastStep){
        long endTime = TimeMachine.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        elapsedTime = endTime - startTime;
        walkedSteps = lastStep - firstStep;
        setDistance();
        setSpeed();
    }

    public void setDistance(){
        distance = walkedSteps * strideLength / 12;
    }

    public void setSpeed(){
        if (elapsedTime > 0) {
            speed = distance / ((float) elapsedTime / 1000);
        } else {
            speed = 0;
        }
    }

    public int getSteps(){
        return this.walkedSteps;
    }

    public float getDistance(){
        return this.distance;
    }

    public float getSpeed(){
        return this.speed;
    }

    public long getTime(){
        return this.elapsedTime / 1000;
    }
}
