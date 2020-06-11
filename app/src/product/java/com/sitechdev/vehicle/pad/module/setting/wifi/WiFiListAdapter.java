package com.sitechdev.vehicle.pad.module.setting.wifi;

import android.content.Context;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

import java.util.List;

import static android.net.NetworkInfo.DetailedState.AUTHENTICATING;
import static android.net.NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK;
import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static android.net.NetworkInfo.DetailedState.CONNECTING;
import static android.net.NetworkInfo.DetailedState.DISCONNECTED;
import static android.net.NetworkInfo.DetailedState.FAILED;
import static android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR;
import static android.net.NetworkInfo.DetailedState.SCANNING;
import static android.net.NetworkInfo.DetailedState.VERIFYING_POOR_LINK;

public class WiFiListAdapter extends RecyclerView.Adapter<WiFiListAdapter.ViewHolder> implements View.OnClickListener {

    public static final String TAG = WiFiListAdapter.class.getSimpleName();

    private Context context;
    private List<ScanResult> data;
    private WifiManager wifiManager;
    private NetworkInfo.DetailedState indexState;
    private String indexSSID;
    private int selectPosition = -1;

    public WiFiListAdapter(Context context, List<ScanResult> data, WifiManager wifiManager) {
        this.data = data;
        this.context = context;
        this.wifiManager = wifiManager;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wifi_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.itemView.setSelected(position == selectPosition);
        holder.wifiName.setText(data.get(position).SSID);
        boolean lockType = true;
        String lockString;
        if (data.get(position).capabilities.contains("WPA2-PSK")) {
            lockString = context.getResources().getString(R.string.wifi_protect_wpa2_psk);
        } else if (data.get(position).capabilities.contains("WPA-PSK")) {
            lockString = context.getResources().getString(R.string.wifi_protect_wpa_psk);
        } else if (data.get(position).capabilities.contains("WPA-EAP")) {
            lockString = context.getResources().getString(R.string.wifi_protect_wpa_eap);
        } else if (data.get(position).capabilities.contains("WEP")) {
            lockString = context.getResources().getString(R.string.wifi_protect_wep);
        } else {
            lockString = context.getResources().getString(R.string.wifi_protect_no);
            lockType = false;
        }
        setSaveState(data.get(position), holder.connectState, lockString, holder.wifiName,
                holder.iv_gou, data.get(position).SSID);
        setLevel(data.get(position), holder.rssi, holder.iv_lock, lockType);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setSaveState(ScanResult scanResult, TextView textView,
                              String lockString, TextView tvTwo,
                              ImageView ivGou, String wifiName) {
        if (indexSSID == null) {
            return;
        }
        if (indexSSID.equals("\"" + scanResult.SSID + "\"")) {
            if (indexState == SCANNING || indexState == CONNECTING) {
                textView.setText(context.getResources().getString(R.string.wifi_connecting));
            } else if (indexState == AUTHENTICATING) {
                textView.setText(context.getResources().getString(R.string.wifi_authenticating));
            } else if (indexState == OBTAINING_IPADDR || indexState == VERIFYING_POOR_LINK
                    || indexState == CAPTIVE_PORTAL_CHECK) {
                textView.setText(context.getResources().getString(R.string.wifi_obtaining_IPADDR));
            } else if (indexState == CONNECTED) {
                textView.setText(context.getResources().getString(R.string.wifi_connected));
                tvTwo.setTextAppearance(context, R.style.wifi_item_text);
                textView.setTextAppearance(context, R.style.wifi_item_text);
                //添加
//                tvOne.setText("断开");
                ivGou.setVisibility(View.VISIBLE);
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.transData(wifiName, indexState);
                }
                //******************
                wifiManager.saveConfiguration();
            } else if (indexState == DISCONNECTED) {
//                tvOne.setText("连接");
                tvTwo.setTextColor(Color.parseColor("#b3ffffff"));
                textView.setTextColor(Color.parseColor("#b3ffffff"));
                ivGou.setVisibility(View.GONE);
                //添加
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.transData("", indexState);
                }
                //*****************
                if (isExists(scanResult.SSID) != null) {
                    textView.setText(context.getResources().getString(R.string.wifi_saved));
                } else {
                    textView.setText(lockString);
                }
            } else if (indexState == FAILED) {
            }
        } else {
//            tvOne.setText("连接");
            tvTwo.setTextColor(Color.parseColor("#b3ffffff"));
            textView.setTextColor(Color.parseColor("#b3ffffff"));
            ivGou.setVisibility(View.GONE);
            if (isExists(scanResult.SSID) != null) {
                textView.setText(context.getResources().getString(R.string.wifi_saved));
            } else {
                textView.setText(lockString);
            }
        }
    }


    private void setLevel(ScanResult scanResult, ImageView imageView, ImageView lockIv,
                          boolean isLock) {
        if (data != null) {
            int strength = WifiManager.calculateSignalLevel(scanResult.level, 4);
            if (strength == 3) {//1 强
                imageView.setBackgroundResource(R.drawable.set_wifi_unlock_4);
                if (isLock) {
                    lockIv.setVisibility(View.VISIBLE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_lock_4);

                } else {
                    lockIv.setVisibility(View.GONE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_unlock_4);
                }
            } else if (strength == 2) {//2较好
                imageView.setBackgroundResource(R.drawable.set_wifi_unlock_3);
                if (isLock) {
                    lockIv.setVisibility(View.VISIBLE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_lock_3);
                } else {
                    lockIv.setVisibility(View.GONE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_unlock_3);
                }
            } else if (strength == 1) {//3一般
                imageView.setBackgroundResource(R.drawable.set_wifi_unlock_2);
                if (isLock) {
                    lockIv.setVisibility(View.VISIBLE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_lock_2);
                } else {
                    lockIv.setVisibility(View.GONE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_unlock_2);
                }
            } else if (strength == 0) {//4较差
                imageView.setBackgroundResource(R.drawable.set_wifi_unlock_1);
                if (isLock) {
                    lockIv.setVisibility(View.VISIBLE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_lock_1);
                } else {
                    lockIv.setVisibility(View.GONE);
//                    imageView.setBackgroundResource(R.drawable.set_wifi_unlock_1);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (onRecyclerViewItemClickListener != null) {
            //添加 indexState
            onRecyclerViewItemClickListener.onItemClick(view, (Integer) view.getTag(), indexState);
        }
    }


    //网络状态变化时刷新列表
    public void refresh(String indexSSID, NetworkInfo.DetailedState indexState) {
        Log.d(TAG, "refresh: indexSSID  " + indexSSID + "indexState  " + indexState);
        this.indexSSID = indexSSID;
        this.indexState = indexState;
        onTopNotifyDataSetChanged();
    }

    //使状态变化的wifi排在第一行
    public void onTopNotifyDataSetChanged() {
        if (data != null && indexSSID != null) {
            for (int i = 0; i < data.size(); i++) {
                if (indexSSID.length() - 1 > 1 && indexSSID.substring(1, indexSSID.length() - 1).equals(data.get(i).SSID)) {
                    if (i == 0) {
                        break;
                    }
                    ScanResult scanResult = data.get(i);
                    data.set(i, data.get(0));
                    data.set(0, scanResult);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    //查看以前是否也配置过这个网络
    private WifiConfiguration isExists(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs == null) {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public void setSelectedPosition(int position) {
        Log.d(TAG, "setSelectedPosition: position " + position);
        selectPosition = position;
        notifyDataSetChanged();
    }

    //添加 NetworkInfo.DetailedState indexState
    public void performClick(int position, NetworkInfo.DetailedState indexState) {
        if (onRecyclerViewItemClickListener != null) {
            //添加 indexState
            onRecyclerViewItemClickListener.onItemClick(null, position, indexState);
        }
    }

    public List<ScanResult> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wifiName;
        private TextView connectState;
        private ImageView rssi;

        //添加
//        private TextView tv_on_off_connect;
        private ImageView iv_gou, iv_lock;
        //**********

        public ViewHolder(View itemView) {
            super(itemView);
            wifiName = itemView.findViewById(R.id.wifi_item_name);
            connectState = itemView.findViewById(R.id.item_wifi_connect_state_tv);
            rssi = itemView.findViewById(R.id.wifi_item_signal_strength_img);

            //添加
//            tv_on_off_connect = itemView.findViewById(R.id.tv_on_off_connect);
            iv_gou = itemView.findViewById(R.id.iv_gou);
            iv_lock = itemView.findViewById(R.id.iv_lock);
            //**********
        }
    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        //添加 NetworkInfo.DetailedState indexState
        void onItemClick(View view, int position, NetworkInfo.DetailedState indexState);

        //添加
        void transData(String wifiName, NetworkInfo.DetailedState indexState);
        //*********
    }

}
