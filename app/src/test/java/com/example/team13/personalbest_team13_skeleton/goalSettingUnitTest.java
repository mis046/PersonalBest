package com.example.team13.personalbest_team13_skeleton;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.activities.GoalSettingsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class goalSettingUnitTest {
    private GoalSettingsActivity activity;
    private TextView text_goal;
    private EditText edit_goal;
    private Button btn_save;

    @Before
    public void setUp() throws Exception {
//        Intent intent = new Intent(RuntimeEnvironment.application, GoalSettingsActivity.class);
//        activity = Robolectric.buildActivity(GoalSettingsActivity.class, intent).create().get();
//
//        text_goal = activity.findViewById(R.id.textGoal);
//        btn_save = activity.findViewById(R.id.btn_save);
//        edit_goal = activity.findViewById(R.id.editGoal);
    }

    @Test
    public void testUpdateStepsButton() {
//        assertEquals("GoalSettingsActivity: 5000 steps", text_goal.getText().toString());
//        edit_goal.setText("7000");
//        btn_save.performClick();
//        assertEquals("GoalSettingsActivity: 7000 steps", text_goal.getText().toString());
    }

}
