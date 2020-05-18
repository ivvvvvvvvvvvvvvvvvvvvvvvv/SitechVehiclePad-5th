package com.sitechdev.vehicle.pad.module.forshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.kaola.PlayItemAdapter;
import com.sitechdev.vehicle.pad.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author zhubaoqiang
 * @date 2019/8/24
 */
public class MusicKaolaForShowAdapter extends
        RecyclerView.Adapter<MusicKaolaForShowAdapter.MusicKaolaItemHolder> {

    private Context context;

    private List<PlayItemAdapter.Item> musicInfos;

    private int noramlColor;

    private int checkedColor;
    public static int mPrePosition = -1;

    private OnCheckEmptyListener onCheckEmptyListener;

    public MusicKaolaForShowAdapter(Context context) {
        this.context = context;
        this.musicInfos = new ArrayList<>();
        noramlColor = AppApplication.getContext().getResources().getColor(R.color.white);
        checkedColor = AppApplication.getContext().getResources().getColor(R.color.item_local_music_checked_color);
    }

    public int getCheckedPositon() {
        return mPrePosition;
    }

    @NonNull
    @Override
    public MusicKaolaItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_local_music2, viewGroup, false);
        return new MusicKaolaItemHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicKaolaItemHolder holder, int position) {
        boolean validData = position < musicInfos.size() && null != musicInfos.get(position);
        if (!validData) {
            return;
        }
        PlayItemAdapter.Item musicInfo = musicInfos.get(position);
        SitechDevLog.d("MusicKaolaAdapter", "-----------musicInfo------------" + musicInfo.title);
        if (mPrePosition == position) {
            if (PlayerManager.getInstance(context).isPaused()) {
                //已暂停
                holder.getIndex().setImageResource(R.drawable.list_icon_playing);
            } else {
                //正在播放
                // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
                // 将gif图资源转化为GifDrawable
                try {
                    GifDrawable gifDrawable = new GifDrawable(context.getResources(), R.drawable.ic_music_play_gif);

                    // gif1加载一个动态图gif
                    holder.getIndex().setImageDrawable(gifDrawable);
                } catch (Exception e) {
                    SitechDevLog.exception(e);
                }
            }
            holder.getName().setTextColor(checkedColor);
            holder.getArt().setTextColor(checkedColor);
        } else {
//            holder.getIndex().setImageResource(R.drawable.list_icon_play);
            holder.getName().setTextColor(noramlColor);
            holder.getArt().setTextColor(noramlColor);
        }
        holder.getName().setText(musicInfo.title);
        holder.getArt().setVisibility(View.GONE);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int clickIndex = (int) v.getTag();
                    mOnItemClickListener.onItemClick(musicInfos.get(clickIndex).id, clickIndex);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicInfos.size();
    }

    public void setSelected(int position) {
        SitechDevLog.e(PlayItemAdapter.class.getSimpleName(), " ======== setSelected ======= position = " + position + "mPrePosition =" + mPrePosition);
//        if (mPrePosition != position) {
//            PlayItemAdapter.Item preItem = getItemData(mPrePosition);
//            if (preItem != null) {
//                preItem.isSelected = false;
//                notifyItemChanged(mPrePosition);
//            }
//            PlayItemAdapter.Item itemData = getItemData(position);
//            if (itemData != null) {
//                itemData.isSelected = true;
//                notifyItemChanged(position);
//            }
        notifyItemChanged(mPrePosition);
        mPrePosition = position;
//        }
        notifyItemChanged(position);
    }

    public void setSelectedShow(int position) {
//        PlayItemAdapter.Item itemData = getItemData(position);
//        if (itemData != null) {
//            itemData.isSelected = true;
//            notifyItemChanged(position);
//        }
        notifyItemChanged(mPrePosition);
        mPrePosition = position;
        notifyItemChanged(position);
    }

    private PlayItemAdapter.Item getItemData(int pos) {
        if (pos >= 0 && pos < musicInfos.size()) {
            return musicInfos.get(pos);
        }
        return null;
    }

    public void setData(List<PlayItemAdapter.Item> mItemList) {
        this.musicInfos = mItemList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnCheckEmptyListener(OnCheckEmptyListener onCheckEmptyListener) {
        this.onCheckEmptyListener = onCheckEmptyListener;
    }

    public void refreshData(List<PlayItemAdapter.Item> musicInfoList, int currentPlayIndex) {
        musicInfos.clear();
        musicInfos = musicInfoList;
        mPrePosition = currentPlayIndex;
    }

    public void refreshDataChanged(List<PlayItemAdapter.Item> musicInfoList, int currentPlayIndex) {
        refreshData(musicInfoList, currentPlayIndex);
        notifyDataSetChanged();
    }

    public OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener {
        void onItemClick(long id, int position);
    }

    public static class MusicKaolaItemHolder extends RecyclerView.ViewHolder {

        private GifImageView vIndex;
        private TextView vName;
        private TextView art;

        public MusicKaolaItemHolder(@NonNull View itemView) {
            super(itemView);
            vIndex = itemView.findViewById(R.id.item_locl_music_index);
            vName = itemView.findViewById(R.id.item_locl_music_name);
            vName.setTypeface(FontUtil.getInstance().getMainFont_Min_i());
            art = itemView.findViewById(R.id.item_locl_music_art);
        }

        public GifImageView getIndex() {
            return vIndex;
        }

        public TextView getName() {
            return vName;
        }

        public TextView getArt() {
            return art;
        }
    }

    public interface OnCheckEmptyListener {
        void onCheckEmpty(boolean isEmpty);
    }
}
