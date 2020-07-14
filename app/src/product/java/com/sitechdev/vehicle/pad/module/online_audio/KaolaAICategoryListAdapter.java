
package com.sitechdev.vehicle.pad.module.online_audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaolafm.opensdk.api.operation.model.ImageFile;
import com.kaolafm.opensdk.api.operation.model.category.AlbumCategoryMember;
import com.kaolafm.opensdk.api.operation.model.category.BroadcastCategoryMember;
import com.kaolafm.opensdk.api.operation.model.category.CategoryMember;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;

import java.util.ArrayList;
import java.util.List;

public class KaolaAICategoryListAdapter extends KaolaBaseAdapter<KaolaAICategoryListAdapter.VHolderAbs> {
    private Context context;
    private List<CategoryMember> mLists;

    // 构造方法
    KaolaAICategoryListAdapter(Context context, List<CategoryMember> mLists) {
        super(context);
        this.context = context;
        this.mLists = mLists == null ? new ArrayList<>() : mLists;
    }

    public void setDataAndNotify(List<CategoryMember> data) {
        this.mLists = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isLandscape()) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public VHolderAbs onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = null;
        root = LayoutInflater.from(context).inflate(R.layout.kaola_item_category, viewGroup, false);
        return new VHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolderAbs vh, @SuppressLint("RecyclerView") int i) {
        CategoryMember categoryMember = mLists.get(i);
        vh.des.setText(mLists.get(i).getTitle());
        if (categoryMember instanceof AlbumCategoryMember) {
            AlbumCategoryMember albumCategoryMember = (AlbumCategoryMember) categoryMember;
            vh.title.setText(albumCategoryMember.getTitle());
            setPlayTime(vh.des, albumCategoryMember.getPlayTimes());
        }
        if (categoryMember instanceof BroadcastCategoryMember) {
            BroadcastCategoryMember broadcastCategoryMember = (BroadcastCategoryMember) categoryMember;
            vh.title.setText(broadcastCategoryMember.getTitle());
            setPlayTime(vh.des, broadcastCategoryMember.getPlayTimes());
        }
        if (mLists.get(i).getImageFiles() != null ) {
            if (mLists.get(i).getImageFiles().containsKey("icon")) {
                ImageFile img = mLists.get(i).getImageFiles().get("icon");
                GlideApp.with(context).load(img.getUrl()).placeholder(R.drawable.default_audio).centerCrop().into(vh.img);
            }
            if (mLists.get(i).getImageFiles().containsKey("cover")) {
                ImageFile img = mLists.get(i).getImageFiles().get("cover");
                GlideApp.with(context).load(img.getUrl()).placeholder(R.drawable.default_audio_2).centerCrop().into(vh.img);
            }
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onClick(mLists.get(i));
                }
            }
        });
    }

    private void setPlayTime(TextView tv_subtitle_name, int playTime) {
        if (playTime > 100000000) {
            tv_subtitle_name.setText("播放次数：" + playTime / 100000000 + "亿");
        } else if (playTime > 10000) {
            tv_subtitle_name.setText("播放次数：" + playTime / 10000 + "万");
        } else {
            tv_subtitle_name.setText("播放次数：" + playTime);
        }
    }

    interface OnItemClick {
        void onClick(CategoryMember warpper);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    OnItemClick onItemClick;

    class VHolder extends VHolderAbs {

        VHolder(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.des);
            title = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.image);
        }
    }

    class VHolder2 extends VHolderAbs {

        VHolder2(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.des);
            img = itemView.findViewById(R.id.image);
        }
    }

    abstract class VHolderAbs extends RecyclerView.ViewHolder {
        TextView des;
        TextView title;
        ImageView img;

        public VHolderAbs(@NonNull View itemView) {
            super(itemView);
        }
    }

    ;
}
