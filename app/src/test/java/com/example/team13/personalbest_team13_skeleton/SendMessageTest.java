package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.activities.ChatBoxActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SendMessageTest {

    ChatBoxActivity testStore;
    // MainActivity testMsg;
    private Context context;

    @Before
    public void setUp() {
        //testMainActivity = new MainActivity(new MockStore());
        //Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(),
        //ViewTransactionActivity.class);
        Intent intentStore = new Intent().putExtra("test", "true");
        testStore = Robolectric.buildActivity(ChatBoxActivity.class, intentStore).create().get();
        context = testStore.getApplicationContext();

    }

    @Test
    public void checkUserNameRemembered() {
        EditText msgView = testStore.findViewById(R.id.text_message);
        msgView.setText("Hello!");
        Button sendBtn = testStore.findViewById(R.id.btn_send);
        sendBtn.performClick();
        // sent
        Assert.assertEquals("Hello!", testStore.chatMessageService.getContent());
    }
}
