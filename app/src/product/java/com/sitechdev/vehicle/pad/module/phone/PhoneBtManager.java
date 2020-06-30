package com.sitechdev.vehicle.pad.module.phone;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.my.hw.ATBluetooth;
import com.my.hw.BtCallBack;
import com.my.hw.SettingConfig;
import com.sitechdev.jpinyin.PinyinException;
import com.sitechdev.jpinyin.PinyinHelper;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.model.phone.CallLog;
import com.sitechdev.vehicle.pad.model.phone.CallingInfo;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.utils.IndexUtils;
import com.sitechdev.vehicle.pad.module.phone.utils.PinyinComparator;

import java.util.Collections;
import java.util.List;

import static com.sitechdev.vehicle.pad.event.SysEvent.EB_SYS_BT_STATE;

public class PhoneBtManager {
    private static PhoneBtManager INSTANCE;
    private ATBluetooth mATBluetooth;

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
            ThreadUtils.runOnUIThread(() -> {
                switch (cmdId) {
                    case ATBluetooth.RETURN_PHONE_CALLLOG: {//通话记录
                        handleCallLogCallback(param2, param3, param4);
                    }
                    break;
                    case ATBluetooth.RETURN_PHONE_CALLLOG_END: {//通话记录下载结束
                        EventBusUtils.postEvent(new BTEvent(BTEvent.PB_OR_CL_UPDATE_SUCCESS, false));
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
                    case ATBluetooth.RETURN_CALLING: {//拨出
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
                    }
                    break;
                    case ATBluetooth.RETURN_HFP_INFO: {//连接状态
                        boolean connect = param2 == 3;
                        if (connect != SettingConfig.getInstance().isHFPConnected()) {
                            SettingConfig.getInstance().setHFPConnected(connect);
                            queryBtInfo();
                            if (!connect) {
                                SettingConfig.getInstance().setConnectBtName("");
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
                        EventBusUtils.postEvent(new SysEvent(EB_SYS_BT_STATE, true));
                    }
                    break;
                    default:
                        break;
                }
            });
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
     * 下载通讯录
     */
    public void reqPhoneBook() {
        mATBluetooth.write(ATBluetooth.REQUEST_PHONE_BOOK);
    }

    /**
     * 下载通讯记录
     */
    public void reqCallLogs() {
        mATBluetooth.write(ATBluetooth.REQUEST_CALL_LOG_ALL);
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
        BtGlobalRef.callLogs.add(new CallLog(type, name, number, date, time));
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
        } else { //通讯录下载结束
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
                    EventBusUtils.postEvent(new BTEvent(BTEvent.PB_OR_CL_UPDATE_SUCCESS, true));
                }
            });
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
}
