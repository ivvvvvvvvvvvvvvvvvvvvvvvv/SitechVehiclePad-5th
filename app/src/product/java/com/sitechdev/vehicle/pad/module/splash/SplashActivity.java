package com.sitechdev.vehicle.pad.module.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.lib.util.ProcessUtil;
import com.sitechdev.vehicle.lib.util.TimeUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;

import java.util.List;

/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity {
    /**
     * Manifest.permission.ACCESS_COARSE_LOCATION=定位功能权限
     * Manifest.permission.WRITE_EXTERNAL_STORAGE=定位功能权限
     * Manifest.permission.READ_PHONE_STATE=获取手机相关设备信息，联网交互使用
     * Manifest.permission.RECORD_AUDIO=语音功能需要使用
     * Manifest.permission.READ_CONTACTS=语音功能需要使用
     * Manifest.permission.CALL_PHONE=语音功能需要使用
     */
    String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE};

    private List<String> rPermissionsList = null;

    private ImageView mLogoImageView = null, mLogoUnderImageView = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//
//        mLogoImageView = findViewById(R.id.id_iv_logo);
//        mLogoUnderImageView = findViewById(R.id.id_iv_logo_under);
//
//        GlideApp.with(this).load(R.drawable.ico_logo).into(mLogoImageView);
//        GlideApp.with(this).load(R.drawable.ico_logo_under).into(mLogoUnderImageView);
        initData();
    }

//    @Override
//    protected boolean checkPermission() {
//        rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
//        if (rPermissionsList == null || rPermissionsList.isEmpty()) {
//            return false;
//        } else {
//            //请求权限
//            PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && rPermissionsList.size() == grantResults.length) {
//            boolean isAllPermissionOK = true;
//            for (int i = 0; i < grantResults.length; i++) {
//                int result = grantResults[i];
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    //权限允许
//                    isAllPermissionOK = false;
//                    break;
//                }
//            }
//            if (isAllPermissionOK) {
//                initData();
//            } else {
//                rPermissionsList.clear();
//                rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
//                //请求权限
//                PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
//            }
//        }
//    }

    @Override
    protected void initData() {
//        initlogic();
//        runOnUiThread(() -> {
//            //初始化定位
//            LocationUtil.getInstance().init(AppApplication.getContext(), true);
//            //发送地图广播，获取一些初始化参数
//            MapUtil.sendAMapInitBroadcast();
        //页面跳转
//            AppUtil.clickTime = System.currentTimeMillis();

        Log.i("ActivityManager", "Displayed com.sitechdev.vehicle.pad.Splash startActivity: " +
                TimeUtils.formatTime(System.currentTimeMillis(), "HH:mm:ss.SSS") + " >>>"
                + ProcessUtil.getCurrentProcessName(this));
        Log.i("ActivityManager", "ActivityUtils.getActivityList().size()====" + ActivityUtils.getActivityList().size());
        if (ActivityUtils.getActivityList().size() <= 1) {
            RouterUtils.getInstance().navigationHomePage(RouterConstants.HOME_MAIN);
        } else {
            Activity activity = ActivityUtils.getActivityList().get(1);
            startActivity(new Intent(this, activity.getClass()));
        }
        finish();
//        });
    }

    private void initlogic() {
    }
}

