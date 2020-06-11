package com.sitechdev.vehicle.pad.view;

import android.app.Activity;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.window.dialog.SitechProgressDialog;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 项目名称：Sitech
 * 类名称：CommonToast
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：2018/03/12 11:32
 * 修改时间：
 * 备注：
 */
public class CommonProgressDialog {
    private SitechProgressDialog mProgressDialog;
    private HashMap<String, SoftReference<SitechProgressDialog>> dialogHashMap = new HashMap<>();

    private CommonProgressDialog() {
        dialogHashMap = new HashMap<>();
    }

    private static class SingleProgressDialog {
        private static final CommonProgressDialog singleDialog = new CommonProgressDialog();
    }

    public static CommonProgressDialog getInstance() {
        return SingleProgressDialog.singleDialog;
    }

    public void show(Activity context) {
        if (context == null) {
            return;
        }
        show(context, context.getString(R.string.loading_text));
    }

    public synchronized void show(Activity context, String text) {
        show(context, text, false);
    }

    public synchronized void show(Activity context, String text, boolean outCancelable) {
        //判断是否存在在map中
        String activityName = context.getClass().getSimpleName();
//        SitechDevLog.e("ProgressDialog", "show Dialog===>" + activityName);
        if (dialogHashMap.containsKey(activityName)) {
            //已包含该Activity
            SoftReference<SitechProgressDialog> progressDialogSoftReference = dialogHashMap.get(activityName);
            if (progressDialogSoftReference != null && progressDialogSoftReference.get() != null) {
                mProgressDialog = progressDialogSoftReference.get();
            }
        } else {
            //未包含在Map中
            mProgressDialog = new SitechProgressDialog(context);
            mProgressDialog.setOnCancelDialogListener(new BaseBribery() {
                @Override
                public void onSuccess(Object successObj) {
//                    SitechDevLog.e("ProgressDialog", "show Dialog===>" + context.getClass().getSimpleName());
                    cancel(context);
                }
            });

            //put 进 map中
            SoftReference<SitechProgressDialog> progressDialogSoftReference = new SoftReference<>(mProgressDialog);
            dialogHashMap.put(activityName, progressDialogSoftReference);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public synchronized void cancel(Activity context) {//判断是否存在在map中
        String activityName = context.getClass().getSimpleName();
//        SitechDevLog.e("ProgressDialog", "cancelDialog===>" + activityName);
        if (dialogHashMap.containsKey(activityName)) {
            //已包含该Activity
            SoftReference<SitechProgressDialog> progressDialogSoftReference = dialogHashMap.get(activityName);
            if (progressDialogSoftReference != null && progressDialogSoftReference.get() != null) {
                mProgressDialog = progressDialogSoftReference.get();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                    mProgressDialog = null;
                }
                //移除该类
                dialogHashMap.remove(activityName);
            }
        }
    }

}
