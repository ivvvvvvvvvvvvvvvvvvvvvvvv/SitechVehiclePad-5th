package com.sitechdev.vehicle.pad.module.phone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.ForbidClickEnable;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.DialEvent;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.adapter.SearchBookAdapter;
import com.sitechdev.vehicle.pad.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 拨号window
 *
 * @author hazens
 */
public class PhoneDialWindow extends PopupWindow implements SearchBookAdapter.OnItemClickListener {

    private SearchBookAdapter adapter;
    private List<Contact> tempContacts = new ArrayList<>();
    private TextView numberTxt;
    private ImageView mBackView;
    private RecyclerView mContactsListView;
    private Context mContext;
    private String dialPadPhoneNumStr = "";

    public PhoneDialWindow(Context context) {
        super(context);
        this.mContext = context;
        setHeight(590);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.window_dial_input, null,
                false);
        initView(contentView);
        initListener();
        setContentView(contentView);
    }

    private void initListener() {
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialPadPhoneNumStr.length() > 1) {
                    dialPadPhoneNumStr = dialPadPhoneNumStr.substring(0,
                            dialPadPhoneNumStr.length() - 1);
                } else {
                    dialPadPhoneNumStr = "";
                }
                numberTxt.setText(dialPadPhoneNumStr);
                EventBusUtils.postEvent(new DialEvent(DialEvent.KEY_DIAL_INPUT_SHOW,
                        dialPadPhoneNumStr));
            }
        });

        mBackView.setOnLongClickListener(view -> {
            dialPadPhoneNumStr = "";
            numberTxt.setText(dialPadPhoneNumStr);
            EventBusUtils.postEvent(new DialEvent(DialEvent.KEY_DIAL_INPUT_SHOW,
                    dialPadPhoneNumStr));
            return true;
        });
    }

    private void initView(View contentView) {
        numberTxt = contentView.findViewById(R.id.tv_dial_number);
        mBackView = contentView.findViewById(R.id.tv_dial_back);

        mContactsListView = contentView.findViewById(R.id.rv_contacts_list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mContactsListView.addItemDecoration(new SpacesItemDecoration(9));
        mContactsListView.setLayoutManager(manager);
        adapter = new SearchBookAdapter(mContext);
        mContactsListView.setAdapter(adapter);

        numberTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    searchContacts(charSequence);
                } else {
                    tempContacts.clear();
                    handleSearchView(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        adapter.setOnItemClickListener(this);
    }


    public void refreshView(String number) {
        dialPadPhoneNumStr = number;
        numberTxt.setText(dialPadPhoneNumStr);
    }

    private void searchContacts(CharSequence str) {
        tempContacts.clear();
        //如果搜索条件以0 1 +开头则按号码搜索
        //if (str.toString().startsWith("0") || str.toString().startsWith("1")
        //        || str.toString().startsWith("+")) {
        //}
        for (int i = 0; i < BtGlobalRef.contacts.size(); i++) {
            Contact contact = BtGlobalRef.contacts.get(i);
            if (null != contact && contact.getPhoneNumber().contains(str)) {
                handleSearchView(true);
                tempContacts.add(contact);
            }
        }
        if (tempContacts.size() == 0) {
            handleSearchView(false);
        }
        adapter.updateList(tempContacts);
        adapter.setChangeStr(str.toString());
    }

    private void handleSearchView(boolean visible) {
        mContactsListView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClicked(int position) {
        if (ForbidClickEnable.isForbidClick(700)) {
            return;
        }
        String phoneNum = tempContacts.get(position).getPhoneNumber();
        PhoneBtManager.getInstance().diaTo(phoneNum);
        numberTxt.setText(phoneNum);
        EventBusUtils.postEvent(new DialEvent(DialEvent.KEY_DIAL_INPUT_HIDE, dialPadPhoneNumStr));
    }
}