package com.sitechdev.vehicle.pad.net.interception;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.manager.UserManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：SitechInterceptor
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 20:39
 * 修改时间：
 * 备注：
 */
public class SitechRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        SitechDevLog.d(AppConst.TAG, this + "拦截   =  : " + request.url());

        Request.Builder requestBuilder = request.newBuilder();
        if (!StringUtils.isEmpty(UserManager.getInstance().getUserToken())) {
            SitechDevLog.d(AppConst.TAG, this + "增加用户token到header中   =  : " + UserManager.getInstance().getUserToken());
            //token必须以Bearer 为前缀
            requestBuilder.header("Authorization", String.format("Bearer %s", UserManager.getInstance().getUserToken()));
        }

        return chain.proceed(requestBuilder.build());
    }

}
