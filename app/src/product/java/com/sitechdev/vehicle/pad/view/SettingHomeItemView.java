package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

public class SettingHomeItemView extends RelativeLayout {
    private TextView mTitle;
    private TextView mContent;

    public SettingHomeItemView(Context context) {
        this(context, null);
    }

    public SettingHomeItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingHomeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_setting_home_item, this);
        mTitle = view.findViewById(R.id.setting_home_item_title);
        mContent = view.findViewById(R.id.setting_home_item_content);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.setting_home_item);
        String title = a.getString(R.styleable.setting_home_item_title_name);
        String content = a.getString(R.styleable.setting_home_item_title_content);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
        }
    }
}
