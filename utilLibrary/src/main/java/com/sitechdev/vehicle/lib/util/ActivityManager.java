package com.sitechdev.vehicle.lib.util;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Stack;

/**
 * 管理所有栈中的Activity
 *
 * @author wxb
 * @date 2019/4/23
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;

    private static ActivityManager instance;

    private HashMap<String, Activity> activityHashMap = null;

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (CollectionUtils.isEmpty(activityStack)) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (CollectionUtils.isEmpty(activityStack)) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !CollectionUtils.isEmpty(activityStack)) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && !CollectionUtils.isEmpty(activityStack)) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (CollectionUtils.isEmpty(activityStack)) {
            return;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public boolean isExistActivity(Class<?> cls) {
        if (CollectionUtils.isEmpty(activityStack)) {
            return false;
        }
        for (Activity activity : activityStack) {
            if (null != activity && activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (CollectionUtils.isEmpty(activityStack)) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
