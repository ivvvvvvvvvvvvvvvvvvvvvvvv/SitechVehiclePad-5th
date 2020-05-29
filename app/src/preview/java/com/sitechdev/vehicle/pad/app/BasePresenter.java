package com.sitechdev.vehicle.pad.app;

import com.sitechdev.vehicle.pad.bean.IContract;

import java.lang.ref.WeakReference;

/**
 * 软引用 Presenter
 *
 * @author liuhe
 * @date 2019/03/29
 */
public abstract class BasePresenter<T extends IContract.IView> implements IContract.IPresenter {

    /**
     * 当内存不足释放内存
     */
    private WeakReference<T> mViewRef;

    @Override
    public void attach(IContract.IView view) {
        mViewRef = new WeakReference<>((T) view);
    }

    /**
     * 获取绑定的View
     */
    public T getView() {
        //偶现mViewRef为null的情况，故增加null判断
        return mViewRef == null ? null : mViewRef.get();
    }

    /**
     * 1. 取消网络请求
     * 2. 把view对象置为空
     */
    @Override
    public void detach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
