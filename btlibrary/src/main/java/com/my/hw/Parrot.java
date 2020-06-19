//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my.hw;

import android.util.Log;
import com.common.util.Util;
import com.my.hw.BtCmd.ReceiveCmdData;
import com.my.hw.BtCmd.SendCmdData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Parrot extends BtCmd {
    public static String mDeviceIndex;
    public static String mMediaIndex = null;
    public static ArrayList<String> mMusicFileInfo = new ArrayList();
    public static int mSyncPhoneBookeStatus = -1;
    private int[] mActivityPhone = new int[6];
    public int mCurA2DPFileNum = 0;
    public String mCurA2DPPath = "";
    private byte[] mParrotPreCmd = null;
    private int mPhoneBookNameSeq = 1;
    private int mPreCmd = 0;
    private int mPreNeedOKCmd = 0;
    private int mRRESNum = 0;

    public Parrot() {
        this.init();
    }

    private boolean checkPhoneStatus(int var1, int var2) {
        if (var1 < this.mActivityPhone.length) {
            this.mActivityPhone[var1] = var2;
            if (var2 != 0) {
                return true;
            }

            for(var1 = 0; var1 < this.mActivityPhone.length; ++var1) {
                if (this.mActivityPhone[var1] == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    private void clearPhoneStatus() {
        for(int var1 = 0; var1 < this.mActivityPhone.length; ++var1) {
            this.mActivityPhone[var1] = 0;
        }

    }

    private int getPhoneActivityIndex() {
        for(int var1 = 0; var1 < this.mActivityPhone.length; ++var1) {
            if (this.mActivityPhone[var1] == 1) {
                return var1;
            }
        }

        return 0;
    }

    private void init() {
        this.addSendCmd(239, "*PBSU=1");
        this.addSendCmd(88, "+CGMREX");
        this.addSendCmd(147, "*CPCC=");
        this.addSendCmd(163, "*PPNO=");
        this.addSendCmd(154, "*AADC=");
        this.addSendCmd(155, "*ALAC=");
        this.addSendCmd(146, "*AVOL=");
        this.addSendCmd(135, "*RSTS?");
        this.addSendCmd(137, "*RREC=");
        this.addSendCmd(139, "*RPRE=");
        this.addSendCmd(141, "*RPMT=");
        this.addSendCmd(122, "*PSSP=0");
        this.addSendCmd(119, "*PSDM=1");
        this.addSendCmd(120, "*PSCA=1");
        this.addSendCmd(121, "*PDSM=0");
        this.addSendCmd(124, "*PLRU");
        this.addSendCmd(8, "*PIND=?");
        this.addSendCmd(126, "*PLCC");
        this.addSendCmd(38, "*PSBD=");
        this.addSendCmd(3, "*PLPD");
        this.addSendCmd(28, "*PBDI=1");
        this.addSendCmd(39, "*PBDI=0");
        this.addSendCmd(30, "*PSFN");
        this.addSendCmd(109, "BA");
        this.addSendCmd(5, "*PPDE");
        this.addSendCmd(69, "*PATR=0");
        this.addSendCmd(70, "*PATR=1");
        this.addSendCmd(73, "*PSCM=1");
        this.addSendCmd(74, "*PSCM=0");
        this.addSendCmd(75, "MP");
        this.addSendCmd(76, "MQ");
        this.addSendCmd(25, "CM");
        this.addSendCmd(40, "*PSCM?");
        this.addSendCmd(32, "*PPAU?");
        this.addSendCmd(10, "D");
        this.addSendCmd(66, "+CHUP");
        this.addSendCmd(65, "+CHUP");
        this.addSendCmd(67, "A");
        this.addSendCmd(68, "+VTS=");
        this.addSendCmd(57, "*PSBD=P");
        this.addSendCmd(110, "*PADDR=");
        this.addSendCmd(22, "*PPMS=P1");
        this.addSendCmd(115, "*PEDS=1");
        this.addSendCmd(116, "*PVCARD=");
        this.addSendCmd(152, "*PVCARD=");
        this.addSendCmd(118, "*PDCT=P1,1");
        this.addSendCmd(1, "BE");
        this.addSendCmd(106, "CY");
        this.addSendCmd(96, "PW");
        this.addSendCmd(80, "*CPLY=2");
        this.addSendCmd(81, "*CPLY=1");
        this.addSendCmd(83, "*CPLY=3");
        this.addSendCmd(82, "*CPLY=4");
        this.addSendCmd(100, "*CPGM=");
        this.addSendCmd(108, "*CGPS");
        this.addSendCmd(107, "RN");
        this.addSendCmd(127, "*RSCL?");
        this.addSendCmd(129, "*RSCL=");
        this.addSendCmd(148, "*CLMS=0");
        this.addSendCmd(156, "*DGBM=");
        this.addSendCmd(157, "*DSCD=");
        this.addSendCmd(158, "*DGCD=");
        this.addSendCmd(159, "*DGEC=");
        this.addSendCmd(160, "*DLSE=");
        this.addSendCmd(161, "*PBSCEX=0");
        this.addSendCmd(162, "*DLPE=");
        this.addReceiveCmd(101, "*CPGM:", 4);
        this.addReceiveCmd(101, "*PBCV:", 4);
    }

    @Override
    public int dataCallback(byte[] var1, int var2) {
        byte[] var6 = new byte[var2];
        Util.byteArrayCopy(var6, var1, 0, 0, var2);
        var1 = var6;
        int var3 = var2;
        if (this.mParrotPreCmd != null) {
            var3 = this.mParrotPreCmd.length + var2;
            var1 = new byte[var3];
            Util.byteArrayCopy(var1, this.mParrotPreCmd, 0, 0, this.mParrotPreCmd.length);
            Util.byteArrayCopy(var1, var6, this.mParrotPreCmd.length, 0, var2);
        }

        String[] var9 = (new String(var1)).split("\r\n");
        boolean var5 = false;
        boolean var8;
        if (var3 > 2 && var1[var3 - 2] == 13 && var1[var3 - 1] == 10) {
            this.mParrotPreCmd = null;
            var8 = true;
        } else {
            int var4 = var3 - 1;

            while(true) {
                var2 = var4;
                if (var4 <= 2) {
                    break;
                }

                if (var1[var4 - 2] == 13 && var1[var4 - 1] == 10) {
                    var2 = var4 - 2;
                    break;
                }

                --var4;
            }

            var4 = var3;
            if (var2 > 2) {
                var4 = var3 - var2;
            } else {
                var2 = 0;
            }

            this.mParrotPreCmd = new byte[var4];
            Util.byteArrayCopy(this.mParrotPreCmd, var1, 0, var2, var4);
            new String(this.mParrotPreCmd);
            var8 = var5;
        }

        StringBuilder var7;
        for(var3 = 0; var3 < var9.length - 1; ++var3) {
            if (var9[var3].length() > 0) {
                var1 = var9[var3].getBytes();
                this.dataCallback2(var1, var1.length);
                var7 = new StringBuilder();
                var7.append("doParrotCmd:");
                var7.append(var9[var3]);
                Log.d("BtCmd", var7.toString());
            }
        }

        if (var8 && var9[var9.length - 1].length() > 0) {
            var1 = var9[var9.length - 1].getBytes();
            this.dataCallback2(var1, var1.length);
            var7 = new StringBuilder();
            var7.append("2doParrotCmd:");
            var7.append(var9[var9.length - 1]);
            Log.d("BtCmd", var7.toString());
        }

        return 0;
    }

    public int dataCallback2(byte[] var1, int var2) {
        if (this.mCallBack == null) {
            return 0;
        } else {
            short var3;
            int var4;
            byte var8;
            Exception var10000;
            boolean var10001;
            String var20;
            String var21;
            String var22;
            Exception var116;
            int var117;
            int var120;
            String var123;
            StringBuilder var131;
            label1249: {
                byte var6;
                Object var28;
                Object var30;
                label1323: {
                    var8 = 0;
                    byte var9 = 0;
                    byte var7 = 0;
                    byte var15 = 0;
                    byte var16 = 0;
                    byte var11 = 0;
                    byte var17 = 0;
                    var3 = 0;
                    byte var5 = 0;
                    byte var10 = 0;
                    var6 = 0;
                    byte var18 = 0;
                    byte var12 = 0;
                    byte var13 = 0;
                    var4 = 0;
                    String[] var23 = null;
                    var20 = null;
                    Object var31 = null;
                    var28 = null;
                    Object var29 = null;
                    Object var32 = null;
                    Object var33 = null;
                    StringBuilder var24 = null;
                    var22 = null;
                    var21 = null;
                    String[] var26 = null;
                    var30 = null;
                    Object var27 = null;
                    String[] var25 = null;
                    byte var19 = var1[0];
                    byte var14 = 3;
                    String[] var128;
                    if (var19 == 42 && var1[1] == 80 && var1[2] == 76 && var1[3] == 80 && var1[4] == 68) {
                        label1254: {
                            var3 = 4;
                            var117 = var1[7] - 48;

                            try {
                                var128 = (new String(var1, 6, var2 - 6)).split(",");
                            } catch (Exception var45) {
                                var10000 = var45;
                                var10001 = false;
                                break label1254;
                            }

                            var20 = var128[2];

                            try {
                                var21 = var128[1].substring(1, var128[1].length() - 1);
                            } catch (Exception var44) {
                                var10000 = var44;
                                var10001 = false;
                                break label1254;
                            }

                            var4 = var117;

                            try {
                                if (var128.length <= 2) {
                                    break label1249;
                                }
                            } catch (Exception var43) {
                                var10000 = var43;
                                var10001 = false;
                                break label1254;
                            }

                            var4 = var117;

                            try {
                                if (!var128[0].equals(mDeviceIndex)) {
                                    break label1249;
                                }
                            } catch (Exception var42) {
                                var10000 = var42;
                                var10001 = false;
                                break label1254;
                            }

                            var4 = var117 | 65536;
                            break label1249;
                        }
                    } else {
                        if (var1[0] == 77 && var1[1] == 70) {
                            var3 = 35;
                            byte var124 = var1[2];
                            var4 = var1[3] - 48 | var124 - 48 << 8;
                            var20 = var23[0];
                            var21 = var22;
                            break label1249;
                        }

                        if (var1[0] == 42 && var1[1] == 80 && var1[2] == 66 && var1[3] == 68 && var1[4] == 73) {
                            label724: {
                                label723: {
                                    label1255: {
                                        try {
                                            var128 = (new String(var1, 6, var2 - 6)).split(",");
                                            if (var128[0].equals("END")) {
                                                break label1255;
                                            }
                                        } catch (Exception var47) {
                                            var10000 = var47;
                                            var10001 = false;
                                            break label724;
                                        }

                                        var20 = var128[2];

                                        try {
                                            var21 = var128[1].substring(1, var128[1].length() - 1);
                                        } catch (Exception var46) {
                                            var10000 = var46;
                                            var10001 = false;
                                            break label724;
                                        }

                                        var3 = 29;
                                        break label723;
                                    }

                                    var3 = 78;
                                }

                                var4 = var5;
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 66 && var1[3] == 69 && var1[4] == 73) {
                            label727: {
                                try {
                                    var20 = (new String(var1, 6, var2 - 6)).split(",")[0];
                                } catch (Exception var48) {
                                    var10000 = var48;
                                    var10001 = false;
                                    break label727;
                                }

                                var3 = 123;
                                var4 = var5;
                                var21 = var22;
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 76 && var1[3] == 82 && var1[4] == 85) {
                            label1256: {
                                var3 = var8;
                                var4 = var5;
                                var20 = var23[0];
                                var21 = var22;

                                try {
                                    if (mDeviceIndex != null) {
                                        break label1249;
                                    }

                                    var20 = new String(var1, 6, var2 - 6);
                                    mDeviceIndex = var20;
                                } catch (Exception var49) {
                                    var10000 = var49;
                                    var10001 = false;
                                    break label1256;
                                }

                                var3 = 125;
                                var4 = var5;
                                var21 = var22;
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 73 && var1[3] == 78 && var1[4] == 68) {
                            label1257: {
                                var3 = var8;
                                var4 = var5;
                                var20 = var23[0];
                                var21 = var22;
                                if (var1[6] != 49) {
                                    break label1249;
                                }

                                var3 = var8;
                                var4 = var5;
                                var20 = var23[0];
                                var21 = var22;

                                try {
                                    if (ATBluetooth.mCurrentHFP >= 3) {
                                        break label1249;
                                    }
                                } catch (Exception var51) {
                                    var10000 = var51;
                                    var10001 = false;
                                    break label1257;
                                }

                                var3 = 9;
                                var4 = 3;

                                try {
                                    (new String(var1, 0, var2)).split(",");
                                    var21 = new String(var1, 7, 1);
                                } catch (Exception var50) {
                                    var10000 = var50;
                                    var10001 = false;
                                    break label1257;
                                }

                                var20 = var23[0];
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 83 && var1[3] == 70 && var1[4] == 78) {
                            label1258: {
                                var3 = 31;

                                try {
                                    var20 = new String(var1, 7, var2 - 8);
                                } catch (Exception var52) {
                                    var10000 = var52;
                                    var10001 = false;
                                    break label1258;
                                }

                                var4 = var5;
                                var21 = var22;
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 73 && var1[3] == 69 && var1[4] == 86) {
                            label1259: {
                                var3 = var8;
                                var4 = var5;
                                var20 = var23[0];
                                var21 = var22;
                                if (var1[6] != 49) {
                                    break label1249;
                                }

                                if (var1[8] != 48) {
                                    var3 = var8;
                                    var4 = var5;
                                    var20 = var23[0];
                                    var21 = var22;
                                    if (var1[6] == 49) {
                                        var3 = 9;
                                        var4 = 3;
                                        var20 = var23[0];
                                        var21 = var22;
                                    }
                                    break label1249;
                                }

                                var3 = 9;
                                var4 = 0;

                                try {
                                    this.clearPhoneStatus();
                                    mSyncPhoneBookeStatus = -1;
                                } catch (Exception var53) {
                                    var10000 = var53;
                                    var10001 = false;
                                    break label1259;
                                }

                                var20 = var23[0];
                                var21 = var22;
                            }
                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 76 && var1[3] == 67 && var1[4] == 67) {
                            label1260: {
                                try {
                                    var128 = (new String(var1, 6, var2 - 6)).split(",");
                                    var117 = Integer.valueOf(var128[0]);
                                } catch (Exception var112) {
                                    var10000 = var112;
                                    var10001 = false;
                                    break label1260;
                                }

                                if (var1[8] == 48) {
                                    var3 = 87;
                                    var4 = 6;

                                    try {
                                        this.checkPhoneStatus(var117, 1);
                                    } catch (Exception var111) {
                                        var10000 = var111;
                                        var10001 = false;
                                        break label1260;
                                    }
                                } else if (var1[8] != 50 && var1[8] != 51) {
                                    if (var1[8] == 52) {
                                        var3 = 85;
                                        var4 = 5;

                                        try {
                                            this.checkPhoneStatus(var117, 1);
                                        } catch (Exception var110) {
                                            var10000 = var110;
                                            var10001 = false;
                                            break label1260;
                                        }
                                    } else {
                                        label1199: {
                                            try {
                                                if (this.checkPhoneStatus(var117, 0)) {
                                                    break label1199;
                                                }
                                            } catch (Exception var114) {
                                                var10000 = var114;
                                                var10001 = false;
                                                break label1260;
                                            }

                                            var3 = 86;
                                            var4 = 3;
                                        }
                                    }
                                } else {
                                    var3 = 84;
                                    var4 = 4;

                                    try {
                                        this.checkPhoneStatus(var117, 1);
                                    } catch (Exception var109) {
                                        var10000 = var109;
                                        var10001 = false;
                                        break label1260;
                                    }
                                }

                                try {
                                    if (var128.length > 7) {
                                        var20 = var128[7].substring(1, var128[7].length() - 1);
                                        var21 = var128[5].substring(1, var128[5].length() - 1).replace(";", " ");
                                        break label1249;
                                    }
                                } catch (Exception var113) {
                                    var10000 = var113;
                                    var10001 = false;
                                    break label1260;
                                }

                                var20 = (String)var31;
                                var21 = var26[0];

                                try {
                                    if (var128.length <= 5) {
                                        break label1249;
                                    }

                                    var20 = var128[5].substring(1, var128[5].length() - 1);
                                } catch (Exception var54) {
                                    var10000 = var54;
                                    var10001 = false;
                                    break label1260;
                                }

                                var21 = var26[0];
                            }
                        } else {
                            String[] var122;
                            if (var1[0] == 42 && var1[1] == 80 && var1[2] == 66 && var1[3] == 83 && var1[4] == 78) {
                                label778: {
                                    label1264: {
                                        label1265: {
                                            try {
                                                var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                if (!var122[1].equals("0") || !var122[2].equals("1")) {
                                                    break label1265;
                                                }
                                            } catch (Exception var59) {
                                                var10000 = var59;
                                                var10001 = false;
                                                break label778;
                                            }

                                            var3 = 9;
                                            var4 = 3;

                                            try {
                                                mDeviceIndex = var122[0];
                                                break label1264;
                                            } catch (Exception var56) {
                                                var10000 = var56;
                                                var10001 = false;
                                                break label778;
                                            }
                                        }

                                        var3 = var9;
                                        var4 = var10;

                                        try {
                                            if (var122[1].equals("0")) {
                                                break label1264;
                                            }
                                        } catch (Exception var58) {
                                            var10000 = var58;
                                            var10001 = false;
                                            break label778;
                                        }

                                        var3 = var9;
                                        var4 = var10;

                                        try {
                                            if (!var122[2].equals("1")) {
                                                break label1264;
                                            }
                                        } catch (Exception var57) {
                                            var10000 = var57;
                                            var10001 = false;
                                            break label778;
                                        }

                                        var3 = 9;
                                        var4 = 0;

                                        try {
                                            this.clearPhoneStatus();
                                            mSyncPhoneBookeStatus = -1;
                                        } catch (Exception var55) {
                                            var10000 = var55;
                                            var10001 = false;
                                            break label778;
                                        }
                                    }

                                    var20 = var23[0];
                                    var21 = var22;
                                }
                            } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 80 && var1[3] == 65 && var1[4] == 85) {
                                label781: {
                                    try {
                                        var20 = new String(var1, 9, 4);
                                    } catch (Exception var60) {
                                        var10000 = var60;
                                        var10001 = false;
                                        break label781;
                                    }

                                    var3 = 33;
                                    var4 = var5;
                                    var21 = var22;
                                }
                            } else {
                                if (var1[0] == 42 && var1[1] == 80 && var1[2] == 83 && var1[3] == 67 && var1[4] == 77) {
                                    var4 = var1[6] - 48;
                                    var3 = 41;
                                    var20 = var23[0];
                                    var21 = var22;
                                    break label1249;
                                }

                                if (var1[0] == 42 && var1[1] == 67 && var1[2] == 71 && var1[3] == 80 && var1[4] == 83) {
                                    label814: {
                                        label813: {
                                            byte var121;
                                            label812: {
                                                label1266: {
                                                    label810: {
                                                        label809: {
                                                            label808: {
                                                                label807: {
                                                                    try {
                                                                        var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                                        if (var122.length <= 2) {
                                                                            break label1266;
                                                                        }

                                                                        switch(Integer.valueOf(var122[0])) {
                                                                            case 0:
                                                                            case 8:
                                                                                break;
                                                                            case 1:
                                                                                break label807;
                                                                            case 2:
                                                                                break label808;
                                                                            case 3:
                                                                            case 4:
                                                                            case 5:
                                                                            case 7:
                                                                            default:
                                                                                return 0;
                                                                            case 6:
                                                                                break label809;
                                                                            case 9:
                                                                                break label810;
                                                                        }
                                                                    } catch (Exception var62) {
                                                                        var10000 = var62;
                                                                        var10001 = false;
                                                                        break label814;
                                                                    }

                                                                    var121 = 3;
                                                                    break label812;
                                                                }

                                                                var121 = 5;
                                                                break label812;
                                                            }

                                                            var121 = 4;
                                                            break label812;
                                                        }

                                                        var121 = 0;
                                                        break label812;
                                                    }

                                                    try {
                                                        var120 = Integer.valueOf(var122[1]);
                                                    } catch (Exception var61) {
                                                        var10000 = var61;
                                                        var10001 = false;
                                                        break label814;
                                                    }

                                                    if (var120 == 2) {
                                                        var121 = 4;
                                                    } else {
                                                        if (var120 != 1 && var120 != 0) {
                                                            return 0;
                                                        }

                                                        var121 = 5;
                                                    }
                                                    break label812;
                                                }

                                                var4 = -1;
                                                break label813;
                                            }

                                            var4 = var121;
                                        }

                                        var3 = 105;
                                        var20 = var23[0];
                                        var21 = var22;
                                    }
                                } else if (var1[0] == 42 && var1[1] == 67 && var1[2] == 80 && var1[3] == 77 && var1[4] == 67) {
                                    label817: {
                                        try {
                                            var20 = new String(var1, 6, var2 - 6);
                                        } catch (Exception var63) {
                                            var10000 = var63;
                                            var10001 = false;
                                            break label817;
                                        }

                                        var3 = 132;
                                        var4 = var5;
                                        var21 = var22;
                                    }
                                } else {
                                    if (var1[0] == 42 && var1[1] == 67 && var1[2] == 80 && var1[3] == 77 && var1[4] == 76) {
                                        var3 = 133;
                                        var4 = var5;
                                        var20 = var23[0];
                                        var21 = var22;
                                        break label1249;
                                    }

                                    if (var1[0] == 42 && var1[1] == 80 && var1[2] == 86 && var1[3] == 67 && var1[4] == 65 && var1[5] == 82 && var1[6] == 68) {
                                        label1267: {
                                            label1268: {
                                                try {
                                                    var26 = (new String(var1, 8, var2 - 8)).split(",");
                                                    if (var26.length != 2) {
                                                        break label1268;
                                                    }

                                                    if (var26[0].startsWith("END")) {
                                                        break label1323;
                                                    }
                                                } catch (Exception var115) {
                                                    var10000 = var115;
                                                    var10001 = false;
                                                    break label1267;
                                                }

                                                var3 = var7;
                                                var4 = var6;
                                                var20 = (String)var28;
                                                var21 = (String)var30;

                                                try {
                                                    if (this.mPreCmd != 152) {
                                                        break label1249;
                                                    }

                                                    var4 = Integer.valueOf(var26[1]);
                                                } catch (Exception var64) {
                                                    var10000 = var64;
                                                    var10001 = false;
                                                    break label1267;
                                                }

                                                var3 = 134;
                                                var20 = (String)var28;
                                                var21 = (String)var30;
                                                break label1249;
                                            }

                                            var3 = var7;
                                            var4 = var6;
                                            var20 = (String)var28;
                                            var21 = (String)var30;

                                            try {
                                                if (var26.length < 4) {
                                                    break label1249;
                                                }

                                                var117 = Integer.valueOf(var26[2]);
                                            } catch (Exception var78) {
                                                var10000 = var78;
                                                var10001 = false;
                                                break label1267;
                                            }

                                            if (var117 != 1) {
                                                var3 = var7;
                                                var4 = var6;
                                                var20 = (String)var28;
                                                var21 = (String)var30;
                                                if (var117 != 4) {
                                                    break label1249;
                                                }
                                            }

                                            if (var117 == 1) {
                                                try {
                                                    var25 = var26[3].substring(1, var26[3].length() - 1).split(";");
                                                } catch (Exception var68) {
                                                    var10000 = var68;
                                                    var10001 = false;
                                                    break label1267;
                                                }

                                                var20 = "";
                                                var123 = "";

                                                label875: {
                                                    try {
                                                        if (var25.length <= 0) {
                                                            break label875;
                                                        }
                                                    } catch (Exception var77) {
                                                        var10000 = var77;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    var123 = var25[0];
                                                }

                                                label870: {
                                                    try {
                                                        if (var25.length <= 1) {
                                                            break label870;
                                                        }
                                                    } catch (Exception var76) {
                                                        var10000 = var76;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    var20 = var25[1];
                                                }

                                                var21 = var20;
                                                var22 = var123;

                                                label1270: {
                                                    try {
                                                        if (this.mPhoneBookNameSeq != 0) {
                                                            break label1270;
                                                        }
                                                    } catch (Exception var74) {
                                                        var10000 = var74;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    label862: {
                                                        try {
                                                            if (var25.length <= 0) {
                                                                break label862;
                                                            }
                                                        } catch (Exception var75) {
                                                            var10000 = var75;
                                                            var10001 = false;
                                                            break label1267;
                                                        }

                                                        var20 = var25[0];
                                                    }

                                                    var21 = var20;
                                                    var22 = var123;

                                                    try {
                                                        if (var25.length <= 1) {
                                                            break label1270;
                                                        }
                                                    } catch (Exception var73) {
                                                        var10000 = var73;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    var22 = var25[1];
                                                    var21 = var20;
                                                }

                                                label1271: {
                                                    label1272: {
                                                        try {
                                                            if (!Locale.getDefault().getLanguage().equals("zh")) {
                                                                break label1272;
                                                            }
                                                        } catch (Exception var72) {
                                                            var10000 = var72;
                                                            var10001 = false;
                                                            break label1267;
                                                        }

                                                        var20 = var22;

                                                        try {
                                                            if (var22.length() > 0) {
                                                                var131 = new StringBuilder();
                                                                var131.append(var22);
                                                                var131.append(" ");
                                                                var20 = var131.toString();
                                                            }
                                                        } catch (Exception var71) {
                                                            var10000 = var71;
                                                            var10001 = false;
                                                            break label1267;
                                                        }

                                                        try {
                                                            StringBuilder var125 = new StringBuilder();
                                                            var125.append(var20);
                                                            var125.append(var21);
                                                            var20 = var125.toString();
                                                            break label1271;
                                                        } catch (Exception var66) {
                                                            var10000 = var66;
                                                            var10001 = false;
                                                            break label1267;
                                                        }
                                                    }

                                                    var20 = var21;

                                                    try {
                                                        if (var21.length() > 0) {
                                                            var131 = new StringBuilder();
                                                            var131.append(var21);
                                                            var131.append(" ");
                                                            var20 = var131.toString();
                                                        }
                                                    } catch (Exception var70) {
                                                        var10000 = var70;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    try {
                                                        StringBuilder var133 = new StringBuilder();
                                                        var133.append(var20);
                                                        var133.append(var22);
                                                        var20 = var133.toString();
                                                    } catch (Exception var67) {
                                                        var10000 = var67;
                                                        var10001 = false;
                                                        break label1267;
                                                    }
                                                }

                                                var21 = var20;
                                                var20 = var24.toString();
                                            } else {
                                                var20 = var24.toString();
                                                var21 = var25[0];
                                                if (var117 == 4) {
                                                    try {
                                                        var20 = var26[3].substring(1, var26[3].length() - 1);
                                                    } catch (Exception var69) {
                                                        var10000 = var69;
                                                        var10001 = false;
                                                        break label1267;
                                                    }

                                                    var21 = var25[0];
                                                }
                                            }

                                            try {
                                                var4 = Integer.valueOf(var26[1]);
                                            } catch (Exception var65) {
                                                var10000 = var65;
                                                var10001 = false;
                                                break label1267;
                                            }

                                            var3 = 24;
                                        }
                                    } else {
                                        if (var1[0] == 42 && var1[1] == 82 && var1[2] == 83 && var1[3] == 84 && var1[4] == 83) {
                                            var3 = 136;
                                            var4 = var5;
                                            var20 = var23[0];
                                            var21 = var22;
                                            break label1249;
                                        }

                                        if (var1[0] == 42 && var1[1] == 80 && var1[2] == 80 && var1[3] == 78 && var1[4] == 79) {
                                            label887: {
                                                try {
                                                    this.mPhoneBookNameSeq = Integer.valueOf(new String(var1, 6, 1));
                                                } catch (Exception var79) {
                                                    var10000 = var79;
                                                    var10001 = false;
                                                    break label887;
                                                }

                                                var3 = var8;
                                                var4 = var5;
                                                var20 = var23[0];
                                                var21 = var22;
                                            }
                                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 80 && var1[3] == 68 && var1[4] == 83) {
                                            label890: {
                                                try {
                                                    var4 = Integer.valueOf(new String(var1, 6, var2 - 6));
                                                } catch (Exception var80) {
                                                    var10000 = var80;
                                                    var10001 = false;
                                                    break label890;
                                                }

                                                var3 = 145;
                                                var20 = var23[0];
                                                var21 = var22;
                                            }
                                        } else if (var1[0] == 42 && var1[1] == 67 && var1[2] == 76 && var1[3] == 77 && var1[4] == 83) {
                                            label1273: {
                                                try {
                                                    var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                } catch (Exception var83) {
                                                    var10000 = var83;
                                                    var10001 = false;
                                                    break label1273;
                                                }

                                                var4 = 0;

                                                label1274: {
                                                    try {
                                                        if (!var122[6].equals("1")) {
                                                            break label1274;
                                                        }
                                                    } catch (Exception var82) {
                                                        var10000 = var82;
                                                        var10001 = false;
                                                        break label1273;
                                                    }

                                                    var4 = 1;

                                                    try {
                                                        mMediaIndex = var122[0];
                                                    } catch (Exception var81) {
                                                        var10000 = var81;
                                                        var10001 = false;
                                                        break label1273;
                                                    }
                                                }

                                                var3 = 151;
                                                var20 = var23[0];
                                                var21 = var22;
                                            }
                                        } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 80 && var1[3] == 66 && var1[4] == 85) {
                                            label905: {
                                                try {
                                                    var4 = Integer.valueOf(new String(var1, 6, var2 - 6));
                                                    mSyncPhoneBookeStatus = var4;
                                                } catch (Exception var84) {
                                                    var10000 = var84;
                                                    var10001 = false;
                                                    break label905;
                                                }

                                                var3 = 143;
                                                var20 = var23[0];
                                                var21 = var22;
                                            }
                                        } else if (var1[0] == 42 && var1[1] == 82 && var1[2] == 82 && var1[3] == 69 && var1[4] == 83) {
                                            label931: {
                                                label1275: {
                                                    label1276: {
                                                        try {
                                                            var23 = (new String(var1, 6, var2 - 6)).split(",");
                                                            if (var23.length < 4) {
                                                                break label1276;
                                                            }

                                                            var120 = this.mRRESNum;
                                                        } catch (Exception var89) {
                                                            var10000 = var89;
                                                            var10001 = false;
                                                            break label931;
                                                        }

                                                        var22 = var23[1];
                                                        var4 = var120;
                                                        var20 = var22;
                                                        var21 = (String)var27;

                                                        try {
                                                            if (var23.length <= 4) {
                                                                break label1275;
                                                            }

                                                            var131 = new StringBuilder();
                                                            var131.append(var23[4]);
                                                            var131.append(",");
                                                            var131.append(var23[5]);
                                                            var21 = var131.toString();
                                                        } catch (Exception var88) {
                                                            var10000 = var88;
                                                            var10001 = false;
                                                            break label931;
                                                        }

                                                        var4 = var120;
                                                        var20 = var22;
                                                        break label1275;
                                                    }

                                                    var4 = var18;
                                                    var20 = (String)var29;
                                                    var21 = (String)var27;

                                                    try {
                                                        if (var23.length < 3) {
                                                            break label1275;
                                                        }

                                                        var120 = Integer.valueOf(var23[1]);
                                                    } catch (Exception var87) {
                                                        var10000 = var87;
                                                        var10001 = false;
                                                        break label931;
                                                    }

                                                    if (var120 == 1) {
                                                        try {
                                                            var4 = Integer.valueOf(var23[2]);
                                                            this.mRRESNum = var4;
                                                        } catch (Exception var86) {
                                                            var10000 = var86;
                                                            var10001 = false;
                                                            break label931;
                                                        }

                                                        var20 = (String)var29;
                                                        var21 = (String)var27;
                                                    } else {
                                                        try {
                                                            this.mRRESNum = 0;
                                                        } catch (Exception var85) {
                                                            var10000 = var85;
                                                            var10001 = false;
                                                            break label931;
                                                        }

                                                        var4 = -var120;
                                                        var20 = (String)var29;
                                                        var21 = (String)var27;
                                                    }
                                                }

                                                var3 = 144;
                                            }
                                        } else {
                                            String[] var126;
                                            if (var1[0] == 42 && var1[1] == 68 && var1[2] == 71 && var1[3] == 67 && var1[4] == 68) {
                                                label1277: {
                                                    try {
                                                        var126 = (new String(var1, 6, var2 - 6)).split(",");
                                                    } catch (Exception var91) {
                                                        var10000 = var91;
                                                        var10001 = false;
                                                        break label1277;
                                                    }

                                                    var3 = var15;
                                                    var20 = (String)var32;

                                                    label937: {
                                                        try {
                                                            if (var126[0].startsWith("END")) {
                                                                break label937;
                                                            }

                                                            var20 = var126[1].substring(1, var126[1].length() - 1);
                                                            this.mCurA2DPPath = var20;
                                                        } catch (Exception var90) {
                                                            var10000 = var90;
                                                            var10001 = false;
                                                            break label1277;
                                                        }

                                                        var3 = 158;
                                                    }

                                                    var4 = var5;
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 68 && var1[2] == 71 && var1[3] == 69 && var1[4] == 67) {
                                                label1278: {
                                                    try {
                                                        var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                    } catch (Exception var93) {
                                                        var10000 = var93;
                                                        var10001 = false;
                                                        break label1278;
                                                    }

                                                    var3 = var16;
                                                    var4 = var12;

                                                    label948: {
                                                        try {
                                                            if (var122[0].startsWith("END")) {
                                                                break label948;
                                                            }

                                                            var4 = Integer.valueOf(var122[1]);
                                                            this.mCurA2DPFileNum = var4;
                                                        } catch (Exception var92) {
                                                            var10000 = var92;
                                                            var10001 = false;
                                                            break label1278;
                                                        }

                                                        var3 = 159;
                                                    }

                                                    var20 = var23[0];
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 68 && var1[2] == 76 && var1[3] == 83 && var1[4] == 69) {
                                                label974: {
                                                    label1279: {
                                                        label972: {
                                                            try {
                                                                var126 = (new String(var1, 6, var2 - 6)).split(",");
                                                                if (!var126[0].startsWith("END")) {
                                                                    if (var126[1].equals("1")) {
                                                                        mMusicFileInfo.clear();
                                                                    }
                                                                    break label972;
                                                                }
                                                            } catch (Exception var96) {
                                                                var10000 = var96;
                                                                var10001 = false;
                                                                break label974;
                                                            }

                                                            var4 = 0;
                                                            break label1279;
                                                        }

                                                        var20 = var126[2];
                                                        var120 = var14;

                                                        while(true) {
                                                            try {
                                                                if (var120 >= var126.length) {
                                                                    break;
                                                                }

                                                                var24 = new StringBuilder();
                                                                var24.append(var20);
                                                                var24.append(",");
                                                                var24.append(var126[var120]);
                                                                var20 = var24.toString();
                                                            } catch (Exception var95) {
                                                                var10000 = var95;
                                                                var10001 = false;
                                                                break label974;
                                                            }

                                                            ++var120;
                                                        }

                                                        try {
                                                            mMusicFileInfo.add(var20);
                                                            var4 = Integer.valueOf(var126[1]);
                                                        } catch (Exception var94) {
                                                            var10000 = var94;
                                                            var10001 = false;
                                                            break label974;
                                                        }
                                                    }

                                                    var3 = 160;
                                                    var20 = var23[0];
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 68 && var1[2] == 83 && var1[3] == 67 && var1[4] == 68) {
                                                label1281: {
                                                    try {
                                                        var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                    } catch (Exception var99) {
                                                        var10000 = var99;
                                                        var10001 = false;
                                                        break label1281;
                                                    }

                                                    var3 = var11;

                                                    label1282: {
                                                        try {
                                                            if (!var122[0].startsWith("END")) {
                                                                break label1282;
                                                            }
                                                        } catch (Exception var98) {
                                                            var10000 = var98;
                                                            var10001 = false;
                                                            break label1281;
                                                        }

                                                        var3 = var11;

                                                        try {
                                                            if (!var122[1].equals("0")) {
                                                                break label1282;
                                                            }
                                                        } catch (Exception var97) {
                                                            var10000 = var97;
                                                            var10001 = false;
                                                            break label1281;
                                                        }

                                                        var3 = 157;
                                                    }

                                                    var4 = var5;
                                                    var20 = var23[0];
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 82 && var1[2] == 83 && var1[3] == 67 && var1[4] == 76) {
                                                label992: {
                                                    try {
                                                        var4 = Integer.valueOf((new String(var1, 6, var2 - 6)).split(",")[0]);
                                                    } catch (Exception var100) {
                                                        var10000 = var100;
                                                        var10001 = false;
                                                        break label992;
                                                    }

                                                    var3 = 128;
                                                    var20 = var23[0];
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 66 && var1[3] == 67 && var1[4] == 86) {
                                                label995: {
                                                    try {
                                                        var20 = new String(var1, 6, var2 - 6);
                                                    } catch (Exception var101) {
                                                        var10000 = var101;
                                                        var10001 = false;
                                                        break label995;
                                                    }

                                                    var3 = 88;
                                                    var4 = var5;
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 43 && var1[1] == 67 && var1[2] == 71 && var1[3] == 77 && var1[4] == 82 && var1[5] == 69 && var1[6] == 88) {
                                                label998: {
                                                    try {
                                                        var20 = new String(var1, 8, var2 - 8);
                                                    } catch (Exception var102) {
                                                        var10000 = var102;
                                                        var10001 = false;
                                                        break label998;
                                                    }

                                                    var3 = 88;
                                                    var4 = var5;
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 42 && var1[1] == 80 && var1[2] == 87 && var1[3] == 78 && var1[4] == 71) {
                                                label1283: {
                                                    try {
                                                        var122 = (new String(var1, 6, var2 - 6)).split(",");
                                                    } catch (Exception var104) {
                                                        var10000 = var104;
                                                        var10001 = false;
                                                        break label1283;
                                                    }

                                                    var3 = var17;
                                                    var4 = var13;

                                                    label1004: {
                                                        try {
                                                            if (!var122[0].equals("2")) {
                                                                break label1004;
                                                            }

                                                            var4 = Integer.valueOf(var122[1].substring(1, 2));
                                                        } catch (Exception var103) {
                                                            var10000 = var103;
                                                            var10001 = false;
                                                            break label1283;
                                                        }

                                                        var3 = 164;
                                                    }

                                                    var20 = var23[0];
                                                    var21 = var22;
                                                }
                                            } else if (var1[0] == 69 && var1[1] == 82 && var1[2] == 82 && var1[3] == 79 && var1[4] == 82) {
                                                label1022: {
                                                    label1284: {
                                                        label1285: {
                                                            try {
                                                                var20 = new String(var1, 6, var2 - 6);
                                                                if (!var20.equals("203")) {
                                                                    break label1285;
                                                                }
                                                            } catch (Exception var107) {
                                                                var10000 = var107;
                                                                var10001 = false;
                                                                break label1022;
                                                            }

                                                            var3 = 9;
                                                            var4 = 0;

                                                            try {
                                                                this.clearPhoneStatus();
                                                            } catch (Exception var106) {
                                                                var10000 = var106;
                                                                var10001 = false;
                                                                break label1022;
                                                            }

                                                            var20 = (String)var33;
                                                            break label1284;
                                                        }

                                                        var3 = 153;

                                                        try {
                                                            var4 = this.mPreCmd;
                                                        } catch (Exception var105) {
                                                            var10000 = var105;
                                                            var10001 = false;
                                                            break label1022;
                                                        }
                                                    }

                                                    var21 = var22;
                                                }
                                            } else {
                                                label1322: {
                                                    var3 = var8;
                                                    var4 = var5;
                                                    var20 = var23[0];
                                                    var21 = var22;
                                                    if (var1[0] != 79) {
                                                        break label1249;
                                                    }

                                                    var3 = var8;
                                                    var4 = var5;
                                                    var20 = var23[0];
                                                    var21 = var22;
                                                    if (var1[1] != 75) {
                                                        break label1249;
                                                    }

                                                    var3 = 131;

                                                    try {
                                                        var4 = this.mPreNeedOKCmd;
                                                        this.mPreNeedOKCmd = 0;
                                                    } catch (Exception var108) {
                                                        var10000 = var108;
                                                        var10001 = false;
                                                        break label1322;
                                                    }

                                                    var21 = var22;
                                                    var20 = var23[0];
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break label1249;
                    }

                    var116 = var10000;
                    var131 = new StringBuilder();
                    var131.append("cmd err");
                    var131.append(var116);
                    Log.d("BtCmd", var131.toString());
                    return 0;
                }

                var3 = 23;
                var4 = var6;
                var20 = (String)var28;
                var21 = (String)var30;
            }

            if (var3 != 0 && this.mCallBack != null) {
                logTest("diao yong caooback 4");
                this.mCallBack.callback(var3, var4, var20, var21);
            } else {
                if (this.mCallBack == null) {
                    return 0;
                }

                var8 = 6;
                Iterator var129 = this.mReceiveCmd.iterator();

                int var118;
                while(true) {
                    var118 = var3;
                    var117 = var4;
                    var22 = var20;
                    var123 = var21;
                    if (!var129.hasNext()) {
                        break;
                    }

                    ReceiveCmdData var127 = (ReceiveCmdData)var129.next();
                    if (var127.mCmd != null) {
                        byte[] var130 = var127.mCmd.getBytes();
                        if (var127.mType == 1) {
                            if (var1[0] == var130[0]) {
                                var118 = var3;
                                var117 = var4;
                                var22 = var20;
                                var123 = var21;
                                break;
                            }
                        } else if (var130.length >= 2 && var1[0] == var130[0] && var1[1] == var130[1]) {
                            int var119 = var127.mId;
                            var117 = var8;
                            var120 = var4;
                            var22 = var20;

                            label642: {
                                label1288: {
                                    label640: {
                                        try {
                                            if ((var127.mType & 2) == 0) {
                                                break label640;
                                            }
                                        } catch (Exception var41) {
                                            var10000 = var41;
                                            var10001 = false;
                                            break label1288;
                                        }

                                        byte var132 = var1[6];
                                        var117 = 6 + 1;
                                        var120 = var132 - 48;
                                    }

                                    var118 = var117;
                                    var123 = var20;
                                    var4 = var120;
                                    var22 = var20;

                                    label1289: {
                                        try {
                                            if ((var127.mType & 4) == 0) {
                                                break label1289;
                                            }
                                        } catch (Exception var40) {
                                            var10000 = var40;
                                            var10001 = false;
                                            break label1288;
                                        }

                                        var4 = var120;
                                        var22 = var20;

                                        label1290: {
                                            try {
                                                if ((var127.mType & 8) != 0) {
                                                    break label1290;
                                                }
                                            } catch (Exception var39) {
                                                var10000 = var39;
                                                var10001 = false;
                                                break label1288;
                                            }

                                            var4 = var120;
                                            var22 = var20;

                                            try {
                                                var127.mLen2 = var2 - var117;
                                            } catch (Exception var38) {
                                                var10000 = var38;
                                                var10001 = false;
                                                break label1288;
                                            }
                                        }

                                        var4 = var120;
                                        var22 = var20;

                                        try {
                                            var123 = new String(var1, var117, var127.mLen2);
                                        } catch (Exception var37) {
                                            var10000 = var37;
                                            var10001 = false;
                                            break label1288;
                                        }

                                        var4 = var120;
                                        var22 = var123;

                                        try {
                                            var118 = var117 + var127.mLen2;
                                        } catch (Exception var36) {
                                            var10000 = var36;
                                            var10001 = false;
                                            break label1288;
                                        }
                                    }

                                    var20 = var21;
                                    var4 = var120;
                                    var22 = var123;

                                    try {
                                        if ((var127.mType & 8) == 0) {
                                            break label642;
                                        }
                                    } catch (Exception var35) {
                                        var10000 = var35;
                                        var10001 = false;
                                        break label1288;
                                    }

                                    var4 = var120;
                                    var22 = var123;

                                    try {
                                        var20 = new String(var1, var118, var2 - var118);
                                        break label642;
                                    } catch (Exception var34) {
                                        var10000 = var34;
                                        var10001 = false;
                                    }
                                }

                                var116 = var10000;
                                var131 = new StringBuilder();
                                var131.append("dataCallback err:");
                                var131.append(var116);
                                Log.d("BtCmd", var131.toString());
                                var118 = var119;
                                var117 = var4;
                                var123 = var21;
                                break;
                            }

                            var118 = var119;
                            var117 = var120;
                            var22 = var123;
                            var123 = var20;
                            break;
                        }
                    }
                }

                if (var118 != 0) {
                    logTest("diao yong caooback 5");
                    this.mCallBack.callback(var118, var117, var22, var123);
                }
            }

            return 0;
        }
    }

    private void logTest(String msg){
        Log.e("Test_Parrot","-----"+msg);
    }

    public byte[] getCmd(int var1, int var2, int var3, String var4) {
        String var5;
        if (var1 == 12) {
            byte var9;
            if (this.mVoiceSwitchLocal) {
                var9 = 70;
            } else {
                var9 = 69;
            }

            this.mVoiceSwitchLocal ^= true;
            var2 = var9;
            var5 = var4;
        } else if (var1 == 25) {
            if (this.mMicMute) {
                var5 = "0";
            } else {
                var5 = "1";
            }

            this.mMicMute ^= true;
            var2 = var1;
        } else if (var1 == 22) {
            var5 = null;
            var2 = var1;
        } else if (var1 == 106) {
            var5 = "1";
            var2 = var1;
        } else {
            StringBuilder var13;
            if (var1 == 57) {
                var13 = new StringBuilder();
                var13.append(var4);
                var13.append(",1");
                var5 = var13.toString();
                var2 = var1;
            } else if (var1 == 30) {
                if (var4 != null) {
                    var13 = new StringBuilder();
                    var13.append("='");
                    var13.append(var4);
                    var13.append("'");
                    var5 = var13.toString();
                    var2 = var1;
                } else {
                    var5 = "?";
                    var2 = var1;
                }
            } else {
                StringBuilder var10;
                if (var1 == 38) {
                    var2 = var1;
                    var5 = var4;
                    if (mDeviceIndex != null) {
                        var10 = new StringBuilder();
                        var10.append(mDeviceIndex);
                        var10.append(",0");
                        var5 = var10.toString();
                        var2 = var1;
                    }
                } else if (var1 == 124) {
                    var2 = var1;
                    var5 = var4;
                } else if (var1 != 100 && var1 != 116) {
                    if (var1 == 147) {
                        var2 = var1;
                        var5 = var4;
                        if (mMediaIndex != null) {
                            if (var4 == null) {
                                var10 = new StringBuilder();
                                var10.append(mMediaIndex);
                                var10.append(",0");
                                var5 = var10.toString();
                                var2 = var1;
                            } else {
                                var13 = new StringBuilder();
                                var13.append(mMediaIndex);
                                var13.append(",");
                                var13.append(var4);
                                var5 = var13.toString();
                                var2 = var1;
                            }
                        }
                    } else if (var1 != 158 && var1 != 160 && var1 != 157) {
                        if (var1 == 159) {
                            var5 = mMediaIndex;
                            var2 = var1;
                        } else if (var1 == 163) {
                            var5 = mDeviceIndex;
                            var2 = var1;
                        } else {
                            var2 = var1;
                            var5 = var4;
                            if (var1 == 5) {
                                var2 = var1;
                                var5 = var4;
                                if (var4 != null) {
                                    var13 = new StringBuilder();
                                    var13.append("=P");
                                    var13.append(var4);
                                    var5 = var13.toString();
                                    var2 = var1;
                                }
                            }
                        }
                    } else if (var4 == null) {
                        var10 = new StringBuilder();
                        var10.append(mMediaIndex);
                        var10.append(",1");
                        var5 = var10.toString();
                        var2 = var1;
                    } else {
                        var13 = new StringBuilder();
                        var13.append(mMediaIndex);
                        var13.append(",");
                        var13.append(var4);
                        var5 = var13.toString();
                        var2 = var1;
                    }
                } else {
                    this.mPreNeedOKCmd = var1;
                    var5 = var4;
                    var2 = var1;
                }
            }
        }

        Object var6 = null;
        Iterator var8 = this.mSendCmd.iterator();

        byte[] var11;
        while(true) {
            var11 = (byte[])var6;
            if (!var8.hasNext()) {
                break;
            }

            SendCmdData var7 = (SendCmdData)var8.next();
            if (var7.mId == var2) {
                var3 = 4 + var7.mCmd.length();
                var1 = var3;
                if (var5 != null) {
                    var1 = var3 + var5.length();
                }

                var3 = var1;
                if (var2 == 10) {
                    var3 = var1 + 1;
                }

                byte[] var12 = new byte[var3];
                var12[0] = 65;
                var12[1] = 84;
                var12[var3 - 2] = 13;
                var12[var3 - 1] = 10;
                if (var2 == 10) {
                    var12[var3 - 3] = 59;
                }

                Util.byteArrayCopy(var12, var7.mCmd.getBytes(), 2, 0, var7.mCmd.length());
                var11 = var12;
                if (var5 != null) {
                    Util.byteArrayCopy(var12, var5.getBytes(), 2 + var7.mCmd.length(), 0, var5.length());
                    var11 = var12;
                }
                break;
            }
        }

        if (var11 != null) {
            this.mPreCmd = var2;
        }

        return var11;
    }
}
