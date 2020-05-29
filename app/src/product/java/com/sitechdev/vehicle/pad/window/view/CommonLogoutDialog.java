package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseDialog;
import com.sitechdev.vehicle.pad.event.WindowEvent;


/**
 * 公共dialog
 * 单按钮和双按钮样式
 *
 * @author liuhe
 * @date 2018/07/30
 */
public class CommonLogoutDialog extends BaseDialog {

    private TextView mDialogMessageTv;
    private TextView mDialogTitleTv;
    private TextView mBtnCancelTv, mBtnOkTv;
    private OnDialogListener mDialogListener;
    private onDialogCancelListener mDialogCancelListener;

    public CommonLogoutDialog(Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public int getDialogContentView() {
        return R.layout.custom_dialog_logout;
    }

    @Override
    public void initView() {
        mDialogMessageTv = findViewById(R.id.tv_dialog_content);
        mBtnCancelTv = findViewById(R.id.id_tv_cancel);
        mBtnOkTv = findViewById(R.id.id_tv_ok);
        mDialogTitleTv = findViewById(R.id.tv_dialog_title);
    }

    @Override
    public void initListener() {
        mBtnCancelTv.setOnClickListener(this);
        mBtnOkTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    public void setDialogMessage(String message){
        if (!StringUtils.isEmpty(message)) {
            mDialogMessageTv.setVisibility(View.VISIBLE);
            mDialogMessageTv.setText(message);
        } else {
            mDialogMessageTv.setVisibility(View.GONE);
        }
    }

    public void setDialogTitle(String title){
        mDialogTitleTv.setText(StringUtils.isEmpty(title) ? "提示" : title);
    }

    public void setListener(OnDialogListener listener) {
        this.mDialogListener = listener;
    }

    public void setCancelListener(onDialogCancelListener listener) {
        this.mDialogCancelListener = listener;
    }

    public interface OnDialogListener {
        void onPositiveListener();
    }

    public interface onDialogCancelListener {
        void onNegativeListener();
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
            case R.id.id_tv_cancel:
                cancel();
                if (null != mDialogCancelListener) {
                    mDialogCancelListener.onNegativeListener();
                }
                break;
            default:
                break;
        }
    }
}
