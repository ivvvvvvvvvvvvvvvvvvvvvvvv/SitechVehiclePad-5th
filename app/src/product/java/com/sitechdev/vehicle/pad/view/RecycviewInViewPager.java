package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/22
 * </pre>
 */
public class RecycviewInViewPager extends RecyclerView {
    public RecycviewInViewPager(@NonNull Context context) {
        super(context);
    }

    public RecycviewInViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycviewInViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }
}
