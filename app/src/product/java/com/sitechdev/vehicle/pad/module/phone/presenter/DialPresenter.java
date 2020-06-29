package com.sitechdev.vehicle.pad.module.phone.presenter;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.DialEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.contract.DialContract;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;

/**
 * @author liuhe
 */
public class DialPresenter extends DialContract.Presenter {

    public static final String TAG = DialPresenter.class.getSimpleName();
    private static DialPresenter instance = new DialPresenter();
    private String dialPadPhoneNumStr = "";

    public DialPresenter() {
    }

    public static DialPresenter getInstance() {
        return instance;
    }

    public void dial() {
        PhoneBtManager.getInstance().diaTo(dialPadPhoneNumStr);
        dialPadPhoneNumStr = "";
        getView().hideInputWindow();
    }

    public void dial(String phoneNumber) {
        PhoneBtManager.getInstance().diaTo(phoneNumber);
        dialPadPhoneNumStr = "";
        getView().hideInputWindow();
    }

    @Override
    public void passKey(char key) {
        if (dialPadPhoneNumStr.length() < 17) {
            getView().onDialNumber(dialPadPhoneNumStr += key);
        }
    }

    @Override
    public void refreshPhoneNumStr(DialEvent dialEvent) {
        switch (dialEvent.eventKey) {
            case DialEvent.KEY_DIAL_INPUT_HIDE:
                dialPadPhoneNumStr = "";
                getView().hideInputWindow();
                break;
            case DialEvent.KEY_DIAL_INPUT_SHOW:
                dialPadPhoneNumStr = dialEvent.phoneNum;
                if (StringUtils.isEmpty(dialPadPhoneNumStr)) {
                    getView().hideInputWindow();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSysEvent(SysEvent event) {
        if (null == event) {
            return;
        }
        boolean isBtEvent = SysEvent.EB_SYS_RESTORE_FACTORY.equals(event.getEvent()) || SysEvent.EB_SYS_BT_STATE.equals(event.getEvent());
        if (!isBtEvent) {
            return;
        }
        if (SysEvent.EB_SYS_RESTORE_FACTORY.equals(event.getEvent())) {
            //TODO cold 恢复出厂设置所需动作
        } else if (SysEvent.EB_SYS_BT_STATE.equals(event.getEvent())) {
            //TODO cold 蓝牙连接状态变化所需动作
//            if (event.getDataCtrlMask() == SysEvent.DEV_BT) {
//                if (!DataFactory.produceMemData().getDeviceState().isBTConn) {
//                    getView().hideInputWindow();
//                }
//            }
        }
    }
}