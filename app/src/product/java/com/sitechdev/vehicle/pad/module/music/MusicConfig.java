package com.sitechdev.vehicle.pad.module.music;

import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.vui.VUIUtils;

public class MusicConfig {
    private static MusicConfig INSTANCE;
    public static final int MUSIC_CURRENT_SHOWN_USB = 0;
    public static final int MUSIC_CURRENT_SHOWN_BT = 1;


    private MusicConfig() {
    }

    public static MusicConfig getInstance() {
        if (null == INSTANCE) {
            synchronized (MusicConfig.class) {
                if (null == INSTANCE) {
                    INSTANCE = new MusicConfig();
                }
            }
        }
        return INSTANCE;
    }

    public enum MusicModeType {
        MUSIC_MODE_LOOP(1),
        MUSIC_MODE_SINGLE(2),
        MUSIC_MODE_RANDOM(3);

        private int value = 0;

        private MusicModeType(int value) {
            this.value = value;
        }

        public static MusicModeType valueOf(int value) {
            switch (value) {
                case 1:
                    return MUSIC_MODE_LOOP;
                case 2:
                    return MUSIC_MODE_SINGLE;
                case 3:
                    return MUSIC_MODE_RANDOM;
                default:
                    return null;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private MusicModeType modeType;
    private MusicInfo currentMusicInfo;
    private int progress;
    private boolean isUdiskMounted = VUIUtils.isUdiskExist();

    public boolean isUdiskMounted() {
        return isUdiskMounted;
    }

    public void setUdiskMounted(boolean udiskMounted) {
        isUdiskMounted = udiskMounted;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public MusicInfo getCurrentMusicInfo() {
        return currentMusicInfo;
    }

    public void setCurrentMusicInfo(MusicInfo currentMusicInfo) {
        this.currentMusicInfo = currentMusicInfo;
    }

    public MusicModeType getModeType() {
        return this.modeType;
    }

    public void setModeType(MusicModeType modeType) {
        this.modeType = modeType;
        MusicManager.getInstance().changeMode((code, s) -> {
            //TODO 回调
        }, modeType.value);
    }
}
