
/**
 * 文件名：  BTModel.java
 * 版  权： 深圳市合正汽车电子有限公司
 * 描  述： 蓝牙模块数据结构
 * 当前版本:	 1.0
 * 作    者: jinlingw
 * 修改历史：
 * [修改序列]	[修改日期] 		[修改者]		[修改内容]
 * 1			2016/9/19    	jinlingw   	创建
 **/

package com.sitechdev.vehicle.pad.model.phone;

import java.io.Serializable;

public class BTModel implements Serializable {

    public static final byte BT_ON = 0x01;
    public static final byte BT_OFF = 0x00;
    private static final long serialVersionUID = -8334786389764779007L;

    /**
     * 蓝牙开关状态
     * 0: OFF  1:ON
     */
    public byte byeBtOnOff = BT_OFF;

    /**
     * 蓝牙name
     */
    public String strBtName = null;
    /**
     * 蓝牙地址
     */
    public String strBtAddr = null;
    /**
     * 蓝牙PIN码
     */
    public String strBtPinCode = null;
    /**
     * 蓝牙Hfp的当前状态
     */
    public HfpState nHFPState = HfpState.DISCONNECTED;
    /**
     * 蓝牙spp的当前状态
     */
    public SppState nSppState = SppState.DISCONNECTED;
    /**
     * 蓝牙A2dp的当前状态
     */
    public A2dpState nA2DPState = A2dpState.DISCONNECTED;
    /**
     * 蓝牙Avrcp的当前状态
     */
    public AvrcpState nAvrcpState = AvrcpState.DISCONNECTED;
    /**
     * 蓝牙Pbapc的当前状态
     */
    public PbapcState nPbapcState = PbapcState.DISCONNECTED;
    /**
     * 蓝牙hid的当前状态
     */
    public HidState nHidState = HidState.DISCONNECTED;
    /**
     * 蓝牙iap的当前状态
     */
    public IapState nIapState = IapState.UNINIT;

    //SPP 状态定义
    public enum SppState {
        DISCONNECTED/* 未连接*/,
        CONNECTING /*连接中*/,
        STANDBY /*待机*/
    }

    //HFP 连接状态定义：
    public enum HfpState {
        DISCONNECTED/*未连接*/,
        CONNECTING/*联接中*/,
        STANDBY/*待机*/,
        INCOMING/*呼入*/,
        CALLOUT/*呼出*/,
        ACTIVE/*通话中*/,
        HELD/*只有保持的通话*/,
        TRI_HELD_WAITING/*保持和等待都有*/,
        TRI_WAITING/*三方等待*/,
        TRI_HELD/*三方保持*/
    }

    //A2DP 连接状态定义：
    public enum A2dpState {
        DISCONNECTED/*未连接*/,
        CONNECTING/*正在连接*/,
        STANDBY/*待机*/,
        PLAYING/*正在播放*/
    }

    public enum AvrcpState {
        DISCONNECTED/*未连接*/,
        CONNECTING/*连接中*/,
        STANDBY/*待机*/}

    //PBAPC 任务状态定义：
    public enum PbapcState {
        DISCONNECTED/*未连接*/,
        CONNECTING/*连接中*/,
        STANDBY/*待机，无下载任务*/,
        PHONEBOOK/*下载电话本中*/,
        CALLLOG/*下载通话记录中*/
    }

    //HID 连接状态定义：
    public enum HidState {
        DISCONNECTED, CONNECTING, STANDBY
    }

    //IAP 连接状态定义：
    public enum IapState {
        UNINIT, INITED, SYNC, CONNECTING, STANDBY, TOUCH_ENABLED
    }

    // 当前音乐播放信息
    public String musicName = null;
    public int musicPosition = 0;  // sec
    public int musicDuration = 0;  // sec

    public String musicArtist = null;
    public String musicAlbum = null;
    public int musicIndex = 0;
    public int totalMusicNum = 0;
    public String musicGenre = null;

    /**
     * 0 for Stopped, 1 for playing, 2 for paused
     * see project(:bluetooth) com.hazens.bt.common.BtDef
     */
    public int musicState = 0;

    public void reset() {
        byeBtOnOff = BTModel.BT_OFF;
        strBtName = null;
        strBtAddr = null;
        strBtPinCode = null;

        nHFPState = HfpState.DISCONNECTED;
        nSppState = SppState.DISCONNECTED;
        nA2DPState = A2dpState.DISCONNECTED;
        nAvrcpState = AvrcpState.DISCONNECTED;
        nPbapcState = PbapcState.DISCONNECTED;
        nHidState = HidState.DISCONNECTED;
        nIapState = IapState.UNINIT;

        musicName = "未知";
        musicPosition = 0;
        musicDuration = 0;
        musicArtist = "未知";
        musicAlbum = "未知";
        musicIndex = 0;
        totalMusicNum = 0;
        musicGenre = null;
        musicState = 0;
    }

    public void disConnect() {
        strBtAddr = null;
        strBtPinCode = null;

        nHFPState = HfpState.DISCONNECTED;
        nSppState = SppState.DISCONNECTED;
        nA2DPState = A2dpState.DISCONNECTED;
        nAvrcpState = AvrcpState.DISCONNECTED;
        nPbapcState = PbapcState.DISCONNECTED;
        nHidState = HidState.DISCONNECTED;
        nIapState = IapState.UNINIT;

        musicName = "未知";
        musicPosition = 0;
        musicDuration = 0;
        musicArtist = "未知";
        musicAlbum = "未知";
        musicIndex = 0;
        totalMusicNum = 0;
        musicGenre = null;
        musicState = 0;
    }
}