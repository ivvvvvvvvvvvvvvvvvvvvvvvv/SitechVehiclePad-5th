package com.sitechdev.vehicle.pad.window.dialog;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseDialog;
import com.sitechdev.vehicle.pad.module.member.bean.PointsSigninBean;


/**
 * 公共dialog
 * 单按钮和双按钮样式
 *
 * @author liuhe
 * @date 2018/07/30
 */
public class SignDialog extends BaseDialog {

    private TextView mSignNumberTv, mSignTipTv, mSignCountDownTv;
    private PointsSigninBean mPointSignBean = null;
    private static final int cDownCloseDialogTimeout = 5;
    private CountDownTimer countDownTimer = null;

    public SignDialog(Context context, PointsSigninBean pointsSigninBean) {
        super(context, R.style.set_dialog);
        mPointSignBean = pointsSigninBean;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public int getDialogContentView() {
        return R.layout.custom_dialog_sign;
    }

    @Override
    public void initView() {
        mSignNumberTv = findViewById(R.id.tv_sign_number);
        mSignTipTv = findViewById(R.id.tv_sign_tip);
        mSignCountDownTv = findViewById(R.id.tv_sign_count_down_time);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        if (mPointSignBean == null) {
            return;
        }
        mSignNumberTv.setText(String.format("%s积分", mPointSignBean.getData().getIntegral()));
        mSignTipTv.setText(String.format("已连续签到%s天，明天可领%s积分", mPointSignBean.getData().getDays(), mPointSignBean.getData().getIntegral1()));
        mSignCountDownTv.setText(String.format("%s秒后自动关闭", cDownCloseDialogTimeout));
        startCountDown();
    }

    private void startCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(cDownCloseDialogTimeout * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mSignCountDownTv.setText(String.format("%s秒后自动关闭", millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    cancelDialog();
                }
            };
            countDownTimer.start();
        }
    }

    public void setDialogMessage(String message) {
    }

    public void setDialogTitle(String title) {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    public void cancelDialog() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        SignDialog.this.cancel();
    }
}
