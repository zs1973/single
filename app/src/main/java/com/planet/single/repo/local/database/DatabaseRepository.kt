package com.planet.single.repo.local.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：11/19/2021
 *邮箱：305562245@qq.com
 *描述：从数据库获取数据的数据仓库
 *
 * @property db 应用数据库
 * @property io 协程运行的线程
 **/
@Singleton
class DatabaseRepository @Inject constructor(
    private val db: AppDatabase,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {

}