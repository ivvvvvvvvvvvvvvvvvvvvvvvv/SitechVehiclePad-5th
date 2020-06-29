package com.sitechdev.vehicle.pad.module.phone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.FormatUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.phone.CallLog;

import java.util.ArrayList;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private static final String TAG = CallLogAdapter.class.getSimpleName();

    private Context context;
    private List<CallLog> data = new ArrayList<>();

    public CallLogAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_call_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(position);
                }
            }
        });
        holder.nameTxt.setText("未知".equals(data.get(position).getName()) ? data.get(position).getPhoneNumber() : data.get(position).getName());
        holder.phoneNumTxt.setText(FormatUtils.getInstance().formatCallTime(data.get(position).getDate(), data.get(position).getTime()));
        if (data.get(position).getType() == 6) {
            holder.nameTxt.setTextColor(Color.RED);
            holder.phoneNumTxt.setTextColor(Color.RED);
        } else {
            holder.nameTxt.setTextColor(Color.WHITE);
            holder.phoneNumTxt.setTextColor(Color.WHITE);
        }
        int resId = getTypeRes(data.get(position).getType());
        if (resId != 0) {
            holder.typeImg.setImageResource(resId);
        }
    }

    private static int getTypeRes(int type) {
        switch (type) {
            case 4:
                return R.drawable.call_type_dialed;
            case 5:
                return R.drawable.call_type_received;
            case 6:
                return R.drawable.call_type_missed;
            default:
                break;
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView typeImg;
        TextView nameTxt;
        TextView phoneNumTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            typeImg = itemView.findViewById(R.id.img_type);
            nameTxt = itemView.findViewById(R.id.tv_name);
            phoneNumTxt = itemView.findViewById(R.id.tv_phonenum);
        }
    }

    public void updateList(ArrayList<CallLog> list) {
        this.data.clear();
        this.data.addAll(list);
        this.notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public List<CallLog> getData() {
        return data;
    }
}
