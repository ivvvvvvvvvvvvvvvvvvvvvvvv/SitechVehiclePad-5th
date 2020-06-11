// AMusicInterface.aidl
package com.sitechdev.vehicle.pad.module.music.service;

import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.module.music.service.IPCResult;
import com.sitechdev.vehicle.pad.module.music.service.IMusicCallBack;

interface AMusicInterface {

    void addCallBack(IMusicCallBack callBack);

    void removeCallBack(IMusicCallBack callBack);

    void init();

    MusicInfo getInfo();

    int getStatus();

    IPCResult play(long id);

    IPCResult stop();

    IPCResult pause();

    IPCResult resume();

    IPCResult toggle();

    IPCResult next(boolean fromUser);

    IPCResult pre();

    IPCResult seekTo(int position);

    IPCResult changeMode(int mode);

}
