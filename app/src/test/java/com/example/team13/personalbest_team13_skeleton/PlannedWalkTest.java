package com.example.team13.personalbest_team13_skeleton;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
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

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class PlannedWalkTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private Handler handle;

    private MainActivity activity;
    private TextView textSteps;
    private Button btnUpdateSteps;
    private long nextStepCount;
    private Button btnWalk;
    private Button btnEndWalk;

    @Before
    public void setup(){
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity);
            }
        });

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        textSteps = activity.findViewById(R.id.textSteps);
        btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
        nextStepCount = 1337;
        handle = new Handler();
        btnWalk = activity.findViewById(R.id.startWalkButton);
        btnEndWalk = activity.findViewById(R.id.endWalkButton);
    }

    /**
     * updating steps
     */
    @Test
    public void testUpdateStepsButton() {
//        assertEquals("0", textSteps.getText().toString());
//        btnUpdateSteps.performClick();
//        assertEquals("1337", textSteps.getText().toString());
    }

    /**
     * walk button should disappear after tapped
     */
    public void startWalk(){
        /*
        Button startBtn = activity.findViewById(R.id.startWalkButton);
        startBtn.performClick();
        assertEquals(startBtn.getVisibility(), View.INVISIBLE);
        */
    }

    /**
     * End walk button should disappear after tapped
     */
    /*@Test
    public void endWalk(){
        Button startBtn = activity.findViewById(R.id.endWalkButton);
        startBtn.performClick();
        assertEquals(startBtn.getVisibility(), View.INVISIBLE);
    }*/

    /**
     * user starts walk but ends walk without taking any steps
     */
    @Test
    public void noStats(){
//        btnWalk.performClick();
//        btnEndWalk.performClick();
//        assertEquals(0, activity.getDistance(), .1);
//        assertEquals(0, activity.getSpeed(), .1);
    }

    /**
     * distance should be derived from number of steps
     * user walked 1337 without recording, then walks another 1337 steps while recording
     * user walked 1337 steps = 3075.1 ft = 0.5824 miles
     */
    @Test
    public void testDistance(){
//        btnUpdateSteps.performClick();
//        double dist = Double.parseDouble(textSteps.getText().toString());
//        btnWalk.performClick();
//        nextStepCount *= 2;
//        btnEndWalk.performClick();
//        double actualDistance = dist * 29;// / 5820.0;
//        assertEquals(actualDistance, activity.getDistance(), .01);
    }

    /**
     * speed should be derived from distance and time
     * user walked 1337 steps in 10 seconds
     * speed should be 1337 * 29 / 12 / 10 = 307.51 ft/sec
     */
    @Test
    public void testSpeed(){
//        btnUpdateSteps.performClick();
//        long startTime = System.currentTimeMillis();
//        btnWalk.performClick();
//        try{
//            TimeUnit.SECONDS.sleep(10);
//        } catch(Exception ignored){
//        }
//        nextStepCount *= 2;
//        btnEndWalk.performClick();
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = (endTime - startTime) / 1000;
//        double actualSpeed = 1337 * 29 / elapsedTime;
//        assertEquals(actualSpeed, activity.getSpeed(), 20);
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;

        public TestFitnessService(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
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
            mainActivity.setStepCount(nextStepCount);
        }

        @Override
        public void getWeekData(){
        }

        @Override
        public void register(FitnessObserver observer) {

        }

        @Override
        public void unregister(FitnessObserver observer) {

        }
    }

}//end planned walk test class
