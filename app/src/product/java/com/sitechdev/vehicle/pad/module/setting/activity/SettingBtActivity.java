package com.sitechdev.vehicle.pad.module.setting.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.my.hw.BtDeviceBean;
import com.my.hw.OnBtPairListChangeListener;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.contract.SettingBtContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;
import com.sitechdev.vehicle.pad.module.setting.bt.BtListAdapter;
import com.sitechdev.vehicle.pad.module.setting.presenter.SettingBtPresenter;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.BluetoothListView;
import com.sitechdev.vehicle.pad.view.CommonProgressDialog;
import com.sitechdev.vehicle.pad.view.CustomSwitchButton;
import com.sitechdev.vehicle.pad.window.view.CommonLogoutDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@BindEventBus
@Route(path = RouterConstants.SETTING_BT_PAGE)
public class SettingBtActivity extends MvpActivity<SettingBtContract.BtPresenter> implements SettingBtContract.View, CustomSwitchButton.OnSwitchCheckChangeListener, OnBtPairListChangeListener {

    private CustomSwitchButton mBtEnableSwitch, mDiscovereSwitch;
    private TextView mBtName;
    private BluetoothListView mListView;
    private View mListEmpty;
    private List<BtDeviceBean> mBondList;
    private BtListAdapter mBondAdapter;
    private View mListLayout;
    private Handler mHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_bt;
    }

    @Override
    protected void initData() {
        mPresenter.init();
        mBtName.setText(SettingConfig.getInstance().getLocalBtName());
        mBondList = new ArrayList<>();
        mBondAdapter = new BtListAdapter(this, mBondList);
        mListView.setAdapter(mBondAdapter);
        mPresenter.showBtPairList();
        mBondAdapter.setOnBtClickListener(new BtListAdapter.OnBtItemClickListener() {
            @Override
            public void onDelete(int pos, boolean isCurrent, BtDeviceBean btDeviceBean) {
                mBondList.remove(pos);
                mBondAdapter.notifyList(mBondList);
                if (isCurrent) {
                    mPresenter.disconnectToDevice();
                }
                mPresenter.clearPairInfo(btDeviceBean.getBtAddress());
            }

            @Override
            public void onConnect(int pos, BtDeviceBean btDeviceBean) {
                if (SettingConfig.getInstance().isBtConnected()) {
                    //断开连接
                    mPresenter.disconnectToDevice();
                    Message message = new Message();
                    message.what = 0;
                    message.obj = btDeviceBean;
                    //重新连接新的需要隔一段时间，所以延迟进行
                    mHandler.sendMessageDelayed(message, 2000);
                } else {
                    mPresenter.connectToDevice(btDeviceBean.getBtAddress());
                    CommonProgressDialog.getInstance().show(SettingBtActivity.this);
                }
            }

            @Override
            public void onDisconnect(int pos, BtDeviceBean btDeviceBean) {
                mPresenter.disconnectToDevice();
            }
        });
        mDiscovereSwitch.setChecked(false);
        mBtEnableSwitch.setChecked(mPresenter.isBtEnable());
        mListLayout.setVisibility(mBtEnableSwitch.isChecked() ? View.VISIBLE : View.GONE);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    if (null != msg.obj && msg.obj instanceof BtDeviceBean) {
                        CommonProgressDialog.getInstance().show(SettingBtActivity.this);
                        BtDeviceBean deviceBean = (BtDeviceBean) msg.obj;
                        mPresenter.connectToDevice(deviceBean.getBtAddress());
                    }
                }
            }
        };
        mPresenter.enableDiscoverable(false);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("蓝牙设置");
        mBtEnableSwitch = findViewById(R.id.setting_bt_enable);
        mDiscovereSwitch = findViewById(R.id.setting_bt_discovered);
        mBtName = findViewById(R.id.setting_bt_name);
        mListView = findViewById(R.id.setting_bt_listview);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListEmpty = findViewById(R.id.setting_bt_list_empty);
        mListLayout = findViewById(R.id.setting_bt_list_layout);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mBtEnableSwitch.setOnSwitchChangeListener(this);
        mDiscovereSwitch.setOnSwitchChangeListener(this);
        mPresenter.registerPaireListCallback(this);
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
    protected SettingBtContract.BtPresenter createPresenter() {
        return new SettingBtPresenter();
    }

    @Override
    public void showBtList(List<BtDeviceItem> list) {
    }

    private void logTest(String msg) {
        Log.e("TEST_SettingBtActivity", "-----" + msg);
    }

    @Override
    public void onSwithChecked(int viewId, boolean isChecked) {
        if (viewId == R.id.setting_bt_enable) {
            mDiscovereSwitch.setEnabled(isChecked);
            if (isChecked) {
                mPresenter.openBt(this);
                mListLayout.setVisibility(View.VISIBLE);
                logTest("openBle activity");
            } else {
                mPresenter.closeBt();
                mListLayout.setVisibility(View.GONE);
                mDiscovereSwitch.setChecked(false);
                logTest("closeBle activity");
            }
        } else if (viewId == R.id.setting_bt_discovered) {
            mPresenter.enableDiscoverable(isChecked);
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

    @Override
    protected void onPause() {
        super.onPause();
        CommonProgressDialog.getInstance().cancel(SettingBtActivity.this);
    }

    @Override
    public void onBtPairListChanged(final BtDeviceBean btDeviceBean, final boolean isPlus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isContains = false;
                for (BtDeviceBean deviceBean : mBondList) {
                    if (deviceBean.getBtAddress().equals(btDeviceBean.getBtAddress())) {
                        isContains = true;
                        continue;
                    }
                }
                if (!isContains && isPlus) {
                    mBondList.add(btDeviceBean);
                } else if (!isPlus && isContains) {
                    mBondList.remove(btDeviceBean);
                }
                mBondAdapter.notifyList(mBondList);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBtEvent(SysEvent event) {
        switch (event.getEvent()) {
            case SysEvent.EB_SYS_BT_ENABLE: {//蓝牙开关
                boolean open = (boolean) event.getObj();
                mBtEnableSwitch.setChecked(open);
            }
            break;
            case SysEvent.EB_SYS_BT_STATE: {//蓝牙连接状态
                boolean isConnected = (boolean) event.getObj();
                CommonProgressDialog.getInstance().cancel(SettingBtActivity.this);
                if (isConnected && !TextUtils.isEmpty(SettingConfig.getInstance().getConnectBtAdd())) {
                    mDiscovereSwitch.setChecked(false);
                    if (null != mBondList && mBondList.size() > 0) {
                        for (int i = 0; i < mBondList.size(); i++) {
                            BtDeviceBean deviceBean = mBondList.get(i);
                            if (deviceBean.getBtAddress().equals(SettingConfig.getInstance().getConnectBtAdd())) {
                                mBondList.remove(i);
                                mBondList.add(0, deviceBean);
                                continue;
                            }
                        }
                        mBondAdapter.notifyList(mBondList);
                    }
                } else {
                    mBondAdapter.notifyList(mBondList);
                }
            }
            break;
            default:
                break;
        }
    }

}
