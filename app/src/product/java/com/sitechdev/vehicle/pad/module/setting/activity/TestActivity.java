package com.sitechdev.vehicle.pad.module.setting.activity;

import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;

public class TestActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_teddy_test;
    }

    @Override
    protected void initData() {
        ((TextView) findViewById(R.id.tv_sub_title)).setText("语音设置");
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_sub_back) {
            finish();
        }
    }
}
