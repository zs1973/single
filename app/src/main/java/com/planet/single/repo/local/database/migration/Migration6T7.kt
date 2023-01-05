package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration6T7:Migration(6,7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //-------------App表新增字段：use_last_remaining_time 沿用上次剩余时间，1是 0否
        database.execSQL("ALTER TABLE `app` ADD COLUMN use_last_remaining_time INTEGER DEFAULT 0")
        //-------------新增应用使用记录表
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_use_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `app_owner_id` INTEGER, `app_name` TEXT NOT NULL, `start_time` INTEGER NOT NULL, `end_time` INTEGER NOT NULL, `date` TEXT NOT NULL, `use_duration` INTEGER NOT NULL, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        //创建本地索引 索引名称命名规则: index_表名称_字段名
        database.execSQL("CREATE INDEX `index_app_use_history_app_owner_id` ON `app_use_history` (`app_owner_id`)")
    }
}