package com.planet.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

@Suppress("unused")
object DisplayUtil {
    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    @JvmStatic
    fun px2dp(pxValue: Int): Int {
        val scale = AppUtils.getApp().applicationContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    @JvmStatic
    fun dp2px(dipValue: Int): Int {
        val scale = AppUtils.getApp().applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    @JvmStatic
    fun px2sp(pxValue: Int): Int {
        val fontScale = AppUtils.getApp().applicationContext.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    @JvmStatic
    fun sp2px(spValue: Int): Int {
        val fontScale = AppUtils.getApp().applicationContext.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * @return DisplayMetrics对象
     */
    @JvmStatic
    private fun getDisplayMetrics(): DisplayMetrics {
        val windowManager = AppUtils.getApp().applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    /**
     * 获取屏幕分辨率-宽
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        val metrics = getDisplayMetrics()
        return metrics.widthPixels
    }

    /**
     * 获取屏幕分辨率-高
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        val metrics = getDisplayMetrics()
        return metrics.heightPixels
    }
}