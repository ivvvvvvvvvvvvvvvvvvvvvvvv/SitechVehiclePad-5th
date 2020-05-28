package com.sitechdev.vehicle.pad.module.member;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.login.bean.util.MemberHttpUtil;
import com.sitechdev.vehicle.pad.module.member.adapter.PointDataItemAdapter;
import com.sitechdev.vehicle.pad.module.member.bean.PointsInfoBean;
import com.sitechdev.vehicle.pad.module.member.bean.TotalPointsBean;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.RecycleViewDivider;
import com.sitechdev.vehicle.pad.view.ReflectTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MemberActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 21:09
 * 修改时间：
 * 备注：
 */
@Route(path = RouterConstants.SUB_APP_MY_POINTS)
public class MyPointActivity extends BaseActivity {
    private RecyclerView mPoiRecycleView = null;
    private ReflectTextView myPointsView = null;
    private PointDataItemAdapter mAdapter = null;
    private TwinklingRefreshLayout refreshLayout = null;
    private int pageIndex = 0;
    private List<PointsInfoBean.PointsDataBean.PointsListBean> mPointsList = new ArrayList<>();
    private TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mypoint;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.tv_sub_title);

        myPointsView = findViewById(R.id.id_tv_sign_count_number);

        refreshLayout = findViewById(R.id.id_points_refresh_layout);

        mPoiRecycleView = findViewById(R.id.id_points_recycle_view);
    }

    @Override
    protected void initListener() {
        super.initListener();

        findViewById(R.id.iv_sub_back).setOnClickListener(v -> {
            finish();
        });
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                SitechDevLog.e(AppConst.TAG, "refreshLayout ============");
                fetchMore(true);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                fetchMore(false);
            }
        });
    }

    @Override
    protected void initData() {
        tvTitle.setText("我的积分");
        //积分数量
        myPointsView.setText(UserManager.getInstance().getLoginUserBean().getPoints());
        //poi数据
        mAdapter = new PointDataItemAdapter(this);
        //设置布局管理器 , 将布局设置成纵向
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPoiRecycleView.setLayoutManager(linerLayoutManager);
        mPoiRecycleView.setItemAnimator(new DefaultItemAnimator());
        mPoiRecycleView.addItemDecoration(new RecycleViewDivider(this,  DensityUtils.dp2px(1),
                getResources().getColor(R.color.color_my_point_recycle_split_line)));
        mPoiRecycleView.setHasFixedSize(true);
        //设置适配器
        mPoiRecycleView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestMyPoint();
        fetchMore(false);
    }

    /**
     * 请求我的积分
     */
    private void requestMyPoint() {
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            CommonToast.showToast(getString(R.string.tip_no_net));
            return;
        }
        showProgressDialog();
        MemberHttpUtil.requestMyPointCount(new BaseBribery() {
            @Override
            public void onSuccess(Object successObj) {
                ThreadUtils.runOnUIThread(() -> {
                    cancelProgressDialog();
                });
                TotalPointsBean pointsBean = (TotalPointsBean) successObj;
                if (pointsBean != null) {
                    String myPoint = pointsBean.getIntegral();
                    ThreadUtils.runOnUIThread(() -> {
                        myPointsView.setText(myPoint);
                    });
                }
            }

            @Override
            public void onFailure(Object failObj) {
                super.onFailure(failObj);
                ThreadUtils.runOnUIThread(() -> {
                    cancelProgressDialog();
                    CommonToast.showToast("积分获取失败，请稍后重试");
                });
            }
        });
    }

    /**
     * 拉取更多数据
     *
     * @param isLoadMore true=上拉加载，pageIndex累加，false=下拉刷新，始终加载第一页
     */
    private void fetchMore(boolean isLoadMore) {

        MemberHttpUtil.requestMyPointList(isLoadMore ? pageIndex : 0, new BaseBribery() {
            @Override
            public void onSuccess(Object successObj) {
                PointsInfoBean pointsInfoBean = (PointsInfoBean) successObj;
                if (pointsInfoBean.data != null) {
                    List<PointsInfoBean.PointsDataBean.PointsListBean> dataPointsList = pointsInfoBean.data.getPointsList();
                    if (dataPointsList != null && !dataPointsList.isEmpty()) {
                        if (isLoadMore) {
                            //上拉加载
                            pageIndex++;
                        } else {
                            //下拉刷新，需要清除原始数据
                            mPointsList.clear();
                            //下拉刷新
                            pageIndex = 1;
                        }
                        mPointsList.addAll(dataPointsList);
                        ThreadUtils.runOnUIThread(() -> {
                            mAdapter.updateData(mPointsList);
                        });
                    }
                }
                ThreadUtils.runOnUIThread(() -> {
                    if (isLoadMore) {
                        //上拉加载
                        refreshLayout.finishLoadmore();
                    } else {
                        //下拉刷新，需要清除原始数据
                        refreshLayout.finishRefreshing();
                    }
                });
            }

            @Override
            public void onFailure(Object failObj) {
                super.onFailure(failObj);
                ThreadUtils.runOnUIThread(() -> {
                    CommonToast.showToast("积分获取失败，请稍后重试");
                    if (isLoadMore) {
                        //上拉加载
                        refreshLayout.finishLoadmore();
                    } else {
                        //下拉刷新，需要清除原始数据
                        refreshLayout.finishRefreshing();
                    }
                });
            }
        });
    }
}
