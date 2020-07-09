package com.sitechdev.vehicle.pad.module.setting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConfig;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.CustomSwitchButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
@Route(path = RouterConstants.SETTING_TEDDY_PAGE)
public class SettingTeddyActivity extends BaseActivity implements CustomSwitchButton.OnSwitchCheckChangeListener {

    private CustomSwitchButton mEnableWakeup;
    private CustomSwitchButton mEnableContinue;
    private CustomSwitchButton mVoiceSex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_teddy;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mEnableWakeup = findViewById(R.id.setting_teddy_enable_wakeup);
        mEnableContinue = findViewById(R.id.setting_teddy_session);
        mVoiceSex = findViewById(R.id.setting_teddy_sex);
    }

    @Override
    protected void initData() {
        ((TextView) findViewById(R.id.tv_sub_title)).setText("语音设置");
        mEnableWakeup.setChecked(TeddyConfig.getAutoMVWStatus());
        mVoiceSex.setChecked(TeddyConfig.getSexIsMale());
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mEnableWakeup.setOnSwitchChangeListener(this);
        mEnableContinue.setOnSwitchChangeListener(this);
        mVoiceSex.setOnSwitchChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_sub_back) {
            finish();
        }
    }

    @Override
    public void onSwithChecked(int viewId, boolean isChecked) {
        switch (viewId) {
            case R.id.setting_teddy_enable_wakeup: {
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_AUTO_MVW_SWITCH,
                        isChecked));
            }
            break;
            case R.id.setting_teddy_session: {

            }
            break;
            case R.id.setting_teddy_sex: {
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SEX_SWITCH,
                        isChecked));
            }
            break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTeddyVoiceEvent(VoiceEvent event) {
        switch (event.getEventKey()) {
            case VoiceEvent.EVENT_VOICE_AUTO_MVW_SWITCH:
                boolean autoSwitch = (boolean) event.getEventValue();
                mEnableWakeup.setChecked(autoSwitch);
                break;
            default:
                break;
        }
    }
}
