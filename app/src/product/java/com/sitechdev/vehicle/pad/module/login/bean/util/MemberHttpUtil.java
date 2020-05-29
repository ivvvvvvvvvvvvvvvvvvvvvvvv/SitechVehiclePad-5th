package com.sitechdev.vehicle.pad.module.login.bean.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.module.member.bean.PointsInfoBean;
import com.sitechdev.vehicle.pad.module.member.bean.PointsSigninBean;
import com.sitechdev.vehicle.pad.module.member.bean.TotalPointsBean;
import com.sitechdev.vehicle.pad.net.util.HttpUtil;

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
public class MemberHttpUtil extends HttpUtil {

    /**
     * 签到
     *
     * @param baseBribery 回调
     */
    public static void requestSignInAccount(BaseBribery baseBribery) {
        OkGo.<PointsSigninBean>post(getFormatRequestUrl(EnvironmentConfig.URL_MALL_HOST, AppUrlConst.URL_POST_POINTS))
                .execute(new JsonCallback<PointsSigninBean>(PointsSigninBean.class) {
                    @Override
                    public void onSuccess(Response<PointsSigninBean> response) {
                        PointsSigninBean pointBean = response.body();
                        switch (pointBean.getCode()) {
                            case AppUrlConst.HTTP_CODE_200:
                                if (baseBribery != null) {
                                    baseBribery.onSuccess(pointBean);
                                }
                                break;
                            default:
                                onError(response);
                                break;
                        }
                    }

                    @Override
                    public void onError(Response<PointsSigninBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response.body());
                        }
                    }
                });
    }

    /**
     * 请求用户积分数据
     *
     * @param baseBribery
     */
    public static void requestMyPointCount(BaseBribery baseBribery) {
        OkGo.<TotalPointsBean>get(EnvironmentConfig.URL_MALL_HOST + AppUrlConst.GET_USER_CREDITS)
                .execute(new JsonCallback<TotalPointsBean>(TotalPointsBean.class) {

                    @Override
                    public void onSuccess(Response<TotalPointsBean> response) {
                        TotalPointsBean databean = null != response ? response.body() : null;
                        if (baseBribery != null) {
                            baseBribery.onSuccess(databean);
                        }
                    }

                    @Override
                    public void onError(Response<TotalPointsBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response);
                        }
                    }
                });
    }

    /**
     * 请求用户积分详情数据列表
     *
     * @param pageIndex   pageIndex
     * @param baseBribery baseBribery
     */
    public static void requestMyPointList(int pageIndex, BaseBribery baseBribery) {
        OkGo.<PointsInfoBean>get(EnvironmentConfig.URL_MALL_HOST + AppUrlConst.URL_GET_POINTSINFO)
                .params("page_index", pageIndex)
                .params("page_size", 5)
                .execute(new JsonCallback<PointsInfoBean>(PointsInfoBean.class) {

                    @Override
                    public void onSuccess(Response<PointsInfoBean> response) {
                        PointsInfoBean pointsInfoBean = null != response ? response.body() : null;
                        if (baseBribery != null) {
                            baseBribery.onSuccess(pointsInfoBean);
                        }
                    }

                    @Override
                    public void onError(Response<PointsInfoBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure(response);
                        }
                    }
                });
    }
}
