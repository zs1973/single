package com.planet.single.repo.local.database.dao

import androidx.room.Query

/**
 * 所有表需继承此接口
 */
interface CommonDao {
    /**
     * 设置每次清空表数据之后，主键都从0开始自增
     */
    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = :tableName")
    suspend fun updatePrimaryKeyAutoIncrementValue(tableName: String)
}