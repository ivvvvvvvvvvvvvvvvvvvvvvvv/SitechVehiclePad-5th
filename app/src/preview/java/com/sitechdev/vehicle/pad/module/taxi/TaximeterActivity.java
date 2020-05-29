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
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.module.taxi.enums.TaxiDataModel;
import com.sitechdev.vehicle.pad.module.taxi.enums.TaxiParamsModel;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.SkinRollingTextView;
import com.sitechdev.vehicle.pad.view.SkinTextView;
import com.yy.mobile.rollingtextview.CharOrder;
import com.yy.mobile.rollingtextview.strategy.Direction;
import com.yy.mobile.rollingtextview.strategy.Strategy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
@BindEventBus
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

    private static int index = 0;
    private static float currentPriceTextSize = TaxiDataModel.Digit_Ten.getPriceSize(), currentKmTextSize = TaxiDataModel.Digit_Ten.getPriceSize();

    private static double[] priceFinalValue = {25.87d, 86.24d, 308.75d, 798.75d, 2974.88d, 8345.88d};
    private static double[] kmFinalValue = {25.8d, 86.2d, 308.75d, 988.7d, 2674.8d, 2965.8d};

    private static ReadWriteLock lockObject = new ReentrantReadWriteLock();

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
        refreshView();
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
            refreshPriceCalcView();
            sendEmptyMessageDelayed(0, 1000);
        }
    }

    /**
     * 刷新计价View显示
     */
    private static synchronized void refreshPriceCalcView() {
        lockObject.writeLock().lock();

        double randomValue = Math.random();
        currentFullPrice += randomValue;
        currentFullKm += randomValue;

//        currentFullPrice = priceFinalValue[index];
//        currentFullKm = kmFinalValue[index];

//        if (currentFullPrice >= TaxiDataModel.Digit_Thousand.getMinValue()) {
//            if (currentPriceTextSize != TaxiDataModel.Digit_Thousand.getPriceSize()) {
//                currentPriceTextSize = TaxiDataModel.Digit_Thousand.getPriceSize();
//            }
//        } else if (currentFullPrice >= TaxiDataModel.Digit_Hundred.getMinValue()) {
//            if (currentPriceTextSize != TaxiDataModel.Digit_Hundred.getPriceSize()) {
//                currentPriceTextSize = TaxiDataModel.Digit_Hundred.getPriceSize();
//            }
//        } else if (currentFullPrice < TaxiDataModel.Digit_Hundred.getMinValue()) {
//            if (currentPriceTextSize != TaxiDataModel.Digit_Ten.getPriceSize()) {
//                currentPriceTextSize = TaxiDataModel.Digit_Ten.getPriceSize();
//            }
//        }
//
//        if (currentFullKm >= TaxiDataModel.Digit_Thousand.getMinValue()) {
//            if (currentKmTextSize != TaxiDataModel.Digit_Thousand.getKmSize()) {
//                currentKmTextSize = TaxiDataModel.Digit_Thousand.getKmSize();
//            }
//        } else if (currentFullKm >= TaxiDataModel.Digit_Hundred.getMinValue()) {
//            if (currentKmTextSize != TaxiDataModel.Digit_Hundred.getKmSize()) {
//                currentKmTextSize = TaxiDataModel.Digit_Hundred.getKmSize();
//            }
//        } else if (currentFullKm < TaxiDataModel.Digit_Hundred.getMinValue()) {
//            if (currentKmTextSize != TaxiDataModel.Digit_Ten.getKmSize()) {
//                currentKmTextSize = TaxiDataModel.Digit_Ten.getKmSize();
//            }
//        }
//
//        fullPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentPriceTextSize);
//        fullKMTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentKmTextSize);

        fullPriceTextView.setText(new DecimalFormat("0.00").format(currentFullPrice));
        fullKMTextView.setText(new DecimalFormat("0.00").format(currentFullKm / 5));
        index++;
        if (index > priceFinalValue.length - 1) {
            index = 0;
        }
        lockObject.writeLock().unlock();
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
                //发布会版本，关闭该事件响应
//                EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_INPUT_SHOW_STATE));
//                TaxiSettingDialogView settingDialogView = new TaxiSettingDialogView(this);
//                settingDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_INPUT_HIDDEN_STATE));
//                        refreshView();
//                    }
//                });
//                settingDialogView.show();
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

    private void refreshView() {
        unitPriceTextView.setText(String.format("%s元/公里", TaxiParamsModel.getInstance().getSinglePriceKm()));
        unitStartPriceTextView.setText(String.format("%s元/%s公里内", TaxiParamsModel.getInstance().getStartPriceInKm(), TaxiParamsModel.getInstance().getStartPrice_km()));
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
            return "0.00";
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
    protected void onStop() {
        super.onStop();
        try {
            resetCalcView();
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    private void resetCalcView() {
        if (isCalcPriceIng) {
            isCalcPriceIng = false;
            stopChangePriceTextView();
        }
        index = 0;
//        fullPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TaxiDataModel.Digit_Ten.getPriceSize());
//        fullKMTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TaxiDataModel.Digit_Ten.getKmSize());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppEvent(AppEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "  onEventUtil====" + event.getEventKey());
        switch (event.getEventKey()) {
            case AppEvent.EVENT_APP_TAXI_START_PRICE:
                if (!isCalcPriceIng) {
                    isCalcPriceIng = true;
                    startChangePriceTextView();
                }
                break;
            case AppEvent.EVENT_APP_TAXI_STOP_PRICE:
                if (isCalcPriceIng) {
                    isCalcPriceIng = false;
                    stopChangePriceTextView();
                }
                break;
            default:
                break;
        }
    }
}
