package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.window.manager.MainPopUpControlWindowManager;

public class MainPopupControlView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = MainPopupControlView.class.getSimpleName();
    private MainPopUpControlWindowManager manager = null;

    public int mWidth;
    public int mHeight;

    private int preX;
    private int preY;
    private int x;
    private int y;

    int lastX, lastY;


    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;  // 快速点击间隔

    private ImageView popControlView = null;

    private boolean isPullDownView = false, isPullUp = false, isPushDown = false;


    public MainPopupControlView(Context context) {
        this(context, null);
    }

    public MainPopupControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.custom_popup_view, this);
        View view = findViewById(R.id.id_main_popup_view);

        mWidth = view.getLayoutParams().width;
        mHeight = view.getLayoutParams().height;

        popControlView = findViewById(R.id.id_top_image);

        manager = MainPopUpControlWindowManager.getInstance();

        initView();

        initListener();
    }

    private void initView() {
    }

    private void initListener() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) event.getRawX();
                preY = (int) event.getRawY();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                if (isInClickView(popControlView, event)) {
                    //点击区域
                    isPullDownView = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                if (isPullDownView) {
                    manager.move(this, x - preX, y - preY);
                }
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                isPullDownView = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断触摸的点是否在EditText范围内
     */
    private boolean isInClickView(View v, MotionEvent event) {
        Rect frame = new Rect();
        v.getHitRect(frame);
        float eventX = event.getX();
        float eventY = event.getY();
        return frame.contains((int) eventX, (int) eventY);
    }
}
