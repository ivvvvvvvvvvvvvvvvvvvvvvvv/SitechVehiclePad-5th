package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatTextView;


public class SkinTextView extends SkinCompatTextView {

    public SkinTextView(Context context) {
        super(context, null);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SkinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}