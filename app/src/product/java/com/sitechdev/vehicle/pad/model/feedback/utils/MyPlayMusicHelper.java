package com.sitechdev.vehicle.pad.model.feedback.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author cold
 */
public class MyPlayMusicHelper implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener {

    String TAG = "MyPlayMusicHelper";
    public MediaPlayer mediaPlayer;
    private long videoTotalSize = 0;
    private long videoCacheSize = 0;
    private String localUrl;
    private Context context;
    private boolean isplay = true;
    private String mp3String = "a";
    boolean hasLoaded = false;
    // 用于判断是否是同一个item的录音，作用完成改变按钮图片状态

    public MyPlayMusicHelper(Context context, PlayListener listener) {
        this.context = context;
        this.mListener = listener;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error", e);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handleProgress = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
// 下载完成之后播放
                    if (null != mListener) {
                        mListener.onSourceLoaded(localUrl);
                    }
                    hasLoaded = true;
                    try {
                        playMusicBySdcard(localUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void play() {
        mediaPlayer.start();
    }

    public void playUrl(final String videoUrl,String filePath) {
        try {
// // 先判断是否是同一个MP3
            if (null != mediaPlayer && hasLoaded && !TextUtils.isEmpty(filePath)) {
                playMusicBySdcard(filePath);
                return;
            }
            if (!mp3String.equals(videoUrl)) {
                if (isplay) {
                    isplay = false;// 第一次进入不销毁mediaPlayer
                } else {
                    destroyMediaPlayer();
                }
            }
            new Thread(() -> {
                try {
                    mp3String = videoUrl;
                    prepareVideo(videoUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
//                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getMyMediaPlayer() {
        if (mediaPlayer != null) {
            return mediaPlayer;
        }
        return null;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(null);
            mediaPlayer.reset();
        }
    }

    /**
     *  
     * 通过onPrepared播放  
     */
    @Override
    public void onPrepared(MediaPlayer arg0) {
        if (arg0.isPlaying()) {
            arg0.pause();
        } else {
            arg0.start();
        }
        if (null != mListener) {
            mListener.onPlayStart();
        }
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        if (null != mListener) {
            mListener.onPlayStop();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
    }

    /**
     * 下载和缓冲文件头部数据
     */
    private void prepareVideo(String remoteUrl) throws IOException {
        URL url = new URL(remoteUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) url
                .openConnection();
        httpConnection.setConnectTimeout(3000);
        httpConnection.setRequestProperty("RANGE", "bytes=" + 0 + "-");
        InputStream is = httpConnection.getInputStream();
        videoTotalSize = httpConnection.getContentLength();
        if (videoTotalSize == -1) {
            return;
        }
        String name = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
        localUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/VideoCache/" + File.separator + name+".wav";
        File cacheFile = new File(localUrl);
        if (!cacheFile.exists()) {
            cacheFile.getParentFile().mkdirs();
            cacheFile.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(cacheFile, "rws");
        raf.setLength(videoTotalSize);
        raf.seek(0);
        byte buf[] = new byte[10 * 1024];
        int size = 0;
        videoCacheSize = 0;
        while ((size = is.read(buf)) != -1) {
            try {
                raf.write(buf, 0, size);
                videoCacheSize += size;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        is.close();
        raf.close();
        handleProgress.sendEmptyMessage(1);
    }

    private void playMusicBySdcard(String localUrl)
            throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        if (null != mediaPlayer) {
            mediaPlayer.reset();
        }
        mediaPlayer.setDataSource(localUrl);
        mediaPlayer.prepareAsync();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private PlayListener mListener;

    public interface PlayListener {
        void onPlayStart();

        void onPlayStop();

        void onSourceLoaded(String filePath);
    }
}
