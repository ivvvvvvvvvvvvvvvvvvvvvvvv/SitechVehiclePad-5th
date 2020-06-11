package com.sitechdev.vehicle.pad.module.setting.wifi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.OptAnimationLoader;
import com.sitechdev.vehicle.pad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/3/14.
 */

public class WiFiDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private int dialogType;
    private List<View> viewList;
    private int selectIndex = -1;

    //共有的
    private Button cancel;
    private TextView wifiNameTxt;

    //输入密码界面
    private Button wifiSure;
    private EditText passWord;
    private CheckBox invisiblePassWord;

    //断开、取消保存界面，无密码界面
    private TextView connectStateTxt, rssiTxt;
    private Button interruptBtn, cancelSaveBtn;
    private String ssid, state, rssi;
    private boolean isConnected;
    private Context context;

    public WiFiDialog(Context context, String ssid, int dialogType) {
        this(context);
        this.context = context;
        this.ssid = ssid;
        this.dialogType = dialogType;
    }

    public WiFiDialog(Context context, String ssid, String state, String rssi, boolean isConnected, int dialogType) {
        this(context);
        this.ssid = ssid;
        this.state = state;
        this.rssi = rssi;
        this.isConnected = isConnected;
        this.dialogType = dialogType;
    }

    public WiFiDialog(Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();

        //设置起始坐标
        switch (dialogType) {
            case 1://断开、取消保存界面
            case 2://无密码界面
                break;
            case 3://输入密码界面
                params.gravity = Gravity.TOP;
                params.y = 20;
                getWindow().setAttributes(params);
                break;
        }
    }

    @Override
    protected void onStart() {
        AnimationSet modalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mDialogView.startAnimation(modalInAnim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (dialogType) {
            case 1://断开、取消保存界面
                setContentView(R.layout.dialog_wifi_cancel_save);
                break;
            case 2://无密码界面
                setContentView(R.layout.dialog_wifi_no_password);
                break;
            case 3://输入密码界面
                setContentView(R.layout.dialog_wifi_connect);
                break;
        }
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        initView();
        initState();
        setListener();
    }

    private void initView() {
        viewList = new ArrayList<>();
        switch (dialogType) {
            case 1://断开、取消保存界面
                wifiNameTxt = mDialogView.findViewById(R.id.wifi_dialog_name);
                connectStateTxt = mDialogView.findViewById(R.id.wifi_dialog_connect_state);
                rssiTxt = mDialogView.findViewById(R.id.wifi_dialog_rssi_info);
                interruptBtn = (Button) mDialogView.findViewById(R.id.wifi_dialog_interrupt_or_connect_btn);
                cancelSaveBtn = (Button) mDialogView.findViewById(R.id.wifi_dialog_cancel_save);
                cancel = (Button) mDialogView.findViewById(R.id.wifi_dialog_cancel_btn);
                viewList.add(interruptBtn);
                viewList.add(cancelSaveBtn);
                viewList.add(cancel);
                break;
            case 2://无密码界面
                wifiNameTxt = mDialogView.findViewById(R.id.wifi_dialog_name);
                connectStateTxt = mDialogView.findViewById(R.id.wifi_dialog_connect_state);
                rssiTxt = mDialogView.findViewById(R.id.wifi_dialog_rssi_info);
                interruptBtn = mDialogView.findViewById(R.id.wifi_dialog_interrupt_or_connect_btn);
                cancel = mDialogView.findViewById(R.id.wifi_dialog_cancel_btn);
                viewList.add(interruptBtn);
                viewList.add(cancel);
                break;
            case 3://输入密码界面
                wifiNameTxt = mDialogView.findViewById(R.id.wifi_dialog_name);
                passWord = mDialogView.findViewById(R.id.wifi_dialog_pass_word_box_edt);
                invisiblePassWord = mDialogView.findViewById(R.id.wifi_dialog_invisible);
                wifiSure = mDialogView.findViewById(R.id.wifi_dialog_sure_btn);
                wifiSure.setAlpha((float) 0.5);
                cancel = mDialogView.findViewById(R.id.wifi_dialog_cancel_btn);
                viewList.add(wifiSure);
                viewList.add(cancel);
//                viewList.add(invisiblePassWord);
                break;
        }
    }

    private void initState() {
//        viewList.get(0).setSelected(true);
        switch (dialogType) {
            case 1://断开、取消保存界面
            case 2://无密码界面
                wifiNameTxt.setText(ssid);
                connectStateTxt.setText(state);

                interruptBtn.setText(isConnected ? R.string.wifi_dialog_interrupt : R.string.wifi_dialog_connect);
                break;
            case 3://输入密码界面
                wifiNameTxt.setText(ssid);
                invisiblePassWord.setBackgroundResource(R.drawable.set_wifi_password_visible_n);
                passWord.setBackgroundResource(R.drawable.set_wifi_connected_password_input_box);
                break;
        }
    }

    private void setListener() {

        switch (dialogType) {
            case 1://断开、取消保存界面
                interruptBtn.setOnClickListener(this);
                cancel.setOnClickListener(this);
                cancelSaveBtn.setOnClickListener(this);
                break;
            case 2://无密码界面
                interruptBtn.setOnClickListener(this);
                cancel.setOnClickListener(this);
                break;
            case 3://输入密码界面
                wifiSure.setEnabled(false);
                wifiSure.setOnClickListener(this);
                cancel.setOnClickListener(this);
                invisiblePassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            invisiblePassWord.setBackgroundResource(R.drawable.set_wifi_password_visible_n);
                        } else {
                            passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            invisiblePassWord.setBackgroundResource(R.drawable.set_wifi_password_invisible_n);
                        }

                        passWord.setSelection(passWord.getText().length());
                    }
                });
                passWord.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (passWord.getText().length() < 8) {
                            wifiSure.setEnabled(false);
                            wifiSure.setAlpha((float) 0.5);
                        } else {
                            wifiSure.setEnabled(true);
                            wifiSure.setAlpha(1);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_dialog_sure_btn: {
                if (callBack != null) {
                    callBack.onSureListener();
                }
            }
            break;

            case R.id.wifi_dialog_cancel_btn: {
                if (callBack != null) {
                    callBack.onCancelListener();
                }
            }
            break;

            case R.id.wifi_dialog_interrupt_or_connect_btn: {
                if (callBack != null) {
                    callBack.onInterruptOrConnectListener();
                }
            }
            break;

            case R.id.wifi_dialog_cancel_save: {
                if (callBack != null) {
                    callBack.onCancelSave();
                }
            }
            break;

            default: {
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

        void onCancelSave();

        void onInterruptOrConnectListener();
    }

    public String getPassWord() {
        return passWord.getText() + "";
    }
}
