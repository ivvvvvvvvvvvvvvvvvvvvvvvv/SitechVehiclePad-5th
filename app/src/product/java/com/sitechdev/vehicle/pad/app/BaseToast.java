package com.sitechdev.vehicle.pad.app;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.sitechdev.vehicle.lib.util.R;


/**
 * 项目名称：GA10-C
 * 类名称：BaseToast
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/04/13 0013 16:01
 * 修改时间：
 * 备注：
 */
public class BaseToast extends BasicWindow {
    private final String TAG = BaseToast.class.getSimpleName();
    private TextView mToastTextView = null;
    private Handler handler = null;
    private long durationTime = 0L;

    private BaseToast() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0://展示Window
                        showWnd();
                        handler.postDelayed(() -> handler.sendEmptyMessage(1), durationTime);
                        break;
                    case 1://关闭Window
                        hideWnd();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private static class SingleBaseToast {
        private static BaseToast instance = new BaseToast();
    }

    public static BaseToast getInstance() {
        return SingleBaseToast.instance;
    }

    @Override
    public int getLayoutId() {
        return R.layout.all_toast_view;
    }

    @Override
    public void findView() {
        mToastTextView = mView.findViewById(R.id.tv_toast_tip);
        mLayoutParams.width = 350;
        mLayoutParams.height = 114;
    }

    public void showToast(String text, int duration) {
        hideWnd();
        showSingleToast(text, duration);
    }

    public void showSingleToast(String text, int duration) {
        if (mToastTextView != null) {
            mToastTextView.setText(text);
        }
        switch (duration) {
            case Toast.LENGTH_LONG:
                durationTime = 3500L;
                break;
            case Toast.LENGTH_SHORT:
                durationTime = 2000L;
                break;
            default:
                durationTime = 2000L;
                break;
        }
        handler.post(() -> handler.sendEmptyMessage(0));
    }

    @Override
    public void hideWnd() {
        super.hideWnd();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }
}
