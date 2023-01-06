package com.planet.single.repo

import com.planet.network.util.Resource
import com.planet.single.repo.local.database.DatabaseRepository
import com.planet.single.repo.remote.AppRemoteDataRepository
import com.planet.single.ui.bean.response.Kind
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：
 **/
@Suppress("unused")
@Singleton
class AppDataRepository @Inject constructor(
    private val mRp: AppRemoteDataRepository,
    private val mDb: DatabaseRepository,
) {

   suspend fun getKind():Resource<List<Kind>>{
        return mRp.getKind()
    }

}