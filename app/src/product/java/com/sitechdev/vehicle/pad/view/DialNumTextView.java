package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sitechdev.vehicle.pad.util.FontUtil;


/**
 * 拨号数字
 *
 * @author liuhe
 * @date 2019/04/17
 */
public class DialNumTextView extends android.support.v7.widget.AppCompatTextView {
    public DialNumTextView(Context context) {
        this(context, null);
    }

    public DialNumTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialNumTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(FontUtil.getInstance().getMainFont());
    }
}
