package com.sitechdev.vehicle.pad.vui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.view.VolumeView;

import org.json.JSONObject;

/**
 * @author zhubaoqiang
 * @date 2019/8/15
 */
public class VUIDialogFragment extends DialogFragment implements
        VUI.OnVolumeChangeListener, View.OnClickListener {

    private FrameLayout mHolderContainer;
    private VUIHolder cueertHolder;
    private ChatHolder chatHolder;

    public static VUIDialogFragment newInstance(Bundle bundle){
        VUIDialogFragment fragment = new VUIDialogFragment();
        if (null != bundle){
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diaog_vui, container, false);
        mHolderContainer = view.findViewById(R.id.vui_dialog_holder_container);
        if (null == chatHolder){
            chatHolder = new ChatHolder(inflater);
        }
        cueertHolder = chatHolder.added(mHolderContainer, cueertHolder);
        view.findViewById(R.id.vui_dialog_close).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.windowAnimations = R.style.BottomDialogAnimation;
        params.gravity = Gravity.BOTTOM;
//        window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),
//                R.drawable.bg_common_btn_pf));
        window.setWindowAnimations(R.style.BottomAnimation);
        window.setAttributes(params);
        //设置边距
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void show(FragmentManager manager){
        FragmentTransaction ft = manager.beginTransaction();
        if (this.isAdded()) {
            ft.remove(this).commit();
        }
        ft.add(this, String.valueOf(System.currentTimeMillis()));
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onVolumeChange(int value) {
//        volumeView.setVisibility(View.VISIBLE);
//        volumeView.start();
//        volumeView.setVolume(value * 8);
        if (null != chatHolder){
            chatHolder.getVolumeView().setVolume(value * 8);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vui_dialog_close:
                dismiss();
                break;
        }
    }
    public void showWeather(JSONObject weather){
        WeatherHolder holder = new WeatherHolder(getContext());
        cueertHolder = holder.added(mHolderContainer, cueertHolder);
        holder.adapter(weather);
    }
}
