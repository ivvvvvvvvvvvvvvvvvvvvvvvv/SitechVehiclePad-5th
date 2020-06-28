package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.vui.VUI;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;
import com.sitechdev.vehicle.pad.view.SkinTextView;
import com.sitechdev.vehicle.pad.view.VolumeView2;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;


/**
 * @author DELL
 * @date 2016/11/8
 */
public class FloatTeddyView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = FloatTeddyView.class.getSimpleName();
    private TeddyWindowManager manager;
    private ImageView voiceIconIv;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;  // 快速点击间隔

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

    public FloatTeddyView(Context context) {
        this(context, null);
    }

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
    }

    public FloatTeddyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.main_teddy_view, this);
        voiceIconIv = findViewById(R.id.id_btn_teddy);
        mWidth = findViewById(R.id.id_rl_teddy).getLayoutParams().width;
        mHeight = findViewById(R.id.id_rl_teddy).getLayoutParams().height;
        manager = TeddyWindowManager.getInstance();

        initView();

        initListener();
    }

    private void initView() {
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
        voiceIconIv.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_teddy:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>手动点击Teddy启动图标===");
                if (VUI.getInstance().isTeddyWorking()) {
                    SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>Teddy正在工作中===发出SR_OVER事件");
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_OVER));
                } else {
                    SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>Teddy未在工作，===发出MVW_SUCCESS事件");
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
