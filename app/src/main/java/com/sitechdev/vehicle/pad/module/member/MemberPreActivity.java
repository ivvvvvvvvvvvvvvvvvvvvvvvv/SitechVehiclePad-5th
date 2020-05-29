package com.sitechdev.vehicle.pad.module.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.module.member.bean.TotalPointsBean;
import com.sitechdev.vehicle.pad.module.member.util.MemberHttpUtil;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.view.ReflectTextView;
import com.sitechdev.vehicle.pad.window.view.CommonLogoutDialog;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SUB_APP_MEMBER)
public class MemberPreActivity extends BaseActivity {
    private static final String TAG = "MemberPreActivity";

    private TextView tvTitle;
    private ImageView mUserIconView = null;
    private TextView mUserNameTextView = null, mUserDescView = null,
    /**
     * 积分兑换
     */
    mMySignChangeRelaLayoutView = null;
    private ReflectTextView mUserSignView = null;

    private RelativeLayout
            /**
             * 签到
             */
            mSignBtnRelaLayoutView = null,
    /**
     * 我的积分
     */
    mMySignRelaLayoutView = null,
    /**
     * 退出登录
     */
    mLogoutLayoutView = null;
    private TotalPointsBean mTotalPointsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.tv_sub_title);

        mUserIconView = findViewById(R.id.id_img_member_logo);
        mUserNameTextView = findViewById(R.id.id_tv_member_name);
        //
        mUserDescView = findViewById(R.id.id_tv_member_desc);

        //积分数量
        mUserSignView = findViewById(R.id.id_tv_sign_count_number);

        //签到
        mSignBtnRelaLayoutView = findViewById(R.id.id_sign_btn);
        //积分兑换
        mMySignChangeRelaLayoutView = findViewById(R.id.id_tv_sign_change);
        //我的积分
        mMySignRelaLayoutView = findViewById(R.id.id_sign_count_top_content);
        //退出登录
        mLogoutLayoutView = findViewById(R.id.id_logout);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        mMySignChangeRelaLayoutView.setOnClickListener(this);
        //签到
        mSignBtnRelaLayoutView.setOnClickListener(this);
        //积分数量
        mUserSignView.setOnClickListener(this);
        //我的积分
        mMySignRelaLayoutView.setOnClickListener(this);
        //退出登录
        mLogoutLayoutView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("个人中心");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
        if (LoginUtils.isLogin()) {
            //TODO 为了发布会版本做的判断处理。发布会版本暂未接入登录功能。待发布会同步登录功能后，此处判断会去掉
            //请求积分
            requestPoints();
        }
    }

    /**
     * 刷新view
     */
    private void refreshView() {
        //积分数量
        if (LoginUtils.isLogin()) {
            // 图片
            Glide.with(this).load(UserManager.getInstance().getLoginUserBean().getAvatarUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(mUserIconView);
            // 昵称
            mUserNameTextView.setText(String.format("Hi，%s", UserManager.getInstance().getLoginUserBean().getNickName()));
            // 描述
            mUserDescView.setText(UserManager.getInstance().getLoginUserBean().getJob());
            mUserSignView.setText(UserManager.getInstance().getLoginUserBean().getPoints());
        } else {
            //TODO 为了发布会版本做的判断处理。发布会版本暂未接入登录功能。待发布会同步登录功能后，此处判断会去掉
            mUserSignView.setText("1208");
        }
    }

    /**
     * 请求积分
     */
    private void requestPoints() {
//        showProgressDialog();
        MemberHttpUtil.requestUserPoints(new BaseBribery() {
            @Override
            public void onSuccess(Object successObj) {
                if (null == successObj) {
                    SitechDevLog.d(TAG, "onSuccess: this message is null totalpointsbean");
                    return;
                }
                mTotalPointsBean = (TotalPointsBean) successObj;
                UserManager.getInstance().getLoginUserBean().setPoints(mTotalPointsBean.getIntegral());
                runOnUiThread(() -> {
//                    cancelProgressDialog();
                    mUserSignView.setText(mTotalPointsBean.getIntegral());
                });
            }

            @Override
            public void onFailure(Object failObj) {
                super.onFailure(failObj);
//                runOnUiThread(() -> {
//                    cancelProgressDialog();
//                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            //积分兑换
            case R.id.id_tv_sign_change:
//                EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_CHANGE_SKIN, SkinModel.SKIN_WHITE_ORANGE));
//                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_TAXI);
                break;
            //签到
            case R.id.id_sign_btn:
                break;
            case R.id.id_tv_sign_count_number:
                //我的积分
            case R.id.id_sign_count_top_content:
                if (!LoginUtils.isLogin()) {
                    return;
                }
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_MY_POINTS);
                break;
            //退出登录
            case R.id.id_logout:
                if (!LoginUtils.isLogin()) {
                    return;
                }
                CommonLogoutDialog logoutDialog = new CommonLogoutDialog(this);
                logoutDialog.setListener(() -> {
                    //确定按钮被点击
                    UserManager.getInstance().logoutUser();
                    RouterUtils.getInstance().navigationWithFlags(RouterConstants.HOME_MAIN,
                            Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );
                });
                logoutDialog.show();
                break;
            default:
                break;
        }
    }
}
