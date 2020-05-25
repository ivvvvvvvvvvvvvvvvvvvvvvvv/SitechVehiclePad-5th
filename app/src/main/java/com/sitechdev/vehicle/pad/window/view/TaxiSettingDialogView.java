package com.sitechdev.vehicle.pad.window.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.taxi.enums.TaxiParamsModel;

public class TaxiSettingDialogView extends Dialog implements View.OnClickListener {

    private final String TAG = "TaxiSettingDialogView";
    private View mDialogView;
    private EditText firstEdt, secondEdt, kmEdt = null;

    public TaxiSettingDialogView(Context context) {
        super(context, R.style.set_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public TaxiSettingDialogView(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        setContentView(R.layout.custom_taxi_setting_view);
        initView();

        initListener();

        initData();
    }
//
//    public View getContentView() {
//        return contentView;
//    }

    private void initView() {

        EditText firstEdt = mDialogView.findViewById(R.id.id_edt_title_km_content);
        EditText secondEdt = mDialogView.findViewById(R.id.id_edt_title_km_content_price);
        EditText kmEdt = mDialogView.findViewById(R.id.id_edt_title_single_km_content);

        LinearLayout saveBtn = findViewById(R.id.id_price_save_content);
        saveBtn.setOnClickListener(this);
    }

    private void initListener() {
    }

    private void initData() {
        firstEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String value = s.toString();
                    TaxiParamsModel.getInstance().setStartPrice_km(Double.parseDouble(value));
                }
            }
        });
        secondEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String value = s.toString();
                    TaxiParamsModel.getInstance().setStartPriceInKm(Double.parseDouble(value));
                }
            }
        });
        kmEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String value = s.toString();
                    TaxiParamsModel.getInstance().setSinglePriceKm(Double.parseDouble(value));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        boolean isActivated = false;
        switch (v.getId()) {
            case R.id.id_price_save_content:
                dismiss();
                break;
            default:
                break;
        }
    }
}
