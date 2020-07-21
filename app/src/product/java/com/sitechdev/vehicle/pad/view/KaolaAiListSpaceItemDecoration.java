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
//                        Log.e("zyf", "mLists.get(position).column = " + mLists.get(position).tag);
                    } else {
//                        Log.e("zyf", "mLists == null");
                    }
                    outRect.left = 0;
                    outRect.bottom = 0;
                    outRect.top = 0;
                } else {
                    if (position == 0) {
                        outRect.left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_space_left);
                    } else {
                        outRect.left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_left);
                    }
                    outRect.bottom = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_top);
                    outRect.top = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_item_space_top);
                    if (position >= parent.getLayoutManager().getItemCount() - 1) {
                        outRect.right = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_land_space_left);
                    } else {
                        outRect.right = 0;
                    }
                }
            } else {
                int position = parent.getChildLayoutPosition(view);
                int top = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_top);
                outRect.bottom = top;
                outRect.top = top;
                int lookupsize = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanSize(position);
                if (lookupsize == 2) {
                    if (position % 3 == 0) {
                        if (mLists.get(position).column != null) {
                            Log.e("zyf", "3_3   " + mLists.get(position).column.getTitle());
                        }
                        int left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_left_span2_1);
                        outRect.left = left;
                    } else if (position % 3 == 1) {
                        if (mLists.get(position).column != null) {
                            Log.e("zyf", "3_2   " + mLists.get(position).column.getTitle());
                        }
                        int left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_left_span2_2);
                        outRect.left = left;
                    } else if (position % 3 == 2) {
                        if (mLists.get(position).column != null) {
                            Log.e("zyf", "3_1   " + mLists.get(position).column.getTitle());
                        }
                        int left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_left_span2_3);
                        outRect.left = left;
                    }
                } else if (lookupsize == 3) {
                    int left = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_left_span3_1);
                    int left2 = view.getContext().getResources().getInteger(R.integer.kaola_ai_list_item_space_left_span3_2);
//                    if (position == 0 || mLists.get(position - 1).column == null) {
//                        outRect.left = left;
//                    } else {
//                        outRect.left = left2;
//                    }

                    //逆序遍历到 group title index
                    int newPos = -1;
                    for (int i = position; i > 0; i--) {
                        if (mLists.get(i).column == null) {
                            newPos = i;
                            break;
                        }

                    }
                    List<KaolaDataWarpper> newList = mLists.subList(newPos+1, mLists.size());
                    //获得一个新的前面不包含 group title 数据的集合
                    //更新posotion
                    int newDataPos = -1;
                    for (int i = 0; i < newList.size(); i++) {
                        if (mLists.get(position).column.getCode().equals(newList.get(i).column.getCode())) {
                            newDataPos = i;
                            break;
                        }
                    }
                    if (newDataPos % 2 == 1) {
                        if (newList.get(newDataPos).column != null) {
                            Log.e("zyf", "2_1   " + newList.get(newDataPos).column.getTitle());
                        }
                        outRect.left = left2;
                    } else {
                        if (newList.get(newDataPos).column != null) {
                            Log.e("zyf", "2_2   " + newList.get(newDataPos).column.getTitle());
                        }
                        outRect.left = left;
                    }
                }

            }
        }

    }

}
