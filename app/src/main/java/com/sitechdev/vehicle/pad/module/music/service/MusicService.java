package com.sitechdev.vehicle.pad.module.music.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {

    private MusicServer musicServer;

    private static final String TAG = MusicService.class.getSimpleName();

    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicServer = new MusicServer(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private static void log(String msg){
        Log.i(TAG, msg);
    }

    private static final class MusicServer extends AMusicInterface.Stub
            implements MediaPlayer.OnCompletionListener{

        private WeakReference<MusicService> serviceRef;
        private static String[] proj_music = new String[]{
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE};
        private static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
        private static final int FILTER_DURATION = 1 * 60 * 1000 + 30 * 1000;// 1.5分钟
        private List<MusicInfo> musicInfos;
        private MusicInfo currentInfo;
        private MusicPlayer musicPlayer;
        private int status;
        private RemoteCallbackList<IMusicCallBack> callbackList;
        private Handler mPlayerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<MusicInfo> news = queryLocalMusicReal();
                if (null != news){
                    int len = news.size();
                    boolean crrBeDel = true;
                    if (null != currentInfo){
                        for (int i = 0; i < len; i++){
                            if (currentInfo.songId == news.get(i).songId){
                                currentInfo = news.get(i);
                                crrBeDel = false;
                            }
                        }
                    }else {
                        crrBeDel = false;
                    }

                    musicInfos.clear();
                    musicInfos.addAll(news);
                    onMusicListUpdate();
                    if (crrBeDel){
                        if (musicPlayer.isPlaying()){
                            musicPlayer.stop();
                        }
                        onPlay(null);
                    }else {
                        if (status == 0){
                            onPause(currentInfo);
                        }else if (status == 1){
                            onResume(currentInfo);
                        }
                    }
                }
            }
        };
        private MediaStoreObserver mMediaStoreObserver;

        public MusicServer(MusicService service) {
            this.serviceRef = new WeakReference<>(service);
            this.musicPlayer = new MusicPlayer(
                    (AudioManager) service.getSystemService(Context.AUDIO_SERVICE),
                    this);
            mMediaStoreObserver = new MediaStoreObserver(mPlayerHandler);
            service.getContentResolver().registerContentObserver(
                    MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                    true, mMediaStoreObserver);
            service.getContentResolver().registerContentObserver(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    true, mMediaStoreObserver);
            musicInfos = new ArrayList<>();
        }

        @Override
        public void addCallBack(IMusicCallBack callBack) throws RemoteException {
            if (null == callbackList){
                callbackList = new RemoteCallbackList<>();
            }
            if (null != musicInfos){
                callBack.onListUpdate(musicInfos, currentInfo, status, 0);
            }
            callbackList.register(callBack);
        }

        @Override
        public void removeCallBack(IMusicCallBack callBack) throws RemoteException {
            callbackList.unregister(callBack);
        }

        @Override
        public void init(){
            try {
                Log.i("Findme!", "remote -> init");
                queryLocalMusic();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        public synchronized void queryLocalMusic() throws RemoteException {
            musicInfos.clear();
            musicInfos.addAll(queryLocalMusicReal());
            onMusicListUpdate();
        }

        /**
         * 真正实现查询的方法
         * @return
         */
        public List<MusicInfo> queryLocalMusicReal(){
            List<MusicInfo> infos = new ArrayList<>();
            if (null != serviceRef && null != serviceRef.get()){
                ContentResolver cr = serviceRef.get().getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                StringBuilder selection = new StringBuilder();
                selection.append("0=0 and ")
                        .append(MediaStore.Audio.Media.TITLE)
                        .append("!='' and ")
                        .append(MediaStore.Audio.Media.SIZE)
                        .append(">").append(FILTER_SIZE)
                        .append(" and ")
                        .append(MediaStore.Audio.Media.DURATION)
                        .append(">").append(FILTER_DURATION);
                Cursor cursor = cr.query(uri, proj_music, selection.toString(),
                        null, SortOrder.SongSortOrder.SONG_A_Z);
                if (null != cursor&&cursor.getCount()>0){
                    cursor.moveToFirst();
                    do {
                        MusicInfo music = new MusicInfo();
                        music.songId = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media._ID));
                        music.albumId = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        music.albumName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                        music.albumData = getAlbumArtUri(music.albumId) + "";
                        music.duration = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DURATION));
                        music.musicName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.TITLE));
                        music.artist = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                        String filePath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DATA));
                        music.data = filePath;
                        music.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
                        music.size = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.SIZE));
                        music.islocal = true;
                        music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();
                        infos.add(music);
                    }while (cursor.moveToNext());
                    cursor.close();
                }
            }
            Log.i("Findme!", "infos.size() -> " + infos.size());
            return infos;
        }

        private void onMusicListUpdate() {
            int n = callbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++){
                    IMusicCallBack callBack = callbackList.getBroadcastItem(i);
                    callBack.onListUpdate(musicInfos, currentInfo, status, 0);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            callbackList.finishBroadcast();
        }

        private void onPlay(MusicInfo info){
            int n = callbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++){
                    IMusicCallBack callBack = callbackList.getBroadcastItem(i);
                    callBack.onPlay(info);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            callbackList.finishBroadcast();
        }

        private void onPause(MusicInfo info){
            int n = callbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++){
                    IMusicCallBack callBack = callbackList.getBroadcastItem(i);
                    callBack.onPause(info);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            callbackList.finishBroadcast();
        }

        private void onResume(MusicInfo info){
            int n = callbackList.beginBroadcast();
            try {
                for (int i = 0; i < n; i++){
                    IMusicCallBack callBack = callbackList.getBroadcastItem(i);
                    callBack.onResume(info);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            callbackList.finishBroadcast();
        }

        @Override
        public IPCResult play(long id) throws RemoteException {
            IPCResult result = new IPCResult(-1, "play error");
            if (null != musicInfos){
                int size = musicInfos.size();
                if (size > 0){
                    for (int i = 0; i < size; i++){
                        MusicInfo info = musicInfos.get(i);
                        if (info.songId == id){
                            if (musicPlayer.play(info)){
                                status = 1;
                                currentInfo = info;
                                onPlay(currentInfo);
                                result.code = 0;
                                result.msg = "play sucess";
                                break;
                            }
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public IPCResult stop() throws RemoteException {
            IPCResult result = new IPCResult(-1, "stop error");
            if (null != musicPlayer){
                if (musicPlayer.stop()){
                    status = 0;
                    onPlay(null);
                    result.code = 0;
                    result.msg = "stop success";
                }
            }
            return result;
        }

        @Override
        public MusicInfo getInfo() throws RemoteException {
            if (null != currentInfo){
                return currentInfo;
            }
            return null;
        }

        @Override
        public int getStatus() throws RemoteException {
            return status;
        }

        @Override
        public IPCResult pause() throws RemoteException {
            IPCResult result = new IPCResult(-1, "pause error");
            if (null != musicPlayer){
                if (musicPlayer.isPlaying()){
                    if (musicPlayer.pause()){
                        status = 0;
                        onPause(currentInfo);
                        result.code = 0;
                        result.msg = "pause success";
                    }
                }else {
                    result.msg = "当前无正在播放的音频";
                }
            }
            return result;
        }

        @Override
        public IPCResult resume() throws RemoteException {
            IPCResult result = new IPCResult(-1, "resume error");
            if (null != musicPlayer){
                if (!musicPlayer.isPlaying()){
                    if (musicPlayer.resume()){
                        status = 1;
                        onResume(currentInfo);
                        result.code = 0;
                        result.msg = "resume success";
                    }
                }else {
                    result.msg = "正在播放";
                }
            }
            return result;
        }

        @Override
        public IPCResult toggle() throws RemoteException {
            IPCResult result = new IPCResult(-1, "toggle error");
            if (null != musicPlayer){
                status = musicPlayer.toggle();
                switch (status){
                    case 0:
                        onPause(currentInfo);
                        break;
                    case 1:
                        onResume(currentInfo);
                        break;
                }
                result.code = 0;
                result.msg = "toggle success";
            }
            return result;
        }

        @Override
        public IPCResult next() throws RemoteException {
            IPCResult result = new IPCResult(-1, "next error");
            if (null != musicInfos){
                int size = musicInfos.size();
                if (size > 0){
                    int index = musicInfos.indexOf(currentInfo);
                    if (index < size - 1){
                        //默认是列表循环
                        play(musicInfos.get(index + 1).songId);
                        result.code = 0;
                        result.msg = "toggle success";
                    }else {
                        result.msg = "已经是最后一首啦";
                    }
                }
            }
            return result;
        }

        @Override
        public IPCResult pre() throws RemoteException {
            IPCResult result = new IPCResult(-1, "pre error");
            if (null != musicInfos){
                int size = musicInfos.size();
                if (size > 0){
                    int index = musicInfos.indexOf(currentInfo);
                    if (index >= 1){
                        //默认是列表循环
                        play(musicInfos.get(index - 1).songId);
                        result.code = 0;
                        result.msg = "toggle success";
                    }else {
                        result.msg = "已经是第一首啦";
                    }
                }
            }
            return result;
        }

        private Uri getAlbumArtUri(long albumId) {
            return ContentUris.withAppendedId(Uri.parse(
                    "content://media/external/audio/albumart"), albumId);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private static class MediaStoreObserver extends ContentObserver{

            private static final long REFRESH_DELAY = 3000;
            private Handler mHandler;

            public MediaStoreObserver(Handler handler) {
                super(handler);
                mHandler = handler;
            }

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                mHandler.sendEmptyMessageDelayed(0, REFRESH_DELAY);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
            }
        }
    }

    private static final class MusicPlayer implements
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnBufferingUpdateListener{

        private MediaPlayer mediaPlayer;
        private AudioManager mAudioManager;
        private MusicServer server;

        private boolean isPrepare = false;
        private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        log("AUDIOFOCUS_LOSS");
                        try {
                            server.pause();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        log("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN:
                        log("AUDIOFOCUS_GAIN");
                        break;
                    default:
                }
            }
        };

        public MusicPlayer(AudioManager audioManager, MusicServer server) {
            this.mAudioManager = audioManager;
            this.server = server;
        }

        private void initIfNecessary(){
            if (null == mediaPlayer){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(server);
                mediaPlayer.setOnBufferingUpdateListener(this);
            }
        }

        public boolean play(MusicInfo info){
            isPrepare = false;
            initIfNecessary();
            boolean exception = false;
            int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            if (status == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(info.data);
                    mediaPlayer.prepare();
                    isPrepare = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    exception = true;
                }
                if (exception){
                    log("play exception");
                    return false;
                }
                mediaPlayer.start();
            }else {
                return false;
            }
            return true;
        }

        public boolean isPlaying(){
            if (null != mediaPlayer && isPrepare){
                return mediaPlayer.isPlaying();
            }else {
                return false;
            }
        }

        public boolean pause(){
            if (null != mediaPlayer && isPrepare && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                return true;
            }else {
                return false;
            }
        }

        public boolean resume() {
            if (null != mediaPlayer && isPrepare && !mediaPlayer.isPlaying()){
                int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (status == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start();
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }

        public int toggle() {
            if (null != mediaPlayer && isPrepare) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    return 0;
                } else {
                    int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    if (status == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mediaPlayer.start();
                        return 1;
                    }else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        }

        public boolean stop(){
            if (null != mediaPlayer && isPrepare){
                mediaPlayer.stop();
                isPrepare = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            log("percent" + percent);
        }
    }
}
