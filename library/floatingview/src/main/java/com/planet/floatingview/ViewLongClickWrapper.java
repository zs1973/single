package com.planet.floatingview;

import android.view.View;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XToast
 *    time   : 2021/09/03
 *    desc   : {@link View.OnLongClickListener} 包装类
 */
@SuppressWarnings("rawtypes")
final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final FloatingView mFloatingView;
    private final FloatingView.OnLongClickListener mListener;

    ViewLongClickWrapper(FloatingView toast, FloatingView.OnLongClickListener listener) {
        mFloatingView = toast;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        return mListener.onLongClick(mFloatingView, view);
    }
}