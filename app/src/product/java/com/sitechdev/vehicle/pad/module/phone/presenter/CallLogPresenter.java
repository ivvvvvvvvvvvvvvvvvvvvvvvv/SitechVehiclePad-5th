package com.sitechdev.vehicle.pad.module.phone.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import com.my.hw.ATBluetooth;
import com.my.hw.BtCallBack;
import com.sitechdev.jpinyin.PinyinException;
import com.sitechdev.jpinyin.PinyinHelper;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.model.contract.CallLogContract;
import com.sitechdev.vehicle.pad.model.phone.CallLog;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;
import com.sitechdev.vehicle.pad.module.phone.utils.IndexUtils;
import com.sitechdev.vehicle.pad.module.phone.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通话记录代理类
 *
 * @author liuhe
 */
public class CallLogPresenter extends CallLogContract.Presenter {

    public static final String TAG = CallLogPresenter.class.getSimpleName();

    public CallLogPresenter() {
    }

    @SuppressLint("LongLogTag")
    private void logTest(String s) {
        Log.e("Test_Phone_CallLogPresenter","-----"+s);
    }

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(BtGlobalRef.callLogs)) {
            getView().showLoading();
            reqCallLogs();
        } else {
            getView().showLoadSuccessView();
        }
    }

    @Override
    public void reqCallLogs() {
        PhoneBtManager.getInstance().reqCallLogs();
    }

    @Override
    public void dial(String name, String number) {
        EventBusUtils.postEvent(new BTEvent(BTEvent.FAST_CALL_WITH_UI, new String[]{name, number}));
    }

    @Override
    public void onBTEvent(final BTEvent event) {
        switch (event.getEvent()) {
            // 通话记录下载个数
            case BTEvent.CL_DOWN_COUNT:
                if (getView() != null) {
                    getView().onClCount((int) event.getData());
                }
                break;
            case BTEvent.PB_CONN_FAILED:
                if (getView() != null) {
                    getView().showLoadFailedView();
                }
                break;
            case BTEvent.PB_OR_CL_UPDATE_SUCCESS:
                if ((int)event.getData() == BtGlobalRef.DOWNLOAD_FINISH_CALLLOGS) {
                    if (getView() != null) {
                        if (CollectionUtils.isEmpty(BtGlobalRef.callLogs)) {
                            getView().showEmptyView(false);
                        } else {
                            getView().showLoadSuccessView();
                        }
                    }
                } else if((int)event.getData() == BtGlobalRef.DOWNLOAD_FINISH_FAIL){
                    getView().showEmptyView(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFilterCallLogs(SysEvent event) {
        String action = event.getEvent();
        //TODO cold 显示不同类型的通话记录
        if (SysEvent.EB_AGAIN_CONTACTS_HISTORY.equals(action)) {
            int param = event.getDataCtrlMask();
            if (param == SysEvent.CALLLOG_RECORDS) {
                getView().refreshCallLogs(BtGlobalRef.callLogs);
            } else if (param == SysEvent.CALLLOG_MISSED) {
//                ArrayList<CallLog> callLogsMissed = BluetoothUtils.getCallLogByType(6);
//                if (CollectionUtils.isEmpty(callLogsMissed)) {
//                    getView().showNoFilerCallLogs();
//                } else {
//                    getView().refreshCallLogs(callLogsMissed);
//                }
            } else if (param == SysEvent.CALLLOG_DIALED) {
//                ArrayList<CallLog> callLogsDialed = BluetoothUtils.getCallLogByType(4);
//                if (CollectionUtils.isEmpty(callLogsDialed)) {
//                    getView().showNoFilerCallLogs();
//                } else {
//                    getView().refreshCallLogs(callLogsDialed);
//                }
            } else if (param == SysEvent.CALLLOG_RECEIVED) {
//                ArrayList<CallLog> callLogsReceived = BluetoothUtils.getCallLogByType(5);
//                if (CollectionUtils.isEmpty(callLogsReceived)) {
//                    getView().showNoFilerCallLogs();
//                } else {
//                    getView().refreshCallLogs(callLogsReceived);
//                }
            }
        }
    }
}