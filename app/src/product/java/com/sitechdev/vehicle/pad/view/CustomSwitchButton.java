package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;


/**
 * @author cold
 */
public class CustomSwitchButton extends LinearLayout {
    String TAG = "CustomSwitchButton";
    TextView mTitle, mTip;
    RadioGroup mRadioGroup;
    RadioButton mRadioButtonOn;
    RadioButton mRadioButtonOff;

    public CustomSwitchButton(Context context) {
        this(context, null);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_custom_switch, this);
        mTitle = inflate.findViewById(R.id.custom_switch_title);
        mTip = inflate.findViewById(R.id.custom_switch_tip);
        mRadioGroup = inflate.findViewById(R.id.custom_radiogroup);
        mRadioButtonOn = inflate.findViewById(R.id.custom_radiobutton_on);
        mRadioButtonOff = inflate.findViewById(R.id.custom_radiobutton_off);
        initData(context, attributeSet);
    }

    private void initData(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.custom_switch);
        String stringResult = a.getString(R.styleable.custom_switch_title);
        String off_title = a.getString(R.styleable.custom_switch_off_title);
        String on_title = a.getString(R.styleable.custom_switch_on_title);
        if (TextUtils.isEmpty(off_title)) {
            off_title = "OFF";
        }
        if (TextUtils.isEmpty(on_title)) {
            on_title = "ON";
        }
        mTitle.setText(stringResult);
        mRadioButtonOff.setText(off_title);
        mRadioButtonOn.setText(on_title);
        String tip = a.getString(R.styleable.custom_switch_tip);
        if (!TextUtils.isEmpty(tip)) {
            mTip.setText(tip);
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.custom_radiobutton_on: {
                        if (null != mCheckChangedListener) {
                            mCheckChangedListener.onSwithChecked(CustomSwitchButton.this.getId(),
                                    true);
                        }
                    }
                    break;
                    case R.id.custom_radiobutton_off: {
                        if (null != mCheckChangedListener) {
                            mCheckChangedListener.onSwithChecked(CustomSwitchButton.this.getId(),
                                    false);
                        }
                    }
                    break;
                    default: {

                    }
                }
            }
        });
    }


    OnSwitchCheckChangeListener mCheckChangedListener;

    public void setOnSwitchChangeListener(OnSwitchCheckChangeListener listener) {
        mCheckChangedListener = listener;
    }

    public interface OnSwitchCheckChangeListener {
        void onSwithChecked(int viewId, boolean isChecked);
    }

    public void setChecked(boolean checked) {
        if (checked) {
            mRadioButtonOn.setChecked(true);
        } else {
            mRadioButtonOff.setChecked(true);
        }
    }

    public boolean isChecked() {
        return mRadioButtonOn.isChecked();
    }

    public TextView getTipTextView() {
        return mTip;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mRadioButtonOff.setEnabled(enabled);
        mRadioButtonOn.setEnabled(enabled);
        mRadioGroup.setEnabled(enabled);
    }
}
