package com.sitechdev.vehicle.pad.util;

import android.graphics.Typeface;

import com.sitechdev.vehicle.pad.app.AppApplication;

/**
 * 项目名称：GA10-DOS2
 * 类名称：FontUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/11/04 0004 10:13
 * 修改时间：
 * 备注：
 */
public class FontUtil {

    private Typeface mainFontFace = null;

    private FontUtil() {
    }

    private static class SingleFontUtil {
        private static final FontUtil SINGLE = new FontUtil();
    }

    public static FontUtil getInstance() {
        return SingleFontUtil.SINGLE;
    }

    public Typeface getMainFont() {
        if (mainFontFace == null) {
            mainFontFace = Typeface.createFromAsset(AppApplication.getContext().getAssets(), "fonts/tv_time.otf");
        }
        return mainFontFace;
    }
}
