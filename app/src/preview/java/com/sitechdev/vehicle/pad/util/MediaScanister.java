package com.sitechdev.vehicle.pad.util;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.util.Log;

/**
 * @author zhubaoqiang
 * @date 2019/9/3
 */
public class MediaScanister {

    private static MediaScanister instance;
    private WeakReference<Context> contextRef;
    private HandlerThread handlerThread;
    private Handler handler;

    private MediaScanister(){
    }

    public static MediaScanister getInstance() {
        if (null == instance){
            synchronized (MediaScanister.class){
                if (null == instance){
                    instance = new MediaScanister();
                }
            }
        }
        return instance;
    }

    public boolean isScaing(){
        return null != handlerThread;
    }

    public void scan(Context context, String rootPath, String mimeType,
                      OnScanCompleteListener listener){
        if (null != handlerThread){
            listener.onScanComplete();
            return;
        }
        handlerThread = new HandlerThread("MediaScanister");
        handlerThread.start();
        handler = new MediaScanisterHandler(handlerThread.getLooper(), context,
                rootPath, mimeType, new OnScanCompleteListener() {
            @Override
            public void onScanComplete() {
                handlerThread.interrupt();
                handlerThread = null;
                handler = null;
                listener.onScanComplete();
            }
        });
        handler.sendEmptyMessage(MediaScanisterHandler.CONNECT);
    }

    public interface OnScanCompleteListener{
        void onScanComplete();
    }

    private static class MediaScanisterHandler extends Handler implements MediaScannerConnectionClient{

        private WeakReference<Context> contextRef;
        private String rootPath;
        private String mimeType;
        private MediaScannerConnection connection;
        private volatile int sendCount;
        private volatile int receiveCount;
        private OnScanCompleteListener listener;

        private static final int CONNECT = 0x0;
        private static final int SCAN = CONNECT + 1;

        public MediaScanisterHandler(Looper looper, Context context,
                                     String rootPath, String mimeType,
                                     OnScanCompleteListener listener) {
            super(looper);
            contextRef = new WeakReference<>(context);
            this.rootPath = rootPath;
            this.mimeType = mimeType;
            this.listener = listener;
        }



        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CONNECT:
                    if (null != contextRef && null != contextRef.get()){
                        connection = new MediaScannerConnection(contextRef.get(), this);
                        connection.connect();
                    }
                    break;
                case SCAN:
                    File file = new File(rootPath);
                    if (file.exists()){
                        File[] files = file.listFiles();
                        if (files.length > 0){
                            LinkedList<File> temps = new LinkedList<>();
                            temps.addAll(Arrays.asList(files));
                            File first = null;
                            while (!temps.isEmpty()){
                                first = temps.removeFirst();
                                if (first.exists()){
                                    File[] firstChilds = first.listFiles();
                                    if (null != firstChilds){
                                        int len = firstChilds.length;
                                        for (int i = 0; i < len; i++){
                                            File child = firstChilds[i];
                                            if (child.isDirectory()){
                                                temps.add(child);
                                            }else {
                                                MediaFile.MediaFileType fileType = MediaFile.getFileType(
                                                        child.getAbsolutePath());
                                                if (null != fileType){
                                                    if (MediaFile.isAudioFileType(fileType.fileType)){
                                                        connection.scanFile(child.getAbsolutePath(), "audio/*");
                                                        sendCount += 1;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onMediaScannerConnected() {
                sendEmptyMessage(SCAN);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            receiveCount += 1;
            Log.i("Findme!", " receiveCount = " + receiveCount
                    + " sendCount = " + sendCount);
            if (sendCount == receiveCount){
                if (null != listener){
                    if (null != connection){
                        connection.disconnect();
                    }
                    connection = null;
                    listener.onScanComplete();
                }
            }
        }
    }

}
