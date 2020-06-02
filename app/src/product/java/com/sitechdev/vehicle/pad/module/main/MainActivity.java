package com.sitechdev.vehicle.pad.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioPlayerManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.net.HttpCode;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.imageloader.GlideUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.event.SSOEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.module.feedback.utils.FeedBackUtils;
import com.sitechdev.vehicle.pad.module.login.bean.LoginResponseBean;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginHttpUtil;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.module.main.bean.WeatherInfoBean;
import com.sitechdev.vehicle.pad.module.main.util.MainHttpUtils;
import com.sitechdev.vehicle.pad.module.main.util.WeatherUtils;
import com.sitechdev.vehicle.pad.receiver.NetReceiver;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ReflectTextClock;
import com.sitechdev.vehicle.pad.view.ScrollTextView;
import com.sitechdev.vehicle.pad.window.manager.AppSignalWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainControlPanelWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainMenuWindowManager;
import com.sitechdev.vehicle.pad.window.view.PersonLoginWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

@BindEventBus
@Route(path = RouterConstants.HOME_MAIN)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_ALL)
public class MainActivity extends BaseActivity
        implements VoiceSourceManager.MusicChangeListener {

    private ImageView ivLogin;
    private TextView tvLogin;
    private TextView tvHome, tvWork, tvWhat;
    private TextView tvLocation, tvTemperatureDay, tvTemperature, tvWeather, tvWindow;
    private RelativeLayout mLoginRelativeView = null, rlWeather = null;
    private LinearLayout llMusic, llNews, llBook, llCar, llLife;
    private ImageView ivMusicBef, ivMusicStop, ivMusicNext;
    private ScrollTextView tvMusicName;
    private TextView btn_child_papers, btn_sitev_news, btn_car_fun, btn_life_all;
    private ImageView mHomeImageView, mWorkImageView;
    private ImageView mWeatherIconView;
    private RelativeLayout flTeddy;
    private static final String TEMP_DATA = "一 一";
    private TextView mPowerPercentView = null, mKmView = null, mRechargeCountView = null;
    private LinearLayout carPowerInfoView = null;

    //    List<Column> mColumns = new ArrayList<>();
//    Column mCurrentColumn;
//    private String KEY_COLUMN = "KEY_COLUMN";
    private Context mContext;
    private TextView btn_music_title;
//    private int musicSource  = -1;
//    private static final int KAOLA = 0;
//    private static final int LOCAL_MUSIC = 1;

    NetReceiver receiver = new NetReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initToolBarView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(AppApplication.getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + AppApplication.getContext().getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

//        NetManagerImpl.getInstance().initNetCallback();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mLoginRelativeView = findViewById(R.id.id_rela_login);
        ivLogin = (ImageView) findViewById(R.id.iv_login);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        rlWeather = (RelativeLayout) findViewById(R.id.fl_weather);
        mHomeImageView = findViewById(R.id.id_img_home);
        tvHome = (TextView) findViewById(R.id.tv_home);
        mWorkImageView = findViewById(R.id.id_img_work);
        tvWork = (TextView) findViewById(R.id.tv_work);
        tvWhat = (TextView) findViewById(R.id.tv_what);

        llMusic = (LinearLayout) findViewById(R.id.ll_music);
        llNews = (LinearLayout) findViewById(R.id.ll_news);
        llBook = (LinearLayout) findViewById(R.id.ll_book);
        llCar = (LinearLayout) findViewById(R.id.ll_car);
        llLife = (LinearLayout) findViewById(R.id.ll_life);

        ivMusicBef = (ImageView) findViewById(R.id.iv_music_bef);
        ivMusicStop = findViewById(R.id.iv_music_stop);
        ivMusicNext = (ImageView) findViewById(R.id.iv_music_next);
//        ivMusicList = (ImageView) findViewById(R.id.iv_music_list);

        tvMusicName = findViewById(R.id.tv_music_name);
//        tvMusicAuthor = (TextView) findViewById(R.id.tv_music_author);

        carPowerInfoView = findViewById(R.id.ll_car_power_info);

        btn_music_title = findViewById(R.id.id_r_m_l_music_main_title);
        btn_child_papers = findViewById(R.id.btn_child_papers);
        btn_sitev_news = findViewById(R.id.btn_sitev_news);
        btn_car_fun = findViewById(R.id.btn_car_fun);
        btn_life_all = findViewById(R.id.btn_life_all);

//        flTeddy = (RelativeLayout) findViewById(R.id.fl_teddy);

        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvTemperatureDay = (TextView) findViewById(R.id.tv_temperature_day);
        mWeatherIconView = (ImageView) findViewById(R.id.iv_weather_icon);
        tvWindow = (TextView) findViewById(R.id.tv_window);
        tvWeather = (TextView) findViewById(R.id.tv_weather);

        mPowerPercentView = (TextView) findViewById(R.id.tv_power);
        mKmView = (TextView) findViewById(R.id.tv_km);
        mRechargeCountView = (TextView) findViewById(R.id.tv_recharge);

        ReflectTextClock tcTime = (ReflectTextClock) findViewById(R.id.btn_hp_time);
        tcTime.setTypeface(FontUtil.getInstance().getMainFont());
        tvTemperature.setTypeface(FontUtil.getInstance().getMainFont());
        FeedBackUtils.deleteVoiceCache();
    }

    @Override
    protected void initData() {
        try {
            if (!StringUtils.isEmpty(UserManager.getInstance().getLoginUserBean().getCredential().getAccessToken())) {
                LoginUserBean userBean = UserManager.getInstance().getLoginUserBean();
                if (userBean != null) {
                    //存在用户token
                    refreshUserView(true, userBean);
                } else {
                    refreshUserView(false, null);
                }

            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        if (AppVariants.activeSuccess) {
            KaolaPlayManager.SingletonHolder.INSTANCE.acquireKaolaData();
            KaolaPlayManager.SingletonHolder.INSTANCE.setCallback(mCallback);
        } else {
            KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
        }

//        KaolaPlayManager.SingletonHolder.INSTANCE.setPlayCallback(mPlayCallback);
        PlayerManager.getInstance(this).addPlayerStateListener(KaolaPlayManager.SingletonHolder.INSTANCE.mIPlayerStateListener);
        BroadcastRadioPlayerManager.getInstance().addPlayerStateListener(KaolaPlayManager.SingletonHolder.INSTANCE.mIPlayerStateListener);
        //        MusicManager.getInstance().addMusicChangeListener(musicChangeListener);
        VoiceSourceManager.getInstance().addMusicChangeListener(this);

        btn_music_title.setTypeface(FontUtil.getInstance().getMainFont_Min_i());

        btn_child_papers.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_sitev_news.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_car_fun.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_life_all.setTypeface(FontUtil.getInstance().getMainFont_Min_i());

        mPowerPercentView.setText(setBottomAlignment("70", "%"));
        mKmView.setText(setBottomAlignment("238", "KM"));
        mRechargeCountView.setText(setBottomAlignment("48", "次"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            VoiceSourceManager.getInstance().removeMusicChangeListener(this);
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
//        MusicManager.getInstance().removeMusicChangeListener(musicChangeListener);
    }

//    private MusicManager.OnMusicChangeListener musicChangeListener =
//            new MusicManager.OnMusicChangeListener() {
//        @Override
//        public void onMusciChange(MusicInfo current, int status) {
//            musicSource = LOCAL_MUSIC;
//            tvMusicName.setText(current.musicName);
//            switch (status){
//                case 0:
//                    ivMusicStop.setActivated(false);
//                    break;
//                case 1:
//                    ivMusicStop.setActivated(true);
//                    break;
//            }
//        }
//    };

    //    private KaolaPlayManager.PlayCallback mPlayCallback = new KaolaPlayManager.PlayCallback() {
//        @Override
//        public void onPrepare(PlayItem playItem) {
//            if (playItem != null) {
//                musicSource = KAOLA;
//                tvMusicName.setText(playItem.getTitle());
//            }
//        }
//
//        @Override
//        public void onPlay() {
//            ivMusicStop.setActivated(true);
//        }
//
//        @Override
//        public void onPause() {
//            ivMusicStop.setActivated(false);
//        }
//    };
    private KaolaPlayManager.Callback mCallback = new KaolaPlayManager.Callback() {
        @Override
        public void onSuccess(int index, String textContent) {
            switch (index) {
                case 0:
                    btn_sitev_news.setText(textContent);
                    break;
                case 1:
                    btn_child_papers.setText(textContent);
                    break;
                case 2:
                    btn_car_fun.setText(textContent);
                    break;
                case 3:
                    btn_life_all.setText(textContent);
                    break;
                case -1:
                    CommonToast.makeText(mContext, "数据异常~~~~");
                    break;
            }
        }

        @Override
        public void onDataGot(List<Column> data) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        SitechDevLog.i(AppConst.TAG, "===MainActivity======================onResume=============================");
        EventBusUtils.register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Settings.canDrawOverlays(AppApplication.getContext())) {
                AppSignalWindowManager.getInstance().show();
                MainMenuWindowManager.getInstance().show();
                MainControlPanelWindowManager.getInstance().show();
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLoginRelativeView.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        mHomeImageView.setOnClickListener(this);
        mWorkImageView.setOnClickListener(this);
        tvWork.setOnClickListener(this);
        tvWhat.setOnClickListener(this);
        llMusic.setOnClickListener(this);
        llNews.setOnClickListener(this);
        llBook.setOnClickListener(this);
        llCar.setOnClickListener(this);
        llLife.setOnClickListener(this);
        ivMusicBef.setOnClickListener(this);
        ivMusicStop.setOnClickListener(this);
        ivMusicNext.setOnClickListener(this);
//        ivMusicList.setOnClickListener(this);
//        flTeddy.setOnClickListener(this);
        carPowerInfoView.setOnClickListener(this);
        rlWeather.setOnClickListener(this);

//        mHomeBtnImageView.setOnClickListener(this);
//        mNaviBtnImageView.setOnClickListener(this);
//        mMusicBtnImageView.setOnClickListener(this);
//        mDriverBtnImageView.setOnClickListener(this);
//        mAppsBtnImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login:
//                CommonToast.makeText(this, "请登录..");
            case R.id.id_rela_login:
                if (LoginUtils.isLogin()) {
                    //已经登录，去往会员中心
                    RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_MEMBER);
                } else {
                    //未登录，去往登录
                    PersonLoginWindow.getInstance().showWnd();
                }
                break;
            case R.id.fl_weather:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_WEATHER);
                break;
            case R.id.id_img_home:
            case R.id.tv_home:
                //回家
//                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_HOME));
                break;
            case R.id.id_img_work:
            case R.id.tv_work:
                //回公司
//                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_COMPONY));
                break;
            case R.id.tv_what:
//                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS, VoiceConstants.TTS_RESPONSE_NAVI_TEXT));
                break;
            case R.id.ll_music:
//                Intent goMusic = new Intent();
//                goMusic.setClass(this, MusicMainActivity.class);
//                startActivity(goMusic);
                break;
            case R.id.ll_news:
                KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(mContext, 0);
                break;
            case R.id.ll_book:
                KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(this, 1);
                break;
            case R.id.ll_car:
                KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(this, 2);
                break;
            case R.id.ll_life:
                KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(this, 3);
//                startActivity(new Intent(this,KaolaAudioActivity.class));
                break;
            case R.id.ll_car_power_info:
//                Intent tempIntent = new Intent(MainActivity.this, CarStatusPreActivity.class);
//                startActivity(tempIntent);
                break;
            case R.id.iv_music_bef:
//                switch (musicSource){
//                    case KAOLA:
//                        KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
//                        break;
//                    case LOCAL_MUSIC:
//                        MusicManager.getInstance().pre(new MusicManager.CallBack<String>() {
//                            @Override
//                            public void onCallBack(int code, String s) {
//                                if (0 != code){
//                                    CommonToast.showToast(s);
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        CommonToast.showToast("当前无可用音源");
//                        break;
//                }
                VoiceSourceManager.getInstance().pre(VoiceSourceManager.SCREEN);
                break;
            case R.id.iv_music_stop:
//                switch (musicSource){
//                    case KAOLA:
//                        PlayerManager.getInstance(this).switchPlayerStatus();                        break;
//                    case LOCAL_MUSIC:
//                        MusicManager.getInstance().toggle(new MusicManager.CallBack<String>() {
//                            @Override
//                            public void onCallBack(int code, String s) {
//                                if (0 != code){
//                                    CommonToast.showToast(s);
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        CommonToast.showToast("当前无可用音源");
//                        break;
//                }
                VoiceSourceManager.getInstance().toggle(VoiceSourceManager.SCREEN);
                break;
            case R.id.iv_music_next:
//                switch (musicSource){
//                    case KAOLA:
//                        KaolaPlayManager.SingletonHolder.INSTANCE.playNext();                        break;
//                    case LOCAL_MUSIC:
//                        MusicManager.getInstance().next(new MusicManager.CallBack<String>() {
//                            @Override
//                            public void onCallBack(int code, String s) {
//                                if (0 != code){
//                                    CommonToast.showToast(s);
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        CommonToast.showToast("当前无可用音源");
//                        break;
//                }
                VoiceSourceManager.getInstance().next(VoiceSourceManager.SCREEN);
                break;
//            case R.id.iv_music_list:
//                VoiceSourceManager.getInstance().toDetailActivity();
//                break;
//            case R.id.fl_teddy:
//                break;

            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(AppEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==消息==" + event.getEventKey());
        switch (event.getEventKey()) {
            case AppEvent.EVENT_APP_LOGIN_SUCCESS:
                //请求用户详细信息
                LoginHttpUtil.requestUserInfo(new BaseBribery() {
                    @Override
                    public void onSuccess(Object successObj) {
                        LoginResponseBean mLoginResponseBean = (LoginResponseBean) successObj;
                        if (mLoginResponseBean != null && mLoginResponseBean.code != null
                                && !"".equals(mLoginResponseBean.code)) {
                            if (HttpCode.HTTP_OK.equals(mLoginResponseBean.code)) {
                                LoginUserBean mLoginUserBean = mLoginResponseBean.data;
                                UserManager.getInstance().setLoginUserBean(mLoginUserBean);
                                ThreadUtils.runOnUIThread(() -> {
                                    refreshUserView(true, mLoginUserBean);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object failObj) {
                        super.onFailure(failObj);
                    }
                });
                break;
            case AppEvent.EVENT_APP_LOGIN_LOGOUT:
                //用户登出，删除用户数据
                refreshUserView(false, null);
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录的默认用户信息
     */
    private void refreshUserView(boolean isLogin, LoginUserBean userBean) {
        tvLogin.setText(isLogin ? String.format("Hi，%s", userBean.getNickName()) : "立即登录");
        if (isLogin && userBean != null) {
            Glide.with(MainActivity.this).load(userBean.getAvatarUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivLogin);
        } else {
            Glide.with(MainActivity.this).load(R.drawable.ico_default_member_logo)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivLogin);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMapEvent(MapEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==MapEvent 消息==" + event.getEventKey());
        switch (event.getEventKey()) {
            case MapEvent.EVENT_LOCATION_SUCCESS:
                //定位成功，请求或刷新天气数据
                ThreadUtils.runOnUIThread(() -> {
                    refreshCityView();
                });
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSsoEvent(SSOEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==SSOEvent 消息==" + event.eventKey);
        switch (event.eventKey) {
            case SSOEvent.EB_MSG_LOGIN:
                LoginUserBean userBean = (LoginUserBean) event.mValue;
                //刷新个人信息
                UserManager.getInstance().setLoginUserBean(userBean);
                refreshUserView(true, userBean);
                break;
            default:
                break;
        }
    }


    public void refreshWeatherView(WeatherInfoBean.DataBean dataBean) {
        tvTemperature.setText(dataBean.getTemp());
        tvTemperatureDay.setText(dataBean.getTemplow() + "°/" + dataBean.getTemphigh() + "°");
        tvWindow.setText(dataBean.getWinddirect() + dataBean.getWindpower());
        tvWeather.setText(dataBean.getWeather());
        GlideUtils.getInstance().loadImage(WeatherUtils.getInstance().getWeatherIcon(dataBean.getImg()), mWeatherIconView);
    }

    public void refreshCityView() {
        String locationDataText = WeatherUtils.getCityDataWithLocation();
        if (!locationDataText.equals(tvLocation.getText().toString().trim())) {
            tvLocation.setText(locationDataText);
            MainHttpUtils.getWeatherData(new BaseBribery() {
                @Override
                public void onSuccess(Object successObj) {
                    WeatherInfoBean weatherInfoBean = (WeatherInfoBean) successObj;
                    if (weatherInfoBean.getData() != null) {
                        ThreadUtils.runOnUIThread(() -> {
                            refreshWeatherView(weatherInfoBean.getData());
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onMusicChange(String name) {
        if (null != tvMusicName) {
            tvMusicName.setText(name);
        }
    }

    @Override
    public void pause() {
        if (null != ivMusicStop) {
            ivMusicStop.setActivated(false);
        }
    }

    @Override
    public void resume() {
        if (null != ivMusicStop) {
            ivMusicStop.setActivated(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtils.unregister(this);
    }

    private SpannableStringBuilder setBottomAlignment(String value, String unitStr) {
        SpannableStringBuilder spanString = new SpannableStringBuilder(value + " " + unitStr);
        //绝对尺寸
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(48);
        spanString.setSpan(absoluteSizeSpan, 0, String.valueOf(value).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //单位字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
        spanString.setSpan(colorSpan, String.valueOf(value).length(), spanString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 字体加粗
//        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
//        spanString.setSpan(styleSpan, 0, String.valueOf(value).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //绝对尺寸
        AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan(30);
        spanString.setSpan(absoluteSizeSpan2, String.valueOf(value).length(), spanString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return spanString;
    }
}
