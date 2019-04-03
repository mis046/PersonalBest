package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;

import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.dialogs.DialogManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class EncouragementDialogTest {

    public static final String TEST_EMAIL = "test@email.com";

    private Context context;

    private static final String TEST_SERVICE = "TEST_SERVICE";
    private MainActivity testMainActivity;
    private long currentStepCountGoal;
    private DialogManager mockDialogManager;
    private DialogManager dialogManager;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent().putExtra("FRIEND_LIST_SERVICE", "test");
        intent.putExtra("EMAIL", "allen");
        testMainActivity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
        context = testMainActivity.getApplicationContext();
    }

    @Test
    public void testAutomatedEncouragement() {
        // Build data management object.
        DataIOStream dataIOStream = new DataIOStream(context, TEST_EMAIL);
        dataIOStream.saveDailyTotalSteps(1500);

        // Load the new person which by default has no friends but has history steps of 500.
        Person person = dataIOStream.loadPerson();

        dialogManager = new DialogManager(testMainActivity, dataIOStream, person);

        // Mock the dialogManager and link with the person object we have now.
        //mockDialogManager = mock(dialogManager.getClass());
        mockDialogManager = mock(dialogManager.getClass());

        mockDialogManager.stepsUpdated(2000);

        verify(mockDialogManager, times(1)).stepsUpdated(2000);
    }
}
