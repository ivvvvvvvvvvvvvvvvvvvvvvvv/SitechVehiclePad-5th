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
        outRect.left = AdaptScreenUtils.px2Pt(view.getContext().getResources().getDimension(R.dimen.category_left_space));
        outRect.bottom = AdaptScreenUtils.px2Pt(view.getContext().getResources().getDimension(R.dimen.category_bottom_space));
        outRect.top = AdaptScreenUtils.px2Pt(view.getContext().getResources().getDimension(R.dimen.category_top_space));;
    }

}
