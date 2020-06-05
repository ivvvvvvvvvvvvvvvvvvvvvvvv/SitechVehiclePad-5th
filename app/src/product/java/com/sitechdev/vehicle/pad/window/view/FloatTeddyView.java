package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.vui.VUI;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;


/**
 * @author DELL
 * @date 2016/11/8
 */
public class FloatTeddyView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = FloatTeddyView.class.getSimpleName();
    private TeddyWindowManager manager;
    private ImageView voiceIconIv;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;  // 快速点击间隔

    public FloatTeddyView(Context context) {
        this(context, null);
    }

    public FloatTeddyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.main_float_view, this);
        voiceIconIv = findViewById(R.id.id_btn_teddy);
        mWidth = voiceIconIv.getLayoutParams().width;
        mHeight = voiceIconIv.getLayoutParams().height;
        manager = TeddyWindowManager.getInstance();

        initView();

        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        voiceIconIv.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_teddy:
                SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>手动点击Teddy启动图标===");
                if (VUI.getInstance().isTeddyWorking()) {
                    SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>Teddy正在工作中===发出SR_OVER事件");
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_OVER));
                } else {
                    SitechDevLog.i(VoiceConstants.TEDDY_TAG, this.getClass().getSimpleName() + "=====>Teddy未在工作，===发出MVW_SUCCESS事件");
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS));
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                preX = (int) event.getRawX();
//                preY = (int) event.getRawY();
//                lastX = (int) event.getRawX();
//                lastY = (int) event.getRawY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                x = (int) event.getRawX();
//                y = (int) event.getRawY();
//                manager.move(this, x - preX, y - preY);
//                preX = x;
//                preY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                if (event.getRawX() - lastX < 10
//                        && event.getRawX() - lastX > -10
//                        && event.getRawY() - lastY < 10
//                        && event.getRawY() - lastY > -10) {
//                    boolean isShowTeddy = TeddyEventHelper.getInstance().isShowTeddyIcon();
//                    DL.d("teddyFloat", "FloatView-onTouchEvent-泰迪是否显示" + isShowTeddy);
//                    if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
//                        break;
//                    }
//                    lastClickTime = System.currentTimeMillis();
//                    TraceManager.getInstance().traceClick(this.getClass(), CommonTraceEnum.TEDDY_NOTIFY.getPoint());
//                    if (DataFactory.produceMemData().getTeddyVoiceData().isPlayTtsWelcomeText) {
//                        DL.i(TeddyConstants.TAG_TEDDY, "正在播放欢迎语");
//                        TeddyUtil.releaseWelcomeFocus();
//                        //正在播放欢迎语
//                        break;
//                    }
//                    //未在工作
//                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_MVW_SUCCESS));
//
//                    TeddyVoiceManager.getInstance().prinTeddyServertLogs();
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}