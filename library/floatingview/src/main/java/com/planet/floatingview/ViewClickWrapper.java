package com.planet.floatingview;

import android.view.View;

/**
 *    author: Android 轮子哥
 *    github: https://github.com/getActivity/XToast
 *    time  : 2019/01/04
 *    desc  : {@link View.OnClickListener} 包装类
 */
@SuppressWarnings("rawtypes")
final class ViewClickWrapper implements View.OnClickListener {

    private final FloatingView mFloatingView;
    private final FloatingView.OnClickListener mListener;

    ViewClickWrapper(FloatingView toast, FloatingView.OnClickListener listener) {
        mFloatingView = toast;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onClick(View view) {
        if (mListener == null) {
            return;
        }
        mListener.onClick(mFloatingView, view);
    }
}