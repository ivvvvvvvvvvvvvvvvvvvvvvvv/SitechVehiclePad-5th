package com.sitechdev.pad.lib.aoplibrary.util;

import java.util.concurrent.TimeUnit;

/**
 * 项目名称：GA10-C
 * 类名称：classWatch
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/04/19 0019 11:05
 * 修改时间：
 * 备注：
 */
public class classWatch {
    private long startTime;
    private long endTime;
    private long elapsedTime;

    public classWatch() {
        //empty
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
    }

    public void start() {
        reset();
        startTime = System.nanoTime();
    }

    public void stop() {
        if (startTime != 0) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        } else {
            reset();
        }
    }

    public long getTotalTimeMillis() {
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(endTime - startTime) : 0;
    }
}
