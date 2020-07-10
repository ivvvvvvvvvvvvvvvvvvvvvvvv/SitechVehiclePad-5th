package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.util.JumpUtils;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：AppSignalView
 * 类描述： 右上角信号window
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 15:04
 * 修改时间：
 * 备注：
 */
public class AppSignalView extends LinearLayout {

    private final String TAG = AppSignalView.class.getSimpleName();

    private ImageView iv_usb_icon, iv_wifi_icon, iv_bluetooth_icon, iv_tbox_icon, iv_hot_icon;
    private ImageView mMuteView;

    public int mWidth;
    public int mHeight;
    private LinearLayout rl;
    private int clickCount = 0;


    public AppSignalView(Context context) {
        this(context, null);
    }

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
    }

    public AppSignalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.app_signal_view, this);
        rl = findViewById(R.id.id_linear_app_signal);

        TextClock tc = findViewById(R.id.id_app_signal_time);
        tc.setTypeface(FontUtil.getInstance().getMainFont());

        iv_usb_icon = findViewById(R.id.iv_usb_icon);
        iv_wifi_icon = findViewById(R.id.iv_wifi_icon);
        iv_bluetooth_icon = findViewById(R.id.iv_bluetooth_icon);
        iv_tbox_icon = findViewById(R.id.iv_tbox_icon);
        iv_hot_icon = findViewById(R.id.iv_hot_icon);
        mMuteView = findViewById(R.id.iv_top_voice);

        mWidth = rl.getLayoutParams().width;
        mHeight = rl.getLayoutParams().height;

        rl.setOnClickListener(v -> {
            clickCount++;
            if (clickCount >= 10) {
                //跳转原生桌面
                JumpUtils.jumpAndroidSetting();
                clickCount = 0;
            }
        });

        initData();
    }

    private void initData() {
        //USB
//        refreshUsbIconView(false);
        //WIFI
        refreshWifiIconView(NetworkUtils.getWifiEnabled());
        //蓝牙
        refreshBtIconView(BtManager.getInstance().isBtEnable());
    }

    /**
     * 刷新USB图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshUsbIconView(boolean isShow) {
        ThreadUtils.runOnUIThread(() -> {
            if (iv_usb_icon == null) {
                iv_usb_icon = findViewById(R.id.iv_usb_icon);
            }
            iv_usb_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * 刷新WIFI图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshWifiIconView(boolean isShow) {
        ThreadUtils.runOnUIThread(() -> {
            if (iv_wifi_icon == null) {
                iv_wifi_icon = findViewById(R.id.iv_wifi_icon);
            }
            iv_wifi_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * 刷新热点图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshHotIconView(boolean isShow) {
        ThreadUtils.runOnUIThread(() -> {
            if (iv_hot_icon == null) {
                iv_hot_icon = findViewById(R.id.iv_hot_icon);
            }
            iv_hot_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * 刷新蓝牙图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshBtIconView(boolean isShow) {
        ThreadUtils.runOnUIThread(() -> {
            if (iv_bluetooth_icon == null) {
                iv_bluetooth_icon = findViewById(R.id.iv_bluetooth_icon);
            }
            iv_bluetooth_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * 刷新Tbox信号图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshTboxIconView(boolean isShow, int netRssi) {
        ThreadUtils.runOnUIThread(() -> {
            if (iv_tbox_icon == null) {
                iv_tbox_icon = findViewById(R.id.iv_tbox_icon);
            }
            iv_tbox_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
//            tBoxIconChange(netRssi);
            if (isShow) {
                mobileIconChange(netRssi);
            }
        });
    }

    public void refreshTboxIconView(boolean isShow) {
//        ThreadUtils.runOnUIThread(() -> {
//            if (iv_tbox_icon == null) {
//                iv_tbox_icon = findViewById(R.id.iv_tbox_icon);
//            }
//            iv_tbox_icon.setVisibility(isShow ? View.VISIBLE : View.GONE);
//        });
    }

    public void refreshTboxIconLevel(int level) {
        ThreadUtils.runOnUIThread(() -> {
            refreshTboxIconView(true);
            tBoxIconChangeLevel(level);
        });
    }

    /**
     * 刷新静音图标
     *
     * @param isShow true=展示，否则隐藏
     */
    public void refreshVoiceIconView(boolean isShow) {
        ThreadUtils.runOnUIThread(() -> {
            if (mMuteView == null) {
                mMuteView = findViewById(R.id.iv_top_voice);
            }
            mMuteView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * 网络强度 取值范围：[0,4]
     *
     * @param netRssi 取值范围：[0,4]
     */
    private void mobileIconChange(int netRssi) {
        if (netRssi < 0) {
            netRssi = 0;
        }
        if (netRssi > 4) {
            netRssi = 4;
        }
        tBoxIconChangeLevel(netRssi);
    }

    /**
     * 网络强度 取值范围：[1,31]
     *
     * @param netRssi 取值范围：[1,31]
     */
    private void tBoxIconChange(int netRssi) {
        if (netRssi <= 0) {
            tBoxIconChangeLevel(0);
        } else if (netRssi <= 8) {
            tBoxIconChangeLevel(1);
        } else if (netRssi <= 16) {
            tBoxIconChangeLevel(2);
        } else if (netRssi <= 24) {
            tBoxIconChangeLevel(3);
        } else if (netRssi <= 33) {
            tBoxIconChangeLevel(4);
        } else {
            tBoxIconChangeLevel(0);
        }
    }

    /**
     * 网络强度 取值范围：[1,31]
     *
     * @param level 取值范围：[0,4]
     */
    private void tBoxIconChangeLevel(int level) {
        switch (level) {
            case 0:
                iv_tbox_icon.setImageResource(R.drawable.tbox_zero);
                break;
            case 1:
                iv_tbox_icon.setImageResource(R.drawable.tbox_one);
                break;
            case 2:
                iv_tbox_icon.setImageResource(R.drawable.tbox_two);
                break;
            case 3:
                iv_tbox_icon.setImageResource(R.drawable.tbox_three);
                break;
            case 4:
                iv_tbox_icon.setImageResource(R.drawable.tbox_four);
                break;
            default:
                iv_tbox_icon.setImageResource(R.drawable.tbox_zero);
                break;
        }
    }
}
