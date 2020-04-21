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


    public String formatCallTime(String data, String time) {
        if (data.equals("") || time.equals("")) {
            return "";
        }
        Date d1 = null;//定义起始日期
        try {
            d1 = new SimpleDateFormat("yyyy/MM/dd").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        int year = Integer.parseInt(sdf0.format(d1));
        int month = Integer.parseInt(sdf1.format(d1));
        int day = Integer.parseInt(sdf2.format(d1));

        if (year == mYear) {
            if (month == month) {
                if (day == mDay) {
                    return time;
                } else {
                    return month + "/" + day;
                }
            } else {
                return month + "/" + day;
            }
        } else {
            return data;
        }
    }

    public static boolean isStartWithNumber(String str) {
        return str.matches("[0-9]+.*");
    }
}
