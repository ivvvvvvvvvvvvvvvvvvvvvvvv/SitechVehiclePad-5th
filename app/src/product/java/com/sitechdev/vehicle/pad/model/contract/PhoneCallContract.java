package com.sitechdev.vehicle.pad.model.contract;

public interface PhoneCallContract {

    void registerPresenter(UICallback callback);

    int UPT_TIME = 0;
    int UPT_BT = 1;
    int UPT_USB = 2;
    int UPT_VOL = 3;
    int UPT_WIFI = 4;

    int KEY_PRIVATE = 5; // key private group id is 5
    int KEY_MICMUTE = 4; // key micmute group id is 4

    int STATE_DIALING = 0;
    int STATE_INCOMING = 1;
    int STATE_HELD = 2;

    int GROUP_DIAL = 0;
    int GROUP_INCOMING = 1;
    int GROUP_ACTIVE = 2;
    int GROUP_WAITING = 3;
    int GROUP_TRI = 4;

    interface UICallback {
        void showPhoneCall();

        void onPhoneNumber(String number, int whichSide); // pic_reduce_n = 0, pic_enlarge_n = 1

        void onPhoneName(String name, int whichSide);

        void hideWindow();

        void setDtmfNumber(String num);

        void showFastCall(String name, String number);

        void hideFastCall();

        void setElapsedTime(int seconds, int whichSide);

        void setConverseState(int state, int whichSide);

        void onFunctionKeyActivated(int which, boolean bActivated);

        void setButtonGroup(int group);

        void onStatusBarUpdate(int which);

        void hideVolume();

        void onAppStateChange();

    }
}
