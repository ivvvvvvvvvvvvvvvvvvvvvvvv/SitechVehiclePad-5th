package com.sitechdev.vehicle.pad.window.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseDialog;


/**
 * 公共dialog
 * 单按钮和双按钮样式
 *
 * @author liuhe
 * @date 2018/07/30
 */
public class SetAddressTipDialog extends BaseDialog {

    private TextView mSignTipTv;
    private View.OnClickListener outClickListener = null;

    public SetAddressTipDialog(Context context) {
        super(context, R.style.set_dialog);
        SitechDevLog.i("SetAddressTipDialog", "SetAddressTipDialog");
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public int getDialogContentView() {
        return R.layout.custom_dialog_set_address;
    }

    @Override
    public void initView() {
        SitechDevLog.i("SetAddressTipDialog", "initView");
        mSignTipTv = findViewById(R.id.tv_sign_tip);
    }

    @Override
    public void initListener() {
        mSignTipTv.setOnClickListener(v -> {
            cancelDialog();
            if (outClickListener != null) {
                outClickListener.onClick(v);
            }
        });
    }

    @Override
    public void initData() {
    }

    public void setDialogCancelListener(View.OnClickListener clickListener) {
        SitechDevLog.i("SetAddressTipDialog", "setDialogCancelListener");
        outClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    public void cancelDialog() {
        cancel();
    }
}
