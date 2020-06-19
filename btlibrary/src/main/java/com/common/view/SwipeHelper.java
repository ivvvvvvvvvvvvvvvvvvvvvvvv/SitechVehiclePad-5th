//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

public class SwipeHelper {
    static final float ALPHA_FADE_END = 0.5F;
    public static float ALPHA_FADE_START = 0.0F;
    private static final boolean CONSTRAIN_SWIPE = true;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_INVALIDATE = false;
    private static final boolean DISMISS_IF_SWIPED_FAR_ENOUGH = true;
    private static final boolean FADE_OUT_DURING_SWIPE = true;
    private static final boolean SLOW_ANIMATIONS = false;
    private static final int SNAP_ANIM_LEN = 150;
    static final String TAG = "com.android.systemui.SwipeHelper";
    public static final int X = 0;
    public static final int Y = 1;
    private static LinearInterpolator sLinearInterpolator = new LinearInterpolator();
    private int DEFAULT_ESCAPE_ANIMATION_DURATION = 200;
    private int MAX_DISMISS_VELOCITY = 2000;
    private int MAX_ESCAPE_ANIMATION_DURATION = 400;
    private float SWIPE_ESCAPE_VELOCITY = 100.0F;
    private SwipeHelper.Callback mCallback;
    private boolean mCanCurrViewBeDimissed;
    private View mCurrAnimView;
    private View mCurrView;
    private float mDensityScale;
    private boolean mDragging;
    private Handler mHandler;
    private float mInitialTouchPos;
    private OnLongClickListener mLongPressListener;
    private boolean mLongPressSent;
    private long mLongPressTimeout;
    private float mMinAlpha = 0.0F;
    private float mPagingTouchSlop;
    private int mSwipeDirection;
    private VelocityTracker mVelocityTracker;
    private Runnable mWatchLongPress;

    public SwipeHelper(int var1, SwipeHelper.Callback var2, float var3, float var4) {
        this.mCallback = var2;
        this.mHandler = new Handler();
        this.mSwipeDirection = var1;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mDensityScale = var3;
        this.mPagingTouchSlop = var4;
        this.mLongPressTimeout = (long)((float)ViewConfiguration.getLongPressTimeout() * 1.5F);
    }

    private ObjectAnimator createTranslationAnimation(View var1, float var2) {
        String var3;
        if (this.mSwipeDirection == 0) {
            var3 = "translationX";
        } else {
            var3 = "translationY";
        }

        return ObjectAnimator.ofFloat(var1, var3, new float[]{var2});
    }

    private float getAlphaForOffset(View var1) {
        float var3 = this.getSize(var1);
        float var4 = 0.5F * var3;
        float var2 = 1.0F;
        float var5 = this.getTranslation(var1);
        if (var5 >= ALPHA_FADE_START * var3) {
            var2 = 1.0F - (var5 - ALPHA_FADE_START * var3) / var4;
        } else if (var5 < (1.0F - ALPHA_FADE_START) * var3) {
            var2 = 1.0F + (ALPHA_FADE_START * var3 + var5) / var4;
        }

        return Math.max(this.mMinAlpha, var2);
    }

    private float getPerpendicularVelocity(VelocityTracker var1) {
        return this.mSwipeDirection == 0 ? var1.getYVelocity() : var1.getXVelocity();
    }

    private float getPos(MotionEvent var1) {
        return this.mSwipeDirection == 0 ? var1.getX() : var1.getY();
    }

    private float getSize(View var1) {
        return this.mSwipeDirection == 0 ? (float)var1.getMeasuredWidth() : (float)var1.getMeasuredHeight();
    }

    private float getTranslation(View var1) {
        return this.mSwipeDirection == 0 ? var1.getTranslationX() : var1.getTranslationY();
    }

    private float getVelocity(VelocityTracker var1) {
        return this.mSwipeDirection == 0 ? var1.getXVelocity() : var1.getYVelocity();
    }

    public static void invalidateGlobalRegion(View var0) {
        invalidateGlobalRegion(var0, new RectF((float)var0.getLeft(), (float)var0.getTop(), (float)var0.getRight(), (float)var0.getBottom()));
    }

    public static void invalidateGlobalRegion(View var0, RectF var1) {
        while(var0.getParent() != null && var0.getParent() instanceof View) {
            var0 = (View)var0.getParent();
            var0.getMatrix().mapRect(var1);
            var0.invalidate((int)Math.floor((double)var1.left), (int)Math.floor((double)var1.top), (int)Math.ceil((double)var1.right), (int)Math.ceil((double)var1.bottom));
        }

    }

    private void setTranslation(View var1, float var2) {
        if (this.mSwipeDirection == 0) {
            var1.setTranslationX(var2);
        } else {
            var1.setTranslationY(var2);
        }
    }

    private void updateAlphaFromOffset(View var1, boolean var2) {
        if (var2) {
            float var3 = this.getAlphaForOffset(var1);
            if (var3 != 0.0F && var3 != 1.0F) {
                var1.setLayerType(2, (Paint)null);
            } else {
                var1.setLayerType(0, (Paint)null);
            }

            var1.setAlpha(this.getAlphaForOffset(var1));
        }

        invalidateGlobalRegion(var1);
    }

    public void dismissChild(final View var1, float var2) {
        final View var6 = this.mCallback.getChildContentView(var1);
        final boolean var5 = this.mCallback.canChildBeDismissed(var1);
        float var3;
        if (var2 < 0.0F || var2 == 0.0F && this.getTranslation(var6) < 0.0F || var2 == 0.0F && this.getTranslation(var6) == 0.0F && this.mSwipeDirection == 1) {
            var3 = -this.getSize(var6);
        } else {
            var3 = this.getSize(var6);
        }

        int var4 = this.MAX_ESCAPE_ANIMATION_DURATION;
        if (var2 != 0.0F) {
            var4 = Math.min(var4, (int)(Math.abs(var3 - this.getTranslation(var6)) * 1000.0F / Math.abs(var2)));
        } else {
            var4 = this.DEFAULT_ESCAPE_ANIMATION_DURATION;
        }

        var6.setLayerType(2, (Paint)null);
        ObjectAnimator var7 = this.createTranslationAnimation(var6, var3);
        var7.setInterpolator(sLinearInterpolator);
        var7.setDuration((long)var4);
        var7.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
                SwipeHelper.this.mCallback.onChildDismissed(var1);
                var6.setLayerType(0, (Paint)null);
            }
        });
        var7.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1) {
                SwipeHelper.this.updateAlphaFromOffset(var6, var5);
            }
        });
        var7.start();
    }

    public boolean onInterceptTouchEvent(MotionEvent var1) {
        switch(var1.getAction()) {
            case 0:
                this.mDragging = false;
                this.mLongPressSent = false;
                this.mCurrView = this.mCallback.getChildAtPosition(var1);
                this.mVelocityTracker.clear();
                if (this.mCurrView != null) {
                    this.mCurrAnimView = this.mCallback.getChildContentView(this.mCurrView);
                    this.mCanCurrViewBeDimissed = this.mCallback.canChildBeDismissed(this.mCurrView);
                    this.mVelocityTracker.addMovement(var1);
                    this.mInitialTouchPos = this.getPos(var1);
                    if (this.mLongPressListener != null) {
                        if (this.mWatchLongPress == null) {
                            this.mWatchLongPress = new Runnable() {
                                public void run() {
                                    if (SwipeHelper.this.mCurrView != null && !SwipeHelper.this.mLongPressSent) {
                                        SwipeHelper.this.mLongPressSent = true;
                                        SwipeHelper.this.mCurrView.sendAccessibilityEvent(2);
                                        SwipeHelper.this.mLongPressListener.onLongClick(SwipeHelper.this.mCurrView);
                                    }

                                }
                            };
                        }

                        this.mHandler.postDelayed(this.mWatchLongPress, this.mLongPressTimeout);
                    }
                }
                break;
            case 1:
            case 3:
                this.mDragging = false;
                this.mCurrView = null;
                this.mCurrAnimView = null;
                this.mLongPressSent = false;
                this.removeLongPressCallback();
                break;
            case 2:
                if (this.mCurrView != null && !this.mLongPressSent) {
                    this.mVelocityTracker.addMovement(var1);
                    if (Math.abs(this.getPos(var1) - this.mInitialTouchPos) > this.mPagingTouchSlop) {
                        this.mCallback.onBeginDrag(this.mCurrView);
                        this.mDragging = true;
                        this.mInitialTouchPos = this.getPos(var1) - this.getTranslation(this.mCurrAnimView);
                        this.removeLongPressCallback();
                    }
                }
        }

        return this.mDragging;
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if (this.mLongPressSent) {
            return true;
        } else {
            boolean var11 = this.mDragging;
            boolean var10 = false;
            if (!var11) {
                this.removeLongPressCallback();
                return false;
            } else {
                this.mVelocityTracker.addMovement(var1);
                int var7 = var1.getAction();
                float var2 = 0.0F;
                float var3;
                float var4;
                switch(var7) {
                    case 1:
                    case 3:
                        if (this.mCurrView != null) {
                            var3 = (float)this.MAX_DISMISS_VELOCITY;
                            var4 = this.mDensityScale;
                            this.mVelocityTracker.computeCurrentVelocity(1000, var3 * var4);
                            var4 = this.SWIPE_ESCAPE_VELOCITY;
                            float var5 = this.mDensityScale;
                            var3 = this.getVelocity(this.mVelocityTracker);
                            float var6 = this.getPerpendicularVelocity(this.mVelocityTracker);
                            boolean var13;
                            if ((double)Math.abs(this.getTranslation(this.mCurrAnimView)) > 0.4D * (double)this.getSize(this.mCurrAnimView)) {
                                var13 = true;
                            } else {
                                var13 = false;
                            }

                            boolean var8;
                            boolean var9;
                            label70: {
                                if (Math.abs(var3) > var4 * var5 && Math.abs(var3) > Math.abs(var6)) {
                                    if (var3 > 0.0F) {
                                        var8 = true;
                                    } else {
                                        var8 = false;
                                    }

                                    if (this.getTranslation(this.mCurrAnimView) > 0.0F) {
                                        var9 = true;
                                    } else {
                                        var9 = false;
                                    }

                                    if (var8 == var9) {
                                        var8 = true;
                                        break label70;
                                    }
                                }

                                var8 = false;
                            }

                            var9 = var10;
                            if (this.mCallback.canChildBeDismissed(this.mCurrView)) {
                                label85: {
                                    if (!var8) {
                                        var9 = var10;
                                        if (!var13) {
                                            break label85;
                                        }
                                    }

                                    var9 = true;
                                }
                            }

                            if (var9) {
                                View var12 = this.mCurrView;
                                if (var8) {
                                    var2 = var3;
                                }

                                this.dismissChild(var12, var2);
                                return true;
                            }

                            this.mCallback.onDragCancelled(this.mCurrView);
                            this.snapChild(this.mCurrView, var3);
                        }
                        break;
                    case 2:
                    case 4:
                        if (this.mCurrView != null) {
                            var3 = this.getPos(var1) - this.mInitialTouchPos;
                            var2 = var3;
                            if (!this.mCallback.canChildBeDismissed(this.mCurrView)) {
                                var4 = this.getSize(this.mCurrAnimView);
                                var2 = 0.15F * var4;
                                if (Math.abs(var3) >= var4) {
                                    if (var3 <= 0.0F) {
                                        var2 = -var2;
                                    }
                                } else {
                                    var2 *= (float)Math.sin((double)(var3 / var4) * 1.5707963267948966D);
                                }
                            }

                            this.setTranslation(this.mCurrAnimView, var2);
                            this.updateAlphaFromOffset(this.mCurrAnimView, this.mCanCurrViewBeDimissed);
                            return true;
                        }
                        break;
                    default:
                        return true;
                }

                return true;
            }
        }
    }

    public void removeLongPressCallback() {
        if (this.mWatchLongPress != null) {
            this.mHandler.removeCallbacks(this.mWatchLongPress);
            this.mWatchLongPress = null;
        }

    }

    public void setDensityScale(float var1) {
        this.mDensityScale = var1;
    }

    public void setLongPressListener(OnLongClickListener var1) {
        this.mLongPressListener = var1;
    }

    public void setMinAlpha(float var1) {
        this.mMinAlpha = var1;
    }

    public void setPagingTouchSlop(float var1) {
        this.mPagingTouchSlop = var1;
    }

    public void snapChild(final View var1, float var2) {
//        var1 = this.mCallback.getChildContentView(var1);
        final boolean var3 = this.mCallback.canChildBeDismissed(var1);
        ObjectAnimator var4 = this.createTranslationAnimation(var1, 0.0F);
        var4.setDuration((long)150);
        var4.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1x) {
                SwipeHelper.this.updateAlphaFromOffset(var1, var3);
            }
        });
        var4.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
                SwipeHelper.this.updateAlphaFromOffset(var1, var3);
            }
        });
        var4.start();
    }

    public interface Callback {
        boolean canChildBeDismissed(View var1);

        View getChildAtPosition(MotionEvent var1);

        View getChildContentView(View var1);

        void onBeginDrag(View var1);

        void onChildDismissed(View var1);

        void onDragCancelled(View var1);
    }
}
