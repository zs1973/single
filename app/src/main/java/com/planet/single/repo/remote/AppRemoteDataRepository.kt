package com.planet.single.repo.remote

import com.planet.network.RemoteDataRepository
import com.planet.network.util.Resource
import com.planet.network.util.ResponseHandler
import com.planet.single.repo.remote.di.BindDefaultNetWorkProvider
import com.planet.single.ui.bean.response.Kind
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：
 **/
class AppRemoteDataRepository @Inject constructor(
    @BindDefaultNetWorkProvider
    private val api: Api,
    responseHandler: ResponseHandler,
    io: CoroutineDispatcher
) : RemoteDataRepository(responseHandler, io) {

    suspend fun getKind():Resource<List<Kind>>{
        return try {
            withContext(io) {
                responseHandler.handleSuccess(api.kind())
            }
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}