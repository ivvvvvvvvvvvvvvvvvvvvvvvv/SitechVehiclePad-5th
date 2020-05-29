package com.sitechdev.vehicle.pad.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/5/7
 */
public class DialogWrapper {

    protected Context context;
    private Dialog dialog;
    private DialogWrapperAdapter adapter;
    private IDialogConfig dialogConfig;
    private OnDissmissListener listener;
//    private IDialogClick dialogClick;

    public DialogWrapper(@NonNull Context context) {
        this.context = context;
    }

    protected Context getContext(){
        return context;
    }

    public DialogWrapper setAdapter(DialogWrapperAdapter adapter){
        this.adapter = adapter;
        adapter.setDialogWrapper(this);
        return this;
    }

    public DialogWrapper configDiglog(IDialogConfig dialogConfig){
        this.dialogConfig = dialogConfig;
        dialogConfig.configDialog(getDialog());
        return this;
    }
//    public DialogWrapper dialogClick(IDialogClick dialogClick){
//        this.dialogClick = dialogClick;
//        return this;
//    }

    public DialogWrapper create(){
        return this;
    }
    public DialogWrapper show(){
        dialog = getDialog();
        dialog.show();
        return this;
    }
    public boolean isShowing(){
        return null == dialog ? false : dialog.isShowing();
    }

    public void dismiss(){
        if (null == dialog){
            return;
        }
        dialog.dismiss();
        if (null != listener){
            listener.onDissmiss();
        }
    }

    public Dialog getDialog(){
         if (null == dialog){
             dialog = createDialog();
         }
         return dialog;
    }

    public DialogWrapper onDismiss(OnDissmissListener listener){
        this.listener = listener;
        return this;
    }

    private Dialog createDialog() {
        if (null == adapter){
            throw new RuntimeException("No adapter");
        }
        dialog = new Dialog(context, R.style.set_dialog);
        dialog.setContentView(adapter.getView());
        return dialog;
    }

    public interface OnDissmissListener{
        void onDissmiss();
    }
}
