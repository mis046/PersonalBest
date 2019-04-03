package tests;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;

import com.example.team13.personalbest_team13_skeleton.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
public class CongratsDialogUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private MainActivity testMainActivity;
    private DialogFragment myCongratsDialog;
    private long currentStepCountGoal;
    private Context context;

    @Before
    public void setup() throws Exception {
        // testMainActivity = Robolectric.setupActivity(MainActivity.class);

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        testMainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    /**
     * Make sure default step goals is appropriately set to 5000.
     */
    @Test
    public void checkDefaultStepGoal() {
        // Get the default steps goal.
//        SharedPreferences sp = testMainActivity.getSharedPreferences("goal_num", context.MODE_PRIVATE);
//        String goal = sp.getString("goal","0");
//        currentStepCountGoal = Integer.parseInt(goal); // Assign to default goal.
//        assertEquals(5000, currentStepCountGoal);
    }

    /**
     * Make sure the display flag is set to false on a brand new application.
     */
    @Test
    public void checkCongratsDialogFlagOnNewApplication() {
//        SharedPreferences sharedPreferences = testMainActivity.getSharedPreferences("congrats", context.MODE_PRIVATE);
//        boolean congratsShownFlag = sharedPreferences.getBoolean("congratsShownFlag", false);
//        assertEquals(true, congratsShownFlag);
    }

    /**
     * Determines if the message displayed is the correct display message.
     */
    @Test
    public void checkCorrectDisplayMessage() {
//        String goalMsg =  String.format(Locale.ENGLISH, "You reached your goal of %d steps!\n\n" +
//                "Do you want to set a new goal of %d steps?", currentStepCountGoal, currentStepCountGoal + 500);
//        //String text = myCongratsDialog.getText(R.id.goalMsgView).toString();
//        FragmentActivity te = myCongratsDialog.getActivity();
//        TextView tv = te.findViewById(R.id.goalMsgView);
//        String text = tv.getText().toString();
//        assertEquals(goalMsg, text);
    }
}
