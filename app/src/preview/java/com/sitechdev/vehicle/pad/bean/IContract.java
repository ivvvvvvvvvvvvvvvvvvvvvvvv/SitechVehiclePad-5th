package com.sitechdev.vehicle.pad.bean;

/**
 * MVP 协议层
 *
 * @author liuhe
 * @date 2019/03/29
 */
public class IContract {

    public interface IPresenter<T extends IView> {

        /**
         * 绑定view
         */
        void attach(T view);

        /**
         * 1. 取消网络请求
         * 2. 把view对象置为空
         */
        void detach();
    }

    public interface IView {
        void showLoading();

        void hideLoading();
    }

    public interface IModel {
    }
}
