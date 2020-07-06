package com.sitechdev.vehicle.pad.module.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.module.music.service.AMusicInterface;
import com.sitechdev.vehicle.pad.module.music.service.IMusicCallBack;
import com.sitechdev.vehicle.pad.module.music.service.IPCResult;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.module.music.service.MusicService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhubaoqiang
 * @date 2019/8/24
 */
public class MusicManager {

    private Context context;

    private static volatile MusicManager INSTANCE;

    private AMusicInterface musicInterface;

    private MusciCallback callBack;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicInterface = AMusicInterface.Stub.asInterface(service);
            try {
                musicInterface.addCallBack(callBack);
                musicInterface.init();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicInterface = null;
        }
    };

    private MusicManager() {
    }

    public static MusicManager getInstance() {
        if (null == INSTANCE) {
            synchronized (MusicManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new MusicManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
        callBack = new MusciCallback();
        if (null != musicInterface) {
            try {
                musicInterface.addCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            context.getApplicationContext().bindService(new Intent().setClass(
                    context, MusicService.class), connection, Context.BIND_AUTO_CREATE);
        }
    }

    public void destory() {
        if (null != musicInterface && musicInterface.asBinder().isBinderAlive()) {
            try {
                musicInterface.removeCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MusicInfo> getPlayList() {
        return callBack.musicInfos;
    }

    public MusicInfo getPlayInfo() {
        return callBack.current;
    }

    public int getStatus() {
        return callBack.status;
    }

    public void addMusicChangeListener(OnMusicChangeListener listener) {
//        SitechMusicNewManager.getInstance().resetCurrentMusicChannel();
        if (null != callBack) {
            callBack.addMusicChangeListener(listener);
        }
    }

    public void removeMusicChangeListener(OnMusicChangeListener listener) {
        if (null != callBack) {
            callBack.removeMusicChangeListener(listener);
        }
    }

    public void addMusicPositionChangeListener(OnMusicPostionChangeListener listener) {
        if (null != callBack) {
            callBack.addMusicPositionChangeListener(listener);
        }
    }

    public void removeMusicPositionChangeListener(OnMusicPostionChangeListener listener) {
        if (null != callBack) {
            callBack.removeMusicPositionChangeListener(listener);
        }
    }

    public void addMusicListUpdateListener(OnMusicListUpdateListener listener) {
        if (null != callBack) {
            callBack.addMusicListUpdateListener(listener);
        }
    }

    public void removeMusicListUpdateListener(OnMusicListUpdateListener listener) {
        if (null != callBack) {
            callBack.removeMusicListUpdateListener(listener);
        }
    }

    public boolean isLocalPlaying(){
        if(null != musicInterface){
            try {
                return musicInterface.getStatus() == 1;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void play(long id, CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "play error");
                try {
                    result = musicInterface.play(id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) throws Exception {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void stop(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "play error");
                try {
                    result = musicInterface.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) throws Exception {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void next(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "next error");
                try {
                    result = musicInterface.next(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void seekTo(CallBack<String> callBack, int position) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "next error");
                try {
                    result = musicInterface.seekTo(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void changeMode(CallBack<String> callBack, int mode) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "next error");
                try {
                    result = musicInterface.changeMode(mode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void pre(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "pre error");
                try {
                    result = musicInterface.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void pause(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "pause error");
                try {
                    result = musicInterface.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) throws Exception {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    public void resume(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "pause error");
                try {
                    result = musicInterface.resume();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) throws Exception {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }

    /**
     * 会返回当前播放状态
     *
     * @param callBack
     */
    public void toggle(CallBack<String> callBack) {
        Observable.create(new ObservableOnSubscribe<IPCResult>() {
            @Override
            public void subscribe(ObservableEmitter<IPCResult> emitter) throws Exception {
                if (null == musicInterface) {
                    emitter.onError(new Throwable("music server can not be use"));
                }
                IPCResult result = new IPCResult(-1, "toggle error");
                try {
                    result = musicInterface.toggle();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IPCResult>() {
                    @Override
                    public void accept(IPCResult result) throws Exception {
                        callBack.onCallBack(result.code, result.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onCallBack(-1, throwable.getMessage());
                    }
                });
    }


    public interface CallBack<T> {
        void onCallBack(int code, T t);
    }

    public interface OnMusicChangeListener {
        int PAUSE = 0;
        int RESUME = 1;

        void onMusciChange(MusicInfo current, int status);
    }

    public interface OnMusicPostionChangeListener {
        void onPositionChange(MusicInfo currentInfo, int position);
    }

    public interface OnMusicListUpdateListener {
        void onMusicListUpdate(List<MusicInfo> infos, MusicInfo info, int status, int postion);
    }

    private static class MusciCallback extends IMusicCallBack.Stub {

        private List<MusicInfo> musicInfos;

        private MusicInfo current;

        private int status;

        private List<WeakReference<OnMusicChangeListener>> musicChangeListeners;

        private List<WeakReference<OnMusicPostionChangeListener>> postionChangeListeners;

        private List<WeakReference<OnMusicListUpdateListener>> musicListUpdateListeners;

        @Override
        public void onListUpdate(List<MusicInfo> list, MusicInfo info, int status, int position) throws RemoteException {
            musicInfos = list;
            current = info;
            this.status = status;
            if (null != musicListUpdateListeners) {
                int len = musicListUpdateListeners.size();
                if (len > 0) {
                    Observable.create(new ObservableOnSubscribe<OnMusicListUpdateListener>() {
                        @Override
                        public void subscribe(ObservableEmitter<OnMusicListUpdateListener> emitter) throws Exception {
                            boolean[] removes = null;
                            for (int i = 0; i < len; i++) {
                                WeakReference<OnMusicListUpdateListener> ref = musicListUpdateListeners.get(i);
                                if (null == ref || null == ref.get()) {
                                    if (null == removes) {
                                        removes = new boolean[len];
                                        removes[i] = true;
                                        continue;
                                    }
                                }
                                emitter.onNext(ref.get());
                            }
                            if (null != removes) {
                                for (int i = 0; i < len; i++) {
                                    if (removes[i]) {
                                        musicListUpdateListeners.remove(i);
                                    }
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<OnMusicListUpdateListener>() {
                                @Override
                                public void accept(OnMusicListUpdateListener listener) throws Exception {
                                    listener.onMusicListUpdate(list, info, status, position);
                                }
                            });
                }
            }
        }

        @Override
        public void onPlay(MusicInfo info) throws RemoteException {
            current = info;
            status = 1;
            if (null != musicChangeListeners) {
                int len = musicChangeListeners.size();
                if (len > 0) {
                    Observable.create(new ObservableOnSubscribe<OnMusicChangeListener>() {
                        @Override
                        public void subscribe(ObservableEmitter<OnMusicChangeListener> emitter) throws Exception {
                            boolean[] removes = null;
                            for (int i = 0; i < len; i++) {
                                WeakReference<OnMusicChangeListener> ref = musicChangeListeners.get(i);
                                if (null == ref || null == ref.get()) {
                                    if (null == removes) {
                                        removes = new boolean[len];
                                        removes[i] = true;
                                        continue;
                                    }
                                }
                                emitter.onNext(ref.get());
                            }
                            if (null != removes) {
                                for (int i = 0; i < len; i++) {
                                    if (removes[i]) {
                                        musicChangeListeners.remove(i);
                                    }
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<OnMusicChangeListener>() {
                                @Override
                                public void accept(OnMusicChangeListener listener) throws Exception {
                                    SitechDevLog.i("onMusicChange", "333333333 ===info,====" + info);
                                    listener.onMusciChange(info, 1);
                                }
                            });
                }
            }
        }

        @Override
        public void onPause(MusicInfo info) throws RemoteException {
            current = info;
            status = 0;
            if (null != musicChangeListeners) {
                int len = musicChangeListeners.size();
                if (len > 0) {
                    Observable.create(new ObservableOnSubscribe<OnMusicChangeListener>() {
                        @Override
                        public void subscribe(ObservableEmitter<OnMusicChangeListener> emitter) throws Exception {
                            int len = musicChangeListeners.size();
                            boolean[] removes = null;
                            for (int i = 0; i < len; i++) {
                                WeakReference<OnMusicChangeListener> ref = musicChangeListeners.get(i);
                                if (null == ref || null == ref.get()) {
                                    if (null == removes) {
                                        removes = new boolean[len];
                                        removes[i] = true;
                                        continue;
                                    }
                                }
                                emitter.onNext(ref.get());
                            }
                            if (null != removes) {
                                for (int i = 0; i < len; i++) {
                                    if (removes[i]) {
                                        musicChangeListeners.remove(i);
                                    }
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<OnMusicChangeListener>() {
                                @Override
                                public void accept(OnMusicChangeListener listener) throws Exception {
                                    SitechDevLog.i("onMusicChange", "2222222222222222 ===info,====" + info);
                                    listener.onMusciChange(info, 0);
                                }
                            });
                }
            }
        }

        @Override
        public void onResume(MusicInfo info) throws RemoteException {
            current = info;
            status = 1;
            if (null != musicChangeListeners) {
                int len = musicChangeListeners.size();
                if (len > 0) {
                    Observable.create(new ObservableOnSubscribe<OnMusicChangeListener>() {
                        @Override
                        public void subscribe(ObservableEmitter<OnMusicChangeListener> emitter) throws Exception {
                            int len = musicChangeListeners.size();
                            boolean[] removes = null;
                            for (int i = 0; i < len; i++) {
                                WeakReference<OnMusicChangeListener> ref = musicChangeListeners.get(i);
                                if (null == ref || null == ref.get()) {
                                    if (null == removes) {
                                        removes = new boolean[len];
                                        removes[i] = true;
                                        continue;
                                    }
                                }
                                emitter.onNext(ref.get());
                            }
                            if (null != removes) {
                                for (int i = 0; i < len; i++) {
                                    if (removes[i]) {
                                        musicChangeListeners.remove(i);
                                    }
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<OnMusicChangeListener>() {
                                @Override
                                public void accept(OnMusicChangeListener listener) throws Exception {
                                    SitechDevLog.i("onMusicChange", "444444444 ===info,====" + info);
                                    listener.onMusciChange(info, 1);
                                }
                            });
                }
            }
        }

        @Override
        public void onSeekTo(MusicInfo info, int position) throws RemoteException {
            if (null != postionChangeListeners) {
                int len = postionChangeListeners.size();
                if (len > 0) {
                    Observable.create(new ObservableOnSubscribe<OnMusicPostionChangeListener>() {
                        @Override
                        public void subscribe(ObservableEmitter<OnMusicPostionChangeListener> emitter) throws Exception {
                            boolean[] removes = null;
                            for (int i = 0; i < len; i++) {
                                WeakReference<OnMusicPostionChangeListener> ref = postionChangeListeners.get(i);
                                if (null == ref || null == ref.get()) {
                                    if (null == removes) {
                                        removes = new boolean[len];
                                        removes[i] = true;
                                        continue;
                                    }
                                }
                                emitter.onNext(ref.get());
                            }
                            if (null != removes) {
                                for (int i = 0; i < len; i++) {
                                    if (removes[i]) {
                                        musicChangeListeners.remove(i);
                                    }
                                }
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<OnMusicPostionChangeListener>() {
                                @Override
                                public void accept(OnMusicPostionChangeListener listener) throws Exception {
                                    listener.onPositionChange(current, position);
                                }
                            });
                }
            }
        }

        public void addMusicChangeListener(OnMusicChangeListener listener) {
            if (null == musicChangeListeners) {
                musicChangeListeners = new ArrayList<>();
            }
            addListener(listener, musicChangeListeners);
            SitechDevLog.i("onMusicChange", "1111111111 ===current,====" + current + "====" + status);
            listener.onMusciChange(current, status);
        }

        public void removeMusicChangeListener(OnMusicChangeListener listener) {
            if (null == musicChangeListeners) {
                return;
            }
            removeListener(listener, musicChangeListeners);
        }

        public void addMusicPositionChangeListener(OnMusicPostionChangeListener listener) {
            if (null == postionChangeListeners) {
                postionChangeListeners = new ArrayList<>();
            }
            addListener(listener, postionChangeListeners);
        }

        public void removeMusicPositionChangeListener(OnMusicPostionChangeListener listener) {
            if (null == postionChangeListeners) {
                return;
            }
            removeListener(listener, postionChangeListeners);
        }

        public void addMusicListUpdateListener(OnMusicListUpdateListener listener) {
            if (null == musicListUpdateListeners) {
                musicListUpdateListeners = new ArrayList<>();
            }
            addListener(listener, musicListUpdateListeners);
            listener.onMusicListUpdate(musicInfos, current, status, 0);
        }

        public void removeMusicListUpdateListener(OnMusicListUpdateListener listener) {
            if (null == musicListUpdateListeners) {
                return;
            }
            removeListener(listener, musicListUpdateListeners);
        }

        private <T> void addListener(T t, List<WeakReference<T>> list) {
            int len = list.size();
            boolean[] removes = null;
            boolean canAdd = true;
            for (int i = 0; i < len; i++) {
                WeakReference<T> ref =
                        list.get(i);
                if (null == ref || null == ref.get()) {
                    if (null == removes) {
                        removes = new boolean[len];
                    }
                    removes[i] = true;
                    continue;
                }
                T curr = ref.get();
                if (curr == t) {
                    canAdd = false;
                    break;
                }
            }
            if (null != removes) {
                for (int i = 0; i < len; i++) {
                    if (removes[i]) {
                        list.remove(i);
                    }
                }
            }
            if (canAdd) {
                list.add(new WeakReference<>(t));
            }
        }

        private <T> void removeListener(T t, List<WeakReference<T>> list) {
            int len = list.size();
            if (len == 0) {
                return;
            }
            boolean[] removes = null;
            for (int i = 0; i < len; i++) {
                WeakReference<T> ref =
                        list.get(i);
                if (null == ref || null == ref.get()) {
                    if (null == removes) {
                        removes = new boolean[len];
                    }
                    removes[i] = true;
                    continue;
                }
                T curr = ref.get();
                if (curr == t) {
                    if (null == removes) {
                        removes = new boolean[len];
                    }
                    removes[i] = true;
                    break;
                }
            }
            if (null != removes) {
                for (int i = 0; i < len; i++) {
                    if (removes[i]) {
                        list.remove(i);
                    }
                }
            }
        }
    }
}
