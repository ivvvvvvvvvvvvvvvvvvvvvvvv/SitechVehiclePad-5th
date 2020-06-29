package com.sitechdev.vehicle.pad.module.phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.phone.Contact;

import java.util.ArrayList;
import java.util.List;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.ViewHolder> {

    private Context context;
    private List<Contact> pbList = new ArrayList<>();
    private int selectedPos = -1;
    private String changeStr = "";
    private OnItemClickListener onItemClickListener;

    public void setChangeStr(String changeStr) {
        this.changeStr = changeStr;
        //notifyDataSetChanged();
    }

    public SearchBookAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<Contact> list) {
        this.pbList.clear();
        if (list.isEmpty()) {
            selectedPos = 0;
        } else {
            this.pbList.addAll(list);
            if (selectedPos >= list.size()) {
                selectedPos = list.size() - 1;
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pbList.size();
    }

    public List<Contact> getData() {
        return pbList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_phonebook, parent, false);
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
        holder.itemView.setSelected(position == selectedPos);
        Contact contact = pbList.get(position);
        String phone = contact.getName().replace(" ", "").replace("+86", "");
        holder.tv_name.setText(phone);
        holder.tv_phone_num.setText(contact.getPhoneNumber());

        if (null != contact && contact.getPhoneNumber().contains(changeStr)) {
            int index = contact.getPhoneNumber().indexOf(changeStr);
            int len = changeStr.length();
            Spanned temp = Html.fromHtml(contact.getPhoneNumber().substring(0, index)
                    + "<font color=#1A4FC3>"
                    + contact.getPhoneNumber().substring(index, index + len) + "</font>"
                    + contact.getPhoneNumber().substring(index + len));
            holder.tv_phone_num.setText(temp);
        } else {
            holder.tv_phone_num.setText(contact.getPhoneNumber());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_phone_num;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone_num = itemView.findViewById(R.id.tv_phone_num);
        }
    }

    public int getCount() {
        return pbList == null ? 0 : pbList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public void setSelected(int position) {
        this.selectedPos = position;
        this.notifyDataSetChanged();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForInitial(char section) {
        for (int i = 0; i < pbList.size(); i++) {
            if (pbList.get(i).getAbbr().charAt(0) == section) {
                return i;
            }
        }
        return -1;
    }
}