package com.sitechdev.vehicle.pad.module.taxi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.model.SkinModel;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.SkinRollingTextView;
import com.sitechdev.vehicle.pad.view.SkinTextView;
import com.yy.mobile.rollingtextview.CharOrder;
import com.yy.mobile.rollingtextview.strategy.Direction;
import com.yy.mobile.rollingtextview.strategy.Strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：TaximeterActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SUB_APP_TAXI)
public class TaximeterActivity extends BaseActivity {


    private TextView tvTitle;
    /**
     * 开始计价前缀图标
     */
    private ImageView calcPriceImgView = null;
    private boolean isCalcPriceIng = false;
    /**
     * 设置计价
     */
    private ImageView settingImageView = null;
    /**
     * 历史计价
     */
    private ImageView historyImageView = null;
    /**
     * 总金额
     */
    private static SkinRollingTextView fullPriceTextView = null;
    private SkinTextView fullPriceBtnTextView = null;
    /**
     * 总里程
     */
    private static SkinTextView fullKMTextView = null;
    /**
     * 里程价金额
     */
    private SkinTextView unitPriceTextView = null;
    /**
     * 起步价
     */
    private SkinTextView unitStartPriceTextView = null;

    private static double defaultFullPrice = 0.00d, defaultFullKm = 0.0d, currentFullPrice = 0.00d, currentFullKm = 0.0d;

    private static Handler mHandler = new MyHandler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_taxi_meter;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.tv_sub_title);

        fullPriceTextView = findViewById(R.id.id_taxi_full_price);

        fullKMTextView = findViewById(R.id.id_taxi_full_km_number);

        unitPriceTextView = findViewById(R.id.id_taxi_single_unit_price);
        unitStartPriceTextView = findViewById(R.id.id_taxi_start_price_price);

        settingImageView = findViewById(R.id.id_taxi_setting);
        historyImageView = findViewById(R.id.id_taxi_history);

        calcPriceImgView = findViewById(R.id.id_taxi_price);
        fullPriceBtnTextView = findViewById(R.id.id_tv_taxi_price_start_calc_text);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);

        //开始计价
        findViewById(R.id.id_linear_taxi_price_start_calc).setOnClickListener(this);

        settingImageView.setOnClickListener(this);
        historyImageView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("出行计价器");
        //设置字体
        fullPriceTextView.setTypeface(FontUtil.getInstance().getMainFont());
        fullKMTextView.setTypeface(FontUtil.getInstance().getMainFont());

        fullPriceTextView.setText("0.00");
        fullPriceTextView.setAnimationDuration(500L);
        fullPriceTextView.addCharOrder(CharOrder.Number);
        fullPriceTextView.setCharStrategy(Strategy.SameDirectionAnimation(Direction.SCROLL_UP));
        fullPriceTextView.setAnimationInterpolator(new AccelerateDecelerateInterpolator());
        fullPriceTextView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //finsih
                SitechDevLog.i("Taxi", "onAnimationEnd===========");
            }
        });

    }

    private void startChangePriceTextView() {
        GlideApp.with(this).load(R.drawable.ico_taxi_stop_cal_price).into(calcPriceImgView);
        fullPriceBtnTextView.setText("停止计价");
        currentFullPrice = defaultFullPrice;
        currentFullKm = defaultFullKm;
        mHandler.sendEmptyMessage(0);
    }

    private void stopChangePriceTextView() {
        GlideApp.with(this).load(R.drawable.ico_taxi_start_cal_price).into(calcPriceImgView);
        fullPriceBtnTextView.setText("开始计价");
        currentFullPrice = defaultFullPrice;
        currentFullKm = defaultFullKm;
        mHandler.removeMessages(0);
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            double randomValue = Math.random();
            currentFullPrice += randomValue;
            currentFullKm += randomValue;

            fullPriceTextView.setText(new DecimalFormat("0.00").format(currentFullPrice));
            fullKMTextView.setText(new DecimalFormat("0.00").format(currentFullKm / 5));

            sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            //设置
            case R.id.id_taxi_setting:
                break;
            //历史
            case R.id.id_taxi_history:
                break;
            //点击了开始计价
            case R.id.id_linear_taxi_price_start_calc:
                if (isCalcPriceIng) {
                    isCalcPriceIng = false;
                    stopChangePriceTextView();
                } else {
                    isCalcPriceIng = true;
                    startChangePriceTextView();
                }
//                fullPriceTextView.setText(getFullPrice(10.3, 3.4, 5.2));
//                fullKMTextView.setText(getFullKmText(10.3));
                break;
            default:
                break;
        }
    }

    private String getFullPrice(double fullKm, double unitPrice, double startPrice) {
        SitechDevLog.i("Taxi", "总里程=" + fullKm + ", 单价（元/公里）=" + unitPrice + ",起步价（元/3公里）=" + startPrice);
        //总里程
        BigDecimal fullKmDecimal = new BigDecimal(fullKm);
        //里程单价 元/公里
        BigDecimal unitPriceDecimal = new BigDecimal(unitPrice);
        //起步价 元/3公里
        BigDecimal startPriceDecimal = new BigDecimal(startPrice);
        //计算结果
        BigDecimal resultDecimal = BigDecimal.ZERO;

        //计算公司==起步价+（总里程-3）*单价
        resultDecimal = startPriceDecimal.add(unitPriceDecimal.multiply(fullKmDecimal.subtract(new BigDecimal(3))));
        if (resultDecimal.equals(BigDecimal.ZERO)) {
            return "00.00";
        } else {
            return String.valueOf(resultDecimal.setScale(2, RoundingMode.UP).doubleValue());
        }
    }

    private String getFullKmText(double fullKm) {
        SitechDevLog.i("Taxi", "总里程=" + fullKm);
        //总里程
        BigDecimal fullKmDecimal = new BigDecimal(fullKm);

        return String.valueOf(fullKmDecimal.setScale(2, RoundingMode.UP).doubleValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (fullPriceTextView != null) {
                fullPriceTextView.getAnimation().cancel();
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }
}
