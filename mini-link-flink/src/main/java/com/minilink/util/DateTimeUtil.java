package com.minilink.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-03  11:23
 * @Description: 日期转换工具类
 * @Version: 1.0
 */
public class DateTimeUtil {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    /**
     * LocalDateTime 转 String，指定日期格式
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDateTime.atZone(DEFAULT_ZONE_ID));
    }

    /**
     * LocalDateTime 转 String，默认日期格式
     */
    public static String format(LocalDateTime localDateTime) {
        return DEFAULT_DATE_TIME_FORMATTER.format(localDateTime.atZone(DEFAULT_ZONE_ID));
    }

    /**
     * Date 转 String，指定日期格式
     */
    public static String format(Date time, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
    }

    /**
     * Date 转 String，默认日期格式
     */
    public static String format(Date time) {
        return DEFAULT_DATE_TIME_FORMATTER.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
    }

    /**
     * timestamp 转 字符串，默认日期格式
     */
    public static String format(long timestamp) {
        return DEFAULT_DATE_TIME_FORMATTER.format(new Date(timestamp).toInstant().atZone(DEFAULT_ZONE_ID));
    }

    /**
     * timestamp 转 LocalDateTime
     */
    public static LocalDateTime timeStampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), DEFAULT_ZONE_ID);
    }

    /**
     * LocalDateTime 转 timestamp
     */
    public static Long localDateTimeToTimeStamp(LocalDateTime time) {
        return time.atZone(DEFAULT_ZONE_ID).toInstant().getEpochSecond();
    }

    /**
     * String 转 Date
     */
    public static Date strToDate(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DEFAULT_DATE_TIME_FORMATTER);
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * String 转 LocalDateTime，默认日期格式
     */
    public static LocalDateTime strToLocalDateTime(String time) {
        return LocalDateTime.parse(time, DEFAULT_DATE_TIME_FORMATTER);
    }
}