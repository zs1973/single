package com.planet.single.repo.remote.config

import com.planet.network.NetWorkProvider
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：提供默认配置的网络请求客户端。
 *
 * @property netWorkProvider [NetWorkProvider] 提供一个默认配置的网络请求客户端
 *
 **/
@Singleton
class DefaultNetWorkProvider @Inject constructor(
	private val netWorkProvider: NetWorkProvider
) {

	fun getDefaultNetworkClient(): Retrofit =  netWorkProvider.retrofit

}