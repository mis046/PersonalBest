package com.example.team13.personalbest_team13_skeleton.fitness;

import com.example.team13.personalbest_team13_skeleton.observer.ISubject;

public interface FitnessService extends ISubject<FitnessObserver> {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void getWeekData();
    void getYesterdaysSteps();
}
