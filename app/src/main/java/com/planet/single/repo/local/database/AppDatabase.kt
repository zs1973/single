package com.planet.single.repo.local.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.planet.single.repo.local.database.dao.AppDao
import com.planet.single.repo.local.database.entities.App
import com.planet.single.repo.local.database.migration.*

/**
 *作者：张硕
 *日期：11/19/2021
 *邮箱：305562245@qq.com
 *
 *文档：https://developer.android.google.cn/training/data-storage/room?hl=zh-cn
 *
 *迁移工具：https://hrankit.github.io/RoomSQLiteDifferenceFinder/
 **/
@Database(
    entities = [
        App::class,
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = AppDatabase.AutoMigration1to2::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppDao(): AppDao

    /**
     * 重命名字段
     */
    @RenameColumn(
        tableName = "app",
        fromColumnName = "is_logic_delete",
        toColumnName = "stop_watch"
    )
    class AutoMigration1to2 : AutoMigrationSpec

    companion object {
        //单例
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            //如果INSTANCE不为null直接返回,
            //如果为空则创建一个实例返回
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "module_quota.db"
                    ).addMigrations(
                        Migration2T3,
                        Migration3T4,
                        Migration4T5,
                        Migration5T6,
                        Migration6T7,
                        Migration7T8,
                        Migration8T9,
                        Migration9T10
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}