package com.planet.network.di

import com.planet.network.BuildConfig
import com.planet.network.interceptor.CacheControlInterceptor
import com.planet.utils.AppUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetWorkModule {

    companion object {
        /**
         * 代理检测，设置okhttp不响应代理请求，不允许抓包。
         */
        val proxySelector = object : ProxySelector() {
            override fun select(uri: URI?): MutableList<Proxy> {
                return Collections.singletonList(Proxy.NO_PROXY)
            }

            override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
            }
        }
    }


    /**
     * 提供一个默认配置的OkHttpClient客户端
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        //有关addInterceptor和addNetworkInterceptor的区别参看一下链接
        //https://www.jianshu.com/p/1752753db538
        //缓存路径
        val cacheDir = File(AppUtils.getApp().applicationContext.externalCacheDir!!.absolutePath)
        //缓存大小
        val cacheSize: Long = 1024 * 1024 * 5 //5M
        //是否允许使用缓存策略
        // false 有网和没有网都是先读缓存；true 离线可以缓存，在线就获取最新数据；default=false
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(CacheControlInterceptor())
            //如果要实现有网络也读本地缓存，解除下面注释
            //.addNetworkInterceptor(InternetCacheControlInterceptor(isAllowCache))
            .proxySelector(proxySelector)
            .retryOnConnectionFailure(false)
            .cache(Cache(cacheDir, cacheSize))
            .build()
    }

    /**
     * 提供一个默认配置的moshi
     * @return Moshi
     */
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * 提供一个默认配置retrofit
     * @param moshi Moshi
     * @param okHttpClient OkHttpClient
     * @return Retrofit
     */
    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

}