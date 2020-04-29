package com.sitechdev.vehicle.pad.window.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BrightnessUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
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

    private ImageView bluetoothControlView = null, wifiControlView = null, mobileNetControlView = null, ecoView = null, screenOriView = null;

    private VerticalSeekBarForSkin volumeVerticalSeekBar = null, lightVerticalSeekBar = null;
    private SeekBar volumeSeekBar = null, lightSeekBar = null;

    private RadioGroup carSpeedGroupView = null, carRecycleGroupView = null;

    private LinearLayout mLedViewLiearLayoutView = null;

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
    private int maxScreenLightValue = 10, currentScreenLightValue = 0;

    private AudioManager mAudioManager = null;

    public MainControlPanelView(Context context) {
        this(context, null);
    }

    public MainControlPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.custom_control_panel_view, this);
        contentView = findViewById(R.id.id_main_popup_view_content);

        mWidth = contentView.getLayoutParams().width;
        mHeight = contentView.getLayoutParams().height;
        SitechDevLog.i(TAG, "MainPopupControlView==mWidth==" + mWidth + "，mHeight==" + mHeight);

        popControlView = findViewById(R.id.id_top_content);

        manager = MainControlPanelWindowManager.getInstance();

        initView();

//        ThreadUtils.runOnUIThreadDelay(this::initListener, 3000);
        initListener();

//        initVolumeAndLightData();

        initData();

        requestFocus();
        requestFocusFromTouch();
    }

    public View getContentView() {
        return contentView;
    }

    private void initView() {
        bluetoothControlView = findViewById(R.id.id_bluetooth_Btn);
        wifiControlView = findViewById(R.id.id_wifi_Btn);
        mobileNetControlView = findViewById(R.id.id_mobile_net_Btn);
        mobileNetControlView = findViewById(R.id.id_mobile_net_Btn);

        if (ScreenUtils.isLandscape()) {
            //横屏的view
            volumeVerticalSeekBar = findViewById(R.id.id_control_volume_seek_bar);
            lightVerticalSeekBar = findViewById(R.id.id_control_light_seek_bar);
        } else {
            //竖屏的view
            volumeSeekBar = findViewById(R.id.id_control_volume_seek_bar);
            lightSeekBar = findViewById(R.id.id_control_light_seek_bar);
        }

        mLedViewLiearLayoutView = findViewById(R.id.id_led_view_content);

        ecoView = findViewById(R.id.id_eco_content);
        screenOriView = findViewById(R.id.id_screen_ori_content);


        carSpeedGroupView = findViewById(R.id.id_car_speed_switch_group);
        carRecycleGroupView = findViewById(R.id.id_car_power_recycle_group);
    }

    public void initVolumeAndLightData() {
        initVolumeData();
        if (ScreenUtils.isLandscape()) {
            volumeVerticalSeekBar.setMax(maxVolumeValue);
            volumeVerticalSeekBar.setProgress(currentVolumeValue);

            lightVerticalSeekBar.setMax(255);
            lightVerticalSeekBar.setProgress(BrightnessUtils.getWindowBrightness(ActivityUtils.getTopActivity().getWindow()));
        } else {
            volumeSeekBar.setMax(maxVolumeValue);
            volumeSeekBar.setProgress(currentVolumeValue);

            lightSeekBar.setMax(255);
            lightSeekBar.setProgress(BrightnessUtils.getWindowBrightness(ActivityUtils.getTopActivity().getWindow()));
        }
    }

    public void initVolumeData() {
        //音量控制,初始化定义
        mAudioManager = (AudioManager) AppApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolumeValue = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        currentScreenLightValue = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void initListener() {
//        contentView.setOnClickListener(this);
        popControlView.setOnClickListener(this);
        bluetoothControlView.setOnClickListener(this);
        wifiControlView.setOnClickListener(this);
        mobileNetControlView.setOnClickListener(this);

        ecoView.setOnClickListener(this);
        screenOriView.setOnClickListener(this);

        if (ScreenUtils.isLandscape()) {
            //横屏的view
            volumeVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "volumeVerticalSeekBar onProgressChanged==========================================" + progress);
//                    if (progress == seekBar.getMax() || progress == 0) {
//                        seekBar.setThumb(null);
//                    } else {
//                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb_land)));
//                    }
                    setVolumeValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            lightVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "lightVerticalSeekBar onProgressChanged==========================================" + progress);
//                    if (progress == seekBar.getMax() || progress == 0) {
//                        seekBar.setThumb(null);
//                    } else {
//                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb_land)));
//                    }
                    setLightValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } else {
            //竖屏的view
            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SitechDevLog.i(TAG, "volumeSeekBar onProgressChanged==========================================" + progress);
//                    if (progress == seekBar.getMax()) {
//                        seekBar.setThumb(null);
//                    } else {
//                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb)));
//                    }
                    setVolumeValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

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

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        carSpeedGroupView.setOnCheckedChangeListener((group, checkedId) -> {
            int currentCarSpeed = 0;
            switch (checkedId) {
                case R.id.id_car_speed_off:
                    currentCarSpeed = 0;
                    break;
                case R.id.id_car_speed_middle:
                    currentCarSpeed = 1;
                    break;
                case R.id.id_car_speed_max:
                    currentCarSpeed = 2;
                    break;
                default:
                    break;
            }
        });
        carRecycleGroupView.setOnCheckedChangeListener((group, checkedId) -> {
            int currentCarRecycle = 0;
            switch (checkedId) {
                case R.id.id_car_recycle_off:
                    currentCarRecycle = 0;
                    break;
                case R.id.id_car_recycle_middle:
                    currentCarRecycle = 1;
                    break;
                case R.id.id_car_recycle_max:
                    currentCarRecycle = 2;
                    break;
                default:
                    break;
            }
        });
        popControlView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SitechDevLog.i(TAG, "ACTION_DOWN ==========产生了按下DOWN事件==== ");
                        preX = (int) event.getRawX();
                        preY = (int) event.getRawY();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
//                        SitechDevLog.i(TAG, "点击坐标prex==" + preX + "，点击坐标prey==" + preY + "，点击坐标lastX==" + lastX + "点击坐标lastY==" + lastY);
                        if (isInClickView(v, event)) {
                            //点击区域
                            isPullDownView = true;
                        } else {
                            isPullDownView = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        SitechDevLog.i(TAG, "ACTION_MOVE ==========产生了滑动MOVE事件==== ");
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        if (isPullDownView) {
                            if ((y - preY) != 0) {
                                isMove = true;
                            }
//                    SitechDevLog.i(TAG, "ACTION_MOVE (x - preX)==" + (x - preX) + "，点击坐标 (y - preY)==" + (y - preY));
                            manager.move(x - preX, y - preY);
                        }
                        preX = x;
                        preY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView=" + isPullDownView + "  , isMove==" + isMove);
                        if (isPullDownView && isMove) {
                            manager.resetView();
                            isPullDownView = false;
                            isMove = false;
                            SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView return true=");
                        } else {
//                            isMove = false;
                            SitechDevLog.i(TAG, "ACTION_UP ========产生了抬起UP事件=======isPullDownView return false,透传给Onclick=");
                            if (isFullScreen) {
                                manager.mustHiddenView();
                            } else {
                                manager.mustShownView();
                            }
                        }
                        return true;
                    default:
                        break;
                }
                return isPullDownView;
            }
        });
    }

    private void initData() {
        //wifi网络
        wifiControlView.setActivated(NetworkUtils.getWifiEnabled());
        //移动网络
        mobileNetControlView.setActivated(NetworkUtils.getMobileDataEnabled());
        //蓝牙是否开启
        bluetoothControlView.setActivated(BluetoothAdapter.getDefaultAdapter().isEnabled());

        if (mLedViewLiearLayoutView != null) {
            mLedViewLiearLayoutView.removeAllViews();
            for (int i = 0; i < LED_VIEW_RESOURCE_ARRAY.length; i++) {
                ImageView ledImgView = (ImageView) View.inflate(getContext(), R.layout.custom_led_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1.0f;
                ledImgView.setImageResource(LED_VIEW_RESOURCE_ARRAY[i]);
                ledImgView.setTag("led索引id：" + i);
                ledImgView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "被点击的是+=======" + v.getTag());
                        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                        ToastUtils.showShort("点击了" + v.getTag());
                    }
                });
                mLedViewLiearLayoutView.addView(ledImgView, params);
            }
        }
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
            case R.id.id_bluetooth_Btn:
                isActivated = bluetoothControlView.isActivated();
                if (isActivated) {
                    //蓝牙是否开启
                    BluetoothAdapter.getDefaultAdapter().disable();
                } else {
                    //蓝牙是否开启
                    BluetoothAdapter.getDefaultAdapter().enable();
                }
                bluetoothControlView.setActivated(!isActivated);
                break;
            case R.id.id_wifi_Btn:
                isActivated = wifiControlView.isActivated();
                //wifi网络
                NetworkUtils.setWifiEnabled(!isActivated);

                wifiControlView.setActivated(!isActivated);
                break;
            case R.id.id_mobile_net_Btn:
                isActivated = mobileNetControlView.isActivated();
                if (isActivated) {
                    //todo 关闭移动网络
                    //移动网络
//                    mobileNetControlView.setActivated(NetworkUtils.getMobileDataEnabled());
                } else {
                    //todo 打开移动网络
                    //移动网络
//                    mobileNetControlView.setActivated(NetworkUtils.getMobileDataEnabled());
                }
                mobileNetControlView.setActivated(!isActivated);
                break;
            case R.id.id_eco_content:
                isActivated = ecoView.isActivated();
                if (isActivated) {
                    //todo 切换到eco
                } else {
                    //todo 切换到eco+
                }
                ecoView.setActivated(!isActivated);
                break;
            case R.id.id_screen_ori_content:
                isActivated = screenOriView.isActivated();
                if (isActivated) {
                    //todo 切换到横屏
                } else {
                    //todo 切换到竖屏
                }
                screenOriView.setActivated(!isActivated);
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                preX = (int) event.getRawX();
//                preY = (int) event.getRawY();
//                lastX = (int) event.getRawX();
//                lastY = (int) event.getRawY();
//                SitechDevLog.i(TAG, "点击坐标prex==" + preX + "，点击坐标prey==" + preY + "，点击坐标lastX==" + lastX + "点击坐标lastY==" + lastY);
//                if (isInClickView(popControlView, event)) {
//                    //点击区域
//                    isPullDownView = true;
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                x = (int) event.getRawX();
//                y = (int) event.getRawY();
//                if (isPullDownView) {
////                    SitechDevLog.i(TAG, "ACTION_MOVE (x - preX)==" + (x - preX) + "，点击坐标 (y - preY)==" + (y - preY));
//                    manager.move(x - preX, y - preY);
//                }
//                preX = x;
//                preY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                SitechDevLog.i(TAG, "ACTION_UP ==============");
//                if (isPullDownView) {
//                    manager.resetView();
//                }
//                isPullDownView = false;
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    /**
     * 判断触摸的点是否在view范围内
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

    public void resetViewAlpha(int alphaValue) {
//        SitechDevLog.i(TAG, "*********************重设透明值==" + alphaValue);
        if (alphaValue < 0 || alphaValue > 255) {
            return;
        }
        int alphaValue2 = alphaValue * 9 / 10;
//        contentView.getBackground().setAlpha(alphaValue);
//        SitechDevLog.i(TAG, "*********************重设透明值2222===============" + alphaValue2);
        contentView.setBackgroundColor(Color.argb(alphaValue2, 36, 100, 149));
    }

    private void setVolumeValue(int value) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0); //tempVolume:音量绝对值
    }

    private void setLightValue(int value) {
        BrightnessUtils.setWindowBrightness(ActivityUtils.getTopActivity().getWindow(), value);
    }
}
