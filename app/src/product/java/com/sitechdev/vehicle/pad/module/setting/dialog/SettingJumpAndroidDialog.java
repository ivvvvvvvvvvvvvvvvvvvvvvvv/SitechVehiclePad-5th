package com.sitechdev.vehicle.pad.module.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sitechdev.vehicle.pad.R;


/**
 * 项目名称：GA10-C
 * 类名称：JumpAndroidDialog
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/06/10 0010 11:07
 * 修改时间：
 * 备注：
 */
public class SettingJumpAndroidDialog extends Dialog implements View.OnClickListener {
    private View mDialogView;
    private Button cancelBtn, confirmBtn;
    private EditText jumpNameEdit;


    public SettingJumpAndroidDialog(@NonNull Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }


    public SettingJumpAndroidDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        setContentView(R.layout.dialog_setting_jump_android);
        initView();
        initListener();
    }

    private void initView() {
        cancelBtn = mDialogView.findViewById(R.id.setting_dialog_jump_cancel_btn);
        confirmBtn = mDialogView.findViewById(R.id.setting_dialog_jump_sure_btn);
        confirmBtn.setAlpha(0.7f);

        jumpNameEdit = mDialogView.findViewById(R.id.dialog_setting_jump_edt);
    }

    private void initListener() {
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        jumpNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setConfirmBtnStatus(s != null && s.toString().length() > 2);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_dialog_jump_cancel_btn:
                if (callBack != null) {
                    callBack.onCancelListener();
                }
                break;
            case R.id.setting_dialog_jump_sure_btn:
                if (callBack != null) {
                    callBack.onSureListener();
                }
                break;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public interface CallBack {
        void onSureListener();

        void onCancelListener();
    }

    public String getWord() {
        return jumpNameEdit.getText().toString();
    }

    /**
     * 设置确定按钮状态
     *
     * @param isEnable true=启用
     */
    private void setConfirmBtnStatus(boolean isEnable) {
        confirmBtn.setAlpha(isEnable ? 1f : 0.5f);
        confirmBtn.setEnabled(isEnable);
    }
}
