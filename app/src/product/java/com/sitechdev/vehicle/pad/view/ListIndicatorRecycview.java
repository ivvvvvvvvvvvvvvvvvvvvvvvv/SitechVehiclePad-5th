package com.sitechdev.vehicle.pad.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/25
 * </pre>
 */
public class ListIndicatorRecycview extends RecyclerView {
    private LinkedHashMap<Integer, String> indexMap = new LinkedHashMap<>();
    private RecyclerView recyclerView;
    private int curChoosed = -1;// 选中

    public ListIndicatorRecycview(@NonNull Context context) {
        super(context);
    }

    public ListIndicatorRecycview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListIndicatorRecycview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setChoose(int choose) {
        this.curChoosed = choose;
        if (indexMap.size() == 0) {
            return;
        }
        if (initWithData != null) {
            notifyIndicatorAdapter(tabCheckClick);
        } else {
            notifyIndicatorAdapter(oneListItemCheck);
        }
    }

    private List<Indexable> initWithData;

    /**
     *  标签切换类型   目标是一个多个tab 点击标签进行tab切换
     * @param indexables
     */

    public void setupWithData(List<Indexable> indexables, OnItemClickListener listener) {
        for (int i = 0; i < indexables.size(); i++) {
            Indexable item = indexables.get(i);
            if (item == null) {
                continue;
            }
            indexMap.put(i, item.getIndex());
        }
        initWithData = indexables;
        tabCheckClick = listener;
        notifyIndicatorAdapter(tabCheckClick);
    }
    /**
     *  标签指示类型 目标是一个recycleview 点击标签进行滑动
     * @param recyclerView
     */
    public void setupWithRecycler(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("recyclerView do not set adapter");
        }
        if (!(adapter instanceof IndexAdapter)) {
            throw new IllegalArgumentException("recyclerView adapter not implement IndexAdapter");
        }
        boolean isHorizontal;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            isHorizontal = linearLayoutManager.canScrollHorizontally();
        }
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            isHorizontal = gridLayoutManager.canScrollHorizontally();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    updateCheckSt();
                }
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                initIndex(adapter);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                initIndex(adapter);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                initIndex(adapter);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                initIndex(adapter);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                initIndex(adapter);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                initIndex(adapter);
            }
        });
        initIndex(adapter);
    }

    private void initIndex(@NonNull RecyclerView.Adapter adapter) {
        if (adapter == null || adapter.getItemCount() <= 0) {
            return;
        }
        indexMap.clear();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Indexable item = ((IndexAdapter) adapter).getItem(i);
            if (item == null) {
                return;
            }
            if (i == 0) {
                indexMap.put(i, item.getIndex());
            } else {
                Indexable preItem = ((IndexAdapter) adapter).getItem(i - 1);
                if (null == preItem || null == item || TextUtils.isEmpty(preItem.getIndex())) {
                    continue;
                }
                if (!preItem.getIndex().equals(item.getIndex())) {
                    indexMap.put(i, item.getIndex());
                }
            }
        }

        notifyIndicatorAdapter(oneListItemCheck);

    }

    private void updateCheckSt() {
//        recyclerView.getLayoutManager()
//        choose = c;
        notifyIndicatorAdapter(oneListItemCheck);
    }

    private ListIndicatorAdapter indicatorAdapter;

    private OnItemClickListener oneListItemCheck = new OnItemClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && recyclerView != null) {
                try {
                    for (Integer key : indexMap.keySet()) {
                        if (indexMap.get(key).equals(((TextView) v).getText())) {
                            recyclerView.scrollToPosition(key);
                            GridLayoutManager mLayoutManager =
                                    (GridLayoutManager) recyclerView.getLayoutManager();
                            mLayoutManager.scrollToPositionWithOffset(key, 0);
                        }
                    }
                    curChoosed = (int) v.getTag();
                    indicatorAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        }
    };

    private OnItemClickListener tabCheckClick = new OnItemClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null ) {

            }
        }
    };

    private void notifyIndicatorAdapter(OnItemClickListener onItemClickListener) {
        if (indicatorAdapter == null) {
            indicatorAdapter = new ListIndicatorAdapter(getContext(), indexMap);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            setLayoutManager(linearLayoutManager);
            setAdapter(indicatorAdapter);
            indicatorAdapter.setOnItemClickListener(onItemClickListener);
        } else {
            indicatorAdapter.notifyDataSetChanged();
        }
    }

    class ListIndicatorAdapter extends RecyclerView.Adapter<ListIndicatorAdapter.VHolder>  {
        private Context context;
        private List<String> mLists;

        // 构造方法
        ListIndicatorAdapter(Context context, @NotNull HashMap<Integer, String> map) {
            this.context = context;
            if (mLists == null) {
                mLists = new ArrayList<>();
            } else {
                mLists.clear();
            }
            Iterator it1 = map.values().iterator();
            while (it1.hasNext()) {
                mLists.add((String) it1.next());
            }
        }

        public void setDataAndNotify(@NotNull HashMap<Integer,String> map) {
            if (mLists == null) {
                mLists = new ArrayList<>();
            } else {
                mLists.clear();
            }
            map = map == null ? new HashMap<>() : map;
            Iterator it1 = map.values().iterator();
            while (it1.hasNext()) {
                mLists.add((String) it1.next());
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mLists.size();
        }

        @NonNull
        @Override
        public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new VHolder(LayoutInflater.from(context).inflate(R.layout.kaola_item_page_indicator, null));
        }

        @Override
        public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
            vHolder.nameText.setTag(i);
            vHolder.nameText.setText(mLists.get(i));
            vHolder.nameText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onClick(v);
                    }
                }
            });
            vHolder.nameText.clearAnimation();
            if (curChoosed == i) {
                selectTabAni(vHolder.nameText);
                vHolder.nameText.setTextColor(getResources().getColor(color_text_selected));
            } else {

                vHolder.nameText.setTextColor(getResources().getColor(color_text_common));
            }
        }

        private void selectTabAni(View view) {
            if (null != view) {
                ValueAnimator animx = ObjectAnimator
                        .ofFloat(view, "ScaleX", 1.0F, 1.1F)
                        .setDuration(100);
                ValueAnimator animy = ObjectAnimator
                        .ofFloat(view, "ScaleY", 1.0F, 1.1F)
                        .setDuration(100);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(animx, animy);
                set.setTarget(view);
                animx.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        view.setScaleX(1.1f);
                        view.setScaleY(1.1f);
                    }
                });
                set.start();
            }
        }


        class VHolder extends RecyclerView.ViewHolder {

            TextView nameText;

            VHolder(View itemView) {
                super(itemView);
                nameText = (TextView) itemView.findViewById(R.id.text);
                nameText.setGravity(Gravity.CENTER);
                nameText.setPadding(50, 20, 50, 20);
            }
        }

        OnItemClickListener onItemClickListener ;
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    int bigTextSize = 24;
    int smallTextSize = 20;
    private int color_text_common = R.color.kaola_page_index_uncheck;
    private int color_text_selected = R.color.kaola_page_index_checked;
    public interface OnItemClickListener{
        void onClick(View v);
    }
}


