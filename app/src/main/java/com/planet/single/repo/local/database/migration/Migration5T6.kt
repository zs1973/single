package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration5T6:Migration(5,6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------app表新增一个字段，标识app再时间锁模式下是否开启自动关闭 0不开启 1 开启
        database.execSQL("ALTER TABLE `app` ADD COLUMN auto_close INTEGER DEFAULT 0")
    }
}