package com.planet.network.util

/**
 * API请求结果
 *
 * @property status:请求状态码
 * @property data: [com.planet.network.model.ApiResponse]
 * @property message:相应提示信息
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.Success, data, null)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(Status.Error,null, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.Loading, data, null)
        }
    }
}
