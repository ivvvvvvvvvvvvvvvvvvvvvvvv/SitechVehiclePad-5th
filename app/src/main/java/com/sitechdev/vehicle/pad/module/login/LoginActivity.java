package com.sitechdev.vehicle.pad.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sitechdev.net.GsonUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.bean.BaseResponseBean;
import com.sitechdev.vehicle.pad.bean.RequestValideCodeBean;
import com.sitechdev.vehicle.pad.bean.UserBean;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginHttpUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：LoginActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 19:20
 * 修改时间：
 * 备注：
 */
public class LoginActivity extends BaseActivity {

    EditText mPhoneEditView = null, mValidEditView = null;
    TextView mValidBtn = null;
    Button mLoginBtn = null;
    private CountDownTimer mCountDownTimer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolBarView.setMainTitle(R.string.string_page_login_title);
        mToolBarView.setLeftImageClickListener(v -> finish());

        mPhoneEditView = findViewById(R.id.id_et_login_phone);
        mValidEditView = findViewById(R.id.id_login_valid_code);
        mValidBtn = findViewById(R.id.id_login_get_valid_code);
        mLoginBtn = findViewById(R.id.id_login_btn);
    }

    @Override
    protected void initData() {
        mValidBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        mPhoneEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!checkPhoneNumberOk(s)) {
                    return;
                }
                refreshValidBtnView(true);
            }
        });
    }

    /**
     * 检查手机号是否ok
     *
     * @param s
     * @return
     */
    private boolean checkPhoneNumberOk(Editable s) {
        if (s == null) {
            refreshValidBtnView(false);
            return false;
        }
        String inputNumber = s.toString();
        if (StringUtils.isEmpty(inputNumber)
                || !inputNumber.startsWith("1")
                || inputNumber.length() != 11) {
            refreshValidBtnView(false);
            //不是手机号码
            return false;
        }
        return true;
    }

    /**
     * 检查验证码是否ok
     *
     * @param s
     * @return
     */
    private boolean checkValidNumberOk(Editable s) {
        if (s == null) {
            return false;
        }
        String inputNumber = s.toString();
        if (StringUtils.isEmpty(inputNumber)
                || inputNumber.length() != 6) {
            //不是手机号码
            return false;
        }
        return true;
    }


    /**
     * 是否激活获取验证码按钮
     *
     * @param enable true=激活
     */
    private void refreshValidBtnView(boolean enable) {
        mValidBtn.setEnabled(enable);
        mValidBtn.setClickable(enable);
        mValidBtn.setBackgroundResource(enable ? R.drawable.ico_login_bt_get_valid_enable : R.drawable.ico_login_bt_get_valid_disable);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_login_get_valid_code:
                if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
                    CommonToast.showToast(getString(R.string.tip_no_net));
                    return;
                }
                showProgressDialog();
                LoginHttpUtil.requestLoginPhoneValid(mPhoneEditView.getEditableText().toString(), new BaseBribery() {
                    @Override
                    public void onSuccess(Object successObj) {
                        SitechDevLog.i(AppConst.TAG, "请求成功   onSuccess====>" + successObj);
                        ThreadUtils.runOnUIThread(() -> {
                            cancelProgressDialog();
                            RequestValideCodeBean validCodeBean = (RequestValideCodeBean) successObj;
                            CommonToast.makeText(LoginActivity.this, validCodeBean.getMessage());
                            startRegetValidCountDown();
                        });
                    }

                    @Override
                    public void onFailure(Object failObj) {
                        super.onFailure(failObj);
                        SitechDevLog.i(AppConst.TAG, "请求失败   onFailure====>" + failObj);
                        ThreadUtils.runOnUIThread(() -> {
                            cancelProgressDialog();
                            CommonToast.makeText(LoginActivity.this, "请求失败，请重试");
                        });
                    }
                });
                break;
            case R.id.id_login_btn:
                if (!checkInputIsOk()) {
                    return;
                }
                showProgressDialog();
                LoginHttpUtil.requestPhoneValidLogin(mPhoneEditView.getEditableText().toString(),
                        mValidEditView.getEditableText().toString(),
                        "",
                        new BaseBribery() {
                            @Override
                            public void onSuccess(Object successObj) {
                                SitechDevLog.i(AppConst.TAG, "请求成功   onSuccess====>" + successObj);
                                ThreadUtils.runOnUIThread(() -> {
                                    cancelProgressDialog();
                                    UserManager.getInstance().saveUserInfo((LoginUserBean) successObj);
                                    //发出登录成功事件
                                    EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_LOGIN_SUCCESS));
                                    //结束
                                    finish();
                                });
                            }

                            @Override
                            public void onFailure(Object failObj) {
                                super.onFailure(failObj);
                                SitechDevLog.i(AppConst.TAG, "请求失败   onFailure====>" + failObj);
                                ThreadUtils.runOnUIThread(() -> {
                                    cancelProgressDialog();
//                                    CommonToast.makeText(LoginActivity.this, ((BaseResponseBean) failObj).getMessage());
                                    if (failObj == null) {
                                        ThreadUtils.runOnUIThread(() -> {
                                            CommonToast.makeText(LoginActivity.this, "请求失败，请重试");
                                        });
                                    } else {
                                        Response response = (Response) failObj;
                                        String bodyMessage = null;
                                        try {
                                            bodyMessage = response.body().string();
                                            if (TextUtils.isEmpty(bodyMessage)) {
                                                ThreadUtils.runOnUIThread(() -> {
                                                    CommonToast.makeText(LoginActivity.this, "请求失败，请重试");
                                                });
                                                return;
                                            }
                                            BaseResponseBean responseBean = GsonUtils.jsonToBean(bodyMessage, BaseResponseBean.class);
                                            ThreadUtils.runOnUIThread(() -> {
                                                CommonToast.makeText(LoginActivity.this, responseBean.getMessage());
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ThreadUtils.runOnUIThread(() -> {
                                                CommonToast.makeText(LoginActivity.this, "请求失败，请重试");
                                            });
                                        }
                                    }
                                });
                            }
                        });
                break;
            default:
                break;
        }
    }

    /**
     * 检查输入项是否OK
     */
    private boolean checkInputIsOk() {
        if (!checkPhoneNumberOk(mPhoneEditView.getEditableText())) {
            CommonToast.makeText(LoginActivity.this, "请输入有效的手机号码");
            return false;
        }
        if (!checkValidNumberOk(mValidEditView.getEditableText())) {
            CommonToast.makeText(LoginActivity.this, "请输入有效的验证码");
            return false;
        }
        return true;
    }

    /**
     * 开始获取验证码倒计时
     */
    private void startRegetValidCountDown() {
        cancelCountDownTimer();
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //获取按钮禁止点击，变为倒计时文字
                mValidBtn.setClickable(false);
                SitechDevLog.i("onTick", "===>" + millisUntilFinished);
                mValidBtn.setText(getString(R.string.string_page_login_reget_valid, LoginHttpUtil.getFormatCountTimeResult(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                mValidBtn.setClickable(true);
                cancelCountDownTimer();
                mValidBtn.setText("重新获取");
            }
        };
        mCountDownTimer.start();
    }

    private void cancelCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountDownTimer();
    }
}
