package com.sitechdev.vehicle.lib.util;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final String TAG = TimeUtils.class.getSimpleName();

    private static final String SYNC_URL = "https://www.sitechdev.com";

    public static String formatTime(long time, String formatStr) {
        if (time <= 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time);
    }

    public static String formatTime(long time) {
        if (time <= 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(time);
    }

    /**
     * 设置网络时间到系统
     */
    public static void setNetworkTime2Sys(final Context ctx, final OnSyncTimeCallback syncTimeCallback) {
        if (null == ctx) {
            if (null != syncTimeCallback) {
                syncTimeCallback.onSyncTimeFailed();
            }
            return;
        }
        try {
            ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Long>() {
                @Nullable
                @Override
                public Long doInBackground() {
                    return getWebsiteDatetime(SYNC_URL);
                }

                @Override
                public void onSuccess(@Nullable Long result) {
                    super.onSuccess(result);
                    if (0 != result) {
                        Date date = new Date(result);
                        setSystemTime(ctx, date.getTime());
                        setHazensTime(ctx, date.getTime());
                        // 输出北京时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        SitechDevLog.d(TAG, "getWebsiteDatetime = " + sdf.format(date));
                        if (null != syncTimeCallback) {
                            syncTimeCallback.onSyncTimeSuccess();
                        }
                    } else {
                        SitechDevLog.e(TAG, "getWebsiteDatetime is 0 ");
                        if (null != syncTimeCallback) {
                            syncTimeCallback.onSyncTimeFailed();
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (null != syncTimeCallback) {
                syncTimeCallback.onSyncTimeFailed();
            }
        }
    }

    /**
     * 设置系统时间
     */
    public static void setHazensTime(Context ctx, long time) {
        Intent it = new Intent("hazens.server.system.settime");
        it.putExtra("time", time);
        if (ctx != null) {
            ctx.sendBroadcast(it);
        }
    }

    public static void setSystemTime(Context ctx, long time) {
        Date dateTime = new Date(time);
        ((AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE)).setTime(dateTime.getTime());
    }

    /**
     * 获取指定网站的日期时间
     */
    private static long getWebsiteDatetime(String webUrl) {
        try {
            // 取得资源对象
            URL url = new URL(webUrl);
            // 生成连接对象
            URLConnection connection = url.openConnection();
            // 发出连接
            connection.connect();
            // 读取网站日期时间
            long ld = connection.getDate();
            return ld;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public interface OnSyncTimeCallback {
        void onSyncTimeSuccess();

        void onSyncTimeFailed();
    }
}
