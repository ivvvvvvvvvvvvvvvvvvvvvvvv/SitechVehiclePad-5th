package com.sitechdev.vehicle.pad.event;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.sitechdev.vehicle.lib.event.BaseEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wujinling
 * @date 2016/12/13
 */
public class BTEvent extends BaseEvent<Integer> {
    /**
     * 蓝牙音乐控制 [IN]
     * 应用场景：方向盘、面板、语音、主菜单等
     * 参数: PlayCtrl 控制类型
     * 响应时间：异步执行
     */
    public static final int EB_BT_AUDIO_CTRL = 1000;

    public enum PlayCtrl {PLAY/*播放(ACC使用)*/, PAUSE/*暂停(ACC使用)*/, PP/*播放暂停按钮*/, STOP/*停止*/, PREV/*上一曲*/, NEXT/*下一曲*/}

    /**
     * 蓝牙电话控制 [IN]
     * 应用场景：方向盘、面板、语音、主菜单等
     * 参数: CallCtrl 控制类型
     * 响应时间：异步执行
     */
    public static final int EB_BT_PHONE_CTRL = 1001;

    public enum CallCtrl {ANSWER/* 蓝牙电话接听 */, HANGUP/* 蓝牙电话挂断 */}

    /**
     * 蓝牙服务控制[IN]
     * 应用场景：SRC切换等
     * 参数: int, ServeCtrl 控制类型的序号ordinal
     * 响应时间：异步执行
     */
    public static final int EB_BT_SVR_CTRL = 1002;

    public enum ServeCtrl {
        CONNA2DP/* 连接A2DP */, DISCONNA2DP/* 断开A2DP */, CONNSPP/* 连接SPP */,
        DISCONNSPP/* 断开SPP */, CONNHID/* 连接HID */, DISCONNHID/* 断开HID */
    }

    /**
     * 拨打蓝牙电话 [IN]
     * 应用场景：语音拨打电话
     * str0: String
     * 电话号码,禁止发null
     * 响应时间：异步执行
     */
    public static final int EB_BT_PHONE_CALL = 1003;

    /**
     * 拨打蓝牙电话(带界面) [IN]
     * 参数: String 格式为： 电话号码:姓名, 为null时隐藏
     * 响应时间：异步执行
     */
    public static final int FAST_CALL_WITH_UI = 1004;

    /**
     * 蓝牙设置 [IN]
     * 应用场景：设置界面自动连接，自动接听
     * 参数： FeatureCtrl
     * 响应时间：异步执行
     */
    public static final int EB_BT_SET_CTRL = 1005;

    public enum FeatureCtrl {AUTO_CONNECT_ON, AUTO_CONNECT_OFF, AUTO_ANSWER_ON, AUTO_ANSWER_OFF}

    /**
     * 发送spp数据 [IN]
     * 参数： byte[]
     * 响应时间：异步执行
     */
    public static final int EB_BT_SEND_SPP_DATA = 1006;

    /**
     * 开启蓝牙可被发现模式 [IN]
     * 参数： boolean
     * false：关闭  true：打开
     * 响应时间：异步执行
     */
    public static final int EB_BT_PAIR_ENABLE = 1007;

    /**
     * 打开关闭蓝牙 [IN]
     * 参数： boolean
     * false：关闭  true：打开
     * 响应时间：异步执行
     */
    public static final int EB_BT_OPEN_CLOSE = 1008;
    /**
     * 蓝牙连接被断开事件
     */
    public static final int EB_BT_DISCONNECT = 10008;

    /**
     * 打开关闭触摸(IOS设备) [IN]
     * 参数： boolean
     * false：关闭  true：打开
     * 响应时间：异步执行
     */
    public static final int IAP_TOUCH_ENABLE = 1009;

    /**
     * iap home键 [IN]
     * 响应时间：异步执行
     */
    public static final int IAP_HOME = 1010;

    /**
     * 打开关闭蓝牙通话界面 [IN]
     * 参数： boolean
     * false：关闭  true：打开
     * 响应时间：异步执行
     */
    public static final int ENABLE_PHONE_UI = 1011;

    /**
     * 打开蓝牙通话记录界面 [IN]
     * 全部通话记录
     * 响应时间：异步执行
     */
    public static final int EB_BT_RECORDS_UI = 1012;

    /**
     * 打开蓝牙通话记录界面 [IN]
     * 未接电话
     * 响应时间：异步执行
     */
    public static final int EB_BT_MISSED_UI = 1013;

    /**
     * 打开蓝牙通话记录界面 [IN]
     * 已拨电话
     * 响应时间：异步执行
     */
    public static final int EB_BT_DIALED_UI = 1014;

    /**
     * 打开蓝牙通话记录界面 [IN]
     * 已接电话
     * 响应时间：异步执行
     */
    public static final int EB_BT_RECEIVED_UI = 1015;

    /**
     * 打开通讯录界面 [IN]
     * str0: String
     * 姓名,禁止发null
     * 响应时间：异步执行
     */
    public static final int EB_BT_QUERY_UI = 1016;

    /**
     * 语音和首页多媒体控制音乐播放
     */
    public static final int EB_BT_AUDIO_TEDDY_CTRL = 1017;

    /**
     * 蓝牙服务状态更新 [OUT]
     * 数据来源：数据中心BTModel中HFP、A2DP、SPP 、HID 、PBAP
     * 参数: ServiceType
     * 响应时间：异步执行
     */
    public static final int EB_BT_SVR_STATE = 1;


    public static final int EB_BT_ATE_CALL = 1020;

    public enum ServiceType {HFP, A2DP, AVRCP, SPP, HID, PBAP, IAP, PLAYSTAT}

    /**
     * 蓝牙设备信息更新 [OUT]
     * 数据来源：数据中心BTModel版本号、蓝牙名称、蓝牙地址、PIN
     * 参数: int 信息类型
     * 响应时间：异步执行
     */
    public static final int LOCAL_BT_INFO = 2;

    /**
     * 蓝牙配对模式更新 [OUT]
     * 响应时间：异步执行
     * 参数 boolean, bEnabled, 已true进入配对模式, 已false退出配对模式
     */
    public static final int PAIR_MODE_UPT = 3;

    /**
     * 蓝牙配对列表更新 [OUT]
     * 数据来源：蓝牙模块BtGlobalRef配对列表pairList
     * 参数: int 配对状态，
     * 响应时间：异步执行
     */
    public static final int PAIR_LIST_UPT = 4;

    /**
     * 连接上的蓝牙设备信息更新 [OUT]
     * 数据来源：蓝牙模块BtGlobalRef当前连接设备地址curDeviceAddr
     * 参数: int 设备的信息类型，
     * 响应时间：异步执行
     */
    public static final int DEVICE_BT_INFO = 5;

    /**
     * 电话本下载计数[OUT]
     * 参数: int 计数（当前项目index）
     */
    public static final int PB_DOWN_COUNT = 6;

    /**
     * 通话记录下载计数[OUT]
     * 参数: int 计数（当前项目index）
     */
    public static final int CL_DOWN_COUNT = 7;

    /**
     * 电话本/通话记录更新[OUT]
     * 参数: boolean isPhoneBook? true电话簿false通话记录
     */
    public static final int PB_OR_CL_UPDATE = 8;
    /**
     * 电话本、通话记录更新成功标记
     */
    public static final int PB_OR_CL_UPDATE_SUCCESS = 18;

    /**
     * 通话事件[OUT]
     * 参数: int, 状态如PhoneState
     */
    public static final int PHONE_CALL_STATE = 9;


    /**
     * 删除蓝牙匹配记录
     */
    public static final int DELETE_BT_PAIR = 1020;

    @IntDef({HANGUP, INCOMING, DIALING, ACTIVE, TRI_HANGUP, HELD, HELD_WAITING, TRI_WAITING, TRI_DIALING, TRI_ACTIVE, TRI_HELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PhoneState {
    }

    public final static int HANGUP = 0;
    public final static int INCOMING = 1;
    public final static int DIALING = 2;
    public final static int ACTIVE = 3;
    public final static int TRI_HANGUP = 4;
    public final static int HELD = 5;
    public final static int HELD_WAITING = 6;
    public final static int TRI_WAITING = 7;
    public final static int TRI_DIALING = 8;
    public final static int TRI_ACTIVE = 9;
    public final static int TRI_HELD = 10;

    /**
     * 蓝牙音乐信息更新 [OUT]
     * 数据来源：BTModel musicName, musicArtist
     * 响应时间：异步执行
     */
    public static final int BT_MUSIC_INFO_UPT = 10;

    /**
     * 蓝牙音乐播放时长更新 [OUT]
     * 数据来源：BTModel musicPosition, musicDuration
     * 响应时间：异步执行
     */
    public static final int EB_BT_MUSIC_TRACK = 11;

    /**
     * HFP Remote[OUT]
     * 参数 boolean, true remote, false local(phone)
     */
    public static final int EB_BT_AUDIO_CONNECTED = 12;

    /**
     * Phone mic state[OUT]
     * 参数 int, 1 ON, 0 OFF
     */
    public static final int EB_BT_MIC_STATE = 13;

    /**
     * Spp数据反馈[OUT]
     * 参数: byte[]
     */
    public static final int EB_BT_SPP_DATA_RECVD = 14;

    /**
     * 来电去电人名反馈[OUT]
     * 参数: String 人名
     */
    public static final int PHONE_ACTIVE_NUMBER = 15;

    /**
     * 来电去电人名反馈[OUT]
     * 参数: String 人名
     */
    public static final int PHONE_ACTIVE_NAME = 16;

    /**
     * 蓝牙解析停止，用于升级[OUT]
     */
    public static final int PROTO_PARSER_STOPPED = 17;

    /**
     * 三方来电号码[OUT]
     */
    public static final int PHONE_WAITING_NUMBER = 18;

    /**
     * 三方来电姓名[OUT]
     */
    public static final int PHONE_WAITING_NAME = 19;

    /**
     * 多方通话保留姓名[OUT]
     */
    public static final int PHONE_HELD_NAME = 20;

    /**
     * 多方通话挂起号码[OUT]
     */
    public static final int PHONE_HELD_NUMBER = 21;

    /**
     * 电话本服务连接失败，请在手机端允许加载[OUT]
     */
    public static final int PB_CONN_FAILED = 22;

    /**
     * 播放器设置改变[OUT]
     * 数据来源 bluetooth模块, BtGlobalReference.PlayerAttributes
     */
    public static final int PLAYER_ATTR_CHANGED = 23;

    /**
     * 应用运行状态改变[OUT]
     * 数据来源S301app isAppBackground
     */
    public static final int APP_STATE_CHANGE = 24;

    /**
     * 将联系人存储到数据库之后，发出此事件
     */
    public static final int CONTACT_SAVE_END = 25;

    /**
     * 接收到此事件后，本地数据库存储的联系人将清空
     */
    public static final int CLEAR_CONTACT_IN_DATABASE = 26;

    /**
     * 接收到此事件后，发送系统 BluetoothAdapter.ACTION_STATE_CHANGED 广播
     */
    public static final int BLUE_ADAPTER_STATE_CHANGE = 27;


    /**
     * 接收到此事件后，蓝牙本地名称改变
     */
    public static final int BLUE_NAME_CHANGE = 28;

    /**
     * 发送BT模块事件
     *
     * @param evt 事件类型
     */
    public BTEvent(int evt) {
        this.eventKey = evt;
    }

    /**
     * 发送BT模块事件
     *
     * @param evt 事件类型
     * @param obj 事件参数
     */
    public BTEvent(int evt, Object obj) {
        this.eventKey = evt;
        this.mObj = obj;
    }

    /**
     * 设置事件类型
     *
     * @param evt 事件类型
     */
    public void setEvent(int evt) {
        this.eventKey = evt;
    }

    /**
     * 设置参数
     *
     * @param obj 事件参数
     */
    public void setData(@NonNull Object obj) {
        this.mObj = obj;
    }

    /**
     * 设置事件+参数
     *
     * @param evt 事件类型
     * @param obj 事件参数
     */
    public void set(int evt, @NonNull Object obj) {
        this.eventKey = evt;
        this.mObj = obj;
    }

    /**
     * 获取事件类型
     *
     * @return 事件类型
     */
    public int getEvent() {
        return this.eventKey;
    }

    /**
     * 获取事件参数
     *
     * @return 事件参数
     * 注：需根据事件自行转换类型
     */
    public Object getData() {
        return this.mObj;
    }

    private Object mObj = null;
}