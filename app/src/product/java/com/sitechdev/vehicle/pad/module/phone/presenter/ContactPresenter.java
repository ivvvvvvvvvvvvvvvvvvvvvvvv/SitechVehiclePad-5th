package com.sitechdev.vehicle.pad.module.phone.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.my.hw.ATBluetooth;
import com.my.hw.BtCallBack;
import com.sitechdev.jpinyin.PinyinException;
import com.sitechdev.jpinyin.PinyinHelper;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.model.contract.ContactContract;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;
import com.sitechdev.vehicle.pad.module.phone.utils.IndexUtils;
import com.sitechdev.vehicle.pad.module.phone.utils.PinyinComparator;

import java.util.Collections;
import java.util.List;

/**
 * @author liuhe
 */
public class ContactPresenter extends ContactContract.Presenter {
    private BtCallBack btCallBack = new BtCallBack() {
        @Override
        public void onBtCallback(int cmdId, int param2, String param3, String param4) {
            ThreadUtils.runOnUIThread(() -> {
                if(cmdId == ATBluetooth.RETURN_PHONE_BOOK_DATA){//回调联系人信息
                    logTest("RETURN_PHONE_BOOK_DATA-----param3:"+param3+" param4:"+param4);
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
                } else if(cmdId == ATBluetooth.RETURN_PHONE_BOOK){//联系人回调结束
                    logTest("RETURN_PHONE_BOOK_OVER-----param3:"+param3+" param4:"+param4+" size:"+BtGlobalRef.contacts.size());
                    EventBusUtils.postEvent(new BTEvent(BTEvent.PB_DOWN_COUNT, BtGlobalRef.contacts.size()));
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
                        }
                    });
                }
            });
        }
    };

    public static final String TAG = ContactPresenter.class.getSimpleName();

    public ContactPresenter() {
//        PhoneBtManager.getInstance().setBtCallback(btCallBack);
    }

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(BtGlobalRef.contactSorts)) {
            getView().showLoading();
            reqPhoneBook();
        } else {
            getView().showLoadSuccessView();
        }
    }

    @Override
    public void dial(String dialPadPhoneNum) {
        PhoneBtManager.getInstance().diaTo(dialPadPhoneNum);
    }

    @Override
    public void reqPhoneBook() {
        PhoneBtManager.getInstance().reqPhoneBook();
    }

    @Override
    public void onBTEvent(final BTEvent event) {
        switch (event.getEvent()) {
            case BTEvent.PB_DOWN_COUNT:
                if (getView() != null) {
                    getView().onPbCount((int) event.getData());
                }
                break;
            case BTEvent.PB_CONN_FAILED:
                if (getView() != null) {
                    getView().showLoadFailedView();
                }
                break;
            case BTEvent.PB_OR_CL_UPDATE_SUCCESS:
                boolean isPhoneBook = (boolean) event.getData();
                if (isPhoneBook) {
                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_SR_CONTACT_DICT));
                    if (getView() != null) {
                        if (CollectionUtils.isEmpty(BtGlobalRef.contactSorts)) {
                            getView().showEmptyView();
                        } else {
                            getView().showLoadSuccessView();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPbDataRecvd(String name, String number) {
        number = number.replace("-", "");
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
        EventBusUtils.postEvent(new BTEvent(BTEvent.PB_DOWN_COUNT, BtGlobalRef.contacts.size()));
    }

    private void logTest(String msg){
        Log.e("Test_Phone_Contactp","-----"+msg);
    }
}