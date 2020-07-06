package com.sitechdev.vehicle.pad.manager;

/**
 * @author 邵志
 * @version 2020/06/12 0012 11:18
 * @name BaseMusicManager
 * @description
 */
public class BaseMusicManager  {
    /**
     * 注册当前音频管理的对象
     */
    public void registerPlayingManager(int musicSource) {
//        if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() != null) {
//            //退出之前的MusicManager
//            releaseMusicSource();
//            //先清除之前的老的MusicManager
//            SitechMusicNewManager.getInstance().resetCurrentMusicChannel();
//        }
//        //新的赋值
//        SitechDevLog.e("MusicManager", "BaseMusicManager registerPlayingManager===" + this);
//        SitechMusicNewManager.getInstance().registerCurrentMusicChannel(this);
        VoiceSourceManager.getInstance().setMusicSource(musicSource);
    }
}
