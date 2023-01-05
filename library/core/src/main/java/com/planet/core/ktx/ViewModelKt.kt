@file:Suppress("unused")
package com.planet.core.ktx
import androidx.lifecycle.ViewModel
import com.planet.utils.LogUtils


fun ViewModel.logi(tag: String, content: String) {
    LogUtils.logi(tag,content)
}

fun ViewModel.loge(tag: String, content: String) {
    LogUtils.loge(tag,content)
}