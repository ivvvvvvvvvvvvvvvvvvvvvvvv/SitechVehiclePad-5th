package com.sitechdev.vehicle.pad.bean;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;


/**
 * MVP Fragment基类
 *
 * @author liuhe
 */
public abstract class MvpLazyFragment extends MvpFragment {
    /**
     * 界面是否已创建完成
     */
    private boolean isViewCreated;
    /**
     * 是否对用户可见
     */
    private boolean isVisibleToUser;
    /**
     * 数据是否已请求
     */
    private boolean isDataLoaded;

    /**
     * 第一次可见时触发调用,此处实现具体的数据请求逻辑
     */
    protected abstract void lazyLoadData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        tryLoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        isViewCreated = true;
        tryLoadData();
    }
    private void tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && !isDataLoaded) {
            lazyLoadData();
            isDataLoaded = true;
            //通知子Fragment请求数据
            dispatchParentVisibleState();
        }
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        return fragment == null || (fragment instanceof MvpLazyFragment && ((MvpLazyFragment) fragment).isVisibleToUser);
    }

    /**
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private void dispatchParentVisibleState() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof MvpLazyFragment && ((MvpLazyFragment) child).isVisibleToUser) {
                ((MvpLazyFragment) child).tryLoadData();
            }
        }
    }
}
