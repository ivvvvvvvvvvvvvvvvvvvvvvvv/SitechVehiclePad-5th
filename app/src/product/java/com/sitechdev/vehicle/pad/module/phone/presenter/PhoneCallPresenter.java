package com.sitechdev.vehicle.pad.module.phone.presenter;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;


import com.common.util.BroadcastUtil;
import com.common.util.MyCmd;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.contract.PhoneCallContract;
import com.sitechdev.vehicle.pad.model.phone.BTModel;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 拨打电话
 *
 * @author liuhe
 */
public class PhoneCallPresenter implements PhoneCallContract,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = PhoneCallPresenter.class.getSimpleName();

    private static PhoneCallPresenter instance = null;
    private static Handler handler = new Handler();
    /**
     * audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
     */
    private final int origVol = 13;
    private boolean isRequestGranted = false;

    private int[] needCount = new int[2];
    private int[] timeCount = new int[2];
    private String dtmfText = "";
    private UICallback callback;

    private PhoneCallPresenter(Context context) {
        EventBusUtils.register(this);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new PhoneCallPresenter(context);
        }
    }

    public static PhoneCallPresenter getInstance() {
        return instance;
    }

    private Runnable elapsedTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (needCount[0] > 0) {
                if (needCount[0] == 2) {
                    callback.setElapsedTime(timeCount[0], 0);
                }
                timeCount[0]++;
            }
            if (needCount[1] > 0) {
                if (needCount[1] == 2) {
                    callback.setElapsedTime(timeCount[1], 1);
                }
                timeCount[1]++;
            }
            handler.postDelayed(this, 1000);
        }
    };

    public void answer() {
        PhoneBtManager.getInstance().answer();
    }

    public void hangup() {
        PhoneBtManager.getInstance().hangup();
    }

    public void micMute() {
        PhoneBtManager.getInstance().micMute();
    }

    public void adtSwitch() {
        PhoneBtManager.getInstance().adtSwitch();
    }

    public void sendDtmf(char code) {
        if (dtmfText.length() > 18) {
            dtmfText = dtmfText.substring(1);
        }
        dtmfText += code;
        callback.setDtmfNumber(dtmfText);
        PhoneBtManager.getInstance().sendDtmf(code);
    }

    @Override
    public void registerPresenter(UICallback callback) {
        this.callback = callback;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBTEvent(BTEvent event) {

        switch (event.getEvent()) {
            case BTEvent.PHONE_CALL_STATE:
                String phoneNumber = "null";
                int which = 0;
                @BTEvent.PhoneState int state = (int) event.getData();
                logTest("onBTEvent-----state:" + state);
                switch (state) {
                    case BTEvent.INCOMING:
                        timeCount[0] = timeCount[1] = 0;
                        BroadcastUtil.sendToCarService(AppApplication.getContext(),
                                MyCmd.Cmd.BT_PHONE_STATUS, MyCmd.PhoneStatus.PHONE_ON);
                        handler.removeCallbacks(elapsedTimeRunnable);
                        callback.setButtonGroup(GROUP_INCOMING);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.setConverseState(STATE_INCOMING, which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.DIALING:
                        BroadcastUtil.sendToCarService(AppApplication.getContext(),
                                MyCmd.Cmd.BT_PHONE_STATUS, MyCmd.PhoneStatus.PHONE_ON);
                        timeCount[0] = timeCount[1] = 0;
                        handler.removeCallbacks(elapsedTimeRunnable);
                        callback.setButtonGroup(GROUP_DIAL);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.setConverseState(STATE_DIALING, which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.ACTIVE:
                        BroadcastUtil.sendToCarService(AppApplication.getContext(),
                                MyCmd.Cmd.BT_PHONE_STATUS, MyCmd.PhoneStatus.PHONE_ON);
                        handler.removeCallbacks(elapsedTimeRunnable);
                        handler.post(elapsedTimeRunnable);
                        callback.setButtonGroup(GROUP_ACTIVE);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.HANGUP:
                        BroadcastUtil.sendToCarService(AppApplication.getContext(),
                                MyCmd.Cmd.BT_PHONE_STATUS, MyCmd.PhoneStatus.PHONE_OFF);
                        isRequestGranted = false;
                        handler.removeCallbacks(elapsedTimeRunnable);
                        needCount[0] = needCount[1] = timeCount[0] = timeCount[1] = 0;
                        callback.hideWindow();
                        callback.setDtmfNumber(dtmfText = "");

                        break;
                    case BTEvent.TRI_HANGUP:
                        callback.setButtonGroup(GROUP_ACTIVE);
                        callback.setDtmfNumber(dtmfText = "");
                        break;
                    case BTEvent.HELD:
                        callback.setConverseState(STATE_HELD, which);
                        break;
                    case BTEvent.HELD_WAITING:
                    case BTEvent.TRI_WAITING:
                        callback.setButtonGroup(GROUP_WAITING);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.setConverseState(STATE_INCOMING, which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.TRI_DIALING:
                        callback.setButtonGroup(GROUP_DIAL);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.setConverseState(STATE_DIALING, which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.TRI_ACTIVE:
                        callback.setButtonGroup(GROUP_TRI);
                        callback.onPhoneNumber(phoneNumber, which);
                        callback.onPhoneName(getNameFromPhoneBook(phoneNumber), which);
                        callback.showPhoneCall();
                        break;
                    case BTEvent.TRI_HELD:
                        callback.setConverseState(STATE_HELD, which);
                        callback.setDtmfNumber(dtmfText = "");
                        break;
                    default:
                        break;
                }
                break;
            case BTEvent.EB_BT_AUDIO_CONNECTED:
                callback.onFunctionKeyActivated(KEY_PRIVATE, !((boolean) event.getData()));
                break;
            case BTEvent.EB_BT_MIC_STATE:
                callback.onFunctionKeyActivated(KEY_MICMUTE, (int) event.getData() == 1);
                break;
            // 对外处理
            case BTEvent.FAST_CALL_WITH_UI:
                if (event.getData() == null) {
                    callback.hideFastCall();
                } else {
                    String[] rawStr = (String[]) event.getData();
                    if (rawStr.length > 1) {
                        callback.showFastCall(rawStr[0], rawStr[1]);
                    }
                    PhoneBtManager.getInstance().diaTo(rawStr[1]);
                }
                break;
            case BTEvent.EB_BT_PHONE_CALL:
                PhoneBtManager.getInstance().diaTo((String) event.getData());
                break;
            case BTEvent.EB_BT_PHONE_CTRL:
                switch ((BTEvent.CallCtrl) event.getData()) {
                    case ANSWER:
                        PhoneBtManager.getInstance().answer();
                        break;
                    case HANGUP:
                        PhoneBtManager.getInstance().hangup();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private static void logTest(String s) {
        Log.e("Test_Phone_Presenter", "-----" + s);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
    }

    private static String getNameFromPhoneBook(String number) {
        logTest("getNameFromPhoneBook-----number:"+number);
        String name = "未知联系人";
        int count = 0;
        if (BtGlobalRef.contacts.size() > 0) {
            for (int i = 0; i < BtGlobalRef.contacts.size(); i++) {
                Contact c = BtGlobalRef.contacts.get(i);
                String tempNumber = number.replace(" ", "").replace("+86", "");
                String tempPhoneNumber = c.getPhoneNumber().replace(" ", "").replace("+86", "");
                if (tempPhoneNumber.equals(tempNumber)) {
                    if (count == 0) {
                        name = c.getName().trim();
                    } else {
                        name = name + "或" + c.getName().trim();
                    }
                    count++;
                }
            }
        }
        logTest("getNameFromPhoneBook-----number:"+number+" name:"+name);
        return name;
    }
}