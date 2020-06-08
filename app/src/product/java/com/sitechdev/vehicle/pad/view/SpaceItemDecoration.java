package com.sitechdev.vehicle.pad.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sitechdev.vehicle.pad.app.BaseActivity;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/26
 * </pre>
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager && view.getContext() instanceof BaseActivity) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (((BaseActivity) view.getContext()).isLandscape()) {
                outRect.left = 60;
                outRect.bottom = 15;
                outRect.top = 15;
            } else {
                int position = parent.getChildLayoutPosition(view);
                int lookupsize = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanSize(position);
                if (lookupsize == 2) {
                    if (position % 3 == 2) {
                        outRect.left = 37;
                    } else if (position % 3 == 1) {
                        outRect.left = 40;
                    } else if (position % 3 == 0) {
                        outRect.left = 45;
                    }
                }
                if (lookupsize == 3) {
                    if (position % 2 == 1) {
                        outRect.left = 45;
                    } else {
                        outRect.left = 45;
                    }
                }
                outRect.bottom = 20;
                outRect.top = 20;
            }
        }

    }

}
