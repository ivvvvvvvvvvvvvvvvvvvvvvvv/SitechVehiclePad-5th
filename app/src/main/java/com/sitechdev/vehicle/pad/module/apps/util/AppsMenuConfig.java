package com.sitechdev.vehicle.pad.module.apps.util;

import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.adapter.MainMenuAdapater;
import com.sitechdev.vehicle.pad.module.apps.view.AppViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：AppViewPagerUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/15 0015 13:47
 * 修改时间：
 * 备注：
 */
public class AppsMenuConfig {
    /**
     * 主界面Gridview最大每页的菜单数
     **/
    public static final int MAX_GRIDVIEW_SIZE = 12;
    /**
     * 菜单的回调接口
     */
    private MainViewMenuListener listener = null;
    /**
     * true=进入编辑状态
     */
    public static boolean isAppsEditStatus = false;
    /**
     * 装载移动item的窗口容器
     */
    public static WindowManager windowManager = null;
    /**
     * 记录当前的触摸点击事件
     */
    public static boolean isItemTouching = false;
    /**
     * windows里面放置的gridview的item的镜像
     */
    public static ImageView mMoveImageView;
    /**
     * pager变换
     */
    public static boolean mPagerChanged = false;
    /**
     * pager变换方向 >0加1,<0减1
     */
    public static int mPagerDirection = 0;
    /**
     * 记录当前的触摸点击事件
     */
    public static int isItemTouchCount = 0;
    /**
     * 是否长按状态
     */
    public static boolean mLongClick = false;
    /**
     * 是否重构了viewpager界面
     */
    public static boolean updateViewPager = false;
    /**
     * handler
     */
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * 移动item动画结束状态
     */
    public static boolean mAnimationEnd = true;
    /**
     * 存储所有的移动事件线程
     */
    public static List<Runnable> moveRunnableLists = new ArrayList<Runnable>();
    /**
     * 主界面每一页GridView的Adapater的集合
     */
    public static List<MainMenuAdapater> mAllMenuAdapterList = new ArrayList<MainMenuAdapater>();
    /**
     * 总的菜单数据的list
     */
    public static List<AllModuleBean.ModuleBean> mAllMenuBeanList = new ArrayList<AllModuleBean.ModuleBean>();


    private AppsMenuConfig() {
    }

    private static final class SingleAppViewPagerUtil {
        private static final AppsMenuConfig SINGLE = new AppsMenuConfig();
    }

    public static AppsMenuConfig getInstance() {
        return SingleAppViewPagerUtil.SINGLE;
    }

    public MainViewMenuListener getListener() {
        return listener;
    }

    public void setListener(MainViewMenuListener listener) {
        this.listener = listener;
    }

    /**
     * 停止移动的处理
     */
    public static synchronized void onStopMove() {
        if (!moveRunnableLists.isEmpty()) {
            for (Runnable childRunnable : moveRunnableLists) {
                mHandler.removeCallbacks(childRunnable);
            }
            moveRunnableLists.clear();
        }
        AppViewPager.isTouchResponse = true;
        // adapter元素刷新
        for (MainMenuAdapater menuAdapater : mAllMenuAdapterList) {
            // refresh all adapter
            menuAdapater.setHideItem(AdapterView.INVALID_POSITION);
        }
        mAnimationEnd = true;
        removeDragImage();
    }

    /**
     * 从界面上面移除拖动window镜像
     */
    public static void removeDragImage() {
        if (mMoveImageView != null && mMoveImageView.getParent() != null) {
            isItemTouching = false;
            isItemTouchCount = 0;
            windowManager.removeView(mMoveImageView);
            mMoveImageView = null;
        }
    }

    public static void setLongClick(boolean click) {
        mLongClick = click;
    }

    ;

    /**
     * 根据mMovePosition获取所点击的item
     *
     * @param postion 索引
     * @return
     */
    public static AllModuleBean.ModuleBean getModuleBeanMenuByPosition(int postion) {
        return mAllMenuBeanList.get(postion);
    }

    /**
     * 移除dom树的指定索引处的一个元素
     *
     * @return
     */
    public static void removeElementPosition(int position) {
        if (mAllMenuBeanList != null && !mAllMenuBeanList.isEmpty()) {
            mAllMenuBeanList.remove(position);
        }
    }

    /**
     * 将元素插入到dom树的指定索引
     *
     * @param element
     * @param position
     */
    public static void insertElement2Position(int position, AllModuleBean.ModuleBean element) {
        if (mAllMenuBeanList != null) {
            mAllMenuBeanList.add(position, element);
        }
    }

    /**
     * 获取一个全新的list，与主list是两个对象
     *
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return
     */
    public static List<AllModuleBean.ModuleBean> getNewSubList(int startIndex, int endIndex) {
        List<AllModuleBean.ModuleBean> moduleBeanList = new ArrayList<>();
        if (mAllMenuBeanList == null || mAllMenuBeanList.isEmpty() || startIndex > endIndex) {
            return moduleBeanList;
        }
        for (int i = startIndex; i < endIndex; i++) {
            moduleBeanList.add(mAllMenuBeanList.get(i));
        }
        return moduleBeanList;
    }
}
