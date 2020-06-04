package com.sitechdev.vehicle.pad.module.feedback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.feedback.FeedbackHistoryBean;
import com.sitechdev.vehicle.pad.view.FeedbackVoiceView;

import java.util.List;

/**
 * 反馈历史界面adapter
 *
 * @author cold
 */
public class FeedbackHistoryAdapter extends RecyclerView.Adapter<FeedbackHistoryAdapter.ViewHolder> implements View.OnClickListener {
    private String TAG = "FeedbackHistoryAdapter";
    private Context mContext;
    private List<FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean> mList;
    private int mPlayingIndex = -1;

    public FeedbackHistoryAdapter(Context mContext, List mList) {
        this.mContext = mContext;
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// huabai de xuran aoguyingfeng
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_history_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        if(holder.view.getPosition() != position) {
            holder.view.setData(mList.get(position));
//        }
        holder.view.setPosition(position);
        holder.view.layout.setOnClickListener(v -> {
            holder.view.clickItem();
            mPlayingIndex = holder.view.getPosition();
            notifyDataSetChanged();
        });
        holder.view.layout.setTag(holder);
        if (mPlayingIndex != -1 && position != mPlayingIndex) {
            holder.view.interrupt();
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.view.clickItem();
        mPlayingIndex = holder.view.getPosition();
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private FeedbackVoiceView view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.feedback_history_item_view);
        }
    }

}
