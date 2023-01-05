package com.planet.utils;

import android.graphics.Color;

/**
 * 作者：张硕
 * 日期：2/17/2022
 * 邮箱：305562245@qq.com
 * 描述：https://www.jianshu.com/p/71efa21b76a3
 **/
public class ColorUtils {

    /**
     * 获取更深颜色
     */
    public static int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        // make darker
        hsv[1] = hsv[1] + 0.1f; // 饱和度更高
        hsv[2] = hsv[2] - 0.1f; // 透明度降低
        return Color.HSVToColor(hsv);
    }

    /**
     * 获取更浅的颜色
     */
    public static int getBrighterColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv

        hsv[1] = hsv[1] - 0.1f; // 饱和度更低
        hsv[2] = hsv[2] + 0.1f; // 透明度更高
        return Color.HSVToColor(hsv);
    }

    /**
     * @param color      颜色
     * @param saturation 饱和度：数值越大颜色越浅
     * @return
     */
    public static int getBrighterColor(int color, float saturation) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        hsv[1] = hsv[1] - saturation; // 饱和度更低
        hsv[2] = hsv[2] + saturation; // 透明度更高
        return Color.HSVToColor(hsv);
    }

}
