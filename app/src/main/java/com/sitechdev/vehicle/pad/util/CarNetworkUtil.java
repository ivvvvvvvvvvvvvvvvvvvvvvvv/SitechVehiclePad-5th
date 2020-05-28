package com.sitechdev.vehicle.pad.util;

import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.AppUtils;
import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.event.SSOEvent;
import com.sitechdev.vehicle.pad.module.login.bean.LoginRefreshResponseBean;
import com.sitechdev.vehicle.pad.module.login.bean.LoginRequestBean;
import com.sitechdev.vehicle.pad.module.login.bean.LoginResponseBean;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;

import static com.sitechdev.vehicle.pad.app.AppUrlConst.LOGIN_GRANT;


/**
 * 项目名称：HZ_SitechDOS
 * 类名称：CarNetworkUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2018/10/16 0016 13:49
 * 修改时间：
 * 备注：
 */
public class CarNetworkUtil {

    private static final String TAG = CarNetworkUtil.class.getSimpleName();

    private static class Holder {
        private static CarNetworkUtil instance = new CarNetworkUtil();
    }

    public static CarNetworkUtil getInstance() {
        return CarNetworkUtil.Holder.instance;
    }

    /**
     * 二维码登录
     */
    public void toLoginByCode(String code, @NonNull OnLoginCallback callback) {
        callback.onBeforeLogin();
        LoginRequestBean loginRequestBean = new LoginRequestBean("authorization_code", code);
        OkGo.<LoginResponseBean>post(EnvironmentConfig.URL_ROOT_HOST + LOGIN_GRANT)
                .upJson(GsonUtils.toJson(loginRequestBean))
                .execute(new JsonCallback<LoginResponseBean>(LoginResponseBean.class) {
                    @Override
                    public void onSuccess(Response<LoginResponseBean> response) {
                        LoginResponseBean v2Bean = null != response ? response.body() : null;
                        callback.onLoginResult(v2Bean);
                    }

                    @Override
                    public void onError(Response<LoginResponseBean> response) {
                        super.onError(response);
                        LoginResponseBean v2Bean = null != response ? response.body() : null;
                        callback.onLoginResult(v2Bean);
                    }
                });
    }

    /**
     * 通过refreshToken自动登录
     */
    public void toAutoLogin() {
        String refreshToken = (String) SPUtils.getData(AppUtils.getApp(), LoginUtils.SP_KEY_REFRESH_TOKEN, "");
        LoginRequestBean loginRequestBean = new LoginRequestBean(refreshToken);
        OkGo.<LoginRefreshResponseBean>post(EnvironmentConfig.URL_ROOT_HOST + "/sitechid/v2/refreshtoken")
                .upJson(GsonUtils.toJson(loginRequestBean))
                .execute(new JsonCallback<LoginRefreshResponseBean>(LoginRefreshResponseBean.class) {
                    @Override
                    public void onSuccess(Response<LoginRefreshResponseBean> response) {
                        LoginRefreshResponseBean refreshResponseBean = null != response ? response.body() : null;
                        if (null != refreshResponseBean && response.code() >= 200 && response.code() < 300 && null != refreshResponseBean.data) {
                            LoginUtils.saveLoginData(refreshResponseBean.data);
                            SitechDevLog.i(TAG, "refresh login success~");
                        } else {
                            SitechDevLog.e(TAG, "onSuccess->refresh login failed~");
                            LoginUtils.setLoginStatus(false);
                            EventBusUtils.postStickyEvent(new SSOEvent(SSOEvent.EB_MSG_LOGIN));
                        }
                    }

                    @Override
                    public void onError(Response<LoginRefreshResponseBean> response) {
                        super.onError(response);
                        SitechDevLog.e(TAG, "onError->refresh login failed~");
//                        LoginUtils.setLoginStatus(false);
                    }
                });
    }

    /**
     * 判断用户是否被踢出
     */
    public void validUserId() {
        if (!LoginUtils.isLogin()) {
            return;
        }
        CarNetworkUtil.getInstance().refreshLoginId(this, LoginUtils.getUserId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (null != response) {
                    SitechDevLog.d(TAG, "valid userId onSuccess responseCode:" + response.code() + "-message:" + response.message());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (null != response) {
                    SitechDevLog.e(TAG, "valid userId onError responseCode:" + response.code() + "-message:" + response.message());
                }
            }
        });
    }

    /**
     * 获取用户userId
     */
    public void refreshLoginId(Object tag, String userId, StringCallback callback) {
        OkGo.<String>get(EnvironmentConfig.URL_ROOT_HOST + AppUrlConst.URL_GET_VALID_ID)
                .tag(tag)
                .params("userId", userId)
                .headers("Authorization", LoginUtils.getBearToken())
                .execute(callback);
    }

    public interface OnLoginCallback {

        void onBeforeLogin();

        void onLoginResult(LoginResponseBean loginResponse);
    }
}
