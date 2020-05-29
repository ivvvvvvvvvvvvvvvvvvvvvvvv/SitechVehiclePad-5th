package com.sitechdev.vehicle.pad.module.map.util;

import android.content.Context;
import android.graphics.Point;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.AnimationSet;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.model.animation.TranslateAnimation;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapAnimUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/20 0020 11:48
 * 修改时间：
 * 备注：
 */
public class MapAnimUtil {

    /**
     * 生长动画
     *
     * @return Animation
     */
    public static Animation getGrowAnimation() {
        Animation animation = new ScaleAnimation(0, 1, 0, 1);
        animation.setInterpolator(new LinearInterpolator());
        //整个移动所需要的时间
        animation.setDuration(500);
        return animation;
    }

    /**
     * 跳跃动画
     *
     * @param context 上下文
     * @param aMap    地图实体
     * @param latLng  经纬度
     * @return Animation
     */
    public static Animation getJumpAnimation(Context context, AMap aMap, LatLng latLng) {
        Point point = aMap.getProjection().toScreenLocation(latLng);
        point.y -= dip2px(context, 125);
        LatLng target = aMap.getProjection()
                .fromScreenLocation(point);
        //使用TranslateAnimation,填写一个需要移动的目标点
        Animation animation = new TranslateAnimation(target);
        animation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                } else {
                    return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                }
            }
        });
        //整个移动所需要的时间
        animation.setDuration(600);
        return animation;
    }

    /**
     * 缩放动画。水波纹
     *
     * @return animationSet
     */
    public static AnimationSet getWaterRippleSet() {
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0f);
        alphaAnimation.setDuration(2000);
        // 设置不断重复
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 3.5f, 1, 3.5f);
        scaleAnimation.setDuration(2000);
        // 设置不断重复
        scaleAnimation.setRepeatCount(Animation.INFINITE);


        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setInterpolator(new LinearInterpolator());

        return animationSet;
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
