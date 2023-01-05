package com.planet.single.repo.remote.config

import com.planet.network.NetWorkProvider
import com.planet.single.BuildConfig
import com.planet.utils.LogUtils
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：提供一个可行自配置的网络请求客户端。
 *
 * @property netWorkProvider NetWorkProvider 提供一个默认配置的网络请求客户端
 *
 **/
@Singleton
class CustomNetWorkProvider @Inject constructor(
	private val netWorkProvider: NetWorkProvider
) {

	fun getDefaultNetworkClient(): Retrofit {
		val loggingInterceptor = HttpLoggingInterceptor(
			object : HttpLoggingInterceptor.Logger {
				override fun log(message: String) {
					LogUtils.loge("PlanetApp", message)
				}
			}).apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

		val okHttpClient = netWorkProvider.okHttpClient
			.newBuilder()
			.addInterceptor(loggingInterceptor)
			.build()

		return netWorkProvider.retrofit
			.newBuilder()
            .baseUrl(BuildConfig.baseUrl)
			.client(okHttpClient)
			.build()
	}

}