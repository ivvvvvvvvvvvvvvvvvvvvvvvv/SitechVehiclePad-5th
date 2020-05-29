package com.sitechdev.vehicle.pad.manager;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhubaoqiang
 * @date 2019/9/3
 * 所有的音源都关心 all
 * 只关心某一种 如kaola local
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VoiceSourceType {
    String value() default VoiceSourceManager.SUPPORT_TYPE_ALL;
}
