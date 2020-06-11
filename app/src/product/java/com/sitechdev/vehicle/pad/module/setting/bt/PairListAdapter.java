package com.sitechdev.vehicle.pad.module.setting.bt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.sitechdev.vehicle.lib.util.ForbidClickEnable;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.manager.trace.TraceManager;
import com.sitechdev.vehicle.pad.model.setting.utils.SettingConfig;
import com.sitechdev.vehicle.pad.view.dialog.DialogWrapper;
import com.sitechdev.vehicle.pad.view.dialog.DialogWrapperFatory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PairListAdapter extends BaseAdapter {

    private Context context;
    private int selectedPos = -1;
    private List<BtDeviceItem> deviceList = new ArrayList<>();

    private boolean isKnobSelected = false;
    private boolean lightBg = false;
    private LayoutInflater layoutInflater;
    private OnClickListener callback;
    private ExecutorService mEs;
    private String deviceAddr;
    private ConcreteHanler hanler;
    private Future mFuture;

    public PairListAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        mEs = Executors.newSingleThreadExecutor();
        hanler = new ConcreteHanler(this);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < deviceList.size()) {
            return deviceList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pairlist, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BtDeviceItem device = (BtDeviceItem) getItem(position);
        if (device != null) {
            viewHolder.tvDevName.setText(device.getDevice().getName());
            final boolean isConnected = device.getDevice().getAddress().equals(SettingConfig.getInstance().getConnectBtAdd());
            ctrlIndex = 0;
            clickableViews[0] = viewHolder.tvConnDisc;
            clickableViews[1] = viewHolder.ivDelete;
            if (lightBg) {
            }
            if (isConnected) {
                viewHolder.tvConnDisc.setText(R.string.bt_pair_list_disc);
            } else {
                if (!TextUtils.equals(deviceAddr, device.getDevice().getAddress())) {
                    viewHolder.tvConnDisc.setText(R.string.bt_pair_list_conn);
                } else {
                    viewHolder.tvConnDisc.setText("正在连接");
                }
            }
//            viewHolder.tvConnDisc.setText(isConnected ? R.string.bt_pair_list_disc : R.string.bt_pair_list_conn);
//            viewHolder.tvConnDisc.setBackgroundResource(R.drawable.selector_common_btn_gray);
            viewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ForbidClickEnable.isForbidClick(700)) {
                        return;
                    }
                    if (isConnected) {
                        if (callback != null) {
                            callback.onDisconnect();
                        }
                    } else {
                        if (callback != null) {
                            if (TextUtils.equals(deviceAddr, device.getDevice().getAddress())) {
                                return;
                            }
                            deviceAddr = device.getDevice().getAddress();
                            CallableImpl call = new CallableImpl();
                            if (null != mFuture && !mFuture.isDone()) {
                                mFuture.cancel(true);
                            }
                            mFuture = mEs.submit(call);
                            callback.onConnect(position);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ForbidClickEnable.isForbidClick(700)) {
                        return;
                    }
                    if (callback != null) {
                        callback.onDelete(position);
                    }
                }
            });
        }
        return convertView;
    }

    public void notifList(List<BtDeviceItem> list) {
        deviceList = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private View convertView;
        private TextView tvDevName;
        private TextView tvConnDisc;
        private ImageView ivDelete;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
            tvDevName = convertView.findViewById(R.id.tv_dev_name);
            tvConnDisc = convertView.findViewById(R.id.tv_conn_disc);
            ivDelete = convertView.findViewById(R.id.iv_delete);
        }
    }

    public void setSelected(int position) {
        isKnobSelected = false;
        lightBg = false;
        this.selectedPos = position;
        this.notifyDataSetChanged();
    }

    private View[] clickableViews = new View[2];
    private int ctrlIndex = 0;

    public void updateList(ArrayList<BtDeviceItem> list) {
        updateList(list, false);
    }

    public void updateList(ArrayList<BtDeviceItem> list, boolean isConnect) {
        this.deviceList.clear();
        isKnobSelected = false;
        if (clickableViews[ctrlIndex] != null) {
            clickableViews[ctrlIndex].setSelected(false);
            ctrlIndex = 0;
        }
        if (list.isEmpty()) {
            selectedPos = 0;
        } else {
            this.deviceList.addAll(list);
            if (selectedPos >= list.size()) {
                selectedPos = list.size() - 1;
            }
        }
        if (isConnect) {
            timeOut();
        } else {
            this.notifyDataSetChanged();
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.callback = listener;
    }

    public interface OnClickListener {
        void onDelete(int pos);

        void onConnect(int pos);

        void onDisconnect();
    }

    public void removeCallBacks() {
        this.callback = null;
    }

    private class CallableImpl implements Callable {
        @Override
        public Object call() throws Exception {
            Thread.sleep(20000);
            hanler.sendEmptyMessage(0);
            return null;
        }
    }

    public void timeOut() {
        deviceAddr = null;
        if (null != mFuture) {
            mFuture.cancel(true);
        }
        notifyDataSetChanged();
    }

    public void showDialog() {
        DialogWrapper dialogWrapper =
                DialogWrapperFatory.forBliuetoothTimeOut(context);
        dialogWrapper.show();
    }

    public void release() {
        if (null != mFuture && !mFuture.isDone()) {
            mFuture.cancel(true);
        }
    }

    private static class ConcreteHanler extends Handler {
        private WeakReference<PairListAdapter> mAdapter;

        public ConcreteHanler(PairListAdapter adapter) {
            mAdapter = new WeakReference<PairListAdapter>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (null != mAdapter && null != mAdapter.get()) {
                        mAdapter.get().timeOut();
                        mAdapter.get().showDialog();
                    }
                    break;
            }
        }
    }
}