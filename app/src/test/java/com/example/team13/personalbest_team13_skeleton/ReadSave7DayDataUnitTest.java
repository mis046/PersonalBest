package com.example.team13.personalbest_team13_skeleton;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.fitness.FitnessObserver;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessService;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessServiceFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class ReadSave7DayDataUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;

    private DataIOStream dataIOStream;

    @Before
    public void setUp() throws Exception {

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

    }

    @Test
    public void testTotalSteps() {
        dataIOStream = new DataIOStream(activity, "example@email.com");

        dataIOStream.saveDailyTotalSteps(3000);
        dataIOStream.saveDailyTotalSteps(4500);

        dataIOStream = new DataIOStream(activity, "email@example.com");

        dataIOStream.saveDailyTotalSteps(3000);
        dataIOStream.saveDailyTotalSteps(4500);
        dataIOStream.saveDailyTotalSteps(4123);
        dataIOStream.saveDailyTotalSteps(1000);
        dataIOStream.saveDailyTotalSteps(6000);
        dataIOStream.saveDailyTotalSteps(3300);
        dataIOStream.saveDailyTotalSteps(300);
        dataIOStream.saveDailyTotalSteps(100);

        dataIOStream = new DataIOStream(activity, "example@email.com");
        int[] result1 = dataIOStream.readPast7DaysTotalSteps();
        int[] expected1 = new int[]{4500, 3000, 0, 0, 0, 0, 0};

        dataIOStream = new DataIOStream(activity, "email@example.com");
        int[] result2 = dataIOStream.readPast7DaysTotalSteps();
        int[] expected2 = new int[]{100, 300, 3300, 6000, 1000, 4123, 4500};



        for (int i = 0; i < result1.length; i++) {
            assert(result1[i] == expected1[i]);
            assert(result2[i] == expected2[i]);
        }
    }

    @Test
    public void testPlannedSteps() {
        dataIOStream = new DataIOStream(activity, "example@email.com");

        dataIOStream.saveDailyPlannedSteps(3000);
        dataIOStream.saveDailyPlannedSteps(4500);

        dataIOStream = new DataIOStream(activity, "email@example.com");

        dataIOStream.saveDailyPlannedSteps(3000);
        dataIOStream.saveDailyPlannedSteps(4500);
        dataIOStream.saveDailyPlannedSteps(4123);
        dataIOStream.saveDailyPlannedSteps(1000);
        dataIOStream.saveDailyPlannedSteps(6000);
        dataIOStream.saveDailyPlannedSteps(3300);
        dataIOStream.saveDailyPlannedSteps(300);
        dataIOStream.saveDailyPlannedSteps(100);

        dataIOStream = new DataIOStream(activity, "example@email.com");
        int[] result1 = dataIOStream.readPast7DaysPlannedSteps();
        int[] expected1 = new int[]{4500, 3000, 0, 0, 0, 0, 0};

        dataIOStream = new DataIOStream(activity, "email@example.com");
        int[] result2 = dataIOStream.readPast7DaysPlannedSteps();
        int[] expected2 = new int[]{100, 300, 3300, 6000, 1000, 4123, 4500};



        for (int i = 0; i < result1.length; i++) {
            assert(result1[i] == expected1[i]);
            assert(result2[i] == expected2[i]);
        }
    }
}