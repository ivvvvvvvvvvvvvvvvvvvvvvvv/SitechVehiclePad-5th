package com.sitechdev.vehicle.pad.kaola;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.kaolafm.opensdk.api.operation.model.ImageFile;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.util.BlurTransformation;
import com.sitechdev.vehicle.pad.util.GlideRoundedCornersTransform;

import java.util.List;
import java.util.Map;



/**
 * Description:
 *
 * @author Steve_qi
 * @date: 2019/8/15
 */
public class KaolaListAdapter extends RecyclerView.Adapter<KaolaListAdapter.ListViewHolder> {
    private List<ColumnMember> mColumnMenbers;
    private final String KEY_COVER = "cover";
    private Context mContext;

    public KaolaListAdapter(Context context, List<ColumnMember> columnMembers) {
        mContext = context;
        mColumnMenbers = columnMembers;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kaola_column, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if (mColumnMenbers != null && mColumnMenbers.get(position) != null) {
            ColumnMember columnMember = mColumnMenbers.get(position);
            if (columnMember.getTitle() != null) {
                holder.tvName.setText(columnMember.getTitle());
            }
            Map<String, ImageFile> imageFiles = columnMember.getImageFiles();
            if (imageFiles != null) {
                ImageFile imageFile = imageFiles.get(KEY_COVER);
                if (imageFile != null && imageFile.getUrl() != null) {
//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    MultiTransformation multiTransformation = new MultiTransformation(new BlurTransformation(15, 1),
                      new GlideRoundedCornersTransform(10
                              , GlideRoundedCornersTransform.CornerType.ALL)
                    );
                    RequestOptions options = RequestOptions
                            .placeholderOf(R.drawable.cover_bg)
                            .bitmapTransform(multiTransformation);
                    Glide.with(mContext).load(imageFile.getUrl()).apply(options).into(holder.iv_cover_bg);
                    RequestOptions options2 = RequestOptions.placeholderOf(R.drawable.cover_bg).circleCropTransform();
                    Glide.with(mContext).load(imageFile.getUrl()).apply(options2).into(holder.ivImage);
                }
            }

            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mColumnMenbers == null ? 0 : mColumnMenbers.size();
    }

    public void setData(List<ColumnMember> columnMembers) {
        this.mColumnMenbers = columnMembers;
        notifyDataSetChanged();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivImage;
        private ImageView iv_cover_bg;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_cover);
            iv_cover_bg = itemView.findViewById(R.id.iv_cover_bg);
        }
    }

    public void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
