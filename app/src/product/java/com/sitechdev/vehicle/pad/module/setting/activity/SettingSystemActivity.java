package com.sitechdev.vehicle.pad.module.setting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.SettingHomeItemView;

@Route(path = RouterConstants.SETTING_SYSTEM_PAGE)
public class SettingSystemActivity extends BaseActivity {
    private SettingHomeItemView mSoftVersion;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_system;
    }

    @Override
    protected void initData() {
        mSoftVersion.setContentText(String.format("v%s", BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("系统设置");
        mSoftVersion = findViewById(R.id.setting_system_soft_version);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            default:
                break;
        }
    }
}
