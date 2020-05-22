
package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.view.IndexAdapter;
import com.sitechdev.vehicle.pad.view.Indexable;

import java.util.ArrayList;
import java.util.List;

// 选择品牌列表适配器
public class KaolaAIListAdapter extends RecyclerView.Adapter<KaolaAIListAdapter.VHolder> implements IndexAdapter {
    private Context context;
    private List mLists ;

    // 构造方法
    KaolaAIListAdapter(Context context, List  mLists) {
        this.context = context;
        this.mLists = mLists == null ? new ArrayList<>() : mLists;
    }

    public void setDataAndNotify(List data) {
        this.mLists = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public Indexable getItem(int position) {
        return (Indexable) mLists.get(position);
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vh, int i) {
        vh.nameText.setWidth(1000);
        vh.nameText.setText("              " + System.currentTimeMillis() + "-00000000000000000000000000000             ");
    }

    class VHolder extends RecyclerView.ViewHolder {

        TextView nameText;

        VHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView;
        }
    }

}
