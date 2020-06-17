package com.sitechdev.vehicle.pad.module.video;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.module.video.service.IAppControlInterface;
import com.sitechdev.vehicle.pad.module.video.service.IVideoPlayInterface;
import com.sitechdev.vehicle.pad.module.video.service.VideoInfo;

import java.util.List;

public class VideoPlayService extends Service {

    private VideoServer videoServer;

    public VideoPlayService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        videoServer = new VideoServer(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return videoServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    private RemoteCallbackList<IVideoPlayInterface> callbackList;
    private class VideoServer extends IAppControlInterface.Stub{
        private Context context;

        public VideoServer(Context context) {
            this.context = context;
        }

        @Override
        public void showNavigation() throws RemoteException {
            EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_APP_FRONT, true));
        }

        @Override
        public void hideNavigation() throws RemoteException {
            EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_APP_BACKGROUD));
        }

        @Override
        public void addCallBack(IVideoPlayInterface callBack) throws RemoteException {
            if (null == callBack) {
                return;
            }
            if (null == callbackList) {
                callbackList = new RemoteCallbackList<>();
            }
            callbackList.register(callBack);
        }

        @Override
        public void removeCallBack(IVideoPlayInterface callBack) throws RemoteException {
            if (null == callBack) {
                return;
            }
            if (null != callbackList) {
                callbackList.unregister(callBack);
            }
        }

        @Override
        public void onListUpdate(List<VideoInfo> list, VideoInfo info, int status, int position) throws RemoteException {

        }

        @Override
        public void onPlay(VideoInfo info) throws RemoteException {

        }

        @Override
        public void onPause(VideoInfo info) throws RemoteException {

        }

        @Override
        public void onResume(VideoInfo info) throws RemoteException {

        }

        @Override
        public void onSeekTo(VideoInfo info, int position) throws RemoteException {

        }
    }
    boolean isCallBackWorking = false;

    private void seekTo(int position) {
        if (isCallBackWorking || callbackList == null) {
            return;
        }
        int n = callbackList.beginBroadcast();
        isCallBackWorking = true;
        try {
            for (int i = 0; i < n; i++) {
                IVideoPlayInterface callBack = callbackList.getBroadcastItem(i);
                callBack.seekTo(position);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        callbackList.finishBroadcast();
        isCallBackWorking = false;
    }

}
