package com.sitechdev.vehicle.pad.module.feedback;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.HttpCode;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.model.feedback.FeedbackCommitBean;
import com.sitechdev.vehicle.pad.model.feedback.utils.PcmToWavUtil;
import com.sitechdev.vehicle.pad.vui.VUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FeedbackPresenter extends FeedbackContract.FeedbackPresenter implements MediaPlayer.OnCompletionListener {
    private String TAG = "FeedBackMainPresenter";
    private final int MSG_START_RECORDER = 0;
    private AudioRecord mAudioRecord;
    private boolean mWhetherRecord;
    private File mPcmFile;
    private File mWavFile;
    private Integer mRecordBufferSize;
    private Handler mHandler;
    private Handler mTimeHandler;
    private MediaPlayer mMediaPlayer;
    private int mRecordTime = 0;
    private String mCommitDur = "0";

    @SuppressLint("HandlerLeak")
    public FeedbackPresenter() {
        initAudioRecord();
        mTimeHandler = new Handler();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_START_RECORDER: {
                        startRecorder();
                    }
                    break;
                    default:
                    break;
                }
            }
        };
    }

    @Override
    public void playRecord() {
        startPlay();
    }

    @Override
    public void startRecord() {
        logTest("----1");
        getView().showRecordFile();
        mRecordTime = 0;
        if (null != mTimeHandler) {
            mTimeHandler.removeCallbacks(mTimerTask);
            mTimeHandler.post(mTimerTask);
        }
        logTest(TAG+"START RECORDER");
        //停止语音
        handleAudioFocus(true);
        VUI.getInstance().stop();
        //延迟两百毫秒，保证mic空出
        mHandler.removeMessages(MSG_START_RECORDER);
        mHandler.sendEmptyMessageDelayed(MSG_START_RECORDER, 200);
    }

    @Override
    public void hideCommitLayout() {
        logTest("hidecommitlayout");
        getView().hideCommitView(true);
        mRecordTime = -1;
    }

    @Override
    public void stopRecord() {
        getView().showStopRecordFile(mRecordTime + "s");
        mCommitDur = mRecordTime + "";
        getView().stopTime();
        getView().showCommitView();
        stopRecorder();
    }

    @Override
    public void sendRecordFile() {
        commitFile(mWavFile);
    }

    @Override
    public void release() {
        handleAudioFocus(false);
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (null != mTimeHandler) {
            mTimeHandler.removeCallbacksAndMessages(null);
            mTimeHandler = null;
        }
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void deleteRecorder() {
        if(null != mPcmFile && mPcmFile.exists()){
            mPcmFile.delete();
        }
        if(null != mWavFile && mWavFile.exists()){
            mWavFile.delete();
        }
    }

    /**
     * 停止录音
     */
    private void stopRecorder() {
        logTest(TAG+"STOP RECORDER");
        //启用语音
        handleAudioFocus(false);
        VUI.getInstance().start();
        mWhetherRecord = false;
    }

    /**
     * 点击录好的音频进行播放
     */
    private void startPlay() {
        mMediaPlayer = new MediaPlayer();
        if (!mMediaPlayer.isPlaying() && null != mWavFile) {
            try {
                mMediaPlayer.setDataSource(mWavFile.getAbsolutePath());
                logTest(TAG + "STARTPLAY FILE:" + mWavFile.getAbsolutePath());
                handleAudioFocus(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(this);
                getView().showVoiceAnim();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAudioRecord() {
        //获取每一帧的字节流大小
        mRecordBufferSize = AudioRecord.getMinBufferSize(8000
                , AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC
                , 8000
                , AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT
                , mRecordBufferSize);
    }

    /**
     * 录音并保存文件
     */
    private void startRecorder() {
        logTest("----3");
        mPcmFile = new File("mnt/sdcard/", "audioRecord.pcm");
        if (null != mWavFile && mWavFile.exists()) {
            mWavFile.delete();
        }
        mWhetherRecord = true;
        new Thread(() -> {
            logTest("----4");
            mAudioRecord.startRecording();//开始录制
            FileOutputStream fileOutputStream = null;
            try {
                logTest("----5");
                fileOutputStream = new FileOutputStream(mPcmFile);
                byte[] bytes = new byte[mRecordBufferSize];
                while (mWhetherRecord) {
                    mAudioRecord.read(bytes, 0, bytes.length);//读取流
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();

                }
                logTest(TAG + "run: 暂停录制");
                mAudioRecord.stop();//停止录制
                fileOutputStream.flush();
                fileOutputStream.close();
                addHeadData();//添加音频头部信息并且转成wav格式
            } catch (FileNotFoundException e) {
                logTest("----7");
                e.printStackTrace();
                mAudioRecord.stop();
            } catch (IOException e) {
                logTest("----8");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 将pcm格式转化为wav格式，并添加head!
     */
    private void addHeadData() {
        logTest(TAG + "addHeadData");
        mWavFile = new File("mnt/sdcard", "audioRecord_handler.wav");
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        pcmToWavUtil.pcmToWav(mPcmFile.toString(), mWavFile.toString());
        if(mPcmFile.exists()){
            mPcmFile.delete();
        }
        logTest("---FILEPATH:"+mPcmFile.getAbsolutePath());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handleAudioFocus(false);
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        getView().stopVoiceAnim();
    }

    private void commitFile(File audioFile) {
        if (null == null && !audioFile.exists()) {
            logTest(TAG + "sendFile NULL");
            return;
        }
        String url = EnvironmentConfig.URL_MALL_HOST.concat(AppUrlConst.COMMIT_FEEDBACK_FILLE);
        logTest("url:"+url);
//        String token = "B7YrT5i.XnmyRxw0R9z33sEllM5f15YtPAP.HnVhc1wIpgnXaoT3eRWZfZ.cJfrPOinxktMLtWdUqqeITw4LR8ADpwI2Jf9jUUyLlXOsiVUJB3mtcLhuiWXyyJ2z6fUSPoyIMp8fKmd-fw7xUL4hSnt574LIxnQATLSiASxeRLY_";
        OkGo.<FeedbackCommitBean>post(url).headers("Content-Type", "application/json")
                .headers(HttpHeaders.HEAD_KEY_COOKIE, "access_token=" + UserManager.getInstance().getUserToken())
                .params("file", audioFile)
                .params("voiceLength", mCommitDur)
                .execute(new JsonCallback<FeedbackCommitBean>(FeedbackCommitBean.class) {
                    @Override
                    public void onSuccess(Response<FeedbackCommitBean> response) {
                        if (null != response && null != response.body()) {
                            if (HttpCode.HTTP_OK.equalsIgnoreCase(response.body().code)) {
                                ToastUtils.showShort("反馈成功！");
                                getView().hideCommitView(true);
                                mRecordTime = -1;
                                logTest(TAG + "onSuccess:" + response.body() == null ? "body = " +
                                        "null" :
                                        response.body() + " response:" + response == null ? "null" :
                                                response.toString());
                            } else {
                                ToastUtils.showShort(R.string.hint_message);
                                getView().hideCommitView(false);
                            }
                        } else {
                            ToastUtils.showShort(R.string.hint_message);
                            getView().hideCommitView(false);
                        }
                    }

                    @Override
                    public void onError(Response<FeedbackCommitBean> response) {
                        super.onError(response);
                        logTest(TAG + "commitFeedbackData onError");
                        ToastUtils.showShort("反馈失败,请您检查网络！");
                        getView().hideCommitView(false);
                    }
                });
    }

    private Runnable mTimerTask = new Runnable() {
        @Override
        public void run() {
            logTest("----2");
            if (mRecordTime < 60 && mRecordTime > -1) {
                mRecordTime++;
                getView().setRecordTime(mRecordTime);
                if (null != mTimeHandler) {
                    mTimeHandler.postDelayed(this, 1000);
                }
            } else if (mRecordTime == 60) {
                stopRecord();
            }
        }
    };

    /**
     * 录音（播放）开始/停止时抢占/释放焦点，使其他音源暂停播放/暂停
     *
     * @param request
     */
    private void handleAudioFocus(boolean request) {
        if (request) {
            AppApplication.getAudioManager().requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 0, 2);
        } else {
            AppApplication.getAudioManager().abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
        }
    }
    
    private void logTest(String msg){
        Log.e("","-----"+msg);
    }
}
