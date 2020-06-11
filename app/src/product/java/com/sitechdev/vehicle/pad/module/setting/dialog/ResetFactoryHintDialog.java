package com.sitechdev.vehicle.pad.module.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.sitechdev.vehicle.pad.R;


public class ResetFactoryHintDialog extends Dialog {

    public ResetFactoryHintDialog(Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_reset_factory_hint);
    }
}
