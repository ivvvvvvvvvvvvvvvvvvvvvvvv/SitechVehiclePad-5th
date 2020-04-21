package com.sitechdev.net;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * OkGo 生命周期回调 json实现
 *
 * @author liuhe
 * @date 18-7-14
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private static final String TAG = JsonCallback.class.getSimpleName();

    private Type type;
    private Class<T> clazz;

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(okhttp3.Response response) {
        ResponseBody body = response.body();
        if (null == body) {
            return null;
        }
        T data = null;
        Gson gson = GsonUtils.getGson();
        try {
            JsonReader jsonReader = new JsonReader(body.charStream());
            if (null != type) {
                data = gson.fromJson(jsonReader, type);
            }
            if (null != clazz) {
                data = gson.fromJson(jsonReader, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void onError(Response<T> response) {
        // todo 后边重新搭建网络框架，抽取baseBean及统一处理
//        if (null != response) {
//            T body = response.body();
//            if (body instanceof  BaseBean){
//                if (null != body && StringUtils.isEquals(HttpCode.HTTP_CODE_LOGOUT, body.code)) {
//                    Log.e(TAG, "登录踢出~");
//                    EventBusUtils.postEvent(new SSOEvent(SSOEvent.EB_MSG_LOGIN_INVALID, body.message));
//                }
//            }
//        }
        super.onError(response);

    }
}