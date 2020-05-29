package com.sitechdev.vehicle.pad.view.dialog;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/9/23
 */
public class RadioListAdapter extends DialogWrapperAdapter {

    private String title;

    private RecyclerView.Adapter adapter;

    public RadioListAdapter(String title, RecyclerView.Adapter adapter) {
        this.title = title;
        this.adapter = adapter;
    }

    @Override
    protected View getView() {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_radio_list,
                null, false);
        root.findViewById(R.id.dialog_radio_list_close).setOnClickListener(this);
        ((TextView)root.findViewById(R.id.dialog_radio_list_title)).setText(title);
        RecyclerView list = root.findViewById(R.id.dialog_radio_list);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
        return root;
    }
}
