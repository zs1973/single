package com.planet.utils

import android.widget.Toast

/**
 *作者：张硕
 *日期：2022/02/15
 *邮箱：305562245@qq.com
 *描述：
 **/
object ToastUtils {

    @JvmStatic
    fun showShort(text: String) {
        Toast.makeText(AppUtils.getApp().applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun showLong(text: String) {
        Toast.makeText(AppUtils.getApp().applicationContext, text, Toast.LENGTH_LONG).show()
    }

}