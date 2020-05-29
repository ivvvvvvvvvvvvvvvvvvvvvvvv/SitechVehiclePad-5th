package com.sitechdev.vehicle.pad.view.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/5/24
 */
public class LoadingView extends LinearLayout {

    private AVLoadingIndicatorView indicatorView;
    private TextView textView;
    private String loadingText;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingViewStyle);
        loadingText = a.getString(R.styleable.LoadingViewStyle_loadingText);
        a.recycle();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this, true);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        indicatorView = findViewById(R.id.loading_indicator);
        textView = findViewById(R.id.loading_text);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case VISIBLE:
                indicatorView.smoothToShow();
                if (TextUtils.isEmpty(loadingText)) {
                    textView.setVisibility(GONE);
                } else {
                    textView.setText(loadingText);
                    textView.setVisibility(VISIBLE);
                }
                break;
            case INVISIBLE:
            case GONE:
                indicatorView.smoothToHide();
                break;
        }
    }

    public void setLoadingText(String text) {
        this.loadingText = text;
        if (TextUtils.isEmpty(loadingText)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(loadingText);
            textView.setVisibility(VISIBLE);
        }
    }
}
