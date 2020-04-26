package com.sitechdev.vehicle.pad.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.view.CommonDialog;
import com.sitechdev.vehicle.pad.view.dialog.DialogWrapper;
import com.sitechdev.vehicle.pad.view.dialog.DialogWrapperFatory;

/**
 * Fragment基类
 * 使用Google推荐的newInstance方法创建Fragment对象
 *
 * @author liuhe
 */
public abstract class BaseFragment extends Fragment {

    protected static final String BUNDLE_KEY = "/bundle/key";
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected View mContentView;
    protected Context mContext;
    private CommonDialog.Builder mDialogBuilder;
    private DialogWrapper mDialogLoading;
    /**
     * 防止fragment既执行onhidden中的埋点事件，又执行onpause中的埋点事件
     */
    private boolean isHidden = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(getLayoutId(), container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewBefore();
        initView(savedInstanceState);
        initData();
        initListener();
    }

    protected void showLoadingDialog() {
        if (null == mDialogLoading) {
            createLoadingDialog(null);
        }
        mDialogLoading.show();
    }

    protected void createLoadingDialog(String text) {
        mDialogLoading = DialogWrapperFatory.createLoading(getContext(), text);
    }

    protected void dismissLoadingDialog() {
        if (null == mDialogLoading) {
            return;
        }
        mDialogLoading.dismiss();
    }

    protected DialogWrapper getLoadingDialog() {
        if (null == mDialogLoading) {
            createLoadingDialog(null);
        }
        return mDialogLoading;
    }


    protected void initViewBefore() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    protected void showLoading() {

    }

    private void hideLoading() {
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected View findViewById(@IdRes int id) {
        return mContentView.findViewById(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBusUtils.unregister(this);
        }
    }

    public void addSound(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }


    protected void showSSODialog(String title, String desc, CommonDialog.OnDialogListener listener) {
        if (null == mDialogBuilder) {
            mDialogBuilder = new CommonDialog.Builder(mContext);
        }

        if (mDialogBuilder.isShowing()) {
            return;
        }
        mDialogBuilder.setTitle(title)
                .setMessage(desc)
                .setListener(listener).build().show();
    }

    /**
     * replace
     */
    @Override
    public void onPause() {
        super.onPause();
        if(!isHidden) {
//            TraceManager.getInstance().tracePage(this.getClass(), TraceClient.PAGE_OUT);
            SitechDevLog.d(TAG, "onPause " + this.getClass().getSimpleName());
        }
        isHidden = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        isHidden = false;
    }

    /**
     * add  hide show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 隐藏
            SitechDevLog.d(TAG, "hide fragment " + this.getClass().getSimpleName());
//            TraceManager.getInstance().tracePage(this.getClass(), TraceClient.PAGE_OUT);
            isHidden = true;
        }else{
            isHidden = false;
        }
    }

//    /**
//     * 当fragment结合viewpager使用的时候 这个方法会调用
//     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            // 展示
//            DL.d(TAG, "show hint fragment " + this.getClass().getSimpleName());
//            TraceManager.getInstance().tracePage(this.getClass(), TraceClient.PAGE_IN);
//        } else {
//            // 隐藏
//            DL.d(TAG, "hide hint fragment " + this.getClass().getSimpleName());
//            TraceManager.getInstance().tracePage(this.getClass(), TraceClient.PAGE_OUT);
//        }
//    }
}
