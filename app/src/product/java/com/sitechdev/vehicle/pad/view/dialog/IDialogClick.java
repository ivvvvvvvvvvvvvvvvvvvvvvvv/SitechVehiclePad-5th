package com.sitechdev.vehicle.pad.view.dialog;

import android.support.annotation.IdRes;

/**
 * @author zhubaoqiang
 * @date 2019/5/7
 */
public interface IDialogClick {
    void onDialogClick(@IdRes int id);
    void setAdapter(DialogWrapperAdapter adapter);
    DialogWrapperAdapter getAdapter();
}
