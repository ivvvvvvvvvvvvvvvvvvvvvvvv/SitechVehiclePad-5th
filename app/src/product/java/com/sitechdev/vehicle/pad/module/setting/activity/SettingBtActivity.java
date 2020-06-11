package com.sitechdev.vehicle.pad.module.setting.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.model.contract.SettingBtContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;
import com.sitechdev.vehicle.pad.module.setting.bt.PairListAdapter;
import com.sitechdev.vehicle.pad.module.setting.presenter.SettingBtPresenter;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.BluetoothListView;
import com.sitechdev.vehicle.pad.view.CustomSwitchButton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.SETTING_BT_PAGE)
public class SettingBtActivity extends MvpActivity<SettingBtContract.BtPresenter> implements SettingBtContract.View, CustomSwitchButton.OnSwitchCheckChangeListener {

    private CustomSwitchButton mBtEnableSwitch, mDiscovereSwitch;
    private TextView mBtName;
    private BluetoothListView mListView;
    private View mListEmpty;
    private List<BtDeviceItem> mBondList;
    private PairListAdapter mBondAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_bt;
    }

    @Override
    protected void initData() {
        mBtName.setText(mPresenter.getLocalName());
        mBondList = new ArrayList<>();
        mBondAdapter = new PairListAdapter(this);
        mListView.setAdapter(mBondAdapter);
        mBondAdapter.setOnClickListener(new PairListAdapter.OnClickListener() {
            @Override
            public void onDelete(int pos) {
                removeBond(mBondList.get(pos).getDevice());
            }

            @Override
            public void onConnect(int pos) {

            }

            @Override
            public void onDisconnect() {

            }
        });
        mBtEnableSwitch.setEnabled(mPresenter.isBtConnected());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("蓝牙设置");
        mBtEnableSwitch = findViewById(R.id.setting_bt_enable);
        mDiscovereSwitch = findViewById(R.id.setting_bt_discovered);
        mBtName = findViewById(R.id.setting_bt_name);
        mListView = findViewById(R.id.setting_bt_listview);
        mListEmpty = findViewById(R.id.setting_bt_list_empty);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mBtEnableSwitch.setOnSwitchChangeListener(this);
        mDiscovereSwitch.setOnSwitchChangeListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.registerReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unRegisterReceiver(this);
    }

    @Override
    protected SettingBtContract.BtPresenter createPresenter() {
        return new SettingBtPresenter();
    }

    @Override
    public void showBtList(List<BtDeviceItem> list) {
        mBondList.clear();
        mBondList.addAll(list);
        mBondAdapter.notifList(mBondList);
    }

    private void logTest(String msg) {
        Log.e("TEST_SettingBtActivity", "-----" + msg);
        ToastUtils.showShort(msg);
    }

    @Override
    public void onSwithChecked(int viewId, boolean isChecked) {
        if (viewId == R.id.setting_bt_enable) {
            mDiscovereSwitch.setEnabled(isChecked);
            if (isChecked) {
                mPresenter.openBt(this);
                mPresenter.showBtPairList();
                logTest("openBle activity");
            } else {
                mPresenter.closeBt();
                logTest("closeBle activity");
                mDiscovereSwitch.setChecked(false);
            }
        } else if (viewId == R.id.setting_bt_discovered) {
            mPresenter.enableDiscoverable(isChecked);
            mPresenter.showBtPairList();
        }
    }

    public void removeBond(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
