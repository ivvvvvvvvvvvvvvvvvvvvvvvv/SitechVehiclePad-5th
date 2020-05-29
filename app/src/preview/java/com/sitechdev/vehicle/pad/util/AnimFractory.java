package com.sitechdev.vehicle.pad.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/***
 * 日期： 2019/3/29 11:50
 * 姓名： bijingshuai
 * 说明： 控件的显示和隐藏动画：缩放和透明度变化动画
 */
public class AnimFractory {
    public static void inAnim(View view) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);//缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);//缩放
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);//透明度
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(fadeInOut).with(scaleX).with(scaleY);
        animSet.setDuration(100);
        animSet.start();
    }

    public static void inAnim(View view, long duration) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);//缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);//缩放
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);//透明度
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(fadeInOut).with(scaleX).with(scaleY);
        animSet.setDuration(duration);
        animSet.start();
    }

    public static void outAnim(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.8f);//缩放
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.8f);//缩放
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f);//透明度

        ObjectAnimator afterScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.0f);//缩放
        ObjectAnimator afterScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.0f);//缩放
        ObjectAnimator afterFadeInOut = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1.0f);//透明度
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).with(fadeInOut).before(afterScaleX).before(afterScaleY).before(afterFadeInOut);
        animSet.setDuration(100);
        animSet.start();
    }

    /**
     * 控件平移显示与隐藏
     * @param view
     * @param isShow
     */
    public static void showDeviceInfoAnimation(View view, boolean isShow) {
        if (isShow) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f);
            animator.setDuration(500);
            animator.start();
            view.setVisibility(View.VISIBLE);

        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
            animator.setDuration(500);
            animator.start();
            view.setVisibility(View.GONE);
        }
    }
}
