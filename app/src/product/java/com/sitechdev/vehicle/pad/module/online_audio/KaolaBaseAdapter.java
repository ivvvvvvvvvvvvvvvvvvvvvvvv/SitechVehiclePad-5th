package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/8
 * </pre>
 */
public abstract class KaolaBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private boolean isLandscape = true;
    private Context mContext = null;

    public boolean isLandscape() {
        return isLandscape;
    }

    KaolaBaseAdapter(Context context) {
        mContext = context;
        isLandscape = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
