package com.planet.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object DateUtils {

    /**
     * 今年
     *
     * @return
     */
    fun getYear(): Int {
        return Calendar.getInstance()[Calendar.YEAR]
    }

    /**
     * 获取当月月份
     *
     * @return
     */
    fun getMonth(): Int {
        return Calendar.getInstance()[Calendar.MONTH] + 1
    }

    /**
     * 获取今日日期
     * @return String
     */
    fun getDateOfToday(): String {
        val calendar = Calendar.getInstance(Locale.CHINA)
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        return format.format(calendar.time)
    }

    /**
     * 获取昨天的日期
     */
    fun getDateOfYesterday(): String {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.add(Calendar.DATE, -1)
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        return format.format(calendar.time)
    }

    /**
     * 本周第一天日期
     *
     * @return string
     */
    fun getFirstDayOfWeek(): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        return format.format(calendar.time)
    }

    /**
     * 本周最后一天日期
     *
     * @return string
     */
    fun getLastDayOfWeek(): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        return format.format(calendar.time)
    }

    /**
     * 获取本月第一天日期
     *
     * @return
     */
    fun getFirstDayOfMonth(): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar[Calendar.DAY_OF_MONTH] = 1
        return format.format(calendar.time)
    }

    /**
     * 今天是当月的第几天
     *
     * @return
     */
    fun getToadyIndexOfMonth(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar[Calendar.DAY_OF_MONTH]
    }

    /**
     * 获取上月第一天日期
     */
    fun getDateOfFirstDayForLastMonth(): String {
        val calendar = Calendar.getInstance() //获取当前日期
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        //设置月份-1（用当前月份计算）
        calendar.add(Calendar.MONTH, -1)
        calendar[Calendar.DAY_OF_MONTH] = 1 //设置为1号，当前日期既为本月第一天
        return format.format(calendar.time)
    }

    /**
     * 获取上周的今天。如：今天是周四，就返回获取上周周四的日期
     */
    fun getDateOfTodayForLastWeek(): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance()
        calendar.roll(Calendar.WEEK_OF_YEAR, -1)
        return format.format(calendar.time)
    }

    /**
     * 获取上月的今天
     */
    fun getDateOfTodayForLastMonth(): String {
        //val calendar = Calendar.getInstance() //获取当前日期
        //val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        ////设置月份-1，也就是上个月
        //calendar.add(Calendar.MONTH, -1)
        ////计算上月的天数
        //val lastMonthDaysCount = getDayCountOfMonth(getYear(), calendar[Calendar.MONTH] + 1)
        ////如果本月今天超出上月总天数，就返回上月的最后一天，反之返回上月今天的日期
        ////比如：当月为10月份，共31天，上月为9月共30天，在10月31号当天获取上月今天日期时应该返回 2022年9月30日
        //calendar[Calendar.DAY_OF_MONTH] = if (getToadyIndexOfMonth() < lastMonthDaysCount) getToadyIndexOfMonth() else lastMonthDaysCount
        //return format.format(calendar.time)
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance()
        calendar.roll(Calendar.MONTH, -1)
        return format.format(calendar.time)
    }

    fun getDateOfTodayForLastYear():String{
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance()
        calendar.roll(Calendar.YEAR, -1)
        return format.format(calendar.time)
    }

    /**
     * 获取当年的第一天
     * @return String
     */
    fun getFirstDayOfYear(): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar[Calendar.DAY_OF_YEAR] = 1
        return format.format(calendar.time)
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return string eg: 2022-01-01
     */
    fun getFirstDayOfYear(year: Int): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar[Calendar.YEAR] = year
        calendar[Calendar.DAY_OF_YEAR] = 1
        return format.format(calendar.time)
    }

    /**
     * 获取今天是今年的第几天
     * @return int
     */
    fun getTodayIndexOfYear(): Int {
        val calendar = Calendar.getInstance(Locale.CHINA)
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 给定日期是今年的第几天
     * @param dateString String
     * @return Int
     */
    fun getDateIndexInYear(dateString: String): Int {
        if (dateString.isNotBlank()) {
            val calendar = Calendar.getInstance(Locale.CHINA)
            val timeStamp = dateString2TimeStamp(dateString)
            calendar.time = Date(timeStamp)
            return calendar.get(Calendar.DAY_OF_YEAR)
        }
        return 0
    }

    /**
     * 获取指定年份的指定月份的天数
     *
     * @param year 年
     * @param month 月份 1-12
     *
     * @return 总天数
     */
    fun getDayCountOfMonth(year: Int, month: Int): Int {
        val dayCount: Int
        var febDay = 28
        if (isLeapYear(year)) febDay = 29
        dayCount = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> febDay
            else -> 0
        }
        return dayCount
    }

    /**
     * 获取今年的总天数
     *
     * @return Int
     */
    fun getDayCountOfThisYear(): Int {
        val cal1 = GregorianCalendar.getInstance(Locale.CHINA)
        val year = cal1[Calendar.YEAR]
        val cal2 = GregorianCalendar(year, 11, 31)
        return cal2[Calendar.DAY_OF_YEAR]
    }

    /**
     * 给定年份和本年中的第几天，计算这天的日期：
     * 如 calculateDateByDayIndex(2022,59)，结果返回 2022-02-28
     *
     * @param year 年份
     * @param dayIndexOfYear 本年第几天
     * @return 日期
     */
    fun getDateByDayIndexInTargetYear(year: Int, dayIndexOfYear: Int): String {
        if (year <= 0 || dayIndexOfYear <= 0) {
            return "1970-01-01"
        }
        val date = StringBuilder()
        val months = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (isLeapYear(year)) {
            months[1] = 29
        }
        var days = 0
        var month = 1
        for ((i) in months.withIndex()) {
            days += months[i]
            when {
                days < dayIndexOfYear -> {
                    month += 1
                }

                days == dayIndexOfYear -> {
                    days = dayIndexOfYear
                }

                else -> {
                    month - 1
                    days -= months[i]
                    break
                }
            }
        }
        date.append("$year")
        if (month < 10) {
            date.append("-0$month")
        } else {
            date.append("-$month")
        }
        val day = if (dayIndexOfYear - days == 0) months[month - 1] else dayIndexOfYear - days
        if (day < 10) {
            date.append("-0$day")
        } else {
            date.append("-$day")
        }
        return date.toString()
    }

    /**
     * 获取指定日期所在周的周一日期
     * @param targetDate  注意日期格式要和格式化格式匹配：默认 yyyy-MM-dd
     */
    fun getMondayForDate(targetDate: String, formatString: String? = null): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        formatString?.let {
            if (formatString.isNotBlank()) {
                format.applyPattern(formatString)
            }
        }
        val calendar = Calendar.getInstance()
        calendar.time = Date(format.parse(targetDate).time)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        return format.format(calendar.time)
    }

    /**
     * 获取指定日期所在周的周日日期
     * @param targetDate  注意日期格式要和格式化格式匹配：默认 yyyy-MM-dd
     */
    fun getSundayForDate(targetDate: String, formatString: String? = null) {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        formatString?.let {
            if (formatString.isNotBlank()) {
                format.applyPattern(formatString)
            }
        }
        val calendar = Calendar.getInstance()
        calendar.time = Date(format.parse(targetDate).time)
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        format.format(calendar.time)
    }


    /**
     * 计算年份是否为闰年
     *
     * @param year 年份
     * @return true为闰年
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 时间戳转日期
     *
     * @param millis
     * @return
     */
    fun timeStampToDateTime(millis: Long, formatString: String? = null): String {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_2, Locale.CHINA)
        formatString?.let {
            if (formatString.isNotBlank()) {
                format.applyPattern(formatString)
            }
        }
        //如果是unix长度为10为需要*1000补齐至13位
        val processedMillis = if (millis.toString().length == 10) millis * 1000 else millis
        return format.format(Date(processedMillis))
    }

    /**
     * 字符串日期转时间戳
     * 注意时间戳长度 php是10位，到秒java是13位到毫秒
     * @param date String
     * @param formatString 日期格式默认 "yyyy-MM-dd"
     * @return Long
     */
    fun dateString2TimeStamp(date: String, formatString: String? = null): Long {
        val format = SimpleDateFormat(DATE_FORMAT_TYPE_1, Locale.CHINA)
        formatString?.let {
            if (formatString.isNotBlank()) {
                format.applyPattern(formatString)
            }
        }
        return format.parse(date)!!.time
    }

}

const val DATE_FORMAT_TYPE_1 = "yyyy-MM-dd"
const val DATE_FORMAT_TYPE_2 = "yyyy-MM-dd HH:mm"
const val DATE_FORMAT_TYPE_3 = "yyyy-MM-dd HH:mm:ss"
const val DATE_FORMAT_TYPE_4 = "yyyy/MM/dd"
const val DATE_FORMAT_TYPE_5 = "yyyy/MM/dd HH:mm"
const val DATE_FORMAT_TYPE_6 = "yyyy/MM/dd HH:mm:ss"