package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author zhubaoqiang
 * @date 2019/8/24
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private Path mPath;
    //    private PathEffect mPathEffect;
    private RecyclerView.Adapter adapter;
    private int checkedClor;
    private int normalClor;

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, RecyclerView.Adapter adapter,
                              int dividerHeight, int dividerColor, int checkedColor) {
        this.adapter = adapter;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.normalClor = dividerColor;
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mPaint.setStrokeWidth(mDividerHeight);
//        mPathEffect = new DashPathEffect(new float[]{mDividerHeight * 2, mDividerHeight * 2},0);
        this.checkedClor = checkedColor;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int dividerHeight, int dividerColor) {
        this.adapter = adapter;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.normalClor = dividerColor;
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mPaint.setStrokeWidth(mDividerHeight);
    }


    //绘制分割线
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        drawHorizontal(c, parent);
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();//获取分割线的左边距，即RecyclerView的padding值
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();//分割线右边距
        final int childSize = parent.getChildCount();
        //遍历所有item view，为它们的下方绘制分割线
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int top = child.getBottom() + layoutParams.bottomMargin;
//            final int bottom = top + mDividerHeight;
            int y = child.getBottom() + layoutParams.bottomMargin + mDividerHeight / 2;
            if (mPaint != null) {
                mPath.reset();
                mPath.moveTo(left, y);
                mPath.lineTo(right, y);
//                int positon = adapter.getCheckedPositon();
//                if (positon >= 0 && positon == parent.getChildAdapterPosition(child)){
//                    mPaint.setStrokeWidth(mDividerHeight * 2);
//                    mPaint.setPathEffect(mPathEffect);
//                    mPaint.setColor(checkedClor);
//                    canvas.drawPath(mPath, mPaint);
//                    mPaint.setPathEffect(null);
//                    mPaint.setStrokeWidth(mDividerHeight);
//                    mPaint.setColor(normalClor);
//                }else {
//                    canvas.drawPath(mPath, mPaint);
//                }
                canvas.drawPath(mPath, mPaint);
            }
        }
    }
}
