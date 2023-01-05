package com.planet.utils;


public class DateTimeUtil {

    private final static long minute = 60 * 1000;   // 1分钟
    private final static long hour = 60 * minute;   // 1小时
    private final static long day = 24 * hour;      // 1天
    private final static long month = 31 * day;     // 月
    private final static long year = 12 * month;    // 年

    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour % 24 == 0) {
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天";
                } else if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

}
