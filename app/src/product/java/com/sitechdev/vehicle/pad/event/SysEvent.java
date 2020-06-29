package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 拨号键盘操作广播
 *
 * @author liuhe
 * @date 2019/04/18
 */
public class SysEvent extends BaseEvent<String> {

    public SysEvent(String evt) {
        this.eventKey = evt;
    }

    /**
     * 发送Sys模块事件
     *
     * @param evt 事件类型
     * @param obj 事件参数
     */
    public SysEvent(String evt, Object obj) {
        this.eventKey = evt;
        this.mObj = obj;
    }

    public SysEvent(String evt, int ctrlMask) {
        this.eventKey = evt;
        this.mDataCtrlMask = ctrlMask;
    }

    public int getDataCtrlMask() {
        return mDataCtrlMask;
    }

    public String getEvent() {
        return eventKey;
    }

    public Object getObj() {
        return mObj;
    }

    private Object mObj;
    private int mDataCtrlMask = -1;

    /**
     * 恢复出厂设置
     */
    public static final String EB_SYS_RESTORE_FACTORY = "com.hazens.EB_SYS_RESTORE_FACTORY";

    /**
     * 系统设备连接状态更新 [OUT]
     * 应用场景：关注设备连接信息模块
     * 数据来源：数据中心USB、SD、IPOD、BT、PHONE
     * 参   数：DEV_
     * 响应时间：异步执行
     */
    public static final String EB_SYS_DEV_STATE = "EB_SYS_DEV_STATE";

    /**
     * 再次加载通话记录
     */
    public static final String EB_AGAIN_CONTACTS_HISTORY = "EB_AGAIN_CONTACTS_HISTORY";



    public static final int CALLLOG_RECORDS = 0;
    public static final int CALLLOG_MISSED = 1;
    public static final int CALLLOG_DIALED = 2;
    public static final int CALLLOG_RECEIVED = 3;
}
