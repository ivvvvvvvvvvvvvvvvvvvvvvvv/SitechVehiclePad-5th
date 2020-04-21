package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhubaoqiang
 * @date 2019/8/22
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;

    private List<Contact> contacts;

    public ContactAdapter(Context context) {
        this.context = context;
        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_contacts_holder, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean validData = position < contacts.size() && null != contacts.get(position);
        if (!validData) {
            return;
        }
        Contact contact = contacts.get(position);
        holder.getName().setText(contact.getName());
        holder.getPhoneNumber().setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addDatas(JSONArray array){
        contacts.clear();
        notifyDataSetChanged();
        int len = array.length();
        for (int i = 0; i < len; i++){
            JSONObject contact = array.optJSONObject(i);
            if (null != contact){
                Contact item = new Contact();
                item.setName(contact.optString("name"));
                item.setPhoneNumber(contact.optString("phoneNumber"));
                contacts.add(item);
                notifyItemInserted(i);
            }
        }
    }

    public List<Contact> getContacts(){
        return contacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView vName;
        private TextView vPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.item_contacts_name);
            vPhoneNumber = itemView.findViewById(R.id.item_contacts_phone_number);
        }

        public TextView getName() {
            return vName;
        }

        public TextView getPhoneNumber() {
            return vPhoneNumber;
        }
    }

    public static class Contact{
        private String name;
        private String phoneNumber;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "Contacts{" +
                    "name='" + name + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }
}
