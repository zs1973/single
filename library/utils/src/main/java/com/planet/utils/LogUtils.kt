package com.planet.utils

import android.util.Log

object LogUtils {

    @JvmStatic
    fun loge(tag: String, content: String) {
        if (AppUtils.isAppDebug()) {
            Log.e(tag, content)
        }
    }

    @JvmStatic
    fun logi(tag: String, content: String) {
        if (AppUtils.isAppDebug()) {
            Log.i(tag, content)
        }
    }

    @JvmStatic
    fun logd(tag: String, content: String) {
        if (AppUtils.isAppDebug()) {
            Log.d(tag, content)
        }
    }
}