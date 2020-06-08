
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

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.kaolafm.opensdk.api.operation.model.ImageFile;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.kaola.KaolaDataWarpper;
import com.sitechdev.vehicle.pad.util.BlurTransformation;
import com.sitechdev.vehicle.pad.util.GlideRoundedCornersTransform;
import com.sitechdev.vehicle.pad.view.IndexAdapter;
import com.sitechdev.vehicle.pad.view.Indexable;

import java.util.ArrayList;
import java.util.List;

// 选择品牌列表适配器
public class KaolaAIListAdapter extends RecyclerView.Adapter<KaolaAIListAdapter.VHolderAbs> implements IndexAdapter {
    private Context context;
    private List<KaolaDataWarpper> mLists;
    private final String recommendtag = "推荐";

    // 构造方法
    KaolaAIListAdapter(Context context, List<KaolaDataWarpper> mLists) {
        this.context = context;
        this.mLists = mLists == null ? new ArrayList<>() : mLists;
    }

    public void setDataAndNotify(List<KaolaDataWarpper> data) {
        this.mLists = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public Indexable getItem(int position) {
        return mLists.get(position);
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
                    if (recommendtag.equals(mLists.get(position).getIndex())) {
                        return 2;
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
        if (recommendtag.equals(mLists.get(i).getIndex())) {
            root = LayoutInflater.from(context).inflate(R.layout.kaola_item_recommend, viewGroup, false);
            return new VHolder(root);
        } else {
            root = LayoutInflater.from(context).inflate(R.layout.kaola_item_common, viewGroup, false);
            return new VHolder2(root);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull VHolderAbs vh, @SuppressLint("RecyclerView") int i) {
        vh.des.setText(mLists.get(i).column.getTitle());
        if (mLists.get(i).column.getImageFiles() != null) {
            ImageFile img = null;
            if (mLists.get(i).column.getImageFiles().containsKey("icon")) {
                img = mLists.get(i).column.getImageFiles().get("icon");
            }
            if (mLists.get(i).column.getImageFiles().containsKey("cover")) {
                img = mLists.get(i).column.getImageFiles().get("cover");
            }
            if (img != null) {
                GlideApp.with(context).load(img.getUrl()).centerCrop().into(vh.img);
                if (!recommendtag.equals(mLists.get(i).getIndex())) {
                    MultiTransformation multiTransformation = new MultiTransformation(new BlurTransformation(15, 1),
                            new GlideRoundedCornersTransform(8
                                    , GlideRoundedCornersTransform.CornerType.ALL));
                    RequestOptions options = RequestOptions
                            .placeholderOf(R.drawable.bg_kaola_audioitem_common)
                            .bitmapTransform(multiTransformation);
                    GlideApp.with(context).load(img.getUrl()).apply(options).into(vh.bg);
                }
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

    interface OnItemClick {
        void onClick(KaolaDataWarpper warpper);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    OnItemClick onItemClick;

    class VHolder extends VHolderAbs {

        VHolder(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.des);
            img = itemView.findViewById(R.id.image);
        }
    }

    class VHolder2 extends VHolderAbs {

        VHolder2(View itemView) {
            super(itemView);
            des = itemView.findViewById(R.id.des);
            img = itemView.findViewById(R.id.image);
            bg = itemView.findViewById(R.id.image_bg);
        }
    }

    abstract class VHolderAbs extends RecyclerView.ViewHolder {
        TextView des;
        ImageView img;
        ImageView bg;

        public VHolderAbs(@NonNull View itemView) {
            super(itemView);
        }
    }

    ;
}
