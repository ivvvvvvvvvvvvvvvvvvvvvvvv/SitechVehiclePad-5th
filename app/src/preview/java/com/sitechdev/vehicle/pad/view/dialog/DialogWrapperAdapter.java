package com.sitechdev.vehicle.pad.view.dialog;

import android.content.Context;
import android.view.View;

/**
 * @author zhubaoqiang
 * @date 2019/5/7
 */
public abstract class DialogWrapperAdapter implements View.OnClickListener {

    protected Context context;
    protected DialogWrapper dialogWrapper;
    protected IDialogClick dialogClick;

    protected void setDialogWrapper(DialogWrapper dialogWrapper){
        this.dialogWrapper = dialogWrapper;
        this.context = dialogWrapper.getContext();
    }

    public DialogWrapper getDialogWrapper() {
        return dialogWrapper;
    }

    public void setDialogClick(IDialogClick dialogClick){
        this.dialogClick = dialogClick;
        dialogClick.setAdapter(this);
    }

    protected abstract View getView();

    @Override
    public void onClick(View v) {
        if (null != dialogClick){
            dialogClick.onDialogClick(v.getId());
        }
    }
}
