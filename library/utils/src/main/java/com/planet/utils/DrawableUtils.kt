package com.planet.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

/**
 * 作者：张硕
 * 日期：2/17/2022
 * 邮箱：305562245@qq.com
 * 描述：
 */
object DrawableUtils {

    fun createGradientDrawable(
        strokeWidth: Int? = null,
        strokeColor: Int? = null,
        dashWidth: Float = 0.0f,
        dashGap: Float = 0.0f,
        solidColor: Any?,
        cornerRadius: Int = 0,
        topLeftRadius: Int = 0,
        topRightRadius: Int = 0,
        bottomLeftRadius: Int = 0,
        bottomRightRadius: Int = 0,
        startColor: Int? = null,
        endColor: Int? = null,
        gradientType: Int? = null,
        drawableOrientation: GradientDrawable.Orientation? = null,
        alpha: Int? = null
    ): Drawable {
        val drawable = GradientDrawable()
        //边框颜色和宽度
        if (strokeColor != null && strokeWidth != null) {
            drawable.setStroke(DisplayUtil.dp2px(strokeWidth), strokeColor, dashWidth, dashGap)
        }
        //填充颜色
        solidColor?.let {
            if (it is Int) drawable.setColor(it)
            if (it is String) drawable.setColor(Color.parseColor(solidColor as String))
        }
        //设置四个角的角度
        if (cornerRadius > 0) {
            drawable.cornerRadius = DisplayUtil.dp2px(cornerRadius).toFloat()
        } else {
            //单独设置四个角角度
            val radii = FloatArray(8)
            //左上角
            radii[0] = DisplayUtil.dp2px(topLeftRadius).toFloat()
            radii[1] = DisplayUtil.dp2px(topLeftRadius).toFloat()
            //右上角
            radii[2] = DisplayUtil.dp2px(topRightRadius).toFloat()
            radii[3] = DisplayUtil.dp2px(topRightRadius).toFloat()
            //右下角
            radii[4] = DisplayUtil.dp2px(bottomRightRadius).toFloat()
            radii[5] = DisplayUtil.dp2px(bottomRightRadius).toFloat()
            //右下角
            radii[6] = DisplayUtil.dp2px(bottomLeftRadius).toFloat()
            radii[7] = DisplayUtil.dp2px(bottomLeftRadius).toFloat()
            drawable.cornerRadii = radii
        }
        //渐变色色，需同时设置
        if (startColor != null && endColor != null) {
            val colors = intArrayOf(startColor, endColor)
            drawable.colors = colors
        }
        //渐变类型
        gradientType?.let { drawable.gradientType = it }
        drawableOrientation?.let { drawable.orientation = drawableOrientation }
        alpha?.let {
            drawable.alpha = alpha
        }

        return drawable
    }
}