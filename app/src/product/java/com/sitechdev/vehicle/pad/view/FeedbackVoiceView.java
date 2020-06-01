package com.sitechdev.vehicle.pad.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.TimeUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.feedback.FeedbackHistoryBean;
import com.sitechdev.vehicle.pad.model.feedback.utils.FeedbackConfig;
import com.sitechdev.vehicle.pad.model.feedback.utils.MyPlayMusicHelper;


/**
 * @author cold
 */
public class FeedbackVoiceView extends RelativeLayout implements MyPlayMusicHelper.PlayListener {
    private String TAG = "FeedbackVoiceView";
    private TextView date;
    private TextView duration;
    public ConstraintLayout layout;
    private ImageView animView;
    private ProgressBar progress;
    private MyPlayMusicHelper mHelper;
    private int mPosition = -1;
    private String mFilePath = null;

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    private FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean data;

    public void setData(FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean data) {
        this.data = data;
        initData(data);
    }

    public FeedbackVoiceView(Context context) {
        this(context, null);
    }

    public FeedbackVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedbackVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        mHelper = new MyPlayMusicHelper(context, this);
    }

    private void initData(FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean data) {
        this.animView.setBackgroundResource(R.drawable.feedback_audio_anim3);
        String date = TimeUtils.formatTime(Long.parseLong(data.getCreateTime()));
        this.date.setText(date);
        this.duration.setText(data.getVoiceLength() + "s");
        setDurationLength(this.layout, Integer.parseInt(data.getVoiceLength()));
//        this.layout.setOnClickListener(this);
    }

    private void setDurationLength(View view, int duration) {
        int extendWidth = 200;
        if (duration >= 50) {
            extendWidth = 400;
        } else if (duration < 50 && duration >= 40) {
            extendWidth = 350;
        } else if (duration < 40 && duration >= 30) {
            extendWidth = 300;
        } else if (duration < 30 && duration >= 20) {
            extendWidth = 250;
        } else if (duration < 20 && duration >= 10) {
            extendWidth = 220;
        } else if (duration < 10) {
            extendWidth = 180;
        }
        LayoutParams params =
                (LayoutParams) view.getLayoutParams();
        params.width = extendWidth;
        view.setLayoutParams(params);
    }

    private void initView(Context context) {
        View itemView =
                LayoutInflater.from(context).inflate(R.layout.layout_feedback_history_item, this);
        this.date = itemView.findViewById(R.id.feedback_history_item_date);
        this.duration = itemView.findViewById(R.id.feedback_history_item_duration);
        this.layout = itemView.findViewById(R.id.feedback_history_item_voice);
        this.animView = itemView.findViewById(R.id.feedback_history_item_anim);
        this.progress = itemView.findViewById(R.id.feedback_history_item_progress);
    }

    private void play(String file) {
        if (null != mHelper && !TextUtils.isEmpty(file)) {
            if (null == FeedbackConfig.getInstance().getFileMap().get(data.getContentUrl())) {
                progress.setVisibility(VISIBLE);
            }
            mHelper.playUrl(file,
                    FeedbackConfig.getInstance().getFileMap().get(data.getContentUrl()));
        }
    }


    private void stop(String from) {
        if (this.animView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) this.animView.getBackground();
            animation.stop();
            this.animView.setBackgroundResource(R.drawable.feedback_audio_anim3);
        }
        if (null != mHelper) {
//            mHelper.stop();
        }
        progress.setVisibility(GONE);
    }

    @Override
    public void onPlayStart() {
        progress.setVisibility(GONE);
        animView.setBackgroundResource(R.drawable.feedback_audio_animation_list);
        if (this.animView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) this.animView.getBackground();
            animation.start();
        }
    }

    @Override
    public void onPlayStop() {
        stop("onPlayStop");
    }

    boolean hasLoaded = false;

    @Override
    public void onSourceLoaded(String filePath) {
        hasLoaded = true;
        mFilePath = filePath;
        FeedbackConfig.getInstance().getFileMap().put(data.getContentUrl(), filePath);
    }

    public void interrupt() {
        stop("interrupt");
    }

    public void clickItem() {
        play(data.getContentUrl());
    }
}
