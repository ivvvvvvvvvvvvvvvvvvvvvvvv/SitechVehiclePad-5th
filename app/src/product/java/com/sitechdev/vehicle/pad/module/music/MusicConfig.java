package com.sitechdev.vehicle.pad.module.music;

import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
    private boolean isUdiskMounted = isUdiskExist();

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

    public static boolean isUdiskExist() {

        String path = "/proc/mounts";

        boolean ret = false;
        try {
            String encoding = "GBK";
            File file = new File(path);
            if ((file.isFile()) && (file.exists())) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while (((lineTxt = bufferedReader.readLine()) != null) && (!ret)) {
                    String[] a = lineTxt.split(" ");//将读出来的一行字符串用 空格 来分割成字符串数组并存储进数组a里面
                    String str = a[0];//取出位置0处的字符串

                    if ((str.contains("/dev/block/vold")) &&
                            ((a[1].toLowerCase().contains("udisk")) || (a[1].toLowerCase().contains("usbdisk")))) {
                        ret = true;
                    }
                }

                read.close();
            } else {
// Log.d("StorageDeviceManager", "can't find file: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
