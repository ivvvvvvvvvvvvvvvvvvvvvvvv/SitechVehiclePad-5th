//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my.hw;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.common.util.MachineConfig;
import com.common.util.Util;
import com.sitechdev.vehicle.lib.event.EventBusUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

public class ATBluetooth {
    public static final int A2DP_INFO_CONNECTED = 3;
    public static final int A2DP_INFO_CONNECTING = 2;
    public static final int A2DP_INFO_INITIAL = 0;
    public static final int A2DP_INFO_PAUSED = 5;
    public static final int A2DP_INFO_PLAY = 4;
    public static final int A2DP_INFO_READY = 1;
    public static final int A2DP_NEED_CONNECT = -1;
    public static final int CLEAR_PAIR_INFO = 5;
    private static final boolean DBG = false;
    public static final int DOWNLOAD_NUM_ONETIME = 5000;
    public static final int GET_A2DP_INFO = 18;
    public static final int GET_AV_INFO = 16;
    public static final int GET_HFP_INFO = 8;
    public static final int GET_HFP_INFO2 = 126;
    public static final int GET_HFP_CONNECT_NAME = 127;
    public static final int GET_PAIR_INFO = 3;
    public static final int HFP_INFO_CALLED = 4;
    public static final int HFP_INFO_CALLING = 6;
    public static final int HFP_INFO_CONNECTED = 3;
    public static final int HFP_INFO_CONNECTING = 2;
    public static final int HFP_INFO_INCOMING = 5;
    public static final int HFP_INFO_INITIAL = 0;
    public static final int HFP_INFO_READY = 1;
    private static final int IVT_MSG_TIME = 1400;
    private static final int IVT_WATCH_DOG_CHECK_TOTAL_TIME = 30000;
    public static final int PHONE_BOOK_CONNECT_STATUS_DISCONNECT = 2;
    public static final int PHONE_BOOK_CONNECT_STATUS_FAIL = 1;
    public static final int PHONE_BOOK_CONNECT_STATUS_OK = 0;
    public static final int PHONE_BOOK_TYPE_CALL_IN = 4;
    public static final int PHONE_BOOK_TYPE_CALL_OUT = 2;
    public static final int PHONE_BOOK_TYPE_MISS = 3;
    public static final int PHONE_BOOK_TYPE_PHONE = 1;
    public static final int PHONE_BOOK_TYPE_SIM = 0;
    public static final int REQUEST_3CALL_ADD = 93;
    public static final int REQUEST_3CALL_ANSWER = 179;
    public static final int REQUEST_3CALL_HANG = 180;
    public static final int REQUEST_3CALL_HANG2 = 181;
    public static final int REQUEST_3CALL_MERGE = 94;
    public static final int REQUEST_A2DP_CONNECT_STATUS = 108;
    public static final int REQUEST_A2DP_ID3 = 100;
    public static final int REQUEST_A2DP_NEXT = 83;
    public static final int REQUEST_A2DP_PAUSE = 81;
    public static final int REQUEST_A2DP_PLAY = 80;
    public static final int REQUEST_A2DP_PP = 79;
    public static final int REQUEST_A2DP_PREV = 82;
    public static final int REQUEST__A2DP_AUDIO_PLAYPAUSE = 1002;
    public static final int REQUEST_A2DP_REPORT_ID3 = 107;
    public static final int REQUEST_A2DP_TIME = 102;
    public static final int REQUEST_ANSWER = 67;
    public static final int REQUEST_AUTOANSWER = 48;
    public static final int REQUEST_AUTOCONNECT = 40;
    public static final int REQUEST_AUTO_ANSWER = 37;
    public static final int REQUEST_AUTO_ANSWER_DISABLE = 76;
    public static final int REQUEST_AUTO_ANSWER_ENABLE = 75;
    public static final int REQUEST_AUTO_CONNECT = 36;
    public static final int REQUEST_AUTO_CONNECT_DISABLE = 74;
    public static final int REQUEST_AUTO_CONNECT_ENABLE = 73;
    public static final int REQUEST_AV = 14;
    public static final int REQUEST_AV_PLAY = 20;
    public static final int REQUEST_BT_MUSIC_CPCC = 147;
    public static final int REQUEST_BT_MUSIC_MUTE = 165;
    public static final int REQUEST_BT_MUSIC_UNMUTE = 166;
    public static final int REQUEST_CALL = 10;
    public static final int REQUEST_CALL_LOG = 26;
    public static final int REQUEST_CALL_LOG_ALL = 221;
    public static final int REQUEST_CALL_LOG_DIAL = 222;
    public static final int REQUEST_CALL_LOG_CONNECT = 223;
    public static final int REQUEST_CALL_LOG_MISS = 224;
    public static final int REQUEST_CLMS = 148;
    public static final int REQUEST_CMD_AADC = 154;
    public static final int REQUEST_CMD_ALAC = 155;
    public static final int REQUEST_CMD_DGBM = 156;
    public static final int REQUEST_CMD_DGCD = 158;
    public static final int REQUEST_CMD_DGEC = 159;
    public static final int REQUEST_CMD_DLPE = 162;
    public static final int REQUEST_CMD_DLSE = 160;
    public static final int REQUEST_CMD_DSCD = 157;
    public static final int REQUEST_CMD_PBSCEX = 161;
    public static final int REQUEST_CMD_PPNO = 163;
    public static final int REQUEST_CONNECTABLE = 120;
    public static final int REQUEST_CONNECT_BY_ADDR = 64;
    public static final int REQUEST_CONNECT_BY_INDEX = 57;
    public static final int REQUEST_CONNECT_DEVICE = 106;
    public static final int REQUEST_CPGM = 130;
    public static final int REQUEST_DEL_PHONE_BOOK = 118;
    public static final int REQUEST_DEVICE_INDEX = 124;
    public static final int REQUEST_DISCONNECT = 38;
    public static final int REQUEST_DISCOVERABLE = 119;
    public static final int REQUEST_DSCD = 149;
    public static final int REQUEST_DTMF = 68;
    public static final int REQUEST_REJECT = 65;
    public static final int REQUEST_HANG = 66;
    public static final int REQUEST_HFP = 6;
    public static final int REQUEST_IF_AUTO_CONNECT = 109;
    public static final int REQUEST_LANGUAGE = 127;
    public static final int REQUEST_MIC = 25;
    public static final int REQUEST_MIC_MUTE = 71;
    public static final int REQUEST_MIC_UNMUTE = 72;
    public static final int REQUEST_NAME = 30;
    public static final int REQUEST_PAIR_BY_ADDR = 110;
    public static final int REQUEST_PHONE_BOOK = 22;
    public static final int REQUEST_PHONE_BOOK_DOWN_STATUS = 115;
    public static final int REQUEST_PHONE_BOOK_SIZE = 152;
    public static final int REQUEST_PHONE_BOOK_SYNC = 116;
    public static final int REQUEST_PIN = 32;
    public static final int REQUEST_POWER = 1;
    public static final int REQUEST_REQUEST_VERSION = 88;
    public static final int REQUEST_RPMT = 141;
    public static final int REQUEST_RPRE = 139;
    public static final int REQUEST_RREC = 137;
    public static final int REQUEST_SEARCH = 28;
    public static final int REQUEST_SETTING = 34;
    public static final int REQUEST_SET_VOLUME = 146;
    public static final int REQUEST_SNIFF_MODE = 121;
    public static final int REQUEST_SOURCE = 769;
    public static final int REQUEST_SOURCE_CALL = 770;
    public static final int REQUEST_SSP_MODE = 122;
    public static final int REQUEST_STOP_PHONE_BOOK = 96;
    public static final int REQUEST_UPDATE_BT = 240;
    public static final int REQUEST_UPDATE_START = 239;
    public static final int REQUEST_VOICE_STATUS = 135;
    public static final int REQUEST_VOICE_SWITCH = 12;
    public static final int REQUEST_VOICE_SWITCH_LOCAL = 69;
    public static final int REQUEST_VOICE_SWITCH_REMOTE = 70;
    public static final int REQUEST_BT_DISCOVERABLE = 666;
    public static final int REQUEST_BT_UN_DISCOVERABLE = 888;
    public static final int RETURN_3CALL_END = 177;
    public static final int RETURN_3CALL_START = 176;
    public static final int RETURN_3CALL_STATUS = 178;
    public static final int RETURN_A2DP_CONNECT_STATUS = 105;
    public static final int RETURN_A2DP_CUR_TIME = 104;
    public static final int RETURN_A2DP_ID3 = 101;
    public static final int RETURN_A2DP_ID3_ALBUM = 184;
    public static final int RETURN_A2DP_ID3_ARTIST = 183;
    public static final int RETURN_A2DP_ID3_NAME = 182;
    public static final int RETURN_A2DP_ID3_TOTAL_TIME = 185;
    public static final int RETURN_A2DP_INFO = 19;
    public static final int RETURN_A2DP_NEED_CONECT = 150;
    public static final int RETURN_A2DP_OFF = 99;
    public static final int RETURN_A2DP_ON = 98;
    public static final int RETURN_A2DP_TIME = 103;
    public static final int RETURN_A2DP_AUDIO_INFO = 1001;
    public static final int RETURN_AUTOANSWER = 49;
    public static final int RETURN_AUTOCONNECT = 41;
    public static final int RETURN_AV = 15;
    public static final int RETURN_AV_INFO = 17;
    public static final int RETURN_AV_PLAY = 21;
    public static final int RETURN_BATTERY = 167;
    public static final int RETURN_BT_CORE_ERROR = 113;
    public static final int RETURN_BT_IVT_CRASH = 114;
    public static final int RETURN_CALL = 11;
    public static final int RETURN_CALLING = 87;
    public static final int RETURN_CALL_END = 86;
    public static final int RETURN_CALL_INCOMING = 85;
    public static final int RETURN_CALL_LOG = 27;
    public static final int RETURN_CALL_OUT = 84;
    public static final int RETURN_CLEAR_PAIR = 112;
    public static final int RETURN_CLMS = 151;
    public static final int RETURN_CMD_ERROR = 153;
    public static final int RETURN_CMD_OK = 131;
    public static final int RETURN_CMD_PWNG2_DISCONNECT = 164;
    public static final int RETURN_CPMC = 132;
    public static final int RETURN_CPML = 133;
    public static final int RETURN_DEVICE_INDEX = 125;
    public static final int RETURN_GOC_MUTE = 91;
    public static final int RETURN_GOC_UNMUTE = 92;
    public static final int RETURN_HFP = 7;
    public static final int RETURN_HFP_INFO = 9;
    public static final int RETURN_HFP_CONNECT_NAME = 777;
    public static final int RETURN_LANGUAGE = 128;
    public static final int RETURN_MIC_STATUS = 169;
    public static final int RETURN_MSG_MAX = 1048576;
    public static final int RETURN_NAME = 31;
    public static final int RETURN_OBD_DISCONNECT = 52;
    public static final int RETURN_PAIR_ADDR = 111;
    public static final int RETURN_PAIR_INFO = 4;
    public static final int RETURN_PAN_NET_CONNECT = 186;
    public static final int RETURN_PAN_NET_DISCONNECT = 188;
    public static final int RETURN_PBEI = 123;
    public static final int RETURN_PHONEBOOK_SIZE = 134;
    public static final int RETURN_PHONE_BOOK = 23;
    public static final int RETURN_PHONE_BOOK_CONNECT_STATUS = 97;
    public static final int RETURN_PHONE_BOOK_DATA = 24;
    public static final int RETURN_PHONE_BOOK_START = 89;
    public static final int RETURN_PHONE_CALLLOG_END = 999;
    public static final int RETURN_PIN = 33;
    public static final int RETURN_PPBU = 143;
    public static final int RETURN_PPDS = 145;
    public static final int RETURN_RPMT = 142;
    public static final int RETURN_RPRE = 140;
    public static final int RETURN_RREC = 138;
    public static final int RETURN_RRES = 144;
    public static final int RETURN_SEARCH = 29;
    public static final int RETURN_SEARCH_END = 78;
    public static final int RETURN_SEARCH_START = 77;
    public static final int RETURN_SEARCH_TYPE = 90;
    public static final int RETURN_SETTING = 35;
    public static final int RETURN_SIGNAL = 168;
    public static final int RETURN_STOP_SEARCH = 39;
    public static final int RETURN_SYNC_STATUS = 117;
    public static final int RETURN_UPDATE_STATUS = 238;
    public static final int RETURN_VOICE_STATUS = 136;
    public static final int RETURN_VOICE_SWITCH = 13;
    public static final int SET_LANGUAGE = 129;
    public static final int START_MODULE = 50;
    public static final int STOP_MODULE = 51;
    private static final String TAG = "TEST_ATBluetooth";
    public static final int WRITE_DATA = 770;
    public static int m3CallActiveIndex;
    public static String m3CallNumber;
    public static int m3CallStatus;
    private static ATBluetooth mATBluetooth;
    public static int mBTType;
    public static int mCallingNum;
    public static int mCurrentHFP;
    public static String mCurrentMac;
    public static int mIVTI145;
    public static boolean mShowPin;
    public static boolean mSuppotAutoAnswer;
    public static boolean mSuppotEditPin;
    public static int mTime;
    public static int mTime2;
    private BtCmd mBtCmd;
    public String mBtName = "";
    public String mBtPin = "";
    private OnBtPairListChangeListener mPairListChangedListener;
    private OnBtConnecStatusChangedListener mConnecStatusListener;
    private BtCallBack mBtCallback;

    public void setBtCallback(BtCallBack mBtCallback) {
        logTest("setBtCallback");
        this.mBtCallback = mBtCallback;
    }
    IBtCallback mCallBack = new IBtCallback() {
        @Override
        public void callback(int var1, int var2, String var3, String var4) {
            btCallback(var1, var2, var3, var4);
        }
    };
    private boolean mCloseIvtModule = false;
    private int mCrashIVTLetd = 0;
    private long mCrashIVTLetdTime = 0L;
    private Handler mHandlerCheckIvtErr = new Handler() {
        public void handleMessage(Message var1) {
            switch (var1.what) {
                case 0:
                    ATBluetooth.this.mHandlerCheckIvtErr.removeMessages(0);
                    if (SystemClock.uptimeMillis() - ATBluetooth.this.mStartWatchDogTime > 30000L) {
                        Log.e(TAG, "!!!!!!!!! stop check ivt carsh");
                        return;
                    }

                    short var3 = 1400;
                    short var2 = var3;
                    if (System.currentTimeMillis() - ATBluetooth.this.mCrashIVTLetdTime >= 1400L) {
//                        ATBluetooth.access$408(ATBluetooth.this);
                        ATBluetooth.this.write(30);
                        var2 = var3;
                        if (ATBluetooth.this.mCrashIVTLetd >= 3) {
                            StringBuilder var4 = new StringBuilder();
                            var4.append("ivt carsh?? ");
                            var4.append(ATBluetooth.this.mCrashIVTLetd);
                            Log.e(TAG, "handleMessage---" + var4.toString());
                            logTest("diao yong caooback 6");
                            mCallBack.callback(113, 2, (String) null, (String) null);
                            ATBluetooth.this.mCrashIVTLetd = -5;
                            var2 = 5000;
                        }
                    }

                    ATBluetooth.this.mHandlerCheckIvtErr.sendEmptyMessageDelayed(0, (long) var2);
                    return;
                case 1:
                    logTest("diao yong caooback 7");
                    mCallBack.callback(114, 0, (String) null, (String) null);
                    return;
                default:
            }
        }
    };
    private Handler mHandlerDelay = new Handler() {
        @Override
        public void handleMessage(Message var1) {
            ATBluetooth.this.write(var1.what, var1.arg1, var1.arg2);
        }
    };
    private Handler mHandlerUI;
    public boolean mHardwareStatus = false;
    private long mLastCmdWrite = 0L;
    private long mStartWatchDogTime = 0L;
    private ArrayList<ATBluetooth.THandler> mTHandler = new ArrayList();

    static {
        System.loadLibrary("BT");
        mBTType = 0;
        mSuppotAutoAnswer = true;
        mSuppotEditPin = true;
        mIVTI145 = 0;
        mCallingNum = 0;
        m3CallActiveIndex = 0;
        m3CallStatus = 0;
        mShowPin = true;
        mCurrentHFP = 0;
    }

    public ATBluetooth() {
        if (mBTType == 0) {
            this.mBtCmd = new IVT();
        } else if (mBTType == 2) {
            this.mBtCmd = new GOC();
        } else if (mBTType == 1) {
            this.mBtCmd = new Parrot();
            mSuppotAutoAnswer = false;
            mSuppotEditPin = false;
        } else {
            this.mBtCmd = new IVT();
        }

        mBtCmd.setCallback(mCallBack);
    }

    public void setOnBtPairListChangeListener(OnBtPairListChangeListener listener) {
        mPairListChangedListener = listener;
    }

    public void setOnBtConnectStatusChangedListener(OnBtConnecStatusChangedListener listener) {
        mConnecStatusListener = listener;
    }

    private void btCallback(int cmdId, int param2, String param3, String param4) {
        logTest("btCallback---param1:" + cmdId + " param2:" + param2 + " p3:" + param3 +
                " p4:" + param4);
        if(null != mBtCallback){
            mBtCallback.onBtCallback(cmdId,param2,param3,param4);
        }
        switch (cmdId) {
            case RETURN_PAIR_INFO:
                if (null != mPairListChangedListener) {
                    BtDeviceBean btDeviceBean = new BtDeviceBean();
                    btDeviceBean.setBtAddress(param3);
                    btDeviceBean.setBtName(param4);
                    mPairListChangedListener.onBtPairListChanged(btDeviceBean, true);
                }
                break;
            case RETURN_A2DP_CONNECT_STATUS:
                SettingConfig.getInstance().setA2dpConnected(param2 == 3);
                break;
            case RETURN_HFP_INFO:
                boolean connect = param2 == 3;
                if (connect != SettingConfig.getInstance().isHFPConnected() && null != mConnecStatusListener) {
                    SettingConfig.getInstance().setHFPConnected(connect);
                    mConnecStatusListener.onBtConnectedChanged(connect);
                }

                if (!TextUtils.isEmpty(param3) && !param3.equals("null")) {
                    SettingConfig.getInstance().setConnectBtAdd(param3);
                }
                break;
            case RETURN_A2DP_ID3_NAME:
                EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_TITLE, param3));
                break;
            case RETURN_A2DP_OFF:
                EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_OFF, null));
                break;
            case RETURN_A2DP_ON:
                EventBusUtils.postEvent(new BluetoothEvent(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_ON, null));
                break;
        }

    }

    public static ATBluetooth create() {
        if (mATBluetooth == null) {
            initBTType();
            mATBluetooth = new ATBluetooth();
        }

        if (!mATBluetooth.mHardwareStatus) {
            mATBluetooth.open();
        }

        return mATBluetooth;
    }

    private void dataCallback(byte[] var1, int var2) {
        logTest("dataCallback---  NALAIDE");
        String s = null;
        try {
            s = new String(var1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logTest("dataCallback-----LEN:" + var2 + " value:" + var1[var1.length - 1] + " string:" + s);
        String var3 = new String(var1);
        StringBuilder var4 = new StringBuilder();
        var4.append("dataCallback:");
        var4.append(var3);
        Log.e(TAG, "dataCallback-----" + var4.toString());
        this.mCrashIVTLetdTime = System.currentTimeMillis();
        this.mCrashIVTLetd = 0;
        logTest("dataCallback---111");
        this.mBtCmd.dataCallback(var1, var2);
    }

    private static int getCurrenIVTBt() {
        byte var4 = 0;
        byte var3 = 0;
        byte var2 = var4;

        boolean var10001;
        Process var5;
        try {
            var5 = Runtime.getRuntime().exec("ls -l /oem/lib");
        } catch (Exception var23) {
            var10001 = false;
            return var2;
        }

        var2 = var4;

        try {
            var5.waitFor();
        } catch (Exception var22) {
            var10001 = false;
            return var2;
        }

        var2 = var4;

        InputStream var24;
        try {
            var24 = var5.getInputStream();
        } catch (Exception var21) {
            var10001 = false;
            return var2;
        }

        var2 = var4;

        InputStreamReader var6;
        try {
            var6 = new InputStreamReader(var24);
        } catch (Exception var20) {
            var10001 = false;
            return var2;
        }

        var2 = var4;

        BufferedReader var7;
        try {
            var7 = new BufferedReader(var6);
        } catch (Exception var19) {
            var10001 = false;
            return var2;
        }

        int var1 = 0;

        byte var0;
        while (true) {
            var0 = var3;
            if (var1 >= 10) {
                break;
            }

            var2 = var4;

            String var8;
            try {
                var8 = var7.readLine();
            } catch (Exception var16) {
                var10001 = false;
                return var2;
            }

            if (var8 == null) {
                var0 = var3;
                break;
            }

            var2 = var4;

            label119:
            {
                try {
                    if (!var8.contains("blueletd -> ./i140/blueletd")) {
                        break label119;
                    }
                } catch (Exception var18) {
                    var10001 = false;
                    return var2;
                }

                var0 = 0;
                break;
            }

            var2 = var4;

            label112:
            {
                try {
                    if (var8.contains("blueletd -> ./i145/blueletd")) {
                        break label112;
                    }
                } catch (Exception var17) {
                    var10001 = false;
                    return var2;
                }

                ++var1;
                continue;
            }

            var0 = 1;
            break;
        }

        var2 = var0;

        try {
            var24.close();
        } catch (Exception var15) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        try {
            var6.close();
        } catch (Exception var14) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        try {
            var7.close();
        } catch (Exception var13) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        StringBuilder var25;
        try {
            var25 = new StringBuilder();
        } catch (Exception var12) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        try {
            var25.append("ivt_type:");
        } catch (Exception var11) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        try {
            var25.append(var0);
        } catch (Exception var10) {
            var10001 = false;
            return var2;
        }

        var2 = var0;

        try {
            Log.e(TAG, "getCurrentIVIBt-----" + var25.toString());
            return var0;
        } catch (Exception var9) {
            var10001 = false;
            return var2;
        }
    }

    private static void initBTType() {
        String var1 = MachineConfig.getProperty("bt_type");
        String var0 = var1;
        if (var1 == null) {
            var0 = MachineConfig.getPropertyReadOnly("bt_type");
        }

        if (var0 != null) {
            try {
                mBTType = Integer.valueOf(var0);
                mBTType &= 255;
            } catch (Exception var2) {
            }
        }

        if (mBTType == 2) {
            mShowPin = false;
        }

        if (mBTType == 3 || mBTType == 4) {
            mBTType = 2;
        }

        if (mBTType == 0) {
            mIVTI145 = getCurrenIVTBt();
        }

    }

    public static boolean isSupport3Call() {
        return mBTType == 3 || mBTType == 2 || mBTType == 4;
    }

    private native int nativeSendCommand(int var1, int var2, int var3, byte[] var4);

    private void updateCallback(int var1) {
        StringBuilder var2 = new StringBuilder();
        var2.append("updateCallback:");
        var2.append(var1);
        Log.e(TAG, "updateCallback-----" + var2.toString());
        if (mCallBack != null) {
            logTest("diao yong caooback 8");
            mCallBack.callback(240, var1, (String) null, (String) null);
        }

    }

    public void addHandler(String param1, Handler param2) {
        // $FF: Couldn't be decompiled
    }

    public void close() {
        this.mHardwareStatus = false;
        this.write(255);
    }

    public void closeIvtModule(boolean var1) {
        this.mCloseIvtModule = var1;
    }

    public void destroy() {
        if (mATBluetooth != null) {
            mATBluetooth.write(255);
            mATBluetooth = null;
        }

    }

    public boolean getCloseIvtModeule() {
        return this.mCloseIvtModule;
    }

    public Handler getHandler(String var1) {
        Iterator var2 = this.mTHandler.iterator();

        ATBluetooth.THandler var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (ATBluetooth.THandler) var2.next();
        } while (var3.mTag == null || !var3.mTag.equals(var1));

        return var3.mHandler;
    }

    public boolean getMicMute() {
        return this.mBtCmd.mMicMute;
    }

    public Handler getUIHandler() {
        return this.mHandlerUI;
    }

    public boolean getVoiceSwitchLocal() {
        return this.mBtCmd.mVoiceSwitchLocal;
    }

    public boolean open() {
        int var1 = mATBluetooth.write(128, mBTType);
        StringBuilder var2 = new StringBuilder();
        var2.append("");
        var2.append(var1);
        Log.e(TAG, "open-----" + var2.toString());
        if (var1 == 0) {
            this.mHardwareStatus = true;
        } else {
            this.mHardwareStatus = false;
            Util.setFileValue("/sys/class/ak/source/bluetoothsw", 1);
            if (mBTType == 0) {
                Util.setProperty("ctl.start", "ivt_blueletd");
            } else {
                var1 = mBTType;
            }
        }

        if (mBTType == 0) {
            this.startcheckIVTErr();
        }

        return this.mHardwareStatus;
    }

    public void removeHandler(String var1) {
        ArrayList var2 = this.mTHandler;
        synchronized (var2) {
        }

        Throwable var10000;
        boolean var10001;
        label250:
        {
            Iterator var3;
            try {
                var3 = this.mTHandler.iterator();
            } catch (Throwable var23) {
                var10000 = var23;
                var10001 = false;
                break label250;
            }

            while (true) {
                try {
                    if (var3.hasNext()) {
                        ATBluetooth.THandler var4 = (ATBluetooth.THandler) var3.next();
                        if (var4.mTag == null || !var4.mTag.equals(var1)) {
                            continue;
                        }

                        this.mTHandler.remove(var4);
                    }
                    break;
                } catch (Throwable var24) {
                    var10000 = var24;
                    var10001 = false;
                    break label250;
                }
            }

            label229:
            try {
                return;
            } catch (Throwable var22) {
                var10000 = var22;
                var10001 = false;
                break label229;
            }
        }

        while (true) {
            Throwable var25 = var10000;

            try {
                throw var25;
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                continue;
            }
        }
    }

    public void requestSource() {
        this.requestSource(8);
    }

    public void requestSource(int var1) {
        this.write(769, 192, var1);
    }

    public void sendHandler(String var1, int var2) {
        this.sendHandler(var1, var2, 0, 0, (Object) null);
    }

    public void sendHandler(String var1, int var2, int var3, int var4) {
        this.sendHandler(var1, var2, var3, var4, (Object) null);
    }

    public void sendHandler(String var1, int var2, int var3, int var4, Object var5) {
        ArrayList var6 = this.mTHandler;
        synchronized (var6) {
        }

        Throwable var10000;
        boolean var10001;
        label213:
        {
            label207:
            {
                Exception var32;
                try {
                    try {
                        if (this.getHandler(var1) != null) {
                            this.getHandler(var1).obtainMessage(var2, var3, var4, var5).sendToTarget();
                        }
                        break label207;
                    } catch (Exception var30) {
                        var32 = var30;
                    }
                } catch (Throwable var31) {
                    var10000 = var31;
                    var10001 = false;
                    break label213;
                }

                try {
                    Log.e(TAG, "sendHandler----" + Log.getStackTraceString(var32));
                } catch (Throwable var29) {
                    var10000 = var29;
                    var10001 = false;
                    break label213;
                }
            }

            label199:
            try {
                return;
            } catch (Throwable var28) {
                var10000 = var28;
                var10001 = false;
                break label199;
            }
        }

        while (true) {
            Throwable var33 = var10000;

            try {
                throw var33;
            } catch (Throwable var27) {
                var10000 = var27;
                var10001 = false;
                continue;
            }
        }
    }

    public void sendHandler(String var1, int var2, Object var3) {
        this.sendHandler(var1, var2, 0, 0, var3);
    }

    public void setUIHandler(Handler var1) {
        this.mHandlerUI = var1;
    }

    public void setVoiceSwitchLocal(boolean var1) {
        this.mBtCmd.mMicMute = var1;
    }

    public void startcheckIVTErr() {
        this.mStartWatchDogTime = SystemClock.uptimeMillis();
        StringBuilder var1 = new StringBuilder();
        var1.append("startcheckIVTErr: ");
        var1.append(this.mCrashIVTLetd);
        Log.e(TAG, "startcheckIVIErr---" + var1.toString());
        this.mHandlerCheckIvtErr.removeMessages(0);
        this.mHandlerCheckIvtErr.sendEmptyMessageDelayed(0, 1400L);
    }

    public int write(int var1) {
        return this.write(var1, 0, 0, (String) null);
    }

    public int write(int var1, int var2) {
        return this.write(var1, var2, 0, (String) null);
    }

    public int write(int var1, int var2, int var3) {
        return this.write(var1, var2, var3, (String) null);
    }

    public int write(int var1, int var2, int var3, String var4) {
        StringBuilder var5 = new StringBuilder();
        var5.append(var1);
        var5.append(":");
        var5.append(var2);
        var5.append(":");
        var5.append(var3);
        var5.append(":");
        var5.append(var4);
        Log.e(TAG, "write---000:" + var5.toString());
        if (this.mCloseIvtModule) {
            Log.e("write", "write close");
            return 0;
        } else {
            byte[] var6 = this.mBtCmd.getCmd(var1, var2, var3, var4);
            if (var6 != null) {
                var5 = new StringBuilder();
                var5.append(new String(var6));
                var5.append(" len:");
                var5.append(var6.length);
                Log.e(TAG, "write-----" + var5.toString());
                return this.nativeSendCommand(770, var2, var3, var6);
            } else {
                return this.nativeSendCommand(var1, var2, var3, var6);
            }
        }
    }

    public int write(int var1, int var2, String var3) {
        return this.write(var1, var2, 0, var3);
    }

    public int write(int var1, String var2) {
        return this.write(var1, 0, 0, var2);
    }

    public int writeDirect(int var1, byte[] var2) {
        return this.nativeSendCommand(var1, 0, 0, var2);
    }

    public void write_delay(int var1, int var2, int var3, int var4) {
        this.mHandlerDelay.sendMessageDelayed(this.mHandlerDelay.obtainMessage(var1, var2, var3),
                (long) var4);
    }

    public class THandler {
        Handler mHandler;
        String mTag;

        public THandler(String var2, Handler var3) {
            this.mTag = var2;
            this.mHandler = var3;
        }
    }

    public class TObject {
        public Object obj2;
        public Object obj3;

        public TObject(Object var2, Object var3) {
            this.obj2 = var2;
            this.obj3 = var3;
        }
    }


    private void logTest(String msg) {
        Log.e("Test_ATBluetooth", "-----" + msg);
    }
}
