
package com.sitechdev.vehicle.pad.module.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.router.RouterConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VHolderAbs> {
    private Context context;
    private List<VideoInfo> mLists = new ArrayList<>();

    VideoListAdapter(Context context) {
        this.context = context;
        this.mLists = VideoManager.getLocalVideos(context);
    }

    public void refreshData() {
        this.mLists = VideoManager.getLocalVideos(context);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public VHolderAbs onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.video_list_item, viewGroup, false);
        return new VHolder2(root);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolderAbs vh, @SuppressLint("RecyclerView") int i) {
        vh.des.setText(mLists.get(i).title);
        if (TextUtils.isEmpty(mLists.get(i).thumbnails)) {
            GlideApp.with(context).load(Uri.fromFile(new File(mLists.get(i).data))).into(vh.img);
        } else {
            GlideApp.with(context).load(mLists.get(i).thumbnails).into(vh.img);
        }
    }

    interface OnItemClick {
        void onClick(VideoInfo info);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    OnItemClick onItemClick;

    class VHolder2 extends VHolderAbs {

        VHolder2(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.image);
        }
    }


    abstract class VHolderAbs extends RecyclerView.ViewHolder {
        TextView des;
        ImageView img;

        public VHolderAbs(@NonNull View itemView) {
            super(itemView);
        }
    }
}
