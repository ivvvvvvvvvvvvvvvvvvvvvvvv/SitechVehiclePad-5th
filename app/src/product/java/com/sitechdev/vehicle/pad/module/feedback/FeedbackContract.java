package com.sitechdev.vehicle.pad.module.feedback;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;

public class FeedbackContract {
    abstract static class FeedbackPresenter extends BasePresenter<FeedbackContract.View>{
        public abstract void startRecord();
        public abstract void stopRecord();
        public abstract void playRecord();
        public abstract void hideCommitLayout();
        public abstract void sendRecordFile();
        public abstract void release();
        public abstract void deleteRecorder();
    }

    interface View extends IContract.IView{
        void showRecordFile();
        void showStopRecordFile(String time);
        void setRecordTime(int second);
        void stopTime();
        void showCommitView();
        void hideCommitView(boolean isSuccess);
        void showVoiceAnim();
        void stopVoiceAnim();

    }
}
