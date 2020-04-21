package com.sitechdev.vehicle.pad.module.music.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.util.List;

/**
 * 三方音乐
 */
public class OtherMusicFragment extends Fragment implements View.OnClickListener {

    private Context context;

    public OtherMusicFragment() {
    }

    public static OtherMusicFragment newInstance() {
        OtherMusicFragment fragment = new OtherMusicFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_music_other,
                container, false);
        initListener(root);
        return root;
    }

    private void initListener(View root) {
        root.findViewById(R.id.other_music_qq).setOnClickListener(this);
        root.findViewById(R.id.other_music_wangyi).setOnClickListener(this);
        root.findViewById(R.id.other_music_xiami).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.other_music_qq:
                goQQMusic();
               break;
           case R.id.other_music_wangyi:
               goNetEaseMusic();
               break;
           case R.id.other_music_xiami:
               getXiamiMusic();
               break;
       }
    }

    private void goQQMusic() {
        AppUtil.goOtherActivity(context,"QQ音乐", "com.tencent.qqmusiccar",
                "com.tencent.qqmusiccar.app.activity.AppStarterActivity");
    }

    private void getXiamiMusic() {
        AppUtil.goOtherActivity(context,"虾米音乐", "fm.xiami.main",
                "fm.xiami.main.SplashActivity");
    }

    private void goNetEaseMusic() {
        AppUtil.goOtherActivity(context,"网易云音乐", "com.netease.cloudmusic",
                "com.netease.cloudmusic.activity.LoadingActivity");
    }
}
