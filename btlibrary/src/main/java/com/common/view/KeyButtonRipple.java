////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.common.view;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.CanvasProperty;
//import android.graphics.ColorFilter;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.view.RenderNodeAnimator;
//import android.view.View;
//import android.view.animation.Interpolator;
//import android.view.animation.PathInterpolator;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//
//public class KeyButtonRipple extends Drawable {
//    public static final Interpolator ALPHA_OUT = new PathInterpolator(0.0F, 0.0F, 0.8F, 1.0F);
//    private static final int ANIMATION_DURATION_FADE = 450;
//    private static final int ANIMATION_DURATION_SCALE = 350;
//    private static final float GLOW_MAX_ALPHA = 0.2F;
//    private static final float GLOW_MAX_SCALE_FACTOR = 1.35F;
//    private final Interpolator mAlphaExitInterpolator;
//    private final AnimatorListenerAdapter mAnimatorListener;
//    private CanvasProperty<Float> mBottomProp;
//    private boolean mDrawingHardwareGlow;
//    private Paint mFocusPaint;
//    private boolean mFocused;
//    private float mGlowAlpha = 0.0F;
//    private float mGlowScale = 1.0F;
//    private final Interpolator mInterpolator = new KeyButtonRipple.LogInterpolator();
//    private CanvasProperty<Float> mLeftProp;
//    private int mMaxWidth;
//    private CanvasProperty<Paint> mPaintProp;
//    private boolean mPressed;
//    private CanvasProperty<Float> mRightProp;
//    private Paint mRipplePaint;
//    private final HashSet<Animator> mRunningAnimations;
//    private CanvasProperty<Float> mRxProp;
//    private CanvasProperty<Float> mRyProp;
//    private boolean mSupportHardware;
//    private final View mTargetView;
//    private final ArrayList<Animator> mTmpArray;
//    private CanvasProperty<Float> mTopProp;
//
//    public KeyButtonRipple(Context var1, View var2) {
//        this.mAlphaExitInterpolator = ALPHA_OUT;
//        this.mRunningAnimations = new HashSet();
//        this.mTmpArray = new ArrayList();
//        this.mAnimatorListener = new AnimatorListenerAdapter() {
//            public void onAnimationEnd(Animator var1) {
//                KeyButtonRipple.this.mRunningAnimations.remove(var1);
//                if (KeyButtonRipple.this.mRunningAnimations.isEmpty() && !KeyButtonRipple.this.mPressed) {
//                    KeyButtonRipple.this.mDrawingHardwareGlow = false;
//                    KeyButtonRipple.this.invalidateSelf();
//                }
//
//            }
//        };
//        this.mMaxWidth = 126;
//        this.mTargetView = var2;
//    }
//
//    private void cancelAnimations() {
//        this.mTmpArray.addAll(this.mRunningAnimations);
//        int var2 = this.mTmpArray.size();
//
//        for(int var1 = 0; var1 < var2; ++var1) {
//            ((Animator)this.mTmpArray.get(var1)).cancel();
//        }
//
//        this.mTmpArray.clear();
//        this.mRunningAnimations.clear();
//    }
//
//    private void drawSoftware(Canvas var1) {
//        if (this.mGlowAlpha > 0.0F) {
//            Paint var8 = this.getRipplePaint();
//            var8.setAlpha((int)(this.mGlowAlpha * 255.0F));
//            float var2 = (float)this.getBounds().width();
//            float var3 = (float)this.getBounds().height();
//            boolean var7;
//            if (var2 > var3) {
//                var7 = true;
//            } else {
//                var7 = false;
//            }
//
//            float var5 = (float)this.getRippleSize() * this.mGlowScale * 0.5F;
//            var2 *= 0.5F;
//            var3 *= 0.5F;
//            float var4;
//            if (var7) {
//                var4 = var5;
//            } else {
//                var4 = var2;
//            }
//
//            if (var7) {
//                var5 = var3;
//            }
//
//            float var6;
//            if (var7) {
//                var6 = var3;
//            } else {
//                var6 = var2;
//            }
//
//            var1.drawRoundRect(var2 - var4, var3 - var5, var2 + var4, var3 + var5, var6, var6, var8);
//        }
//
//    }
//
//    private void enterHardware() {
//        this.cancelAnimations();
//        this.mDrawingHardwareGlow = true;
//        this.setExtendStart(CanvasProperty.createFloat((float)(this.getExtendSize() / 2)));
//        RenderNodeAnimator var1 = new RenderNodeAnimator(this.getExtendStart(), (float)(this.getExtendSize() / 2) - (float)this.getRippleSize() * 1.35F / 2.0F);
//        var1.setDuration(350L);
//        var1.setInterpolator(this.mInterpolator);
//        var1.addListener(this.mAnimatorListener);
//        var1.setTarget(this.mTargetView);
//        this.setExtendEnd(CanvasProperty.createFloat((float)(this.getExtendSize() / 2)));
//        RenderNodeAnimator var2 = new RenderNodeAnimator(this.getExtendEnd(), (float)(this.getExtendSize() / 2) + (float)this.getRippleSize() * 1.35F / 2.0F);
//        var2.setDuration(350L);
//        var2.setInterpolator(this.mInterpolator);
//        var2.addListener(this.mAnimatorListener);
//        var2.setTarget(this.mTargetView);
//        if (this.isHorizontal()) {
//            this.mTopProp = CanvasProperty.createFloat(0.0F);
//            this.mBottomProp = CanvasProperty.createFloat((float)this.getBounds().height());
//            this.mRxProp = CanvasProperty.createFloat((float)(this.getBounds().height() / 2));
//            this.mRyProp = CanvasProperty.createFloat((float)(this.getBounds().height() / 2));
//        } else {
//            this.mLeftProp = CanvasProperty.createFloat(0.0F);
//            this.mRightProp = CanvasProperty.createFloat((float)this.getBounds().width());
//            this.mRxProp = CanvasProperty.createFloat((float)(this.getBounds().width() / 2));
//            this.mRyProp = CanvasProperty.createFloat((float)(this.getBounds().width() / 2));
//        }
//
//        this.mGlowScale = 1.35F;
//        this.mGlowAlpha = 0.2F;
//        this.mRipplePaint = this.getRipplePaint();
//        this.mRipplePaint.setAlpha((int)(this.mGlowAlpha * 255.0F));
//        this.mPaintProp = CanvasProperty.createPaint(this.mRipplePaint);
//        var1.start();
//        var2.start();
//        this.mRunningAnimations.add(var1);
//        this.mRunningAnimations.add(var2);
//        this.invalidateSelf();
//    }
//
//    private void enterSoftware() {
//        this.cancelAnimations();
//        this.mGlowAlpha = 0.2F;
//        ObjectAnimator var1 = ObjectAnimator.ofFloat(this, "glowScale", new float[]{0.0F, 1.35F});
//        var1.setInterpolator(this.mInterpolator);
//        var1.setDuration(350L);
//        var1.addListener(this.mAnimatorListener);
//        var1.start();
//        this.mRunningAnimations.add(var1);
//    }
//
//    private void exitHardware() {
//        this.mPaintProp = CanvasProperty.createPaint(this.getRipplePaint());
//        RenderNodeAnimator var1 = new RenderNodeAnimator(this.mPaintProp, 1, 0.0F);
//        var1.setDuration(450L);
//        var1.setInterpolator(this.mAlphaExitInterpolator);
//        var1.addListener(this.mAnimatorListener);
//        var1.setTarget(this.mTargetView);
//        var1.start();
//        this.mRunningAnimations.add(var1);
//        this.invalidateSelf();
//    }
//
//    private void exitSoftware() {
//        ObjectAnimator var1 = ObjectAnimator.ofFloat(this, "glowAlpha", new float[]{this.mGlowAlpha, 0.0F});
//        var1.setInterpolator(this.mAlphaExitInterpolator);
//        var1.setDuration(450L);
//        var1.addListener(this.mAnimatorListener);
//        var1.start();
//        this.mRunningAnimations.add(var1);
//    }
//
//    private CanvasProperty<Float> getExtendEnd() {
//        return this.isHorizontal() ? this.mRightProp : this.mBottomProp;
//    }
//
//    private int getExtendSize() {
//        return this.isHorizontal() ? this.getBounds().width() : this.getBounds().height();
//    }
//
//    private CanvasProperty<Float> getExtendStart() {
//        return this.isHorizontal() ? this.mLeftProp : this.mTopProp;
//    }
//
//    private Paint getRipplePaint() {
//        if (this.mRipplePaint == null) {
//            this.mRipplePaint = new Paint();
//            this.mRipplePaint.setAntiAlias(true);
//            this.mRipplePaint.setColor(-1);
//        }
//
//        return this.mRipplePaint;
//    }
//
//    private int getRippleSize() {
//        int var1;
//        if (this.isHorizontal()) {
//            var1 = this.getBounds().width();
//        } else {
//            var1 = this.getBounds().height();
//        }
//
//        return Math.min(var1, this.mMaxWidth);
//    }
//
//    private boolean isHorizontal() {
//        return this.getBounds().width() > this.getBounds().height();
//    }
//
//    private void setExtendEnd(CanvasProperty<Float> var1) {
//        if (this.isHorizontal()) {
//            this.mRightProp = var1;
//        } else {
//            this.mBottomProp = var1;
//        }
//    }
//
//    private void setExtendStart(CanvasProperty<Float> var1) {
//        if (this.isHorizontal()) {
//            this.mLeftProp = var1;
//        } else {
//            this.mTopProp = var1;
//        }
//    }
//
//    private void setPressedHardware(boolean var1) {
//        if (var1) {
//            this.enterHardware();
//        } else {
//            this.exitHardware();
//        }
//    }
//
//    private void setPressedSoftware(boolean var1) {
//        if (var1) {
//            this.enterSoftware();
//        } else {
//            this.exitSoftware();
//        }
//    }
//
//    public void draw(Canvas var1) {
//        this.mSupportHardware = false;
//        if (this.mFocused) {
//            if (this.mFocusPaint == null) {
//                this.mFocusPaint = new Paint();
//                this.mFocusPaint.setColor(-1996488705);
//            }
//
//            var1.drawRect(this.getBounds(), this.mFocusPaint);
//        } else if (!this.mSupportHardware) {
//            this.drawSoftware(var1);
//        }
//    }
//
//    public float getGlowAlpha() {
//        return this.mGlowAlpha;
//    }
//
//    public float getGlowScale() {
//        return this.mGlowScale;
//    }
//
//    public int getOpacity() {
//        return -3;
//    }
//
//    public boolean isStateful() {
//        return true;
//    }
//
//    public void jumpToCurrentState() {
//        this.cancelAnimations();
//    }
//
//    protected boolean onStateChange(int[] var1) {
//        boolean var6 = false;
//        boolean var7 = false;
//        boolean var5 = false;
//        int var2 = 0;
//
//        boolean var3;
//        boolean var4;
//        while(true) {
//            var3 = var6;
//            var4 = var7;
//            if (var2 >= var1.length) {
//                break;
//            }
//
//            if (var1[var2] == 16842919) {
//                var3 = true;
//                var4 = var7;
//                break;
//            }
//
//            if (var1[var2] == 16842908) {
//                var4 = true;
//                var3 = var6;
//                break;
//            }
//
//            ++var2;
//        }
//
//        if (var3 != this.mPressed) {
//            this.setPressed(var3);
//            this.mPressed = var3;
//            var5 = true;
//        }
//
//        this.mFocused = var4;
//        this.invalidateSelf();
//        return var5;
//    }
//
//    public void setAlpha(int var1) {
//    }
//
//    public void setColorFilter(ColorFilter var1) {
//    }
//
//    public void setGlowAlpha(float var1) {
//        this.mGlowAlpha = var1;
//        this.invalidateSelf();
//    }
//
//    public void setGlowScale(float var1) {
//        this.mGlowScale = var1;
//        this.invalidateSelf();
//    }
//
//    public void setPressed(boolean var1) {
//        if (this.mSupportHardware) {
//            this.setPressedHardware(var1);
//        } else {
//            this.setPressedSoftware(var1);
//        }
//    }
//
//    private static final class LogInterpolator implements Interpolator {
//        private LogInterpolator() {
//        }
//
//        public float getInterpolation(float var1) {
//            return 1.0F - (float)Math.pow(400.0D, (double)(-var1) * 1.4D);
//        }
//    }
//}
