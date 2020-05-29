package com.sitechdev.vehicle.pad.util;

import android.content.Context;
import android.media.AudioManager;

import com.sitechdev.vehicle.pad.app.AppApplication;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AudioUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/29 0029 14:36
 * 修改时间：
 * 备注：
 */
public class AudioUtil {

    private static AudioManager mAudioManager = null;

    public static void initAudioManager() {
        mAudioManager = (AudioManager) AppApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 增大音量
     */
    public static void onKeyUpVolume(int streamType) {
        if (mAudioManager == null) {
            initAudioManager();
        }
        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 降低音量
     */
    public static void onKeyDownVolume(int streamType) {
        if (mAudioManager == null) {
            initAudioManager();
        }
        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

}
