package com.netease.channel.util;

import java.sql.Date;
import java.util.Calendar;

public class DateUtil {
    /**
     * 获取当前时间的前一天
     *
     * @return java.sql.date
     */
    public static Date getYesterday() {

        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        int currentDay = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, currentDay - 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        return Date.valueOf(date);
    }

    /**
     * 获取当前时间的前两天
     *
     * @return java.sql.date
     */
    public static Date getTheDayBeforeYesterday() {

        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        int currentDay = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, currentDay - 2);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        return Date.valueOf(date);
    }

    /**
     * 获取当前时间的前一个月
     *
     * @return java.sql.date
     */
    public static Date getLastMonth() {

        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        int currentDay = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, currentDay - 30);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        return Date.valueOf(date);
    }
}
