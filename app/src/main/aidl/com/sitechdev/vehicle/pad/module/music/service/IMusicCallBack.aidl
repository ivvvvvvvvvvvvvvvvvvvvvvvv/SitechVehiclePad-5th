// IMusicCallBack.aidl
package com.sitechdev.vehicle.pad.module.music.service;

import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;

interface IMusicCallBack {
    void onListUpdate(in List<MusicInfo> list, in MusicInfo info, int status, int position);
    void onPlay(in MusicInfo info);
    void onPause(in MusicInfo info);
    void onResume(in MusicInfo info);
    void onSeekTo(in MusicInfo info, int position);
}
