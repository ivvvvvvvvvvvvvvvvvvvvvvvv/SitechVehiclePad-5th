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
public class SpaceItemKaolaDialogItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpaceItemKaolaDialogItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space / 2;
        outRect.top = space / 2;
//        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
//        if (parent.getChildLayoutPosition(view) % 2 == 0) {
//            outRect.left = space;
//        }
    }

}
