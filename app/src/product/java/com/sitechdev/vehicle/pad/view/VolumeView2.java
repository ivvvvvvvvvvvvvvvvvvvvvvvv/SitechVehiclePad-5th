package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.pad.vui.VUI;

public class VolumeView2 extends View {

    private static final String TAG = VolumeView2.class.getSimpleName();
    // small wave 是外面的小波，big wave 是里面的大波
    private static final int SMALL_WAVE_COLOR = 0x800B8AFD;//小波颜色--绿色--50%
    private static final int MIDDLE_WAVE_COLOR = 0x8032B883;//中波颜色--黄色--50%
    private static final int BIG_WAVE_COLOR = 0x80FFFFFF;//大波颜色--白色--50%
    private static final float SMALL_WAVE_SPEED = 0.6f;//小波速度
    private static final float MIDDLE_WAVE_SPEED = 1.0f;//中波速度
    private static final float BIG_WAVE_SPEED = 1.2f;//大波速度
    private static final int SMALL_WAVE_PERIOD = 3;//小波周期
    private static final int MIDDLE_WAVE_PERIOD = 4;//中波周期
    private static final int BIG_WAVE_PERIOD = 5;//大波周期
    private static final float BIG_SINE_UP_AND_DOWN_MOVE = 1.5f;//大波 上移动或者下移动的比值
    private static final float MIDDLE_SINE_UP_AND_DOWN_MOVE = 1.4f;//中波 上移动或者下移动的比值
    private static final float SMALL_SINE_UP_AND_DOWN_MOVE = 1.3f;//小波 上移动或者下移动的比值
    private static final float MAX_VALUE = 60;// 最大振幅对应的值。对应0到60分贝
    private static final float SMALL_AMPLITUDE_SIZE_SCALE = 8f;
    private static final float MIDDLE_AMPLITUDE_SIZE_SCALE = 7f;
    private static final float BIG_AMPLITUDE_SIZE_SCALE = 6f;
    private static final float MOVE_DISTACE = 5f;

    private Paint mPaint;
    private Path mPath;
    private float mDrawHeight;
    private float mDrawWidth;
    private float mViewHeight;
    private float mViewWidth;
    private float mCenterPointX;
    private float mCenterPointY;
    private double mPhase; // 相位
    private MoveThread mMoveThread;
    private float mMaxBigWaveAmplitude;
    private float mMaxMiddleWaveAmplitude;
    private float mMaxSmallWaveAmplitude;
    private float mVerticalSpeed; // (中间位置)垂直扩展时的移动速度，每秒移动像素数
    private float mVerticalRestoreSpeed; // (中间位置)垂直恢复时的移动速度，每秒移动像素数
    private float mMinAmplitude;// 最小振幅,这个可以手动修改。
    private float mVolumeAmplitude;

    public VolumeView2(Context context) {
        super(context);
        init();
    }

    public VolumeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VolumeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Style.FILL);

        mVerticalSpeed = DensityUtils.dp2px(0.1f);
        mVerticalRestoreSpeed = DensityUtils.dp2px(0.05f);
        mMinAmplitude = 0.2f;
        mVolumeAmplitude = mMinAmplitude;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        widthMeasureSpec = measureWidth(widthMeasureSpec);
//        heightMeasureSpec = measureHeight(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        if (mViewWidth == viewWidth && mViewHeight == viewHeight) {
            return;
        }
        mViewWidth = viewWidth / 3 * 2;
        mViewHeight = viewHeight / 3 * 2;
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        mDrawWidth = mViewWidth - paddingLeft - paddingRight;
        mDrawHeight = mViewHeight - paddingTop - paddingBottom;
        mCenterPointX = paddingLeft + mDrawWidth / 5 * 4;
        mCenterPointY = paddingTop + mDrawHeight * 2;
        mMaxSmallWaveAmplitude = mDrawHeight / SMALL_AMPLITUDE_SIZE_SCALE;
        mMaxBigWaveAmplitude = mDrawHeight / BIG_AMPLITUDE_SIZE_SCALE;
        mMaxMiddleWaveAmplitude = mDrawHeight / MIDDLE_AMPLITUDE_SIZE_SCALE;
    }

    private int measureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.UNSPECIFIED) {
            WindowManager wm = (WindowManager) getContext().getSystemService(
                    Context.WINDOW_SERVICE);
            @SuppressWarnings("deprecation")
            int width = wm.getDefaultDisplay().getWidth();
            spec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        } else if (mode == MeasureSpec.AT_MOST) {
            int value = MeasureSpec.getSize(spec);
            spec = MeasureSpec.makeMeasureSpec(value, MeasureSpec.EXACTLY);
        }
        return spec;
    }

    private int measureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.EXACTLY) {
            return spec;
        }
        // 其他模式下的最大高度
        int height = (int) DensityUtils.dp2px(50);
        if (mode == MeasureSpec.AT_MOST) {
            int preValue = MeasureSpec.getSize(spec);
            if (preValue < height) {
                height = preValue;
            }
        }
        spec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        return spec;
    }

    private double sine(float x, int period, float drawWidth, double phase) {
        return Math.sin(2 * Math.PI * period * (x + phase) / drawWidth);
    }

    private void drawSine(Canvas canvas, Path path, Paint paint, int period,
                          float drawWidth, float amplitude, double phase, float upAndDownScale) {
        float halfDrawWidth = drawWidth / 2f;
        path.reset();
        path.moveTo(-halfDrawWidth, 0);
        float y;
        double scaling;
//        for (float x = -halfDrawWidth; x <= halfDrawWidth; x++) {
//            scaling = 1 - Math.pow(x / halfDrawWidth, 2);// 对y进行缩放
//            y = (float) ((sine(x, period, drawWidth, phase) + upAndDownScale)
//                    * amplitude * (1) * Math.pow(scaling, 3));
//            path.lineTo(x, y);
//        }

        for (float x = halfDrawWidth; x >= -halfDrawWidth; x--) {
            scaling = 1 - Math.pow(x / halfDrawWidth, 2);// 对y进行缩放
            y = (float) ((sine(x, period, drawWidth, phase) + upAndDownScale)
                    * amplitude * (-1) * Math.pow(scaling, 3));
            path.lineTo(x, y * 3);
        }

        for (float x = halfDrawWidth; x >= -halfDrawWidth; x--) {
            scaling = 1 - Math.pow(x / halfDrawWidth, 2);// 对y进行缩放
            y = (float) ((sine(x, period, drawWidth, phase) + upAndDownScale)
                    * amplitude * Math.pow(scaling, 3));
            path.lineTo(x, y * 3);
        }
        canvas.drawPath(path, paint);
        canvas.save();
        canvas.restore();
    }

    private void drawWave(Canvas canvas, Path path, Paint paint, int period,
                          float drawWidth, double phase) {
        paint.setColor(BIG_WAVE_COLOR);
        drawSine(canvas, mPath, mPaint, BIG_WAVE_PERIOD, mDrawWidth,
                mVolumeAmplitude * mMaxBigWaveAmplitude, mPhase
                        * BIG_WAVE_SPEED, BIG_SINE_UP_AND_DOWN_MOVE);
        paint.setColor(MIDDLE_WAVE_COLOR);
        drawSine(canvas, mPath, mPaint, MIDDLE_WAVE_PERIOD, mDrawWidth,
                mVolumeAmplitude * mMaxMiddleWaveAmplitude, mPhase
                        * MIDDLE_WAVE_SPEED, MIDDLE_SINE_UP_AND_DOWN_MOVE);
        paint.setColor(SMALL_WAVE_COLOR);
        drawSine(canvas, mPath, mPaint, SMALL_WAVE_PERIOD, mDrawWidth,
                mVolumeAmplitude * mMaxSmallWaveAmplitude, mPhase
                        * SMALL_WAVE_SPEED, SMALL_SINE_UP_AND_DOWN_MOVE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mCenterPointX, mDrawHeight / 2);
        drawWave(canvas, mPath, mPaint, BIG_WAVE_PERIOD, mDrawWidth, mPhase);
    }

    private class MoveThread extends Thread {
        private static final int MOVE_STOP = 1;
        private static final int MOVE_START = 0;
        private int state;
        private long amplitudeSetTime;
        private float lastAmplitude;
        private float nextTargetAmplitude;
        private long curTime;
        private long lastTime;

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            mPhase = 0;
            state = MOVE_START;
            lastTime = System.currentTimeMillis();
            amplitudeSetTime = lastTime;
            lastAmplitude = mVolumeAmplitude;
            nextTargetAmplitude = lastAmplitude;
            while (true) {
                if (state == MOVE_STOP) {
                    return;
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                }
                curTime = System.currentTimeMillis();
                mPhase -= MOVE_DISTACE;
                lastTime = curTime;
//                mVolumeAmplitude = currentVolumeAmplitude(curTime);
                postInvalidate();
            }
        }

        public void stopRunning() {
            state = MOVE_STOP;
        }

        // 计算当前时间下的振幅
        private float currentVolumeAmplitude(long curTime) {
            if (lastAmplitude == nextTargetAmplitude) {
                return nextTargetAmplitude;
            }
            if (curTime == amplitudeSetTime) {
                return lastAmplitude;
            }
            // 走设置流程，改变速度快
            if (nextTargetAmplitude > lastAmplitude) {
                float target = lastAmplitude + mVerticalSpeed
                        * (curTime - amplitudeSetTime) / 1000;
                if (target >= nextTargetAmplitude) {
                    target = nextTargetAmplitude;
                    lastAmplitude = nextTargetAmplitude;
                    amplitudeSetTime = curTime;
                    nextTargetAmplitude = mMinAmplitude;
                }
                return target;
            }

            // 走恢复流程，改变速度慢
            if (nextTargetAmplitude < lastAmplitude) {
                float target = lastAmplitude - mVerticalRestoreSpeed
                        * (curTime - amplitudeSetTime) / 1000;
                if (target <= nextTargetAmplitude) {
                    target = nextTargetAmplitude;
                    lastAmplitude = nextTargetAmplitude;
                    amplitudeSetTime = curTime;
                    nextTargetAmplitude = mMinAmplitude;
                }
                return target;
            }

            return mMinAmplitude;
        }

        public void setAmplitude(float amplitude) {
//            long curTime = System.currentTimeMillis();
//            lastAmplitude = currentVolumeAmplitude(curTime);
//            DL.i(TAG, "lastAmplitude===>" + lastAmplitude);
//            amplitudeSetTime = curTime;
//            nextTargetAmplitude = amplitude;
            mVolumeAmplitude = amplitude;
        }
    }

    public void start() {
        mPhase = 0;
        invalidate();
        if (mMoveThread != null) {
            return;
//            mMoveThread.stopRunning();
//            mMoveThread = null;
        }
        mMoveThread = new MoveThread();
        mMoveThread.start();
    }

    // stop volume animation
    public void stop() {
        if (mMoveThread != null) {
            mMoveThread.stopRunning();
            mMoveThread = null;
        }
    }

    // set volume value; 将音量转化成不同的振幅，其中有最大振幅和最小振幅.将音量对应成1到100
    public void setVolume(float volume) {
        VUI.log("------------>" + volume);
        if (Float.compare(volume, 0) < 0
                || Float.compare(volume, MAX_VALUE) > 0) {
            return;
        }
//        DL.i(TAG, "setVolume===>" + volume);
        float amplitude = volume / MAX_VALUE;
        Log.i(TAG, "amplitude===>" + amplitude);
        if (amplitude < mMinAmplitude) {
            amplitude = mMinAmplitude;
        } else if (amplitude > 1.0f) {
            amplitude = 1.0f;
        }
        if (mMoveThread != null) {
            mMoveThread.setAmplitude(amplitude);
        }
    }
}
