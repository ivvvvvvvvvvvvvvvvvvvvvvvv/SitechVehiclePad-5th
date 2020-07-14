package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.SitechDevLog;


public class ScrollTextView extends SkinTextView implements View.OnClickListener {

    private final String TAG = ScrollTextView.class.getSimpleName();
    private float textLength;//文本长度
    private float viewWidth;
    private float step;//文字的横坐标
    private float y;//文字的纵坐标
    public boolean isStarting = true;//是否开始滚动
    private Paint paint; //绘图样式
    private String text = "";//文本内容
    private int marqueeRepeatLimit = -1;
    private int currentRepeatLimit;

    public ScrollTextView(Context context) {
        super(context);
        init();
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = getPaint();
        paint.setColor(getCurrentTextColor());
        text = getText().toString();
        textLength = paint.measureText(text);
        y = getTextSize() + 5;
        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (textLength <= viewWidth) {
            isStarting = false;
            super.onDraw(canvas);
            return;
        }
        canvas.drawText(text, -step, y, paint);
        if (!isStarting) return;
        step += 1;//为文字滚动速度。
        if (step >= textLength) {
            step = -viewWidth;
            currentRepeatLimit++;
            SitechDevLog.d(TAG, "onDraw: currentRepeatLimit=" + currentRepeatLimit);
            if (currentRepeatLimit == marqueeRepeatLimit) {
                step = 0;
                isStarting = false;
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewWidth != View.MeasureSpec.getSize(widthMeasureSpec)) {
            viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        float width = getPaint().measureText(this.text);
        if (width != textLength) {
            textLength = width;
            if (!isStarting) {
                currentRepeatLimit = 0;
                isStarting = true;
                invalidate();
            }
        }
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        super.setText(text, type);
        if (text != null) {
            this.text = text.toString();
        }
        step = 0;
        isStarting = true;
        currentRepeatLimit = 0;
    }

    @Override
    public void onClick(View v) {
        if (textLength <= viewWidth) {
            return;
        }
        if (!isStarting) {
            currentRepeatLimit = 0;
            isStarting = true;
            invalidate();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        SitechDevLog.d(TAG, "onWindowFocusChanged: hasWindowFocus=" + hasWindowFocus);
//        if (!hasWindowFocus) {
//            step = 0;
//            isStarting = false;
//        }
    }

}