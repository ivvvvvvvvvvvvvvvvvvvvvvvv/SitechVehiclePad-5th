package com.sitechdev.vehicle.pad.module.weather;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sitechdev.vehicle.lib.imageloader.GlideUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpActivity;
import com.sitechdev.vehicle.pad.bean.WeatherInfoBeanTwo;
import com.sitechdev.vehicle.pad.model.contract.WeatherContract;
import com.sitechdev.vehicle.pad.module.main.util.WeatherUtils;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.weather.presenter.WeatherPresenter;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.CommonProgressDialog;
import com.sitechdev.vehicle.pad.view.loading.LoadingView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SUB_APP_WEATHER)
public class WeatherActivity extends MvpActivity<WeatherPresenter> implements WeatherContract.View, View.OnClickListener {
    private final String TAG = WeatherActivity.class.getSimpleName();

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter();
    }

    private TextView tvWeatherTemp, tvTemp, tvWindLevel,
            tvCarState, tvDegree, tvAlarm, tvTitle, tvHumidity,
            tvAqi, tvGanmao, tvChuanyifu, tvYunDong, tvUv,
            tvKongtiao, tvError, tv_city;

    private Chronometer mChronometer;
    private ImageView ivWeatherIcon, ivAlarm, ivAqi, ivError, btn_backk, refreshWeatherImg = null;
    //private View viewGray;
    private LinearLayout llError, llAQI, linearWarnInfo, weatherDayInfo;
    private RelativeLayout clLayout, btnUpdate;
    private WeatherInfoBeanTwo.DataBean dataBean;
    private LoadingView llLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ivWeatherIcon = findViewById(R.id.id_weather_info_img);
        tvWeatherTemp = findViewById(R.id.id_text_weather_temp);
        tvTemp = findViewById(R.id.id_weather_info_text);
        tvWindLevel = findViewById(R.id.id_weather_wind_text);

        //预警信息
        linearWarnInfo = findViewById(R.id.btn_weather_alarm_content);
        tvAlarm = findViewById(R.id.id_tv_weather_alarm);
        ivAlarm = findViewById(R.id.id_img_weather_alarm);

        //空气质量
        tvAqi = findViewById(R.id.tv_aqi);

        //定位城市
        tv_city = findViewById(R.id.id_weather_location_city);

        //湿度
        tvHumidity = findViewById(R.id.id_weather_humudity_text);
        //更新时间
        mChronometer = findViewById(R.id.tv_update_time_desc);
        btnUpdate = findViewById(R.id.id_rl_update_time);
        refreshWeatherImg = findViewById(R.id.iv_update_img);


//        llLoading = findViewById(R.id.ll_loading);
//        llLoading.setLoadingText("加载中...");

        //洗车适宜
        tvCarState = findViewById(R.id.weather_right_info_wash_car_text_params);
        tvYunDong = findViewById(R.id.weather_right_info_sport_text_params);
        tvUv = findViewById(R.id.weather_right_info_UR_text_params);
        tvGanmao = findViewById(R.id.weather_right_info_cold_text_params);
        tvChuanyifu = findViewById(R.id.weather_right_info_clothes_text_params);
        tvKongtiao = findViewById(R.id.weather_right_info_aw_text_params);


        ivAqi = findViewById(R.id.iv_aqi);
        tvDegree = findViewById((R.id.tv_degree));
        btn_backk = findViewById(R.id.iv_sub_back);
        tvTitle = findViewById(R.id.tv_sub_title);
        llError = findViewById(R.id.ll_weather_error);
        clLayout = findViewById(R.id.id_weather_all_data);
        tvError = findViewById(R.id.tv_weather_error);
        ivError = findViewById(R.id.iv_weather_error);
        llAQI = findViewById(R.id.btn_weather_pm);

        weatherDayInfo = findViewById(R.id.id_weather_day_content);

        tvTitle.setText("天气");
        clLayout.setVisibility(View.GONE);

        tvDegree.setTypeface(FontUtil.getInstance().getMainFont());
        tvWeatherTemp.setTypeface(FontUtil.getInstance().getMainFont());
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_backk.setOnClickListener(this);
        mChronometer.setOnClickListener(this);
        refreshWeatherImg.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
//
//        tv_city.setOnClickListener(this);       //定位
//        tvAlarm.setOnClickListener(this);       //天气预警
//        llAQI.setOnClickListener(this);         //污染指数
//        tvWeatherTemp.setOnClickListener(this); //温度
//        tvHumidity.setOnClickListener(this);    //湿度
//        tvWindLevel.setOnClickListener(this);   //风力
//        btn_weather_life.setOnClickListener(this);//生活指数
//
//        llMingtian.setOnClickListener(this);
//        llHoutian.setOnClickListener(this);
//        llDahoutian.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvWeatherTemp.setTypeface(FontUtil.getInstance().getMainFont());
        tv_city.setText(String.format(" %s", mPresenter.loadCityDataWithLocation()));
        mPresenter.loadWeatherData(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            case R.id.id_rl_update_time:
            case R.id.tv_update_time_desc:
            case R.id.iv_update_img:
                loadLocalCityWeather();
                break;
//            case R.id.tv_city:
////                TraceManager.getInstance().traceClick(WeatherFragment.class, WeatherTraceEnum.BTN_WEATHER_LOCATION.getPoint(), WeatherTraceEvj.getLocation(mPresenter.loadCityDataWithLocation()));
//                break;
//
//            case R.id.ll_mingtian:
//            case R.id.ll_houtian:
//            case R.id.ll_dahoutian:
////                TraceManager.getInstance().traceClick(WeatherFragment.class, WeatherTraceEnum.BTN_WEATHER_FUTURE.getPoint());
//                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_WEATHER));

    }

    private void loadLocalCityWeather() {
//        mChronometer.setBase(SystemClock.elapsedRealtime());
//        btnUpdate.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(LocationData.getInstance().getCityName())) {
            tv_city.setText(String.format(" %s", mPresenter.loadCityDataWithLocation()));
            mPresenter.loadWeatherData(false);
        } else {
            showError(WeatherUtils.ERROR_TYPE_TOAST, WeatherUtils.ERROR_MSG_GPS);
        }
    }

    @Override
    public void showError(int type, String message) {
//        TraceManager.getInstance().traceClick(WeatherFragment.class, WeatherTraceEnum.BTN_WEATHER_REFRESH.getPoint(), WeatherTraceEvj.getRefreshStatus(false));
//        if (getActivity() == null) {
//            return;
//        }
//        llLoading.setVisibility(View.GONE);
        CommonProgressDialog.getInstance().cancel(this);
        if (type == WeatherUtils.ERROR_TYPE_NET) {
            clLayout.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(R.drawable.no_web, ivError);
            tvError.setText(message);
        } else if (type == WeatherUtils.ERROR_TYPE_OTHER) {
            clLayout.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(R.drawable.ic_teddy_error, ivError);
            tvError.setText(message);
        } else if (type == WeatherUtils.ERROR_TYPE_TOAST) {
            llError.setVisibility(View.GONE);
            clLayout.setVisibility(View.VISIBLE);
            ToastUtils.showShort(message);
        }
    }

    @Override
    public void refreshWeatherView(WeatherInfoBeanTwo msg, boolean isCatch) {
        if (!isCatch) {
//            TraceManager.getInstance().traceClick(WeatherFragment.class, WeatherTraceEnum.BTN_WEATHER_REFRESH.getPoint(), WeatherTraceEvj.getRefreshStatus(true));
        }
        dataBean = msg.getData();
        if (dataBean == null) {
            return;
        }
        RequestOptions options = new RequestOptions();
        //options.placeholder(R.drawable.qing_0);
        Glide.with(this).asBitmap().apply(options).load(WeatherUtils.getInstance().getWeatherIcon(dataBean.getImg())).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                Bitmap reflectionImageWithOrigin = createReflectionImageWithOrigin(resource);
                ivWeatherIcon.setImageBitmap(resource);
            }
        });
        // Temperature
        tvWeatherTemp.setText(dataBean.getTemp());
        // 比如：多云　下雨　下雪
        tvTemp.setText(dataBean.getWeather());
        // Wind
        tvWindLevel.setText(String.format("风力%s", dataBean.getWindpower()));
        // Humidity
        tvHumidity.setText(String.format("湿度%s", dataBean.getHumidity()));
        // Aqi
        if ("".equals(dataBean.getAqi().getAqi())) {
            llAQI.setVisibility(View.GONE);
        }
        tvAqi.setText(String.format("%s %s", dataBean.getAqi().getAqi(), dataBean.getAqi().getQuality()));
        ivAqi.setImageDrawable(getResources().getDrawable(WeatherUtils.getInstance().getAqiIcon(dataBean.getAqi().getAqi())));
        // Alarm
        if (dataBean.getAlarm().size() == 0) {
            tvAlarm.setVisibility(View.GONE);
            ivAlarm.setVisibility(View.GONE);
            linearWarnInfo.setVisibility(View.GONE);
        } else {
            tvAlarm.setText(dataBean.getAlarm().get(0));
            linearWarnInfo.setVisibility(View.VISIBLE);
        }
        // 发布时间
        updateTime();

        //生活指数
        tvCarState.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(0), 4));
        tvGanmao.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(1), 4));
        tvYunDong.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(2), 4));
        tvUv.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(3), 4));
        tvKongtiao.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(4), 4));
        tvChuanyifu.setText(WeatherUtils.getFormatLivingIndexDesc(getIndexValue(5), 4));

        // 大后天
        List<WeatherInfoBeanTwo.DataBean.DailyBean> dailyBeans = dataBean.getDaily();
        if (dailyBeans == null || dailyBeans.isEmpty()) {
            return;
        }

        SitechDevLog.i("Weather", "全部子View数量为：" + weatherDayInfo.getChildCount());
        for (int i = 0; i < dailyBeans.size(); i++) {
            WeatherInfoBeanTwo.DataBean.DailyBean dailyBean = dailyBeans.get(i);
            RelativeLayout watherDayLayout = (RelativeLayout) weatherDayInfo.getChildAt(i * 2);
            TextView dayDate = watherDayLayout.findViewById(R.id.id_weather_date);
            //date
            String iDate = dailyBean.getDate();
            if (!TextUtils.isEmpty(iDate) && iDate.length() > 8) {
                dayDate.setText(String.format("%s日", iDate.substring(8)));
            } else {
                dayDate.setVisibility(View.GONE);
            }
            TextView dayWeek = watherDayLayout.findViewById(R.id.id_weather_date_week);
            //day
            String iWeek = dailyBean.getWeek();
            if (!TextUtils.isEmpty(iWeek)) {
                if (iWeek.length() > 2) {
                    dayWeek.setText(String.format("周%s", iWeek.substring(2)));
                } else {
                    dayWeek.setText(iWeek);
                }
            } else {
                dayWeek.setVisibility(View.GONE);
            }
            TextView dayQuality = watherDayLayout.findViewById(R.id.id_weather_tip);
            //aqi
            if ("污染".equals(dailyBean.getQuality())) {
                dayQuality.setBackgroundResource(R.drawable.weather_info_tip_wuran_bg);
            } else {
                dayQuality.setBackgroundResource(R.drawable.weather_info_tip_liang_bg);
            }
            dayQuality.setText(dailyBean.getQuality());
            WeatherInfoBeanTwo.DataBean.DailyBean.NightBean nightBean = dailyBean.getNight();
            WeatherInfoBeanTwo.DataBean.DailyBean.DayBean dayBean = dailyBean.getDay();
            TextView dayStatus = watherDayLayout.findViewById(R.id.id_weather_status);
            // 多云　下雨　下雪　等等
            dayStatus.setText(dayBean.getWeather());
            TextView dayTemp = watherDayLayout.findViewById(R.id.id_weather_temp);
            // 温度
            dayTemp.setText(String.format("%s/%s°", nightBean.getTemplow(), dayBean.getTemphigh()));
        }
    }

    @Override
    public void showLoading() {
        CommonProgressDialog.getInstance().show(this);
//        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCatchSuccessLoading() {
        clLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
//        llLoading.setVisibility(View.GONE);
        CommonProgressDialog.getInstance().cancel(this);
        clLayout.setVisibility(View.VISIBLE);
    }

    private void updateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long different;
        try {
            String mUpdateTime = dataBean.getUpdatetime();
            Date updateTime = df.parse(mUpdateTime);

            Date currentTime = Calendar.getInstance().getTime();
            String now = df.format(currentTime);
            currentTime = df.parse(now);
            different = currentTime.getTime() - updateTime.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;
            long elapsedMinutes = different / minutesInMilli;

            mChronometer.setOnChronometerTickListener(cArg -> {
                if (elapsedHours > 0) {
                    cArg.setText(String.format(Locale.getDefault(), "%2d小时%2d分钟前发布", elapsedHours, elapsedMinutes));
                } else {
                    cArg.setText(elapsedMinutes < 2 ? "刚刚 " : String.format(Locale.getDefault(), "%2d分钟前发布", elapsedMinutes));
                }
                btnUpdate.setVisibility(View.VISIBLE);
            });
            mChronometer.start();
        } catch (ParseException e) {
            SitechDevLog.e(TAG, "e==updatetime" + e.toString());
        }
    }

    private String getIndexValue(int index) {
        List<WeatherInfoBeanTwo.DataBean.IndexBean> indexBeans = dataBean.getIndex();
        if (indexBeans != null && indexBeans.size() > 0 && indexBeans.get(index) != null && !TextUtils.isEmpty(indexBeans.get(index).getIvalue())) {
            return indexBeans.get(index).getIvalue();
        }
        return "";
    }
}
