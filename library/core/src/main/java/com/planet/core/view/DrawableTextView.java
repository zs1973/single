package com.planet.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.planet.core.R;
import com.planet.utils.DisplayUtil;

/**
 * 可以设置Drawable背景的TextView
 */
@SuppressWarnings("unused")
public class DrawableTextView extends AppCompatTextView {

    /**
     * 作为背景的drawable
     */
    private final GradientDrawable mDrawable = new GradientDrawable();
    /**
     * 默认透明度
     */
    private final int DEFAULT_ALPHA = 255;

    public DrawableTextView(Context context) {
        this(context, null);
    }


    public DrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        int mStrokeWidth = ta.getDimensionPixelSize(R.styleable.DrawableTextView_strokeWidth, 0);
        int mStrokeColor = ta.getColor(R.styleable.DrawableTextView_strokeColor, Color.TRANSPARENT);
        int mSolidColor = ta.getColor(R.styleable.DrawableTextView_solidColor, Color.TRANSPARENT);
        int mRadius = ta.getDimensionPixelSize(R.styleable.DrawableTextView_radius, 0);
        ta.recycle();
        setUpDrawable(mStrokeColor, mStrokeWidth, mSolidColor, mRadius);
        ViewCompat.setBackground(this, mDrawable);
    }

    private void setUpDrawable(int strokeColor, int strokeWidth, int solidColor, int radius) {
        mDrawable.setStroke(strokeWidth, strokeColor);
        mDrawable.setCornerRadius(radius);
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setColor(solidColor);
    }

    /**
     * 设置背景边框的宽度和颜色
     * @param strokeWidth 宽度
     * @param strokeColor 颜色
     * @return this
     */
    public DrawableTextView setStroke(int strokeWidth, int strokeColor) {
        this.mDrawable.setStroke(DisplayUtil.dp2px(strokeWidth), strokeColor);
        return this;
    }

    /**
     * 设置背景填充颜色
     *
     * @param solidColor 色值
     * @return this
     */
    public DrawableTextView setSolidColor(int solidColor) {
        this.mDrawable.setColor(solidColor);
        return this;
    }

    /**
     * 设置背景圆角
     *
     * @param radius 圆角角度值
     * @return this
     */
    public DrawableTextView setRadius(int radius) {
        this.mDrawable.setCornerRadius(radius);
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param textColor 文字颜色
     * @return this
     */
    public DrawableTextView textColor(int textColor) {
        setTextColor(textColor);
        return this;
    }

    public GradientDrawable getDrawable(){
        return mDrawable;
    }

    /**
     * 设置背景drawable的透明度
     * @param alpha 透明度 0-255
     */
    public void setBackgroundAlpha(int alpha){
        if(alpha>=0&&alpha<=DEFAULT_ALPHA){
            mDrawable.setAlpha(alpha);
        }
    }
}