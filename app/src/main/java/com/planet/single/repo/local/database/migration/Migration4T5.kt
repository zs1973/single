package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration4T5:Migration(4,5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------app表新增一个字段，标识此数据是否被逻辑删除
        database.execSQL("ALTER TABLE `app` ADD COLUMN logic_delete INTEGER DEFAULT 0")
    }
}