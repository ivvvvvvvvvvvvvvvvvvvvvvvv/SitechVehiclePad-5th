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

    private Typeface mainFontFace = null, mainFontFace_i = null, getMainFont_Min_i = null;

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
            mainFontFace = Typeface.createFromAsset(AppApplication.getContext().getAssets(), "fonts/main_font_95w.otf");
        }
        return mainFontFace;
    }

    public Typeface getMainFont_i() {
        if (mainFontFace_i == null) {
            mainFontFace_i = Typeface.createFromAsset(AppApplication.getContext().getAssets(), "fonts/main_font_65w.otf");
        }
        return mainFontFace_i;
    }

    public Typeface getMainFont_Min_i() {
        if (getMainFont_Min_i == null) {
            getMainFont_Min_i = Typeface.createFromAsset(AppApplication.getContext().getAssets(), "fonts/main_font_55w.otf");
        }
        return getMainFont_Min_i;
    }
}
