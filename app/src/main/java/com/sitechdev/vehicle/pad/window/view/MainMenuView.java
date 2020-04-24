package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.manager.MainPopUpControlWindowManager;

public class MainMenuView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = MainMenuView.class.getSimpleName();

    private ImageView mTeddyView, mHomeBtnImageView, mNaviBtnImageView, mMusicBtnImageView, mDriverBtnImageView, mAppsBtnImageView;

    public int mWidth;
    public int mHeight;
    //Teddy展示区域
    private RelativeLayout mTeddyContentView;

    private int reflectionValue = 30;


    public MainMenuView(Context context) {
        this(context, null);
    }

    public MainMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充布局，并添加至
        LayoutInflater.from(context).inflate(R.layout.main_menu_view, this);
        View view = findViewById(R.id.id_main_menu_view);

        mHomeBtnImageView = (ImageView) findViewById(R.id.id_btn_home);
        mNaviBtnImageView = (ImageView) findViewById(R.id.id_btn_location);
        mMusicBtnImageView = (ImageView) findViewById(R.id.id_btn_music);
        mDriverBtnImageView = (ImageView) findViewById(R.id.id_btn_driver);
        mAppsBtnImageView = (ImageView) findViewById(R.id.id_btn_apps);

        mWidth = view.getLayoutParams().width;
        mHeight = view.getLayoutParams().height;

        mTeddyView = findViewById(R.id.id_btn_teddy);

        mTeddyContentView = findViewById(R.id.id_Teddy_Content);

        initView();

        initListener();
    }

    private void initView() {
        mHomeBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_home), reflectionValue, true));
        mNaviBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_navi), reflectionValue, true));
        mMusicBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_music), reflectionValue, true));
        mDriverBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_driver), reflectionValue, true));
        mAppsBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_apps), reflectionValue, true));

//        mTeddyView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.iv_teddy), reflectionValue * 3, true));
    }

    private void initListener() {
        mTeddyView.setOnClickListener(this);
        mHomeBtnImageView.setOnClickListener(this);
        mNaviBtnImageView.setOnClickListener(this);
        mMusicBtnImageView.setOnClickListener(this);
        mDriverBtnImageView.setOnClickListener(this);
        mAppsBtnImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_home:
                ToastUtils.showShort("主页按钮被点击了。。。");
                break;
            case R.id.id_btn_location:
                ToastUtils.showShort("导航按钮被点击了。。。");
                break;
            case R.id.id_btn_music:
                ToastUtils.showShort("音乐按钮被点击了。。。");
                break;
            case R.id.id_btn_driver:
                ToastUtils.showShort("驾驶按钮被点击了。。。");
                break;
            case R.id.id_btn_apps:
                ToastUtils.showShort("应用按钮被点击了。。。");
                break;
            case R.id.id_btn_teddy:
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS));
                break;
            default:
                break;
        }
    }
}
