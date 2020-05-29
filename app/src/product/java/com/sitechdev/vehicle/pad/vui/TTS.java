package com.sitechdev.vehicle.pad.vui;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhubaoqiang
 * @date 2019/8/18
 */
public class TTS {
    private HandlerThread mWorker;
    private TTSHandler mHandler;
    private Handler mMainHandler;
    private static TTS instance;

    private TTS(AIUIListener listener){
        mWorker = new HandlerThread("TTS Thread");
        mWorker.start();
        mMainHandler = new Handler(Looper.getMainLooper());
        mHandler = new TTSHandler(mWorker.getLooper(), mMainHandler, listener);
        mHandler.sendEmptyMessage(TTSMessage.INIT_AUDIOTRACK);
    }

    public static TTS getInstance(AIUIListener listener) {
        if (null == instance){
            synchronized (TTS.class){
                if (null == instance){
                    instance = new TTS(listener);
                }
            }
        }
        return instance;
    }

    public boolean isInit(){
        return mHandler.isInit();
    }

    public void handleTTS(AIUIEvent event, JSONObject content) throws JSONException {
        if (content.has("cnt_id")) {
            TTSWrapper wrapper = new TTSWrapper(event, content);
            Message msg = Message.obtain();
            msg.what = TTSMessage.TTS;
            msg.obj = wrapper;
            mHandler.sendMessage(msg);
//            String sid = event.data.getString("sid");
//            String cnt_id = content.getString("cnt_id");
//            byte[] audio = event.data.getByteArray(cnt_id); //合成音频数据
//            /**
//             *
//             * 音频块位置状态信息，取值：
//             * - 0（合成音频开始块）
//             * - 1（合成音频中间块，可出现多次）
//             * - 2（合成音频结束块)
//             * - 3（合成音频独立块,在短合成文本时出现）
//             *
//             * 举例说明：
//             * 一个正常语音合成可能对应的块顺序如下：
//             *   0 1 1 1 ... 2
//             * 一个短的语音合成可能对应的块顺序如下:
//             *   3
//             **/
//            int dts = content.getInt("dts");
//            int frameId = content.getInt("frame_id");// 音频段id，取值：1,2,3,...
//            int percent = event.data.getInt("percent"); //合成进度
//            boolean isCancel = "1".equals(content.getString("cancel"));  //合成过程中是否被取消
//            Message message = Message.obtain();
//            switch (dts){
//                case 0:
//                    message.what = TTSMessage.TTS;
//                    message.arg1 = TTSMessage.AUDIO_PLAY;
//
//                    break;
//                case 1:
//                    message.what = TTSMessage.TTS;
//                    message.arg1 = TTSMessage.APPEND_DATA;
//                    break;
//                case 2:
//                    message.what = TTSMessage.TTS;
//                    message.arg1 = TTSMessage.AUDIO_END;
//                    break;
//            }
//            message.obj = audio;
//            mHandler.sendMessage(message);
        }
    }

    public void stop(){
        mHandler.removeMessages(TTSMessage.TTS);
        mHandler.sendEmptyMessage(TTSMessage.TTS_STOP);
    }


    private static class TTSHandler extends Handler{
        private AudioTrack mAudioTrack;

        //音频流类型
        private static final int mStreamType = AudioManager.STREAM_MUSIC;
        //指定采样率 （MediaRecoder 的采样率通常是8000Hz AAC的通常是44100Hz。 设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置）
        private static final int mSampleRateInHz=16000 ;
        //指定捕获音频的声道数目。在AudioFormat类中指定用于此的常量
        private static final int mChannelConfig= AudioFormat.CHANNEL_CONFIGURATION_MONO; //单声道
        //指定音频量化位数 ,在AudioFormaat类中指定了以下各种可能的常量。通常我们选择ENCODING_PCM_16BIT和ENCODING_PCM_8BIT PCM代表的是脉冲编码调制，它实际上是原始音频样本。
        //因此可以设置每个样本的分辨率为16位或者8位，16位将占用更多的空间和处理能力,表示的音频也更加接近真实。
        private static final int mAudioFormat=AudioFormat.ENCODING_PCM_16BIT;
        //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法可以获得。
        private int mMinBufferSize;
        //STREAM的意思是由用户在应用程序通过write方式把数据一次一次得写到audiotrack中。这个和我们在socket中发送数据一样，
        // 应用层从某个地方获取数据，例如通过编解码得到PCM数据，然后write到audiotrack。
        private static int mMode = AudioTrack.MODE_STREAM;
        private AIUIListener mAIUIListener;
        private Handler handler;

        public TTSHandler(Looper looper, Handler handler, AIUIListener listener) {
            super(looper);
            this.mAIUIListener = listener;
            this.handler = handler;
        }

        private boolean isInit(){
            return null != mAudioTrack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TTSMessage.INIT_AUDIOTRACK:
                    initAudioTrack();
                    break;
//                case TTSMessage.TTS:
//                    byte[] data = (byte[]) msg.obj;
//                    AIUIEvent event = new AIUIEvent();
//                    event.eventType = AIUIConstant.EVENT_TTS;
//                    event.arg1 = -1;
//                    switch (msg.arg1){
//                        case TTSMessage.AUDIO_PLAY:
//                            mAudioTrack.play();
//                            event.arg1 = AIUIConstant.TTS_SPEAK_BEGIN;
//                        case TTSMessage.APPEND_DATA:
//                            if (event.arg1 < 0){
//                                event.arg1 = AIUIConstant.TTS_SPEAK_PROGRESS;
//                            }
//                        case TTSMessage.AUDIO_END:
//                            if (event.arg1 < 0){
//                                event.arg1 = AIUIConstant.TTS_SPEAK_COMPLETED;
//                            }
//                            mAudioTrack.write(data, 0, data.length);
//                            if (msg.what == TTSMessage.AUDIO_END){
//                                mAudioTrack.stop();
//                            }
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mAIUIListener.onEvent(event);
//                                }
//                            });
//                            break;
//                    }
//                    break;
                case TTSMessage.TTS:
                    TTSWrapper wrapper = (TTSWrapper) msg.obj;
                    AIUIEvent event = wrapper.event;
                    JSONObject content = wrapper.conetnt;
                    String sid = event.data.getString("sid");
                    String cnt_id = content.optString("cnt_id");
                    byte[] audio = event.data.getByteArray(cnt_id); //合成音频数据
                    /**
                     *
                     * 音频块位置状态信息，取值：
                     * - 0（合成音频开始块）
                     * - 1（合成音频中间块，可出现多次）
                     * - 2（合成音频结束块)
                     * - 3（合成音频独立块,在短合成文本时出现）
                     *
                     * 举例说明：
                     * 一个正常语音合成可能对应的块顺序如下：
                     *   0 1 1 1 ... 2
                     * 一个短的语音合成可能对应的块顺序如下:
                     *   3
                     **/
                    int dts = content.optInt("dts", -1);
                    int frameId = content.optInt("frame_id", -1);// 音频段id，取值：1,2,3,...
                    int percent = event.data.getInt("percent"); //合成进度
                    String textEsg = content.optString("text_seg");
                    boolean isCancel = "1".equals(content.optString("cancel"));  //合成过程中是否被取消
                    if (isCancel){
                        VUI.log("TTS isCancel ---> " + isCancel);
                    }
                    if (null != audio && dts >= 0){
                        AIUIEvent callBack = new AIUIEvent();
                        callBack.eventType = AIUIConstant.EVENT_TTS;
                        callBack.arg1 = -1;
                        switch (dts){
                            case 0:
                                mAudioTrack.play();
                                callBack.arg1 = AIUIConstant.TTS_SPEAK_BEGIN;
                            case 1:
                                if (callBack.arg1 < 0){
                                    callBack.arg1 = AIUIConstant.TTS_SPEAK_PROGRESS;
                                }
                                callBack.arg2 = percent;
                                callBack.info = textEsg;
                            case 2:
                                if (callBack.arg1 < 0){
                                    callBack.arg1 = AIUIConstant.TTS_SPEAK_COMPLETED;
                                }
                                mAudioTrack.write(audio, 0, audio.length);
                                if (dts == 2){
                                    VUI.log("dts ----->" + dts);
                                    mAudioTrack.stop();
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAIUIListener.onEvent(callBack);
                                    }
                                });
                            break;
                        }
                    }
                    break;
                case TTSMessage.TTS_STOP:
                    if (null != mAudioTrack){
                        mAudioTrack.pause();
                        mAudioTrack.flush();
                        mAudioTrack.stop();
                    }
                    break;
            }
        }

        private void initAudioTrack() {
            //根据采样率，采样精度，单双声道来得到frame的大小。
            mMinBufferSize = AudioTrack.getMinBufferSize(mSampleRateInHz,mChannelConfig, mAudioFormat);//计算最小缓冲区
            //注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。
            //创建AudioTrack
            mAudioTrack = new AudioTrack(mStreamType, mSampleRateInHz,mChannelConfig,
                    mAudioFormat,mMinBufferSize,mMode);
        }
    }

    private static class TTSWrapper{
        public AIUIEvent event;
        public JSONObject conetnt;

        public TTSWrapper(AIUIEvent event, JSONObject conetnt) {
            this.event = event;
            this.conetnt = conetnt;
        }
    }

    private interface TTSMessage{

        int INIT_AUDIOTRACK = 0x0;
        int TTS = INIT_AUDIOTRACK + 1;
        int TTS_STOP = TTS + 1;
    }
}
