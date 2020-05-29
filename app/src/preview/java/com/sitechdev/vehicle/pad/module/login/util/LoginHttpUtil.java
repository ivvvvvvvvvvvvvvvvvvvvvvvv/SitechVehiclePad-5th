package com.sitechdev.vehicle.pad.module.login.util;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.bean.RequestValideCodeBean;
import com.sitechdev.vehicle.pad.bean.UserBean;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.module.login.bean.LoginResponseBean;
import com.sitechdev.vehicle.pad.net.util.HttpUtil;

import org.json.JSONObject;

/**
 * 项目名称：Sitech
 * 类名称：LoginHttpUtil
 * 类描述：
 *
 * @author ：shaozhi
 * 创建时间：2018/3/5 上午11:29
 * 修改时间：
 * 备注：
 */
public class LoginHttpUtil extends HttpUtil {

    /**
     * 根据手机号获取登录验证码
     *
     * @param phoneNumber 用户输入的手机号码
     */
    public static void requestLoginPhoneValid(String phoneNumber, BaseBribery baseBribery) {
        //
        OkGo.<RequestValideCodeBean>get(String.format("%s%s/", formatUserFinalRequestUrl(AppUrlConst.URL_PHONEVALID), phoneNumber))
                //SID001:app登陆验证，SID002:车控短信验证
                .params("templateId", AppConst.VALID_TYPE_APP_LOGIN)
                .execute(new JsonCallback<RequestValideCodeBean>(RequestValideCodeBean.class) {
                    /**
                     * 对返回数据进行操作的回调， UI线程
                     *
                     * @param response response
                     */
                    @Override
                    public void onSuccess(Response<RequestValideCodeBean> response) {
                        RequestValideCodeBean validCodeBean = response.body();
                        switch (validCodeBean.getCode()) {
                            case AppUrlConst.HTTP_CODE_200:
                                if (baseBribery != null) {
                                    baseBribery.onSuccess(validCodeBean);
                                }
                                break;
                            default:
                                onError(response);
                                break;
                        }
                    }

                    @Override
                    public void onError(Response<RequestValideCodeBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response.body());
                        }
                    }
                });
    }

    /**
     * 手机号码+验证码登录
     * + mobile:15800001111 (string,required) - 手机号
     * + captchaCode:123456 (string,optional) - 验证码
     * + relationId (string,optional) - 第三方登录授权接口处理后返回的relationId，如：微信code授权接口，返回的relationId，使用该id与微信用户进行关联绑定
     * + referralCode (string,optional) - 推荐识别代码，记录推荐人id、推荐码、第三方导流识别码、微信小程序二维码的场景值等等
     *
     * @param mobile      手机号
     * @param captchaCode 验证码
     * @param relationId  第三方登录授权接口处理后返回的relationId，如：微信code授权接口，返回的relationId，使用该id与微信用户进行关联绑定
     * @param baseBribery baseBribery
     */
    public static void requestPhoneValidLogin(String mobile, String captchaCode,
                                              String relationId, BaseBribery baseBribery) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("mobile", mobile);
            requestJson.put("captchaCode", captchaCode);
            requestJson.put("source", AppConst.DEVICE_SOURCE);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        OkGo.<String>post(formatUserFinalRequestUrl(AppUrlConst.URL_USER_LOGIN))
                .upJson(requestJson)
                .execute(new StringCallback() {
                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        if (null != response && response.isSuccessful()) {
                            try {
                                String bodyMessage = response.body().string();
                                SitechDevLog.i(AppConst.TAG, "OkGo--callback---onSuccess====bodyMessage========>" + bodyMessage);
                                if (TextUtils.isEmpty(bodyMessage)) {
                                    if (baseBribery != null) {
                                        baseBribery.onFailure(null);
                                    }
                                    return null;
                                }

                                JSONObject bodyObject = new JSONObject(bodyMessage);
                                if (bodyObject == null) {
                                    if (baseBribery != null) {
                                        baseBribery.onFailure(null);
                                    }
                                    return null;
                                }

                                String code = bodyObject.optString("code");
                                switch (code) {
                                    case AppUrlConst.HTTP_CODE_200:
                                        if (bodyObject.has("data") && !StringUtils.isEmpty(bodyObject.optString("data"))) {
                                            UserBean userBean = GsonUtils.jsonToBean(bodyObject.optString("data"), UserBean.class);
                                            baseBribery.onSuccess(userBean);
                                        }
                                        return null;
                                    default:
                                        if (baseBribery != null) {
                                            baseBribery.onFailure(response);
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                SitechDevLog.exception(e);
                                if (baseBribery != null) {
                                    baseBribery.onFailure(response);
                                }
                            }
                        } else {
                            if (baseBribery != null) {
                                baseBribery.onFailure(response);
                            }
                        }
                        return null;
                    }

                    /**
                     * 对返回数据进行操作的回调， UI线程
                     *
                     * @param response
                     */
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });
    }

    /**
     * 获取格式化时间
     *
     * @param mills
     * @return
     */
    public static String getFormatCountTimeResult(long mills) {
        int result = (int) (mills / 1000);
        return result < 10 ? "0" + result : String.valueOf(result);
    }

    /**
     * 请求用户详细信息
     *
     * @param baseBribery 回调
     */
    public static void requestUserInfo(BaseBribery baseBribery) {
        OkGo.<LoginResponseBean>get(EnvironmentConfig.URL_ROOT_HOST + AppUrlConst.URL_GET_PERSONALINFO)
                .execute(new JsonCallback<LoginResponseBean>(LoginResponseBean.class) {

                    @Override
                    public void onSuccess(Response<LoginResponseBean> response) {
                        LoginResponseBean loginResponseBean = response.body();
                        switch (loginResponseBean.code) {
                            case AppUrlConst.HTTP_CODE_200:
                                if (baseBribery != null) {
                                    baseBribery.onSuccess(loginResponseBean);
                                }
                                break;
                            default:
                                onError(response);
                                break;
                        }
                    }

                    @Override
                    public void onError(Response<LoginResponseBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response.body());
                        }
                    }
                });
    }
}
