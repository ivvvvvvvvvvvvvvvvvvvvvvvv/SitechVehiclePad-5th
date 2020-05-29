package com.sitechdev.vehicle.pad.callback;

/**
 * 公共回调接口- 用于业务层数据返回
 */
public abstract class BaseBribery {
    /**
     * 成功的响应
     *
     * @param successObj 响应数据
     */
    public abstract void onSuccess(Object successObj);

    /**
     * 失败的响应
     *
     * @param failObj 错误数据信息
     */
    public void onFailure(Object failObj) {
    }

    /**
     * 进度的回调
     *
     * @param progress 当前已完成进度
     * @param total    需要的总长度
     * @param percent  已完成的百分比
     */
    public void onLoading(long progress, long total, String percent) {
    }
}
