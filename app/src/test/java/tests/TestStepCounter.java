package tests;

import android.util.Log;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.R;
import com.example.team13.personalbest_team13_skeleton.TimeMachine;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * General tests for the step counter.
 */
@RunWith(RobolectricTestRunner.class)
public class TestStepCounter {
    private MainActivity testMainActivity;
    private long currentStepCount;

    @Before
    public void init() {
//        testMainActivity = Robolectric.setupActivity(MainActivity.class);
//        currentStepCount = 1234;
    }

    /**
     * Test if the step count view is displayed.
     */
    @Test
    public void testStepCountIsDisplayed() {
//        final TextView viewStepCounter = testMainActivity.findViewById(R.id.textSteps);
//        assertEquals(true, viewStepCounter.isShown());
    }

    /**
     * Make sure the step counter view resets to 0 at the end of the day.
     */
    @Test
    public void testResetStepCounter() {
//        // Get time midnight.
//        LocalDateTime dummyTime = LocalDateTime.of(2018, 2, 7, 23, 59);
//        TextView viewStepCounter = testMainActivity.findViewById(R.id.textSteps);
//
//        // Set the clock to this time.
//        TimeMachine.useFixedClockAt(dummyTime);
    }
}
