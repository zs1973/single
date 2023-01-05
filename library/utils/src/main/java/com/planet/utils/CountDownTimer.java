package com.planet.utils;

import android.os.Handler;
import android.os.SystemClock;

public class CountDownTimer {
    private long mTimes;
    private long allTimes;
    private final long mCountDownInterval;
    private final Handler mHandler;
    private OnTimerCallBack mCallBack;
    private boolean isStart;
    private long startTime;

    public CountDownTimer(long times, long countDownInterval) {
        this.mTimes = times;
        this.mCountDownInterval = countDownInterval;
        mHandler = new Handler();
    }

    public synchronized void start(OnTimerCallBack callBack) {
        this.mCallBack = callBack;
        if (isStart || mCountDownInterval <= 0) {
            return;
        }
        isStart = true;
        if (callBack != null) {
            callBack.onStart();
        }
        startTime = SystemClock.elapsedRealtime();
        if (mTimes <= 0) {
            finishCountDown();
            return;
        }
        allTimes = mTimes;
        mHandler.postDelayed(runnable, mCountDownInterval);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimes--;
            if (mTimes > 0) {
                if (mCallBack != null) {
                    mCallBack.onTick(mTimes);
                }
                long nowTime = SystemClock.elapsedRealtime();
                long delay = (nowTime - startTime) - (allTimes - mTimes) * mCountDownInterval;
                // 处理跳秒
                while (delay > mCountDownInterval) {
                    mTimes--;
                    if (mCallBack != null) {
                        mCallBack.onTick(mTimes);
                    }
                    delay -= mCountDownInterval;
                    if (mTimes <= 0) {
                        finishCountDown();
                        return;
                    }
                }
                mHandler.postDelayed(this, 1000 - delay);
            } else {
                mHandler.postDelayed(this, 0);
                finishCountDown();
            }
        }
    };

    private void finishCountDown() {
        if (mCallBack != null) {
            mCallBack.onFinish();
        }
        isStart = false;
    }

    public void cancel() {
        mHandler.removeCallbacksAndMessages(null);
        isStart = false;
    }

    public interface OnTimerCallBack {
        void onStart();

        void onTick(long times);

        void onFinish();
    }
}