package com.sitechdev.vehicle.pad.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.sitechdev.vehicle.lib.util.SitechDevLog;

/**
 * @author 邵志
 * @version 2020/05/29 0029 10:25
 * @name BaseDialog
 * @description
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public View mDialogView = null;

    /**
     * Creates a dialog window that uses the default dialog theme.
     * <p>
     * The supplied {@code context} is used to obtain the window manager and
     * base theme used to present the dialog.
     *
     * @param context the context in which the dialog should run
     * @see android.R.styleable#Theme_dialogTheme
     */
    public BaseDialog(@NonNull Context context) {
        super(context);
        SitechDevLog.i(TAG, "====>BaseDialog");
    }

    /**
     * Creates a dialog window that uses a custom dialog style.
     * <p>
     * The supplied {@code context} is used to obtain the window manager and
     * base theme used to present the dialog.
     * <p>
     * The supplied {@code theme} is applied on top of the context's theme. See
     * <a href="{@docRoot}guide/topics/resources/available-resources.html#stylesandthemes">
     * Style and Theme Resources</a> for more information about defining and
     * using styles.
     *
     * @param context    the context in which the dialog should run
     * @param themeResId a style resource describing the theme to use for the
     *                   window, or {@code 0} to use the default dialog theme
     */
    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        SitechDevLog.i(TAG, "====>BaseDialog 2");
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SitechDevLog.i(TAG, "====>onCreate");
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (getDialogContentView() != 0) {
            setContentView(getDialogContentView());
        }
    }

    /**
     * Dialog内容布局
     *
     * @return
     */
    public int getDialogContentView() {
        return 0;
    }

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
