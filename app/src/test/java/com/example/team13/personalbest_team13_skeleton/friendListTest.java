package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.activities.FriendList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class friendListTest {

    FriendList testFriendList;
    private Context context;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent().putExtra("FRIEND_LIST_SERVICE", "test");
        intent.putExtra("EMAIL", "allen");
        testFriendList = Robolectric.buildActivity(FriendList.class, intent).create().get();
        context = testFriendList.getApplicationContext();
    }

    // BDD scenario test for adding a friend. Scenario 1 in MS2 planning.
    @Test
    public void testAddANewFriend(){

        testFriendList.initFriendListUpdateListener();
        String[] friendList = testFriendList.getFriendlistdata();
        friendList = testFriendList.getFriendlistdata();
        Assert.assertNull(friendList);

        EditText editFriendEmail = testFriendList.findViewById(R.id.addFriendInput);
        editFriendEmail.setText("ming");
        Button addBtn = testFriendList.findViewById(R.id.add_btn);
        addBtn.performClick();

        testFriendList.initFriendListUpdateListener();
        friendList = testFriendList.getFriendlistdata();
        Assert.assertEquals(0, friendList.length);

        testFriendList.friends.setUser("ming");
        editFriendEmail.setText("allen");
        addBtn.performClick();

        testFriendList.friends.setUser("allen");

        testFriendList.initFriendListUpdateListener();
        friendList = testFriendList.getFriendlistdata();
        Assert.assertEquals("ming", friendList[0]);
    }
}

