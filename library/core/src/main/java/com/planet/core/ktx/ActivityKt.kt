@file:Suppress("unused")

package com.planet.core.ktx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.planet.utils.LogUtils

/**
 *作者：张硕
 *日期：2021/12/23
 *邮箱：305562245@qq.com
 *描述：
 **/
fun AppCompatActivity.showToast(text: CharSequence) {
    Toast.makeText(this.applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(@StringRes text: Int) {
    Toast.makeText(this.applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.logi(tag: String, content: String) {
    LogUtils.logi(tag, content)
}

fun AppCompatActivity.loge(tag: String, content: String) {
    LogUtils.loge(tag, content)
}

inline fun <reified T : Activity> AppCompatActivity.navTo() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> AppCompatActivity.navTo(bundle: (bundle: Bundle) -> Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle(Bundle()))
    startActivity(intent)
}
