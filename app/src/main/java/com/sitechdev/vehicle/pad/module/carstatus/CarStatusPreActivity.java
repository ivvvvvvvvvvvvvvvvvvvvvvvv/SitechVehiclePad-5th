package com.sitechdev.vehicle.pad.module.carstatus;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SUB_APP_CAR_STATUS)
public class CarStatusPreActivity extends BaseActivity {


    private TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_car_status;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.tv_sub_title);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("车辆状态");
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
