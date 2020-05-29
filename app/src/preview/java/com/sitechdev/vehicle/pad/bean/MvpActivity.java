package com.sitechdev.vehicle.pad.bean;

import com.sitechdev.vehicle.pad.app.BaseActivity;

/**
 * MVP Activity基类
 *
 * @author liuhe
 */
public abstract class MvpActivity<T extends IContract.IPresenter>
        extends BaseActivity implements IContract.IView {

    protected T mPresenter;

    protected abstract T createPresenter();

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    protected void initViewBefore() {
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }
        if (null!=mPresenter){
            mPresenter.attach(this);
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        this.mPresenter = null;
        super.onDestroy();
    }
}
