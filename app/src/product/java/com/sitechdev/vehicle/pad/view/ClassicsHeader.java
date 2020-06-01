package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.pad.R;

public class ClassicsHeader extends LinearLayout implements RefreshHeader {


    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;

    public ClassicsHeader(Context context) {
        super(context);
        initView(context);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        textView = new TextView(context);
        progressBar = new ProgressBar(context);
        imageView = new ImageView(context);
        textView.setTextSize(20);

        imageView.setImageResource(R.drawable.arrow_down);
        LayoutParams params = new LayoutParams(20, 20);
        imageView.setLayoutParams(params);
        addView(imageView, DensityUtils.dp2px(20), DensityUtils.dp2px(20));
        addView(progressBar, DensityUtils.dp2px(40), DensityUtils.dp2px(40));
        addView(textView);
        setMinimumHeight(80);
    }

    @Override
    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (success) {
            textView.setText(" ");
            progressBar.setVisibility(GONE);
        } else {
            textView.setText(" ");
            progressBar.setVisibility(GONE);
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                imageView.setRotation(0);
                textView.setText("下拉刷新");
                imageView.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                break;
            case Refreshing:
                progressBar.setVisibility(VISIBLE);
                imageView.setVisibility(GONE);
                textView.setText("正在刷新");
                break;
            case ReleaseToRefresh:
                imageView.setRotation(180);
                textView.setText("松开刷新");
//                imageView.setVisibility(VISIBLE);
//                progressBar.setVisibility(GONE);
                break;
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void onPulling(float percent, int offset, int headHeight, int maxDragHeight) {
    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int maxDragHeight) {
    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
    }
}
