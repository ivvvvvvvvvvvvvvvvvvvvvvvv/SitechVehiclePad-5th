package com.sitechdev.vehicle.lib.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author liuhe
 * @date 18-8-2
 * use CommonToast.makeToast() instead
 */
@Deprecated
public class ToastUtils {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT, false);
    }

    public static void showSingleShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT, true);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showShort(@StringRes final int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showShort(@StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(resId, Toast.LENGTH_SHORT);
        } else {
            show(resId, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showShort(final String format, final Object... args) {
        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_LONG, false);
    }

    private static void show(@StringRes final int resId, final int duration) {
        show(AppUtils.getApp().getResources().getText(resId).toString(), duration);
    }

    private static void show(@StringRes final int resId, final int duration, final Object... args) {
        show(String.format(AppUtils.getApp().getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration, false);
    }

    private static void show(final CharSequence text, final int duration, boolean showSingle) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        HANDLER.post(() -> {
            showToast((String) text, duration, showSingle);
        });
    }

    private static void showToast(String text, int duration, boolean showSingle) {
//        if (showSingle) {
//            BaseToast.getInstance().showSingleToast(text, duration);
//        } else {
//            BaseToast.getInstance().showToast(text, duration);
//        }
    }
}