package com.sitechdev.vehicle.lib.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.util.R;


public abstract class BasicWindow {

    public WindowManager.LayoutParams mLayoutParams;
    public WindowManager mWindowManager;
    public View mView;
    public Context context;

    public void init(Context context) {
        this.context = context;
        initWindow();
        createView();
        initData();
        initListener();
    }


    protected void initWindow() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mLayoutParams.windowAnimations = R.style.sitech_window_anim;
//            mLayoutParams.x = 150;
            mLayoutParams.gravity = Gravity.CENTER;
            mLayoutParams.width = 1770;
            mLayoutParams.height = 720;
        }
    }

    private void createView() {
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(getLayoutId(), null);
            findView();
        }
    }

    public void initData() {

    }

    public abstract int getLayoutId();

    public abstract void findView();

    public void initListener() {

    }

    public void showWnd() {
        if (mWindowManager != null && mView != null && !mView.isShown()) {
            mWindowManager.addView(mView, mLayoutParams);
        }
    }

    public void hideWnd() {
        if (mWindowManager != null && mView != null && mView.isShown()) {
            mWindowManager.removeViewImmediate(mView);
        }
    }

    public boolean isShow() {
        return mWindowManager != null && mView != null && mView.isShown();
    }


}
