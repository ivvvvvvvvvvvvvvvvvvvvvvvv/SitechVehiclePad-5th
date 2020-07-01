package com.sitechdev.vehicle.pad.module.phone;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.base.BaseApp;
import com.sitechdev.vehicle.lib.util.ForbidClickEnable;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.module.phone.presenter.PhoneCallPresenter;
import com.sitechdev.vehicle.pad.view.ScrollTextView;


/**
 * 通话window
 *
 * @author hazens
 */
public class PhoneCallWindow implements PhoneCallPresenter.UICallback, View.OnClickListener {

    public static final String TAG = PhoneCallWindow.class.getSimpleName();
    private final static int[] timeFmtMMSS = new int[]{
            R.string.text_time_elapsed1, R.string.mini_time_elapsed1
    };
    private final static int[] timeFmtHHMMSS = new int[]{
            R.string.text_time_elapsed2, R.string.mini_time_elapsed2
    };

    private static PhoneCallWindow instance = null;
    private PhoneCallPresenter presenter;
    private WindowManager windowManager;
    private WindowManager.LayoutParams phoneWindowParams;

    private View phoneView;
    private ConstraintLayout areaPhoneCall;
    private ScrollTextView nameTxt, numberTxt;
    private TextView stateTxt;
    private View[] phoneViewGroup;

    private LinearLayout areaNumPad;
    private TextView tvDtmfNumber;
    private View[] padViewGroup;
    /**
     * key private group id is 5
     */
    private static final int KEY_PRIVATE = 5;
    /**
     * key micmute group id is 4
     */
    private static final int KEY_MICMUTE = 4;
    private static final int MSG_START_SHOW_TIME = 0;
    private ImageButton mPriBtn;
    private ImageButton mMuteBtn;
    private ImageButton mHangupIbt;
    private ImageButton mAnswerIbt;
    private Handler mHandler;
    private int mTime;

    public static PhoneCallWindow getInstance() {
        if (instance == null) {
            synchronized (PhoneCallWindow.class) {
                if (instance == null) {
                    instance = new PhoneCallWindow();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        Context appContext = context.getApplicationContext();
        this.windowManager = BaseWindow.getInstance().getWinManager();
        initWindowParams();
        createPhoneCallWindow(appContext);
        PhoneCallPresenter.initialize(appContext);
        presenter = PhoneCallPresenter.getInstance();
        presenter.registerPresenter(this);
    }

    private PhoneCallWindow() {
    }


    private void initWindowParams() {
        phoneWindowParams = new WindowManager.LayoutParams();
        phoneWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        phoneWindowParams.format = PixelFormat.RGBA_8888;
        phoneWindowParams.width = 510;
        phoneWindowParams.height = 590;
        phoneWindowParams.x = 50;
        phoneWindowParams.windowAnimations = R.style.phoneCallAnimStyle;
        phoneWindowParams.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
        phoneWindowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        phoneWindowParams.dimAmount = (float) 0.8;
    }

    private void createPhoneCallWindow(Context ctx) {
        phoneView = View.inflate(ctx, R.layout.window_phone_call, null);
        areaPhoneCall = phoneView.findViewById(R.id.phone_call_layout);
        nameTxt = phoneView.findViewById(R.id.tv_converse_name01);
        numberTxt = phoneView.findViewById(R.id.tv_converse_number01);
        stateTxt = phoneView.findViewById(R.id.tv_converse_state01);
        mAnswerIbt = phoneView.findViewById(R.id.btn_phone_answer);
        mHangupIbt = phoneView.findViewById(R.id.btn_hangup);
        mMuteBtn = phoneView.findViewById(R.id.btn_phone_silence);
        mPriBtn = phoneView.findViewById(R.id.btn_phone_secret);

        for (View v : phoneViewGroup = new View[]{
                phoneView.findViewById(R.id.btn_phone_answer_layout),
                mHangupIbt,
                mMuteBtn,
                mPriBtn,
                phoneView.findViewById(R.id.btn_numpad)}) {
            v.setOnClickListener(this);
        }
        areaNumPad = phoneView.findViewById(R.id.area_numpad);
        tvDtmfNumber = phoneView.findViewById(R.id.tv_dtmf_number);
        for (View v : padViewGroup = new View[]{
                phoneView.findViewById(R.id.numpad_key_one),
                phoneView.findViewById(R.id.numpad_key_two),
                phoneView.findViewById(R.id.numpad_key_three),
                phoneView.findViewById(R.id.numpad_key_four),
                phoneView.findViewById(R.id.numpad_key_five),
                phoneView.findViewById(R.id.numpad_key_six),
                phoneView.findViewById(R.id.numpad_key_seven),
                phoneView.findViewById(R.id.numpad_key_eight),
                phoneView.findViewById(R.id.numpad_key_nine),
                phoneView.findViewById(R.id.numpad_key_zero),
                phoneView.findViewById(R.id.numpad_key_star),
                phoneView.findViewById(R.id.numpad_key_hash),
                phoneView.findViewById(R.id.numpad_key_hangup),
                phoneView.findViewById(R.id.btn_hide_numpad)}) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void showPhoneCall() {
        areaPhoneCall.setVisibility(View.VISIBLE);
        areaNumPad.setVisibility(View.GONE);
        showPhoneCallWindow(true);
    }

    @Override
    public void onPhoneNumber(String number, int whichSide) {
        logTest("onPhoneNumber-----number:"+number+" whichSide:"+whichSide);
        numberTxt.setText(number);
    }

    @Override
    public void onPhoneName(String name, int whichSide) {
        logTest("onPhoneName-----name:"+name);
        nameTxt.setText(name);
    }

    @Override
    public void setElapsedTime(int seconds, int which) {
        if(null != mHandler){
            mHandler.postDelayed(mTimeTask,500);
        }
    }

    private Runnable mTimeTask = new Runnable() {
        @Override
        public void run() {
            mTime++;
            if(null != mHandler){
                mHandler.sendEmptyMessage(MSG_START_SHOW_TIME);
                mHandler.postDelayed(this,1000);
            }
        }
    };

    private void showTime() {

    }

    @Override
    public void setConverseState(int state, int whichSide) {
        String stateText = null;
        switch (state) {
            case PhoneCallPresenter.STATE_INCOMING:
                stateText = StringUtils.getString(R.string.text_incoming);
                break;
            case PhoneCallPresenter.STATE_DIALING:
                stateText = StringUtils.getString(R.string.text_dialing);
                break;
            default:
                break;
        }
        stateTxt.setText(stateText);
    }

    @Override
    public void onFunctionKeyActivated(int which, boolean bActivated) {
        switch (which) {
            case KEY_PRIVATE:
                if (bActivated) {
                    mPriBtn.setSelected(true);
                } else {
                    mPriBtn.setSelected(false);
                }
                break;
            case KEY_MICMUTE:
                if (bActivated) {
                    mMuteBtn.setSelected(true);
                } else {
                    mMuteBtn.setSelected(false);
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void setButtonGroup(int group) {
        switch (group) {
            case PhoneCallPresenter.GROUP_DIAL:
                phoneViewGroup[0].setVisibility(View.GONE);
                phoneViewGroup[1].setVisibility(View.VISIBLE);
                phoneViewGroup[2].setVisibility(View.GONE);
                phoneViewGroup[3].setVisibility(View.GONE);
                phoneViewGroup[4].setVisibility(View.GONE);
                break;
            case PhoneCallPresenter.GROUP_INCOMING:
                phoneViewGroup[0].setVisibility(View.VISIBLE);
                phoneViewGroup[1].setVisibility(View.VISIBLE);
                phoneViewGroup[2].setVisibility(View.GONE);
                phoneViewGroup[3].setVisibility(View.GONE);
                phoneViewGroup[4].setVisibility(View.GONE);
                break;
            case PhoneCallPresenter.GROUP_ACTIVE:
                phoneViewGroup[0].setVisibility(View.GONE);
                phoneViewGroup[1].setVisibility(View.VISIBLE);
                phoneViewGroup[2].setVisibility(View.VISIBLE);
                phoneViewGroup[3].setVisibility(View.VISIBLE);
                phoneViewGroup[4].setVisibility(View.VISIBLE);
                break;
            case PhoneCallPresenter.GROUP_WAITING:
                break;
            case PhoneCallPresenter.GROUP_TRI:
                break;
            default:
                break;
        }
    }

    @Override
    public void hideWindow() {
        showPhoneCallWindow(false);
        numberTxt.setText(null);
        nameTxt.setText(null);
        areaNumPad.setVisibility(View.GONE);
        areaPhoneCall.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDtmfNumber(String num) {
        tvDtmfNumber.setText(num);
    }

    @Override
    public void showFastCall(String name, String number) {

    }

    @Override
    public void hideFastCall() {

    }

    @Override
    public void onStatusBarUpdate(int which) {

    }

    @Override
    public void hideVolume() {

    }

    @Override
    public void onAppStateChange() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_phone_answer) {
            presenter.answer();

        } else if (i == R.id.btn_hangup || i == R.id.numpad_key_hangup) {
            presenter.hangup();
        } else if (i == R.id.btn_phone_silence) {
            if (ForbidClickEnable.isForbidClick(1000)) {
                return;
            }
            if (BtGlobalRef.isAudioConnected) {
                presenter.micMute();
            }
        } else if (i == R.id.btn_phone_secret) {
            if (ForbidClickEnable.isForbidClick(1000)) {
                return;
            }
            if (!BtGlobalRef.isMicMute) {
                presenter.adtSwitch();
            }
        } else if (i == R.id.btn_numpad) {
            areaNumPad.setVisibility(View.VISIBLE);
            areaPhoneCall.setVisibility(View.GONE);
        } else if (i == R.id.btn_hide_numpad) {
            areaNumPad.setVisibility(View.GONE);
            areaPhoneCall.setVisibility(View.VISIBLE);
        } else if (i == R.id.numpad_key_one) {
            presenter.sendDtmf(BtConstants.DP_ONE);
        } else if (i == R.id.numpad_key_two) {
            presenter.sendDtmf(BtConstants.DP_TWO);
        } else if (i == R.id.numpad_key_three) {
            presenter.sendDtmf(BtConstants.DP_THREE);
        } else if (i == R.id.numpad_key_four) {
            presenter.sendDtmf(BtConstants.DP_FOUR);
        } else if (i == R.id.numpad_key_five) {
            presenter.sendDtmf(BtConstants.DP_FIVE);
        } else if (i == R.id.numpad_key_six) {
            presenter.sendDtmf(BtConstants.DP_SIX);
        } else if (i == R.id.numpad_key_seven) {
            presenter.sendDtmf(BtConstants.DP_SEVEN);
        } else if (i == R.id.numpad_key_eight) {
            presenter.sendDtmf(BtConstants.DP_EIGHT);
        } else if (i == R.id.numpad_key_nine) {
            presenter.sendDtmf(BtConstants.DP_NINE);
        } else if (i == R.id.numpad_key_zero) {
            presenter.sendDtmf(BtConstants.DP_ZERO);
        } else if (i == R.id.numpad_key_star) {
            presenter.sendDtmf(BtConstants.DP_STAR);
        } else if (i == R.id.numpad_key_hash) {
            presenter.sendDtmf(BtConstants.DP_HASH);
        }
    }

    private void showPhoneCallWindow(boolean bShow) {
        logTest("showPhoneCallWindow-----bShow:" + bShow);
        if (bShow) {
            mPriBtn.setSelected(BtGlobalRef.isAudioConnected ? false : true);
            mMuteBtn.setSelected(BtGlobalRef.isMicMute ? true : false);
            if (null != phoneView && phoneView.getParent() == null && !phoneView.isShown()) {
                phoneView.setBackgroundResource(R.drawable.shape_phone_call);
                windowManager.addView(phoneView, phoneWindowParams);
            }
            if(null == mHandler){
                mTime = 0;
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case MSG_START_SHOW_TIME:{
                                stateTxt.setText(getFormattedTime(mTime, 0));
                            }
                            break;
                        }
                    }
                };
            }
        } else {
            if (phoneView.isShown()) {
                windowManager.removeViewImmediate(phoneView);
                phoneView.setBackground(null);
                if (BtGlobalRef.isMicMute) {
                    presenter.micMute();
                }
            }
            if(null != mHandler){
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
        }
    }

    private void logTest(String s) {
        Log.e("Test_Phone_Window", "-----" + s);
    }

    public boolean isShow() {
        if (phoneView != null && phoneView.isShown()) {
            return true;
        } else {
            return false;
        }
    }

    private String getFormattedTime(int seconds, int idx) {
        int hours = seconds / 3600;
        if (hours == 0) {
            return BaseApp.getApp().getString(timeFmtMMSS[idx], (seconds / 60) % 60, seconds % 60);
        }
        return BaseApp.getApp().getString(timeFmtHHMMSS[idx], hours, (seconds / 60) % 60,
                seconds % 60);
    }
}