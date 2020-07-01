package com.sitechdev.vehicle.pad.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.sitechdev.vehicle.pad.R;

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
        outRect.left = view.getContext().getResources().getInteger(R.integer.category_left_space);
        outRect.bottom = view.getContext().getResources().getInteger(R.integer.category_bottom_space);
        outRect.top = view.getContext().getResources().getInteger(R.integer.category_bottom_space);;
    }

}
