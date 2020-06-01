package com.sitechdev.vehicle.pad.module.feedback;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.model.feedback.FeedbackHistoryBean;
import com.sitechdev.vehicle.pad.module.feedback.utils.FeedBackHttpUtils;
import com.sitechdev.vehicle.pad.view.loading.LoadingView;

import java.util.ArrayList;
import java.util.List;

public class FeedbackHistoryActivity extends BaseActivity {

    private String TAG = "FeedBackHistoryFragment";
    private TextView mTitleView;
    private View mBack;
    private View mAllView;
    private RecyclerView mRecyclerView;
    private TextView mErrorTv;
    private LinearLayout mErroView;
    private LoadingView mLoading;
    private FeedbackHistoryAdapter mAdapter;
    private List<FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean> mList;
    private TwinklingRefreshLayout mRefreshLayout;
    private TextView mFooterTextView;
    private ImageView mFooterImageView;
    private ProgressBar mFooterProgressBar;
    private ProgressBar mHeaderProgressBar;
    private ImageView mHeaderImgView;
    private TextView mHeaderTxtView;
    private String mEndChecker;
    private View mEmptyView;
    private int pageNo = 1;
    public static final String ERROR_MSG_NET = "网络不稳定，请稍后重试";
    public static final String ERROR_MSG_ERROR = "数据走丢了，请稍后重试";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback_history;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTitleView = findViewById(R.id.tv_sub_title);
        mBack = findViewById(R.id.iv_sub_back);
        mLoading = findViewById(R.id.feedback_history_loading);
        mRefreshLayout = findViewById(R.id.feedback_history_refresh);
        mErrorTv = findViewById(R.id.feedback_history_erro_noweb_tv);
        mErroView = findViewById(R.id.feedback_history_erro_layout);
        mRecyclerView = findViewById(R.id.feedback_history_recyclerview);
        mEmptyView = findViewById(R.id.feedback_history_empty);
        mAllView = findViewById(R.id.feedback_history_all);
        findViewById(R.id.iv_sub_back).setOnClickListener(this);

        mLoading.setVisibility(View.VISIBLE);
        mLoading.setLoadingText("加载中...");
        mAllView.setVisibility(View.GONE);
        mTitleView.setText(getResources().getText(R.string.feedback_history_title));
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                Log.e("Test", "ON REFRESH");
                pageNo = 1;
                requestMessageList(pageNo);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                Log.e("Test", "ON onLoadMore");
                pageNo++;
                requestMessageList(pageNo);
            }
        });
        initRecyclerView();
    }


    private void initRecyclerView() {
        mList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FeedbackHistoryAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            showError("网络不稳定，请稍后重试");
        }
        requestMessageList(pageNo);
    }

    private void showError(String message) {
        mAllView.setVisibility(View.GONE);
        mErroView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        if (message == null) {
            mErrorTv.setText("网络不稳定，请稍后重试");
        } else {
            mErrorTv.setText(message);
        }
    }

    private void requestMessageList(int pageIndex) {
        try {
            if (!NetworkUtils.isNetworkAvailable(this)) {
                showError(ERROR_MSG_NET);
                return;
            }
        } catch (Exception e) {
            showError(ERROR_MSG_NET);
        }
        if (0 == pageIndex) {
            mList.clear();
        }
        FeedBackHttpUtils.loadFeedbackHistoryList(pageIndex,
                new FeedBackHttpUtils.OnLoadFeedbackHistoryCallBack<FeedbackHistoryBean>() {
                    @Override
                    public void onLoadSuccess(FeedbackHistoryBean feedbackHistoryBean) {
                        mRefreshLayout.setEnableRefresh(false);
                        mRefreshLayout.setEnableLoadmore(false);
                        mEndChecker = feedbackHistoryBean.data.getCount();
                        List<FeedbackHistoryBean.FeedbackDataBean.FeedbackItemBean> list =
                                feedbackHistoryBean.data.getFeedbackList();
                        mLoading.setVisibility(View.GONE);
                        if (null != list && list.size() > 0) {
                            mEmptyView.setVisibility(View.GONE);
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            mAllView.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onLoadFailed(String message) {
                        mRefreshLayout.setEnableRefresh(false);
                        showError(ERROR_MSG_ERROR);
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
            default:
                break;
        }
    }
}
