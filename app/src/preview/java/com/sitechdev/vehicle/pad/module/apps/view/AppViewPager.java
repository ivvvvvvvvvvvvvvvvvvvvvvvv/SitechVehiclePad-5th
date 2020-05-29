package com.sitechdev.vehicle.pad.module.apps.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;
import com.sitechdev.vehicle.pad.module.apps.util.MenuBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：AppRecycleView
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/14 0014 17:09
 * 修改时间：
 * 备注：
 */
public class AppViewPager extends ViewPager {
    /**
     * 主界面每一页Viewpager的GridView的集合
     */
    public static List<View> mAllMenuGridViewList = new ArrayList<View>();
    /**
     * 是否响应Touch事件
     */
    public static boolean isTouchResponse = true;
    /**
     * item原属的pager索引
     */
    public static int mLastPagerIndex = 0;
    /**
     * 当前显示的tag索引
     */
    public static int mCurrentPagerIndex = 0;
    /**
     * 当前view 的循环索引
     */
    public static int mCurrentPos = 0;
    // /** ViewPager当前所显示的view对象 */
    // public static View mCurrentView = null;
    /**
     * 当前view 的循环索引
     */
    public static int mCurrentViewIndex = 0;

    public AppViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AppViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!isTouchResponse) {
                    isTouchResponse = true;
                    return false;
                }
                break;
            default:
                if (!isTouchResponse) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private SimpleOnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
//                mCurrentPagerIndex = getCurrentItem();
//                SitechDevLog.w(AppConst.TAG_APP, "onPageScrollStateChanged  mCurrentPagerIndex=" + mCurrentPagerIndex);
            }
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            SitechDevLog.w(AppConst.TAG_APP, "onPageSelected  position=" + position);
            mCurrentPagerIndex = position;
            if (MenuBundle.getInstance().getMainViewMenuListener() != null) {
                MenuBundle.getInstance().getMainViewMenuListener().refreshMainTagInChangePager();
            }
        }
    };
}