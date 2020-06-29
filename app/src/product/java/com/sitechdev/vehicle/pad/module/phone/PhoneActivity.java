package com.sitechdev.vehicle.pad.module.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FragmentUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.model.contract.PhoneContract;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.music.fragment.LocalMusicFragment;
import com.sitechdev.vehicle.pad.module.music.fragment.OtherMusicFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.CallLogFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.ContactFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.DialFragment;
import com.sitechdev.vehicle.pad.module.phone.presenter.PhonePresenter;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.TabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.PHONE_MAIN_PAGE)
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
        mTabLayout = findViewById(R.id.tv_sub_title);
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back: {
                finish();
            }
            break;
            default:
                break;
        }
    }

}
