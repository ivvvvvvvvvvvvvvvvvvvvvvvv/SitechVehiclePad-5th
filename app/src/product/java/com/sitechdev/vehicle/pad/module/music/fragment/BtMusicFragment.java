package com.sitechdev.vehicle.pad.module.music.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.hw.BluetoothEvent;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.module.music.BtMusicManager;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManagers;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.MediaScanister;
import com.sitechdev.vehicle.pad.view.CustomPlaySeekBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 蓝牙音乐
 */

@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_BT)
@BindEventBus
public class BtMusicFragment extends BaseFragment {

    private Context context;

    private View stView;

    private TextView musicName, singer, btTip1, connectBtn;
    private ImageView musicIcon;
    private CustomPlaySeekBar customPlaySeekBar;

    public BtMusicFragment() {

    }

    public static BtMusicFragment newInstance() {
        BtMusicFragment fragment = new BtMusicFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData() {
        super.initData();
        BtMusicManager.getInstance().getInfo();
        BtMusicManager.getInstance().btCtrlRequestStatus();
        AppApplication.getAudioManager().requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 0, 2);
    }

    @Override
    protected int getLayoutId() {
        return isLandscape() ? R.layout.fragment_bt_music_land : R.layout.fragment_bt_music;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        View root = mContentView;
        customPlaySeekBar = root.findViewById(R.id.music_usb_play_ctr);
        stView = root.findViewById(R.id.bt_connect_st_view);

        musicName = root.findViewById(R.id.music_usb_play_name);
        singer = root.findViewById(R.id.music_usb_play_singer);
        musicIcon = root.findViewById(R.id.music_usb_play_icon);

        btTip1 = root.findViewById(R.id.music_bt_tip);
        connectBtn = root.findViewById(R.id.button_music_bt);

        customPlaySeekBar.setSeekBarVisible(false);
        customPlaySeekBar.setModeVisible(false);
        refreshConnectSt();
    }

    private void refreshConnectSt(){
        if (SettingConfig.getInstance().isBtConnected()) {
            connectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BtManagers.getInstance().disConnectToDevice();
                    refreshConnectSt();
                }
            });
            btTip1.setText(SettingConfig.getInstance().getConnectBtName());
            connectBtn.setText("断开连接");
        } else {
            connectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouterUtils.getInstance().navigation(RouterConstants.SETTING_BT_PAGE);
                }
            });
            btTip1.setText("请连接蓝牙设备");
            connectBtn.setText("连接蓝牙");
        }
    }

    private void refresh() {
        if (!MediaScanister.getInstance().isScaing()) {
            MediaScanister.getInstance().scan(context,
                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "audio/*",
                    new MediaScanister.OnScanCompleteListener() {
                        @Override
                        public void onScanComplete() {
                        }
                    });
        } else {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BluetoothEvent event) {
        if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_TITLE)) {
            musicName.setText((String) event.getObject());
        } else if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_ART)) {
            singer.setText((String) event.getObject());
        } else if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_ON)) {
            customPlaySeekBar.setCurrentStatusPlaying(true);
        } else if (event.getTag().equals(BluetoothEvent.BT_EVENT_RECEIVE_PLAY_OFF)) {
            customPlaySeekBar.setCurrentStatusPlaying(false);
        }
    }

}
