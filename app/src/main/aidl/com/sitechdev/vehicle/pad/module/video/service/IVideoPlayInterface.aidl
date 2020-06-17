package com.sitechdev.vehicle.pad.module.video.service;


interface IVideoPlayInterface {
    void play();
    void pause();
    void next();
    void pre();
    void close();
    void seekTo(int position);
    void changeMode(int mode);
    void playLocalSource(String path);


}
