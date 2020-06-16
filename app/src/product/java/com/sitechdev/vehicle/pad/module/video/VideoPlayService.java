package com.sitechdev.vehicle.pad.module.video;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.module.video.service.IVideoPlayInterface;

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

    private class VideoServer extends IVideoPlayInterface.Stub {
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
    }
}
