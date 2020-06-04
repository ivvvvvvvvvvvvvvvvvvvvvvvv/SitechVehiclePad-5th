package com.sitechdev.vehicle.pad.kaola;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Description:
 *
 * @author Steve_qi
 * @date: 2019/8/15
 */
public class PlayItemAdapter extends RecyclerView.Adapter<PlayItemAdapter.PopViewHolder> {

    private Context mContext;
    private List<Item> mPlayItemList;
    public static int mPrePosition = -1;

    public static class Item {

        public long id;

        public int type;

        public String title;

        public String details;

        public boolean isSelected;

        public int status;

        public PlayItem item;
    }

    public PlayItemAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public PopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_play_list, parent, false);
        return new PlayItemAdapter.PopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopViewHolder holder, int position) {
        if (mPlayItemList != null) {
            Item item = mPlayItemList.get(position);
            String formatID = generateID(position + 1);
            holder.btn_content.setText(formatID + "   " + item.title);

            SitechDevLog.e(PlayItemAdapter.class.getSimpleName(), "========= isSelected " + mPlayItemList.get(position).isSelected);
            if (mPlayItemList.get(position).isSelected) {
                holder.btn_content.setTextColor(Color.parseColor("#239CE2"));
            } else {
                holder.btn_content.setTextColor(Color.WHITE);
            }

            holder.btn_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(item.id, position);
                }
            });

        }

    }

    private String generateID(int position) {
        DecimalFormat mFormat = new DecimalFormat("00");
        String formatStr = mFormat.format(position);
        return formatStr;
    }

    @Override
    public int getItemCount() {
        return mPlayItemList.size();
    }

    public void setSelected(int position) {
        SitechDevLog.e(PlayItemAdapter.class.getSimpleName(), " ======== setSelected ======= position = " + position + "mPrePosition =" + mPrePosition);
        if (mPrePosition != position) {
            PlayItemAdapter.Item preItem = getItemData(mPrePosition);
            if (preItem != null) {
                preItem.isSelected = false;
                notifyItemChanged(mPrePosition);
            }
            PlayItemAdapter.Item itemData = getItemData(position);
            if (itemData != null) {
                itemData.isSelected = true;
                notifyItemChanged(position);
            }
            mPrePosition = position;
        }
    }

    public void setSelectedShow(int position) {
        PlayItemAdapter.Item itemData = getItemData(position);
        if (itemData != null) {
            itemData.isSelected = true;
            notifyItemChanged(position);
        }
        mPrePosition = position;
    }

    private Item getItemData(int pos) {
        if (pos >= 0 && pos < mPlayItemList.size()) {
            return mPlayItemList.get(pos);
        }
        return null;
    }

    public void setData(List<Item> mItemList) {
        this.mPlayItemList = mItemList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class PopViewHolder extends RecyclerView.ViewHolder {
        private TextView btn_content;

        public PopViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_content = itemView.findViewById(R.id.btn_content);
        }
    }

    public OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener {
        void onItemClick(long id, int position);
    }
}
