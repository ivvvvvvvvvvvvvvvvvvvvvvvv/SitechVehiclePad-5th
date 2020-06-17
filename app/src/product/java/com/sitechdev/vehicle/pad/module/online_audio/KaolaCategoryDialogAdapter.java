
package com.sitechdev.vehicle.pad.module.online_audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.kaola.KaolaCategoryDataWarpper;

import java.util.ArrayList;
import java.util.List;

// 选择品牌列表适配器
public class KaolaCategoryDialogAdapter extends RecyclerView.Adapter<KaolaCategoryDialogAdapter.VHolderAbs> {
    private Context context;
    private List<KaolaCategoryDataWarpper> mLists;

    // 构造方法
    KaolaCategoryDialogAdapter(Context context, List<KaolaCategoryDataWarpper> mLists) {
        this.context = context;
        this.mLists = mLists == null ? new ArrayList<>() : mLists;
    }

    public void setDataAndNotify(List<KaolaCategoryDataWarpper> data) {
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
                    if (mLists.get(position).category == null) {
                        return 5;
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
        if (mLists.get(i).category == null) {
            root = LayoutInflater.from(context).inflate(R.layout.dialog_kaola_category_item_group, null);
        } else {
            root = LayoutInflater.from(context).inflate(R.layout.dialog_kaola_category_item, null);
        }
        return new VHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull VHolderAbs vh, @SuppressLint("RecyclerView") int i) {
        vh.des.setText(mLists.get(i).category == null ? mLists.get(i).name : mLists.get(i).category.getName());
        if (mLists.get(i).category == null) {
            vh.des.setText(mLists.get(i).category == null ? mLists.get(i).name : mLists.get(i).category.getName());
        } else {
            vh.des.setText(mLists.get(i).category == null ? mLists.get(i).name : mLists.get(i).category.getName());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClick) {
                        onItemClick.onClick(mLists.get(i).category);
                    }
                }
            });
        }
    }

    interface OnItemClick {
        void onClick(Category warpper);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    OnItemClick onItemClick;

    class VHolder extends VHolderAbs {

        VHolder(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.des);
        }
    }

    abstract class VHolderAbs extends RecyclerView.ViewHolder {
        TextView des;

        public VHolderAbs(@NonNull View itemView) {
            super(itemView);
        }
    }
}
