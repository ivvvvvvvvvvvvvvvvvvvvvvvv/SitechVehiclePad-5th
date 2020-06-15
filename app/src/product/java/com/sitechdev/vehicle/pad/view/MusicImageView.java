package com.sitechdev.vehicle.pad.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import skin.support.widget.SkinCompatImageView;

/**
 * 用于圆角的图片旋转动画的处理
 */
public class MusicImageView extends SkinCompatImageView {
    private ValueAnimator anim_imageBg = null;

    public MusicImageView(Context context) {
        this(context, null);
    }

    public MusicImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAnimation() {
        if (anim_imageBg == null) {
            anim_imageBg = ObjectAnimator.ofFloat(this, "rotation", 0F, 360F);
            anim_imageBg.setDuration(9000);
            anim_imageBg.setInterpolator(new LinearInterpolator());
            anim_imageBg.setRepeatCount(ValueAnimator.INFINITE);
            anim_imageBg.setRepeatMode(ValueAnimator.RESTART);
        }
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        initAnimation();
        if (anim_imageBg.isPaused()) {
            anim_imageBg.resume();
        } else {
            anim_imageBg.start();
        }

    }

    /**
     * 暂停动画
     */
    public void pauseAnimation() {
        initAnimation();
        anim_imageBg.pause();
    }


    /**
     * 继续动画
     */
    public void resumeAnimation() {
        initAnimation();
        anim_imageBg.resume();
    }

    /**
     * 暂停动画
     */
    public void stopAnimation() {
        if (anim_imageBg != null) {
            anim_imageBg.cancel();
        }
    }
}
