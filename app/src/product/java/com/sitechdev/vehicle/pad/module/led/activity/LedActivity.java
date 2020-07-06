package com.sitechdev.vehicle.pad.module.led.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.model.contract.LedContract;
import com.sitechdev.vehicle.pad.module.led.presenter.LedPresenter;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.phone.fragment.CallLogFragment;
import com.sitechdev.vehicle.pad.module.phone.fragment.ContactFragment;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.TabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.SETTING_LED_MANAGER)
public class LedActivity extends MvpActivity<LedContract.Presenter> implements LedContract.View {
    private TabLayout mTab;
    private ViewPager mVp;
    String[] titleArr = new String[]{"常用表情", "内置图片", "动图", "文字"};
    private static final int EDIT_STATE_COMMON = 0;
    private static final int EDIT_STATE_PIC = 1;
    private static final int EDIT_STATE_PGIF = 2;
    private static final int EDIT_STATE_TEXT = 3;

    private TextView mConnectTv;
    private TextView mEditTv;
    private View mEditLayout;

    @Override
    protected LedPresenter createPresenter() {
        return new LedPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mTab = findViewById(R.id.led_tablayout);
        mVp = findViewById(R.id.led_vp);
        mConnectTv = findViewById(R.id.led_connect_state);
        mEditTv = findViewById(R.id.led_edit);
        mEditLayout = findViewById(R.id.led_edit_layout);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ContactFragment.newInstance());
        fragments.add(CallLogFragment.newInstance());
        fragments.add(ContactFragment.newInstance());
        fragments.add(CallLogFragment.newInstance());
        mTab.setupWithViewPager(mVp);
        MusicPagerAdapter adapter = new MusicPagerAdapter(getSupportFragmentManager(), fragments);
        mVp.setAdapter(adapter);
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            tab.setText(titleArr[i]);
        }
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                showEditState(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_led;
    }

    @Override
    protected void initData() {
        ((TextView) findViewById(R.id.tv_sub_title)).setText("LED管理");
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mEditLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back: {
                finish();
            }
            break;
            case R.id.led_edit_layout: {
                //管理文字
                if (mEditTv.isSelected()) {

                } else {//常用表情管理

                }
            }
            break;
            default:
                break;
        }

    }

    private void showEditState(int state) {
        switch (state) {
            case EDIT_STATE_COMMON:
                mEditLayout.setVisibility(View.VISIBLE);
                mEditTv.setSelected(false);
                mEditTv.setText("编辑");
                break;
            case EDIT_STATE_TEXT:
                mEditLayout.setVisibility(View.VISIBLE);
                mEditTv.setSelected(true);
                mEditTv.setText("添加");
                break;
            default:
                mEditLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void showConnectState(String name) {
        mConnectTv.setSelected(!StringUtils.isEmpty(name));
        if (StringUtils.isEmpty(name)) {
            mConnectTv.setText("未连接");
        } else {
            mConnectTv.setText(name);
        }
    }
}
