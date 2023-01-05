package com.planet.single.repo.local.database.dao

import androidx.room.*
import com.planet.single.repo.local.database.entities.App
import kotlinx.coroutines.flow.Flow

/**
 *作者：张硕
 *日期：2/16/2022
 *邮箱：305562245@qq.com
 *描述：
 **/
@Dao
interface AppDao : CommonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: App): Long

    @Update
    suspend fun update(app: App)

    /**
     * 修改应用的监控状态
     * @param appId 应用id
     */
    @Query("update app set stop_watch=:stopWatch where id=:appId")
    suspend fun updateWatchStatus(appId: Long, stopWatch: Int)

    /**
     * 更新enable的状态
     * @param enable Int
     */
    @Query("update app set enable=:enable where id=:appId")
    suspend fun updateAppEnable(appId: Long, enable: Int)

    /**
     * 获取监控的应用列表
     * @return List<App>
     */
    @Query("select * from app where logic_delete = 0")
    suspend fun getKeepList(): List<App>

    /**
     * 获取有效监控应用列表（监控开关没被关闭 未被逻辑删除）
     */
    @Query("select * from app where stop_watch=0 and logic_delete = 0")
    fun getKeepFlowList(): Flow<List<App>>

    /**
     * 逻辑删除被监控的应用
     * @param appId Long
     */
    @Query("update app set logic_delete = 1 where id=:appId")
    suspend fun logicDelete(appId: Long)

    /**
     * 查询监控应用总数量
     * @return Int
     */
    @Query("select count(1) from app where logic_delete = 0")
    suspend fun keepAppCount(): Int

    /**
     * 查询应用是否在监控列表
     * @param packageName String
     * @return Int
     */
    @Query("select count(1) from app where package_name=:packageName")
    fun isInKeepList(packageName: String): Int

    /**
     * 获取有效监控应用的总数量（监控开关没被关闭 未被逻辑删除）
     */
    @Query("select count(1) from app where package_name=:packageName and stop_watch=0 and logic_delete = 0")
    fun isInKeep(packageName: String): Int

    /**
     * 通过包名获取应用信息
     */
    @Query("select * from app where package_name=:packageName")
    fun getAppInfoByPackageName(packageName: String): App

    /**
     * 获取应用在时间锁模式下自动关闭状态
     * 0 不开启到时自动关闭
     * 1 开启到时自动关闭
     */
    @Query("select auto_close from app where id=:appId")
    suspend fun getAppAutoCloseStatus(appId: Long): Int

    /**
     * 更新应用的自动开启状态
     */
    @Query("update app set auto_close=:autoCloseStatus where id=:appId")
    suspend fun updateAppAutoCloseStatus(appId: Long, autoCloseStatus: Int)

    /**
     * 更新是否沿用上次剩余使用时间开关的状态
     *
     * @param appId 应用id
     * @param status 0 关闭 1开启
     */
    @Query("update app set use_last_remaining_time=:status where id=:appId")
    suspend fun updateUseLastRemainingTimeStatus(appId: Long, status: Int)

    /**
     * 获取是否沿用上次剩余使用时间开关的状态
     *
     * @param appId 应用id
     */
    @Query("select use_last_remaining_time from app where id=:appId")
    suspend fun getAppUseLastRemainingTimeStatus(appId: Long): Int

}