@file:Suppress("unused")
package com.planet.core.ktx

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.planet.utils.LogUtils
import kotlin.reflect.KClass

/**
 *作者：张硕
 *日期：2021/12/23
 *邮箱：305562245@qq.com
 *描述：
 **/
fun Fragment.showToast(text: CharSequence) {
    Toast.makeText(requireContext().applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(@StringRes text: Int) {
    Toast.makeText(requireContext().applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.logi(tag: String, content: String) {
    LogUtils.logi(tag,content)
}

fun Fragment.loge(tag: String, content: String) {
    LogUtils.loge(tag,content)
}

fun Fragment.navToActivity(target: KClass<*>) {
    startActivity(Intent(requireContext(), target.java))
}

fun Fragment.navToActivity(target: KClass<*>, bundle: (bundle: Bundle) -> Bundle) {
    val intent = Intent(requireContext(), target.java)
    intent.putExtras(bundle(Bundle()))
    startActivity(intent)
}