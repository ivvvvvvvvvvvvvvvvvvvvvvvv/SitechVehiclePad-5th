package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;

/**
 * @author zhubaoqiang
 * @date 2019/8/12
 */
public class WakeupEngine {

    private Context context;

    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    private static final int WAKE_THRESH = 1450;
    /**
     * 开启持续唤醒
     * 0/1
     */
    private static final String WAKE_KEEP_LIVE = "1";
    /**
     * 闭环优化是针对开发者的唤醒资源由云端优化系统不断优化的功能。
     * 通过开发者 APP 使用场景，本地唤醒 SDK 自动挑选音频数据上传至云端，
     * 进行训练生成优化唤醒资源。开发者 APP 使用场景中，
     * 优化唤醒资源在相比原有资源在提升唤醒率及抑制误唤醒方面有良好的表现。
     * 持续优化包含两种网络模式：
     *
     * 模式 0：关闭优化功能，禁止向服务端发送本地挑选数据；
     * 模式 1：开启优化功能，允许向服务端发送本地挑选数据；
     */
    private static final String WAKE_NETMODE = "1";

    private static WakeuperListener mWakeuperListener;

    private WakeupEngine() {
        context = AppApplication.getContext();
        initWakeUP();
    }

    private void initWakeUP() {
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"="+ BuildConfig.AIUI_APPID);
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(context, new InitListener() {
            @Override
            public void onInit(int i) {
                Log.i("Findme!", "VoiceWakeuper onInit = " + i);
            }
        });
    }

    public int startListening(){
        mIvw = VoiceWakeuper.getWakeuper();
        if (null != mIvw){
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:"+ WAKE_THRESH);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, WAKE_KEEP_LIVE);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, WAKE_NETMODE);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getPath() +
                            "/msc/ivw.wav" );
            mIvw.setParameter( SpeechConstant.AUDIO_FORMAT, "wav" );
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
            return mIvw.startListening(mWakeuperListener);
        }
        return -1;
    }

    public void stop(){
        if (null != mIvw && mIvw.isListening()){
            mIvw.stopListening();
        }
    }

    public boolean  isListening(){
        return null != mIvw && mIvw.isListening();
    }

    public void destroy(){
        if (null != mIvw){
            if (mIvw.isListening()){
                mIvw.stopListening();
            }
            mIvw.destroy();
            SpeechUtility.getUtility().destroy();
            mIvw = null;
        }
    }

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(
                context, ResourceUtil.RESOURCE_TYPE.assets,
                "ivw/" + BuildConfig.AIUI_APPID+".jet");
        return resPath;
    }


    protected static WakeupEngine getInstance(WakeuperListener listener) {
        mWakeuperListener = listener;
        return WakeupEngineHolder.INSTANCE;
    }

    private static class WakeupEngineHolder{
        private static WakeupEngine INSTANCE = new WakeupEngine();
    }
}
