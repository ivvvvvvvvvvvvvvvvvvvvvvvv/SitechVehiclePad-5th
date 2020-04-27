package com.sitechdev.vehicle.pad.module.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.location.LocationUtil;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.util.PermissionHelper;
import com.sitechdev.vehicle.pad.vui.VUI;

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

        mLogoImageView = findViewById(R.id.id_iv_logo);
        mLogoUnderImageView = findViewById(R.id.id_iv_logo_under);

        GlideApp.with(this).load(R.drawable.ico_logo).into(mLogoImageView);
        GlideApp.with(this).load(R.drawable.ico_logo_under).into(mLogoUnderImageView);
    }

    @Override
    protected boolean checkPermission() {
        rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
        if (rPermissionsList == null || rPermissionsList.isEmpty()) {
            return false;
        } else {
            //请求权限
            PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && rPermissionsList.size() == grantResults.length) {
            boolean isAllPermissionOK = true;
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED) {
                    //权限允许
                    isAllPermissionOK = false;
                    break;
                }
            }
            if (isAllPermissionOK) {
                initData();
            } else {
                rPermissionsList.clear();
                rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
                //请求权限
                PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void initData() {
        initlogic();
        ThreadUtils.runOnUIThreadDelay(() -> {
            //初始化定位
            LocationUtil.getInstance().init(AppApplication.getContext(), true);
            //发送地图广播，获取一些初始化参数
            MapUtil.sendAMapInitBroadcast();
            //页面跳转
            Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mIntent);
            finish();
        }, 0);
    }

    private void initlogic() {
        //语音
        VUI.getInstance().start();
        MusicManager.getInstance().init(AppApplication.getContext());
        VoiceSourceManager.getInstance().init();
    }
}

