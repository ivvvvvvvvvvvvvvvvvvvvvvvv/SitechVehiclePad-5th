package com.sitechdev.vehicle.lib.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuhe
 * <p>
 * 一般在BaseActivity/BaseFragment中添加如下方法，然后在子类中需要使用BindBus的地方添加注解即可
 * <p>
 * 若使用BindBus注解，则绑定EventBus
 * if(this.getClass().isAnnotationPresent(BindBus.class)){
 * XtBusUtils.register(this);
 * }
 * <p>
 * 若使用BindBus注解，则解绑定EventBus
 * if(this.getClass().isAnnotationPresent(BindBus.class)){
 * XtBusUtils.unregister(this);
 * }
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindBus {
}
