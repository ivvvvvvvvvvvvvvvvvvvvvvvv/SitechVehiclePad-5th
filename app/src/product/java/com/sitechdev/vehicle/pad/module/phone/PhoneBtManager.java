package com.sitechdev.vehicle.pad.module.phone;

import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.common.util.BroadcastUtil;
import com.common.util.MyCmd;
import com.my.hw.ATBluetooth;
import com.my.hw.BluetoothEvent;
import com.my.hw.BtCallBack;
import com.my.hw.SettingConfig;
import com.sitechdev.jpinyin.PinyinException;
import com.sitechdev.jpinyin.PinyinHelper;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.model.phone.CallLog;
import com.sitechdev.vehicle.pad.model.phone.CallingInfo;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.music.BtMusicManager;
import com.sitechdev.vehicle.pad.module.phone.utils.IndexUtils;
import com.sitechdev.vehicle.pad.module.phone.utils.PinyinComparator;

import java.util.Collections;
import java.util.List;

import static com.sitechdev.vehicle.pad.event.SysEvent.EB_SYS_BT_STATE;

public class PhoneBtManager {
    private static PhoneBtManager INSTANCE;
    private ATBluetooth mATBluetooth;
    private boolean isPullNewCalllog = false;
    public boolean isPlayingMusic = false;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (AudioManager.AUDIOFOCUS_LOSS == focusChange) {
                BtMusicManager.getInstance().btCtrlPause();
            }
        }
    };
    private PhoneBtManager() {
    }

    public void initPhone() {
        mATBluetooth = ATBluetooth.create();
        mATBluetooth.setBtCallback(btCallBack);
        mATBluetooth.write(8);
    }

    private BtCallBack btCallBack = new BtCallBack() {
        @Override
        public void onBtCallback(int cmdId, int param2, String param3, String param4) {
            logTest("onBtCallback-----cmdId:" + cmdId + " param2:" + param2 + " param3:" + param3 + " param4:" + param4);
//            ThreadManager.getInstance().addTask(() -> {
                switch (cmdId) {
                    case ATBluetooth.RETURN_PHONE_CALLLOG: {//通话记录
                        handleCallLogCallback(param2, param3, param4);
                    }
                    break;
                    case ATBluetooth.RETURN_PHONE_CALLLOG_END: {//通话记录下载结束
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PB_OR_CL_UPDATE_SUCCESS, BtGlobalRef.DOWNLOAD_FINISH_CALLLOGS));
                        isPullNewCalllog = false;
                    }
                    break;
                    case ATBluetooth.RETURN_PHONE_BOOK_DATA: {//下载通讯录
                        handleContactCallback(param2, param3, param4, false);
                    }
                    break;
                    case ATBluetooth.RETURN_PHONE_BOOK: {//通讯录下载结束
                        handleContactCallback(param2, param3, param4, true);
                    }
                    break;
                    case ATBluetooth.RETURN_CALLING: {//接通
                        CallingInfo info = new CallingInfo();
                        info.setPhoneNum(param3);
                        info.setState(BTEvent.ACTIVE);
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PHONE_CALL_STATE,
                                info));
                    }
                    break;
                    case ATBluetooth.RETURN_CALL_INCOMING: {//打进电话
                        CallingInfo info = new CallingInfo();
                        info.setPhoneNum(param3);
                        info.setState(BTEvent.INCOMING);
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PHONE_CALL_STATE,
                                info));
                    }
                    break;
                    case ATBluetooth.RETURN_CALL_OUT: {//拨出
                        CallingInfo info = new CallingInfo();
                        info.setPhoneNum(param3);
                        info.setState(BTEvent.DIALING);
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PHONE_CALL_STATE,
                                info));
                    }
                    break;
                    case ATBluetooth.RETURN_CALL_END: {//通话结束
                        CallingInfo info = new CallingInfo();
                        info.setPhoneNum(param3);
                        info.setState(BTEvent.HANGUP);
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PHONE_CALL_STATE,
                                info));
                        reqNewCalllog();
                    }
                    break;
                    case ATBluetooth.RETURN_HFP_INFO: {//连接状态
                        boolean connect = param2 == 3;
                        if (connect != SettingConfig.getInstance().isHFPConnected()) {
                            SettingConfig.getInstance().setHFPConnected(connect);
                            queryBtInfo();
                            if (!connect) {
                                SettingConfig.getInstance().setConnectBtName("");
                                SettingConfig.getInstance().setHFPConnected(false);
                                SettingConfig.getInstance().setA2dpConnected(false);
                                BtGlobalRef.contacts.clear();
                                BtGlobalRef.contactSorts.clear();
                                BtGlobalRef.callLogs.clear();
                                BtGlobalRef.contactName.clear();
                                EventBusUtils.postEvent(new SysEvent(EB_SYS_BT_STATE, false));
                            }
                        }
                        if (!TextUtils.isEmpty(param3) && !param3.equals("null")) {
                            SettingConfig.getInstance().setConnectBtAdd(param3);
                        }
                    }
                    break;
                    case ATBluetooth.RETURN_HFP_CONNECT_NAME: {//返回连接的蓝牙设备名称
                        SettingConfig.getInstance().setConnectBtName(param3);
                        SettingConfig.getInstance().setHFPConnected(true);
                        SettingConfig.getInstance().setA2dpConnected(true);
                        EventBusUtils.postEvent(new SysEvent(EB_SYS_BT_STATE, true));
                    }
                    break;
                    case ATBluetooth.RETURN_NAME:{//本地蓝牙名称
                        if(!StringUtils.isEmpty(param3)) {
                            SettingConfig.getInstance().setLocalBtName(param3);
                        }
                    }
                    break;
                    case ATBluetooth.RETURN_ADDRESS:{//本地蓝牙地址
                        if(!StringUtils.isEmpty(param3) && !SettingConfig.getInstance().getLocalBtName().contains("SITECH")) {
                            modifyBtName("SITECH_"+param3);
                        }
                    }
                    break;
                    case ATBluetooth.RETURN_A2DP_ID3_NAME:
                        EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_TITLE, param3));
                        break;
                    case ATBluetooth.RETURN_A2DP_ID3_ARTIST:
                        EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_ART, param3));
                        break;
                    case ATBluetooth.RETURN_A2DP_OFF:
                        isPlayingMusic = false;
                        EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_OFF, null));
                        break;
                    case ATBluetooth.RETURN_A2DP_ON:
                        isPlayingMusic = true;
                        BroadcastUtil.sendToCarServiceSetSource(AppApplication.getContext(),
                                MyCmd.SOURCE_BT_MUSIC);
                        int result = AppApplication.getAudioManager().requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                        EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_ON, null));
                        break;
                    default:
                        break;
                }
//            });
        }
    };

    public static PhoneBtManager getInstance() {
        if (null == INSTANCE) {
            synchronized (PhoneBtManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PhoneBtManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 查询已连接蓝牙的名称
     */
    public void queryBtInfo() {
        mATBluetooth.write(ATBluetooth.GET_HFP_CONNECT_NAME);
    }

    /**
     * 拨号
     *
     * @param phoneNum
     */
    public void diaTo(String phoneNum) {
        mATBluetooth.write(ATBluetooth.REQUEST_CALL, phoneNum);
    }

    /**
     * 根据姓名拨打电话（有通讯录的前提下）
     * @param contactName
     */
    public void diaToName(String contactName) {
        if (BtGlobalRef.contactSorts.size() > 0 && StringUtils.isEmpty(contactName)) {
            for (Contact contact : BtGlobalRef.contacts) {
                if (contact.getName().equals(contactName) && StringUtils.isEmpty(contact.getPhoneNumber())) {
                    mATBluetooth.write(ATBluetooth.REQUEST_CALL, contact.getPhoneNumber());
                }
            }
        }
    }

    /**
     * 下载通讯录
     */
    public void reqPhoneBook() {
        mATBluetooth.write(ATBluetooth.REQUEST_PHONE_BOOK);
    }

    /**
     * 下载最新一次的通话记录，每次通话结束时执行
     */
    private void reqNewCalllog(){
        mATBluetooth.write(ATBluetooth.REQUEST_CALL_LOG_ALL,"1");
        isPullNewCalllog = true;
    }

    /**
     * 下载通讯记录
     */
    public void reqCallLogs() {
        mATBluetooth.write(ATBluetooth.REQUEST_CALL_LOG_ALL);
        isPullNewCalllog = false;
    }

    /**
     * 接听电话
     */
    public void answer() {
        mATBluetooth.write(ATBluetooth.REQUEST_ANSWER);
    }

    /**
     * 挂断电话
     */
    public void hangup() {
        mATBluetooth.write(ATBluetooth.REQUEST_HANG);
    }

    /**
     * 静音
     */
    public void micMute() {
        mATBluetooth.write(ATBluetooth.REQUEST_MIC);
    }

    private void handleCallLogCallback(int type, String dateAndTime, String nameAndNum) {
        String name = "";
        String number = "";
        String date = "";
        String time = "";
        if (nameAndNum.contains(";")) {
            String[] s = nameAndNum.split(";");
            if (s.length == 2) {
                name = s[0];
                number = s[1];
            }
            if (StringUtils.isEmpty(name) && !StringUtils.isEmpty(number)) {
                name = number;
            }
        }
        if (dateAndTime.contains("T")) {
            String[] s = dateAndTime.split("T");
            if (s.length == 2) {
                date = s[0];
                time = s[1];
            }
        }
        logTest("handleCallLogCallback-----type:" + type + " name:" + name + " number:" + number + " date:" + date + " time:" + time);
        if (isPullNewCalllog) {
            if (BtGlobalRef.callLogs.size() < 0 || (BtGlobalRef.callLogs.size()>0 && !BtGlobalRef.callLogs.get(0).getPhoneNumber().equals(number)) || (BtGlobalRef.callLogs.size()>0 && !BtGlobalRef.callLogs.get(0).getName().equals(name)) || (BtGlobalRef.callLogs.size()>0 && !BtGlobalRef.callLogs.get(0).getDate().equals(date)) || (BtGlobalRef.callLogs.size()>0 && !BtGlobalRef.callLogs.get(0).getTime().equals(time))) {
                BtGlobalRef.callLogs.add(0, new CallLog(type, name, number, date, time));
            }
        } else {
            BtGlobalRef.callLogs.add(new CallLog(type, name, number, date, time));
        }
    }

    private void handleContactCallback(int param2, String param3, String param4, boolean isFinish) {
        //没有下载结束
        if (!isFinish) {
            String number = param3.replace("-", "");
            String name = param4;
            String abbr;
            String contactName = number;
            //&& !FormatUtils.isStartWithNumber(name)
            if (!name.isEmpty()) {
                try {
                    contactName = name;
                    String tempName = name.replace(" ", "");
                    abbr = PinyinHelper.convertToPinyinString(tempName, "_").toUpperCase();
                } catch (PinyinException e) {
                    abbr = "#";
                    e.printStackTrace();
                }
            } else {
                abbr = "#";
            }
            number = number.replace(" ", "");
            BtGlobalRef.contacts.add(new Contact(abbr, contactName, number));
            BtGlobalRef.contactName.add(name);
        } else { //通讯录下载结束
            if (param2 == 1) {//下载失败
                EventBusUtils.postEvent(new BTEvent(BTEvent.PB_OR_CL_UPDATE_SUCCESS, BtGlobalRef.DOWNLOAD_FINISH_FAIL));
            } else if (param2 == 2) {//下载结束
                EventBusUtils.postEvent(new BTEvent(BTEvent.PB_DOWN_COUNT,
                        BtGlobalRef.contacts.size()));
                if (CollectionUtils.isEmpty(BtGlobalRef.callLogs)) {
                    reqCallLogs();
                }
                ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Void>() {
                    @Nullable
                    @Override
                    public Void doInBackground() {
                        Collections.sort(BtGlobalRef.contacts, PinyinComparator.getDefault());
                        List<Contact> tmpContacts = IndexUtils.sortContacts(BtGlobalRef.contacts);
                        BtGlobalRef.contactSorts.clear();
                        BtGlobalRef.contactSorts.addAll(tmpContacts);
                        return null;
                    }

                    @Override
                    public void onSuccess(@Nullable Void result) {
                        super.onSuccess(result);
                        EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_SR_CONTACT_DICT));
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PB_OR_CL_UPDATE_SUCCESS, BtGlobalRef.DOWNLOAD_FINISH_CONTACT));
                    }
                });
            }
        }
    }

    private void logTest(String msg) {
        Log.e("Test_Phone_manager", "-----" + msg);
    }

    /**
     * 拨打分机号
     */
    public void adtSwitch() {
        mATBluetooth.write(ATBluetooth.REQUEST_DTMF);
    }

    public void sendDtmf(char code) {
        mATBluetooth.write(ATBluetooth.REQUEST_DTMF);
    }

    public void reject() {
        mATBluetooth.write(ATBluetooth.REQUEST_REJECT);
    }

    private void modifyBtName(String name){
        mATBluetooth.write(ATBluetooth.REQUEST_NAME,name);
    }
}
