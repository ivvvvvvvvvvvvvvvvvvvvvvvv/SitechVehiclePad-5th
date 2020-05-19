package com.sitechdev.vehicle.pad.module.apps.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.contract.AllAppsContract;
import com.sitechdev.vehicle.pad.module.apps.adapter.MainMenuAdapater;
import com.sitechdev.vehicle.pad.module.apps.util.MainViewMenuListener;
import com.sitechdev.vehicle.pad.module.apps.adapter.MainViewPagerAdapter;
import com.sitechdev.vehicle.pad.module.apps.util.MenuBundle;
import com.sitechdev.vehicle.pad.module.apps.model.AllAppsModel;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;

import java.util.List;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：TaximeterActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SETTING_APP_LIST)
public class AppsListGridActivity extends BaseActivity {

    private AppViewPager mAppViewPager = null;
    private MainViewPagerAdapter appsViewPagerAdapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_grid;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mAppViewPager = findViewById(R.id.id_all_apps_vp);

    }

    @Override
    protected void initListener() {
        super.initListener();
//        mAppViewPager.setPageTransformer(true, new ViewpagerTransformer());
    }

    @Override
    protected void initData() {
        // 菜单变换接口
        MenuBundle.getInstance().setMainViewMenuListener(mainViewPagerListener);
        AllAppsModel appsModel = new AllAppsModel();
        appsModel.getAllModuleData(new AllAppsContract.AllModuleCallback() {
            @Override
            public void onSuccess(AllModuleBean bean) {
                AppsMenuConfig.mAllMenuBeanList = bean.apps;
                initRecycleData();
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    /**
     *
     */
    private void initRecycleData() {
        //计算一共需要几个RecycleView
        int appCount = AppsMenuConfig.mAllMenuBeanList.size();
        boolean isAddPage = false;
        int recycleViewCount = appCount / AppsMenuConfig.MAX_GRIDVIEW_SIZE;
        if (appCount > 0 && appCount % AppsMenuConfig.MAX_GRIDVIEW_SIZE != 0) {
            recycleViewCount++;
            isAddPage = true;
        }
        SitechDevLog.i(AppConst.TAG_APP, "一共需要 " + recycleViewCount + " 个RecycleView");

        //初始化单个页面容器
        for (int i = 0; i < recycleViewCount; i++) {
            //开始的索引
            int startAppIndex = i * AppsMenuConfig.MAX_GRIDVIEW_SIZE;
            //结束的索引
            int endAppIndex = (((i == (recycleViewCount - 1)) && isAddPage) ? (AppsMenuConfig.mAllMenuBeanList.size() % AppsMenuConfig.MAX_GRIDVIEW_SIZE) : AppsMenuConfig.MAX_GRIDVIEW_SIZE);
            //新的子list
            // 声明具体的每一页的adapter
            MainMenuAdapater menuAdapter = new MainMenuAdapater(this, startAppIndex, startAppIndex + endAppIndex);
            AppPagerMenuGridView gridView = getMainGridView(menuAdapter);
            // 加到menu adapter list中
            AppsMenuConfig.mAllMenuAdapterList.add(menuAdapter);
            // Viewpager内容
            AppViewPager.mAllMenuGridViewList.add(gridView);
            // 底部tag
            addMainBottomTag();
        }
        initViewpagerParmeter();
    }

    /**
     * 初始化viewpager事件
     */
    private void initViewpagerParmeter() {
        if (appsViewPagerAdapter == null) {
            appsViewPagerAdapter = new MainViewPagerAdapter(AppViewPager.mAllMenuGridViewList);
            mAppViewPager.setAdapter(appsViewPagerAdapter);
        }
    }

    /**
     * 得到一个gridview
     *
     * @return gridview
     */
    private AppPagerMenuGridView getMainGridView(MainMenuAdapater menuAdapter) {
        AppPagerMenuGridView gridView = (AppPagerMenuGridView) View.inflate(this, R.layout.main_menu_grid_view, null);
        if (menuAdapter != null) {
            // 设置适配器
            gridView.setAdapter(menuAdapter);
        }
        return gridView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        exitRecycleViewEditStatus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitRecycleViewEditStatus();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出recycleView的编辑状态
     */
    private void exitRecycleViewEditStatus() {
        AppsMenuConfig.isAppsEditStatus = false;
    }

//    /**
//     * 页面切换事件
//     *
//     * @param isNext true=切换到下一页
//     */
//    @Override
//    public void onPageChange(boolean isNext) {
//        SitechDevLog.w(AppConst.TAG_APP, "onPageChange ==" + (isNext ? "切换到下一页" : "切换到上一页"));
//        int curPageIndex = mAppViewPager.getCurrentItem();
//        if (isNext && curPageIndex < appsViewPagerAdapter.getCount() - 1) {
//            curPageIndex++;
//            //切换到下一页
//            mAppViewPager.setCurrentItem(curPageIndex);
//        } else if (!isNext && curPageIndex > 0) {
//            curPageIndex--;
//            //切换到上一页
//            mAppViewPager.setCurrentItem(curPageIndex);
//        } else {
//            SitechDevLog.w(AppConst.TAG_APP, "已到viewpager临界点");
//        }
//    }

    // 菜单增、删、移
    private MainViewMenuListener mainViewPagerListener = new MainViewMenuListener() {

        /**
         * 添加一个菜单到list中
         *
         * @param menuElement 需要添加的菜单对象
         */
        @Override
        public void onAddMenu(AllModuleBean.ModuleBean menuElement) {

        }

        /**
         * 添加一个菜单list到list中
         *
         * @param menuElementlist 需要添加的菜单list
         */
        @Override
        public void onAddMenuList(List<AllModuleBean.ModuleBean> menuElementlist) {

        }

        /**
         * 删除指定索引一个菜单
         *
         * @param position 需要删除的菜单的索引
         */
        @Override
        public void onDeleteMenu(int position) {

        }

        /**
         * 删除指定一个菜单
         *
         * @param menuElement 需要删除的菜单对象
         */
        @Override
        public void onDeleteMenu(AllModuleBean.ModuleBean menuElement) {

        }

        /**
         * 长按时刷新主界面
         */
        @Override
        public void refreshMainViewLongClick() {
            SitechDevLog.w(AppConst.TAG_APP, "长按刷新主界面  长按刷新主界面");
//            refreshMainViewHead();
//            longClickUtil();
        }

        /**
         * 刷新主界面
         */
        @Override
        public void refreshMainView() {
            SitechDevLog.w(AppConst.TAG_APP, "刷新主界面  刷新主界面");
            resetMainView();
            refreshViewPager();
        }

        /**
         * 滑动切换页面时刷新下标
         */
        @Override
        public void refreshMainTagInChangePager() {
            SitechDevLog.w(AppConst.TAG_APP, "refreshMainTagInChangePager");
            refreshBottomTag();
        }

        /**
         * 移动item时viewpager滑动界面
         */
        @Override
        public void dragViewPager() {
            SitechDevLog.w(AppConst.TAG_APP, "滑动界面  滑动viewpager界面");
            runOnUiThread(() -> {
                if (AppsMenuConfig.mAllMenuAdapterList.size() > 1) {
                    AppViewPager.mLastPagerIndex = AppViewPager.mCurrentPos;
                    if (AppsMenuConfig.mPagerDirection > 0) {
                        AppViewPager.mCurrentPos = AppViewPager.mLastPagerIndex + 1;
//                        if (AppViewPager.mCurrentPos > AppViewPagerUtil.mAllMenuAdapterList.size()  - 1) {
//                            AppViewPager.mCurrentPos = 0;
//                            AppViewPagerUtil.mPagerDirection = 0 - AppViewPagerUtil.mPagerDirection;
//                        }
                        if (AppViewPager.mCurrentPos > AppsMenuConfig.mAllMenuAdapterList.size()) {
                            AppViewPager.mCurrentPos = AppViewPager.mLastPagerIndex;
//                            AppViewPagerUtil.mPagerDirection = 0 - AppViewPagerUtil.mPagerDirection;
                            //当前已经是最后一页，不再滑动
                            return;
                        }
                    } else if (AppsMenuConfig.mPagerDirection < 0) {
                        AppViewPager.mCurrentPos = AppViewPager.mLastPagerIndex - 1;
//                        if (AppViewPager.mCurrentPos < 0) {
//                            AppViewPager.mCurrentPos = AppViewPagerUtil.mAllMenuAdapterList.size() - 1;
//                            AppViewPagerUtil.mPagerDirection = 0 - AppViewPagerUtil.mPagerDirection;
//                        }
                        if (AppViewPager.mCurrentPos < 0) {
                            AppViewPager.mCurrentPos = AppViewPager.mLastPagerIndex;
                            //当前已经是第一页，不再滑动
                            return;
                        }
                    }
                    AppsMenuConfig.mPagerChanged = true;
                    // LoggerUtil.warn("滑动界面mLastPagerIndex", "之前页索引" +
                    // CyberViewPager.mLastPagerIndex);
                    // LoggerUtil.warn("滑动界面mCurrentPos", "当前页索引" +
                    // CyberViewPager.mCurrentPos);
                    mAppViewPager.setCurrentItem(AppViewPager.mCurrentPos, false);
                    AppViewPager.mCurrentPagerIndex = mAppViewPager.getCurrentItem();
                    // LoggerUtil.warn("dragViewPagerPagerIndex==", "#" +
                    // CyberViewPager.mCurrentPagerIndex);
                    refreshBottomTag();
                }
            });
        }

        /**
         * 推荐页面时跳转
         */
        @Override
        public void gotoRecommendActivity() {
            SitechDevLog.w(AppConst.TAG_APP, "跳转界面  跳转至应用推荐页面");
        }
    };

    /**
     * 重置主界面布局
     */
    private void resetMainView() {
        // CompareMenuUtil.compareMenus();
        resetMainViewList();
        // 重组数据，刷新
        initRecycleData();
    }

    /**
     * clear list
     */
    private void resetMainViewList() {
        // 重组刷新界面操作
        if (AppsMenuConfig.mAllMenuAdapterList != null) {
            AppsMenuConfig.mAllMenuAdapterList.clear();
        }
        // Viewpager内容
        if (AppViewPager.mAllMenuGridViewList != null) {
            AppViewPager.mAllMenuGridViewList.clear();
        }
        // 下标 todo
//        if (mainTag != null) {
//            mainTag.removeAllViews();
//        }
    }

    /**
     * 刷新Viewpager
     */
    private void refreshViewPager() {
        SitechDevLog.w(AppConst.TAG_APP, "refreshViewPager 刷新 主界面viewpager");
        mAppViewPager.setAdapter(appsViewPagerAdapter);
        appsViewPagerAdapter.notifyDataSetChanged();
        refreshBottomTag();
    }

    /**
     * 刷新底部标签
     * TODO
     */
    private void refreshBottomTag() {
//        int count = mainTag.getChildCount();
//        for (int i = 0; i < count; i++) {
//            ImageView imgTag = (ImageView) mainTag.getChildAt(i);
//            if (CyberViewPager.mCurrentPagerIndex == i) {
//                imgTag.setImageResource(R.drawable.menu_selected);
//            } else {
//                imgTag.setImageResource(R.drawable.menu_unselected);
//            }
//        }
    }

    private void addMainBottomTag() {
    }
}