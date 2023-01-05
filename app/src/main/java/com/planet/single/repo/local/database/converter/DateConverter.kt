package com.planet.single.repo.local.database.converter

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * 日期转换
 */
class DateConverter {

	@TypeConverter
	fun timestamp(timeStamp: Long?): Long? {
		return timeStamp
	}

	/**
	 * 时间戳转日期
	 *
	 * @param timeStamp
	 * @return
	 */
	@TypeConverter
	fun timestampToDate(timeStamp: String?): String? {
		return timeStamp?.let {
			FORMATTER.format(timeStamp)
		}
	}

	companion object {
		val FORMATTER: DateFormat = SimpleDateFormat.getDateInstance()
	}

}