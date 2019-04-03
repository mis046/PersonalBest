package com.example.team13.personalbest_team13_skeleton;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Object used to controll time throughout the entire application.
 */
public class TimeMachine {
    private static Clock clock = Clock.systemDefaultZone();
    private static Clock pastClock = Clock.offset(Clock.systemDefaultZone(), Duration.ofMillis(-10));

    private static ZoneId zoneId = ZoneId.systemDefault();

    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public static LocalDateTime pastNow() {
        return LocalDateTime.now(getPastClock());
    }

    public static void useFixedClockAt(LocalDateTime date){
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
        setPastTime(Clock.offset(getClock(), Duration.ofMinutes(-61)));
    }

    public static void setPastTime(Clock clock) {
        //pastClock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
        pastClock = clock;
    }

    // Move the clock forward one hour.
    public static void passOneHour() {
        useFixedClockAt(now().plusHours(1));
    }

    // Reset the clock to this clock.
    public static void useSystemDefaultZoneClock(){
        clock = Clock.systemDefaultZone();
        pastClock = Clock.systemDefaultZone();
    }

    private static Clock getClock() {
        return clock ;
    }

    private static Clock getPastClock() {
        return pastClock ;
    }
}

