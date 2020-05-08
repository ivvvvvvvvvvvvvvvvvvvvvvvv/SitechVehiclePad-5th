package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yy.mobile.rollingtextview.RollingTextView;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;


public class SkinRollingTextView extends RollingTextView implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinRollingTextView(Context context) {
        super(context, null);
    }

    public SkinRollingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SkinRollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }


    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}