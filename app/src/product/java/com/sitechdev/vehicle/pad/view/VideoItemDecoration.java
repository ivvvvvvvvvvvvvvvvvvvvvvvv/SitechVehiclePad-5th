package com.sitechdev.vehicle.pad.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/26
 * </pre>
 */
public class VideoItemDecoration extends RecyclerView.ItemDecoration {

    public VideoItemDecoration() {
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager && view.getContext() instanceof BaseActivity) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (((BaseActivity) view.getContext()).isLandscape()) {
                outRect.left = view.getContext().getResources().getInteger(R.integer.video_list_land_left_space);
                outRect.top = view.getContext().getResources().getInteger(R.integer.video_list_land_top_space);
            } else {
                outRect.left = view.getContext().getResources().getInteger(R.integer.video_list_left_space);
                outRect.top = view.getContext().getResources().getInteger(R.integer.video_list_top_space);
            }
        }
    }
}
