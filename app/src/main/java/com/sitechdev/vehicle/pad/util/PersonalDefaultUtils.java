package com.sitechdev.vehicle.pad.util;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.net.HttpCode;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.bean.PersonalDefaultBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;


/**
 * 用户个性化设置工具类
 * <p>
 * 整体思路：
 * 1 大前提：当前各个设置的参数自己都已经进行了持久化操作
 * 2 登录时，先将本地的个性化参数持久化（LocationBean），再从网络获取个性化参数（NetBean），最后将网络数据设置到本地。
 * 3 退出登录时，获取用户当前的个性化参数并提交到网络，再获取LocationBean设置到本地。
 *
 * @author bijingshuai
 * @date 2019/6/26
 */
public class PersonalDefaultUtils {

    private static String TAG = PersonalDefaultUtils.class.getSimpleName();
    public static String SP_PERSONAL_LOCATION = "SP_PERSONAL_LOCATION";

    /**
     * 用于标识接口访问次数，用该变量来控制只有一次访问
     */
    private static boolean isLogin = false;

    /**
     * 请求用户的个性化配置
     */
    public static void requestPersonal() {
        if (isLogin) {
            return;
        }
        isLogin = true;

        String requestUrl = String.format("%s%s%s", EnvironmentConfig.URL_ROOT_HOST, AppUrlConst.PERSONAL_CONFIG_URL, getParams());
        OkGo.<PersonalDefaultBean>get(requestUrl)
                .headers("Authorization", LoginUtils.getBearToken())
                .execute(new JsonCallback<PersonalDefaultBean>(PersonalDefaultBean.class) {
                    @Override
                    public void onSuccess(Response<PersonalDefaultBean> response) {
                        if (null == response || null == response.body()) {
                            return;
                        }
                        PersonalDefaultBean personalConfigBean = response.body();
                        if (HttpCode.HTTP_OK.equals(personalConfigBean.code)) {
                            //获取个性化配置成功
                            if (personalConfigBean.data != null) {
//                                Log.e(TAG,"onSuccess commitPersonal personalConfigBean:" + personalConfigBean.toString());
                                //将登陆前的用户个性化数据保存到本地
                                set2Location(getPersonalLocation());
                                setNomalPersonal2Location(personalConfigBean.data);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<PersonalDefaultBean> response) {
                        super.onError(response);
//                        Log.e(TAG,"====onError: " + response.message());
                    }
                });
    }

    private static String getParams() {
        //因为OKGO框架不能传递相同key的参数，所以暂时组拼参数
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        sb.append("item=welcome&");
        sb.append("item=sex&");
        sb.append("item=hu_vol_click&");
        sb.append("item=hu_vol_cpsate&");
        sb.append("item=hu_vol_type&");
        sb.append("item=ted_wake_up_w&");
        sb.append("item=hu_ted_wake_up&");
        sb.append("item=hu_ted_welcome&");
        sb.append("item=hu_ted_icon&");
        sb.append("item=hu_ted_talk&");
        sb.append("item=hu_ted_speaker&");
        sb.append("item=hu_ted_speed");
        return sb.toString();
    }

    /**
     * 将用户个性化设置提交到服务器
     */
    public static void commitPersonal() {
        isLogin = false;
        String requestUrl = String.format("%s%s", EnvironmentConfig.URL_ROOT_HOST, AppUrlConst.PERSONAL_CONFIG_URL);
        Log.e(TAG, "commitPersonal:" + GsonUtils.toJson(getPersonalLocation()));
        OkGo.<PersonalDefaultBean>put(requestUrl)
                .headers("Authorization", LoginUtils.getBearToken())
                .upJson(GsonUtils.toJson(getPersonalLocation()))
                .execute(new JsonCallback<PersonalDefaultBean>(PersonalDefaultBean.class) {
                    @Override
                    public void onSuccess(Response<PersonalDefaultBean> response) {
                        if (null == response || null == response.body()) {
                            return;
                        }
                        PersonalDefaultBean personalConfigBean = response.body();
                        if (HttpCode.HTTP_OK.equals(personalConfigBean.code)) {
//                            Log.e(TAG,"onSuccess commitPersonal personalConfigBean:" + personalConfigBean.toString());
                        }
                    }

                    @Override
                    public void onError(Response<PersonalDefaultBean> response) {
                        super.onError(response);
//                        Log.e(TAG,"=commitPersonal===onError: " + response.message());
                    }
                });
    }

    /**
     * 获取本地所有的用户个性化参数
     * TODO
     */
    private static PersonalDefaultBean.PersonalBean getPersonalLocation() {
        PersonalDefaultBean.PersonalBean model = new PersonalDefaultBean.PersonalBean();
//        model.hu_vol_click = SPUtils.getValue(AppApplication.getContext(), SettingModuleConstants.TOUCH_SOUND_EFFECT_ENABLE, true) ? 1 : 0;
//        model.hu_vol_cpsate = DataFactory.produceMemData().getSetupData().byeAVC;
//        model.hu_vol_type = DataMemImpl.getInstance().getToneData().byeEffects;
//        model.ted_wake_up_w = TeddyMain.getInstance().getMVWKeywords(false);
//        model.hu_ted_wake_up = TeddyUtil.isDisableVoiceByMvw() ? 1 : 0;
//        model.hu_ted_welcome = TeddyUtil.isDisableWelcomeMessage() ? 1 : 0;
//        model.hu_ted_talk = TeddyUtil.isDisableOngoingTalk() ? 1:0;
//        String welcomeText = SPUtils.getValue(GA10App.getContext(), TEDDY_SPKEY_TTS_WELCOME, "");
//        model.welcome = TextUtils.isEmpty(welcomeText) ? "NULL" : welcomeText ;
//        model.sex = TeddyMain.getInstance().getSexSpeaker(false) ? "男" : "女";
//        model.hu_ted_speaker = TeddyMain.getInstance().getTTSSpeaker();
//        model.hu_ted_speed = TeddyMain.getInstance().getTTSSpeed();
//        model.hu_ted_icon = TeddyConfig.isShowTeddyIcon() ? 0 : 1;

        return model;
    }

    /**
     * 将默认用户个性化值设置到本地使用
     */
    public static void setNomalPersonal2Location() {
        PersonalDefaultBean.PersonalBean model = get4Location();
        if (model == null) {
            model = new PersonalDefaultBean.PersonalBean();
            model.reset();
            //语音功能置为默认 TODO
//            TeddyUtil.resetTeddyVoice();
        }
        setNomalPersonal2Location(model);
    }

    /**
     * 将从网络获取的用户个性化值设置到本地使用
     * TODO
     */
    private static void setNomalPersonal2Location(PersonalDefaultBean.PersonalBean model) {
        //关于声音、显示的部分功能暂时不使用=======================下=========================
        //点击音效
//        SPUtils.putValue(GA10App.getContext(), TOUCH_SOUND_EFFECT_ENABLE, model.hu_vol_click == 1);
//        DataMemImpl.getInstance().getToneData().isTouchSoundEffectEnable = model.hu_vol_click == 1;
//        EventBusUtils.postEvent(new SoundEvent(SoundEvent.EB_SOUND_PLAY_CLICK));

//        DataFactory.produceMemData().getSetupData().byeAVC = (byte) model.hu_vol_cpsate;
//        DataMemImpl.getInstance().getToneData().byeEffects = model.hu_vol_type;
        //关于声音、显示的部分功能暂时不使用=======================上=========================

//        TeddyUtil.setDisableVoiceByMvw(model.hu_ted_wake_up == 1);
//        TeddyUtil.setDisableWelcomeMessage(model.hu_ted_welcome == 1);
//        TeddyUtil.setDisableOngoingTalk(model.hu_ted_talk == 1);
//        TeddyConfig.setTeddyIconVisible(model.hu_ted_icon == 0);
//        if (model.hu_ted_icon == 0) {
//            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_VOICE_ICON_ENABLE));
//        } else {
//            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_VOICE_ICON_DISABLE));
//        }
//        if (!TextUtils.isEmpty(model.ted_wake_up_w)) {
//            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_RESET_MVW_TEXT, model.ted_wake_up_w));
//        } else {
//            String word = TeddyMain.getInstance().getMVWKeywords(true);
//            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_RESET_MVW_TEXT, word));
//        }
//
//        if (!TextUtils.isEmpty(model.sex)) {
//            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_TTS_RESET_SPEAKER, "男".equals(model.sex)));
//        }
//        EventBusUtils.postEvent(new TeddyEvent(
//                TeddyEvent.EB_TEDDY_EVENT_TTS_RESET_PARAMS,
//                TeddyEvent.SPEAKER, model.hu_ted_speaker));
//        EventBusUtils.postEvent(new TeddyEvent(
//                TeddyEvent.EB_TEDDY_EVENT_TTS_RESET_PARAMS,
//                TeddyEvent.SPEED, model.hu_ted_speed));
//        if (!TextUtils.isEmpty(model.welcome)) {
//            SPUtils.putValue(GA10App.getContext(), TEDDY_SPKEY_TTS_WELCOME, "NULL".equals(model.welcome) ? "" : model.welcome);
//        } else {
//            SPUtils.putValue(GA10App.getContext(), TEDDY_SPKEY_TTS_WELCOME,"");
//        }
    }

    /**
     * @param bean
     */
    public static void set2Location(PersonalDefaultBean.PersonalBean bean) {
        SPUtils.save(AppApplication.getContext(), SP_PERSONAL_LOCATION, bean);
    }

    /**
     * @param
     */
    public static PersonalDefaultBean.PersonalBean get4Location() {
        return (PersonalDefaultBean.PersonalBean) SPUtils.get(AppApplication.getContext(), SP_PERSONAL_LOCATION);
    }
}
