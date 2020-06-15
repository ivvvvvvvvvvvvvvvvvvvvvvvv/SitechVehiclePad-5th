package com.sitechdev.vehicle.pad.manager;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.bean.SitechMusicBean;
import com.sitechdev.vehicle.pad.callback.SitechMusicSource;

/**
 * @author 邵志
 * @version 2020/06/12 0012 11:18
 * @name BaseMusicManager
 * @description
 */
public class BaseMusicManager implements SitechMusicSource.onMusicControlListener {
    /**
     * 注册当前音频管理的对象
     */
    public void registerPlayingManager() {
        if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() != null) {
            //退出之前的MusicManager
            releaseMusicSource();
            //先清除之前的老的MusicManager
            SitechMusicNewManager.getInstance().resetCurrentMusicChannel();
        }
        //新的赋值
        SitechDevLog.e("MusicManager", "BaseMusicManager registerPlayingManager===" + this);
        SitechMusicNewManager.getInstance().registerCurrentMusicChannel(this);
    }

    /**
     * 下一首
     */
    @Override
    public void onMusicPlayNext() {

    }

    /**
     * 上一首
     */
    @Override
    public void onMusicPlayPre() {

    }

    /**
     * 暂停播放
     */
    @Override
    public void onMusicPlayPause() {

    }

    /**
     * 继续播放
     */
    @Override
    public void onMusicPlayResume() {

    }

    /**
     * 切换播放模式: 顺序、单曲、随机
     *
     * @param playModel
     */
    @Override
    public void onMusicChangePlayModel(SitechMusicSource.MusicPlayModels playModel) {

    }

    /**
     * 随机播放一首歌曲
     */
    @Override
    public void onMusicRandomPlay() {

    }

    /**
     * 当前是否有音频正在播放
     */
    @Override
    public boolean isMusicPlaying() {
        return false;
    }

    /**
     * 清除音频资源
     */
    @Override
    public void releaseMusicSource() {

    }

    /**
     * 通知相关其它页面UI进行音频数据改变。包括 当前音乐标题、图片、歌手等静态信息
     *
     * @param musicBean 当前播放的音乐对象
     */
    public void onChangeUiMusicInfo(SitechMusicBean musicBean) {
        if (SitechMusicNewManager.getInstance().getOnMusicUiListener() != null) {
            SitechMusicNewManager.getInstance().getOnMusicUiListener().onMusicInfo(musicBean);
        }
    }

    /**
     * 通知相关其它页面UI进行音频数据改变。 当前音乐进度
     *
     * @param musicBean 当前播放的音乐对象
     */
    public void onChangeUiMusicProgress(SitechMusicBean musicBean) {
        if (SitechMusicNewManager.getInstance().getOnMusicUiListener() != null) {
            SitechMusicNewManager.getInstance().getOnMusicUiListener().onMusicProgress(musicBean);
        }
    }

    /**
     * 通知相关其它页面UI进行音频数据改变。 当前音乐 状态--暂停或是继续播放
     *
     * @param musicBean 当前播放的音乐对象
     */
    public void onChangeUiMusicPlayStatus(SitechMusicBean musicBean) {
        if (SitechMusicNewManager.getInstance().getOnMusicUiListener() != null) {
            SitechMusicNewManager.getInstance().getOnMusicUiListener().onMusicPlayStatus(musicBean);
        }
    }
}
