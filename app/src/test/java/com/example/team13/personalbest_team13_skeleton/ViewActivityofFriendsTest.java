package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.activities.FriendList;
import com.example.team13.personalbest_team13_skeleton.activities.ViewFriendActivities;
import com.example.team13.personalbest_team13_skeleton.friendActivities.IFriendActivityService;
import com.example.team13.personalbest_team13_skeleton.friendActivities.MockFriendActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ViewActivityofFriendsTest {

    ViewFriendActivities viewFriendActivities;
    Context context;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent().putExtra("test", true);
        intent.putExtra("friendEmail", "allen");
        viewFriendActivities = Robolectric.buildActivity(ViewFriendActivities.class, intent).create().get();
        context = viewFriendActivities.getApplicationContext();
    }

    // BDD Scenario: View activities of a friend who has been using PB for only a week
    @Test
    public void test(){
        IFriendActivityService mockFriendActivity = viewFriendActivities.firestoreFriendActivityAdapter;
        mockFriendActivity.addData(Integer.toString(0), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(1), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(2), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(3), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(4), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(5), 3000, 1000, 5000);
        mockFriendActivity.addData(Integer.toString(6), 3000, 1000, 5000);
        viewFriendActivities.setData(28);
    }
}

