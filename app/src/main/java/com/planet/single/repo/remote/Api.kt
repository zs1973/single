package com.planet.single.repo.remote

import com.planet.network.model.ApiResponse
import com.planet.single.ui.bean.response.Kind
import retrofit2.http.GET


/**
 *作者：张硕
 *日期：2/22/2022
 *邮箱：305562245@qq.com
 *描述：
 **/
interface Api {
    @GET("project/tree/json")
    suspend fun kind():ApiResponse<List<Kind>>
}