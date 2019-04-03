package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.SharedPreferences;

public class DataIOStream {
    public static final String STR_GOAL = "goal";
    public static final String STR_TOTAL_STEPS = "total_steps";
    public static final String STR_PLANNED_STEPS = "planned_steps";
    public static final String STR_HEIGHT = "height";
    public static final String STR_HAS_FRIEND = "past_planned_steps";
    public static final String STR_STRIDE_LEN = "stride_len";
    public static final String STR_IS_WALKER = "is_walker";
    public static final String STR_SHOWN_CONGRATS = "shown_congrats";
    public static final String STR_SHOWN_ENCOURAG = "shown_encouragement";
    public static final String PAST_TOTAL = "past_total_steps";
    public static final String PAST_PLANNED = "past_planned_steps";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String email;

    public DataIOStream(Context context, String email) {
        this.email = email;
        sharedPreferences = context.getSharedPreferences(email, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void savePerson(Person person) {
        editor.putInt(STR_GOAL, person.getGoal());
        editor.putFloat(STR_HEIGHT, person.getHeight());
        editor.putFloat(STR_STRIDE_LEN, person.getStrideLen());
        editor.putInt(STR_TOTAL_STEPS, person.getTotalSteps());
        editor.putInt(STR_PLANNED_STEPS, person.getPlannedSteps());
        editor.putBoolean(STR_IS_WALKER, person.isWalker());
        editor.apply();
    }

    public Person loadPerson() {
        int ts = sharedPreferences.getInt(STR_TOTAL_STEPS, 0);
        int ps = sharedPreferences.getInt(STR_PLANNED_STEPS, 0);
        int goal = sharedPreferences.getInt(STR_GOAL, Person.DEFAULT_GOAL);
        float height = sharedPreferences.getFloat(STR_HEIGHT, Person.DEFAULT_HEIGHT_INCHES);
        float stride = sharedPreferences.getFloat(STR_STRIDE_LEN, Person.DEFAULT_STRIDE_INCHES);
        boolean isWalker = sharedPreferences.getBoolean(STR_IS_WALKER, true);

        Person person = new Person(email, ts, ps, goal, height, stride, isWalker);
        return person;
    }

    public void saveFlag(String flagName, boolean flag) {
        editor.putBoolean(flagName, flag);
        editor.apply();
    }

    public boolean loadFlag(String flagName) {
        return sharedPreferences.getBoolean(flagName, false);
    }

    public void saveDailyTotalSteps(int todaySteps) {
        // shift out the oldest and insert at newest
        // past 7, 6, 5, 4, 3, 2, 1, today
        // -> 6, 5, 4, 3, 2, 1, today, new day
        for (int i = 7; i > 1; i --) {
            editor.putInt(PAST_TOTAL + i, getPastTotalSteps(i-1));
        }
        editor.putInt(PAST_TOTAL + 1, todaySteps);
        editor.apply();
    }
    /*
        Returns an int array of size 7.
        0 is the latest day - past 1 day
        6 is the earliest day - past 7 day
     */
    public int[] readPast7DaysTotalSteps() {
        int[] pastTotalSteps = new int[7];
        for (int i = 0; i < 7; i++) {
            pastTotalSteps[i] = getPastTotalSteps(i+1);
        }
        return pastTotalSteps;
    }
    /*
        Past 1 day = yesterday
     */
    private int getPastTotalSteps(int pastDays) {
        return sharedPreferences.getInt(PAST_TOTAL + pastDays, 0);
    }


    public void saveDailyPlannedSteps(int todaySteps) {
        for (int i = 7; i > 1; i --) {
            editor.putInt(PAST_PLANNED + i, getPastPlannedSteps(i-1));
        }
        editor.putInt(PAST_PLANNED + 1, todaySteps);
        editor.apply();
    }
    public int[] readPast7DaysPlannedSteps() {
        int[] pastPlannedSteps = new int[7];
        for (int i = 0; i < 7; i++) {
            pastPlannedSteps[i] = getPastPlannedSteps(i+1);
        }
        return pastPlannedSteps;
    }
    private int getPastPlannedSteps(int pastDays) {
        return sharedPreferences.getInt(PAST_PLANNED + pastDays, 0);
    }


    // Reset all flags
    public void resetFlags() {
        saveFlag(STR_SHOWN_CONGRATS, false);
        saveFlag(STR_SHOWN_ENCOURAG, false);
    }

    // Reset the person here
    public void resetPersonData() {
        Person person = loadPerson();
        Person resetedPerson = new Person(person.getEmailAcc());
        resetedPerson.setHeight(person.getHeight());
        resetedPerson.setWalker(person.isWalker());
        resetedPerson.setGoal(person.getGoal());
        savePerson(resetedPerson);
    }

    public boolean getHasFriend() {
        return loadFlag(STR_HAS_FRIEND);

    }

    public void setHasFriend(boolean hasFriend){
        saveFlag(STR_HAS_FRIEND, hasFriend);
    }
}
