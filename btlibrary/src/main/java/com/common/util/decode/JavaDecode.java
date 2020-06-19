//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.decode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

public class JavaDecode {
    public JavaDecode() {
    }

    public static String BytesToString(byte[] var0) {
        ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
        DataInputStream var3 = new DataInputStream(var2);
        String var7 = null;

        Exception var10000;
        label33: {
            String var1;
            boolean var10001;
            try {
                var1 = var3.readUTF();
            } catch (Exception var6) {
                var10000 = var6;
                var10001 = false;
                break label33;
            }

            var7 = var1;

            try {
                var2.close();
            } catch (Exception var5) {
                var10000 = var5;
                var10001 = false;
                break label33;
            }

            var7 = var1;

            try {
                var3.close();
                return var1;
            } catch (Exception var4) {
                var10000 = var4;
                var10001 = false;
            }
        }

        Exception var8 = var10000;
        var8.printStackTrace();
        return var7;
    }

    public static byte[] StringtoBytes(String var0) {
        Object var2 = null;
        byte[] var1 = (byte[])var2;

        Exception var10000;
        label57: {
            ByteArrayOutputStream var3;
            boolean var10001;
            try {
                var3 = new ByteArrayOutputStream();
            } catch (Exception var10) {
                var10000 = var10;
                var10001 = false;
                break label57;
            }

            var1 = (byte[])var2;

            DataOutputStream var4;
            try {
                var4 = new DataOutputStream(var3);
            } catch (Exception var9) {
                var10000 = var9;
                var10001 = false;
                break label57;
            }

            var1 = (byte[])var2;

            try {
                var4.writeUTF(var0);
            } catch (Exception var8) {
                var10000 = var8;
                var10001 = false;
                break label57;
            }

            var1 = (byte[])var2;

            byte[] var11;
            try {
                var11 = var3.toByteArray();
            } catch (Exception var7) {
                var10000 = var7;
                var10001 = false;
                break label57;
            }

            var1 = var11;

            try {
                var3.close();
            } catch (Exception var6) {
                var10000 = var6;
                var10001 = false;
                break label57;
            }

            var1 = var11;

            try {
                var4.close();
                return var11;
            } catch (Exception var5) {
                var10000 = var5;
                var10001 = false;
            }
        }

        Exception var12 = var10000;
        var12.printStackTrace();
        return var1;
    }

    public static final int bcd2int(byte var0) {
        return (var0 >> 4 & 15) * 10 + (var0 & 15);
    }

    public static int byteArrToInt(byte[] var0, int var1, int var2, boolean var3) {
        int var5 = var0.length;
        int var4 = 0;
        if (var1 >= var5 - 1) {
            return 0;
        } else {
            for(var5 = 0; var4 < var2; ++var4) {
                byte var7 = var0[var4 + var1];
                int var6 = var7;
                if (var7 < 0) {
                    var6 = var7 + 256;
                }

                if (var3) {
                    var6 <<= var4 * 8;
                } else {
                    var6 <<= (var2 - var4 - 1) * 8;
                }

                var5 |= var6;
            }

            return var5;
        }
    }

    public static String decodingUTF(String var0) {
        return decodingUTF(var0, "utf-8");
    }

    public static String decodingUTF(String var0, String var1) {
        try {
            var0 = new String(decodingUTFToByte(var0), var1);
            return var0;
        } catch (UnsupportedEncodingException var2) {
            var2.toString();
            return null;
        }
    }

    public static byte[] decodingUTFToByte(String var0) {
        ByteArrayOutputStream var6 = new ByteArrayOutputStream();
        int var5 = var0.length();

        int var2;
        for(int var1 = 0; var1 < var5; var6.write(var2)) {
            int var3 = var1 + 1;
            char var4 = var0.charAt(var1);
            var2 = var4;
            var1 = var3;
            if (var4 == '%') {
                var2 = var3 + 1;
                var3 = Character.digit(var0.charAt(var3), 16);
                var1 = var2 + 1;
                var2 = (var3 << 4) + Character.digit(var0.charAt(var2), 16);
            }
        }

        return var6.toByteArray();
    }

    public static String encodingUTF(String var0) {
        return encodingUTF(var0, "utf-8");
    }

    public static String encodingUTF(String var0, String var1) {
        try {
            var1 = encodingUTF(var0.getBytes(var1));
            return var1;
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return var0;
        }
    }

    public static String encodingUTF(byte[] var0) {
        Exception var10000;
        label59: {
            StringBuffer var2;
            boolean var10001;
            try {
                var2 = new StringBuffer();
            } catch (Exception var9) {
                var10000 = var9;
                var10001 = false;
                break label59;
            }

            int var1 = 0;

            while(true) {
                try {
                    if (var1 >= var0.length) {
                        break;
                    }
                } catch (Exception var7) {
                    var10000 = var7;
                    var10001 = false;
                    break label59;
                }

                if ((var0[var1] & 255) <= 127 && (var0[var1] & 255) > 32) {
                    try {
                        var2.append((char)var0[var1]);
                    } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label59;
                    }
                } else {
                    String var3;
                    try {
                        var2.append("%");
                        var3 = Integer.toHexString(var0[var1] & 255);
                        if (var3.length() < 2) {
                            var2.append("0");
                        }
                    } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label59;
                    }

                    try {
                        var2.append(var3);
                    } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                        break label59;
                    }
                }

                ++var1;
            }

            try {
                String var11 = var2.toString();
                return var11;
            } catch (Exception var4) {
                var10000 = var4;
                var10001 = false;
            }
        }

        Exception var10 = var10000;
        var10.printStackTrace();
        return new String(var0);
    }

    public static final String fromUTF(String var0) {
        return var0.equals("") ? null : var0;
    }

    public static final byte int2bcd(int var0) {
        return (byte)(var0 % 10 | (byte)(var0 / 10 << 4));
    }

    public static boolean intToByteArr(byte[] var0, int var1, int var2, int var3, boolean var4) {
        int var6 = var0.length;
        int var5 = 0;
        if (var1 >= var6 - 1) {
            return false;
        } else {
            for(; var5 < var2; ++var5) {
                if (var4) {
                    var0[var1 + var5] = (byte)(var3 >> var5 * 8 & 255);
                } else {
                    var0[var1 + var2 - 1 - var5] = (byte)(var3 >> var5 * 8 & 255);
                }
            }

            return true;
        }
    }

    public static final String toUTF(String var0) {
        return var0 == null ? "" : var0;
    }
}
