package com.planet.network.util

/**
 * 接口的请求状态
 */
sealed class Status {
    object Loading : Status()
    object Success : Status()
    object Error : Status()
}