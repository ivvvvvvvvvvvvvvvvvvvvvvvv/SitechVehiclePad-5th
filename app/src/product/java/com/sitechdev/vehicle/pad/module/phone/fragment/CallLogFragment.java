package com.sitechdev.vehicle.pad.module.phone.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpFragment;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.manager.trace.TraceManager;
import com.sitechdev.vehicle.pad.model.contract.CallLogContract;
import com.sitechdev.vehicle.pad.model.phone.BTModel;
import com.sitechdev.vehicle.pad.model.phone.CallLog;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.adapter.CallLogAdapter;
import com.sitechdev.vehicle.pad.module.phone.presenter.CallLogPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 最近通话
 *
 * @author haznes
 */
@BindEventBus
public class CallLogFragment extends MvpFragment<CallLogPresenter> implements CallLogContract.View, View.OnClickListener {

    public static final String TAG = CallLogFragment.class.getSimpleName();
    private CallLogAdapter adapter;
    private TextView hintTxt;
    private RecyclerView recyclerCallLog;
    private RelativeLayout downLayout;
    private Button downBtn;
    private ImageView mEmptyView;

    public CallLogFragment() {
    }

    public static CallLogFragment newInstance() {
        CallLogFragment fragment = new CallLogFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_call_log;
    }

    @Override
    protected CallLogPresenter createPresenter() {
        return new CallLogPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerCallLog = mContentView.findViewById(R.id.call_log_recycler_view);
        hintTxt = mContentView.findViewById(R.id.call_log_hint_txt);
        downLayout = mContentView.findViewById(R.id.call_log_down_layout);
        downBtn = mContentView.findViewById(R.id.call_log_down_btn);
        mEmptyView = mContentView.findViewById(R.id.iv_call_logs_empty);
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerCallLog.setLayoutManager(manager);
        adapter = new CallLogAdapter(getActivity());
        recyclerCallLog.setAdapter(adapter);
        mPresenter.start();
    }

    @Override
    protected void initListener() {
        downBtn.setOnClickListener(this);
        adapter.setOnItemClickListener(position -> {
            CallLog cl = adapter.getData().get(position);
            if (cl != null) {
                mPresenter.dial("未知".equals(cl.getName())
                        ? cl.getPhoneNumber() : cl.getName(), cl.getPhoneNumber());
            }
        });
    }

    @Override
    public void showLoadSuccessView() {
        downLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        hintTxt.setText("加载完成");
        refreshCallLogs(BtGlobalRef.callLogs);
        changeDisplay(3);
    }

    @Override
    public void showLoadFailedView() {
        changeDisplay(2);
    }

    @Override
    public void showEmptyView(boolean isFail) {
        if(isFail){
            hintTxt.setText("手机通讯录未授权,请授权后重新操作");
        } else {
            hintTxt.setText("暂无通话记录!");
        }
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        downLayout.setVisibility(View.VISIBLE);
        recyclerCallLog.setVisibility(View.GONE);
    }

    @Override
    public void refreshCallLogs(ArrayList<CallLog> callLogs) {
        if (null != adapter) {
            adapter.updateList(callLogs);
        }
    }

    @Override
    public void onClCount(int count) {
        hintTxt.setText("已加载通话记录条数" + count);
        downLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        hintTxt.setVisibility(View.VISIBLE);
        downBtn.setVisibility(View.GONE);
    }

    @Override
    public void showNoFilerCallLogs() {
        hintTxt.setText("未搜索到相应结果，\n请加载全部通话记录!");
        downBtn.setVisibility(View.VISIBLE);
        downLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
        recyclerCallLog.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call_log_down_btn) {
            //TODO cold 下载通话记录的逻辑
            mPresenter.reqCallLogs();
//            BTModel btData = DataFactory.produceMemData().getBTData();
//            if (btData.nPbapcState.ordinal() <= BTModel.PbapcState.STANDBY.ordinal()) {
//                showLoading();
//                mPresenter.reqCallLogs();
//            } else {
//                ToastUtils.showShort("通讯录正在加载中，请稍后");
//            }
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        downLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        downBtn.setVisibility(View.GONE);
        hintTxt.setText("加载中...");
    }

    @Override
    public void changeDisplay(int type) {
        switch (type) {
            case 1: {
                downLayout.setVisibility(View.VISIBLE);
                hintTxt.setText("蓝牙未连接");
                downBtn.setVisibility(View.GONE);
                recyclerCallLog.setVisibility(View.GONE);
            }
            break;
            case 2: {
                hintTxt.setText("通话记录列表为空，请加载重试!");
                downBtn.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.VISIBLE);
                downLayout.setVisibility(View.VISIBLE);
                recyclerCallLog.setVisibility(View.GONE);
            }
            break;
            case 3: {
                downLayout.setVisibility(View.GONE);
                recyclerCallLog.setVisibility(View.VISIBLE);
            }
            break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBTEvent(final BTEvent event) {
        if (null != mPresenter) {
            mPresenter.onBTEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFilterCallLogs(SysEvent event) {
        if (null != mPresenter) {
            mPresenter.onFilterCallLogs(event);
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
