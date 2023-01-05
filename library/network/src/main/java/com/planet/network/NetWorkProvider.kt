package com.planet.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：
 **/
@Singleton
class NetWorkProvider @Inject constructor(
	val moshi:Moshi,
	val okHttpClient: OkHttpClient,
	val retrofit: Retrofit
)