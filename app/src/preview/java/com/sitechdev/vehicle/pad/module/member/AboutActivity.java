package com.sitechdev.vehicle.pad.module.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.PackageInfoUtils;
import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolBarView.setLeftImageClickListener(v -> finish());
        mToolBarView.setMainTitle(R.string.string_page_about_title);

        TextView mAppVersionView = findViewById(R.id.id_tv_app_version);
        mAppVersionView.setText(getString(R.string.string_app_version, PackageInfoUtils.getAppVersionName(), BuildConfig.AIUI_APPID));

    }

    @Override
    protected void initData() {

    }
}
