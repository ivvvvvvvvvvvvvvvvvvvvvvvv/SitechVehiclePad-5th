package com.sitechdev.vehicle.pad.module.login.util;

public class LoginIntent {

    public interface onLoginIntent {
        void onNext();
    }

    private onLoginIntent onLoginIntent = null;

    private LoginIntent() {
    }

    private static class Single {
        private static final LoginIntent SINGLE = new LoginIntent();
    }

    public static LoginIntent getInstance() {
        return Single.SINGLE;
    }

    public onLoginIntent getOnLoginIntent() {
        return onLoginIntent;
    }

    public void setOnLoginIntent(onLoginIntent onLoginIntent) {
        this.onLoginIntent = onLoginIntent;
    }

    public void clearCache() {
        this.onLoginIntent = null;
    }
}
