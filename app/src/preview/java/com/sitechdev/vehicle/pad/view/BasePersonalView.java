package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;

/**
 * @author 邵志
 * @version 2020/06/04 0004 16:35
 * @name BasePersonalView
 * @description
 */
public class BasePersonalView extends ConstraintLayout {
    public BasePersonalView(Context context) {
        super(context);
    }

    public BasePersonalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePersonalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
    }
}
