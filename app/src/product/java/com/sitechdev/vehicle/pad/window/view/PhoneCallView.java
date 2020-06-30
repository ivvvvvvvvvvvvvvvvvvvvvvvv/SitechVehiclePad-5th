package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;

public class PhoneCallView extends ConstraintLayout {

    public PhoneCallView(Context context) {
        this(context, null);
    }

    public PhoneCallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneCallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
    }
}
