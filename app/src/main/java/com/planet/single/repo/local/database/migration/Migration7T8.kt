package com.planet.single.repo.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration7T8:Migration(7,8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //--------------迁移app表数据 更新表null字段为not null
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `package_name` TEXT NOT NULL, `quota_duration_every_day` INTEGER NOT NULL, `keep_mode` INTEGER NOT NULL, `remind_time_interval` INTEGER NOT NULL, `stop_watch` INTEGER NOT NULL DEFAULT 0, `enable` INTEGER NOT NULL, `logic_delete` INTEGER NOT NULL, `auto_close` INTEGER NOT NULL, `use_last_remaining_time` INTEGER NOT NULL)")
        database.execSQL("INSERT INTO app_temp (id,name,package_name,quota_duration_every_day,keep_mode,remind_time_interval,stop_watch,enable,logic_delete,auto_close,use_last_remaining_time) select id,name,package_name,quota_duration_every_day,keep_mode,remind_time_interval,stop_watch,enable,logic_delete,auto_close,use_last_remaining_time from app")
        database.execSQL("DROP TABLE app")
        database.execSQL("ALTER TABLE app_temp RENAME TO app")
        //--------------迁移提醒文案表数据  更新表null字段为not null
        database.execSQL("CREATE TABLE IF NOT EXISTS `remind_temp` (`remindId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `app_owner_id` INTEGER NOT NULL, `remind_time` INTEGER NOT NULL, `content` TEXT NOT NULL, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("DROP INDEX `index_remind_app_owner_id`")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_remind_app_owner_id` ON `remind_temp` (`app_owner_id`)")
        database.execSQL("INSERT INTO remind_temp (remindId,app_owner_id,remind_time,content) select  remindId,app_owner_id,remind_time,content  from remind")
        database.execSQL("DROP TABLE remind")
        database.execSQL("ALTER TABLE remind_temp RENAME TO remind")
        //--------------迁移app使用记录表数据 更新表null字段为not null
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_use_record_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `app_owner_id` INTEGER NOT NULL, `quota_duration_every_day` INTEGER NOT NULL, `spend_time_today` INTEGER NOT NULL, `open_times_today` INTEGER NOT NULL, `remaining_time_today` INTEGER NOT NULL, `create_time` TEXT NOT NULL, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("DROP INDEX `index_app_use_record_app_owner_id`")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_app_use_record_app_owner_id` ON `app_use_record_temp` (`app_owner_id`)")
        database.execSQL("INSERT INTO app_use_record_temp (id,app_owner_id,quota_duration_every_day,spend_time_today,open_times_today,remaining_time_today,create_time) select  id,app_owner_id,quota_duration_every_day,spend_time_today,open_times_today,remaining_time_today,create_time  from app_use_record")
        database.execSQL("DROP TABLE app_use_record")
        database.execSQL("ALTER TABLE app_use_record_temp RENAME TO app_use_record")
        //--------------迁移app使用历史表数据 更新表null字段为not null
        database.execSQL("CREATE TABLE IF NOT EXISTS `app_use_history_temp` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `app_owner_id` INTEGER NOT NULL, `app_name` TEXT NOT NULL, `start_time` INTEGER NOT NULL, `end_time` INTEGER NOT NULL, `date` TEXT NOT NULL, `use_duration` INTEGER NOT NULL, FOREIGN KEY(`app_owner_id`) REFERENCES `app`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("DROP INDEX `index_app_use_history_app_owner_id`")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_app_use_history_app_owner_id` ON `app_use_history_temp` (`app_owner_id`)")
        database.execSQL("INSERT INTO app_use_history_temp (id,app_owner_id,app_name,start_time,end_time,date,use_duration) select  id,app_owner_id,app_name,start_time,end_time,date,use_duration  from app_use_history")
        database.execSQL("DROP TABLE app_use_history")
        database.execSQL("ALTER TABLE app_use_history_temp RENAME TO app_use_history")
    }
}