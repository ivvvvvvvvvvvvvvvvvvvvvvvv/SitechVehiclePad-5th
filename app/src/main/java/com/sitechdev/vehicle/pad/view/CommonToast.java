package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;

/**
 * 项目名称：Sitech
 * 类名称：CommonToast
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：2018/03/12 11:32
 * 修改时间：
 * 备注：
 */
public class CommonToast {

    private static TextView mContentView = null;
    private static ImageView mTopImageView = null;
    private static Toast sToast = null;

    private static void initView() {
        View v = LayoutInflater.from(AppApplication.getContext()).inflate(R.layout.custom_toast, null);
        mTopImageView = v.findViewById(R.id.id_img_toast_top);
        mContentView = v.findViewById(R.id.tv_toast_content);
        if (sToast == null) {
            sToast = new Toast(AppApplication.getContext());
        }
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.setView(v);
    }

    public static void showToast(CharSequence text) {
        if (text == null || StringUtils.isEmpty(text.toString())) {
            return;
        }
        showToast(0, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int topImageResId, CharSequence text) {
        if (text == null || StringUtils.isEmpty(text.toString())) {
            return;
        }
        showToast(topImageResId, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(CharSequence text, int duration) {
        if (text == null || StringUtils.isEmpty(text.toString())) {
            return;
        }
        showToast(0, text, duration);
    }

    public static void showToast(int topImageResId, CharSequence text, int duration) {
        if (sToast == null) {
            initView();
        }
        if (topImageResId != 0) {
            mTopImageView.setImageResource(topImageResId);
            mTopImageView.setVisibility(View.VISIBLE);
        } else {
            mTopImageView.setVisibility(View.GONE);
        }
        mContentView.setText(text);
        sToast.setDuration(duration);
        sToast.show();
    }

    public static void makeText(Context context, CharSequence text) {
        showToast(text);
    }
}
