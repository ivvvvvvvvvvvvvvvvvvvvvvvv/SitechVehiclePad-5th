
package com.sitechdev.vehicle.lib.util;

import android.os.Environment;
import android.os.RemoteException;

import java.io.FileWriter;
import java.util.Calendar;

/**
 *
 * @author wujinling
 * @date 2017/3/11
 */
public class TraceImpl {
    // 线程安全单例
    private static TraceImpl mInstance = null;

    public static TraceImpl getInstance() {
        if (mInstance == null) {
            synchronized (TraceImpl.class) {
                if (mInstance == null) {
                    mInstance = new TraceImpl();
                }
            }
        }
        return mInstance;
    }

    private String tempBuffer;
    private String tempMcuBuffer;
    private String tracePath = Environment.getExternalStorageDirectory() + "/trace.txt";
    private String mcuTracePath = "/storage/udisk/mcuLog.txt";


    /**
     * 向Log文件中写入出错日志
     *
     * @param msg
     * @return void
     */
    public void setTrackLog(String msg) throws RemoteException {
        Calendar calendar = Calendar.getInstance();
        String curDate = String.format(
                "%04d-%02d-%02d %02d:%02d:%02d ",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        tempBuffer += (curDate + "TRACK " + msg + "\r\n");
        if (tempBuffer.length() > 256 * 1024) {
            saveTrack();
        }
    }

    /**
     * 向Log文件中写入出错日志
     *
     * @param tag
     * @param msg
     * @return void
     */
    public void setErrLog(String tag, String msg) throws RemoteException {
        Calendar calendar = Calendar.getInstance();
        String curDate = String.format(
                "%04d-%02d-%02d %02d:%02d:%02d ",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        tempBuffer += (curDate + tag + " " + msg + "\r\n");
        // 出错日志实时保存
        saveTrack();
    }

    /**
     * 保存诊断日志记录，最大50M，超过删除
     * 调用时机：
     * 1.Acc Off 的时候调用
     * 2.当缓冲区256K写满时调用
     * 3.发生Err时调用
     *
     * @return void
     */
    public void saveTrack() {
        ThreadManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                long llLen = FileUtil.getFileSizes(tracePath);
                if (llLen > 50 * 1024 * 1024) {
                    FileUtil.deleteFile(tracePath);
                }

                try {
                    FileWriter writer = new FileWriter(tracePath, true);
                    if (writer != null && null != tempBuffer) {
                        writer.write(tempBuffer);
                        writer.close();
                        tempBuffer = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 向Log文件中写入mcu相关数据日志
     *
     * @param tag
     * @param msg
     * @return void
     */
    public void setMcuLog(String tag, String msg) throws RemoteException {
        Calendar calendar = Calendar.getInstance();
        String curDate = String.format(
                "%04d-%02d-%02d %02d:%02d:%02d ",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

        tempMcuBuffer += (curDate + tag + " " + msg + "\r\n");
        // MCU日志保存
        saveToUdiskTrack();
    }

    /**
     * 保存诊断日志记录到U盘，最大50M，超过删除
     * 调用时机：
     * 向U盘写入mcu相关数据的Log
     * @return void
     */
    public void saveToUdiskTrack() {
        ThreadManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                long llLen = FileUtil.getFileSizes(mcuTracePath);
                if (llLen > 50 * 1024 * 1024) {
                    FileUtil.deleteFile(mcuTracePath);
                }

                try {
                    FileWriter writer = new FileWriter(mcuTracePath, true);
                    if (writer != null && null != tempMcuBuffer) {
                        writer.write(tempMcuBuffer);
                        writer.close();
                        tempMcuBuffer = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取轨迹文件路径
     *
     * @return
     */
    public String traceFilePath() {
        return tracePath;
    }

}
