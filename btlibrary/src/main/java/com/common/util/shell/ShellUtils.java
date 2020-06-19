//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellUtils {
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_SU = "su";
    private static final String TAG = "ShellUtils";
    public static boolean isRunning = false;
    public static Map<String, SeanovoCmdCallback> seanovoCmdCallbacks = new HashMap();
    public static List<String> seanovoCmds = new ArrayList();

    private ShellUtils() {
        throw new AssertionError();
    }

    public static boolean checkRootPermission() {
        boolean var0 = false;
        if (execCommand("echo root", true, false).result == 0) {
            var0 = true;
        }

        return var0;
    }

    public static ShellUtils.CommandResult execCommand(String var0, boolean var1) {
        return execCommand(new String[]{var0}, var1, true);
    }

    public static ShellUtils.CommandResult execCommand(String var0, boolean var1, boolean var2) {
        return execCommand(new String[]{var0}, var1, var2);
    }

    public static ShellUtils.CommandResult execCommand(List<String> var0, boolean var1) {
        String[] var2;
        if (var0 == null) {
            var2 = null;
        } else {
            var2 = (String[])var0.toArray(new String[0]);
        }

        return execCommand(var2, var1, true);
    }

    public static ShellUtils.CommandResult execCommand(List<String> var0, boolean var1, boolean var2) {
        String[] var3;
        if (var0 == null) {
            var3 = null;
        } else {
            var3 = (String[])var0.toArray(new String[0]);
        }

        return execCommand(var3, var1, var2);
    }

    public static ShellUtils.CommandResult execCommand(String[] var0, boolean var1) {
        return execCommand(var0, var1, true);
    }

    public static ShellUtils.CommandResult execCommand(String[] param0, boolean param1, boolean param2) {
        // $FF: Couldn't be decompiled
        return new CommandResult(28,"string","string2");
    }

    public static void execSeanovoCmd(String var0, ShellUtils.SeanovoCmdCallback var1) {
        seanovoCmds.add(var0);
        seanovoCmdCallbacks.put(var0, var1);
        if (!isRunning && seanovoCmds.size() > 0) {
            isRunning = true;
            (new Thread(new Runnable() {
                public void run() {
                    while(ShellUtils.seanovoCmds.size() >= 1) {
                        String var1 = (String) ShellUtils.seanovoCmds.remove(0);
                        if (var1 != null) {
                            StringBuilder var2 = new StringBuilder();
                            var2.append("start akd:");
                            var2.append(var1.replace(" ", ":"));
                            String var8 = var2.toString();

                            try {
                                Runtime.getRuntime().exec(var8).waitFor();
                            } catch (InterruptedException var5) {
                                var5.printStackTrace();
                            } catch (IOException var6) {
                                var6.printStackTrace();
                            }

                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException var4) {
                                var4.printStackTrace();
                            }

                            while(ShellUtils.getAkdRunStatus()) {
                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException var3) {
                                    var3.printStackTrace();
                                }
                            }

                            ShellUtils.SeanovoCmdCallback var7 = (ShellUtils.SeanovoCmdCallback) ShellUtils.seanovoCmdCallbacks.remove(var1);
                            if (var7 != null) {
                                var7.notifyExecFinish();
                            }

                            if (ShellUtils.seanovoCmds.size() <= 0) {
                                ShellUtils.isRunning = false;
                                return;
                            }
                        }
                    }

                    ShellUtils.isRunning = false;
                }
            })).start();
        }

    }

    public static boolean getAkdRunStatus() {
        ShellUtils.CommandResult var0 = execCommand("getprop | grep -w akd", false);
        return var0 != null && var0.toString().contains("running");
    }

    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int var1) {
            this.result = var1;
        }

        public CommandResult(int var1, String var2, String var3) {
            this.result = var1;
            this.successMsg = var2;
            this.errorMsg = var3;
        }

        public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("result = ");
            var1.append(this.result);
            var1.append(", successMsg = ");
            var1.append(this.successMsg);
            var1.append(", errorMsg = ");
            var1.append(this.errorMsg);
            return var1.toString();
        }
    }

    public interface SeanovoCmdCallback {
        void notifyExecFinish();
    }
}
