
package com.sitechdev.vehicle.pad.module.online_audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaolafm.opensdk.api.search.model.SearchProgramBean;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;

import java.util.ArrayList;
import java.util.List;

public class KaolaSearchAdapter extends RecyclerView.Adapter<KaolaSearchAdapter.VHolderAbs> {
    private Context context;
    private List<SearchProgramBean> mLists;
    public List<SearchProgramBean> getData(){
        return mLists;
    }
    // 构造方法
    KaolaSearchAdapter(Context context, List<SearchProgramBean> mLists) {
        this.context = context;
        this.mLists = mLists == null ? new ArrayList<>() : mLists;
    }

    public void setDataAndNotify(List<SearchProgramBean> data) {
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
        SearchProgramBean searchProgramBean = mLists.get(i);
        vh.title.setText(searchProgramBean.getName());
        vh.des.setText(searchProgramBean.getAlbumName());
        GlideApp.with(context).load(searchProgramBean.getImg()).into(vh.img);
//        setPlayTime(vh.des, searchProgramBean.get);
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
        void onClick(SearchProgramBean warpper);
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
