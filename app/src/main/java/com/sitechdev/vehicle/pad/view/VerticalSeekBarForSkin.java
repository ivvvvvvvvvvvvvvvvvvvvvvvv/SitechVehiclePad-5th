package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;


public class VerticalSeekBarForSkin extends AppCompatSeekBar {// SkinCompatSeekBar {

    private float mTouchDownY;
    private boolean mIsDragging;
    private float mTouchProgressOffset;
    private int mScaledTouchSlop;

    public VerticalSeekBarForSkin(Context context) {
        super(context);
    }

    public VerticalSeekBarForSkin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBarForSkin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        this.setProgress(getProgress());
        canvas.rotate(-90);
        canvas.translate(-getHeight(), 0);
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(90);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer()) {
                    mTouchDownY = event.getY();
                } else {
                    setPressed(true);
                    invalidate();
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();
                    onSizeChanged(getWidth(), getHeight(), 0, 0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float y = event.getY();
                    if (Math.abs(y - mTouchDownY) > mScaledTouchSlop) {
                        setPressed(true);
                        invalidate();
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                    }
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                invalidate();
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int top = getPaddingTop();
        final int bottom = getPaddingBottom();
        final int available = height - top - bottom;
        int y = (int) event.getY();
        float scale;
        float progress = 0;
        if (y > height - bottom) {
            scale = 0.0f;
        } else if (y < top) {
            scale = 1.0f;
        } else {
            scale = (float) (available - y + top) / (float) available;
            progress = mTouchProgressOffset;
        }
        final int max = getMax();
        progress += scale * max;
        setProgress((int) progress);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mSeekChange != null) {
                mSeekChange.onStopTouchView(this);
                mSeekChange.onProgressChanged(this, (int) progress);
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mSeekChange != null) {
                mSeekChange.onProgressChanged(this, (int) progress);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mSeekChange != null) {
                mSeekChange.onStartTouchView(this);
            }
        }
    }

    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    private void attemptClaimDrag() {
        ViewParent p = getParent();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    private boolean isInScrollingContainer = false;

    public boolean isInScrollingContainer() {
        return isInScrollingContainer;
    }

    public onSeekBarChangeVertical mSeekChange;

    public void setOnSeekBarChangeVertical(onSeekBarChangeVertical msb) {
        mSeekChange = msb;
    }

    public interface onSeekBarChangeVertical {
        void onStopTouchView(VerticalSeekBarForSkin mSeekbar);

        void onProgressChanged(VerticalSeekBarForSkin seekBar, int progress);

        void onStartTouchView(VerticalSeekBarForSkin seekBar);
    }
}
