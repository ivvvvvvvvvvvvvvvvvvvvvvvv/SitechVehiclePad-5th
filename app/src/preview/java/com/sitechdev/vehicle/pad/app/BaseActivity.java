package com.sitechdev.vehicle.pad.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.SkinAppCompatDelegateImpl;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.sitechdev.vehicle.lib.event.BindBus;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.event.XtBusUtil;
import com.sitechdev.vehicle.lib.util.ActivityManager;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.view.CommonDialog;
import com.sitechdev.vehicle.pad.view.CommonProgressDialog;
import com.sitechdev.vehicle.pad.view.ToolbarView;

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
        //
        BarUtils.setStatusBarVisibility(this, false);
        BarUtils.setNavBarVisibility(this, false);

        super.onCreate(savedInstanceState);
        setFullScreen();
        initToolBarView();
        setContentView(getLayoutId());
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.register(this);
        }
        if (this.getClass().isAnnotationPresent(BindBus.class)) {
            XtBusUtil.register(this);
        }
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
        //TODO 预览版本，强制横屏,除了首页
        SitechDevLog.i("BaseActivity", "当前类===>" + (this.getClass()) + "===>" + (this.getClass() != MainActivity.class));
        if (this.getClass() != MainActivity.class) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        BarUtils.setNavBarVisibility(this, false);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        SitechDevLog.i("BaseActivity", "onConfigurationChanged  当前类===>" + (this.getClass()));
//        super.onConfigurationChanged(newConfig);
//    }

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
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.unregister(this);
        }
        if (this.getClass().isAnnotationPresent(BindBus.class)) {
            XtBusUtil.unregister(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SitechDevLog.i("task", "*****onNewIntent()方法*****");
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
}
