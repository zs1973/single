package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration8T9:Migration(8,9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------为使用记录表date创建索引 提高getSumsOfAppUseDurationAndOpenTimes的查询效率
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_app_use_history_date` ON `app_use_history` (`date`)")
    }
}