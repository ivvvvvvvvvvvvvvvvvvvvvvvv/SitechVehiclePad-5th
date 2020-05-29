package com.sitechdev.vehicle.pad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;

import java.util.Collections;
import java.util.List;


/**
 * 公共dialog
 * 单按钮和双按钮样式
 *
 * @author liuhe
 * @date 2018/07/30
 */
public class CommonDialog {
    private Builder mBuilder;

    private CommonDialog(Builder builder) {
        this.mBuilder = builder;
    }

    public void show() {
        ThreadUtils.runOnUIThread(() -> mBuilder.showDialog());
    }

    public static class Builder extends Dialog implements View.OnClickListener {

        private TextView mNegativeTv;
        private TextView mPositiveTv;
        private ListView mListView;
        private TextView mDialogMessageTv;
        private TextView mDialogTitleTv;
        private Group mDoubleGroup;
        private Group mSingleGroup;
        private TextView mResetTipView;
        private TextView mSingleTv;
        private String mMessage;
        private String mTitle;
        private List<String> mMessageList = Collections.emptyList();
        private boolean mShowSingleBtn;
        private boolean showRestTip;
        private OnDialogListener mDialogListener;
        private onDialogCancelListener mDialogCancelListener;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_base);
            initView();
            initEvent();
            initData();
        }

        private void initView() {
            mDialogMessageTv = findViewById(R.id.tv_dialog_content);
            mPositiveTv = findViewById(R.id.tv_upgrade_positive);
            mNegativeTv = findViewById(R.id.tv_upgrade_negative);
            mListView = findViewById(R.id.lv_upgrade_list);
            mDialogTitleTv = findViewById(R.id.tv_dialog_title);
            mResetTipView = findViewById(R.id.tv_reset_tip);
            mSingleTv = findViewById(R.id.tv_upgrade_single);
            mSingleGroup = findViewById(R.id.group_single_btn);
            mDoubleGroup = findViewById(R.id.group_single_btn);
        }

        private void initEvent() {
            mPositiveTv.setOnClickListener(this);
            mNegativeTv.setOnClickListener(this);
            mSingleTv.setOnClickListener(this);
        }

        private void initData() {
            if (!StringUtils.isEmpty(mMessage)) {
                mListView.setVisibility(View.GONE);
                mDialogMessageTv.setVisibility(View.VISIBLE);
                mDialogMessageTv.setText(mMessage);
            } else {
                mListView.setVisibility(View.VISIBLE);
                mDialogMessageTv.setVisibility(View.GONE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_upgrade_list, mMessageList);
                mListView.setAdapter(adapter);
            }
            mDialogTitleTv.setText(StringUtils.isEmpty(mTitle) ? "提示" : mTitle);
            if (mShowSingleBtn) {
                mSingleGroup.setVisibility(View.VISIBLE);
                mDoubleGroup.setVisibility(View.GONE);
            } else {
                mDoubleGroup.setVisibility(View.VISIBLE);
                mSingleGroup.setVisibility(View.GONE);
            }
            mResetTipView.setVisibility(showRestTip ? View.VISIBLE : View.GONE);
        }

        public Builder(@NonNull Context context) {
            super(context, R.style.set_dialog);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wl = getWindow().getAttributes();
            wl.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            wl.gravity = Gravity.CENTER;
            getWindow().setAttributes(wl);
        }

        public Builder setListener(OnDialogListener listener) {
            this.mDialogListener = listener;
            return this;
        }

        public Builder setCancelListener(onDialogCancelListener listener) {
            this.mDialogCancelListener = listener;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setListMessage(List<String> messages) {
            this.mMessageList = messages;
            return this;
        }

        public Builder showSingleBtn(boolean singleBtn) {
            this.mShowSingleBtn = singleBtn;
            return this;
        }

        public Builder setRestTip(boolean showRestTip) {
            this.showRestTip = showRestTip;
            return this;
        }

        public CommonDialog build() {
            return new CommonDialog(this);
        }

        void showDialog() {
            show();
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.tv_upgrade_single || i == R.id.tv_upgrade_positive) {
                cancel();
                if (null != mDialogListener) {
                    mDialogListener.onPositiveListener();
                }
            } else if (i == R.id.tv_upgrade_negative) {
                cancel();
                if (null != mDialogCancelListener) {
                    mDialogCancelListener.onNegativeListener();
                }
            } else {
                cancel();
            }
        }
    }

    public interface OnDialogListener {
        void onPositiveListener();
    }

    public interface onDialogCancelListener {
        void onNegativeListener();
    }
}
