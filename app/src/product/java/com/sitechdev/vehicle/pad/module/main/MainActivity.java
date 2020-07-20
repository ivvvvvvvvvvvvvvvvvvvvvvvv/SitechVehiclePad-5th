package com.sitechdev.vehicle.pad.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioPlayerManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.my.hw.BluetoothEvent;
import com.my.hw.SettingConfig;
import com.sitechdev.net.HttpCode;
import com.sitechdev.vehicle.lib.event.AppEvent;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.event.MusicStatusEvent;
import com.sitechdev.vehicle.pad.event.SSOEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.module.feedback.utils.FeedBackUtils;
import com.sitechdev.vehicle.pad.module.location.LocationUtil;
import com.sitechdev.vehicle.pad.module.login.bean.LoginResponseBean;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginHttpUtil;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.module.main.bean.WeatherInfoBean;
import com.sitechdev.vehicle.pad.module.main.util.MainHttpUtils;
import com.sitechdev.vehicle.pad.module.main.util.WeatherUtils;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.music.BtMusicManager;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;
import com.sitechdev.vehicle.pad.receiver.UsbReciver;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.util.CommonUtil;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.MusicImageView;
import com.sitechdev.vehicle.pad.view.ReflectTextClock;
import com.sitechdev.vehicle.pad.view.ScrollTextView;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;
import com.sitechdev.vehicle.pad.window.manager.AppSignalWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainControlPanelWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainMenuWindowManager;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;
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
    private TextView tvBtPhoneName;
    private RelativeLayout mLoginRelativeView = null, rlWeather = null;
    private LinearLayout llMusic, llNews, llBook, llCar, llLife;
    private ImageView ivMusicBef, ivMusicStop, ivMusicNext;
    private MusicImageView ivMusicIcon;
    private ScrollTextView tvMusicName;
    private TextView btn_child_papers, btn_sitev_news, btn_car_fun, btn_life_all;
    private ImageView mHomeImageView, mWorkImageView;
    private ImageView mWeatherIconView;
    private RelativeLayout flTeddy;
    private static final String TEMP_DATA = "一 一";
    private TextView mPowerPercentView = null, mKmView = null, mRechargeCountView = null;
    private LinearLayout carPowerInfoView = null, mLinearHomeWorkView = null, mLinearHomeView = null, mLinearWorkView = null;
    private UsbReciver mUsbReceiver;
    private Context mContext;
    private TextView btn_music_title;

    //长按跳往设置家庭地址页面
    private View.OnLongClickListener homeAddressSetListener = v -> {
        SitechDevLog.i("MainActivity", "家庭地址长按事件被激活============================>");
        //家庭地址--地图选择
        MapUtil.sendAMapAddressView(0);
        return true;
    };

    //长按跳往设置公司地址页面
    private View.OnLongClickListener workAddressSetListener = v -> {
        SitechDevLog.i("MainActivity", "公司地址长按事件被激活============================>");
        //家庭地址--地图选择
        MapUtil.sendAMapAddressView(1);
        return true;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(AppApplication.getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + AppApplication.getContext().getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
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

        mLinearHomeWorkView = findViewById(R.id.id_main_right_top_left_content);
        mLinearHomeView = findViewById(R.id.id_main_top_home_content);
        mLinearWorkView = findViewById(R.id.id_main_top_work_content);
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
        ivMusicIcon = findViewById(R.id.id_r_m_l_music_icon);
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

        TextClock tcTime = (TextClock) findViewById(R.id.btn_hp_time);
        tcTime.setTypeface(FontUtil.getInstance().getMainFont());
        tvTemperature.setTypeface(FontUtil.getInstance().getMainFont());
        FeedBackUtils.deleteVoiceCache();

        //蓝牙设备名称
        tvBtPhoneName = findViewById(R.id.iv_bluetooth_phone_name);
    }

    @Override
    protected void initData() {
        try {
            if (UserManager.getInstance() != null &&
                    UserManager.getInstance().getLoginUserBean() != null &&
                    UserManager.getInstance().getLoginUserBean().getCredential() != null &&
                    !StringUtils.isEmpty(UserManager.getInstance().getLoginUserBean().getCredential().getAccessToken())) {
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

        PlayerManager.getInstance(this).addPlayerStateListener(KaolaPlayManager.SingletonHolder.INSTANCE.mIPlayerStateListener);
        BroadcastRadioPlayerManager.getInstance().addPlayerStateListener(KaolaPlayManager.SingletonHolder.INSTANCE.mIPlayerStateListener);
        VoiceSourceManager.getInstance().addMusicChangeListener(this);

        btn_music_title.setTypeface(FontUtil.getInstance().getMainFont_Min_i());

        btn_child_papers.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_sitev_news.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_car_fun.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
        btn_life_all.setTypeface(FontUtil.getInstance().getMainFont_Min_i());

        mPowerPercentView.setText(setBottomAlignment("70", "%"));
        mKmView.setText(setBottomAlignment("238", "KM"));
        mRechargeCountView.setText(setBottomAlignment("48", "次"));
        registerDeviceReceiver();

        ThreadUtils.runOnUIThreadDelay(() -> {
            //初始化定位
            LocationUtil.getInstance().init(AppApplication.getContext(), true);
            //kaola
            if (AppVariants.activeSuccess) {
                ThreadManager.getInstance().addTask(() -> {
                    try {
                        KaolaPlayManager.SingletonHolder.INSTANCE.acquireKaolaData();
                    } catch (Exception e) {
                        SitechDevLog.exception(e);
                    }
                });
                KaolaPlayManager.SingletonHolder.INSTANCE.setCallback(mCallback);
            } else {
                ThreadManager.getInstance().addTask(() -> {
                    try {
                        KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
                    } catch (Exception e) {
                        SitechDevLog.exception(e);
                    }
                });
            }

            if (BtManager.getInstance().isBtEnable()) {
                BtMusicManager.getInstance().btCtrlRequestStatus();//获取蓝牙音乐播放状态
            }
        }, 2000);
    }

    private void registerDeviceReceiver() {
        if (null == mUsbReceiver) {
            mUsbReceiver = new UsbReciver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addDataScheme("file");
        registerReceiver(mUsbReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            VoiceSourceManager.getInstance().removeMusicChangeListener(this);
//            if (receiver != null) {
//                unregisterReceiver(receiver);
//            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        unregisterReceiver(mUsbReceiver);
//        MusicManager.getInstance().removeMusicChangeListener(musicChangeListener);
    }

//    private MusicManager.OnMusicChangeListener musicChangeListener =
//            new MusicManager.OnMusicChangeListener() {
//        @Override
//        public void onMusicChange(MusicInfo current, int status) {
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
                case 1:
                    btn_sitev_news.setText(textContent);
                    break;
                case 2:
                    btn_child_papers.setText(textContent);
                    break;
                case 3:
                    btn_car_fun.setText(textContent);
                    break;
                case 4:
                    btn_life_all.setText(textContent);
                    break;
                case -1:
//                    CommonToast.makeText(mContext, "数据异常~~~~");
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
        refreshUserView(LoginUtils.isLogin(), null);

        if (!StringUtils.isEmpty(SettingConfig.getInstance().getConnectBtName())) {
            tvBtPhoneName.setText(SettingConfig.getInstance().getConnectBtName());
        } else {
            tvBtPhoneName.setText(R.string.bt_pair_unlink_tip);
        }

        //酷我有时判断不对的问题
        if (ivMusicStop != null) {
            ivMusicStop.setActivated(VoiceSourceManager.getInstance().isMusicPlaying());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Settings.canDrawOverlays(AppApplication.getContext())) {
                AppSignalWindowManager.getInstance().show();
                TeddyWindowManager.getInstance().show();
                MainMenuWindowManager.getInstance().show();
                MainControlPanelWindowManager.getInstance().show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("MainActivity", "====>onNewIntent");
        setIntent(intent);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLoginRelativeView.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        mHomeImageView.setOnClickListener(this);
        mWorkImageView.setOnClickListener(this);

        //长按跳往设置地址页面
        mLinearHomeView.setOnLongClickListener(homeAddressSetListener);
        tvHome.setOnLongClickListener(homeAddressSetListener);
        mHomeImageView.setOnLongClickListener(homeAddressSetListener);

        tvWork.setOnLongClickListener(workAddressSetListener);
        mLinearWorkView.setOnLongClickListener(workAddressSetListener);
        mWorkImageView.setOnLongClickListener(workAddressSetListener);

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
//        carPowerInfoView.setOnClickListener(this);
        rlWeather.setOnClickListener(this);

//        mHomeBtnImageView.setOnClickListener(this);
//        mNaviBtnImageView.setOnClickListener(this);
//        mMusicBtnImageView.setOnClickListener(this);
//        mDriverBtnImageView.setOnClickListener(this);
//        mAppsBtnImageView.setOnClickListener(this);
        findViewById(R.id.id_bluetooth_content).setOnClickListener(this);
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
                    PersonLoginWindow.getInstance().showWnd(() -> RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_MEMBER));
                }
                break;
            case R.id.fl_weather:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_WEATHER);
                break;
            case R.id.id_img_home:
            case R.id.tv_home:
                //回家
                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_HOME));
                break;
            case R.id.id_img_work:
            case R.id.tv_work:
                //回公司
                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_COMPONY));
                break;
            case R.id.tv_what:
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS, VoiceConstants.TTS_RESPONSE_NAVI_TEXT));
                break;
            case R.id.ll_music:
                VoiceSourceManager.getInstance().clickEvent();
                break;
            case R.id.ll_news:
                RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                        .withInt("pageIndex", 1)
                        .withInt("deepIndex", 1)
                        .withBoolean("playIfSuspend", true)
                        .navigation();
                break;
            case R.id.ll_book:
                RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                        .withInt("pageIndex", 2)
                        .withInt("deepIndex", 1)
                        .withBoolean("playIfSuspend", true)
                        .navigation();
                break;
            case R.id.ll_car:
                RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                        .withInt("pageIndex", 3)
                        .withInt("deepIndex", 1)
                        .withBoolean("playIfSuspend", true)
                        .navigation();
                break;
            case R.id.ll_life:
                RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                        .withInt("pageIndex", 4)
                        .withInt("deepIndex", 1)
                        .withBoolean("playIfSuspend", true)
                        .navigation();
                break;
            case R.id.ll_car_power_info:
//                Intent tempIntent = new Intent(MainActivity.this, CarStatusPreActivity.class);
//                startActivity(tempIntent);
//                EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_CONTROL_MENU, true));
                break;
            case R.id.iv_music_bef:
                VoiceSourceManager.getInstance().pre(VoiceSourceManager.SCREEN);
                break;
            case R.id.iv_music_stop:
                VoiceSourceManager.getInstance().toggle(VoiceSourceManager.SCREEN);
                break;
            case R.id.iv_music_next:
                VoiceSourceManager.getInstance().next(VoiceSourceManager.SCREEN);
//                SitechMusicNewManager.getInstance().onPlayNext();
                break;
//            case R.id.iv_music_list:
//                VoiceSourceManager.getInstance().toDetailActivity();
//                break;
//            case R.id.fl_teddy:
//                break;
            case R.id.id_bluetooth_content:
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_BT_PAGE);
                break;

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
        String userAreaName = "立即登录";
        if (userBean == null && isLogin) {
            userBean = UserManager.getInstance().getLoginUserBean();
            userAreaName = userBean.getNickName();
            if (StringUtils.isEmpty(userAreaName)) {
                userAreaName = CommonUtil.formatNumber(userBean.getMobile());
            }
        }
        if (tvLogin != null) {
            tvLogin.setText(isLogin ? String.format("Hi，%s", userAreaName) : userAreaName);
        }
        if (isLogin) {
            GlideApp.with(MainActivity.this).load(userBean.getAvatarUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivLogin);
        } else {
            GlideApp.with(MainActivity.this).load(R.drawable.ico_default_member_logo)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivLogin);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMapEvent(MapEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==MapEvent 消息==" + event.getEventKey());
        switch (event.getEventKey()) {
            case MapEvent.EVENT_LOCATION_SUCCESS:
                refreshCityView();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEventChange(SysEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==消息==" + event.getEvent());
        switch (event.getEvent()) {
            case SysEvent.EB_SYS_BT_STATE:
                //接收蓝牙连接或断开事件
                if (event.getObj() != null) {
                    boolean status = (boolean) event.getObj();
                    if (status) {
                        tvBtPhoneName.setText(SettingConfig.getInstance().getConnectBtName());
                        BtMusicManager.getInstance().btCtrlRequestStatus();//获取蓝牙音乐播放状态
                        BtMusicManager.getInstance().getInfo();
                        ToastUtils.showShort("蓝牙已连接");
                    } else {
                        tvBtPhoneName.setText(R.string.bt_pair_unlink_tip);
                        tvMusicName.setText("--");
                        ivMusicStop.setActivated(false);
                        ivMusicIcon.stopAnimation();
                        ToastUtils.showShort("蓝牙已断开");
                    }
                }
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
        try {
            tvTemperature.setText(dataBean.getTemp());
            tvTemperatureDay.setText(dataBean.getTemplow() + "°/" + dataBean.getTemphigh() + "°");
            tvWindow.setText(dataBean.getWinddirect() + dataBean.getWindpower());
            tvWeather.setText(dataBean.getWeather());
            GlideApp.with(this).load(BitmapFactory.decodeResource(this.getResources(), WeatherUtils.getInstance().getWeatherIcon(dataBean.getImg()))).into(mWeatherIconView);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    public void refreshCityView() {
        String locationDataText = WeatherUtils.getCityDataWithLocation();
        if (!locationDataText.equals(tvLocation.getText().toString().trim())) {
            //定位成功，刷新当前地址显示
            ThreadUtils.runOnUIThread(() -> {
                tvLocation.setText(locationDataText);
            });
            //重新请求或刷新天气数据
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
        SitechDevLog.i("Music", "MainActivity===>onMusicChange" + name);
        if (null != tvMusicName) {
            tvMusicName.setText(name);
        }
        if (null != ivMusicIcon) {
            ivMusicIcon.resumeAnimation();
            GlideApp.with(MainActivity.this).load(R.drawable.iv_music).into(ivMusicIcon);
        }
    }

    @Override
    public void pause() {
        SitechDevLog.i("Music", "MainActivity===>pause");
        if (null != ivMusicStop) {
            ivMusicStop.setActivated(false);
        }
        if (null != ivMusicIcon) {
            ivMusicIcon.pauseAnimation();
        }
    }

    @Override
    public void resume() {
        SitechDevLog.i("Music", "MainActivity===>resume");
        if (null != ivMusicStop) {
            ivMusicStop.setActivated(true);
        }
        if (null != ivMusicIcon) {
            ivMusicIcon.resumeAnimation();
        }
    }

    private SpannableStringBuilder setBottomAlignment(String value, String unitStr) {
        SpannableStringBuilder spanString = new SpannableStringBuilder(value + " " + unitStr);
        //绝对尺寸
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(36);
        spanString.setSpan(absoluteSizeSpan, 0, String.valueOf(value).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //单位字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
        spanString.setSpan(colorSpan, String.valueOf(value).length(), spanString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        // 字体加粗
//        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
//        spanString.setSpan(styleSpan, 0, String.valueOf(value).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //绝对尺寸
        AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan(20);
        spanString.setSpan(absoluteSizeSpan2, String.valueOf(value).length(), spanString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return spanString;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicEvent(MusicStatusEvent event) {
        SitechDevLog.i("MainActivity", "onMusicEvent====event.getKey()=" + event.getKey());
        switch (event.getKey()) {
            case MusicStatusEvent.EVENT_UPD_MUSIC_TITLE_INFO:
                //歌名
                String musicTitle = (String) event.getBean();
                if (StringUtils.isEmpty(musicTitle)) {
                    if (null != tvMusicName) {
                        tvMusicName.setText("-- --");
                    }
                    if (null != ivMusicIcon) {
                        ivMusicIcon.stopAnimation();
                        GlideApp.with(this).load(R.drawable.iv_music).into(ivMusicIcon);
                    }
                } else {
                    SitechDevLog.i("MainActivity", "onMusicEvent=====musicBean.getName()= " + musicTitle);
                    //当前播放的音乐名称
                    if (null != tvMusicName) {
                        //musicBean.getName() + " -- " + musicBean.getAuthor()
                        tvMusicName.setText(musicTitle);
                    }
                }
                break;
            case MusicStatusEvent.EVENT_UPD_MUSIC_SINGLE_INFO:
                //歌手
                String musicAuthor = (String) event.getBean();
                if (StringUtils.isEmpty(musicAuthor)) {
                    if (null != tvMusicName) {
                        tvMusicName.setText("-- --");
                    }
                    if (null != ivMusicIcon) {
                        ivMusicIcon.stopAnimation();
                        GlideApp.with(this).load(R.drawable.iv_music).into(ivMusicIcon);
                    }
                } else {
                    SitechDevLog.i("MainActivity", "onMusicEvent=====musicBean.getName()= " + musicAuthor);
                    //当前播放的音乐名称
                    if (null != tvMusicName) {
                        //musicBean.getName() + " -- " + musicBean.getAuthor()
                        tvMusicName.setText(musicAuthor);
                    }
                }
                break;
            case MusicStatusEvent.EVENT_UPD_MUSIC_IMAGE:
                //当前播放的音乐图片
//                Bitmap musicIconBitmap = (Bitmap) event.getBean();
//                SitechDevLog.i("MainActivity", "===> onMusicEvent=====musicBean.getIconBitmap()= " + musicIconBitmap);
//                if (musicIconBitmap == null) {
//                    ivMusicIcon.stopAnimation();
//                    GlideApp.with(this).load(R.drawable.iv_music).into(ivMusicIcon);
//                } else {
//                    if (null != ivMusicIcon) {
//                        GlideApp.with(this).load(musicIconBitmap).circleCrop().into(ivMusicIcon);
//                        ivMusicIcon.startAnimation();
//                    }
//                }
                break;
            case MusicStatusEvent.EVENT_UPD_MUSIC_PLAY_STATUS:
                //当前状态--true代表正在播放，false代表暂停播放
                boolean musicPlayStatus = (Boolean) event.getBean();
                SitechDevLog.i("MainActivity", "===> onMusicEvent=====musicPlayStatus= " + musicPlayStatus);
                if (ivMusicStop != null) {
                    ivMusicStop.setActivated(musicPlayStatus);
                    if (null != ivMusicIcon) {
                        if (musicPlayStatus) {
                            ivMusicIcon.resumeAnimation();
                        } else {
                            ivMusicIcon.pauseAnimation();
                        }
                    }
                }
                break;
            case MusicStatusEvent.EVENT_UPD_MUSIC_PROGRESS:
                //当前播放进度
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BluetoothEvent event) {
        if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_TITLE)) {
            if (VoiceSourceManager.BT_MUSIC == VoiceSourceManager.getInstance().getMusicSource()) {
                tvMusicName.setText((String) event.getObject());
            }
        } else if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_ON)) {
            if (SettingConfig.getInstance().isBtConnected()) {
                ivMusicStop.setActivated(true);
                ivMusicIcon.startAnimation();
                VoiceSourceManager.getInstance().setMusicSource(VoiceSourceManager.BT_MUSIC);
            }
        } else if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_OFF)) {
            if (SettingConfig.getInstance().isBtConnected()) {
                ivMusicStop.setActivated(false);
                ivMusicIcon.stopAnimation();
            }
        }
    }
}
