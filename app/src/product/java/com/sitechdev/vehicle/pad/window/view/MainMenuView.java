package com.sitechdev.vehicle.pad.window.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.manager.MapManager;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;

public class MainMenuView extends RelativeLayout implements View.OnClickListener {

    private final String TAG = MainMenuView.class.getSimpleName();

    private ImageView mHomeBtnImageView, mNaviBtnImageView, mMusicBtnImageView, mDriverBtnImageView, mAppsBtnImageView;

    public int mWidth;
    public int mHeight;
//
    private int reflectionValue = 30;


    public MainMenuView(Context context) {
        this(context, null);
    }

    @Override
    public Resources getResources() {
        return AppUtil.getCurrentResource(ActivityUtils.getTopActivity().getResources());
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

        initView();

        initListener();
    }

    private void initView() {
        mHomeBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_home), reflectionValue, true));
        mNaviBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_navi), reflectionValue, true));
        mMusicBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_music), reflectionValue, true));
        mDriverBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_driver), reflectionValue, true));
        mAppsBtnImageView.setImageBitmap(ImageUtils.addReflection(ImageUtils.getBitmap(R.drawable.ico_btn_apps), reflectionValue, true));
    }

    private void initListener() {
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
                //主页按钮
                RouterUtils.getInstance().navigationHomePage(RouterConstants.HOME_MAIN);
                break;
            case R.id.id_btn_location:
//                JumpUtils.jumpFromActivity(ActivityUtils.getTopActivity(), RouterConstants.THIRD_APP_MAP);
                MapManager.getInstance().startMap();
                break;
            case R.id.id_btn_music:
                //音乐按钮
                RouterUtils.getInstance().navigation(RouterConstants.FRAGMENT_LOCAL_MUSIC);
                break;
            case R.id.id_btn_driver:
                //驾驶按钮
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_CAR_STATUS);
                break;
            case R.id.id_btn_apps:
                //应用按钮
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_APP_LIST);
                break;
            default:
                break;
        }
    }
}
