package com.sitechdev.vehicle.pad.view.dialog;

/**
 * @author zhubaoqiang
 * @date 2019/5/9
 */
public abstract class DialogClick implements IDialogClick {
    protected DialogWrapperAdapter adapter;

    @Override
    public void setAdapter(DialogWrapperAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public DialogWrapperAdapter getAdapter(){
        return adapter;
    }

    @Override
    public abstract void onDialogClick(int id);
}
