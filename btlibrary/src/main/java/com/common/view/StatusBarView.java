//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.common.util.Kernel;
import com.common.util.ResourceUtil;

import java.util.Date;

public class StatusBarView implements OnClickListener {
    private static final String TAG = "StatusBarView";
    private static StatusBarView mThis;
    TextView mAppName;
    View mBack;
    private BroadcastReceiver mBroadcastReceiver;
    TextView mClock;
    private Context mContext;
    int mFlag;
    Handler mHandler = new Handler() {
        public void handleMessage(Message var1) {
            StatusBarView.this.initTime();
            super.handleMessage(var1);
        }
    };
    View mHome;
    int mIdBack = 0;
    int mIdHome = 0;
    private FrameLayout mMainView;
    private boolean mPause = true;
    private View mView;

    public StatusBarView() {
    }

    private void doPause() {
        this.mHandler.removeMessages(0);
        this.unregisterReceiver();
    }

    private void doResume() {
        this.initTime();
        this.registerReceiver();
    }

    public static StatusBarView getInstance(Context var0, View var1) {
        mThis = new StatusBarView();
        mThis.mView = var1;
        mThis.mContext = var0;
        mThis.init();
        return mThis;
    }

    private void init() {
        this.mPause = false;
        this.mMainView = (FrameLayout)this.mView.findViewById(ResourceUtil.getId(this.mContext, "common_status_bar_main"));
        this.mAppName = (TextView)this.mView.findViewById(ResourceUtil.getId(this.mContext, "cur_app_name"));
        if (this.mAppName != null) {
            this.mAppName.setVisibility(0);
            PackageManager var1 = this.mContext.getPackageManager();
            this.mAppName.setText(this.mContext.getApplicationInfo().loadLabel(var1).toString());
        }

        this.mIdBack = ResourceUtil.getId(this.mContext, "back");
        this.mBack = this.mView.findViewById(this.mIdBack);
        if (this.mBack != null) {
            this.mBack.setOnClickListener(this);
            this.mBack.setVisibility(0);
        }

        this.mIdHome = ResourceUtil.getId(this.mContext, "home");
        this.mHome = this.mView.findViewById(this.mIdHome);
        if (this.mHome != null) {
            this.mHome.setOnClickListener(this);
            this.mHome.setVisibility(0);
        }

        this.mClock = (TextView)this.mView.findViewById(ResourceUtil.getId(this.mContext, "clock"));
        if (this.mClock != null) {
            this.mClock.setVisibility(0);
            this.initTime();
        }

    }

    private void initTime() {
        if (!this.mPause) {
            int var1 = this.setTime();
            this.mHandler.sendEmptyMessageDelayed(0, (long)((60 - var1) * 1000));
        }

    }

    public static void onDestroy() {
        if (mThis != null) {
            mThis.mPause = true;
        }

        mThis = null;
    }

    public static void onPuase() {
        if (mThis != null) {
            mThis.doPause();
            mThis.mPause = true;
        }

    }

    public static void onResume() {
        if (mThis != null) {
            mThis.mPause = false;
            mThis.doResume();
        }

    }

    private void registerReceiver() {
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context var1, Intent var2) {
                    if (var2.getAction().equals("android.intent.action.TIME_SET")) {
                        StatusBarView.this.initTime();
                    }

                }
            };
        }

        IntentFilter var1 = new IntentFilter();
        var1.addAction("android.intent.action.TIME_SET");
        this.mContext.registerReceiver(this.mBroadcastReceiver, var1);
    }

    private int setTime() {
        Date var3 = new Date(System.currentTimeMillis());
        int var2 = var3.getHours();
        int var1 = var2;
        if ("12".equals(android.provider.Settings.System.getString(this.mContext.getContentResolver(), "time_12_24"))) {
            if (var2 > 12) {
                var1 = var2 - 12;
            } else {
                var1 = var2;
                if (var2 == 0) {
                    var1 = 12;
                }
            }
        }

        this.mClock.setText(String.format("%02d:%02d", var1, var3.getMinutes()));
        return var3.getSeconds();
    }

    private void unregisterReceiver() {
        if (this.mBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            this.mBroadcastReceiver = null;
        }

    }

    public void onClick(View var1) {
        if (!this.mPause) {
            if (var1.getId() == this.mIdHome) {
                Kernel.doKeyEvent(172);
                return;
            }

            int var2 = this.mIdBack;
        }

    }
}
