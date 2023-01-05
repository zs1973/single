package com.planet.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val DATETIME_FORMAT_1 = SimpleDateFormat("yyyy-MM-DD-hh-mm-ss", Locale.CHINA)
    private val DATETIME_FORMAT_2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
    val time = "HH:mm:ss"
    val date = "YYYY-MM-DD"

    /**
     * 获取现在时间 HH:mm:ss
     * @return String
     */
    fun now(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA) // HH:mm:ss
        val date = Date(System.currentTimeMillis())
        return simpleDateFormat.format(date)
    }


    fun timestampToString(format: String, timeStamp: Long): String {
        return format.ifBlank { "yyyy-MM-dd HH:mm" }.let {
            val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
            val date = Date(timeStamp)
            simpleDateFormat.format(date)
        }
    }

    /**
     * 时间戳
     *
     * @param millis
     * @return
     */
    fun millis2HHmm(millis: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA) // HH:mm:ss
        val date = Date(System.currentTimeMillis())
        return simpleDateFormat.format(millis)
    }

    /**
     * PHP时间戳转日期
     * @param timeStamp
     * @return String
     */
    @JvmStatic
    fun millisTransToString(timeStamp: Long): String {
        //php后台返回的时间戳为10位
        if (timeStamp.toString().length == 10) {
            val date = Date(timeStamp * 1000)
            return DATETIME_FORMAT_2.format(date)
        }
        return DATETIME_FORMAT_2.format(Date(timeStamp))
    }

    /**
     * PHP日期转13位时间戳
     * @param dateTime
     * @return Timestamp
     */
    @JvmStatic
    fun dateTransToTimeStamp(dateTime: String): Long {
        return DATETIME_FORMAT_1.parse(dateTime)!!.time
    }

}