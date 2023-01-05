package com.planet.network.interceptor

import com.planet.utils.AppUtils
import com.planet.utils.PackageUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CopyOnWriteArrayList

/**
 * okhttp请求拦截器
 * 负责统一给请求添加请求头。
 */
@Suppress("unused")
abstract class HeaderInterceptor : Interceptor {

    private val mTag = javaClass.simpleName

    /**
     * 不需要校验token的接口地址
     */
    private val apiWhiteList = arrayListOf<String>()

    /**
     * 请求头集合
     */
    private val mHeaderPairList = CopyOnWriteArrayList<Pair<String, String>>()

    init {
        mHeaderPairList.add(Pair(HeaderName.ACCEPT, HeaderValue.ACCEPT))
        mHeaderPairList.add(Pair(HeaderName.CONTENT_TYPE, HeaderValue.CONTENT_TYPE))
        mHeaderPairList.add(
            Pair(
                HeaderName.APP_VERSION,
                PackageUtils.getVerName(AppUtils.getApp())
            )
        )
    }

    fun addToApiWhiteList(path: String) {
        if (!apiWhiteList.contains(path)) {
            apiWhiteList.add(path)
        }
    }

    fun removeFromApiWhiteList(path: String) {
        if (apiWhiteList.contains(path)) {
            apiWhiteList.remove(path)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //接口路径
        val path = request.url.encodedPath
        //添加各组件新增的header
        if (headers().isNotEmpty()) {
            headers().forEach {
                if (!mHeaderPairList.contains(it)) {
                    mHeaderPairList.add(it)
                }
            }
            //mHeaderPairList.addAll(headers())
        }
        val builder = request.newBuilder()
        mHeaderPairList.forEach {
            //白名单中的接口不向header中添加token
            if (it.first == HeaderName.AUTHORIZATION && apiWhiteList.contains(path)) {
                return@forEach
            }
            //builder.removeHeader(it.first)
            builder.header(it.first, it.second)
        }
        builder.method(request.method, request.body)
        return chain.proceed(builder.build())
    }

    open fun headers(): ArrayList<Pair<String, String>> = arrayListOf()

    /**
     * 需要添加header，必须先从这里配置header的name和value
     */
    companion object {

        /**
         * header的name
         */
        object HeaderName {
            //token
            const val AUTHORIZATION = "Authorization"
            const val ACCEPT = "Accept"
            const val CONTENT_TYPE = "Content-Type"
            //应用版本号
            const val APP_VERSION = "App-Version"
        }

        /**
         * header的value
         */
        object HeaderValue {
            const val ACCEPT = "application/json"
            const val CONTENT_TYPE = "application/json; charset=utf-8"
        }
    }
}