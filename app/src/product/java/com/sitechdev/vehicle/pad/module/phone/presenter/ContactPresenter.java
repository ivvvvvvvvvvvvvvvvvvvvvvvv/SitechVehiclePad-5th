package com.sitechdev.vehicle.pad.module.phone.presenter;

import android.util.Log;

import com.sitechdev.jpinyin.PinyinException;
import com.sitechdev.jpinyin.PinyinHelper;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.model.contract.ContactContract;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;
import com.sitechdev.vehicle.pad.vui.VUIUtils;

/**
 * @author liuhe
 */
public class ContactPresenter extends ContactContract.Presenter {

    public static final String TAG = ContactPresenter.class.getSimpleName();

    public ContactPresenter() {
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
                    if (BtGlobalRef.contactName.size() > 0) {
                        ThreadManager.getInstance().addTask(() -> {
//                            VUIUtils.updataUserWordsContact(AppApplication.getContext(),
//                                    BtGlobalRef.contactName);
                        });
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