package com.sitechdev.vehicle.pad.module.map.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;

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
public class PoiDataItemAdapter extends RecyclerView.Adapter<PoiDataItemAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<PoiItem> mDatas;

    //创建构造参数
    public PoiDataItemAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void updateData(List<PoiItem> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    //创建ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_poi_data_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, mItemClickListener);
        return viewHolder;
    }

    //绑定ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PoiItem item = mDatas.get(position);
        if (item == null) {
            return;
        }
        holder.setViewTag(item);
        //为textview 赋值
        holder.titleTv.setText(item.getTitle());
        String distance = "";
        if (LocationData.getInstance().getaMapLocation() != null && item.getLatLonPoint() != null) {
            distance = MapUtil.getDistance(LocationData.getInstance().getaMapLocation().getLatitude(),
                    LocationData.getInstance().getaMapLocation().getLongitude(),
                    item.getLatLonPoint().getLatitude(),
                    item.getLatLonPoint().getLongitude());
//            holder.distanceTv.setText("距离您" + item.getDistance());
            holder.distanceTv.setText(String.format("距离您 %s", distance));
        }
        holder.addressTv.setText(MapUtil.getPoiAddressInfo(item));
//        SitechDevLog.w(AppConst.TAG, "***************************************************");
        //getTitle=正定县解放街小学,getAdName=正定县,getCityName=石家庄市,getProvinceName=河北省,
        // getSnippet=镇州南街与中山东路交汇西,getTypeDes=科教文化服务;学校;小学,getBusinessArea=,
        // getDistance=-1,toString=正定县解放街小学
//        SitechDevLog.i(AppConst.TAG, "兴趣点信息==》getTitle=" + item.getTitle()
//                + ",getAdName=" + item.getAdName()
//                + ",getCityName=" + item.getCityName()
//                + ",getProvinceName=" + item.getProvinceName()
//                + ",getSnippet=" + item.getSnippet()
//                + ",getTypeDes=" + item.getTypeDes()
//                + ",getBusinessArea=" + item.getBusinessArea()
//                + ",getDistance=" + item.getDistance()
//                + ",toString=" + item.toString()
//        );
//        SitechDevLog.w(AppConst.TAG, "=====================================================");

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
        TextView titleTv, distanceTv, addressTv;
        View itemHolderView;

        public MyViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            itemHolderView = itemView;
            titleTv = itemView.findViewById(R.id.id_tv_title);
            addressTv = itemView.findViewById(R.id.id_tv_address);
            distanceTv = itemView.findViewById(R.id.id_tv_distance);
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


