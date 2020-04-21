package com.sitechdev.vehicle.pad.view;

import android.app.ProgressDialog;
import android.content.Context;

import com.sitechdev.vehicle.pad.R;

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
    private ProgressDialog mProgressDialog;

    private CommonProgressDialog() {
    }

    private static class SingleProgressDialog {
        private static final CommonProgressDialog singleDialog = new CommonProgressDialog();
    }

    public static CommonProgressDialog getInstance() {
        return SingleProgressDialog.singleDialog;
    }

    public void show(Context context) {
        if (context == null) {
            return;
        }
        show(context, context.getString(R.string.loading_text));
    }

    public synchronized void show(Context context, String text) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(text);
        mProgressDialog.show();
    }
    public synchronized void show(Context context, String text, boolean outCancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCanceledOnTouchOutside(outCancelable);
        mProgressDialog.setMessage(text);
        mProgressDialog.show();
    }

    public synchronized void cancel() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }

}
