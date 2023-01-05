package com.planet.network

import com.planet.network.util.ResponseHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：11/19/2021
 *邮箱：305562245@qq.com
 *
 * 从服务器获取数据的数据仓库
 **/
@Singleton
open class RemoteDataRepository @Inject constructor(
	protected val responseHandler: ResponseHandler,
	protected val io: CoroutineDispatcher
)