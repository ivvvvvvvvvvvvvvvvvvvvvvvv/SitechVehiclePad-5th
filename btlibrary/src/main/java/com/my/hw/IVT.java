//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my.hw;

import android.util.Log;

public class IVT extends BtCmd {
    public static final String TAG = "IVT";
    public int mPPBU;
    public int mPPDS;

    public IVT() {
        this.init();
    }

    private void init() {
        this.addSendCmd(8, "CY");
        this.addSendCmd(38, "CD");
        this.addSendCmd(3, "MX");
        this.addSendCmd(28, "GD");

//        this.addSendCmd(118,"P1");
//        this.addSendCmd(128,"P0");
        this.addSendCmd(39, "GC");
        this.addSendCmd(30, "MM");
        this.addSendCmd(88, "TI");
        this.addSendCmd(50, "BE1");
        this.addSendCmd(51, "BE0");
        this.addSendCmd(109, "BA");
        this.addSendCmd(5, "MR");
        this.addSendCmd(69, "CO");
        this.addSendCmd(70, "CO");
        this.addSendCmd(73, "MG");
        this.addSendCmd(74, "MH");
        this.addSendCmd(75, "MP");
        this.addSendCmd(76, "MQ");
        this.addSendCmd(25, "CM");
        this.addSendCmd(34, "MF");
        this.addSendCmd(32, "MN");
        this.addSendCmd(10, "CW");
        this.addSendCmd(66, "CG");
        this.addSendCmd(65, "CF");
        this.addSendCmd(67, "CE");
        this.addSendCmd(68, "CX");
        this.addSendCmd(64, "CC");
        this.addSendCmd(110, "GPC");
        this.addSendCmd(22, "PA");
        this.addSendCmd(1, "BE");
        this.addSendCmd(106, "CY");
        this.addSendCmd(96, "PW");
        this.addSendCmd(80, "MA");
        this.addSendCmd(81, "MB");
        this.addSendCmd(83, "MD");
        this.addSendCmd(82, "ME");
        this.addSendCmd(100, "RF");
        this.addSendCmd(102, "RE");
        this.addSendCmd(108, "MV");
        this.addSendCmd(107, "RN");
        this.addSendCmd(179, "CK");
        this.addSendCmd(180, "CI");
        this.addSendCmd(181, "CJ");
        this.addReceiveCmd(78, "GE");
        this.addReceiveCmd(84, "IC", 4);
        this.addReceiveCmd(85, "ID", 4);
        this.addReceiveCmd(86, "IF");
        this.addReceiveCmd(87, "IR", 4);
        this.addReceiveCmd(168, "IY", 4);
        this.addReceiveCmd(167, "IW", 4);
        this.addReceiveCmd(23, "PC");
        this.addReceiveCmd(9, "MG", 2);
        this.addReceiveCmd(105, "ML", 2);
        this.addReceiveCmd(105, "MU", 2);
        this.addReceiveCmd(31, "MM", 4);
        this.addReceiveCmd(33, "MN", 4);
        this.addReceiveCmd(101, "RN", 4);
        this.addReceiveCmd(77, "GD", 1);
        this.addReceiveCmd(98, "RB");
        this.addReceiveCmd(99, "RA");
        this.addReceiveCmd(113, "TS", 2);
        this.addReceiveCmd(88, "IS", 4);
    }

    @Override
    public int dataCallback(byte[] var1, int var2) {
        if (this.mCallBack == null) {
            return 0;
        } else {
            String var9;
            String var10;
            short var39 = 0;
            int var41;
            label384: {
                int var40 = 0;
                label382: {
                    byte var7 = 0;
                    byte var5 = 0;
                    byte var8 = 0;
                    byte var6 = 0;
                    StringBuilder var14 = null;
                    String var13 = null;
                    String var12 = null;
                    String var11 = null;
                    int var3;
                    byte var4;
                    Exception var10000;
                    boolean var10001;
                    if (var1[0] == 71 && var1[1] == 70) {
                        label359: {
                            var4 = var7;
                            var3 = var8;
                            var9 = var14.toString();
                            var10 = var12;

                            try {
                                var13 = new String(var1, 15, 8);
                            } catch (Exception var23) {
                                var10000 = var23;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var8;
                            var9 = var14.toString();
                            var10 = var12;

                            label236: {
                                label235: {
                                    try {
                                        if (!"00001F00".equals(var13)) {
                                            break label235;
                                        }
                                    } catch (Exception var22) {
                                        var10000 = var22;
                                        var10001 = false;
                                        break label359;
                                    }

                                    var5 = 1;
                                    break label236;
                                }

                                var5 = 0;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var14.toString();
                            var10 = var12;

                            try {
                                var11 = new String(var1, 2, 12);
                            } catch (Exception var21) {
                                var10000 = var21;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                var12 = new String(var1, 23, var2 - 23);
                            } catch (Exception var20) {
                                var10000 = var20;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                var14 = new StringBuilder();
                            } catch (Exception var19) {
                                var10000 = var19;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                var14.append(var13);
                            } catch (Exception var18) {
                                var10000 = var18;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                var14.append(":");
                            } catch (Exception var17) {
                                var10000 = var17;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                var14.append(var12);
                            } catch (Exception var16) {
                                var10000 = var16;
                                var10001 = false;
                                break label359;
                            }

                            var4 = var7;
                            var3 = var5;
                            var9 = var11;
                            var10 = var12;

                            try {
                                Log.d("allen", var14.toString());
                            } catch (Exception var15) {
                                var10000 = var15;
                                var10001 = false;
                                break label359;
                            }

                            var39 = 29;
                            var40 = var5;
                            var9 = var11;
                            var10 = var12;
                            break label382;
                        }
                    } else {
                        int var43;
                        if (var1[0] == 77 && var1[1] == 88) {
                            label360: {
                                var6 = 4;
                                var43 = var1[2] - 48;
                                var41 = var43;
                                var4 = var6;
                                var3 = var43;
                                var9 = var14.toString();
                                var10 = var12;

                                label255: {
                                    try {
                                        if (!"00001F00".equals(new String(var1, 3, 8))) {
                                            break label255;
                                        }
                                    } catch (Exception var26) {
                                        var10000 = var26;
                                        var10001 = false;
                                        break label360;
                                    }

                                    var41 = var43 | 256;
                                }

                                var4 = var6;
                                var3 = var41;
                                var9 = var14.toString();
                                var10 = var12;

                                try {
                                    var11 = new String(var1, 11, 12);
                                } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                    break label360;
                                }

                                var4 = var6;
                                var3 = var41;
                                var9 = var11;
                                var10 = var12;

                                try {
                                    var12 = new String(var1, 23, var2 - 23);
                                } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label360;
                                }

                                var39 = var6;
                                var40 = var41;
                                var9 = var11;
                                var10 = var12;
                            }
                        } else {
                            if (var1[0] == 77 && var1[1] == 70) {
                                var39 = 35;
                                var40 = var1[2] - 48 | var1[3] - 48 << 8;
                                var9 = var13;
                                var10 = var11;
                                break label382;
                            }

                            if (var1[0] == 71 && var1[1] == 69) {
                                var39 = 78;
                                var40 = var6;
                                var9 = var13;
                                var10 = var11;
                                break label382;
                            }

                            if (var1[0] == 73 && var1[1] == 65) {
                                var39 = 9;
                                var40 = 0;
                                var9 = var13;
                                var10 = var11;
                                break label382;
                            }

                            if (var1[0] == 73 && var1[1] == 86) {
                                var39 = 9;
                                var40 = 2;
                                var9 = var13;
                                var10 = var11;
                                break label382;
                            }

                            if (var1[0] == 73 && var1[1] == 66) {
                                label361: {
                                    var6 = 9;
                                    var5 = 3;
                                    var4 = var6;
                                    var3 = var5;
                                    var9 = var14.toString();
                                    var10 = var12;

                                    try {
                                        var12 = new String(var1, 2, var2 - 3);
                                    } catch (Exception var27) {
                                        var10000 = var27;
                                        var10001 = false;
                                        break label361;
                                    }

                                    var39 = var6;
                                    var40 = var5;
                                    var9 = var12;
                                    var10 = var11;
                                }
                            } else {
                                if (var1[0] == 80 && var1[1] == 83) {
                                    var39 = 97;
                                    var40 = var1[3] - 48;
                                    var9 = var13;
                                    var10 = var11;
                                    break label382;
                                }

                                if (var2 >= 3 && var1[0] == 82 && var1[1] == 79 && var1[2] == 80) {
                                    label362: {
                                        var5 = 104;
                                        var4 = var5;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var12 = new String(var1, 3, var2 - 3);
                                        } catch (Exception var28) {
                                            var10000 = var28;
                                            var10001 = false;
                                            break label362;
                                        }

                                        var39 = var5;
                                        var40 = var6;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 80 && var1[1] == 66) {
                                    label363: {
                                        var6 = 24;
                                        var41 = var1[2] - 48;
                                        var4 = var6;
                                        var3 = var41;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var43 = Integer.parseInt(new String(var1, 5, 2));
                                        } catch (Exception var32) {
                                            var10000 = var32;
                                            var10001 = false;
                                            break label363;
                                        }

                                        var4 = var6;
                                        var3 = var41;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        int var44;
                                        try {
                                            var44 = Integer.parseInt(new String(var1, 7, 2));
                                        } catch (Exception var31) {
                                            var10000 = var31;
                                            var10001 = false;
                                            break label363;
                                        }

                                        var4 = var6;
                                        var3 = var41;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var11 = new String(var1, 9, var43);
                                        } catch (Exception var30) {
                                            var10000 = var30;
                                            var10001 = false;
                                            break label363;
                                        }

                                        var4 = var6;
                                        var3 = var41;
                                        var9 = var14.toString();
                                        var10 = var11;

                                        try {
                                            var12 = new String(var1, 9 + var43, var44);
                                        } catch (Exception var29) {
                                            var10000 = var29;
                                            var10001 = false;
                                            break label363;
                                        }

                                        var39 = var6;
                                        var40 = var41;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 77 && var1[1] == 67) {
                                    label364: {
                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            this.mVoiceSwitchLocal = true;
                                        } catch (Exception var33) {
                                            var10000 = var33;
                                            var10001 = false;
                                            break label364;
                                        }

                                        var39 = 13;
                                        var40 = var6;
                                        var9 = var13;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 77 && var1[1] == 68) {
                                    label365: {
                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            this.mVoiceSwitchLocal = false;
                                        } catch (Exception var34) {
                                            var10000 = var34;
                                            var10001 = false;
                                            break label365;
                                        }

                                        var39 = 13;
                                        var40 = var6;
                                        var9 = var13;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 71 && var1[1] == 80 && var1[2] == 66) {
                                    label366: {
                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var12 = new String(var1, 3, 12);
                                        } catch (Exception var35) {
                                            var10000 = var35;
                                            var10001 = false;
                                            break label366;
                                        }

                                        var40 = var1[15] - 48;
                                        var39 = 111;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 73 && var1[1] == 80) {
                                    label367: {
                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var12 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var36) {
                                            var10000 = var36;
                                            var10001 = false;
                                            break label367;
                                        }

                                        var40 = var1[4] - 48 | var1[2] - 48 << 8;
                                        var39 = 176;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                } else if (var1[0] == 73 && var1[1] == 81) {
                                    label368: {
                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var12 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var37) {
                                            var10000 = var37;
                                            var10001 = false;
                                            break label368;
                                        }

                                        var40 = var1[2] - 48;
                                        var39 = 177;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                } else {
                                    label380: {
                                        if (var1[0] != 73 || var1[1] != 84) {
                                            var39 = var5;
                                            var40 = var6;
                                            var9 = var13;
                                            var10 = var11;
                                            if (var1[0] == 69) {
                                                var39 = var5;
                                                var40 = var6;
                                                var9 = var13;
                                                var10 = var11;
                                                if (var1[1] == 48) {
                                                    var39 = var5;
                                                    var40 = var6;
                                                    var9 = var13;
                                                    var10 = var11;
                                                    if (var1[2] == 48) {
                                                        if (var1[3] == 71 && var1[4] == 68) {
                                                            var39 = 77;
                                                            var40 = var6;
                                                            var9 = var13;
                                                            var10 = var11;
                                                        } else if (var1[3] == 80 && var1[4] == 65) {
                                                            var39 = 89;
                                                            var40 = var6;
                                                            var9 = var13;
                                                            var10 = var11;
                                                        } else {
                                                            var39 = var5;
                                                            var40 = var6;
                                                            var9 = var13;
                                                            var10 = var11;
                                                            if (var1[3] == 77) {
                                                                byte var42 = var1[4];
                                                                var39 = var5;
                                                                var40 = var6;
                                                                var9 = var13;
                                                                var10 = var11;
                                                                if (var42 == 82) {
                                                                    var39 = 112;
                                                                    var10 = var11;
                                                                    var9 = var13;
                                                                    var40 = var6;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            break label382;
                                        }

                                        var4 = var7;
                                        var3 = var8;
                                        var9 = var14.toString();
                                        var10 = var12;

                                        try {
                                            var12 = new String(var1, 6, var1.length - 6);
                                        } catch (Exception var38) {
                                            var10000 = var38;
                                            var10001 = false;
                                            break label380;
                                        }

                                        var40 = var1[4] - 48 | var1[2] - 48 << 8;
                                        var39 = 178;
                                        var9 = var12;
                                        var10 = var11;
                                    }
                                }
                            }
                        }
                        break label382;
                    }

                    Exception var45 = var10000;
                    StringBuilder var46 = new StringBuilder();
                    var46.append("dataCallback err");
                    var46.append(var45);
                    Log.d("IVT", var46.toString());
                    var41 = var3;
                    var39 = var4;
                    break label384;
                }

                var41 = var40;
            }

            if (var39 != 0 && this.mCallBack != null) {
                logTest("diao yong caooback 3");
                this.mCallBack.callback(var39, var41, var9, var10);
                return 0;
            } else {
                logTest("dataCallback-----111");
                return super.dataCallback(var1, var2);
            }
        }
    }

    private void logTest(String msg){
        Log.e("Test_IVT","-----"+msg);
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
        } else if (var1 == 25) {
            if (this.mMicMute) {
                var4 = "0";
            } else {
                var4 = "1";
            }

            this.mMicMute ^= true;
            var5 = var1;
        } else if (var1 == 22) {
            StringBuilder var7 = new StringBuilder();
            var7.append(var2);
            var7.append(",");
            var7.append(var3);
            var7.append(",");
            var7.append(var3 + 5000);
            var4 = var7.toString();
            var5 = var1;
        } else {
            var5 = var1;
            if (var1 == 106) {
                var4 = "1";
                var5 = var1;
            }
        }

        return super.getCmd(var5, var2, var3, var4);
    }
}
