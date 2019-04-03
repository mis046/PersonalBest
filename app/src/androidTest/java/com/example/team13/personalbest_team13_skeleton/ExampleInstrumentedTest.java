package com.example.team13.personalbest_team13_skeleton;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/*
If you want to execute the firebase espresso test from terminal you can use these two calls

./gradlew packageDebug packageDebugAndroidTest

gcloud firebase test android run \
 --type instrumentation \
 --app ./app/build/outputs/apk/debug/app-debug.apk \
 --test ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk

 */


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.team13.personalbest_team13_skeleton", appContext.getPackageName());
    }
}
