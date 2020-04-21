package com.sitechdev.vehicle.pad.module.member.util;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.net.HttpCode;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.bean.BaseResponseBean;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.member.bean.FeedBackTypeBean;
import com.sitechdev.vehicle.pad.net.util.HttpUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

/**
 * 用户反馈的网络请求
 *
 * @author bijingshuai
 * @date 2019/8/21
 */
public class FeedBackHttpUtils extends HttpUtil {

    /**
     * 请求反馈的类型：车机端使用“优化意见”，若匹配失败，默认使用1所标识的类型
     *
     * @param baseBribery
     */
    public static void requestFeedBackType(BaseBribery baseBribery) {
        OkGo.<FeedBackTypeBean>get(formatBBSUrlRequestUrl(AppUrlConst.URL_FEEDBACK_TYPELIST))
                .params("platformId", AppUrlConst.HTTP_PLAT_TYPE)
                .execute(new JsonCallback<FeedBackTypeBean>(FeedBackTypeBean.class) {
                    /**
                     * 对返回数据进行操作的回调， UI线程
                     *
                     * @param response response
                     */
                    @Override
                    public void onSuccess(Response<FeedBackTypeBean> response) {
                        FeedBackTypeBean feedBackTypeBean = response.body();
                        switch (feedBackTypeBean.getCode()) {
                            case AppUrlConst.HTTP_CODE_200:
                                if (feedBackTypeBean.getData() != null && feedBackTypeBean.getData().getTypeList() != null) {
                                    List<FeedBackTypeBean.BackTypeBean.TypeBean> typeBeanList = feedBackTypeBean.getData().getTypeList();
                                    for (int i = 0; i < typeBeanList.size(); i++) {
                                        FeedBackTypeBean.BackTypeBean.TypeBean typeBean = typeBeanList.get(i);
                                        if ("优化意见".equals(typeBean.getName())) {
                                            if (baseBribery != null) {
                                                baseBribery.onSuccess(typeBean.getType());
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    onError(response);
                                }

                                break;
                            default:
                                onError(response);
                                break;
                        }
                    }

                    @Override
                    public void onError(Response<FeedBackTypeBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure("3");
                        }
                    }
                });
    }

    /**
     * 用户反馈问题提交
     *
     * @param feedbackType 反馈类型
     * @param content      反馈内容
     * @param imgFile      反馈图片
     * @param baseBribery
     */
    public static void commitFeedBack(String feedbackType, String content, List<String> imgFile, BaseBribery baseBribery) {

        if (UserManager.getInstance().getUser() == null || TextUtils.isEmpty(UserManager.getInstance().getUser().getUserId())) {
            if (baseBribery != null) {
                baseBribery.onFailure("用户未登录");
            }
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", UserManager.getInstance().getUser().getUserId());
            jsonObject.put("feedbackType", Byte.parseByte(feedbackType));
            jsonObject.put("platformId", AppUrlConst.HTTP_PLAT_TYPE);
            jsonObject.put("content", content);
            jsonObject.put("imgFile", getStringWithComma(imgFile));
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        OkGo.<BaseResponseBean>post(formatMallUrlRequestUrl(AppUrlConst.URL_FEEDBACK_SUBMIT))
                .upJson(jsonObject)
                .execute(new JsonCallback<BaseResponseBean>(BaseResponseBean.class) {
                    /**
                     * 对返回数据进行操作的回调， UI线程
                     *
                     * @param response response
                     */
                    @Override
                    public void onSuccess(Response<BaseResponseBean> response) {
                        try {
                            BaseResponseBean validCodeBean = response.body();
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
                        } catch (Exception e) {
                            SitechDevLog.exception(e);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponseBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response.message());
                        }
                    }
                });
    }

    /**
     * 用户反馈问题提交
     */
    public static void commitFeedBackUploadImg(ArrayList<File> imgFiles, BaseBribery baseBribery) {
        ArrayList<HttpParams.FileWrapper> fileWrappers = new ArrayList<>();
        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (File file : imgFiles) {
                HttpParams.FileWrapper fileWrapper = new HttpParams.FileWrapper(file, file.getName(), MediaType.parse("image/*"));
                fileWrappers.add(fileWrapper);
            }
        }
        OkGo.<String>post(formatBBSUrlRequestUrl(AppUrlConst.URL_FEEDBACK_IMAGE_UPLOAD))
                .isMultipart(true)
                .addFileWrapperParams("files", fileWrappers)
                .execute(new StringCallback() {
                    /**
                     * 对返回数据进行操作的回调， UI线程
                     *
                     * @param response
                     */
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("123", "11111111111");
                        SitechDevLog.i(AppConst.TAG, "OkGo--callback---onSuccess====response========>" + response);
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(null);
                        }
                        SitechDevLog.i(AppConst.TAG, "OkGo--callback---onError====response========>" + response);
                    }

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
                                            List<String> imgUrlList = GsonUtils.jsonToList(bodyObject.optString("data"), String.class);
                                            if (baseBribery != null) {
                                                baseBribery.onSuccess(imgUrlList);
                                            }
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
                });
    }

    /**
     * 得到String用英文逗号分割
     *
     * @param symbolList
     * @return
     */
    public static String getStringWithComma(List<String> symbolList) {
        if (symbolList == null || symbolList.size() == 0) {
            return "";
        }
        String symbol = StringUtils.join(symbolList.toArray(), ",");
        return symbol;
    }
}
