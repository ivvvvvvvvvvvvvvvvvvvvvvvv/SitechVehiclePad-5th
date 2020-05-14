package com.sitechdev.pad.lib.aoplibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：GA10-C
 * 类名称：DebugTrace
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/04/19 0019 11:04
 * 修改时间：
 * 备注：
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface DebugTrace {
}
