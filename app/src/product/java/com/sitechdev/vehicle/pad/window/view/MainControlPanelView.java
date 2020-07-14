package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.manager.ScreenLightControlManager;
import com.sitechdev.vehicle.pad.manager.VolumeControlManager;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConfig;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.view.VerticalSeekBarForSkin;
import com.sitechdev.vehicle.pad.window.manager.MainControlPanelWindowManager;

public class MainControlPanelView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = "MainControlPanel";
    private MainControlPanelWindowManager manager = null;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;  // 快速点击间隔

    private LinearLayout popControlView = null;

    private boolean isPullDownView = false, isPullUp = false, isPushDown = false;

    private View contentView = null;

    private ImageView bluetoothControlView = null, wifiControlView = null, mobileNetControlView = null, mTeddySwitchControlView = null, ecoView = null, screenOriView = null;

    private VerticalSeekBarForSkin volumeVerticalSeekBar = null, lightVerticalSeekBar = null;
    private SeekBar volumeSeekBar = null, lightSeekBar = null;

//    private RadioGroup carSpeedGroupView = null, carRecycleGroupView = null;
//    private RadioButton carSpeedGroup_Off = null, carSpeedGroup_Middle = null, carSpeedGroup_Max = null;
//    private RadioButton carRecycleGroup_Off = null, carRecycleGroup_Middle = null, carRecycleGroup_Max = null;
//
//    private LinearLayout mLedViewLiearLayoutView = null;

    private boolean isFullScreen = false, isMove = false;

    private static final int[] LED_VIEW_RESOURCE_ARRAY = {
            R.drawable.ico_control_led1,
            R.drawable.ico_control_led2,
            R.drawable.ico_control_led3,
            R.drawable.ico_control_led4,
            R.drawable.ico_control_led5,
            R.drawable.ico_control_led6,
            R.drawable.ico_control_led7,
            R.drawable.ico_control_led8,
            R.drawable.ico_control_led9};

    private int maxVolumeValue = 10, currentVolumeValue = 0;
    private int maxScreenLightValue = 255, currentScreenLightValue = 0;

//    private AudioManager mAudioManager = null;

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
    }

    public MainControlPanelView(Context context) {
        this(context, null);
    }

    public MainControlPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        long startTime = System.currentTimeMillis();
//        TouchEffectsFactory.initTouchEffects(this);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.custom_control_panel_view, this);
        contentView = findViewById(R.id.id_main_popup_view_content);

        mWidth = contentView.getLayoutParams().width;
        mHeight = contentView.getLayoutParams().height;
        SitechDevLog.i(TAG, "MainPopupControlView==mWidth==" + mWidth + "，mHeight==" + mHeight);
        //音量控制,初始化定义
//        mAudioManager = (AudioManager) AppApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolumeValue = VolumeControlManager.getInstance().getMaxVolumeValue();// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        SitechDevLog.i(TAG, "最大音量===" + maxVolumeValue);

        popControlView = findViewById(R.id.id_top_content);

        manager = MainControlPanelWindowManager.getInstance();

        initView();

        initListener();

//        initVolumeAndLightData();

        initData();

        requestFocus();
        requestFocusFromTouch();
        long endTime = System.currentTimeMillis();
        SitechDevLog.w("runTime", "Displayed com.sitechdev.vehicle.pad.MainControlPanelView:" + (endTime - startTime));
    }

    public View getContentView() {
        return contentView;
    }

    private void initView() {
        bluetoothControlView = findViewById(R.id.id_bluetooth_Btn);
        wifiControlView = findViewById(R.id.id_wifi_Btn);
        mobileNetControlView = findViewById(R.id.id_mobile_net_Btn);
        mTeddySwitchControlView = findViewById(R.id.id_teddy_Btn);

        if (ScreenUtils.isLandscape()) {
            //横屏的view
            volumeVerticalSeekBar = findViewById(R.id.id_control_volume_seek_bar);
            lightVerticalSeekBar = findViewById(R.id.id_control_light_seek_bar);
        } else {
            //竖屏的view
            volumeSeekBar = findViewById(R.id.id_control_volume_seek_bar);
            lightSeekBar = findViewById(R.id.id_control_light_seek_bar);
        }

//        mLedViewLiearLayoutView = findViewById(R.id.id_led_view_content);

//        ecoView = findViewById(R.id.id_eco_content);
//        screenOriView = findViewById(R.id.id_screen_ori_content);
//
//
//        carSpeedGroupView = findViewById(R.id.id_car_speed_switch_group);
//        carRecycleGroupView = findViewById(R.id.id_car_power_recycle_group);
//
//        carSpeedGroup_Off = findViewById(R.id.id_car_speed_off);
//        carSpeedGroup_Middle = findViewById(R.id.id_car_speed_middle);
//        carSpeedGroup_Max = findViewById(R.id.id_car_speed_max);
//        carRecycleGroup_Off = findViewById(R.id.id_car_recycle_off);
//        carRecycleGroup_Middle = findViewById(R.id.id_car_recycle_middle);
//        carRecycleGroup_Max = findViewById(R.id.id_car_recycle_max);

        initSimStatus();
    }

    public void initVolumeAndLightData() {
        contentView.setOnClickListener(this);

        //当前音量
        currentVolumeValue = VolumeControlManager.getInstance().getCurrentVolumeValue();
        SitechDevLog.i(TAG, "当前音量===" + currentVolumeValue);

        currentScreenLightValue = ScreenLightControlManager.getInstance().getScreenLightValue();
        SitechDevLog.i(TAG, "当前亮度===>" + currentScreenLightValue);

        if (ScreenUtils.isLandscape()) {
            volumeVerticalSeekBar.setProgress(currentVolumeValue);
            lightVerticalSeekBar.setProgress(currentScreenLightValue);
        } else {
            volumeSeekBar.setProgress(currentVolumeValue);
            lightSeekBar.setProgress(currentScreenLightValue);
        }
        //按键监听
        initSeekBarListener();
    }

    private void initListener() {
        popControlView.setOnClickListener(this);
        bluetoothControlView.setOnClickListener(this);
        wifiControlView.setOnClickListener(this);
        mobileNetControlView.setOnClickListener(this);
        mTeddySwitchControlView.setOnClickListener(this);

        wifiControlView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SitechDevLog.i(TAG, "setOnLongClickListener wifiControlView===长按事件被激活,跳转网络设置");
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_NET_PAGE);
                manager.mustHiddenView();
                return true;
            }
        });

        mobileNetControlView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SitechDevLog.i(TAG, "setOnLongClickListener mobileNetControlView===长按事件被激活,跳转网络设置");
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_NET_PAGE);
                manager.mustHiddenView();
                return true;
            }
        });

//        ecoView.setOnClickListener(this);
//        screenOriView.setOnClickListener(this);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SitechDevLog.i(TAG, "setOnTouchListener onTouch===" + event.getAction() + "，v=" + (v.getId() == R.id.id_main_popup_view_content));
                if (v.getId() != R.id.id_main_popup_view_content) {
                    if (isFullScreen) {
                        manager.mustHiddenView();
                    }
                }
                return false;
            }
        });

//        carSpeedGroupView.setOnCheckedChangeListener((group, checkedId) -> {
//            SitechDevLog.i(TAG, "carSpeedGroupView setOnCheckedChangeListener ==========checkedId==== " + checkedId);
//            int currentCarSpeed = 0;
//            switch (checkedId) {
//                case R.id.id_car_speed_off:
//                    currentCarSpeed = 0;
//                    break;
//                case R.id.id_car_speed_middle:
//                    currentCarSpeed = 1;
//                    break;
//                case R.id.id_car_speed_max:
//                    currentCarSpeed = 2;
//                    break;
//                default:
//                    break;
//            }
//        });
//        carRecycleGroupView.setOnCheckedChangeListener((group, checkedId) -> {
//            SitechDevLog.i(TAG, "carRecycleGroupView setOnCheckedChangeListener ==========checkedId==== " + checkedId);
//            int currentCarRecycle = 0;
//            switch (checkedId) {
//                case R.id.id_car_recycle_off:
//                    currentCarRecycle = 0;
//                    break;
//                case R.id.id_car_recycle_middle:
//                    currentCarRecycle = 1;
//                    break;
//                case R.id.id_car_recycle_max:
//                    currentCarRecycle = 2;
//                    break;
//                default:
//                    break;
//            }
//        });

//        carSpeedGroup_Off.setOnClickListener(this);
//        carSpeedGroup_Middle.setOnClickListener(this);
//        carSpeedGroup_Max.setOnClickListener(this);
//        carRecycleGroup_Off.setOnClickListener(this);
//        carRecycleGroup_Middle.setOnClickListener(this);
//        carRecycleGroup_Max.setOnClickListener(this);

        popControlView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SitechDevLog.i(TAG, "ACTION_DOWN ==========产生了按下DOWN事件==== " + v);
                        preX = (int) event.getRawX();
                        preY = (int) event.getRawY();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
//                        SitechDevLog.i(TAG, "点击坐标prex==" + preX + "，点击坐标prey==" + preY + "，点击坐标lastX==" + lastX + "点击坐标lastY==" + lastY);
                        if (isInClickView(v, event)) {
                            SitechDevLog.i(TAG, "ACTION_DOWN ==========按住了 popControlView  ==== ");
                            //点击区域
                            isPullDownView = true;
                        } else {
                            isPullDownView = false;
                        }
                        isPullDownView = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        SitechDevLog.i(TAG, "ACTION_MOVE ==========产生了滑动MOVE事件==== ");
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        if (isPullDownView) {
//                            if ((y - preY) != 0) {
//                                isMove = true;
//                            }
                            if ((x - preX) != 0) {
                                isMove = true;
                            }
                            SitechDevLog.i(TAG, "ACTION_MOVE (x - preX)==" + (x - preX) + "，点击坐标 (y - preY)==" + (y - preY));
                            manager.moveH(x - preX, y - preY);
                        }
                        preX = x;
                        preY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView=" + isPullDownView + "  , isMove==" + isMove);
                        if (isPullDownView && isMove) {
                            manager.resetView(lastX, (int) event.getRawX());
                            isPullDownView = false;
                            isMove = false;
                            SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView return true=");
                        } else {
//                            isMove = false;
//                            SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView return false,透传给Onclick=");
//                            if (isFullScreen) {
//                                manager.mustHiddenView();
//                            } else {
////                                manager.mustShownView();
//                            }
                        }
                        return true;
                    default:
                        break;
                }
                return isPullDownView;
            }
        });
    }

    /**
     * 初始化SeekBar的按键监听事件
     */
    private void initSeekBarListener() {
        if (ScreenUtils.isLandscape()) {
            //横屏的view
            volumeVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "volumeVerticalSeekBar onProgressChanged==========================================" + progress);
                    setVolumeValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "volumeVerticalSeekBar onStartTrackingTouch==========================================");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "volumeVerticalSeekBar onStopTrackingTouch==========================================");

                }
            });

            lightVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "lightVerticalSeekBar onProgressChanged==========================================" + progress);
                    setLightValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "lightVerticalSeekBar onStartTrackingTouch==========================================");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "lightVerticalSeekBar onStopTrackingTouch==========================================");

                }
            });
        } else {
            //竖屏的view
            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "volumeSeekBar onProgressChanged==========================================" + progress);
                    setVolumeValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "volumeSeekBar onStartTrackingTouch==========================================");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "volumeSeekBar onStopTrackingTouch==========================================");

                }
            });

            lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "lightSeekBar onProgressChanged==========================================" + progress);
//                    if (progress == seekBar.getMax()) {
//                        seekBar.setThumb(null);
//                    } else {
//                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb)));
//                    }
                    setLightValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "lightSeekBar onStartTrackingTouch==========================================");

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SitechDevLog.i(TAG, "lightSeekBar onStopTrackingTouch==========================================");

                }
            });
        }
    }

    private void initData() {
        //wifi网络
        refreshWifiSwitchView(NetworkUtils.isWifiConnected());
        //移动网络
        refreshMobileNetSwitchView();
        //使用Teddy开关
        refreshTeddySwitchView();
        //蓝牙是否开启
        refreshBtSwitchView();

        if (ScreenUtils.isLandscape()) {
            volumeVerticalSeekBar.setMax(maxVolumeValue);
            Log.i(TAG, "volumeVerticalSeekBar setMax=======" + maxVolumeValue);

            lightVerticalSeekBar.setMax(maxScreenLightValue);
        } else {
            volumeSeekBar.setMax(maxVolumeValue);
            Log.i(TAG, "volumeSeekBar setMax=======" + maxVolumeValue);

            lightSeekBar.setMax(maxScreenLightValue);
        }

//        if (mLedViewLiearLayoutView != null) {
//            mLedViewLiearLayoutView.removeAllViews();
//            for (int i = 0; i < LED_VIEW_RESOURCE_ARRAY.length; i++) {
//                ImageView ledImgView = (ImageView) View.inflate(getContext(), R.layout.custom_led_view, null);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.weight = 1.0f;
//                params.gravity = Gravity.CENTER_VERTICAL;
//                ledImgView.setImageResource(LED_VIEW_RESOURCE_ARRAY[i]);
//                ledImgView.setTag("led索引id：" + i);
//                ledImgView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i(TAG, "被点击的是+=======" + v.getTag());
//                        ToastUtils.setGravity(Gravity.TOP, 0, 0);
////                        ToastUtils.showShort("点击了" + v.getTag());
//                    }
//                });
//                mLedViewLiearLayoutView.addView(ledImgView, params);
//            }
//        }

        contentView.getBackground().setAlpha(0);
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    @Override
    public void onClick(View v) {
        boolean isActivated = false;
        SitechDevLog.i(TAG, "onClick 被点击的是+=======" + v);
        switch (v.getId()) {
//            case R.id.id_top_content:
//                SitechDevLog.i(TAG, "onClick 被点击的是+====isPullDownView=" + isPullDownView + "  , isMove==" + isMove);
//            case R.id.id_top_image:
//            case R.id.id_main_popup_view_content:
//                SitechDevLog.i(TAG, "onClick 被点击的是+====isFullScreen===" + isFullScreen);
//                if (isFullScreen) {
//                    manager.mustHiddenView();
//                } else {
//                    manager.mustShownView();
//                }
//                break;
//            case R.id.id_main_popup_view_content:
//                if (isFullScreen) {
//                    manager.mustHiddenView();
//                }
//                break;
            case R.id.id_bluetooth_Btn:
                isActivated = bluetoothControlView.isActivated();
                if (isActivated) {
                    //蓝牙是否开启
                    BtManager.getInstance().closeBt();
                } else {
                    //蓝牙是否开启
                    BtManager.getInstance().openBt();
                }
//                bluetoothControlView.setActivated(!isActivated);
                break;
            case R.id.id_wifi_Btn:
                isActivated = wifiControlView.isActivated();
                //wifi网络
                wifiControlView.setActivated(!isActivated);
                NetworkUtils.setWifiEnabled(!isActivated);
                break;
            case R.id.id_mobile_net_Btn:
                isActivated = mobileNetControlView.isActivated();
                mobileNetControlView.setActivated(!isActivated);
                NetworkUtils.setMobileDataEnabled(!isActivated);
//                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_MOBILE_NET_SWITCH_STATE));
                break;
            case R.id.id_teddy_Btn:
                //开启或关闭Teddy语音唤醒
                isActivated = mTeddySwitchControlView.isActivated();
                SitechDevLog.i(TAG, "语音开关状态=========isActivated===" + isActivated);
                mTeddySwitchControlView.setActivated(!isActivated);
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_AUTO_MVW_SWITCH, !isActivated));
                break;
//            case R.id.id_eco_content:
//                isActivated = ecoView.isActivated();
//                if (isActivated) {
//                    //todo 切换到eco
//                } else {
//                    //todo 切换到eco+
//                }
//                ecoView.setActivated(!isActivated);
//                break;
//            case R.id.id_screen_ori_content:
//                isActivated = screenOriView.isActivated();
//                if (isActivated) {
//                    //todo 切换到横屏
//                } else {
//                    //todo 切换到竖屏
//                }
//                screenOriView.setActivated(!isActivated);
//                break;
            default:
                break;
        }
    }

    /**
     * 判断触摸的点是否在view范围内
     *
     * @param v     点击到的view
     * @param event 点击事件
     * @return true= 点击到了v，false=未点击到
     */
    private boolean isInClickView(View v, MotionEvent event) {
//        SitechDevLog.i(TAG, "isInClickView22==========================================");
        int[] l = {0, 0};
        v.getLocationInWindow(l);
        int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
        float eventX = event.getX();
        float eventY = event.getY();
        Rect rect = new Rect(left, top, right, bottom);
//        SitechDevLog.i(TAG, "isInClickView22=eventX=" + eventX + "，isInClickView  eventY==" + eventY);
//        SitechDevLog.i(TAG, "isInClickView22=frame.contains((int) eventX, (int) eventY)=" + (rect.contains((int) eventX, (int) eventY)));
        return rect.contains((int) eventX, (int) eventY);
    }

    /**
     * 重设主背景的透明值
     *
     * @param alphaValue alphaValue透明值。0-255之间
     */
    public void resetViewAlpha(int alphaValue) {
        SitechDevLog.i(TAG, "*********************重设透明值==" + alphaValue);
        if (alphaValue < 0 || alphaValue > 255) {
            return;
        }
        int alphaValue2 = alphaValue * 95 / 100;
        alphaValue2 = alphaValue2 * 2;
        if (alphaValue2 > 255) {
            alphaValue2 = 255 * 95 / 100;
        }
        contentView.getBackground().setAlpha(alphaValue2);
//        SitechDevLog.i(TAG, "*********************重设透明值2222===============" + alphaValue2);
//        contentView.setBackgroundColor(Color.argb(alphaValue2, 24, 50, 63));
    }

    /**
     * 初始化SIM卡状态
     */
    public void initSimStatus() {
        boolean hasSim = NetworkUtils.hasSimCard(AppApplication.getContext());
        SitechDevLog.i(TAG, "当前SIM卡状态===>" + hasSim);
        if (!hasSim) {
            mobileNetControlView.setEnabled(false);
            mobileNetControlView.setActivated(false);
            if (mobileNetControlView.getBackground() != null) {
                mobileNetControlView.getBackground().setAlpha(200);
            }
        } else {
            mobileNetControlView.setEnabled(true);
        }
    }

    /**
     * 设置音量大小
     *
     * @param value 音量大小，seekbar的progress值
     */
    private void setVolumeValue(int value) {
        SitechDevLog.i(TAG, "setVolumeValue===" + value);
        /**
         * flags ：一个或多个标志。可能这里的标志不是很好理解，是这样，AudioManager提供了一些常量，我们可以将这些系统已经准备好的常量设置为这里的flags，比如：
         *
         * FLAG_ALLOW_RINGER_MODES（更改音量时是否包括振铃模式作为可能的选项），
         *
         * FLAG_PLAY_SOUND（是否在改变音量时播放声音），
         *
         * FLAG_REMOVE_SOUND_AND_VIBRATE（删除可能在队列中或正在播放的任何声音/振动（与更改音量有关）），
         *
         * FLAG_SHOW_UI（显示包含当前音量的吐司），
         *
         * FLAG_VIBRATE（是否进入振动振铃模式时是否振动）
         */
//        if (false) {
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_PLAY_SOUND);// | AudioManager.FLAG_SHOW_UI);
//        }
        //设置音量
        VolumeControlManager.getInstance().setCurrentVolume(value);
    }

    /**
     * 设置屏幕亮度
     *
     * @param value 亮度大小，seekbar的progress值
     */
    private void setLightValue(int value) {
        SitechDevLog.i(TAG, "setLightValue===" + value);
        ScreenLightControlManager.getInstance().setScreenLightValue(value);
    }

    /**
     * 刷新蓝牙状态
     */
    public void refreshBtSwitchView() {
        SitechDevLog.i(TAG, "当前蓝牙状态===>" + BtManager.getInstance().isBtEnable());
        bluetoothControlView.setActivated(BtManager.getInstance().isBtEnable());
    }

    /**
     * 刷新wifi网络状态
     */
    public void refreshWifiSwitchView(boolean isConnected) {
        SitechDevLog.i(TAG, "当前wifi网络状态===>" + isConnected);
        wifiControlView.setActivated(isConnected);
    }

    /**
     * 刷新移动网络状态
     */
    public void refreshMobileNetSwitchView() {
        SitechDevLog.i(TAG, "当前移动网络状态===>" + NetworkUtils.getMobileDataEnabled());
        //移动网络
        mobileNetControlView.setActivated(NetworkUtils.getMobileDataEnabled());
    }

    /**
     * 刷新Teddy开关状态
     */
    public void refreshTeddySwitchView() {
        SitechDevLog.i(TAG, "当前Teddy状态===>" + TeddyConfig.getAutoMVWStatus());
        //使用Teddy开关
        mTeddySwitchControlView.setActivated(TeddyConfig.getAutoMVWStatus());
    }
}
