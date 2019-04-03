package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SubscriberTest {
    MainActivity testMsg;
    private Context context;

    @Before
    public void setUp() {
        Intent intentMsg = new Intent().putExtra("TEST_MESS", "true");
        // intentMsg.putExtra("TEST_STORE", "true");
        testMsg = Robolectric.buildActivity(MainActivity.class, intentMsg).create().get();
        context = testMsg.getApplicationContext();
    }

    @Test
    public void checkSubscriber() {
        Assert.assertEquals("Subscribed to notifications chat1", testMsg.messText);
    }
}
