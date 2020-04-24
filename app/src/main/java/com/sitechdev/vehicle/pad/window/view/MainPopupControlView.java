package com.sitechdev.vehicle.pad.window.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.view.VerticalSeekBarForSkin;
import com.sitechdev.vehicle.pad.window.manager.MainPopUpControlWindowManager;

public class MainPopupControlView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = "MainPopUpControl";
    private MainPopUpControlWindowManager manager = null;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;  // 快速点击间隔

    private ImageView popControlView = null;

    private boolean isPullDownView = false, isPullUp = false, isPushDown = false;

    private View contentView = null;

    private ImageView bluetoothControlView = null, wifiControlView = null, mobileNetControlView = null, ecoView = null, screenOriView = null;

    private VerticalSeekBarForSkin volumeVerticalSeekBar = null, lightVerticalSeekBar = null;
    private SeekBar volumeSeekBar = null, lightSeekBar = null;

    private RadioGroup carSpeedGroupView = null, carRecycleGroupView = null;

    private LinearLayout mLedViewLiearLayoutView = null;

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

    public MainPopupControlView(Context context) {
        this(context, null);
    }

    public MainPopupControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.custom_popup_view, this);
        contentView = findViewById(R.id.id_main_popup_view_content);

        mWidth = contentView.getLayoutParams().width;
        mHeight = contentView.getLayoutParams().height;
        SitechDevLog.i(TAG, "MainPopupControlView==mWidth==" + mWidth + "，mHeight==" + mHeight);

        popControlView = findViewById(R.id.id_top_image);
        findViewById(R.id.id_top_content).setOnClickListener(this);
        findViewById(R.id.id_main_popup_view).setOnClickListener(this);
        findViewById(R.id.id_main_popup_view_content).setOnClickListener(this);

        manager = MainPopUpControlWindowManager.getInstance();

        initView();

        initListener();

        initData();

        requestFocus();
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

    private void initListener() {
        findViewById(R.id.id_main_popup_view).setOnClickListener(this);
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
                    if (progress == seekBar.getMax()) {
                        seekBar.setThumb(null);
                    } else {
                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb_land)));
                    }
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
                    if (progress == seekBar.getMax()) {
                        seekBar.setThumb(null);
                    } else {
                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb_land)));
                    }
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
                    if (progress == seekBar.getMax()) {
                        seekBar.setThumb(null);
                    } else {
                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb)));
                    }
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
                    if (progress == seekBar.getMax()) {
                        seekBar.setThumb(null);
                    } else {
                        seekBar.setThumb(getResources().getDrawable((R.drawable.bg_popup_btn_thumb)));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        carSpeedGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            }
        });
        carRecycleGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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


    @Override
    public void onClick(View v) {
        boolean isActivated = false;
        switch (v.getId()) {
            case R.id.id_top_image:
            case R.id.id_top_content:
            case R.id.id_main_popup_view:
                manager.hide();
                break;
            case R.id.id_main_popup_view_content:
                break;
            case R.id.id_bluetooth_Btn:
                isActivated = bluetoothControlView.isActivated();
                if (isActivated) {
                    //todo 关闭蓝牙
                    //蓝牙是否开启
                    BluetoothAdapter.getDefaultAdapter().disable();
                } else {
                    //todo 打开蓝牙
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
//                    SitechDevLog.i(TAG, "ACTION_MOVE (x - preX)==" + (x - preX) + "，点击坐标 (y - preY)==" + (y - preY));
//                    manager.move(x - preX, y - preY);
//                }
//                preX = x;
//                preY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                isPullDownView = false;
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

//    /**
//     * 判断触摸的点是否在view范围内
//     */
//    private boolean isInClickView(View v, MotionEvent event) {
//        SitechDevLog.i(TAG, "isInClickView22==========================================");
//        int[] l = {0, 0};
//        v.getLocationInWindow(l);
//        int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
//        float eventX = event.getX();
//        float eventY = event.getY();
//        Rect rect = new Rect(left, top, right, bottom);
//        SitechDevLog.i(TAG, "isInClickView22=eventX=" + eventX + "，isInClickView  eventY==" + eventY);
//        SitechDevLog.i(TAG, "isInClickView22=getRawX=" + event.getRawX() + "，isInClickView   getRawY==" + event.getRawY());
//        SitechDevLog.i(TAG, "isInClickView22=frame.contains((int) getRawX, (int) getRawY)=" + (rect.contains((int) (event.getRawX()), (int) (event.getRawY()))));
//        SitechDevLog.i(TAG, "isInClickView22=frame.contains((int) eventX, (int) eventY)=" + (rect.contains((int) eventX, (int) eventY)));
//        return rect.contains((int) eventX, (int) eventY);
//    }
}
