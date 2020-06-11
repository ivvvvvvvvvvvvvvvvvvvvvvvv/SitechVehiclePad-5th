package com.sitechdev.vehicle.pad.module.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.view.loading.LoadingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SettingVoiceDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private Context context;
    private Button cancelBtn, confirmBtn;
    private EditText voiceNameEdit;
    private TextView tvTitleName;
    private InputFilter filter, filter1;
    private ImageView ivTextClear;
    private ImageView ivTeskAsk;
    private LoadingView mLoadingView;
    /**
     * 用来标识用户是否点击了语音输入
     */
    public static boolean isAskTeddyIcon = false;

    public SettingVoiceDialog(@NonNull Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
    }


    public SettingVoiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        setContentView(R.layout.dialog_setting_voice);
        initView();
        initLisener();
    }

    private void initView() {
        tvTitleName = mDialogView.findViewById(R.id.setting_dialog_name);
        ivTextClear = mDialogView.findViewById(R.id.iv_dialog_setting_voice_clear);
        ivTeskAsk = mDialogView.findViewById(R.id.iv_dialog_setting_voice_ask);
        mLoadingView = mDialogView.findViewById(R.id.ll_dialog_loading);

        cancelBtn = mDialogView.findViewById(R.id.setting_dialog_cancel_btn);
        confirmBtn = mDialogView.findViewById(R.id.setting_dialog_sure_btn);
        confirmBtn.setAlpha(0.7f);

        voiceNameEdit = mDialogView.findViewById(R.id.dialog_setting_voice_edt);

        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                //若是非中文或中文标点，返回空，起到拦截的作用
                for (int i = start; i < end; i++) {
                    if (!isChinese(source.charAt(i)) || isChinesePunctuation(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        filter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                //若是非中文，返回空，起到拦截的作用
                for (int i = start; i < end; i++) {
                    if (!isChinese(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    private Dialog_Type type;

    public enum Dialog_Type {
        //唤醒词
        type_wakeup,
        //开机问候语
        type_welcome
    }

    /**
     * 设置对话框类型
     */
    public void setDialogType(Dialog_Type type) {
        this.type = type;
        changeView(type);
    }

    private void changeView(Dialog_Type type) {
        switch (type) {
            case type_wakeup:
                setTitleName("修改语音唤醒词（限4个汉字）");
                if (voiceNameEdit != null) {
                    voiceNameEdit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
                }
//                setEditTextDefaultText(TeddyMain.getInstance().getMVWKeywords(false));
                break;
            case type_welcome:
                setTitleName("自定义问候语（限15个汉字）");
                if (voiceNameEdit != null) {
                    voiceNameEdit.setFilters(new InputFilter[]{filter1, new InputFilter.LengthFilter(15)});
                }
//                setEditTextDefaultText(SPUtils.getValue(AppApplication.getContext(), TEDDY_SPKEY_TTS_WELCOME, ""));
                break;
            default:
                break;
        }
    }

    /**
     * 设置对话框标题
     *
     * @param titleName
     */
    public void setTitleName(String titleName) {
        if (!TextUtils.isEmpty(titleName) && tvTitleName != null) {
            tvTitleName.setText(titleName);
        }
    }

    /**
     * 设置输入框中默认展示的文本
     *
     * @param word
     */
    public void setEditTextDefaultText(String word) {
        switch (type) {
            case type_wakeup:
                if (!TextUtils.isEmpty(word)) {
                    voiceNameEdit.setText(word);
                    voiceNameEdit.setSelection(word.length());
                    setConfirmBtnStatus(isChinese(word) && word.length() >= 4);
                }
                break;
            case type_welcome:
                voiceNameEdit.setText(word);
                if (word != null) {
                    voiceNameEdit.setSelection(word.length());
                }
                setConfirmBtnStatus(true);
                break;
            default:
                break;
        }
    }


    /**
     * 只允许输入中文
     *
     * @param c
     * @return
     */
    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是标点符号
     *
     * @param c
     * @return
     */
    public boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    private void initLisener() {
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        ivTextClear.setOnClickListener(this);
        ivTeskAsk.setOnClickListener(this);

        voiceNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setShowClearView(s.length() > 0);
                if (type == Dialog_Type.type_welcome) {
                    setConfirmBtnStatus(s.length() >= 0);
                } else {
                    setConfirmBtnStatus(s.length() >= 4);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_setting_voice_ask:
                EventBusUtils.postEvent(new TeddyEvent(
                        TeddyEvent.EB_TEDDY_EVENT_SETTING_TEDDY_MVW_SUCCESS,
//                        TeddyAnswer.TEDDY_TTS_SETTING_TEDDY_MVW_TEXT));
                isAskTeddyIcon = true));
                break;

            case R.id.iv_dialog_setting_voice_clear:
                voiceNameEdit.setText("");
                if (type == Dialog_Type.type_welcome) {
                    setConfirmBtnStatus(true);
                } else {
                    setConfirmBtnStatus(false);
                }
                break;

            case R.id.setting_dialog_cancel_btn:
                if (callBack != null) {
                    switch (type) {
                        case type_welcome:
                            callBack.onWelcomeCancelListener();
                            break;
                        case type_wakeup:
                            callBack.onWakeupCancelListener();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.setting_dialog_sure_btn:
                if (callBack != null) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    switch (type) {
                        case type_welcome:
                            callBack.onWelcomeOkListener();
                            break;
                        case type_wakeup:
                            callBack.onWakeupOkListener();
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        isAskTeddyIcon = false;
        EventBusUtils.register(this);
    }

    /**
     * 语音输入的文本:只在当前界面拦截输入的语句文本
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedIndex(TeddyEvent event) {
        if (TeddyEvent.EB_TEDDY_EVENT_SETTING_WELCOME.equals(event.getEventKey()) && isAskTeddyIcon) {
            try {
                JSONObject messageObject = (JSONObject) event.getEventValue();
                int type = messageObject.optInt("type");
                String msg = messageObject.optString("text");
                if (type == 0) {
                    //停止语音交互，并将数据展示到界面上
//                    TeddyUtil.startTTSText(isChinese(msg) ? "输入成功" : "输入失败，请说中文", new TeddyEventObject(this::sendSROverEvent));
                    setEditTextDefaultText(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (TeddyEvent.EB_TEDDY_RELEASE_VOICE_FOCUS_OVER.equals(event.getEventKey())) {
            isAskTeddyIcon = false;
        }
    }

    private void sendSROverEvent() {
        EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_SR_OVER));
    }


    @Override
    public void dismiss() {
        super.dismiss();
        isAskTeddyIcon = false;
        if (null != mLoadingView) {
            mLoadingView.setVisibility(View.GONE);
        }
        EventBusUtils.unregister(this);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public interface CallBack {
        //开机问候语
        void onWelcomeOkListener();

        void onWelcomeCancelListener();

        //唤醒词
        void onWakeupOkListener();

        void onWakeupCancelListener();
    }

    public String getWord() {
        return voiceNameEdit.getText() + "";
    }

    /**
     * 字符串是否为中文
     *
     * @param text 字符串
     * @return true=中文
     */
    private boolean isChinese(String text) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher m = p.matcher(text);
        return m.matches();
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

    /**
     * 输入框右侧显示的控件
     *
     * @param isShow true显示叉号， false 显示语音
     */
    private void setShowClearView(boolean isShow) {
        ivTeskAsk.setVisibility(isShow ? View.GONE : View.VISIBLE);
        ivTextClear.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setLoadingViewGone() {
        mLoadingView.setVisibility(View.GONE);
    }
}
