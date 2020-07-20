package com.sitechdev.vehicle.pad.module.phone.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpFragment;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.model.contract.ContactContract;
import com.sitechdev.vehicle.pad.model.phone.BTModel;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.adapter.ContactAdapter;
import com.sitechdev.vehicle.pad.module.phone.presenter.ContactPresenter;
import com.sitechdev.vehicle.pad.module.phone.utils.IndexUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

//import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

/**
 * 通讯录
 *
 * @author hazens
 */
@BindEventBus
public class ContactFragment extends MvpFragment<ContactPresenter> implements View.OnClickListener,
        ContactAdapter.OnItemClickListener, ContactContract.View {

    public static final String TAG = ContactFragment.class.getSimpleName();

    private ContactAdapter adapter;
    private RecyclerView phoneBookList;
    private ImageView mEmptyView;
    private RelativeLayout mEmptyLayout;
    private TextView hintTxt;
    private Button downBtn;
    private ListView mIndexListView;
    private LinearLayoutManager mLayoutManager;
    private TwinklingRefreshLayout refreshLayout;
    private ArrayAdapter<String> mIndexAdapter;

    public ContactFragment() {
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_phone_book;
    }

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        phoneBookList = mContentView.findViewById(R.id.recy_pb);
        mEmptyView = mContentView.findViewById(R.id.iv_contact_empty);
        mEmptyLayout = mContentView.findViewById(R.id.phone_down_layout);
        hintTxt = mContentView.findViewById(R.id.phone_book_hint_txt);
        downBtn = mContentView.findViewById(R.id.phone_book_down_btn);
        mIndexListView = mContentView.findViewById(R.id.lv_index);
        refreshLayout = mContentView.findViewById(R.id.pb_swipe_refresh);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        phoneBookList.setLayoutManager(mLayoutManager);
        adapter = new ContactAdapter(getActivity());
        phoneBookList.setAdapter(adapter);

        mIndexAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_bluetooth_phonebook_index
                , IndexUtils.getAllIndex());
        mIndexListView.setAdapter(mIndexAdapter);

        mPresenter.start();
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(this);
        downBtn.setOnClickListener(this);
        mIndexListView.setOnItemClickListener((adapterView, view, i, l) -> mLayoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(IndexUtils.getAllIndex().get(i)), 0));
        mIndexListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingDown(refreshLayout, fraction);
            }

            @Override
            public void onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingUp(refreshLayout, fraction);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                //开始进行业务逻辑处理
                requestPb(false);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager.scrollToPositionWithOffset(-1, 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phone_book_down_btn) {
            requestPb(true);
        }
    }

    @Override
    public void showLoading() {
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        hintTxt.setVisibility(View.VISIBLE);
        hintTxt.setText("加载中...");
    }

    private void requestPb(boolean showLoading) {
        //TODO cold 加载通讯录的逻辑
        mPresenter.reqPhoneBook();
//        BTModel btData = DataFactory.produceMemData().getBTData();
//        if (btData.nPbapcState.ordinal() <= BTModel.PbapcState.PHONEBOOK.ordinal()) {
//            if (showLoading) {
//                showLoading();
//            }
//            mPresenter.reqPhoneBook();
//        } else {
//            ToastUtils.showShort("最近通话记录正在加载中，请稍后");
//            refreshLayout.finishRefreshing();
//        }
    }

    @Override
    public void showLoadSuccessView() {
        logTest("showLoadSuccessView");
        if (phoneBookList.getVisibility() == View.VISIBLE) {
        } else {
            hintTxt.setText("加载完成");
        }
        adapter.updateList(BtGlobalRef.contactSorts);
        refreshLayout.setVisibility(View.VISIBLE);
        phoneBookList.setVisibility(View.VISIBLE);
        hintTxt.setVisibility(View.GONE);
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mIndexListView.setVisibility(View.VISIBLE);
        refreshLayout.finishRefreshing();
    }

    @Override
    public void showLoadFailedView() {
        refreshLayout.setVisibility(View.GONE);
        phoneBookList.setVisibility(View.GONE);
        mIndexListView.setVisibility(View.GONE);
        hintTxt.setVisibility(View.VISIBLE);
        downBtn.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        hintTxt.setText("通讯录列表为空，请加载重试!");
    }

    @Override
    public void showEmptyView(boolean isFail) {
        refreshLayout.setVisibility(View.GONE);
        phoneBookList.setVisibility(View.GONE);
        mIndexListView.setVisibility(View.GONE);
        hintTxt.setVisibility(View.VISIBLE);
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        if(isFail){
            hintTxt.setText("手机通讯录未授权，请重新连接蓝牙设备并允许同步通讯录");
        } else {
            hintTxt.setText("暂无通讯录!");
        }
    }

    @Override
    public void onPbCount(int count) {
        logTest("onPbCount-----count:"+count);
        if (phoneBookList.getVisibility() == View.VISIBLE) {
            hintTxt.setVisibility(View.GONE);
        } else {
            hintTxt.setText("已加载通讯录数目" + count);
            hintTxt.setVisibility(View.VISIBLE);
        }
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.INVISIBLE);
        mEmptyLayout.setVisibility(View.VISIBLE);

        adapter.updateList(BtGlobalRef.contactSorts);
        refreshLayout.setVisibility(View.VISIBLE);
        phoneBookList.setVisibility(View.VISIBLE);
        hintTxt.setVisibility(View.GONE);
        downBtn.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mIndexListView.setVisibility(View.VISIBLE);
        refreshLayout.finishRefreshing();
    }

    @Override
    public void onItemClicked(int position) {
        List<Contact> data = adapter.getData();
        if (data != null && 0 <= position && position <= data.size() - 1) {
            mPresenter.dial(data.get(position).getPhoneNumber());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBTEvent(final BTEvent event) {
        if (null != mPresenter) {
            mPresenter.onBTEvent(event);
        }
    }

    @SuppressLint("LongLogTag")
    private void logTest(String msg){
        Log.e("Test_Phone_ContactFragment","-----"+msg);
    }
}
