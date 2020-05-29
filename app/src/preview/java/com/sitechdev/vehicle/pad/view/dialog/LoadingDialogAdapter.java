package com.sitechdev.vehicle.pad.view.dialog;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.view.loading.AVLoadingIndicatorView;

/**
 * @author zhubaoqiang
 * @date 2019/5/22
 */
public class LoadingDialogAdapter extends DialogWrapperAdapter implements
        DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private String showText;
    private AVLoadingIndicatorView indicatorView;
    private TextView textView;

    public LoadingDialogAdapter(String showText) {
        this.showText = showText;
    }

    @Override
    protected void setDialogWrapper(DialogWrapper dialogWrapper) {
        super.setDialogWrapper(dialogWrapper);
        dialogWrapper.getDialog().setOnShowListener(this);
        dialogWrapper.getDialog().setOnDismissListener(this);
    }

    @Override
    protected View getView() {
        View root = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_layout,
                null, false);
        indicatorView = root.findViewById(
                R.id.loading_indicator);
        textView = root.findViewById(R.id.loading_text);
        return root;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        indicatorView.smoothToHide();
        textView.setVisibility(View.GONE);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        indicatorView.smoothToShow();
        if (TextUtils.isEmpty(showText)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(showText);
        }
    }
}
