package com.planet.utils

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.view.ViewCompat

object ViewBackgroundUtils {

    /**
     * 替代xml shape方式给View设置Drawable纯色或者渐变色背景
     *
     * @param view View
     * @param strokeWidth Int?                      边框宽度
     * @param strokeColor Int?                      边框颜色
     * @param solidColor Int?                       填充颜色
     * @param cornerRadius Int?                     圆角大小:单位dp
     * @param gradientColors IntArray?              渐变色（设置单个填充颜色时不要设置此属性）
     * @param gradientType Int?                     渐变类型（如果要设置渐变色背景可设置此属性）
     * 线性[GradientDrawable.LINEAR_GRADIENT]，
     * 圆形 [GradientDrawable.RADIAL_GRADIENT]，
     * 扫描 [GradientDrawable.SWEEP_GRADIENT]
     * @param drawableOrientation Orientation?      控制渐变色的绘制方向（）
     */
    @JvmStatic
    fun applyDrawableWith(
        view: View,
        strokeWidth: Int?,
        strokeColor: Int?,
        solidColor: Int?,
        cornerRadius: Int?,
        gradientColors: IntArray?,
        gradientType: Int? = GradientDrawable.LINEAR_GRADIENT,
        drawableOrientation: GradientDrawable.Orientation?
    ) {
        val drawable = GradientDrawable()
        if (strokeColor != null && strokeWidth != null) {
            drawable.setStroke(strokeWidth, strokeColor)
        }
        solidColor?.let { drawable.setColor(it) }
        cornerRadius?.let { drawable.cornerRadius = DisplayUtil.dp2px(it).toFloat() }
        gradientColors?.let { drawable.colors = gradientColors }
        gradientType?.let { drawable.gradientType = it }
        drawableOrientation?.let { drawable.orientation = drawableOrientation }
        ViewCompat.setBackground(view, drawable)
    }

}