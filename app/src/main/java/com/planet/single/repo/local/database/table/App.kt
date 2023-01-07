package com.planet.single.repo.local.database.table

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * 被加入托管或者守护计划的应用信息
 */
@Entity(
    tableName = "app"
)
@Parcelize
data class App(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    /**
     * 应用名称
     */
    val name: String,
    /**
     * 应用包名
     */
    @ColumnInfo(name = "package_name")
    val packageName: String,
    /**
     * 应用每日配额时长(仅托管模式)
     */
    @ColumnInfo(name = "quota_duration_every_day", defaultValue = "0")
    val quotaDurationEveryDay: Int = 0,
    /**
     * 计划模式
     */
    @ColumnInfo(name = "keep_mode")
    val keepMode: Int,
    /**
     * 守护模式下的提醒间隔
     */
    @ColumnInfo(name = "remind_time_interval", defaultValue = "0")
    val remindTimeInterval: Int = 0,
    /**
     * 是否停止监控
     * 0否，1是
     */
    @ColumnInfo(name = "stop_watch", defaultValue = "0")
    val stopWatch: Int = 0,
    /**
     * 前端控制stopWatch是否可以编辑
     * 1是  2否
     */
    @ColumnInfo(defaultValue = "1")
    val enable: Int = 1,
    /**
     * 是否被逻辑删除 0否 1是
     */
    @ColumnInfo(defaultValue = "0")
    val logic_delete: Int = 0,
    /**
     * 时间锁模式下到时是否自动关闭
     * 0：不自动关闭
     * 1：自动关闭
     */
    @ColumnInfo(name = "auto_close", defaultValue = "0")
    val autoClose: Int = 0,

    /**
     * 值为1开启沿用上次剩余使用时长，0关闭。
     * 开启后，如果今日剩余使用时长>0，则会不会显示时间锁悬浮窗并继续消耗剩余时长。
     * 否则，将显示时间锁悬浮窗重新给应用设置配额时长。
     * @see [AppUseRecord]
     * @see [AppUseRecord.remainingTimeToday]
     */
    @ColumnInfo(name = "use_last_remaining_time", defaultValue = "0")
    val useLastRemainingTime: Int = 0

) : Parcelable