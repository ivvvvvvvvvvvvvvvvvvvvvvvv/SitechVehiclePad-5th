package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.pad.R;

import org.json.JSONArray;

import java.util.List;

/**
 * @author zhubaoqiang
 * @date 2019/8/22
 */
public class ContactsHolder extends VUIHolder {

    private RecyclerView contactsList;
    private ContactAdapter adapter;

    public ContactsHolder(@NonNull Context context){
        super(context, R.layout.holder_contacts);
    }

    public ContactsHolder(@NonNull LayoutInflater inflater){
        super(inflater, R.layout.holder_contacts);
        this.context = inflater.getContext();
    }

    @Override
    protected void getView() {
        contactsList = findViewById(R.id.contacts_holder_list);
        contactsList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        contactsList.setItemAnimator(new DefaultItemAnimator());
        adapter = new ContactAdapter(context);
        contactsList.setAdapter(adapter);
    }

    @Override
    protected void removed() {
        super.removed();
        if (null != adapter){
            if (null != adapter.getContacts()){
                adapter.getContacts().clear();
            }
        }
    }

    public void adapter(JSONArray contacts){
        adapter.addDatas(contacts);
    }

    public List<ContactAdapter.Contact> getContacts(){
        return adapter.getContacts();
    }
}
