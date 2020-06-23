package com.sitechdev.vehicle.pad.manager;

import android.content.Context;
import android.content.Intent;

import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.module.music.BtMusicManager;
import com.sitechdev.vehicle.pad.module.music.MusicMainActivity;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.vui.VUI;
import com.sitechdev.vehicle.pad.vui.VUIWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhubaoqiang
 * @date 2019/8/26
 */
public class VoiceSourceManager {

    private Context context;
    private static VoiceSourceManager INSTANCE;
    private int musicSource = -1;
    private static final int KAOLA = 0;
    private static final int LOCAL_MUSIC = KAOLA + 1;
    public static final int BT_MUSIC = LOCAL_MUSIC + 1;
    public static final int VOICE = 0;
    public static final int SCREEN = VOICE + 1;
    /**
     * 语音交互暂停音乐
     */
    public static final int CONTENT = SCREEN + 1;
    private List<WeakReference<MusicChangeListener>> listeners;
    private boolean isFromWakeUp = false;
    public static final String SUPPORT_TYPE_ALL = "all";
    public static final String SUPPORT_TYPE_KAOLA = "kaola";
    public static final String SUPPORT_TYPE_LOCAL = "local";
    public static final String SUPPORT_TYPE_BT = "bluetooth";

    private KaolaPlayManager.PlayCallback mPlayCallback = new KaolaPlayManager.PlayCallback() {
        @Override
        public void onPrepare(PlayItem playItem) {
//            if (playItem != null) {
//                musicSource = KAOLA;
//                int len = listeners.size();
//                boolean[] removes = null;
//                for (int i = 0; i < len; i++){
//                    WeakReference<MusicChangeListener> ref = listeners.get(i);
//                    if (null == ref || null == ref.get()){
//                        if (null == removes){
//                            removes = new boolean[len];
//                            removes[i] = true;
//                            continue;
//                        }
//                    }
//                    ref.get().onMusicChange(playItem.getTitle());
//                }
//                if (null != removes){
//                    for (int i = 0; i < len; i++){
//                        if (removes[i]){
//                            listeners.remove(i);
//                        }
//                    }
//                }
//            }
        }

        @Override
        public void onPlay() {
            musicSource = KAOLA;
            int len = listeners.size();
            boolean[] removes = null;
            for (int i = 0; i < len; i++) {
                WeakReference<MusicChangeListener> ref = listeners.get(i);
                if (null == ref || null == ref.get()) {
                    if (null == removes) {
                        removes = new boolean[len];
                    }
                    removes[i] = true;
                    continue;
                }
                PlayItem curPlayItem;
                if (KaolaPlayManager.SingletonHolder.INSTANCE.isCurPlayingBroadcast()) {
                    curPlayItem = BroadcastRadioListManager.getInstance().getCurPlayItem();
                } else {
                    curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
                }
                if (curPlayItem != null) {
                    String title = curPlayItem.getTitle();
                    MusicChangeListener listener = ref.get();
                    String value = listener.getClass().getAnnotation(
                            VoiceSourceType.class).value();
                    SitechDevLog.i("onMusicChange", "MainActivty vourceManger=  onPlay  =value=" + value + "==title=" + title);
                    switch (value) {
                        case SUPPORT_TYPE_ALL:
                            listener.onMusicChange(title);
                            listener.resume();
                            break;
                        case SUPPORT_TYPE_KAOLA:
                            listener.onMusicChange(title);
                            listener.resume();
                            break;
                        case SUPPORT_TYPE_LOCAL:
                            break;
                        case SUPPORT_TYPE_BT:
                            break;
                        default:
                            removes[i] = true;
                            break;
                    }
                }
            }
            if (null != removes) {
                for (int i = 0; i < len; i++) {
                    if (removes[i]) {
                        listeners.remove(i);
                    }
                }
            }
        }

        @Override
        public void onPause() {
            if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() != null) {
                return;
            }
            int len = listeners.size();
            boolean[] removes = null;
            for (int i = 0; i < len; i++) {
                WeakReference<MusicChangeListener> ref = listeners.get(i);
                if (null == ref || null == ref.get()) {
                    if (null == removes) {
                        removes = new boolean[len];
                    }
                    removes[i] = true;
                    continue;
                }
                MusicChangeListener listener = ref.get();
                PlayItem curPlayItem;
                if (KaolaPlayManager.SingletonHolder.INSTANCE.isCurPlayingBroadcast()) {
                    curPlayItem = BroadcastRadioListManager.getInstance().getCurPlayItem();
                } else {
                    curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
                }
                SitechDevLog.i("music", "MainActivty vourceManger=  onPause  = curPlayItem == null=" + (curPlayItem == null));
                if (curPlayItem == null) {
                    continue;
                }
                SitechDevLog.i("music", "MainActivty vourceManger=  onPause  =listener.getClass()=" + listener.getClass());
                String value = listener.getClass().getAnnotation(
                        VoiceSourceType.class).value();
                if (value.equals(SUPPORT_TYPE_ALL) || value.equals(SUPPORT_TYPE_KAOLA)) {
                    listener.pause();
                }
                SitechDevLog.i("onMusicChange", "MainActivty vourceManger=  onPause  =value=" + value);
                if (curPlayItem != null) {
                    String title = curPlayItem.getTitle();
                    SitechDevLog.i("onMusicChange", "MainActivty vourceManger=  onPause  ===title=" + title);
                    switch (value) {
                        case SUPPORT_TYPE_ALL:
                            listener.onMusicChange(title);
                            break;
                        case SUPPORT_TYPE_KAOLA:
                            listener.onMusicChange(title);
                            break;
                        case SUPPORT_TYPE_LOCAL:
                            break;
                        case SUPPORT_TYPE_BT:
                            break;
                    }
                }
            }
            if (null != removes) {
                for (int i = 0; i < len; i++) {
                    if (removes[i]) {
                        listeners.remove(i);
                    }
                }
            }
        }
    };
    private MusicManager.OnMusicChangeListener musicChangeListener =
            new MusicManager.OnMusicChangeListener() {
                @Override
                public void onMusciChange(MusicInfo current, int status) {
                    if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() != null) {
                        return;
                    }
                    if (null == current) {
                        musicSource = -1;
                    } else {
                        if (1 == status) {
                            musicSource = LOCAL_MUSIC;
                        }
                    }
                    if (null != listeners) {
                        int len = listeners.size();
                        boolean[] removes = null;
                        for (int i = 0; i < len; i++) {
                            WeakReference<MusicChangeListener> ref = listeners.get(i);
                            if (null == ref || null == ref.get()) {
                                if (null == removes) {
                                    removes = new boolean[len];
                                }
                                removes[i] = true;
                                continue;
                            }
                            MusicChangeListener listener = ref.get();
                            String value = listener.getClass().getAnnotation(
                                    VoiceSourceType.class).value();
                            SitechDevLog.i("onMusicChange", "MainActivty vourceManger musicChangeListener=  onMusciChange  ===value=" + value + "==current=" + current);
                            switch (value) {
                                case SUPPORT_TYPE_ALL:
                                case SUPPORT_TYPE_LOCAL:
                                    if (null == current) {
                                        listener.onMusicChange("");
                                        listener.pause();
                                    } else {
                                        StringBuilder content = new StringBuilder();
                                        content.append(current.musicName).append(" - ").append(current.artist);
                                        listener.onMusicChange(content.toString());
                                        content.delete(0, content.length());
                                        switch (status) {
                                            case 0:
                                                listener.pause();
                                                break;
                                            case 1:
                                                listener.resume();
                                                break;
                                        }
                                    }
                                    break;
                                case SUPPORT_TYPE_KAOLA:
                                    break;
                            }
                        }
                        if (null != removes) {
                            for (int i = 0; i < len; i++) {
                                if (removes[i]) {
                                    listeners.remove(i);
                                }
                            }
                        }
                    }
                }
            };


    private VoiceSourceManager() {
        context = AppApplication.getContext();
    }

    public static VoiceSourceManager getInstance() {
        if (null == INSTANCE) {
            synchronized (VoiceSourceManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new VoiceSourceManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init() {
        KaolaPlayManager.SingletonHolder.INSTANCE.setPlayCallback(mPlayCallback);
        MusicManager.getInstance().addMusicChangeListener(musicChangeListener);
    }

    public void destory() {
        MusicManager.getInstance().removeMusicChangeListener(musicChangeListener);
    }

    public void pre(int type) {
        switch (musicSource) {
            case KAOLA:
                if (KaolaPlayManager.SingletonHolder.INSTANCE.hasPre(context)) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST));
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                } else {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("已经是第一首啦");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("已经是第一首啦");
                    }
                }
                break;
            case LOCAL_MUSIC:
                MusicManager.getInstance().pre(new MusicManager.CallBack<String>() {
                    @Override
                    public void onCallBack(int code, String s) {
                        if (0 != code) {
                            if (type == VOICE) {
                                VUI.getInstance().shutAndTTS(s);
                            } else if (type == SCREEN) {
                                CommonToast.showToast(s);
                            }
                        } else {
                            if (type == VOICE) {
                                VUIWindow.getInstance().hide();
                            }
                        }
                    }
                });
                break;
            case BT_MUSIC:
                BtMusicManager.getInstance().btCtrlPre();
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    SitechMusicNewManager.getInstance().onPlayPre();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
        }
    }

    public void next(int type) {
        switch (musicSource) {
            case KAOLA:
                if (KaolaPlayManager.SingletonHolder.INSTANCE.hasNext(context)) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.playNext();
                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST));
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                } else {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("已经是最后一首啦");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("已经是最后一首啦");
                    }
                }
                break;
            case LOCAL_MUSIC:
                MusicManager.getInstance().next(new MusicManager.CallBack<String>() {
                    @Override
                    public void onCallBack(int code, String s) {
                        if (0 != code) {
                            if (type == VOICE) {
                                VUI.getInstance().shutAndTTS(s);
                            } else if (type == SCREEN) {
                                CommonToast.showToast(s);
                            }
                        } else {
                            if (type == VOICE) {
                                VUIWindow.getInstance().hide();
                            }
                        }
                    }
                });
                break;
            case BT_MUSIC:
                BtMusicManager.getInstance().btCtrlNext();
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    SitechMusicNewManager.getInstance().onPlayNext();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
        }

    }

    public void changeAnother(int type) {
        switch (musicSource) {
            case KAOLA:
                boolean result = KaolaPlayManager.SingletonHolder.INSTANCE.playAnother();
                VUIWindow.getInstance().hide();
                if (!result) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                }
                break;
            case LOCAL_MUSIC:
                List<MusicInfo> list = MusicManager.getInstance().getPlayList();
                if (null != list && list.size() > 0) {
                    Random random = new Random();
                    int index = random.nextInt(list.size());
                    MusicManager.getInstance().play(list.get(index).songId, new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String s) {
                            if (0 != code) {
                                AppUtil.goOtherActivity(context, "QQ音乐", "com.tencent.qqmusiccar",
                                        "com.tencent.qqmusiccar.app.activity.AppStarterActivity");
                            }
                            if (type == VOICE) {
                                VUIWindow.getInstance().hide();
                            }
                        }
                    });
                } else {
                    AppUtil.goOtherActivity(context, "QQ音乐", "com.tencent.qqmusiccar",
                            "com.tencent.qqmusiccar.app.activity.AppStarterActivity");
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
            case BT_MUSIC:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("暂不支持");
                } else if (type == SCREEN) {
                    CommonToast.showToast("暂不支持");
                }
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    SitechMusicNewManager.getInstance().onPlayRandomMusic();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
        }
    }

    public void pause(int type) {
        if (isFromWakeUp) {
            isFromWakeUp = false;
            VUI.getInstance().shut();
            return;
        }
        switch (musicSource) {
            case KAOLA:
                if (KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(context)) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.switchPlayPause(context);
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    } else if (type == CONTENT) {
                        //do nothing
                        isFromWakeUp = true;
                    }
                } else {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无正在播放的音频");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无正在播放的音频");
                    } else if (type == CONTENT) {
                        //do nothing
                    }
                }
                break;
            case LOCAL_MUSIC:
                MusicManager.getInstance().pause(new MusicManager.CallBack<String>() {
                    @Override
                    public void onCallBack(int code, String s) {
                        if (0 != code) {
                            if (type == VOICE) {
                                VUI.getInstance().shutAndTTS(s);
                            } else if (type == SCREEN) {
                                CommonToast.showToast(s);
                            } else if (type == CONTENT) {
                                //do nothing
                            }
                        } else {
                            if (type == VOICE) {
                                VUIWindow.getInstance().hide();
                            } else if (type == CONTENT) {
                                //do nothing
                                isFromWakeUp = true;
                            }
                        }
                    }
                });
                break;
            case BT_MUSIC:
                BtMusicManager.getInstance().btCtrlPause();
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    SitechMusicNewManager.getInstance().onPlayPause();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    } else if (type == CONTENT) {
                        //do nothing
                        isFromWakeUp = true;
                    }
                }
                break;
        }
    }

    public void resume(int type) {
        if (type == CONTENT) {
            if (!isFromWakeUp) {
                return;
            }
            isFromWakeUp = false;
        }
        switch (musicSource) {
            case KAOLA:
                if (!KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(context)) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.switchPlayPause(context);
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                } else {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("正在播放");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("正在播放");
                    }
                }
                break;
            case LOCAL_MUSIC:
                MusicManager.getInstance().resume(new MusicManager.CallBack<String>() {
                    @Override
                    public void onCallBack(int code, String s) {
                        if (0 != code) {
                            if (type == VOICE) {
                                VUI.getInstance().shutAndTTS(s);
                            } else if (type == SCREEN) {
                                CommonToast.showToast(s);
                            }
                        } else {
                            if (type == VOICE) {
                                VUIWindow.getInstance().hide();
                            }
                        }
                    }
                });
                break;
            case BT_MUSIC:
                BtMusicManager.getInstance().btCtrlPlay();
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    SitechMusicNewManager.getInstance().onPlayResume();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
        }
    }

    public void toggle(int type) {
        switch (musicSource) {
            case KAOLA:
                KaolaPlayManager.SingletonHolder.INSTANCE.switchPlayPause(context);
                break;
            case LOCAL_MUSIC:
                MusicManager.getInstance().toggle(new MusicManager.CallBack<String>() {
                    @Override
                    public void onCallBack(int code, String s) {
                        if (0 != code) {
                            CommonToast.showToast(s);
                        }
                    }
                });
                break;
            case BT_MUSIC:
                BtMusicManager.getInstance().btCtrlPlayPause();
                break;
            default:
                if (SitechMusicNewManager.getInstance().getCurrentMusicChannel() == null) {
                    if (type == VOICE) {
                        VUI.getInstance().shutAndTTS("当前无可用音源");
                    } else if (type == SCREEN) {
                        CommonToast.showToast("当前无可用音源");
                    }
                } else {
                    if (SitechMusicNewManager.getInstance().hasMusicPlaying()) {
                        SitechMusicNewManager.getInstance().onPlayPause();
                    } else {
                        SitechMusicNewManager.getInstance().onPlayResume();
                    }
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    }
                }
                break;
        }
    }

    public void addMusicChangeListener(MusicChangeListener listener) {
        if (null != listener) {
            if (null == listeners) {
                listeners = new ArrayList<>();
            }
            String value = listener.getClass().getAnnotation(
                    VoiceSourceType.class).value();
            switch (value) {
                case SUPPORT_TYPE_ALL:
                    break;
                case SUPPORT_TYPE_KAOLA:
                    break;
                case SUPPORT_TYPE_LOCAL:
                    break;
                case SUPPORT_TYPE_BT:
                    break;
                default:
                    return;
            }
            addListener(listener, listeners);
            switch (musicSource) {
                case KAOLA:
                    switch (value) {
                        case SUPPORT_TYPE_ALL:
                        case SUPPORT_TYPE_KAOLA:
                            notifyKaola(listener);
                            break;
                        default:
                            break;
                    }
                    break;
                case LOCAL_MUSIC:
                    SitechDevLog.i("onMusicChange", "MainActivty vourceManger addMusicChangeListener=  LOCAL_MUSIC  ==musicName==" + MusicManager.getInstance().getPlayInfo().musicName);
                    switch (value) {
                        case SUPPORT_TYPE_ALL:
                        case SUPPORT_TYPE_LOCAL:
                            if (null != MusicManager.getInstance().getPlayInfo()) {
                                listener.onMusicChange(MusicManager.getInstance().getPlayInfo().musicName);
                                switch (MusicManager.getInstance().getStatus()) {
                                    case 0:
                                        listener.pause();
                                        break;
                                    case 1:
                                        listener.resume();
                                        break;
                                }
                            } else {
                                musicSource = -1;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case BT_MUSIC:
                    break;
                default:
                    break;
            }
        }
    }

    private void notifyKaola(MusicChangeListener listener) {
        if (null != KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingItem()) {
            SitechDevLog.i("onMusicChange", "MainActivty vourceManger notifyKaola  ==getTitle==" + KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingItem().getTitle());
            listener.onMusicChange(KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingItem().getTitle());
            if (KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(context)) {
                listener.resume();
            } else {
                listener.pause();
            }
        } else {
            musicSource = -1;
        }
    }

    public void toDetailActivity() {
        switch (musicSource) {
            case KAOLA:
                toKaoLa();
                break;
            case LOCAL_MUSIC:
                toLocalMusic();
                break;
            case BT_MUSIC:
                toBtMusic();
                break;
            default:
                CommonToast.showToast("当前无可用音源");
                break;
        }
    }

    private void toKaoLa() {
//        if (null == ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember) {
//            CommonToast.makeText(context, "暂无播放的列表~~~~");
//            return;
//        }
//        Intent intent = new Intent(context, NewsDetailsActivity.class);
//        intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
//        context.startActivity(intent);

    }

    private void toLocalMusic() {
        Intent intent = new Intent();
        intent.setClass(context, MusicMainActivity.class);
        intent.putExtra("index", 1);
        context.startActivity(intent);
    }

    private void toBtMusic() {
        Intent intent = new Intent();
        intent.setClass(context, MusicMainActivity.class);
        intent.putExtra("index", 2);
        context.startActivity(intent);
    }

    public void removeMusicChangeListener(MusicChangeListener listener) {
        if (null == listeners) {
            return;
        }
        removeListener(listener, listeners);
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

    public int getMusicSource() {
        return musicSource;
    }

    public void setMusicSource(int musicSource) {
        this.musicSource = musicSource;
    }

    public interface MusicChangeListener {

        void onMusicChange(String name);

        /**
         * 暂停
         */
        void pause();

        /**
         * 继续
         */
        void resume();
    }

    public interface onPlaySourceMusicChangeListener {

        /**
         * 暂停
         */
        void onMusicPause();

        /**
         * 继续
         */
        void onMusicResume();

        /**
         * 歌曲信息
         */
        void onPlayerFailed(PlayItem item);

        /**
         * 继续
         */
        void onMusicPlaying(PlayItem item);

        /**
         * 播放完毕
         */
        void onMusicPlayEnd(PlayItem item);

        /**
         * 播放进度
         */
        void onMusicPlayProgress(String s, int i, int i1, boolean b);
    }
}
