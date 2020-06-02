package com.sitechdev.vehicle.pad.window.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.base.BasicWindow;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.SSOEvent;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginIntent;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.util.CreateCodeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PersonLoginWindow extends BasicWindow implements View.OnClickListener {

    private final String TAG = PersonLoginWindow.class.getSimpleName();
    private ImageButton login_back_ibtn;
    private ImageView iv_code;
    private RelativeLayout rl_load;
    private LinearLayout ll_count;
    private ConstraintLayout mLoginContainer;
    private TextView mScanTipView;
    private String mLoginCode;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                LoginUtils.loopLoginStatus(mLoginCode, new BaseBribery() {

                    /**
                     * 成功的响应
                     *
                     * @param successObj 响应数据
                     */
                    @Override
                    public void onSuccess(Object successObj) {
                        SitechDevLog.i(TAG, "扫描登录成功，关闭");
                        handler.removeMessages(0);
                    }

                    @Override
                    public void onFailure(Object failObj) {
                        handler.postDelayed(() -> handler.sendEmptyMessage(0), 1000);
                    }
                });
            } else {

            }
        }
    };

    private PersonLoginWindow() {
    }

    private static PersonLoginWindow mInstance = null;

    public static PersonLoginWindow getInstance() {
        if (mInstance == null) {
            synchronized (PersonLoginWindow.class) {
                if (mInstance == null) {
                    mInstance = new PersonLoginWindow();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        mLayoutParams.dimAmount = 0.5f;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
    }

    @Override
    public int getLayoutId() {
        return R.layout.window_person_login;
    }

    @Override
    public void findView() {
        login_back_ibtn = mView.findViewById(R.id.login_back_ibtn);
        iv_code = mView.findViewById(R.id.iv_code);
        rl_load = mView.findViewById(R.id.rl_load);
        ll_count = mView.findViewById(R.id.ll_count);
        mLoginContainer = mView.findViewById(R.id.cl_person_login_container);
        mScanTipView = mView.findViewById(R.id.tv_login_scan);
        mScanTipView.setTextSize(30);
        rl_load.setVisibility(View.GONE);
        ll_count.setVisibility(View.GONE);
        login_back_ibtn.setVisibility(View.GONE);
    }

    @Override
    public void showWnd() {
        super.showWnd();
        EventBusUtils.register(mInstance);
        showQrCode();
    }

    /**
     * 带返回处理的登录回调
     *
     * @param onLoginIntent onLoginIntent
     */
    public void showWnd(LoginIntent.onLoginIntent onLoginIntent) {
        LoginIntent.getInstance().setOnLoginIntent(onLoginIntent);
        showWnd();
    }

    private void showQrCode() {
//        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_LOGIN_WINDOW));
        rl_load.setVisibility(View.GONE);
        mLoginCode = LoginUtils.generateQrMessage();
        Bitmap logo = BitmapFactory.decodeResource(AppApplication.getContext().getResources(), R.drawable.xintelogo);
        Bitmap bitmap = CreateCodeUtil.createQRImage(mLoginCode, 256, 256, logo);
        iv_code.setImageBitmap(bitmap);
        //重置标志位
        LoginUtils.resetLoginUtil();
        if (null != handler) {
            handler.postDelayed(() -> handler.sendEmptyMessage(0), 2000);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        login_back_ibtn.setOnClickListener(this);
        rl_load.setOnClickListener(this);
        mLoginContainer.setOnClickListener(this);
    }

    @Override
    public void hideWnd() {
        super.hideWnd();
        handler.removeMessages(0);
        EventBusUtils.unregister(mInstance);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back_ibtn:
                hideWnd();
                break;
            case R.id.rl_load:
                showQrCode();
                iv_code.setAlpha((float) 1);
                break;
            case R.id.cl_person_login_container:
                LoginIntent.getInstance().clearCache();
                hideWnd();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLoginStatus(SSOEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==SSOEvent 消息==" + event.eventKey);
        switch (event.eventKey) {
            case SSOEvent.EB_MSG_LOGIN:
                SitechDevLog.d(TAG, "login success!!!");
                LoginUserBean loginBean = (LoginUserBean) event.mValue;
                if (null != loginBean) {
                    hideWnd();
                }
                break;
            default:
                break;
        }
    }
}