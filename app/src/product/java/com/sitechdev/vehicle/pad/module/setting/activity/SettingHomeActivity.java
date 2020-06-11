package com.sitechdev.vehicle.pad.module.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;

@Route(path = RouterConstants.SETTING_HOME_PAGE)
public class SettingHomeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("设置");
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.setting_home_item_skin).setOnClickListener(this);
        findViewById(R.id.setting_home_item_net).setOnClickListener(this);
        findViewById(R.id.setting_home_item_bt).setOnClickListener(this);
        findViewById(R.id.setting_home_item_teddy).setOnClickListener(this);
        findViewById(R.id.setting_home_item_system).setOnClickListener(this);
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            case R.id.rl_setting_vol:
                break;
            case R.id.setting_home_item_skin:
                startActivity(new Intent(this, SettingSkinActivity.class));
                break;
            case R.id.setting_home_item_teddy:
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.setting_home_item_bt:
                startActivity(new Intent(this, SettingBtActivity.class));
                break;
            case R.id.setting_home_item_system:
                startActivity(new Intent(this, SettingSystemActivity.class));
                break;
            case R.id.setting_home_item_net:
                startActivity(new Intent(this, SettingNetActivity.class));
                break;
            default:
                break;
        }
    }

}
