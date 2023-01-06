package com.planet.network.util

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import com.planet.network.R
import com.planet.network.model.ApiResponse
import com.planet.network.model.ApiResponseCode
import com.planet.utils.AppUtils
import com.planet.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(10001),
    UnknownHostException(10002),
    ConnectException(10003),
    UnauthorizedError(401),
    ResourceNotFound(404),
    ServiceInternalError(500)
}

/**
 * 统一处理API请求结果
 *
 * [点击查看参考medium文章](https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-network-error-handling-a5b98b5e5ca0)
 */
open class ResponseHandler @Inject constructor() {

    private val mTag = this.javaClass.name

    fun <T> handleSuccess(data: ApiResponse<T?>): Resource<T> {
        return when (data.errorCode) {
            ApiResponseCode.Success.code -> {
                Resource.success(data.data)
            }
            //ApiResponseCode.TokenExpire.code -> {
            //    handLoginExpire()
            //    Resource.error(data.message)
            //}
            else -> {
                Resource.error("${data.errorCode}:${data.errorMsg}")
            }
        }
    }

    /**
     * 处理异常，获取异常类型
     *
     * @param T 接口返回的数据model
     * @param e 异常
     * @return  异常类型
     */
    fun <T> handleException(e: Exception): Resource<T> {
        return  Resource.error("${e.message.toString()}\n点击重试")
//        return when (e) {
//            is HttpException -> Resource.error(getErrorMessage(e.code()))
//            is SocketTimeoutException -> Resource.error(
//                getErrorMessage(ErrorCodes.SocketTimeOut.code)
//            )
//            is ConnectException -> Resource.error(
//                getErrorMessage(ErrorCodes.ConnectException.code)
//            )
//            is UnknownHostException -> Resource.error(
//                getErrorMessage(ErrorCodes.UnknownHostException.code)
//            )
//            else -> {
//                LogUtils.loge(mTag, "handleException：${e.message.toString()}")
//                Resource.error(e.message.toString())
//            }
//        }
    }

    /**
     * 根据错误码获取错误消息
     *
     * @param code 错误码
     * @return String 提示信息
     */
    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "网络连接超时"
            ErrorCodes.UnknownHostException.code -> "网络异常\n点击重试"
            ErrorCodes.UnauthorizedError.code -> "API未授权"
            ErrorCodes.ResourceNotFound.code -> "未找到访问资源"
            ErrorCodes.ServiceInternalError.code -> "服务器内部错误"
            ErrorCodes.ConnectException.code -> "网络异常\n点击重试"
            else -> "出现未知异常，请联系我们。"
        }
    }

    private suspend fun handLoginExpire() {
        withContext(Dispatchers.Main) {
            ToastUtils.showShort(AppUtils.getApp().getString(R.string.planet_login_expire))
            val targetActivity = "com.planet.timesaver/com.planet.main.ui.activity.ThirdLoginActivity"
            val intent = Intent()
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    component = ComponentName.unflattenFromString(targetActivity)
                }
            val packageManager = AppUtils.getApp().packageManager
            val activities =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (activities.size > 0) {
                try {
                    AppUtils.getApp().startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
