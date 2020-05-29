package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sitechdev.vehicle.pad.R;

import java.util.ArrayList;


// 字母栏控件
public class ZSideBar extends View {
    private SimpleArrayMap<Integer, String> indexMap = new SimpleArrayMap<>();
    private RecyclerView recyclerView;
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private int offsetY;//item y间距
    private int offsetX = 40;//item x间距
    private int singleHeight;
    int bigTextSize = 32;

    private int color_text_common = R.color.kaola_page_index_uncheck;
    private int color_text_selected = R.color.kaola_page_index_checked;
    private ArrayList<Float> itemRight  = new ArrayList<>();
    public ZSideBar(Context context) {
        this(context, null);
    }

    public ZSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ZSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(0x00000000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((ViewGroup) getParent()).setClipChildren(false);
    }

    public void setupWithRecycler(RecyclerView recyclerView, int color_text_common, int color_text_checked) {
        this.color_text_common = color_text_common;
        this.color_text_selected = color_text_checked;
        setupWithRecycler(recyclerView);
    }
    boolean isHorizontal = false;
    public void setupWithRecycler(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("recyclerView do not set adapter");
        }
        if (!(adapter instanceof IndexAdapter)) {
            throw new IllegalArgumentException("recyclerView adapter not implement IndexAdapter");
        }

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

    public void setChoose(int choose) {
        this.choose = choose;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int oldChoose = choose;

        int checkIndex = -1;
        if (isHorizontal) {
            final float x = event.getX();// 点击x坐标
            for (int i = 0; i < itemRight.size(); i++) {
                if (x < itemRight.get(i)) {
                    checkIndex = i;
                    break;
                }
            }
        } else {
            final float y = event.getY();// 点击y坐标
            // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
            checkIndex = (int) ((y - offsetY) / singleHeight);
        }
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(0x00000000);
                break;

            default:
                setBackgroundColor(0x00000000);
                if (oldChoose != checkIndex) {
                    if (checkIndex >= 0 && checkIndex < indexMap.size()) {
                        int position = indexMap.keyAt(checkIndex);
                        recyclerView.getLayoutManager().scrollToPosition(position);
                        choose = checkIndex;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isHorizontal) {
            setMeasuredDimension((int) getContentwidth(), heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (indexMap.isEmpty()) return;

        int height = getHeight(); // 获取对应高度
        int width = getWidth();   // 获取对应宽度
        float widthTotal = 0f;

        itemRight.clear();
        if (!isHorizontal) {
            singleHeight = height / indexMap.size();// 获取每一个字母的高度
            singleHeight = singleHeight > bigTextSize ? bigTextSize : singleHeight;
            offsetY = (height - singleHeight * indexMap.size()) / 2;
        }

        for (int i = 0; i < indexMap.size(); i++) {
            paint.setAntiAlias(true);
            paint.setTextSize(bigTextSize);
            paint.setTypeface(Typeface.DEFAULT);
            int colorId = i == choose ? color_text_selected : color_text_common;
            paint.setColor(ContextCompat.getColor(getContext(), colorId));
            if (indexMap == null || TextUtils.isEmpty(indexMap.get(indexMap.keyAt(i)))) {
                return;
            }
            float xPos = 0;
            float yPos = 0;
            float offset_horizontal_text = 0;//make text gravity center
            if (isHorizontal) {
                float curOffset = i == 0 ? 0 : offsetX;
                xPos = widthTotal + curOffset;
                widthTotal = widthTotal + curOffset + paint.measureText(indexMap.get(indexMap.keyAt(i)));
                itemRight.add(widthTotal + offsetX / 2);//  【offsetX / 2】目的优化点及区域

                Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
                paint.getFontMetrics(fontMetrics);
                yPos = height / 2 + Math.abs(paint.ascent() + paint.descent()) / 2;
            } else {
                xPos = width / 2 - paint.measureText(indexMap.get(indexMap.keyAt(i))) / 2;
                yPos = offsetY + singleHeight * (i + 0.5F);
            }
            // x坐标等于中间-字符串宽度的一半.
            canvas.drawText(indexMap.get(indexMap.keyAt(i)), xPos, offset_horizontal_text, paint);
        }

    }

    private float getContentwidth() {
        if (isHorizontal) {
            float widthTotal = 0;
            for (int i = 0; i < indexMap.size(); i++) {
                float curOffset = i == 0 ? 0 : offsetX;
                widthTotal = widthTotal + curOffset + paint.measureText(indexMap.get(indexMap.keyAt(i)));
            }
            return widthTotal;
        } else {
            if (indexMap == null || TextUtils.isEmpty(indexMap.get(indexMap.keyAt(0)))) {
                return 0;
            }
            return paint.measureText(indexMap.get(indexMap.keyAt(0)));
        }
    }

    private void updateCheckSt(){
//        recyclerView.getLayoutManager()
//        choose = c;
        invalidate();
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

        //重绘
        invalidate();
    }

}
