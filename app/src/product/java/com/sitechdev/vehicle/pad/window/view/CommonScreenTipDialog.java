package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseDialog;


/**
 * 公共dialog
 * 横竖屏提示
 */
public class CommonScreenTipDialog extends BaseDialog {

    private TextView mBtnCancelTv, mBtnOkTv;
    private OnDialogListener mDialogListener;

    public CommonScreenTipDialog(Context context) {
        super(context, R.style.set_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public int getDialogContentView() {
        return R.layout.custom_dialog_screen_tip;
    }

    @Override
    public void initView() {
        mBtnOkTv = findViewById(R.id.id_tv_ok);
    }

    @Override
    public void initListener() {
        mBtnOkTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    public void setListener(OnDialogListener listener) {
        this.mDialogListener = listener;
    }

    public interface OnDialogListener {
        void onPositiveListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        switch (v.getId()) {
            case R.id.id_tv_ok:
                cancel();
                if (null != mDialogListener) {
                    mDialogListener.onPositiveListener();
                }
                break;
            default:
                break;
        }
    }
}
