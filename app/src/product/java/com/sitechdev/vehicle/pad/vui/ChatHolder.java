package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.view.VolumeView;

/**
 * @author zhubaoqiang
 * @date 2019/8/19
 */
public class ChatHolder extends VUIHolder {

    private VolumeView volumeView;
    private TextView textView;

    public ChatHolder(@NonNull Context context){
        super(context, R.layout.holder_chat);
    }

    public ChatHolder(@NonNull LayoutInflater inflater){
        super(inflater, R.layout.holder_chat);
    }

    @Override
    protected void getView() {
        volumeView = findViewById(R.id.chat_holder_volume_view);
        textView = findViewById(R.id.chat_holder_text);
    }

    @Override
    public VUIHolder added(ViewGroup container, VUIHolder currentHolder) {
        volumeView.start();
        textView.setSelected(true);
        textView.setText(context.getResources().getString(R.string.vui_welcome_text));
        return super.added(container, currentHolder);
    }

    @Override
    protected void removed() {
        super.removed();
        volumeView.stop();
        textView.setSelected(false);
    }

    public VolumeView getVolumeView() {
        return volumeView;
    }

    public void setVolumeView(VolumeView volumeView) {
        this.volumeView = volumeView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
