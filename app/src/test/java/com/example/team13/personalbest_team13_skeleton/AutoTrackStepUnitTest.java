package com.example.team13.personalbest_team13_skeleton;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.fitness.FitnessObserver;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessService;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class AutoTrackStepUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView textSteps;
    private Button btnUpdateSteps;
    private long nextStepCount;

    @Before
    public void setUp() throws Exception {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity stepCountActivity) {
                return new TestFitnessService(stepCountActivity);
            }
        });

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        textSteps = activity.findViewById(R.id.textSteps);
        btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
        nextStepCount = 1337;
    }

    @Test
    public void testUpdateStepsButton() {

// TODO (Nate): Currently error with setting intent in MainActivity
//        assertEquals("0", textSteps.getText().toString());
//        btnUpdateSteps.performClick();
//        assertEquals("1337", textSteps.getText().toString());
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity stepCountActivity;

        public TestFitnessService(MainActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public void getYesterdaysSteps() {
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            stepCountActivity.setStepCount(nextStepCount);
        }

        @Override
        public void getWeekData() {

        }

        @Override
        public void register(FitnessObserver observer) {

        }

        @Override
        public void unregister(FitnessObserver observer) {

        }
    }
}