package com.sitechdev.vehicle.pad.module.feedback;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.model.feedback.utils.FeedbackConfig;
import com.sitechdev.vehicle.pad.view.RoundProgressBarWidthNumber;
import com.sitechdev.vehicle.pad.view.VolumeView2;

import java.io.IOException;
import java.util.HashMap;

public class FeedbackActivity extends MvpActivity<FeedbackContract.FeedbackPresenter> implements FeedbackContract.View {

    String TAG = "FeedBackFragment";
    private TextView mTitleView;
    private View mToHistory;
    private View mBack;
    private View mStartRecord;
    private View mStopRecord;
    private View mStartTip;
    private VolumeView2 mRecordGif;
    private View mCommitLayout;
    private View mCommitBtn;
    private View mCancleBtn;
    private TextView mRecordTime;
    private TextView mCommitTime;
    private View mAnimView;
    private View mPlay;
    private View mLoading;
    private RoundProgressBarWidthNumber mProgress;

    @Override
    protected FeedbackContract.FeedbackPresenter createPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mTitleView = findViewById(R.id.tv_sub_title);
        mToHistory = findViewById(R.id.feedback_main_tohistory_btn);
        mBack = findViewById(R.id.iv_sub_back);
        mStartRecord = findViewById(R.id.feedback_main_record);
        mStopRecord = findViewById(R.id.feedback_main_stoprecord);
        mStartTip = findViewById(R.id.feedback_main_record_tip);
        mRecordGif = findViewById(R.id.feedback_main_record_gif);
        mCommitLayout = findViewById(R.id.feedback_main_commit_layout);
        mCommitBtn = findViewById(R.id.feedback_main_commit);
        mCancleBtn = findViewById(R.id.feedback_main_cancle);
        mPlay = findViewById(R.id.feedback_main_play);
        mRecordTime = findViewById(R.id.feedback_main_time);
        mCommitTime = findViewById(R.id.feedback_main_commit_time);
        mAnimView = findViewById(R.id.feedback_main_commit_anim);
        mAnimView.setBackgroundResource(R.drawable.feedback_audio_anim3);
        mLoading = findViewById(R.id.feedback_main_commit_loading);
        mProgress = findViewById(R.id.feedback_main_progressbar);
        mTitleView.setText(getResources().getText(R.string.feedback_main_title));
        if (null == FeedbackConfig.getInstance().getFileMap()) {
            FeedbackConfig.getInstance().setFileMap(new HashMap());
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mPresenter) {
            mPresenter.stopRecord();
            mPresenter.hideCommitLayout();
            mPresenter.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.stopRecord();
            mPresenter.hideCommitLayout();
            mPresenter.release();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mTitleView.setOnClickListener(this);
        mToHistory.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mStartRecord.setOnClickListener(this);
        mStopRecord.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
        mPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sub_back: {
                finish();
            }
            break;
            case R.id.feedback_main_tohistory_btn: {
                toHistory();
            }
            break;
            case R.id.feedback_main_record: {
                mPresenter.startRecord();
            }
            break;
            case R.id.feedback_main_stoprecord: {
                mPresenter.stopRecord();
            }
            break;
            case R.id.feedback_main_commit: {
                mPresenter.sendRecordFile();
                mLoading.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.feedback_main_cancle: {
                logTest("click cancle");
                mPresenter.hideCommitLayout();
            }
            break;
            case R.id.feedback_main_play: {
                mPresenter.playRecord();
            }
            break;
            default: {
            }
            break;
        }
    }

    private void toHistory() {
        startActivity(new Intent(FeedbackActivity.this,FeedbackHistoryActivity.class));
    }

    @Override
    public void showRecordFile() {
        mStartRecord.setVisibility(View.GONE);
        mStartTip.setVisibility(View.GONE);
        mStopRecord.setVisibility(View.VISIBLE);
        mRecordGif.setVisibility(View.VISIBLE);
        LogUtil.i(TAG + "showRecordFile gif show");
        mRecordGif.start();
        mRecordTime.setText("0s");
        mRecordTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStopRecordFile(String time) {
        mStopRecord.setVisibility(View.GONE);
        mRecordGif.stop();
        mRecordGif.setVisibility(View.GONE);
        LogUtil.i(TAG + "showStopRecordFile gif hide");
        mStartTip.setVisibility(View.VISIBLE);
        mStartRecord.setVisibility(View.VISIBLE);
        mRecordTime.setVisibility(View.GONE);
        mCommitTime.setText(time);
    }

    @Override
    public void setRecordTime(int second) {
        mRecordTime.setText(second+"s");
        mProgress.setProgress(second);
    }

    @Override
    public void stopTime() {
    }

    @Override
    public void showCommitView() {
        LogUtil.i(TAG + "showCommitView");
        mCommitLayout.setVisibility(View.VISIBLE);
        mStopRecord.setVisibility(View.GONE);
        mRecordGif.stop();
        mRecordGif.setVisibility(View.GONE);
        LogUtil.i(TAG + "showCommitView gif hide");
        mStartTip.setVisibility(View.GONE);
        mStartRecord.setVisibility(View.GONE);
    }

    @Override
    public void hideCommitView(boolean isSuccess) {
        LogUtil.i(TAG + "hideCommitView");
        if (isSuccess) {
            mCommitLayout.setVisibility(View.GONE);
            mStopRecord.setVisibility(View.GONE);
            mRecordGif.stop();
            mRecordGif.setVisibility(View.GONE);
            LogUtil.i(TAG + "hideCommitView gif hide");
            mStartTip.setVisibility(View.VISIBLE);
            mStartRecord.setVisibility(View.VISIBLE);
        }
        mLoading.setVisibility(View.GONE);
        mPresenter.deleteRecorder();
    }

    @Override
    public void showVoiceAnim() {
        startAnim();
    }

    @Override
    public void stopVoiceAnim() {
        stopAnim();
    }

    private void startAnim() {
        mAnimView.setBackgroundResource(R.drawable.feedback_audio_animation_list);
        if (mAnimView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
            animation.start();
        }
    }

    private void stopAnim() {
        if (mAnimView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
            animation.stop();
        }
        mAnimView.setBackgroundResource(R.drawable.feedback_audio_anim3);
    }

    private void logTest(String msg) {
        Log.e("FeedbackActivity", "TEST----" + msg);

    }
}