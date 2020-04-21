package com.sitechdev.vehicle.pad.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;

/**
 * 项目名称：Sitech
 * 类名称：ToolbarView
 * 类描述：
 * 创建人：zhaiyu
 * 创建时间：2018/3/5 上午12:25
 * 修改时间：
 * 备注：
 */
public class ToolbarView {

    private Activity mActivity = null;

    private View mToolBarView = null;
    private ImageView mLeftImgeView = null;
    private TextView mMainTextView = null;

    public ToolbarView(Activity activity, int resID) {
        mActivity = activity;
    }

    public void setDarkStyle() {
    }

    public void setLightStyle() {
        if (mToolBarView == null) {
            init();
        }
        if (mToolBarView != null) {
            mToolBarView.setBackgroundColor(Color.WHITE);
        }
    }

    public void init() {
        if (mToolBarView == null) {
            mToolBarView = mActivity.findViewById(R.id.root_toolbar);
        }
        if (mLeftImgeView == null) {
            mLeftImgeView = mToolBarView.findViewById(R.id.id_img_left);
        }
        if (mMainTextView == null) {
            mMainTextView = mToolBarView.findViewById(R.id.id_tv_maintitle);
        }
    }

    public void setMainToolbarBg(int color) {
        init();
        mToolBarView.setBackgroundColor(color);
    }

    public void setMainTitle(int resID) {
        init();
        if (resID != 0) {
            mMainTextView.setText(resID);
        }
    }

    public void setMainTitle(String title) {
        init();
        if (!StringUtils.isEmpty(title)) {
            mMainTextView.setText(title);
        }
    }

    public View getRootToolBarView() {
        init();
        return mToolBarView;
    }

    public TextView getMainTitleView() {
        init();
        return mMainTextView;
    }

    public ImageView getLeftImageView() {
        init();
        return mLeftImgeView;
    }

    public void setLeftImageClickListener(int resID) {
        setLeftImageClickListener(resID, null);
    }

    public void setLeftImageClickListener(View.OnClickListener listener) {
        init();
        if (listener != null) {
            mLeftImgeView.setImageResource(R.drawable.ico_back);
            mLeftImgeView.setOnClickListener(listener);
        }
    }

    public void setLeftImageClickListener(int resID, View.OnClickListener listener) {
        init();
        if (resID != 0) {
            mLeftImgeView.setImageResource(resID);
            enableLeftView(true);
        }
        if (listener != null) {
            mLeftImgeView.setOnClickListener(listener);
        }
    }

    public void enableMainTitleView(boolean enable) {
        init();
        if (mMainTextView != null) {
            mMainTextView.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void enableLeftView(boolean enable) {
        init();
        if (mLeftImgeView != null) {
            mLeftImgeView.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setMainBgGray() {
        init();
        mToolBarView.setBackgroundResource(R.color.product_classify_title_bg);
    }
}
