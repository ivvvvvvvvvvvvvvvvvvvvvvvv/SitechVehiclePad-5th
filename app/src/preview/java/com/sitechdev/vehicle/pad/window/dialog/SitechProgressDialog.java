package com.sitechdev.vehicle.pad.window.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseDialog;
import com.sitechdev.vehicle.pad.callback.BaseBribery;


/**
 * 公共Progressdialog
 *
 * @author shaozhi
 * @date 2020/06/10
 */
public class SitechProgressDialog extends BaseDialog {

    private ImageView mLoadingImageView = null;
    private TextView mLoadingTextView = null;

    private Context context = null;

    public SitechProgressDialog(Context context) {
        super(context, R.style.set_sitech_progress_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public int getDialogContentView() {
        return R.layout.custom_dialog_progress;
    }

    @Override
    public void initView() {
        mLoadingImageView = findViewById(R.id.id_loading_img);
        mLoadingTextView = findViewById(R.id.id_loading_text);
        if (mLoadingImageView != null) {
            GlideApp.with(context).asGif().load(R.drawable.ic_loading)
                    .into(mLoadingImageView);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public void setOnCancelDialogListener(BaseBribery bribery) {
        setOnCancelListener(dialog -> {
            if (bribery != null) {
                bribery.onSuccess(null);
            }
        });
        setOnDismissListener(dialog -> {
            if (bribery != null) {
                bribery.onSuccess(null);
            }
        });
    }

    public void cancelDialog() {
        cancel();
    }
}
