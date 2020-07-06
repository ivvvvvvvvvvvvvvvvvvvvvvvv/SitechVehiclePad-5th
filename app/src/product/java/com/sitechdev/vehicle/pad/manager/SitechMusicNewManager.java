package com.sitechdev.vehicle.pad.manager;

/**
 * @author zhubaoqiang
 * @date 2019/8/26
 */
public class SitechMusicNewManager {
    private SitechMusicNewManager() {
    }

    private static final class SingleSitechMusicNewManager {
        private static final SitechMusicNewManager SINGLE = new SitechMusicNewManager();
    }

    public static SitechMusicNewManager getInstance() {
        return SingleSitechMusicNewManager.SINGLE;
    }
}
