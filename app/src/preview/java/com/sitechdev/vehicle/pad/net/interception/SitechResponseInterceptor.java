package com.sitechdev.vehicle.pad.net.interception;

import com.lzy.okgo.OkGo;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.bean.RefreshTokenBean;
import com.sitechdev.vehicle.pad.bean.UserBean;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.net.util.HttpUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：SitechInterceptor
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 20:39
 * 修改时间：
 * 备注：
 */
public class SitechResponseInterceptor implements Interceptor {
    private volatile String oldToken = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        SitechDevLog.d(AppConst.TAG, this + "==拦截   =  : " + request.url());

        switch (response.code()) {
            case AppUrlConst.HTTPRESPONSE_500:
                SitechDevLog.d(AppConst.TAG, this + "响应报错   =  : " + AppUrlConst.HTTPRESPONSE_500);
                return response;
            case AppUrlConst.HTTPRESPONSE_401:
                //401时，代表用户登录身份为空或失效，中断本次请求，做相应的处理
                //返回登录界面，做重新登录处理
                SitechDevLog.d(AppConst.TAG, this + "响应报错   =  : " + AppUrlConst.HTTPRESPONSE_401);
                EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_RELOGIN));
                return null;
            case AppUrlConst.HTTPRESPONSE_403:
//                    //需要刷新登录Token
                oldToken = UserManager.getInstance().getUserToken();
                SitechDevLog.d(AppConst.TAG, this + "响应报错403 需要刷新Token流程 ==threadID=" + Thread.currentThread().getId()
                        + " , oldToken = " + oldToken);
                if (StringUtils.isEmpty(oldToken)) {
                    //token为空，需要登录
                    //重新请求
                    return response;
                }
                //如果本次请求是refreshToken的请求，则退出响应处理。跳转登录页面
                if (request != null && HttpUtil.formatUserFinalRequestUrl(AppUrlConst.URL_REFRESHTOKEN).equals(request.url())) {
                    EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_RELOGIN));
                } else {
                    //获取新Token
                    if (requestNewToken()) {
                        SitechDevLog.d(AppConst.TAG, this + "token已重新请求OK。重新发起原始请求   =  : rquest==" + request.url());
                        Request newRequest = request.newBuilder()
                                .header("Authorization", String.format("Bearer %s", UserManager.getInstance().getUserToken())).build();
                        //重新请求
                        return chain.proceed(newRequest);
                    }
                }
                break;
            default:
                //表示token刷新已结束
                break;
        }

        //继续进行后续的请求
        if (response.body() != null && response.body().contentType() != null) {
            MediaType mediaType = response.body().contentType();
            String string = response.body().string();
            SitechDevLog.d(AppConst.TAG, "mediaType =  :  " + mediaType.toString());
            SitechDevLog.d(AppConst.TAG, "string    =  : " + string);
            ResponseBody responseBody = ResponseBody.create(mediaType, string);
            return response.newBuilder().body(responseBody).build();
        } else {
            return response;
        }
    }

    /**
     * 请求新Token成功
     *
     * @return true=成功,false=失败
     */
    private synchronized boolean requestNewToken() {
        String newToken = UserManager.getInstance().getUserToken();
        SitechDevLog.d(AppConst.TAG, this + "进入刷新Token流程 = 同步请求 : start==threadID=" + Thread.currentThread().getId()
                + " , oldToken = " + oldToken + ", newToken = " + newToken + ", isEqual = " + StringUtils.isEquals(oldToken, newToken));
        if (!StringUtils.isEquals(oldToken, newToken)) {
            //isRefreshNewTokenOver=true 代表 已经进行过Token刷新
            //两个token不相同，代表已刷新了新token，不用再重新刷token
            return true;
        }

        //清空之前的token
        UserManager.getInstance().getUser().getCredential().setAccessToken("");
        try {
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("refreshToken", UserManager.getInstance().getUser().getCredential().getRefreshToken());
            Response refreshTokenResponse = OkGo.post(HttpUtil.formatUserFinalRequestUrl(AppUrlConst.URL_REFRESHTOKEN))
                    .upJson(bodyJson)
                    .execute();
            String bodyString = getResponseBody(refreshTokenResponse);
            SitechDevLog.d(AppConst.TAG, this + "   bodyString====" + bodyString);

            RefreshTokenBean refreshTokenData = GsonUtils.jsonToBean(bodyString, RefreshTokenBean.class);

            switch (refreshTokenData.getCode()) {
                case AppUrlConst.HTTP_CODE_200:
                    //刷新用户信息内容
                    LoginUserBean data = UserManager.getInstance().getLoginUserBean();
                    //对旧TOKEN重新赋值
                    oldToken = refreshTokenData.getData().getAccessToken();
                    data.getCredential().setAccessToken(refreshTokenData.getData().getAccessToken());
                    data.getCredential().setRefreshToken(refreshTokenData.getData().getRefreshToken());
                    data.getCredential().setExpiresIn(refreshTokenData.getData().getExpiresIn());
                    SitechDevLog.d(AppConst.TAG, this + "  刷新Token流程   = 新Token为 : " + refreshTokenData.getData().getAccessToken());
//                    data.getCredential().getCmdTokenDTO().setCmdExpiresIn(refreshTokenData.getData().getCmdTokenDTO().getCmdExpiresIn());
//                    data.getCredential().getCmdTokenDTO().setCmdToken(refreshTokenData.getData().getCmdTokenDTO().getCmdToken());
//                    CarManager.getInstance().refreshCarCmdToken(refreshTokenData.getCmdTokenDTO().getCmdToken(), refreshTokenData.getCmdTokenDTO().getCmdExpiresIn());
                    UserManager.getInstance().saveUserInfo(data);
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
            return false;
        }
        return true;
    }

    /**
     * 返回响应报文内容
     *
     * @return 报文内容。
     */
    private String getResponseBody(Response response) {
        if (response != null) {
            try {
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                return buffer.clone().readString(Charset.forName("utf-8"));
            } catch (Exception e) {
                SitechDevLog.exception(e);
            }
        }
        return "";
    }
}
