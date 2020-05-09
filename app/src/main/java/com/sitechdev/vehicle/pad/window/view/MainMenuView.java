package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ImageUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.view.SkinTextView;
import com.sitechdev.vehicle.pad.view.VolumeView2;
import com.sitechdev.vehicle.pad.vui.VUI;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;

public class MainMenuView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = MainMenuView.class.getSimpleName();

    private ImageView mTeddyView, mHomeBtnImageView, mNaviBtnImageView, mMusicBtnImageView, mDriverBtnImageView, mAppsBtnImageView;

    public int mWidth;
    public int mHeight;
    //Teddy展示区域
    private RelativeLayout mTeddyContentView = null;

    //默认的Teddy展示样式
    private RelativeLayout mTeddyDefaultContentView = null;
    //识别过程中的Teddy展示样式
    private RelativeLayout mTeddySringContentView = null;
    //tts中的Teddy展示样式
    private RelativeLayout mTeddyTtsContentView = null;

    private SkinTextView mSrTextView = null, mTtsTextView = null;
    private VolumeView2 mSrVolumeView = null;

    private int reflectionValue = 30;


    public MainMenuView(Context context) {
        this(context, null);
    }

    public MainMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.main_menu_view, this);
        View view = findViewById(R.id.id_main_menu_view);

        mHomeBtnImageView = (ImageView) findViewById(R.id.id_btn_home);
        mNaviBtnImageView = (ImageView) findViewById(R.id.id_btn_location);
        mMusicBtnImageView = (ImageView) findViewById(R.id.id_btn_music);
        mDriverBtnImageView = (ImageView) findViewById(R.id.id_btn_driver);
        mAppsBtnImageView = (ImageView) findViewById(R.id.id_btn_apps);

        mWidth = view.getLayoutParams().width;
        mHeight = view.getLayoutParams().height;

        mTeddyView = findViewById(R.id.id_btn_teddy);

        initView();

        initListener();
    }

    private void initView() {
        mHomeBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_home), reflectionValue, true));
        mNaviBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_navi), reflectionValue, true));
        mMusicBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_music), reflectionValue, true));
        mDriverBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_driver), reflectionValue, true));
        mAppsBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_apps), reflectionValue, true));

        //Teddy的内容区域
        mTeddyContentView = findViewById(R.id.id_Teddy_Content);

        //默认的Teddy展示样式
        mTeddyDefaultContentView = findViewById(R.id.id_teddy_default_content);

        //识别过程中的Teddy展示样式
        mTeddySringContentView = findViewById(R.id.id_teddy_sr_content);
        mSrVolumeView = findViewById(R.id.id_teddy_sr_volume_content);
        mSrVolumeView.start();
        mSrTextView = findViewById(R.id.id_teddy_sr_text);

        //tts中的Teddy展示样式
        mTeddyTtsContentView = findViewById(R.id.id_teddy_tts_content);
        //tts文字描述
        mTtsTextView = findViewById(R.id.id_teddy_tts_text);
    }

    private void initListener() {
        mTeddyView.setOnClickListener(this);
        mHomeBtnImageView.setOnClickListener(this);
        mNaviBtnImageView.setOnClickListener(this);
        mMusicBtnImageView.setOnClickListener(this);
        mDriverBtnImageView.setOnClickListener(this);
        mAppsBtnImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_home:
//                ToastUtils.showShort("主页按钮被点击了。。。");
                break;
            case R.id.id_btn_location:
//                ToastUtils.showShort("导航按钮被点击了。。。");
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_TAXI);
                break;
            case R.id.id_btn_music:
//                ToastUtils.showShort("音乐按钮被点击了。。。");
                break;
            case R.id.id_btn_driver:
//                ToastUtils.showShort("驾驶按钮被点击了。。。");
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_CAR_STATUS);
                break;
            case R.id.id_btn_apps:
//                ToastUtils.showShort("应用按钮被点击了。。。");
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_SKIN_PAGE);
                break;
            case R.id.id_btn_teddy:
                if (VUI.getInstance().isTeddyWorking()) {
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_OVER));
                } else {
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新TeddyView
     *
     * @param event
     */
    public void refreshTeddyView(VoiceEvent event) {
        switch (event.getEventKey()) {
            //唤醒成功
            case VoiceEvent.EVENT_VOICE_MVW_SUCCESS:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_MVW_SUCCESS===");
                refreshTeddyViewMvwSuccess();
                //
                mTtsTextView.setText(StringUtils.isEmpty((String) event.getEventValue()) ? VoiceConstants.TTS_RESPONSE_DEFAULT_TEXT : (String) event.getEventValue());
                break;
            //开始识别
            case VoiceEvent.EVENT_VOICE_START_SR:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_START_SR===");
                refreshTeddyViewSr();
                if (mSrVolumeView != null) {
                    mSrVolumeView.start();
                }
                if (mSrTextView != null && mSrTextView.isShown()) {
                    mSrTextView.setVisibility(View.GONE);
                }
                break;
            //识别过程中的音量变化
            case VoiceEvent.EVENT_VOICE_SR_ING_VOLUME:
                refreshTeddyViewSr();
//                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_SR_ING_VOLUME===" + event.getEventValue());
                try {
                    int volumeValue = (int) event.getEventValue();
                    mSrVolumeView.setVolume((float) volumeValue);
                } catch (Exception e) {
                    SitechDevLog.exception(e);
                }
                break;
            //识别成功，TTS播报结果中
            case VoiceEvent.EVENT_VOICE_TTS_PLAYIING:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_TTS_PLAYIING===" + (String) event.getEventValue());
                refreshTeddyViewTtsSuccess();
                if (!StringUtils.isEmpty((String) event.getEventValue())) {
                    mTtsTextView.setText((String) event.getEventValue());
                }
                break;
            //结束识别，返回结果
            case VoiceEvent.EVENT_VOICE_SR_SUCCESS:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_SR_SUCCESS===");
                refreshTeddyViewSr();
                try {
                    mSrTextView.setText((String) event.getEventValue());
                } catch (Exception e) {
                    mSrTextView.setText("");
                }
                if (mSrTextView != null && !mSrTextView.isShown()) {
                    mSrTextView.setVisibility(View.VISIBLE);
                }
                break;
            //结束识别
            case VoiceEvent.EVENT_VOICE_SR_OVER:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_SR_OVER===");
                if (mSrTextView != null && !mSrTextView.isShown()) {
                    mSrTextView.setVisibility(View.GONE);
                }
                resetTeddyViewDefault();
                break;
            //停止语音
            case VoiceEvent.EVENT_VOICE_STOP_VOICE:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_STOP_VOICE===");
                break;
            //启用语音
            case VoiceEvent.EVENT_VOICE_RESUME_VOICE:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, "VoiceEvent.EVENT_VOICE_RESUME_VOICE===");
                break;
            default:
                break;
        }
    }

    /**
     * 唤醒成功View
     */
    private void refreshTeddyViewMvwSuccess() {
        //默认状态
        if (mTeddyDefaultContentView.getVisibility() != View.GONE) {
            mTeddyDefaultContentView.setVisibility(View.GONE);
        }
        //识别过程中的Teddy展示样式
        if (mTeddySringContentView.getVisibility() != View.GONE) {
            mTeddySringContentView.setVisibility(View.GONE);
        }
        //tts中的Teddy展示样式
        if (mTeddyTtsContentView.getVisibility() != View.VISIBLE) {
            mTeddyTtsContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 识别过程的View
     */
    private void refreshTeddyViewSr() {
        //默认状态
        if (mTeddyDefaultContentView.getVisibility() != View.GONE) {
            mTeddyDefaultContentView.setVisibility(View.GONE);
        }
        //识别过程中的Teddy展示样式
        if (mTeddySringContentView.getVisibility() != View.VISIBLE) {
            mTeddySringContentView.setVisibility(View.VISIBLE);
        }
        //tts中的Teddy展示样式
        if (mTeddyTtsContentView.getVisibility() != View.GONE) {
            mTeddyTtsContentView.setVisibility(View.GONE);
        }
    }

    /**
     * 合成过程的View
     */
    private void refreshTeddyViewTtsSuccess() {
        //默认状态
        if (mTeddyDefaultContentView.getVisibility() != View.GONE) {
            mTeddyDefaultContentView.setVisibility(View.GONE);
        }
        //识别过程中的Teddy展示样式
        if (mTeddySringContentView.getVisibility() != View.GONE) {
            mTeddySringContentView.setVisibility(View.GONE);
        }
        //tts中的Teddy展示样式
        if (mTeddyTtsContentView.getVisibility() != View.VISIBLE) {
            mTeddyTtsContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 默认的View
     */
    private void resetTeddyViewDefault() {
        //默认状态
        if (mTeddyDefaultContentView.getVisibility() != View.VISIBLE) {
            mTeddyDefaultContentView.setVisibility(View.VISIBLE);
        }
        //识别过程中的Teddy展示样式
        if (mTeddySringContentView.getVisibility() != View.GONE) {
            mTeddySringContentView.setVisibility(View.GONE);
        }
        //tts中的Teddy展示样式
        if (mTeddyTtsContentView.getVisibility() != View.GONE) {
            mTeddyTtsContentView.setVisibility(View.GONE);
        }
    }
}
