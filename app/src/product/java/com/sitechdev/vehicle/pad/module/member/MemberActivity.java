package com.sitechdev.vehicle.pad.module.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sitechdev.net.HttpCode;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.manager.CommonTopWindowManager;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.module.member.bean.PointsSigninBean;
import com.sitechdev.vehicle.pad.view.CommonToast;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
public class MemberActivity extends BaseActivity {

    private ImageView mMemberLogoView = null;
    private TextView mMemberNameView = null;
    private TextView mMemberQDView = null;
    private LinearLayout mMember_MyPointView = null, mMember_FeedbackView = null, mMember_AboutView = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mMemberLogoView = findViewById(R.id.id_img_member_logo);
        mMemberNameView = findViewById(R.id.id_tv_member_name);
        mMemberQDView = findViewById(R.id.id_tv_qd_btn);
        mMember_MyPointView = findViewById(R.id.id_member_my_point);
        mMember_FeedbackView = findViewById(R.id.id_member_feedback);
        mMember_AboutView = findViewById(R.id.id_member_about);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.id_img_back).setOnClickListener(this);
        mMemberLogoView.setOnClickListener(this);
        mMemberNameView.setOnClickListener(this);
        mMemberQDView.setOnClickListener(this);
        mMember_MyPointView.setOnClickListener(this);
        mMember_FeedbackView.setOnClickListener(this);
        mMember_AboutView.setOnClickListener(this);
        findViewById(R.id.id_tv_logout).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        try {
            if (UserManager.getInstance().getLoginUserBean() != null) {
                Glide.with(MemberActivity.this).load(UserManager.getInstance().getLoginUserBean().getAvatarUrl())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(mMemberLogoView);
            } else {
                Glide.with(MemberActivity.this).load(R.drawable.ico_default_member_logo)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(mMemberLogoView);
            }
            mMemberNameView.setText(UserManager.getInstance().getUser().getNickName());
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_img_back:
                finish();
                break;
            case R.id.id_tv_qd_btn:
                //签到
                startSignInAccount();
                break;
            case R.id.id_member_my_point:
                //我的积分
                Intent mIntent = new Intent();
                mIntent.setClass(this, MyPointActivity.class);
                startActivity(mIntent);
                break;
            case R.id.id_member_feedback:
                //意见反馈
                Intent mIntent1 = new Intent();
                mIntent1.setClass(this, FeedbackActivity.class);
                startActivity(mIntent1);
                break;
            case R.id.id_member_about:
                //关于
                Intent mIntent2 = new Intent();
                mIntent2.setClass(this, AboutActivity.class);
                startActivity(mIntent2);
                break;
            case R.id.id_tv_logout:
                //退出登录
                CommonTopWindowManager commonTopWindowManager = CommonTopWindowManager.getInstance();
                commonTopWindowManager.setWindowMessage("是否确认退出登录？");
                commonTopWindowManager.setWindowOkClickListener(v1 -> {
                    requestLogout();
                    commonTopWindowManager.hide();
                });
                commonTopWindowManager.setWindowCancelListener(v1 -> commonTopWindowManager.hide());
                commonTopWindowManager.show();
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void requestLogout() {
        //清除登录数据
        UserManager.getInstance().logoutUser();
        //发出退出登录事件
        EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_LOGIN_LOGOUT));
        //返回首页
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 签到
     */
    private void startSignInAccount() {
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            CommonToast.showToast(getString(R.string.tip_no_net));
            return;
        }
//        showProgressDialog();
//        MemberHttpUtil.requestSignInAccount(new BaseBribery() {
//            @Override
//            public void onSuccess(Object successObj) {
//                ThreadUtils.runOnUIThread(() -> {
//                    cancelProgressDialog();
//                });
//                PointsSigninBean mPointsSignBean = (PointsSigninBean) successObj;
//                if (mPointsSignBean != null) {
//                    UserManager.getInstance().setPointsSigninBean(mPointsSignBean);
//                }
//                if (HttpCode.HTTP_OK.equals(mPointsSignBean.code)) {
//                    if ("-1".equalsIgnoreCase(mPointsSignBean.data.getIntegral())) {
//                        //已签到，签到成功
//                        ThreadUtils.runOnUIThread(() -> {
//                            mMemberQDView.setText("已签到");
//                            CommonToast.showToast(getString(R.string.personal_point_ed_message));
//                        });
//                    } else {
//                        String pt = mPointsSignBean.data.getIntegral();
//                        ThreadUtils.runOnUIThread(() -> {
//                            mMemberQDView.setText("已签到");
//                            CommonToast.showToast(R.drawable.ico_mypoint, getString(R.string.personal_point_sign_success_message, pt));
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Object failObj) {
//                super.onFailure(failObj);
//                ThreadUtils.runOnUIThread(() -> {
//                    cancelProgressDialog();
//                    CommonToast.showToast("请求失败，请稍候重试。");
//                });
//            }
//        });
    }
}
