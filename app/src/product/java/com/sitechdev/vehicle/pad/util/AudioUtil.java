package com.sitechdev.vehicle.pad.util;

import com.sitechdev.vehicle.pad.manager.VolumeControlManager;

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

//    private static AudioManager mAudioManager = null;

    public static void initAudioManager() {
//        mAudioManager = (AudioManager) AppApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 增大音量
     */
    public static void onKeyUpVolume(int streamType) {
//        if (mAudioManager == null) {
//            initAudioManager();
//        }
//        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        VolumeControlManager.getInstance().setCurrentVolumeValue(true);
    }

    /**
     * 最大音量
     */
    public static void maxVolume() {
        VolumeControlManager.getInstance().setCurrentVolume(100);
    }



    /**
     * 降低音量
     */
    public static void onKeyDownVolume(int streamType) {
//        if (mAudioManager == null) {
//            initAudioManager();
//        }
//        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        VolumeControlManager.getInstance().setCurrentVolumeValue(false);
    }

    /**
     * 静音
     */
    public static void onKeyMuteVolume(int streamType) {
//        if (mAudioManager == null) {
//            initAudioManager();
//        }
//        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
        VolumeControlManager.getInstance().setMuteVolume(true);
    }

    /**
     * 静音取消
     *
     * @deprecated 使用{@link VolumeControlManager.getInstance().setMuteVolume(false);}代替
     */
    @Deprecated
    public static void onKeyUnmuteVolume(int streamType) {
//        if (mAudioManager == null) {
//            initAudioManager();
//        }
//        mAudioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
        VolumeControlManager.getInstance().setMuteVolume(false);
    }

    /**
     * 设置音量值
     *
     * @param value
     */
    public static void onKeyVolumeValue(int value) {
        VolumeControlManager.getInstance().setCurrentVolume(value);
    }
}
