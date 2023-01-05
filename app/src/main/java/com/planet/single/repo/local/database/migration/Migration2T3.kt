package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration2T3:Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------迁移app表数据
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL, `package_name` TEXT NOT NULL, `quota_duration_every_day` INTEGER,`keep_mode` INTEGER NOT NULL, `remind_time_interval` INTEGER,  `stop_watch` INTEGER NOT NULL DEFAULT 0)")
        database.execSQL("INSERT INTO app_temp (id,name,package_name,quota_duration_every_day,keep_mode,remind_time_interval,stop_watch)select  id,name,package_name,quota_duration_every_day,keep_mode,remind_time_interval,stop_watch  from app")
        database.execSQL("DROP TABLE app")
        database.execSQL("ALTER TABLE app_temp RENAME TO app")
        //--------------迁移提醒文案表数据
        database.execSQL("CREATE TABLE IF NOT EXISTS `remind_temp` (`remindId` INTEGER PRIMARY KEY AUTOINCREMENT, `app_owner_id` INTEGER, `remind_time` INTEGER NOT NULL, `content` TEXT NOT NULL, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION)")
        database.execSQL("DROP INDEX `index_remind_app_owner_id`")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_remind_app_owner_id` ON `remind_temp` (`app_owner_id`)")
        database.execSQL("INSERT INTO remind_temp (remindId,app_owner_id,remind_time,content) select  remindId,app_owner_id,remind_time,content  from remind")
        database.execSQL("DROP TABLE remind")
        database.execSQL("ALTER TABLE remind_temp RENAME TO remind")
        //--------------迁移app使用记录表数据
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_use_record_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `app_owner_id` INTEGER, `quota_duration_every_day` INTEGER, `spend_time_today` INTEGER, `open_times_today` INTEGER, `remaining_time_today` INTEGER, `create_time` TEXT, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("DROP INDEX `index_app_use_record_app_owner_id`")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_app_use_record_app_owner_id` ON `app_use_record_temp` (`app_owner_id`)")
        database.execSQL("INSERT INTO app_use_record_temp (id,app_owner_id,quota_duration_every_day,spend_time_today,open_times_today,remaining_time_today,create_time) select  id,app_owner_id,quota_duration_every_day,spend_time_today,open_times_today,remaining_time_today,create_time  from app_use_record")
        database.execSQL("DROP TABLE app_use_record")
        database.execSQL("ALTER TABLE app_use_record_temp RENAME TO app_use_record")
    }
}