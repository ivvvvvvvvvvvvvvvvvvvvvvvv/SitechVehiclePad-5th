package com.sitechdev.vehicle.pad.view.dialog;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/5/7
 * T字形的对话框
 */
public class TDialogadapter extends DialogWrapperAdapter {
    private String title;
    private String content;
    private String leftButton;
    private String rightButton;

    public TDialogadapter(String title, @NonNull String content,
                          @NonNull String leftButton, String rightButton) {
        this.title = title;
        this.content = content;
        this.leftButton = leftButton;
        this.rightButton = rightButton;
    }

    @Override
    protected View getView() {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_t,
                null, false);
        TextView vTitle = root.findViewById(R.id.dialog_t_title);
        TextView vContent = root.findViewById(R.id.dialog_t_content);
        TextView vLeft = root.findViewById(R.id.dialog_t_left);
        TextView vRight = root.findViewById(R.id.dialog_t_right);
        if (TextUtils.isEmpty(title)) {
            vTitle.setVisibility(View.GONE);
        } else {
            vTitle.setText(title);
        }
        vContent.setText(content);
        vLeft.setText(leftButton);
        vLeft.setOnClickListener(this);
        if (TextUtils.isEmpty(rightButton)) {
            vRight.setVisibility(View.GONE);
            root.findViewById(R.id.v_line).setVisibility(View.GONE);
        } else {
            vRight.setText(rightButton);
            vRight.setOnClickListener(this);
        }
        return root;
    }
}
