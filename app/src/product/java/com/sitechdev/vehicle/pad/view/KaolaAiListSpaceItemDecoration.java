package com.sitechdev.vehicle.pad.view;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.model.kaola.KaolaDataWarpper;
import com.sitechdev.vehicle.pad.module.online_audio.KaolaAIListAdapter;

import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/26
 * </pre>
 */
public class KaolaAiListSpaceItemDecoration extends RecyclerView.ItemDecoration {
    List<KaolaDataWarpper> mLists;

    public KaolaAiListSpaceItemDecoration(List<KaolaDataWarpper> mLists) {
        setData(mLists);
    }

    public void setData(List<KaolaDataWarpper> mLists) {
        this.mLists = mLists;
    }

    @SuppressLint("ResourceType")
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager && view.getContext() instanceof BaseActivity) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (((BaseActivity) view.getContext()).isLandscape()) {
                int position = parent.getChildLayoutPosition(view);
                if (KaolaAIListAdapter.VIEWHOLDER_TAG.equals(view.getTag())) {
                    if (mLists != null) {
                        Log.e("zyf", "mLists.get(position).column = " + mLists.get(position).tag);
                    } else {
                        Log.e("zyf", "mLists == null");
                    }
                    outRect.left = 0;
                    outRect.bottom = 0;
                    outRect.top = 0;
                } else {
                    outRect.left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_left);
                    outRect.bottom = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_top);
                    outRect.top = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_top);
                }
            } else {
                int position = parent.getChildLayoutPosition(view);
                Log.e("zyf","mLists.get(position).column = "+mLists.get(position).tag);
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
