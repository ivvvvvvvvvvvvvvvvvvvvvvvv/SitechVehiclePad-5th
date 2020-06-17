// IMusicCallBack.aidl
package com.sitechdev.vehicle.pad.module.video.service;
import com.sitechdev.vehicle.pad.module.video.service.IVideoPlayInterface;

import com.sitechdev.vehicle.pad.module.video.service.VideoInfo;

interface IAppControlInterface {

    void showNavigation();

    void hideNavigation();

    void addCallBack(IVideoPlayInterface callBack);

    void removeCallBack(IVideoPlayInterface callBack);

    void onListUpdate(in List<VideoInfo> list, in VideoInfo info, int status, int position);
        void onPlay(in VideoInfo info);
        void onPause(in VideoInfo info);
        void onResume(in VideoInfo info);
        void onSeekTo(in VideoInfo info, int position);
}
