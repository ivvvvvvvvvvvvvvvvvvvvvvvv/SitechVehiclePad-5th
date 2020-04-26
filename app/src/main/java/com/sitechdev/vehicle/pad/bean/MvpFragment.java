package com.sitechdev.vehicle.pad.bean;

import android.os.Bundle;

/**
 * MVP Fragment基类
 *
 * @author liuhe
 */
public abstract class MvpFragment<T extends IContract.IPresenter> extends BaseFragment implements IContract.IView {

    protected T mPresenter;

    protected abstract T createPresenter();

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    protected void initViewBefore() {
        super.initViewBefore();
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        if (null != mPresenter) {
            mPresenter.attach(this);
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (null != mPresenter) {
            this.mPresenter = null;
        }
        super.onDestroy();
    }
}
