package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration3T4:Migration(3,4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------app表新增一个字段，控制前端是否可以编辑stopWatch字段的值
        database.execSQL("ALTER TABLE `app` ADD COLUMN enable INTEGER DEFAULT 1")
    }
}