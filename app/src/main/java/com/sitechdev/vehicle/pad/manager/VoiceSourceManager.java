package com.sitechdev.vehicle.pad.manager;

import android.content.Context;
import android.content.Intent;

import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.kaola.ColumnMemberMamager;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.kaola.NewsDetailsActivity;
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
    public static final int VOICE = 0;
    public static final int SCREEN = VOICE + 1;
    /**
     * 语音交互暂停音乐
     */
    public static final int CONTENT = SCREEN + 1;
    private List<WeakReference<MusicChangeListener>> listeners;
    private boolean beContent = false;
    public static final String SUPPORT_TYPE_ALL = "all";
    public static final String SUPPORT_TYPE_KAOLA = "kaola";
    public static final String SUPPORT_TYPE_LOCAL = "local";

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
                PlayItem curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
                if (curPlayItem != null) {
                    String title = curPlayItem.getTitle();
                    MusicChangeListener listener = ref.get();
                    String value = listener.getClass().getAnnotation(
                            VoiceSourceType.class).value();
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
                if (PlayerListManager.getInstance().getCurPlayItem() == null) {
                    continue;
                }
                String value = listener.getClass().getAnnotation(
                        VoiceSourceType.class).value();
                if (value.equals(SUPPORT_TYPE_ALL) || value.equals(SUPPORT_TYPE_KAOLA)) {
                    listener.pause();
                }
                if (PlayerListManager.getInstance().getCurPlayItem() != null) {
                    String title = PlayerListManager.getInstance().getCurPlayItem().getTitle();
                    switch (value) {
                        case SUPPORT_TYPE_ALL:
                            listener.onMusicChange(title);
                            break;
                        case SUPPORT_TYPE_KAOLA:
                            listener.onMusicChange(title);
                            break;
                        case SUPPORT_TYPE_LOCAL:
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
                if (PlayerManager.getInstance(context).hasPre()) {
                    PlayerManager.getInstance(context).playPre();
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
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
                }
                break;
        }
    }

    public void next(int type) {
        switch (musicSource) {
            case KAOLA:
                if (PlayerManager.getInstance(context).hasNext()) {
                    PlayerManager.getInstance(context).playNext();
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
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
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
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
                }
                break;
        }
    }

    public void pause(int type) {
        if (beContent) {
            beContent = false;
            VUI.getInstance().shut();
            return;
        }
        switch (musicSource) {
            case KAOLA:
                if (PlayerManager.getInstance(context).isPlaying()) {
                    PlayerManager.getInstance(context).pause();
                    if (type == VOICE) {
                        VUIWindow.getInstance().hide();
                    } else if (type == CONTENT) {
                        //do nothing
                        beContent = true;
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
                                beContent = true;
                            }
                        }
                    }
                });
                break;
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
                }
                break;
        }
    }

    public void resume(int type) {
        if (type == CONTENT) {
            if (!beContent) {
                return;
            }
            beContent = false;
        }
        switch (musicSource) {
            case KAOLA:
                if (!PlayerManager.getInstance(context).isPlaying()) {
                    PlayerManager.getInstance(context).switchPlayerStatus();
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
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
                }
                break;
        }
    }

    public void toggle(int type) {
        switch (musicSource) {
            case KAOLA:
                PlayerManager.getInstance(context).switchPlayerStatus();
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
            default:
                if (type == VOICE) {
                    VUI.getInstance().shutAndTTS("当前无可用音源");
                } else if (type == SCREEN) {
                    CommonToast.showToast("当前无可用音源");
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
                default:
                    break;
            }
        }
    }

    private void notifyKaola(MusicChangeListener listener) {
        if (null != PlayerListManager.getInstance().getCurPlayItem()) {
            listener.onMusicChange(PlayerListManager.getInstance().getCurPlayItem().getTitle());
            if (PlayerManager.getInstance(AppApplication.getContext()).isPaused()) {
                listener.pause();
            }
            if (PlayerManager.getInstance(AppApplication.getContext()).isPlaying()) {
                listener.resume();
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
            default:
                CommonToast.showToast("当前无可用音源");
                break;
        }
    }

    private void toKaoLa() {
        if (null == ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember) {
            CommonToast.makeText(context, "暂无播放的列表~~~~");
            return;
        }
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
        context.startActivity(intent);

    }

    private void toLocalMusic() {
        Intent intent = new Intent();
        intent.setClass(context, MusicMainActivity.class);
        intent.putExtra("index", 1);
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
