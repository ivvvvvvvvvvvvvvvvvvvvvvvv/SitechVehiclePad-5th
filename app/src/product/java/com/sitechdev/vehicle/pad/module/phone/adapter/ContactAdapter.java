package com.sitechdev.vehicle.pad.module.phone.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = ContactAdapter.class.getSimpleName();


    private LayoutInflater mLayoutInflater;
    private Context mContext;
    //private String[] mContactNames; // 联系人信息字符串数组
    //private List<String> mContactList; // 联系人名称List（转换成拼音）
    //private List<Contact> resultList; // 最终结果（包含分组的字母）
    //private List<String> mCharacterList; // 字母List

    private List<Contact> pbList = new ArrayList<>();
    private int selectedPos = -1;
    private OnItemClickListener onItemClickListener;

    public enum ITEM_TYPE {
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }

    /*public ContactAdapter(Context context, String[] contactNames) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        //mContactNames = contactNames;

        //handleContact();
    }*/

    public ContactAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };

    public void updateList(ArrayList<Contact> list) {
        ThreadManager.getInstance().addTask(() -> {
            pbList.clear();
            if (list.isEmpty()) {
                selectedPos = 0;
            } else {
                pbList.addAll(list);
                if (selectedPos >= pbList.size()) {
                    selectedPos = pbList.size() - 1;
                }
            }
            handler.sendEmptyMessage(0);
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_character, parent,
                    false));
        } else {
            return new ContactHolder(mLayoutInflater.inflate(R.layout.item_phonebook, parent,
                    false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact contact = pbList.get(position);
        if (holder instanceof CharacterHolder) {
            if (position == 0) {
                ((CharacterHolder) holder).mLineView.setVisibility(View.GONE);
            } else {
                ((CharacterHolder) holder).mLineView.setVisibility(View.VISIBLE);
            }
            ((CharacterHolder) holder).mTextView.setText(contact.getName());
        } else if (holder instanceof ContactHolder) {
            ((ContactHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(position);
                    }
                }
            });
            ((ContactHolder) holder).itemView.setSelected(position == selectedPos);
            ((ContactHolder) holder).mTextView.setText(contact.getName());
            ((ContactHolder) holder).tvContactPhoneNum.setText(contact.getPhoneNumber());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return pbList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return pbList == null ? 0 : pbList.size();
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        View mLineView;

        CharacterHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.character);
            mLineView = view.findViewById(R.id.view_contact_line);
        }
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private TextView tvContactPhoneNum;

        ContactHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.tv_contact_name);
            tvContactPhoneNum = view.findViewById(R.id.tv_contact_phonenum);
        }
    }

    public int getScrollPosition(String character) {
        if (BtGlobalRef.characterSorts != null) {
            if (BtGlobalRef.characterSorts.contains(character)) {
                for (int i = 0; i < pbList.size(); i++) {
                    if (pbList.get(i).getName().equals(character)) {
                        return i;
                    }
                }
            }
        }

        return -1; // -1不会滑动
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

    public List<Contact> getData() {
        return pbList;
    }
}

