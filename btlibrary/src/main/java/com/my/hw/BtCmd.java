//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my.hw;

import android.util.Log;

import com.common.util.Util;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class BtCmd {
    static final String TAG = "BtCmd";
    public static final int TYPE_CMD_RETURN = 1;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_PARAM1 = 2;
    public static final int TYPE_PARAM12 = 6;
    public static final int TYPE_PARAM123 = 14;
    public static final int TYPE_PARAM23 = 12;
    public static final int TYPE_PARAM_INT1 = 2;
    public static final int TYPE_PARAM_STR1 = 4;
    public static final int TYPE_PARAM_STR2 = 8;
    IBtCallback mCallBack;
    public boolean mMicMute = false;
    protected ArrayList<BtCmd.ReceiveCmdData> mReceiveCmd = new ArrayList();
    protected ArrayList<BtCmd.SendCmdData> mSendCmd = new ArrayList();
    public boolean mVoiceSwitchLocal = true;

    public BtCmd() {
    }

    public void addReceiveCmd(int var1, String var2) {
        this.mReceiveCmd.add(new BtCmd.ReceiveCmdData(var1, var2, 0));
    }

    public void addReceiveCmd(int var1, String var2, int var3) {
        this.mReceiveCmd.add(new BtCmd.ReceiveCmdData(var1, var2, var3));
    }

    public void addSendCmd(int var1, String var2) {
        this.mSendCmd.add(new BtCmd.SendCmdData(var1, var2, 0));
    }

    public void addSendCmd(int var1, String var2, int var3) {
        this.mSendCmd.add(new BtCmd.SendCmdData(var1, var2, var3));
    }

    private void logTest(String msg) {
        Log.e("Test_BtCmd", "-----" + msg);
    }

    public int dataCallback(byte[] var1, int var2) {
        logTest("BtCmd-----dataCallback--");
        if (this.mCallBack == null) {
            return 0;
        } else {
            byte var7 = 0;
            int var5 = 0;
            byte var9 = 0;
            byte var8 = 0;
            Object var15 = null;
            Object var16 = null;
            String var11 = null;
            Object var13 = null;
            Object var14 = null;
            byte var6 = 2;
            Iterator var18 = this.mReceiveCmd.iterator();

            int var3;
            int var4;
            String var10;
            String var12;
            while (true) {
                var4 = var7;
                var3 = var9;
                var10 = (String) var16;
                var12 = (String) var13;
                if (!var18.hasNext()) {
                    break;
                }

                BtCmd.ReceiveCmdData var17 = (BtCmd.ReceiveCmdData) var18.next();
                if (var17.mCmd != null) {
                    byte[] var31 = var17.mCmd.getBytes();
                    if (var17.mType == 1) {
                        if (var1[0] == var31[0]) {
                            var4 = var7;
                            var3 = var9;
                            var10 = (String) var16;
                            var12 = (String) var13;
                            break;
                        }
                    } else if (var31.length >= 2 && var1[0] == var31[0] && var1[1] == var31[1]) {
                        int var30 = var17.mId;
                        var3 = var8;
                        var4 = var6;
                        var10 = (String) var15;

                        label95:
                        {
                            Exception var10000;
                            label118:
                            {
                                boolean var10001;
                                label93:
                                {
                                    try {
                                        if ((var17.mType & 2) == 0) {
                                            break label93;
                                        }
                                    } catch (Exception var26) {
                                        var10000 = var26;
                                        var10001 = false;
                                        break label118;
                                    }

                                    byte var28 = var1[2];
                                    var4 = 2 + 1;
                                    var3 = var28 - 48;
                                }

                                int var29 = var4;
                                var5 = var3;
                                var10 = (String) var15;

                                label119:
                                {
                                    try {
                                        if ((var17.mType & 4) == 0) {
                                            break label119;
                                        }
                                    } catch (Exception var25) {
                                        var10000 = var25;
                                        var10001 = false;
                                        break label118;
                                    }

                                    var5 = var3;
                                    var10 = (String) var15;

                                    label120:
                                    {
                                        try {
                                            if ((var17.mType & 8) != 0) {
                                                break label120;
                                            }
                                        } catch (Exception var24) {
                                            var10000 = var24;
                                            var10001 = false;
                                            break label118;
                                        }

                                        var5 = var3;
                                        var10 = (String) var15;

                                        try {
                                            var17.mLen2 = var2 - var4;
                                        } catch (Exception var23) {
                                            var10000 = var23;
                                            var10001 = false;
                                            break label118;
                                        }
                                    }

                                    var5 = var3;
                                    var10 = (String) var15;

                                    try {
                                        var11 = new String(var1, var4, var17.mLen2);
                                    } catch (Exception var22) {
                                        var10000 = var22;
                                        var10001 = false;
                                        break label118;
                                    }

                                    var5 = var3;
                                    var10 = var11;

                                    try {
                                        var29 = var4 + var17.mLen2;
                                    } catch (Exception var21) {
                                        var10000 = var21;
                                        var10001 = false;
                                        break label118;
                                    }
                                }

                                var12 = (String) var14;
                                var5 = var3;
                                var10 = var11;

                                try {
                                    if ((var17.mType & 8) == 0) {
                                        break label95;
                                    }
                                } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                    break label118;
                                }

                                var5 = var3;
                                var10 = var11;

                                try {
                                    var12 = new String(var1, var29, var2 - var29);
                                    break label95;
                                } catch (Exception var19) {
                                    var10000 = var19;
                                    var10001 = false;
                                }
                            }

                            Exception var27 = var10000;
                            StringBuilder var32 = new StringBuilder();
                            var32.append("dataCallback err:");
                            var32.append(var27);
                            Log.d("BtCmd", var32.toString());
                            var4 = var30;
                            var3 = var5;
                            var12 = (String) var13;
                            break;
                        }

                        var4 = var30;
                        var10 = var11;
                        break;
                    }
                }
            }

            if (var4 != 0) {
                this.mCallBack.callback(var4, var3, var10, var12);
            }

            return 0;
        }
    }

    public byte[] getCmd(int var1, int var2, int var3, String var4) {
        Object var6 = null;
        Iterator var8 = this.mSendCmd.iterator();

        byte[] var5;
        while (true) {
            var5 = (byte[]) var6;
            if (!var8.hasNext()) {
                break;
            }

            BtCmd.SendCmdData var7 = (BtCmd.SendCmdData) var8.next();
            if (var7.mId == var1) {
                var2 = 5 + var7.mCmd.length();
                var1 = var2;
                if (var4 != null) {
                    var1 = var2 + var4.length();
                }

                byte[] var9 = new byte[var1 + 1];
                var9[0] = 65;
                var9[1] = 84;
                var9[2] = 35;
                var9[var1 - 2] = 13;
                var9[var1 - 1] = 10;
                var9[var1] = 0;
                Util.byteArrayCopy(var9, var7.mCmd.getBytes(), 3, 0, var7.mCmd.length());
                var5 = var9;
                if (var4 != null) {
                    Util.byteArrayCopy(var9, var4.getBytes(), 3 + var7.mCmd.length(), 0,
                            var4.length());
                    return var9;
                }
                break;
            }
        }

        return var5;
    }

    public void setCallback(IBtCallback var1) {
        this.mCallBack = var1;
    }

    public class ReceiveCmdData {
        String mCmd;
        int mId;
        int mLen2;
        int mLen3;
        int mType;

        public ReceiveCmdData(int var2, String var3, int var4) {
            this.mId = var2;
            this.mCmd = var3;
            this.mType = var4;
        }

        public ReceiveCmdData(int var2, String var3, int var4, byte var5, int var6, int var7) {
            this.mId = var2;
            this.mCmd = var3;
            this.mType = var4;
            this.mLen2 = var6;
            this.mLen3 = var7;
        }
    }

    public class SendCmdData {
        String mCmd;
        int mId;
        int mType;

        public SendCmdData(int var2, String var3, int var4) {
            this.mId = var2;
            this.mCmd = var3;
            this.mType = var4;
        }
    }
}
