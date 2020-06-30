//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my.hw;

import android.util.Log;
import com.common.util.Util;

import java.io.UnsupportedEncodingException;

public class GOC extends BtCmd {
    public static final String TAG = "GOC";
    public int mPPBU;
    public int mPPDS;
    private byte[] mParrotPreCmd = null;

    public GOC() {
        this.init();
    }

    private void init() {
        this.addSendCmd(165, "VA");//设置蓝牙音量
        this.addSendCmd(166, "VB");//
        this.addSendCmd(50, "P1");//开启蓝牙
        this.addSendCmd(51, "P0");//关闭蓝牙
        this.addSendCmd(221,"PB");//所有通话记录
        this.addSendCmd(222,"PH");//已拨通记录
        this.addSendCmd(223,"PI");//已接通记录
        this.addSendCmd(224,"PJ");//未接通记录
        this.addSendCmd(93, "CZ");
        this.addSendCmd(94, "IU");
        this.addSendCmd(8, "CY");
        this.addSendCmd(126, "QA");
        this.addSendCmd(127,"QB");
        this.addSendCmd(28, "SD");
        this.addSendCmd(39, "ST");
        this.addSendCmd(38, "CD");
        this.addSendCmd(64, "CC");
        this.addSendCmd(88, "MY");
        this.addSendCmd(30, "MM");
        this.addSendCmd(32, "MN");
        this.addSendCmd(3, "MX");
        this.addSendCmd(34, "MF");
        this.addSendCmd(80, "MS");
        this.addSendCmd(81, "MB");
        this.addSendCmd(83, "MD");
        this.addSendCmd(82, "ME");
        this.addSendCmd(100, "MZ");
        this.addSendCmd(5, "CV");
        this.addReceiveCmd(77, "CT");
        this.addReceiveCmd(78, "IY");
        this.addReceiveCmd(186, "NC");
        this.addReceiveCmd(188, "NA");
        this.addSendCmd(110, "PD");
        this.addSendCmd(69, "CP");
        this.addSendCmd(70, "CP");
        this.addSendCmd(25, "CM");
        this.addSendCmd(10, "CZ");
        this.addSendCmd(66, "CG");
        this.addSendCmd(65, "CE");
        this.addSendCmd(67, "CF");
        this.addSendCmd(239, "UP");
        this.addReceiveCmd(91, "HA");
        this.addReceiveCmd(92, "HB");
        this.addReceiveCmd(168, "PS", 4);
        this.addReceiveCmd(999,"PD");
        this.addSendCmd(109, "BA");
        this.addSendCmd(73, "MG");
        this.addSendCmd(74, "MH");
        this.addSendCmd(75, "MP");
        this.addSendCmd(76, "MQ");
        this.addSendCmd(68, "CX");
        this.addSendCmd(22, "PK");
        this.addSendCmd(1, "BE");
        this.addSendCmd(106, "CY");
        this.addSendCmd(96, "PS");
        this.addSendCmd(102, "RE");
        this.addSendCmd(108, "MV");
        this.addSendCmd(107, "RN");
        this.addSendCmd(179, "CK");
        this.addSendCmd(180, "CI");
        this.addSendCmd(181, "CJ");
        this.addSendCmd(666,"CA");//蓝牙可见
        this.addSendCmd(888,"CB");//蓝牙不可见

        this.addReceiveCmd(86, "IF");
        this.addReceiveCmd(23, "PC");
        this.addReceiveCmd(444,"PE");//通话记录下载结束
        this.addReceiveCmd(105, "ML", 2);
        this.addReceiveCmd(105, "MU", 2);
        this.addReceiveCmd(31, "MM", 4);
        this.addReceiveCmd(33, "MN", 4);
        this.addReceiveCmd(182, "M0", 4);
        this.addReceiveCmd(183, "M1", 4);
        this.addReceiveCmd(184, "M2", 4);
        this.addReceiveCmd(185, "M6", 4);
        this.addReceiveCmd(98, "MB");
        this.addReceiveCmd(99, "MA");
        this.addReceiveCmd(113, "TS", 2);
        this.addReceiveCmd(88, "MW", 4);
        this.addReceiveCmd(777,"SA");

        this.addSendCmd(1001, "MK");
        this.addSendCmd(1002, "MA");


    }

    @Override
    public int dataCallback(byte[] var1, int var2) {
        String s = null;
        try {
            s = new String(var1,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logTest("dataCallback-----LEN:"+var2+" value:"+var1[var1.length-1]+" string:"+s);
        int var3;
        for(var3 = 0; var3 < var2; ++var3) {
            if (var1[var3] == -1) {
                var1[var3] = 59;
            }
        }

        byte[] var6 = new byte[var2];
        Util.byteArrayCopy(var6, var1, 0, 0, var2);
        var1 = var6;
        var3 = var2;
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
                logTest("diao yong callback2---1");
                this.dataCallback2(var1, var1.length);
                var7 = new StringBuilder();
                var7.append("doParrotCmd:");
                var7.append(var9[var3]);
                Log.d("GOC", var7.toString());
            }
        }

        if (var8 && var9.length > 1 && var9[var9.length - 1].length() > 0) {
            var1 = var9[var9.length - 1].getBytes();
            logTest("diao yong callback2---2");
            this.dataCallback2(var1, var1.length);
            var7 = new StringBuilder();
            var7.append("2doParrotCmd:");
            var7.append(var9[var9.length - 1]);
            Log.d("GOC", var7.toString());
        }

        return 0;
    }

    public int dataCallback2(byte[] var1, int var2) {
        String s = null;
        try {
            s = new String(var1,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logTest("dataCallback2--var49-diao yong--LEN:"+var2+" value:"+var1[var1.length-1]+" string:"+s+" var1[0]:"+var1[0]+" var1[1]:"+var1[1]);
        int var6;
        if (var1[var2 - 1] == 10) {
            var6 = var2 - 1;
        } else {
            var6 = var2;
        }

        if (this.mCallBack == null) {
            return 0;
        } else {
            byte var7 = 0;
            byte var4 = 0;
            logTest("dataCallback2--1--var4:"+var4);
            byte var9 = 0;
            byte var8 = 0;
            byte var10 = 0;
            byte var5 = 0;
            Object var16 = null;
            Object var18 = null;
            String var14 = null;
            Object var15 = null;
            Object var17 = null;
            String var13 = null;
            int var3;
            String var11;
            String var12;
            short var49;
            if (var6 == 3 && var1[0] == 83 && var1[1] == 80 && var1[2] == 83) {
                var49 = 52;
                var3 = var5;
                var11 = var14;
                var12 = var13;
            } else {
                label581: {
                    Exception var10000;
                    boolean var10001;
                    int var50;
                    if (var1[0] == 83 && var6 == 3) {
                        int var52 = var1[1] - 48;
                        if (var52 >= 0 && var52 <= 6) {
                            label549: {
                                byte var55;
                                if (var52 == 6) {
                                    var55 = 87;
                                } else {
                                    var55 = 9;
                                }

                                var4 = var55;
                                logTest("dataCallback2--2--var4:"+var4);
                                if (var55 != 0) {
                                    label550: {
                                        var49 = var55;
                                        var3 = var52;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            if (this.mCallBack == null) {
                                                break label550;
                                            }
                                        } catch (Exception var21) {
                                            var10000 = var21;
                                            var10001 = false;
                                            break label549;
                                        }

                                        var49 = var55;
                                        var3 = var52;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            this.mCallBack.callback(var4, var52, (String)null, var13);
                                        } catch (Exception var20) {
                                            var10000 = var20;
                                            var10001 = false;
                                            break label549;
                                        }
                                    }
                                }

                                var49 = 0;
                                var50 = var1[2] - 48;
                                if (var50 != 2 && var50 != 4) {
                                    var3 = var50;
                                    var11 = var14;
                                    var12 = var13;
                                    if (var50 == 3) {
                                        var49 = 98;
                                        var3 = var50;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                    break label581;
                                }

                                var49 = 99;
                                var3 = var50;
                                var11 = var14;
                                var12 = var13;
                                break label581;
                            }
                        } else {
                            var49 = var7;
                            var3 = var52;
                            var11 = (String)var16;
                            var12 = (String)var15;

                            try {
                                Log.d("GOC", "HFP status err");
                                return 0;
                            } catch (Exception var19) {
                                var10000 = var19;
                                var10001 = false;
                            }
                        }
                    } else {
                        if (var1[0] == 84 && var6 == 2) {
                            var50 = var1[1] - 48;
                            if (var50 == 1) {
                                label551: {
                                    var49 = var7;
                                    var3 = var50;
                                    var11 = (String)var16;
                                    var12 = (String)var15;

                                    try {
                                        this.mVoiceSwitchLocal = false;
                                    } catch (Exception var22) {
                                        var10000 = var22;
                                        var10001 = false;
                                        break label551;
                                    }

                                    var49 = 13;
                                    var3 = var50;
                                    var11 = var14;
                                    var12 = var13;
                                }
                            } else {
                                label552: {
                                    var49 = var9;
                                    var3 = var50;
                                    var11 = var14;
                                    var12 = var13;
                                    if (var50 != 0) {
                                        break label581;
                                    }

                                    var49 = var7;
                                    var3 = var50;
                                    var11 = (String)var16;
                                    var12 = (String)var15;

                                    try {
                                        this.mVoiceSwitchLocal = true;
                                    } catch (Exception var23) {
                                        var10000 = var23;
                                        var10001 = false;
                                        break label552;
                                    }

                                    var49 = 13;
                                    var3 = var50;
                                    var11 = var14;
                                    var12 = var13;
                                }
                            }
                        } else if (var1[0] == 73 && var1[1] == 88) {
                            label553: {
                                if (var1[2] - 48 == 9) {
                                    var4 = 1;
                                } else {
                                    var4 = 0;
                                }
                                var49 = var7;
                                var3 = var4;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var13 = new String(var1, 3, 12);
                                } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                    break label553;
                                }

                                var49 = var7;
                                var3 = var4;
                                var11 = var13;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 15, var6 - 15);
                                } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label553;
                                }

                                var49 = 29;
                                var3 = var4;
                                var11 = var13;
                                var12 = var14;
                            }
                        } else if (var1[0] == 74 && var1[1] == 73) {
                            label576: {
                                var5 = 4;
                                var2 = var1[2] - 48;
                                var50 = var2;
                                if (var1[3] - 48 == 9) {
                                    var50 = var2 | 256;
                                }

                                var49 = var5;
                                var3 = var50;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var13 = new String(var1, 4, 12);
                                } catch (Exception var27) {
                                    var10000 = var27;
                                    var10001 = false;
                                    break label576;
                                }

                                var49 = var5;
                                var3 = var50;
                                var11 = var13;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 16, var6 - 16);
                                } catch (Exception var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break label576;
                                }

                                var49 = var5;
                                var3 = var50;
                                var11 = var13;
                                var12 = var14;
                            }
                        } else if (var1[0] == 68 && var1[1] == 69) {
                            label555: {
                                var49 = var7;
                                var3 = var8;
                                var11 = (String)var16;
                                var12 = (String)var15;
                                var5 = var10;

                                label383: {
                                    try {
                                        if (!"001f00".equals(new String(var1, 2, 6))) {
                                            break label383;
                                        }
                                    } catch (Exception var28) {
                                        var10000 = var28;
                                        var10001 = false;
                                        break label555;
                                    }

                                    var4 = 90;
                                    logTest("dataCallback2--4--var4:"+var4);
                                    var5 = 1;
                                }

                                var49 = var4;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                            }
                        } else if (var1[0] == 73 && var1[1] == 67) {
                            label556: {
                                var4 = 84;
                                var49 = var4;
                                var3 = var8;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 4, var6 - 4);
                                } catch (Exception var29) {
                                    var10000 = var29;
                                    var10001 = false;
                                    break label556;
                                }

                                var49 = var4;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                            }
                        } else if (var1[0] == 73 && var1[1] == 68) {
                            label557: {
                                var4 = 85;
                                var49 = var4;
                                var3 = var8;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 4, var6 - 4);
                                } catch (Exception var30) {
                                    var10000 = var30;
                                    var10001 = false;
                                    break label557;
                                }

                                var49 = var4;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                            }
                        } else if (var1[0] == 73 && var1[1] == 71) {
                            label558: {
                                var49 = var9;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                                if (var6 <= 4) {
                                    break label581;
                                }

                                var4 = 87;
                                var49 = var4;
                                var3 = var8;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 4, var6 - 4);
                                } catch (Exception var31) {
                                    var10000 = var31;
                                    var10001 = false;
                                    break label558;
                                }

                                var49 = var4;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                            }
                        } else if (var1[0] == 74 && var1[1] == 72) {
                            label559: {
                                var5 = 9;
                                var4 = 3;
                                var49 = var5;
                                var3 = var4;
                                var11 = (String)var16;
                                var12 = (String)var15;

                                try {
                                    var14 = new String(var1, 2, 12);
                                } catch (Exception var32) {
                                    var10000 = var32;
                                    var10001 = false;
                                    break label559;
                                }

                                var49 = var5;
                                var3 = var4;
                                var11 = var14;
                                var12 = var13;
                            }
                        } else {
                            if (var1[0] == 77 && var1[1] == 70) {
                                var49 = 35;
                                var3 = var1[3] - 48 | var1[2] - 48 << 8;
                                var11 = var14;
                                var12 = var13;
                                break label581;
                            }

                            if (var1[0] == 71 && var1[1] == 69) {
                                var49 = 78;
                                var3 = var5;
                                var11 = var14;
                                var12 = var13;
                                break label581;
                            }

                            if (var1[0] == 73 && var1[1] == 65) {
                                var49 = 9;
                                var3 = 0;
                                var11 = var14;
                                var12 = var13;
                                break label581;
                            }

                            if (var1[0] == 73 && var1[1] == 86) {
                                var49 = 9;
                                var3 = 2;
                                var11 = var14;
                                var12 = var13;
                                break label581;
                            }

                            if (var1[0] == 73 && var1[1] == 66) {
                                label560: {
                                    var5 = 9;
                                    var4 = 3;
                                    logTest("dataCallback2--9--var4:"+var4);
                                    var49 = var5;
                                    var3 = var4;
                                    var11 = (String)var16;
                                    var12 = (String)var15;

                                    try {
                                        var14 = new String(var1, 2, var6 - 3);
                                    } catch (Exception var33) {
                                        var10000 = var33;
                                        var10001 = false;
                                        break label560;
                                    }

                                    var49 = var5;
                                    var3 = var4;
                                    var11 = var14;
                                    var12 = var13;
                                }
                            } else {
                                if (var1[0] == 80 && var1[1] == 65) {
                                    var3 = var1[2] - 48;
                                    if (var3 == 1) {
                                        var49 = 89;
                                        var11 = var14;
                                        var12 = var13;
                                    } else {
                                        var49 = 97;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                    break label581;
                                }

                                if (var6 >= 3 && var1[0] == 82 && var1[1] == 79 && var1[2] == 80) {
                                    label561: {
                                        var4 = 104;
                                        var49 = var4;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 3, var6 - 3);
                                        } catch (Exception var34) {
                                            var10000 = var34;
                                            var10001 = false;
                                            break label561;
                                        }

                                        var49 = var4;
                                        var3 = var5;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 80 && var1[1] == 66) {
                                    label562: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var50 = var1.length - 1;
                                        } catch (Exception var38) {
                                            var10000 = var38;
                                            var10001 = false;
                                            break label562;
                                        }

                                        while(var50 > 2 && var1[var50] != 59) {
                                            --var50;
                                        }

                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;
                                        var13 = (String)var18;
                                        var14 = (String)var17;

                                        label564: {
                                            try {
                                                if (var50 >= var1.length) {
                                                    break label564;
                                                }
                                            } catch (Exception var37) {
                                                var10000 = var37;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                var14 = new String(var1, 2, var50 - 2);
                                            } catch (Exception var36) {
                                                var10000 = var36;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = var14;

                                            try {
                                                var13 = new String(var1, var50 + 1, var6 - 1 - var50);
                                            } catch (Exception var35) {
                                                var10000 = var35;
                                                var10001 = false;
                                                break label562;
                                            }
                                        }

                                        var49 = 24;
                                        var3 = var5;
                                        var11 = var13;
                                        var12 = var14;
                                    }
                                }  else if (var1[0] == 80 && var1[1] == 76) {//通话记录下载结束
                                    label562: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var50 = var1.length - 1;
                                        } catch (Exception var38) {
                                            var10000 = var38;
                                            var10001 = false;
                                            break label562;
                                        }

                                        while(var50 > 2 && var1[var50] != 59) {
                                            --var50;
                                        }

                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;
                                        var13 = (String)var18;
                                        var14 = (String)var17;

                                        label564: {
                                            try {
                                                if (var50 >= var1.length) {
                                                    break label564;
                                                }
                                            } catch (Exception var37) {
                                                var10000 = var37;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                var14 = new String(var1, 0, var1.length);
                                            } catch (Exception var36) {
                                                var10000 = var36;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = var14;

                                            try {
                                                var13 = new String(var1, var50 + 1, var6 - 1 - var50);
                                            } catch (Exception var35) {
                                                var10000 = var35;
                                                var10001 = false;
                                                break label562;
                                            }
                                        }
                                        var49 = 444;
                                        var3 = var5;
                                        var11 = var13;
                                        var12 = var14;
                                    }
                                } else if (var1[0] == 83 && var1[1] == 65) {
                                    label562: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var50 = var1.length - 1;
                                        } catch (Exception var38) {
                                            var10000 = var38;
                                            var10001 = false;
                                            break label562;
                                        }

                                        while(var50 > 2 && var1[var50] != 59) {
                                            --var50;
                                        }

                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;
                                        var13 = (String)var18;
                                        var14 = (String)var17;

                                        label564: {
                                            try {
                                                if (var50 >= var1.length) {
                                                    break label564;
                                                }
                                            } catch (Exception var37) {
                                                var10000 = var37;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                var14 = new String(var1, 2, var50 - 2);
                                            } catch (Exception var36) {
                                                var10000 = var36;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = var14;

                                            try {
                                                var13 = new String(var1, var50, var6 - 1 - var50);
                                            } catch (Exception var35) {
                                                var10000 = var35;
                                                var10001 = false;
                                                break label562;
                                            }
                                        }

                                        var49 = 777;
                                        var11 = var13;
                                        var12 = var14;
                                    }
                                } else if (var1[0] == 80 && var1[1] == 68) {
                                    label562: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var50 = var1.length - 1;
                                        } catch (Exception var38) {
                                            var10000 = var38;
                                            var10001 = false;
                                            break label562;
                                        }

                                        while(var50 > 2 && var1[var50] != 59) {
                                            --var50;
                                        }

                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;
                                        var13 = (String)var18;
                                        var14 = (String)var17;

                                        label564: {
                                            try {
                                                if (var50 >= var1.length) {
                                                    break label564;
                                                }
                                            } catch (Exception var37) {
                                                var10000 = var37;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                var14 = new String(var1, 2, var50 - 2);
                                            } catch (Exception var36) {
                                                var10000 = var36;
                                                var10001 = false;
                                                break label562;
                                            }

                                            var49 = var7;
                                            var3 = var8;
                                            var11 = (String)var16;
                                            var12 = var14;

                                            try {
                                                var13 = new String(var1, var50 + 1, var6 - 1 - var50);
                                            } catch (Exception var35) {
                                                var10000 = var35;
                                                var10001 = false;
                                                break label562;
                                            }
                                        }

                                        var49 = 999;
                                        if (var14.length() > 0) {
                                            var3 = Integer.valueOf(var14.charAt(0)) - 48;
                                            var14 = var14.substring(1,var14.length());
                                        } else {
                                            var3 = 0;
                                        }
                                        var11 = var13;
                                        var12 = var14;
                                    }
                                } else if (var1[0] == 77 && var1[1] == 67) {
                                    label565: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            this.mVoiceSwitchLocal = true;
                                        } catch (Exception var39) {
                                            var10000 = var39;
                                            var10001 = false;
                                            break label565;
                                        }

                                        var49 = 13;
                                        var3 = var5;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 77 && var1[1] == 68) {
                                    label566: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            this.mVoiceSwitchLocal = false;
                                        } catch (Exception var40) {
                                            var10000 = var40;
                                            var10001 = false;
                                            break label566;
                                        }

                                        var49 = 13;
                                        var3 = var5;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 71 && var1[1] == 80 && var1[2] == 66) {
                                    label567: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 3, 12);
                                        } catch (Exception var41) {
                                            var10000 = var41;
                                            var10001 = false;
                                            break label567;
                                        }

                                        var3 = var1[15] - 48;
                                        var49 = 111;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 73 && var1[1] == 80) {
                                    label568: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var42) {
                                            var10000 = var42;
                                            var10001 = false;
                                            break label568;
                                        }

                                        var3 = var1[4] - 48 | var1[2] - 48 << 8;
                                        var49 = 176;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 73 && var1[1] == 81) {
                                    label569: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var43) {
                                            var10000 = var43;
                                            var10001 = false;
                                            break label569;
                                        }

                                        var3 = var1[4] - 48 | var1[2] - 48 << 8;
                                        var49 = 177;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 73 && var1[1] == 84) {
                                    label570: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var44) {
                                            var10000 = var44;
                                            var10001 = false;
                                            break label570;
                                        }

                                        var3 = var1[4] - 48 | var1[2] - 48 << 8;
                                        var49 = 178;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 85 && var1[1] == 80) {
                                    label571: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 2, var1.length - 2);
                                        } catch (Exception var45) {
                                            var10000 = var45;
                                            var10001 = false;
                                            break label571;
                                        }

                                        var3 = 1;
                                        var49 = 238;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else if (var1[0] == 68 && var1[1] == 70 && var1[2] == 85) {
                                    label572: {
                                        var49 = var7;
                                        var3 = var8;
                                        var11 = (String)var16;
                                        var12 = (String)var15;

                                        try {
                                            var14 = new String(var1, 3, var1.length - 3);
                                        } catch (Exception var46) {
                                            var10000 = var46;
                                            var10001 = false;
                                            break label572;
                                        }

                                        var3 = 2;
                                        var49 = 238;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                } else {
                                    label602: {
                                        if (var1[0] != 77 || var1[1] != 73 || var1[2] != 67) {
                                            var49 = var9;
                                            var3 = var5;
                                            var11 = var14;
                                            var12 = var13;
                                            if (var1[0] == 69) {
                                                var49 = var9;
                                                var3 = var5;
                                                var11 = var14;
                                                var12 = var13;
                                                if (var1[1] == 48) {
                                                    var49 = var9;
                                                    var3 = var5;
                                                    var11 = var14;
                                                    var12 = var13;
                                                    if (var1[2] == 48) {
                                                        if (var1[3] == 71 && var1[4] == 68) {
                                                            var49 = 77;
                                                            var3 = var5;
                                                            var11 = var14;
                                                            var12 = var13;
                                                        } else if (var1[3] == 80 && var1[4] == 65) {
                                                            var49 = 89;
                                                            var3 = var5;
                                                            var11 = var14;
                                                            var12 = var13;
                                                        } else {
                                                            var49 = var9;
                                                            var3 = var5;
                                                            var11 = var14;
                                                            var12 = var13;
                                                            if (var1[3] == 77) {
                                                                byte var51 = var1[4];
                                                                var49 = var9;
                                                                var3 = var5;
                                                                var11 = var14;
                                                                var12 = var13;
                                                                if (var51 == 82) {
                                                                    var49 = 112;
                                                                    var12 = var13;
                                                                    var11 = var14;
                                                                    var3 = var5;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            break label581;
                                        }

                                        var50 = var1[3] - 48;
                                        if (var50 == 0) {
                                            var49 = var7;
                                            var3 = var50;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                this.mMicMute = false;
                                            } catch (Exception var48) {
                                                var10000 = var48;
                                                var10001 = false;
                                                break label602;
                                            }
                                        } else {
                                            var49 = var7;
                                            var3 = var50;
                                            var11 = (String)var16;
                                            var12 = (String)var15;

                                            try {
                                                this.mMicMute = true;
                                            } catch (Exception var47) {
                                                var10000 = var47;
                                                var10001 = false;
                                                break label602;
                                            }
                                        }

                                        var49 = 169;
                                        var3 = var50;
                                        var11 = var14;
                                        var12 = var13;
                                    }
                                }
                            }
                        }
                        break label581;
                    }

                    Exception var54 = var10000;
                    StringBuilder var53 = new StringBuilder();
                    var53.append("dataCallback err");
                    var53.append(var54);
                    Log.d("GOC", var53.toString());
                }
            }

            logTest("dataCallback2-----befor callback 2 var49:"+var49);
            if (var49 != 0 && this.mCallBack != null) {
                this.mCallBack.callback(var49, var3, var11, var12);
                return 0;
            } else {
                logTest("dataCallback2-----11111");
                return super.dataCallback(var1, var6);
            }
        }
    }

    public byte[] getCmd(int var1, int var2, int var3, String var4) {
        int var5;
        if (var1 == 12) {
            byte var6;
            if (this.mVoiceSwitchLocal) {
                var6 = 70;
            } else {
                var6 = 69;
            }

            this.mVoiceSwitchLocal ^= true;
            var5 = var6;
        } else {
            var5 = var1;
            if (var1 == 106) {
                var4 = "1";
                logTest("dataCallback2--11--var4:"+var4);
                var5 = var1;
            }
        }

        return super.getCmd(var5, var2, var3, var4);
    }

    private void logTest(String msg){
        Log.e("TEST_GOC","-----"+msg);
    }
}
