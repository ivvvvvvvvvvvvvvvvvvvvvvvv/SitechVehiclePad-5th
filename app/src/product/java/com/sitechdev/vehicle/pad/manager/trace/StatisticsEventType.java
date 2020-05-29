package com.sitechdev.vehicle.pad.manager.trace;

/**
 * 埋点ev
 *
 * @author qishuaishuai
 * @date 2019/6/25
 */
public enum StatisticsEventType {
    //APP启动：start、APP后台切入：enter、app切到后台：out、app进程杀死：end、页面进入(page_enter)、按钮点击(click)、滑动（slide）、翻页（pageup）、自定义事件（userdefined）、页面退出（page_out）
    START(1001, "start|default|"),// app启动
    ENTER(1002, "enter|default|"),//APP后台切入
    OUT(1003, "out|default|"),//app切到后台
    END(1004, "end|default|"),//app进程杀死
    CLICK(1006, "click|default|"),//按钮点击
    PAGEUP(1008, "pageup|default|"),//翻页
    USERDEFINED(1009, "userdefined|default|"),//自定义事件

    PAGE_OUT(1010, "page_out|default|"),//页面退出
    SLIDE(1007, "slide|default|"),//滑动
    PAGE_ENTER(1005, "page_enter|default|"),//页面进入

    /**
     * 分类标识（采集数据源)
     */
    /**
     * 默认，app启动
     */
    CATEGORY_DEFAULT(2001, "default|"),
    /**
     * 实体按键
     */
    CATEGORY_KEY(2002, "click|key|"),
    /**
     * 语音
     */
    CATEGORY_VOICE(2003, "userdefined|voice|");

    private int code;
    private String name;

    StatisticsEventType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}