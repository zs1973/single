package com.planet.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 作者：张硕
 * 日期：2022/03/23
 * 邮箱：305562245@qq.com
 * 谨记：想要完美时，完美即已不存在。
 * 描述：
 **/
public class ServiceCompat {

    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
