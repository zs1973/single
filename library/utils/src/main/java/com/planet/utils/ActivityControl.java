package com.planet.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActivityControl {

    private static final String TAG = "ActivityControl";
    private static ActivityControl sInstance = null;
    private final List<Activity> mList = new ArrayList<>();

    public static ActivityControl getInstance() {
        if (sInstance == null) {
            synchronized (ActivityControl.class) {
                if (sInstance == null) {
                    sInstance = new ActivityControl();
                }
            }
        }
        return sInstance;
    }

    public void add(Activity activity) {
        Log.d(TAG, "add:" + activity.toString());
        mList.add(activity);
    }

    public void close(Activity activity) {
        if (mList.contains(activity)) {
            Log.d(TAG, "close:" + activity.toString());
            mList.remove(activity);
        }
    }

    public void closeAll() {
        try {
            Log.d(TAG, "mList.size() = " + mList.size());
            for (int i = 0; i < mList.size(); i++) {
                Activity activity = mList.get(i);
                if (activity != null) {
                    Log.d(TAG, "closeAll:" + activity);
                    mList.remove(activity);
                    activity.finish();
                    i--; //因为位置发生改变，所以必须修改i的位置,否则会有ConcurrentModificationException
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}