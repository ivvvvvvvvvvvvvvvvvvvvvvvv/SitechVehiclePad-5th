package com.sitechdev.vehicle.pad.module.setting.bt;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.my.hw.BtDeviceBean;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.pad.R;

import java.util.List;

public class BtListAdapter extends RecyclerView.Adapter<BtListAdapter.BtItemViewHolder> {

    private List<BtDeviceBean> mData;
    private Context mContext;
    private OnBtItemClickListener mListener;

    public BtListAdapter(Context mContext, List<BtDeviceBean> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setOnBtClickListener(OnBtItemClickListener listener) {
        mListener = listener;
    }

    public void notifyList(List<BtDeviceBean> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @Override
    public BtItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pairlist, parent, false);
        return new BtItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BtItemViewHolder holder, final int position) {
        final BtDeviceBean bean = mData.get(position);
        if (null == bean) {
            return;
        }
        holder.tvDevName.setText(bean.getBtName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (SettingConfig.getInstance().isBtConnected() && bean.getBtAddress().equals(SettingConfig.getInstance().getConnectBtAdd())) {
                        mListener.onDisconnect(position, bean);
                    } else {
                        mListener.onConnect(position, bean);
                    }
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDelete(position, SettingConfig.getInstance().isBtConnected() && bean.getBtAddress().equals(SettingConfig.getInstance().getConnectBtAdd()),bean);
                }
            }
        });
        if (SettingConfig.getInstance().isBtConnected() && bean.getBtAddress().equals(SettingConfig.getInstance().getConnectBtAdd())) {
            holder.tvConnDisc.setText("已连接");
            holder.tvDevName.setTextColor(mContext.getResources().getColor(R.color.wifi_txt));
            holder.tvConnDisc.setTextColor(mContext.getResources().getColor(R.color.wifi_txt));
        } else {
            holder.tvConnDisc.setText("未连接");
            holder.tvDevName.setTextColor(Color.WHITE);
            holder.tvConnDisc.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class BtItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDevName;
        private TextView tvConnDisc;
        private ImageView ivDelete;

        public BtItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDevName = itemView.findViewById(R.id.tv_dev_name);
            tvConnDisc = itemView.findViewById(R.id.tv_conn_disc);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnBtItemClickListener {
        void onDelete(int pos, boolean isCurrent,BtDeviceBean btDeviceBean);

        void onConnect(int pos, BtDeviceBean btDeviceBean);

        void onDisconnect(int pos, BtDeviceBean btDeviceBean);
    }
}
