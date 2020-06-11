package com.sitechdev.vehicle.pad.module.setting.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.setting.TTSConfig;
import com.sitechdev.vehicle.pad.module.setting.dialog.SettingVoiceDialog;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConfig;
import com.sitechdev.vehicle.pad.util.AnimFractory;
import com.sitechdev.vehicle.pad.view.dialog.DialogWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.TEDDY_SPKEY_TTS_WELCOME;

public class SettingTeddyActivity extends BaseActivity {

    private TextView voiceWord, voiceResetTxt, voiceSetWelcomeText,
            ttsSpeaker, ttsSpeed;
    private RadioGroup voiceDisplay, voiceEnableSwitch, voiceWelcomeSwitch, voiceOngoingTalk;
    private RelativeLayout voiceWakeBtn, voiceResetBtn, voiceSetWelcome;
    private SettingVoiceDialog voiceDialog;
    private List<TTSConfig> speakers;
    private List<TTSConfig> speeds;
    private TTSConfig speaker;
    private TTSConfig speed;
    private static final String TEST_TTS = "这是一段没有任何意义的试听音";
    private DialogWrapper speakerDialog;
    private DialogWrapper speedDialog;
    private volatile boolean isUploadLog = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_teddy;
    }

    @Override
    protected void initData() {
        speakers = getTTSSpeakers();
        speeds = getTTSSpeeds();
        //允许语音唤醒-默认false为关闭
        if (true) {
            ((RadioButton) (voiceEnableSwitch.findViewById(R.id.setting_voice_enable_voice_switch_off))).setChecked(true);
        } else {
            ((RadioButton) (voiceEnableSwitch.findViewById(R.id.setting_voice_enable_voice_switch_on))).setChecked(true);
        }
        speaker = setTTSConfig(ttsSpeaker, speakers, "");
        speed = setTTSConfig(ttsSpeed, speeds, "");
        //持续对话
        if (true) {
            ((RadioButton) (voiceOngoingTalk.findViewById(R.id.setting_voice_ongoing_dialogue_on))).setChecked(true);
        } else {
            ((RadioButton) (voiceOngoingTalk.findViewById(R.id.setting_voice_ongoing_dialogue_off))).setChecked(true);
        }

        //开机问候语-默认false为关闭
        if (true) {
            ((RadioButton) (voiceWelcomeSwitch.findViewById(R.id.setting_voice_welcome_text_switch_off))).setChecked(true);
            AnimFractory.showDeviceInfoAnimation(voiceSetWelcome, false);
        } else {
            ((RadioButton) (voiceWelcomeSwitch.findViewById(R.id.setting_voice_welcome_text_switch_on))).setChecked(true);
            AnimFractory.showDeviceInfoAnimation(voiceSetWelcome, true);
        }

        String word = "word";
        voiceWord.setText(word);
        if (!TeddyConfig.isShowTeddyIcon()) {
            ((RadioButton) (voiceDisplay.findViewById(R.id.setting_voice_display_switch_on))).setChecked(true);
        } else {
            ((RadioButton) (voiceDisplay.findViewById(R.id.setting_voice_display_switch_off))).setChecked(true);
        }

        voiceSetWelcomeText.setText((TextUtils.isEmpty(SPUtils.getValue(AppApplication.getContext(), TEDDY_SPKEY_TTS_WELCOME, "")) ? "关闭" : "打开"));
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("系统设置");
        voiceWord = findViewById(R.id.setting_voice_wake_up_txt);
        voiceResetTxt = findViewById(R.id.setting_voice_reset_txt);
        voiceWakeBtn = findViewById(R.id.btn_sett_teddy);
        voiceResetBtn = findViewById(R.id.setting_voice_reset_btn);
        voiceDisplay = findViewById(R.id.setting_voice_display_switch);
        voiceEnableSwitch = findViewById(R.id.setting_voice_enable_voice_switch);
        voiceWelcomeSwitch = findViewById(R.id.setting_voice_welcome_text_switch);
        voiceSetWelcome = findViewById(R.id.setting_voice_set_welcome_text_rl);
        voiceSetWelcomeText = findViewById(R.id.setting_voice_set_welcome_text);
        voiceOngoingTalk = findViewById(R.id.setting_voice_ongoing_dialogue);
        ttsSpeaker = findViewById(R.id.tts_speaker);
        ttsSpeed = findViewById(R.id.tts_speed);
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
            case R.id.iv_sub_back:
                finish();
                break;
            default:
                break;
        }
    }

    private List<TTSConfig> getTTSSpeakers() {
        List<TTSConfig> configs = new ArrayList<>();
        configs.add(new TTSConfig("青年女声", "jiajia", 9));
        configs.add(new TTSConfig("中年女声", "xiaoxue", 50110));
        configs.add(new TTSConfig("童声女孩", "nannan", 7));
        configs.add(new TTSConfig("青年男声", "xiaofeng", 4));
        configs.add(new TTSConfig("中年男声", "laoma", 12));
        configs.add(new TTSConfig("河南话男声", "xiaokun", 25));
        configs.add(new TTSConfig("四川话女声", "xiaorong", 14));
        configs.add(new TTSConfig("东北话女声", "xiaoqian", 11));
        configs.add(new TTSConfig("广东话女声", "xiaomei", 15));
        configs.add(new TTSConfig("湖南话男声", "xiaoqiang", 24));
        return configs;
    }

    private List<TTSConfig> getTTSSpeeds() {
        List<TTSConfig> configs = new ArrayList<>();
        return configs;
    }

    private TTSConfig setTTSConfig(TextView textView, List<TTSConfig> configs, String value) {
        int len = configs.size();
        for (int i = 0; i < len; i++) {
            TTSConfig config = configs.get(i);
            if (TextUtils.equals(value, config.getValue())) {
                textView.setText(config.getShow());
                return config;
            }
        }
        return null;
    }

}
