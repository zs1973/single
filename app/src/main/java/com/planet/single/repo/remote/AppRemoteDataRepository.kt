package com.planet.single.repo.remote

import com.planet.network.RemoteDataRepository
import com.planet.network.util.ResponseHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：
 **/
class AppRemoteDataRepository @Inject constructor(
    private val api: Api,
    responseHandler: ResponseHandler,
    io: CoroutineDispatcher
) : RemoteDataRepository(responseHandler, io) {

}