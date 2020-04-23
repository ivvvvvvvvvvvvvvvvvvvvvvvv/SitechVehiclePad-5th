package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;

import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.util.FontUtil;

public class RightTopView extends LinearLayout {

    private final String TAG = RightTopView.class.getSimpleName();

    private ImageView iv_usb_icon, iv_wifi_icon, iv_bluetooth_icon, iv_tbox_icon, iv_hot_icon;
    private ImageView mMuteView;

    public int mWidth;
    public int mHeight;
    private RelativeLayout rl;


    public RightTopView(Context context) {
        this(context, null);
    }

    public RightTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.right_top_view, this);
        rl = findViewById(R.id.rl);

        TextClock tc = findViewById(R.id.tc);
        tc.setTypeface(FontUtil.getInstance().getMainFont());

        iv_usb_icon = findViewById(R.id.iv_usb_icon);
        iv_wifi_icon = findViewById(R.id.iv_wifi_icon);
        iv_bluetooth_icon = findViewById(R.id.iv_bluetooth_icon);
        iv_tbox_icon = findViewById(R.id.iv_tbox_icon);
        iv_hot_icon = findViewById(R.id.iv_hot_icon);
        mMuteView = findViewById(R.id.iv_top_voice);

        mWidth = rl.getLayoutParams().width;
        mHeight = rl.getLayoutParams().height;
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
            tBoxIconChange(netRssi);
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
     * 网络强度 取值范围：[1,31]
     *
     * @param netRssi 取值范围：[1,31]
     */
    private void tBoxIconChange(int netRssi) {
        ThreadUtils.runOnUIThread(() -> {
            if (netRssi <= 0) {
                iv_tbox_icon.setImageResource(R.drawable.tbox_zero);
            } else if (netRssi <= 8) {
                iv_tbox_icon.setImageResource(R.drawable.tbox_one);
            } else if (netRssi <= 16) {
                iv_tbox_icon.setImageResource(R.drawable.tbox_two);
            } else if (netRssi <= 24) {
                iv_tbox_icon.setImageResource(R.drawable.tbox_three);
            } else if (netRssi <= 33) {
                iv_tbox_icon.setImageResource(R.drawable.tbox_four);
            } else {
                iv_tbox_icon.setImageResource(R.drawable.tbox_zero);
            }
        });
    }
}
