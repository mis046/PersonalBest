package com.example.team13.personalbest_team13_skeleton.fitness;

import android.util.Log;

import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.Person;

import java.util.ArrayList;
import java.util.Collection;

public class FitnessMock implements FitnessService {
    public final String TAG = "FitnessMock";
    public static final int STEP_INC = 500;

    private Person person;
    private MainActivity activity;
    private Collection<FitnessObserver> observers;

    public FitnessMock(MainActivity activity, Person person) {
        this.person = person;
        this.activity = activity;
        observers = new ArrayList<FitnessObserver>();
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateStepCount() {
        int total = person.getTotalSteps();
        person.setTotalSteps(total);
        Log.d(TAG, "update step count");
        //activity.setStepCount(total);
        notifyRegisteredDevices(total);
    }

    public void incrementStepsBy500() {
        int total = person.getTotalSteps() + FitnessMock.STEP_INC;
        person.setTotalSteps(total);
        //activity.setStepCount(total);
        notifyRegisteredDevices(total);
    }

    public void notifyRegisteredDevices(int steps) {
        for (FitnessObserver o : observers) {
            Log.d(TAG, "notifiy obj");
            o.stepsUpdated(steps);
        }
    }

    @Override
    public void getWeekData() {

    }

    @Override
    public void getYesterdaysSteps() {

    }

    @Override
    public void register(FitnessObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(FitnessObserver observer) {

    }
}
