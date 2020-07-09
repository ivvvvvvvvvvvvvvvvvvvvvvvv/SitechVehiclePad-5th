package com.sitechdev.vehicle.pad.module.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.contract.PhoneContract;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.phone.fragment.CallLogFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.ContactFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.DialFragment;
import com.sitechdev.vehicle.pad.module.phone.presenter.PhonePresenter;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.view.TabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.PHONE_MAIN_PAGE)
@BindEventBus
public class PhoneActivity extends MvpActivity<PhoneContract.Presenter> implements PhoneContract.View {
    private TabLayout mTabLayout;
    private ViewPager mPager;
    String[] titleArr = new String[]{"通讯录", "最近通话"};

    @Override
    protected PhoneContract.Presenter createPresenter() {
        return new PhonePresenter();
    }

    private void logTest(String msg) {
        Log.e("Test_PhoneActivity", "-----" + msg);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        findViewById(R.id.iv_phone_sub_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
            case R.id.iv_phone_sub_back:
                finish();
                break;
            case R.id.btn_phone_bluetooth:
                RouterUtils.getInstance().navigationNoFinish(RouterConstants.SETTING_BT_PAGE);
                break;
            default:
                break;
        }
    }

    private void showBtConnectContent() {
        findViewById(R.id.phone_bt_connect_content_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.phone_bt_dis_content_layout).setVisibility(View.GONE);
        mTabLayout = findViewById(R.id.tv_sub_title_tab);
        mPager = findViewById(R.id.phone_view_pager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ContactFragment.newInstance());
        fragments.add(CallLogFragment.newInstance());
        mTabLayout.setupWithViewPager(mPager);
        MusicPagerAdapter adapter = new MusicPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(adapter);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setText(titleArr[i]);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.phone_dialog, DialFragment.newInstance()).commit();
    }

    private void showBtDisConnectContent() {
        findViewById(R.id.phone_bt_connect_content_layout).setVisibility(View.GONE);
        findViewById(R.id.phone_bt_dis_content_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_phone_bluetooth).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("电话");
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingConfig.getInstance().isBtConnected()) {
            showBtConnectContent();
        } else {
            showBtDisConnectContent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEventChange(SysEvent event) {
        switch (event.getEvent()) {
            case SysEvent.EB_SYS_BT_STATE:
                if (event.getObj() != null) {
                    boolean status = (boolean) event.getObj();
                    if (status) {
                        showBtConnectContent();
                    } else {
                        showBtDisConnectContent();
                    }
                }
                break;
            default:
                break;
        }
    }
}
