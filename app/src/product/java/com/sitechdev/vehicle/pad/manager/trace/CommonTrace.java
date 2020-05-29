package com.sitechdev.vehicle.pad.manager.trace;


import java.util.HashMap;

/**
 * 埋点事件公共类
 *
 * @author liuhe
 * @date 2019/06/27
 */
public class CommonTrace {
    /**
     * 命令类型,required
     */
    public String ev;
    /**
     * 页面标识，required
     */
    public Class pa;

    /**
     * 埋点ID,required
     */
    public String point;

    /**
     * 事件内容,optional
     */
    public HashMap<String, String> evj;

    /**
     * 是否不经过内存，直接上传
     */
    public boolean quick;
}
