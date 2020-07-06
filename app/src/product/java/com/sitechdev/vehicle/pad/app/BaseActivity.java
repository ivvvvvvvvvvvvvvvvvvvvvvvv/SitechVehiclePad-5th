package com.sitechdev.vehicle.pad.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.SkinAppCompatDelegateImpl;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.routes.ARouter$$Group$$m_main;
import com.blankj.utilcode.util.BarUtils;
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;
import com.sitechdev.vehicle.lib.event.AppEvent;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.ActivityManager;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.view.CommonDialog;
import com.sitechdev.vehicle.pad.view.CommonProgressDialog;
import com.sitechdev.vehicle.pad.view.ToolbarView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：BaseActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/09 0009 20:08
 * 修改时间：
 * 备注：
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private CommonDialog.Builder mDialogBuilder;
    public ToolbarView mToolBarView;
    private static Map<String, String> activityPathHashMap = new HashMap<>();

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(super.getResources());
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //
        BarUtils.setStatusBarVisibility(this, false);
        BarUtils.setNavBarVisibility(this, false);

        TouchEffectsFactory.initTouchEffects(this);
        super.onCreate(savedInstanceState);
        setFullScreen();
        initToolBarView();
        setContentView(getLayoutId());
//        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
        EventBusUtils.register(this);
//        }
//        if (this.getClass().isAnnotationPresent(BindBus.class)) {
//            XtBusUtil.register(this);
//        }
        ActivityManager.getInstance().addActivity(this);
        initViewBefore();
        //无需检查权限
        initView(savedInstanceState);
        if (!checkPermission()) {
            initData();
            initListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapUtil.sendAMapInitBroadcast();
        BarUtils.setNavBarVisibility(this, false);
    }

    @SuppressLint("NewApi")
    public void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //去除半透明状态栏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                //一般配合fitsSystemWindows()使用, 或者在根部局加上属性android:fitsSystemWindows="true", 使根部局全屏显示
                getWindow().getDecorView().setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 返回Bundle参数
     *
     * @return bundle
     */
    public Bundle getBundle() {
        Intent mIntent = getIntent();
        if (mIntent == null) {
            return null;
        }
        return mIntent.getExtras();
    }

    public void initToolBarView() {
        mToolBarView = new ToolbarView(this, R.id.root_toolbar);
    }

    /**
     * 黑色状态栏文字
     */
    public void setDarkStatusBar() {
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        //一般配合fitsSystemWindows()使用, 或者在根部局加上属性android:fitsSystemWindows="true", 使根部局全屏显示
        getWindow().getDecorView().setSystemUiVisibility(option);
    }


    @LayoutRes
    protected abstract int getLayoutId();

    protected void initViewBefore() {
    }

    protected void initView(Bundle savedInstanceState) {
    }

    protected abstract void initData();

    protected void initListener() {
    }

    protected boolean checkPermission() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SitechDevLog.i("onDestroy", this + "*****onDestroy*****");
//        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
        EventBusUtils.unregister(this);
//        }
//        if (this.getClass().isAnnotationPresent(BindBus.class)) {
//            XtBusUtil.unregister(this);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SitechDevLog.i("task", "onNewIntent：" + getClass().getSimpleName() + " TaskId: " + getTaskId() + " hasCode:" + this.hashCode());
        dumpTaskAffinity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void dumpTaskAffinity() {
        try {
            ActivityInfo info = this.getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.i("task", "taskAffinity:" + info.taskAffinity);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showProgressDialog() {
        //进度窗口展示
        CommonProgressDialog.getInstance().show(this);
    }

    protected void showProgressDialog(String text) {
        //进度窗口展示
        CommonProgressDialog.getInstance().show(this, text);
    }

    public void cancelProgressDialog() {
        //进度窗口消失
        CommonProgressDialog.getInstance().cancel(this);
    }

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAppEvent(AppEvent event) {
        SitechDevLog.i(AppConst.TAG, "onAppEvent===>消息==" + event.getEventKey());
        switch (event.getEventKey()) {
            case AppEvent.EB_ACTIVITY_START_EVENT:
                SitechDevLog.i(AppConst.TAG, "onAppEvent===>消息Value==" + event.getEventValue());
                //{"module":"m_main","level":"subPage","group":"appList"}
                String pathParams = (String) event.getEventValue();
                if (StringUtils.isEmpty(pathParams)) {
                    return;
                }
                try {
                    JSONObject pathObject = new JSONObject(pathParams);
                    //取Path中的group
                    String group = pathObject.optString("group");
                    SitechDevLog.i(AppConst.TAG, "onAppEvent===>group==" + group);

                    //取当前页面中的path
                    String curPagePath = getActivityPath(this.getClass().getSimpleName());
                    SitechDevLog.i(AppConst.TAG, "onAppEvent===>当前页面 curPagePath==" + curPagePath + ", 匹配：className=" + this.getClass().getSimpleName());

                    if (StringUtils.isEmpty(curPagePath)) {
                        return;
                    }

                    //取当前页面中的group
                    String curPageGroup = getCurGroupName(curPagePath);
                    SitechDevLog.i(AppConst.TAG, "onAppEvent===>当前页面Group==" + curPageGroup + ", 匹配：className=" + this.getClass().getSimpleName());

                    //取当前页面的level
                    String level = getCurLevelName(curPagePath);
                    SitechDevLog.i(AppConst.TAG, "onAppEvent===>当前页面level==" + level + ", 匹配：className=" + this.getClass().getSimpleName());

                    if (!group.equalsIgnoreCase(curPageGroup) && RouterConstants.LEVEL_THIRD.equalsIgnoreCase("/" + level)) {
                        SitechDevLog.i(AppConst.TAG, "onAppEvent===>当前页面 为三级页面，且不同组，finish");
                        //不是同一组的，finish
                        finish();
                    }
                } catch (Exception e) {
                    SitechDevLog.exception(e);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取路由表
     *
     * @return Map<activityName, path>
     */
    private Map<String, String> getRouteMap() {
        if (!activityPathHashMap.isEmpty()) {
            return activityPathHashMap;
        }
        ARouter$$Group$$m_main c = new ARouter$$Group$$m_main();
        Map<String, RouteMeta> atlas = new HashMap<>();
        c.loadInto(atlas);
        Iterator<Map.Entry<String, RouteMeta>> routeMetaIterator = atlas.entrySet().iterator();
        SitechDevLog.e("zyf", "this getSimpleName() = " + this.getClass().getSimpleName());
        while (routeMetaIterator.hasNext()) {
            Map.Entry<String, RouteMeta> routeMetaEntry = routeMetaIterator.next();
            activityPathHashMap.put(((RouteMeta) routeMetaEntry.getValue()).getDestination().getSimpleName(), routeMetaEntry.getKey());
        }
        return activityPathHashMap;
    }

    private String getActivityPath(String aName) {
        if (activityPathHashMap.isEmpty()) {
            getRouteMap();
        }

        return activityPathHashMap.get(aName);
    }

    /**
     * 取出当前Class的group
     *
     * @return class group
     */
    private String getCurGroupName(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathParams = path.split("/");
        if (pathParams.length > 3) {
            return pathParams[3];
        }
        return "";
    }

    /**
     * 取出当前Class的level
     *
     * @return class level
     */
    private String getCurLevelName(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        String[] pathParams = path.split("/");
        if (pathParams.length > 2) {
            return pathParams[2];
        }
        return "";
    }

}
