package com.planet.network.interceptor

import android.text.TextUtils
import com.planet.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response


/**
 * OkhHttp请求拦截器配置请求缓存，配合[CacheControlInterceptor]实现有网没网都先走缓存。
 * 参考：[CSDN博客](https://blog.csdn.net/baobei0921/article/details/125102792)。
 *
 * 实现简单的无网络获取本地缓存有网络请求最新数据，使用：
 * ```
 *  OkHttpClient.Builder()
 *           .addInterceptor(CacheControlInterceptor())
 *           .build()
 * ```
 *
 * 无论有无网络都先读缓存：
 * ```
 *  OkHttpClient.Builder()
 *           .addInterceptor(CacheControlInterceptor())
 *           .addNetworkInterceptor(InternetCacheControlInterceptor())
 *           .build()
 * ```
 *
 * @see okhttp3.OkHttpClient.Builder.addInterceptor
 * @see okhttp3.OkHttpClient.Builder.addNetworkInterceptor
 *
 * @see CacheControlInterceptor
 */
class InternetCacheControlInterceptor() : Interceptor {
    private val mTag = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val serverCache = response.header("Cache-Control")//服务端设置的缓存策略
        // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改
        if (TextUtils.isEmpty(serverCache)) {
            val cacheControl = request.cacheControl.toString()//请求接口设置的缓存策略
            return if (TextUtils.isEmpty(cacheControl)) {
                // 如果请求接口中未设置cacheControl，则统一设置为一分钟
                val maxAge = 1 * 60 // 在线缓存在1分钟内可读取 单位:秒
                response.newBuilder()
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build().let {
                        LogUtils.loge(mTag, "有网状态下使用缓存，客户端本地设置默认一分钟：${it.request.url.encodedPath}")
                        it
                    }
            } else {
                response.newBuilder()
                    .addHeader("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build().let {
                        LogUtils.loge(mTag, "有网状态下使用缓存，遵循服务器设置：${it.request.url.encodedPath}")
                        it
                    }
            }
        }
        return response
    }
}