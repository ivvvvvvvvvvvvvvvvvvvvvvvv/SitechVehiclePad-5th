package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.TimeUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.music.MusicConfig;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.vui.VUIUtils;

import java.util.List;

public class CustomPlaySeekBar extends RelativeLayout implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MusicManager.OnMusicChangeListener,
        MusicManager.OnMusicListUpdateListener, MusicManager.OnMusicPostionChangeListener {
    private TextView mTime, mTotalTime;
    private ImageView mPlayMode, mPre, mNext, mPlayPause;
    private SeekBar mSeekBar;
    private View mSeekLayout, mCtrlLayout;
    private boolean isPlaying;
    private OnPlayControlListener mPlayCtrlListener;
    private OnPlaySeekChangedListener mSeekChangedListener;
    private OnMusicInfoChangedListener mMusicInfoChangedListener;
    private int musicSource;

    public void setMusicSource(int musicSource) {
        this.musicSource = musicSource;
    }

    public void setOnPlayCtrlListener(OnPlayControlListener mPlayCtrlListener) {
        this.mPlayCtrlListener = mPlayCtrlListener;
    }

    public void setOnSeekBarChangedListener(OnPlaySeekChangedListener mSeekChangedListener) {
        this.mSeekChangedListener = mSeekChangedListener;
    }

    public void setOnMusicInfoChangedListener(OnMusicInfoChangedListener mMusicInfoChangedListener) {
        this.mMusicInfoChangedListener = mMusicInfoChangedListener;
    }

    public CustomPlaySeekBar(Context context) {
        this(context, null);
    }

    public CustomPlaySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPlaySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_play_seekbar, this);
        mTime = view.findViewById(R.id.custom_play_seekbar_time);
        mTotalTime = view.findViewById(R.id.custom_play_seekbar_totaltime);
        mSeekBar = view.findViewById(R.id.custom_play_seekbar_seecbar);
        mPlayMode = view.findViewById(R.id.custom_play_ctr_mode);
        mPre = view.findViewById(R.id.custom_play_ctr_pre);
        mNext = view.findViewById(R.id.custom_play_ctr_next);
        mPlayPause = view.findViewById(R.id.custom_play_ctr_playpause);
        mSeekLayout = view.findViewById(R.id.custom_play_seekbar_layout);
        mCtrlLayout = view.findViewById(R.id.custom_play_ctr_layout);
        mPlayMode.setOnClickListener(this);
        mPre.setOnClickListener(this);
        mPlayPause.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void setTotalTime(String totalTime) {
        mTotalTime.setText(totalTime);
    }

    public void setTime(String time) {
        mTime.setText(time);
    }

    public void setProgress(int progress) {
        mSeekBar.setProgress(progress);
        mTime.setText(TimeUtils.timeParse(progress));
    }

    public void setMax(int max) {
        mSeekBar.setMax(max);
    }

    public void setModeVisible(boolean isShown) {
        mPlayMode.setVisibility(isShown ? VISIBLE : GONE);
    }

    public void setSeekBarVisible(boolean isShown) {
        mSeekLayout.setVisibility(isShown ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_play_ctr_pre:
                if (null != mPlayCtrlListener) {
                    mPlayCtrlListener.onPre();
                }
                VoiceSourceManager.getInstance().setMusicSource(musicSource);
                VoiceSourceManager.getInstance().pre(VoiceSourceManager.SCREEN);
                break;
            case R.id.custom_play_ctr_playpause:
                if (null != mPlayCtrlListener) {
                    if (isPlaying) {
                        mPlayCtrlListener.onPlay();
                    } else {
                        mPlayCtrlListener.onPause();
                    }
                }
                VoiceSourceManager.getInstance().setMusicSource(musicSource);
                VoiceSourceManager.getInstance().toggle(VoiceSourceManager.SCREEN);
                break;
            case R.id.custom_play_ctr_next:
                if (null != mPlayCtrlListener) {
                    mPlayCtrlListener.onNext();
                }
                VoiceSourceManager.getInstance().setMusicSource(musicSource);
                VoiceSourceManager.getInstance().next(VoiceSourceManager.SCREEN);
                break;
            case R.id.custom_play_ctr_mode:
                if (null == MusicConfig.getInstance().getModeType() || MusicConfig.getInstance().getModeType() == MusicConfig.MusicModeType.MUSIC_MODE_LOOP) {
                    MusicConfig.getInstance().setModeType(MusicConfig.MusicModeType.MUSIC_MODE_SINGLE);
                    mPlayMode.setImageResource(R.drawable.music_usb_mode_single);
                } else if (MusicConfig.getInstance().getModeType() == MusicConfig.MusicModeType.MUSIC_MODE_SINGLE) {
                    MusicConfig.getInstance().setModeType(MusicConfig.MusicModeType.MUSIC_MODE_RANDOM);
                    mPlayMode.setImageResource(R.drawable.music_usb_mode_random);
                } else if (MusicConfig.getInstance().getModeType() == MusicConfig.MusicModeType.MUSIC_MODE_RANDOM) {
                    MusicConfig.getInstance().setModeType(MusicConfig.MusicModeType.MUSIC_MODE_LOOP);
                    mPlayMode.setImageResource(R.drawable.music_usb_mode_loop);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            mSeekBar.setProgress(0);
            mSeekBar.setEnabled(false);
            mTime.setText("00:00");
            mTotalTime.setText("00:00");
            mPre.setEnabled(false);
            mNext.setEnabled(false);
            mPlayMode.setEnabled(false);
            mPlayPause.setEnabled(false);
        } else {
            mSeekBar.setEnabled(true);
            mPre.setEnabled(true);
            mNext.setEnabled(true);
            mPlayMode.setEnabled(true);
            mPlayPause.setEnabled(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (null != mSeekChangedListener) {
            mSeekChangedListener.onSeekChanged(progress, fromUser);
        }
        if (fromUser) {
            MusicManager.getInstance().seekTo(new MusicManager.CallBack<String>() {
                @Override
                public void onCallBack(int code, String s) {

                }
            }, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (MusicManager.getInstance().getStatus() == MusicManager.OnMusicChangeListener.PAUSE) {
            MusicManager.getInstance().toggle(new MusicManager.CallBack<String>() {
                @Override
                public void onCallBack(int code, String s) {

                }
            });
        }
    }

    public void setCurrentStatusPlaying(boolean isPlaying) {
        mPlayPause.setActivated(isPlaying);
    }
    @Override
    public void onMusicChange(MusicInfo current, int status) {
        if (null != current) {
            switch (status) {
                case MusicManager.OnMusicChangeListener.PAUSE:
                    mPlayPause.setActivated(false);
                    break;
                case MusicManager.OnMusicChangeListener.RESUME:
                    mPlayPause.setActivated(true);
                    break;
            }
            mTotalTime.setText(TimeUtils.timeParse(current.duration) + "");
            mSeekBar.setMax(current.duration);
        } else {
            mPlayPause.setActivated(false);
        }
        if (null != mMusicInfoChangedListener && VUIUtils.isUdiskExist()) {
            mMusicInfoChangedListener.onMusicInfoChanged(current, status);
        }
    }

    @Override
    public void onMusicListUpdate(List<MusicInfo> infos, MusicInfo info, int status, int postion) {
    }

    @Override
    public void onPositionChange(MusicInfo currentInfo, int position) {
        mSeekBar.setMax(currentInfo.duration);
        if (position > currentInfo.duration) {
            position = currentInfo.duration;
        }
        mSeekBar.setProgress(position);
        MusicConfig.getInstance().setProgress(position);
        mTime.setText(TimeUtils.timeParse(position) + "");
    }

    public interface OnPlayControlListener {
        void onPre();

        void onPlay();

        void onPause();

        void onNext();
    }

    public interface OnPlaySeekChangedListener {
        void onTimeChanged(String time);

        void onTotalTimeChanged(String totalTime);

        void onSeekChanged(int progress, boolean fromUser);
    }

    public interface OnMusicInfoChangedListener {
        void onMusicInfoChanged(MusicInfo current, int status);
    }
}
