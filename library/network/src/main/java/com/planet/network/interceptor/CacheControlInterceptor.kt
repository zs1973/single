package com.planet.network.interceptor

import com.planet.utils.LogUtils
import com.planet.utils.NetWorkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


/**
 * OkhHttp请求拦截器配置请求缓存，离线走缓存，在线获取最新数据。
 * 参考：[`CSDN博客`](https://blog.csdn.net/baobei0921/article/details/125102792)。
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
 * @see InternetCacheControlInterceptor
 */
class CacheControlInterceptor : Interceptor {

    private val mTag = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!NetWorkUtils.isConnected()) {
            LogUtils.loge(mTag, "使用离线缓存：${request.url.encodedPath}")
            //离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
            val maxStale = 60 * 60 * 24 * 7 // 离线时缓存保存1周,单位:秒
            val tempCacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(maxStale, TimeUnit.SECONDS)
                .build()
            val processedRequest = request.newBuilder()
                .cacheControl(tempCacheControl)
                .build()
            return chain.proceed(processedRequest)
        } else {
            LogUtils.loge(mTag, "请求网络新数据：${request.url.encodedPath}")
        }
        return chain.proceed(request)
    }
}