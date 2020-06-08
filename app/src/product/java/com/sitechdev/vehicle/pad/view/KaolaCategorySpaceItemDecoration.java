package com.sitechdev.vehicle.pad.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/26
 * </pre>
 */
public class KaolaCategorySpaceItemDecoration extends RecyclerView.ItemDecoration {

    public KaolaCategorySpaceItemDecoration() {
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 80;
        outRect.bottom = 15;
        outRect.top = 15;
    }

}
