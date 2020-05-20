package com.sitechdev.vehicle.pad.module.online_audio;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sitechdev.vehicle.pad.R;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaRecommendFrag extends Fragment {
    private View root;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.audio_kaola_info_frame, null);
//            initContentView();
        }
        return root;
    }
    private void initContentView() {


    }
}
