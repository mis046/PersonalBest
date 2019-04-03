package com.example.team13.personalbest_team13_skeleton;

import android.content.Intent;

import com.example.team13.personalbest_team13_skeleton.activities.StackedBarChartActivity;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class DataVisualizationTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final String TEST_EMAIL = "test@email.com";

    private MainActivity activity;

    private DataIOStream dataIOStream;
    private StackedBarChartActivity stackedBarChartActivity;
    @Before
    public void setUp() throws Exception {

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
    }

    // BDD Scenario test: Check weekly progress.
    @Test
    public void testWeeklyProgress() {
        // Build a data stream with dummy email.
        dataIOStream = new DataIOStream(activity, TEST_EMAIL);

        // Give the user some general walk data for 8 days.
        dataIOStream.saveDailyTotalSteps(3000);
        dataIOStream.saveDailyTotalSteps(4500);
        dataIOStream.saveDailyTotalSteps(4123);
        dataIOStream.saveDailyTotalSteps(1000);
        dataIOStream.saveDailyTotalSteps(6000);
        dataIOStream.saveDailyTotalSteps(3300);
        dataIOStream.saveDailyTotalSteps(300);
        dataIOStream.saveDailyTotalSteps(100);

        // Give the user some planned steps for the last 2 days.
        dataIOStream.saveDailyPlannedSteps(3000);
        dataIOStream.saveDailyPlannedSteps(4500);

        // Build a mock fitness to get generate some fake data.
        Person person = new Person(TEST_EMAIL);
        FitnessMock fitnessService = new FitnessMock(activity, person);

        fitnessService.getWeekData(); // get week data into sharedPref "week_progress", stored as (k(string),v(int)): e.g. "Sunday":41
        Intent intent = new Intent(activity, StackedBarChartActivity.class);
        intent.putExtra("currentTotalSteps", person.getTotalSteps());
        intent.putExtra("currentPlannedSteps", person.getPlannedSteps());
        intent.putExtra("currentGoal", person.getGoal());
        intent.putExtra("EMAIL", person.getEmailAcc());
        ActivityController<StackedBarChartActivity> controller = Robolectric.buildActivity(StackedBarChartActivity.class, intent);
        ActivityController<StackedBarChartActivity> mockChartActivity = spy(controller);
        StackedBarChartActivity act = mockChartActivity.create().get();

        verify(mockChartActivity, times(1)).create();

    }
}
