package com.example.team13.personalbest_team13_skeleton.observer;

public interface ISubject<ObserverT> {
    /**
     * Register a new listener.
     */
    void register(ObserverT observer);

    /**
     * Unregister a listener.
     */
    void unregister(ObserverT observer);
}
