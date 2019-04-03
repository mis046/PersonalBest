package com.example.team13.personalbest_team13_skeleton.dialogs;

import android.os.Bundle;
import android.util.Log;

import com.example.team13.personalbest_team13_skeleton.DataIOStream;
import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.Person;
import com.example.team13.personalbest_team13_skeleton.fitness.FitnessObserver;

/**
 * Manages which dialog menu should be displayed
 */
public class DialogManager implements FitnessObserver {
    // TODO (Nate): Apply dependency inversion here to make SRP and DRY.
    private static final String TAG = "DialogManager";
    private static final int ENC_VAL = 500;

    // Save the activity where we will display all of our dialogs.
    private MainActivity activity;
    private DataIOStream dataIOStream;
    private Person person;
    private int numberOfStepsYesterday;

    /**
     * @param activity - where we will display all of our dialogs.
     */
    public DialogManager(MainActivity activity, DataIOStream dataIOStream, Person person) {
        this.activity = activity;
        this.dataIOStream = dataIOStream;
        this.person = person;
    }

    /**
     * Detremine if we should show the congratulations menu.
     */
    private void checkCongratulationsDialog(int steps) {
        boolean flag = dataIOStream.loadFlag(DataIOStream.STR_SHOWN_CONGRATS);
        int goal = person.getGoal();

        if (!flag && steps > goal) {
            //setCongratsShownFlag(true);
            openCongratsDialog();
        }
    }

    /**
     * Open the congrats window in the main activity.
     */
    private void openCongratsDialog() {
        Log.d(TAG, "Showing the congrats dialog");
        CongratsDialog congratsDialog = new CongratsDialog(this);
        //congratsDialog.show(activity.getSupportFragmentManager(), "congrats dialog");

        Bundle bundle = new Bundle();
        bundle.putInt("goal", person.getGoal());
        bundle.putString("email", person.getEmailAcc());
        Log.d(TAG, "person goal: " + person.getGoal());
        //congratsDialog.onAttach(activity.getApplicationContext());
        congratsDialog.setArguments(bundle);
        congratsDialog.show(activity.getSupportFragmentManager(), TAG);
        dataIOStream.saveFlag(DataIOStream.STR_SHOWN_CONGRATS, true);
    }

    public void handleNewGoal(int newGoal) {
        person.setGoal(newGoal);
        activity.setStepGoalView();
    }

    /**
     * Determine if we should show the encouragement dialog.
     */
    private void checkEncouragementDialog(int steps) {
        this.numberOfStepsYesterday = dataIOStream.readPast7DaysTotalSteps()[0];
        boolean flag = dataIOStream.loadFlag(DataIOStream.STR_SHOWN_ENCOURAG);
        boolean hasFriends = dataIOStream.loadFlag(DataIOStream.STR_HAS_FRIEND);
        Log.d(TAG, "hasFriends: " + hasFriends);

        if (!flag && !hasFriends && steps > numberOfStepsYesterday + ENC_VAL) {
            openEncouragementDialog();
            //setEncouragementShownFlag(true);
        }
    }

    /**
     * Open the encouragement window
     */
    private void openEncouragementDialog() {
        Log.d(TAG, "Showing the encouragement window");
        EncouragementDialog encouragementDialog = new EncouragementDialog();
        encouragementDialog.show(activity.getSupportFragmentManager(), "encouragement dialog");
        dataIOStream.saveFlag(DataIOStream.STR_SHOWN_ENCOURAG, true);
    }

    /**
     * Decide what to display when we update the step count.
     */
    @Override
    public void stepsUpdated(int steps) {
        checkCongratulationsDialog(steps);
        checkEncouragementDialog(steps);
    }
}
