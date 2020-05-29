package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.manager.CommonTopWindowManager;

public class CommonTipView extends LinearLayout {

    private final String TAG = CommonTipView.class.getSimpleName();
    private CommonTopWindowManager manager;
    private RelativeLayout parentView = null;
    private TextView messageView = null;
    private TextView mTitleView = null;
    private Button okBtn = null, cancelBtn = null;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    public CommonTipView(Context context) {
        this(context, null);
    }

    public CommonTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.common_tip_view, this);
        parentView = findViewById(R.id.id_relative_tip);
        messageView = findViewById(R.id.tv_tip_content);

        mWidth = parentView.getLayoutParams().width;
        mHeight = parentView.getLayoutParams().height;
        manager = CommonTopWindowManager.getInstance();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleView.setText(title);
        }
    }

    public void setMessage(String text) {
        if (!TextUtils.isEmpty(text)) {
            messageView.setText(text);
        }
    }

    public void setOKClickListener(OnClickListener okClickListener) {
        if (okBtn != null) {
            okBtn.setOnClickListener(okClickListener);
        }
    }

    public void setCancelClickListener(OnClickListener cancelClickListener) {
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(cancelClickListener);
        }
    }

    public void setCancelBtnTxt(String cancelTxt) {
        if (null != cancelBtn && !StringUtils.isEmpty(cancelTxt)) {
            cancelBtn.setText(cancelTxt);
        }
    }
}
