package com.sitechdev.vehicle.pad.module.member.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.sitechdev.vehicle.lib.util.TimeUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.member.bean.PointsInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Sitech
 * 类名称：CustomDestinationAdapter
 * 类描述：
 * 创建人：wzb
 * 创建时间：2018/11/16 11:59
 * 修改时间：
 * 备注：
 */
public class PointDataItemAdapter extends RecyclerView.Adapter<PointDataItemAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<PointsInfoBean.PointsDataBean.PointsListBean> mDatas;

    //创建构造参数
    public PointDataItemAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void updateData(List<PointsInfoBean.PointsDataBean.PointsListBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    //创建ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_mypoint_single_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, mItemClickListener);
        return viewHolder;
    }

    //绑定ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PointsInfoBean.PointsDataBean.PointsListBean item = mDatas.get(position);
        if (item == null) {
            return;
        }
        //缘由
        holder.pointTitleTv.setText(item.getRemark());
        //获得时间
        holder.pointDateTv.setText(TimeUtils.formatTime(Long.parseLong(item.getCreateTime()), "yyyy-MM-dd HH:mm"));
        //数量
        int integral = Integer.parseInt(item.getIntegral());
        if (integral > 0) {
            holder.pointNumberTv.setText("+" + item.getIntegral());
        } else {
            holder.pointNumberTv.setText(item.getIntegral());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    //新增item
    public void addData(int pos) {
//        mDatas.add("新增");
        notifyItemInserted(pos);
    }

    //移除item
    public void deleateData(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

    private MyItemClickListener mItemClickListener;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pointTitleTv, pointDateTv, pointNumberTv;
        View itemHolderView;

        public MyViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            itemHolderView = itemView;
            pointTitleTv = itemView.findViewById(R.id.id_item_point_title);
            pointDateTv = itemView.findViewById(R.id.id_item_point_date);
            pointNumberTv = itemView.findViewById(R.id.id_item_point_info);
            mItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        public void setViewTag(PoiItem item) {
            if (itemHolderView != null) {
                itemHolderView.setTag(item);
            }
        }

        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;

    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }


//    //我的当前位置与点击的marker的距离a
//    LatLng latlngA = new LatLng(myLat, myLongt);
//    distance = AMapUtils.calculateLineDistance(latlngA, marker.getPosition());
//    tvDistance.setText("两点间距离为：" + distance + "m");


}


