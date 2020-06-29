package com.sitechdev.vehicle.pad.module.phone;



import com.my.hw.BtDeviceBean;
import com.sitechdev.vehicle.pad.model.phone.CallLog;
import com.sitechdev.vehicle.pad.model.phone.Contact;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BtGlobalRef {

    // 当前通话号码
    public static String activeNumber = null;
    public static String activeName = null;
    public static String waitingNumber = null;
    public static String waitingName = null;
    public static String heldNumber = null;
    public static String heldName = null;

    public static String lastDeviceAddr = null;
    // 当前连接的设备的地址
    public static String curDeviceAddr = null;
    // 当前连接的设备的名称
    public static String curDeviceName = null;

    public static int pairMode = BtConstants.PAIRMODE_OUT;

    // 是否允许在其他界面开启配对模式
    public static boolean bCanPairInOtherUI = false;
    /**
     * 声音是否在蓝牙。（通话） (bug: iphone手机端拨打电话时不更新该值)
     */
    public static boolean isAudioConnected = false;

    public static boolean isMicMute = false;

    public static boolean bEnablePhoneUI = true;

    public static ArrayList<BtDeviceBean> pairList = new ArrayList<>();

    public static ArrayList<Contact> contacts = new ArrayList<>();

//    public static ArrayList<AudioModel> gUsbMusics = new ArrayList<>();

    public static ArrayList<String> gUnavailableMusic = new ArrayList<>();

    /**
     * 排好序的通讯录集合
     */
    public static ArrayList<Contact> contactSorts = new ArrayList<>();

    public static JSONArray contactsArray = new JSONArray();


    /**
     * 通讯录字母索引集合
     */
    public static List<String> characterSorts = new ArrayList<>();

    public static ArrayList<CallLog> callLogs = new ArrayList<>();

    public static boolean needToReqMusicPosition = false;

    /**
     * 播放器属性，内容定义见{@link BtConstants}
     */
    public static int[] PlayerAttributes = new int[]{
            BtConstants.FUNC_OFF,
            BtConstants.FUNC_OFF,
            BtConstants.FUNC_OFF,
            BtConstants.FUNC_OFF};
}