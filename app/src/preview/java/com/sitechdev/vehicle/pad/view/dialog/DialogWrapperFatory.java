package com.sitechdev.vehicle.pad.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/5/9
 */
public class DialogWrapperFatory {

    public static DialogWrapper createDialogWrapper(
            @NonNull Context context,
            @NonNull DialogWrapperAdapter adapter,
            @Nullable IDialogClick dialogClick,
            @Nullable IDialogConfig dialogConfig){
        DialogWrapper dialogWrapper = new DialogWrapper(context);
        if (null != dialogClick){
            adapter.setDialogClick(dialogClick);
        }
        return dialogWrapper.setAdapter(adapter).configDiglog(dialogConfig);
    }

    public static DialogWrapper forBliuetoothTimeOut(@NonNull Context context){
        TDialogadapter dialogadapter = new TDialogadapter("连接失败",
                "请确认设备已打开而且在通信范围内",
                "好",
                null);
        IDialogConfig dialogConfig = new IDialogConfig() {
            @Override
            public void configDialog(Dialog dialog) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
        };
        return createDialogWrapper(context, dialogadapter,
                new LeftCancelDialogClock(),
                dialogConfig);
    }
    public static DialogWrapper createLoading(@NonNull Context context, String showText){
        LoadingDialogAdapter dialogAdapter = new LoadingDialogAdapter(showText);
        IDialogConfig dialogConfig = new IDialogConfig() {
            @Override
            public void configDialog(Dialog dialog) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
        };
        return createDialogWrapper(context, dialogAdapter,
                null, dialogConfig);
    }

    public static DialogWrapper createRadioList(Context context,
                                                String title, RecyclerView.Adapter adapter){
        RadioListAdapter listAdapter = new RadioListAdapter(title, adapter);
        IDialogConfig dialogConfig = new IDialogConfig() {
            @Override
            public void configDialog(Dialog dialog) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            }
        };
        DialogClick click = new DialogClick() {
            @Override
            public void onDialogClick(int id) {
                if (id == R.id.dialog_radio_list_close){
                    listAdapter.getDialogWrapper().dismiss();
                }
            }
        };
        return createDialogWrapper(context, listAdapter,
                click, dialogConfig);
    }
}
