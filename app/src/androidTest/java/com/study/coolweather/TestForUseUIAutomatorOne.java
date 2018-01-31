package com.study.coolweather;

import android.os.SystemClock;
import android.support.test.uiautomator.UiAutomatorTestCase;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/29 , 11:20.
 */

public class TestForUseUIAutomatorOne extends UiAutomatorTestCase{
    private static final String TAG = "TestForUseUIAutomatorOne";

    private UiDevice mDevice = null;

    @Override
    public void setUp() throws Exception {
        Log.d(TAG, "***setUp: ***");

        super.setUp();
        mDevice = getUiDevice();
    }

    public void testPressHome() throws Exception {
        mDevice.pressHome();


        UiObject buriedApp = mDevice
                .findObject(new UiSelector().text("欧酷天气"));
        buriedApp.clickAndWaitForNewWindow();
        SystemClock.sleep(500);


        UiObject switchCity = mDevice.findObject(new UiSelector().resourceId("com.study.coolweather:id/switch_city"));
        switchCity.clickAndWaitForNewWindow();
        SystemClock.sleep(500);


        UiObject shanxiProvince = mDevice.findObject(new UiSelector().text("陕西"));
        shanxiProvince.clickAndWaitForNewWindow();
        SystemClock.sleep(500);


        UiObject xianArea = mDevice.findObject(new UiSelector().text("西安"));
        xianArea.clickAndWaitForNewWindow();
        SystemClock.sleep(500);


        UiScrollable listOfXian = new UiScrollable(new UiSelector().resourceId("com.study.coolweather:id/list_view"));
        assertTrue("list not found",listOfXian.exists());

        UiObject xianCity = listOfXian.getChild(new UiSelector().text("西安"));
        xianCity.clickAndWaitForNewWindow();
        SystemClock.sleep(500);


        UiObject xianTextView = mDevice.findObject(new UiSelector().className(TextView.class).text("西安"));
        assertTrue("there is something wrong with getting the weather of xian",xianTextView.exists());
        assertTrue("there is something wrong with getting the weather of xian",switchCity.exists());
    }

    @Override
    public void tearDown() throws Exception{
        Log.d(TAG, "***tearDown:***");
        super.tearDown();
    }
}
