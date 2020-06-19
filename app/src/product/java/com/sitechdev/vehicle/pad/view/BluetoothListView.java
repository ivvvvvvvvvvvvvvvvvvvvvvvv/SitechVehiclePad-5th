package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ListView;

public class BluetoothListView extends RecyclerView {
    public BluetoothListView(Context context) {
        super(context);
    }

    public BluetoothListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
