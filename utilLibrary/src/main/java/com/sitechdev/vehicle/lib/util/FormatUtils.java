package com.sitechdev.vehicle.lib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuhe
 * @date 18-8-21
 */
public class FormatUtils {

    private static int mYear;
    private int mMonth;
    private static int mDay;

    public static FormatUtils getInstance() {
        return Holder.instance;
    }

    static class Holder {
        static final FormatUtils instance = new FormatUtils();
    }

    private FormatUtils() {
        Calendar c = Calendar.getInstance();//
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
    }

    public static String formatDate(String rawDate) {
        StringBuilder sb = new StringBuilder(rawDate);
        sb.insert(6, '/');
        sb.insert(4, '/');
        return sb.toString();
    }

    public static String formatTime(String rawTime) {
        StringBuilder sb = new StringBuilder(rawTime);
        sb.insert(2, ':');
        return sb.toString();
    }


    public String formatCallTime(String date, String time) {
        if (date.equals("") || time.equals("")) {
            return "";
        }
        String timeResult = time;
        String dateResult = date;
        if (time.length() == 6) {
            timeResult = time.substring(0, 2).concat(":").concat(time.substring(2, 4));
        }
        int year = 0, month = 0, day = 0;
        if (date.length() == 8) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(4, 6));
            day = Integer.parseInt(date.substring(6, 8));
        }
        if (year == mYear) {
            if (month == month) {
                if (day == mDay) {
                    return timeResult;
                } else {
                    return (month < 10 ? "0" + month : month) + "/" + day;
                }
            } else {
                return (month < 10 ? "0" + month : month) + "/" + day;
            }
        } else {
            return year + "/" + (month < 10 ? "0" + month : month) + "/" + day;
        }
    }

    public static boolean isStartWithNumber(String str) {
        return str.matches("[0-9]+.*");
    }
}
