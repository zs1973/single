package com.planet.network.model

import androidx.annotation.Keep

/**
 * 作者：张硕
 * 日期：11/22/2021
 * 邮箱：305562245@qq.com
 *
 * 接口返回的数据格式
 */
@Keep
data class ApiResponse<out T>(
    var code: Int,
    val data: T?,
    val message: String
)