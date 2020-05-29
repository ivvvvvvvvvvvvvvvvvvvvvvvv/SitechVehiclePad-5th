package com.sitechdev.vehicle.pad.module.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.PoiEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.module.map.adapter.PoiDataItemAdapter;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：PoiSearchActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/13 0013 16:32
 * 修改时间：
 * 备注：
 */
@BindEventBus
public class PoiSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private EditText poiAddressTview = null;
    //    private ImageView contentCloseImageView = null;
    private RecyclerView mPoiRecycleView = null;
    /**
     * 输入法管理器
     */
    private InputMethodManager mInputMethodManager;
    private PoiDataItemAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_poi_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ImageView backImage = findViewById(R.id.id_img_back);
        backImage.setOnClickListener(this);

//        TextView cancelTextview = findViewById(R.id.id_tv_cancel);
//        cancelTextview.setOnClickListener(this);

        poiAddressTview = findViewById(R.id.id_input_poi);
        poiAddressTview.setOnClickListener(this);
        poiAddressTview.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                startQueryPoiView();
            } else {
                hideSoftInput();
            }
        });
        poiAddressTview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !StringUtils.isEmpty(editable.toString())) {
//                    contentCloseImageView.setVisibility(View.VISIBLE);
                    //开始搜索poi
                    startQueryPoiInfo(editable.toString());
                } else {
//                    contentCloseImageView.setVisibility(View.GONE);
                }
            }
        });

//        contentCloseImageView = findViewById(R.id.id_img_txt_close);
//        contentCloseImageView.setOnClickListener(this);

        mPoiRecycleView = findViewById(R.id.id_poi_recycle_view);
        //poi数据
        mAdapter = new PoiDataItemAdapter(this);
        mAdapter.setOnItemClickListener(new PoiDataItemAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //点击了索引值
                if (view != null && view.getTag() != null) {
                    PoiItem poiItem = (PoiItem) view.getTag();
                    SitechDevLog.i(AppConst.TAG, "点击了POI详情为：==========&&&");
//        SitechDevLog.w(AppConst.TAG, "***************************************************");
                    //getTitle=正定县解放街小学,getAdName=正定县,getCityName=石家庄市,getProvinceName=河北省,
                    // getSnippet=镇州南街与中山东路交汇西,getTypeDes=科教文化服务;学校;小学,getBusinessArea=,
                    // getDistance=-1,toString=正定县解放街小学
                    SitechDevLog.i(AppConst.TAG, "兴趣点信息==》getTitle=" + poiItem.getTitle()
                            + ",getAdName=" + poiItem.getAdName()
                            + ",getCityName=" + poiItem.getCityName()
                            + ",getProvinceName=" + poiItem.getProvinceName()
                            + ",getSnippet=" + poiItem.getSnippet()
                            + ",getTypeDes=" + poiItem.getTypeDes()
                            + ",getBusinessArea=" + poiItem.getBusinessArea()
                            + ",getDistance=" + poiItem.getDistance()
                            + ",toString=" + poiItem.toString()
                    );
                    SitechDevLog.i(AppConst.TAG, "点击了POI详情为：==========&&&");
                    Intent mIntent = new Intent();
                    mIntent.putExtra(AppConst.POI_DATA, poiItem);
                    setResult(AppConst.REQUEST_RESULT_CODE, mIntent);
                    finish();
                }
            }
        });
        mPoiRecycleView.setAdapter(mAdapter);//设置适配器
        //设置布局管理器 , 将布局设置成纵向
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPoiRecycleView.setLayoutManager(linerLayoutManager);
    }

    private void startQueryPoiInfo(String keyword) {
        MapUtil.startQueryPoiInfo(PoiSearchActivity.this, keyword, 30, 1, PoiSearchActivity.this);
    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Bundle mBundle = getBundle();
        if (mBundle != null) {
            String queryKeyword = mBundle.getString(AppConst.POI_QUERY_KEYWORD);
            queryPoiKeyword(queryKeyword);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.id_tv_cancel:
//                //隐藏这个搜索view
//                hideSoftInput();
            case R.id.id_img_back:
                finish();
                break;
            case R.id.id_input_poi:
                startQueryPoiView();
                break;
//            case R.id.id_img_txt_close:
//                queryPoiKeyword("");
//                break;
            default:
                break;
        }
    }

    private void startQueryPoiView() {
        poiAddressTview.setFocusable(true);//设置输入框可聚集
        poiAddressTview.setFocusableInTouchMode(true);//设置触摸聚焦
        poiAddressTview.requestFocus();//请求焦点
        poiAddressTview.findFocus();//获取焦点
        mInputMethodManager.showSoftInput(poiAddressTview, InputMethodManager.SHOW_FORCED);//显示输入法
    }

    /**
     * edittext失去焦点，并隐藏键盘
     */
    public void hideSoftInput() {
        poiAddressTview.setFocusable(false);//设置输入框不可聚焦,即失去焦点和光标
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(poiAddressTview.getWindowToken(), 0);//隐藏输入法
        }
    }

    /**
     * 1）解析result，获取POI信息。
     * 2）result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类。
     * 3）若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市。
     * 4）如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
     * 5）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
     *
     * @param poiResult
     * @param i
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类。
        SitechDevLog.i(AppConst.TAG, "搜索到的结果为====>" + poiResult.getPois().toString());
        if (poiResult != null && poiResult.getPois() != null && !poiResult.getPois().isEmpty()) {
            ArrayList<PoiItem> poiItemList = poiResult.getPois();
            if (poiItemList.size() > 0) {
                //合成tts语音播报
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_TTS_PLAY_TEXT, "为您找到" + poiItemList.size() + "个结果"));
                mAdapter.updateData(poiItemList);
                hideSoftInput();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(PoiEvent poiEvent) {
        SitechDevLog.i(AppConst.TAG, this + "==地图语音消息==" + poiEvent.toString());
        if (!MapUtil.isPoiActivityFront()) {
            //当前不是地图页面，不做处理，交由相应页面的事件响应处理
            SitechDevLog.i(AppConst.TAG, this + " == " + poiEvent.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
            return;
        }
        switch (poiEvent.getEventKey()) {
            case PoiEvent.EVENT_QUERY_POI_KEYWORD:
                //开始检索
                ThreadUtils.runOnUIThread(() -> {
                    queryPoiKeyword(poiEvent.getEventValue());
                });
                break;
            case PoiEvent.EVENT_QUERY_NEARBY_POI_KEYWORD:
                break;
            default:
                break;
        }
    }

    private void queryPoiKeyword(String keyword) {
        if (poiAddressTview != null) {
            poiAddressTview.setText(keyword);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }
}
