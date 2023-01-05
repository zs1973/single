package com.planet.core.view.fix;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ChildViewPager extends ViewPager {
    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //每次进行onTouch事件都记录当前的按下的坐标
        if (getChildCount() <= 1) {
            return super.onTouchEvent(event);
        }

        curP.x = event.getX();
        curP.y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = event.getX();
            downP.y = event.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            getParent().requestDisallowInterceptTouchEvent(false);
            if (downP.x == curP.x && downP.y == curP.y) {
                return true;
            }
        }
        super.onTouchEvent(event); //注意这句不能 return super.onTouchEvent(arg0); 否则触发parent滑动
        return true;
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }
}