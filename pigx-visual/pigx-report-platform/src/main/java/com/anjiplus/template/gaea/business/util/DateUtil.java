package com.anjiplus.template.gaea.business.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by raodeming on 2021/4/29.
 */
public class DateUtil {

    private static String defaultDatePattern = "yyyy-MM-dd";

    private static String defaultDateTimePattern = "yyyy-MM-dd HH:mm:ss.SSS";

    private static String defaultyyyyMMddPattern = "yyyyMMdd";

    private static String defaultYmdHmsPattern = "yyyy-MM-dd HH:mm:ss";

    private static String defaultHmsPattern = "HH:mm:ss";
    /**字符串yyyy-MM-dd HH:mm:ss转日期
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parseHmsTime(String dateStr) {
        return parse(dateStr, defaultYmdHmsPattern);
    }

    /**字符串转日期
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        try {
            Date d = sdf.parse(dateStr);
            return d;
        } catch (ParseException e) {
            System.out.println("日期转换错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取未来第几天的日期
     *
     * @param day
     * @return
     */
    public static Date getFutureDateTmdHms(int day) {
        if (day <= 0) {
            //默认2099年
            return parse("2099-01-01", defaultDatePattern);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTime();
    }

    /**未来时间
     * 根据指定时间获取
     * @param time
     * @param day
     * @return
     */
    public static Date getFutureDateTmdHmsByTime(Date time, int day) {
        if (day <= 0) {
            //默认2099年
            return parse("2099-01-01", defaultDatePattern);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date futureDateTmdHms = getFutureDateTmdHms(7);
        System.out.println(futureDateTmdHms);
    }
}
