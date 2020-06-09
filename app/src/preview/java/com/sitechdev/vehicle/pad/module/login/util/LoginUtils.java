package com.sitechdev.vehicle.pad.module.login.util;

import android.text.TextUtils;

import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.ParamsUtil;
import com.sitechdev.vehicle.lib.util.SerialUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.event.SSOEvent;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.manager.trace.TraceManager;
import com.sitechdev.vehicle.pad.module.login.bean.LoginResponseBean;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.util.CarNetworkUtil;
import com.sitechdev.vehicle.pad.util.PersonalDefaultUtils;

import java.security.MessageDigest;

public class LoginUtils {

    private static final String TAG = LoginUtils.class.getSimpleName();
    public static final String SP_KEY_LOGIN = "isLogin";
    public static final String SP_KEY_REFRESH_TOKEN = "refreshToken";
    public static final String SP_KEY_USER_ID = "userId";
    public static final String SP_KEY_ACCESS_TOKEN = "accessToken";
    public static final String SP_KEY_USER_NAME = "login_user_name";
    public static final String SP_KEY_USER_IMG_URL = "login_user_img_url";
    public static final String SP_VALUE_LOGIN_YES = "yes";
    public static final String SP_VALUE_LOGIN_NO = "no";

    private static boolean isParseData = false;
    /**
     * 用户积分签到bean。 主要用来判断用户是否已经签到
     */
    private static boolean userPointsToday = false;

    /**
     * 判断用户是否已经签到
     *
     * @return true=用户已经签到过
     */
    public static boolean isUserPointsToday() {
        return userPointsToday;
    }

    /**
     * 判断用户是否已经签到
     *
     * @return true=用户已经签到过
     */
    public static void setUserPointsToday(boolean userPoint) {
        LoginUtils.userPointsToday = userPoint;
    }

    /**
     * 二维码登录
     *
     * @param str
     */
    public static void loopLoginStatus(String str, BaseBribery bribery) {
        if (!isLogin()) {
            CarNetworkUtil.getInstance().toLoginByCode(str, new CarNetworkUtil.OnLoginCallback() {
                @Override
                public void onBeforeLogin() {
                }

                @Override
                public void onLoginResult(LoginResponseBean loginResponse) {
                    if (null != loginResponse && null != loginResponse.data
                            && "200".equals(loginResponse.code)) {
                        SitechDevLog.i(TAG, "login success~");
                        parseLoginInfo(loginResponse.data);
//                        FunChatLoginUtils.login();
                        if (bribery != null) {
                            bribery.onSuccess(loginResponse);
                        }
                    } else {
                        SitechDevLog.i(TAG, "login failed~");
                        if (bribery != null) {
                            bribery.onFailure(loginResponse);
                        }
                    }
                }
            });
        }
    }

    /**
     * 通过Token获取登陆信息
     */
    public static void requestLoginInfoWithToken() {
        if (TextUtils.isEmpty(LoginUtils.getBearToken())) {
            return;
        }
        OkGo.<LoginResponseBean>get(EnvironmentConfig.URL_ROOT_HOST + AppUrlConst.URL_GET_PERSONALINFO)
                .headers("Authorization", LoginUtils.getBearToken())
                .execute(new JsonCallback<LoginResponseBean>(LoginResponseBean.class) {

                    @Override
                    public void onSuccess(Response<LoginResponseBean> response) {
                        if (null != response && null != response.body()) {
                            LoginResponseBean loginResponse = response.body();
                            if (null != loginResponse.data && "200".equals(loginResponse.code)) {
                                SitechDevLog.i(TAG, "login success~");
                                parseLoginInfo(loginResponse.data);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LoginResponseBean> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * 解析登录接口返回结果。加入同步锁，防止 多线程冲突
     *
     * @param userBean loginResponse
     */
    private static synchronized void parseLoginInfo(LoginUserBean userBean) {
        if (isParseData) {
            return;
        }
        LoginUtils.setLoginStatus(true);
        LoginUtils.saveLoginData(userBean.getCredential());
        LoginUtils.saveLoginUserData(userBean);

        UserManager.getInstance().setLoginUserBean(userBean);

        //获取用户个性化设置
        PersonalDefaultUtils.requestPersonal();
//        ServerUtils.requestFunChatPersonalData();

        EventBusUtils.postEvent(new SSOEvent(SSOEvent.EB_MSG_LOGIN, userBean));
        isParseData = true;
    }

    /**
     * 重置标志位
     */
    public static void resetLoginUtil() {
        isParseData = false;
    }

    public static String generateQrMessage() {
        String ihuId = SerialUtils.getSnCode();
        String vin = SerialUtils.getVinCode();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String ihuKey = "4fc138cfba5df28fbaea23d76e251401";
        String sign = md5(vin + "." + ihuId + "." + timestamp + "." + ihuKey);
        return "QRIHU." + ihuId + "." + vin + "." + timestamp + "." + sign;
    }

    private static String md5(String plaintext) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 判断用户是否已经登录
     *
     * @return true=已经登录，false=未登录
     */
    public static boolean isLogin() {
        String isLogin = ParamsUtil.getStringData(LoginUtils.SP_KEY_LOGIN);
        boolean hasLoged = LoginUtils.SP_VALUE_LOGIN_YES.equals(isLogin);
//        if (hasLoged && NIMClient.getStatus() != StatusCode.LOGINED) {
//            FunChatLoginUtils.login();
//        }
        return hasLoged;
    }

    /**
     * 获取用户Token
     *
     * @return 用户Token或“”
     */
    public static String getUserToken() {
        String token = ParamsUtil.getStringData(LoginUtils.SP_KEY_ACCESS_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        return token;
    }

    public static String getBearToken() {
        return String.format("Bearer %s", getUserToken());
    }

    public static String getUserId() {
        return ParamsUtil.getStringData(LoginUtils.SP_KEY_USER_ID);
    }

    public static void saveLoginData(LoginUserBean.CredentialBean credentialBean) {
        //和旧版本v1.0切换时，偶现credentialBean为空的情况
//        if (credentialBean != null) {
//            GA10App.refreshUserToken = SPUtils.setData(AppApplication.getContext(), LoginUtils.SP_KEY_ACCESS_TOKEN, credentialBean.accessToken);
//            SPUtils.setData(AppApplication.getContext(), LoginUtils.SP_KEY_REFRESH_TOKEN, credentialBean.refreshToken);
//        }
        String userId = LoginUtils.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            TraceManager.getInstance().setUserId(userId);
        }
    }

    public static void saveLoginUserData(LoginUserBean userBean) {
        ParamsUtil.setData(LoginUtils.SP_KEY_USER_NAME, userBean.getNickName());
        ParamsUtil.setData(LoginUtils.SP_KEY_USER_IMG_URL, userBean.getAvatarUrl());
        ParamsUtil.setData(LoginUtils.SP_KEY_USER_ID, userBean.getUserId());
        TraceManager.getInstance().setUserId(userBean.getUserId());
    }

    public static void setLoginStatus(boolean isLogin) {
        ParamsUtil.setData(LoginUtils.SP_KEY_LOGIN, isLogin ? LoginUtils.SP_VALUE_LOGIN_YES : SP_VALUE_LOGIN_NO);
        if (!isLogin) {
            ParamsUtil.removeValue(LoginUtils.SP_KEY_ACCESS_TOKEN);
            ParamsUtil.removeValue(LoginUtils.SP_KEY_REFRESH_TOKEN);
            ParamsUtil.removeValue(LoginUtils.SP_KEY_USER_ID);
//            AppApplication.refreshUserToken = false;
            TraceManager.getInstance().setUserId("");
        }
    }

    /**
     * 处理退出登录
     * TODO
     */
    public static void handleLogout() {
//        //将用户个性化设置添加到服务器中
//        PersonalDefaultUtils.commitPersonal();
//        //将用户个性化设置恢复到登录前状态
//        PersonalDefaultUtils.setNomalPersonal2Location();
//
//        if (FunChatConfig.getInstance().isHasFunChatVersion()) {
//            FunChatLoginUtils.logout();
//            FunFragmentFactory.clear();
//            FunChatSearchWindow.getInstance().removeSearchWindow();
//        }

        setLoginStatus(false);
        EventBusUtils.postStickyEvent(new SSOEvent(SSOEvent.EB_MSG_LOGIN));
        PlayerManager.getInstance(AppApplication.getContext()).reset();
//        switch (DataFactory.produceMemData().getSysState().mCurrentSource) {
//            case MODE_KUWO:
//                KuwoUtil.exitKuwoApp();
//                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_SRC_SWITCTH, DataCenterConstants.CurrentSource.MODE_UNKNOWN));
//                EventBusUtils.postEvent(new MusicEvent(MusicEvent.EB_MUSIC_BT_CTRL_HOME, false));
//                break;
//
//            case MODE_KAOLA_FM:
//                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_SRC_SWITCTH, DataCenterConstants.CurrentSource.MODE_UNKNOWN));
//                EventBusUtils.postEvent(new MusicEvent(MusicEvent.EB_MUSIC_BT_CTRL_HOME, false));
//                break;
//            default:
//                break;
//        }
    }
}
