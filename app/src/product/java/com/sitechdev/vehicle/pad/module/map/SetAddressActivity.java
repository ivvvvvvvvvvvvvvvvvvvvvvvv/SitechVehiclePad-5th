package com.sitechdev.vehicle.pad.module.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.router.RouterConstants;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
@Route(path = RouterConstants.SET_ADDRESS_PAGE)
public class SetAddressActivity extends BaseActivity {
    private EditText mInputHomeAddressEdtView = null, mInputWorkAddressEdtView = null;
    private LinearLayout mSelectHomeMyLocation = null, mSelectHomeMapLocation = null;
    private LinearLayout mSelectWorkMyLocation = null, mSelectWorkMapLocation = null;
    private TextView mSaveBtn = null, tvTitle;
    private ImageView btn_back = null;
    /**
     * 输入法管理器
     */
    private InputMethodManager mInputMethodManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_set_address;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.tv_sub_title);
        btn_back = findViewById(R.id.iv_sub_back);
        //家庭地址输入框
        mInputHomeAddressEdtView = findViewById(R.id.id_input_set_home_address);
        //公司地址输入框
        mInputWorkAddressEdtView = findViewById(R.id.id_input_set_work_address);

        //家庭地址--当前定位
        mSelectHomeMyLocation = findViewById(R.id.id_select_home_my_location);
        //家庭地址--地图选择
        mSelectHomeMapLocation = findViewById(R.id.id_select_home_map_location);

        //公司地址--当前定位
        mSelectWorkMyLocation = findViewById(R.id.id_select_work_my_location);
        //公司地址--地图选择
        mSelectWorkMapLocation = findViewById(R.id.id_select_work_map_location);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //保存
        mSaveBtn = findViewById(R.id.id_tv_save);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_back.setOnClickListener(this);

        mSelectHomeMyLocation.setOnClickListener(this);
        mSelectHomeMapLocation.setOnClickListener(this);

        mSelectWorkMyLocation.setOnClickListener(this);
        mSelectWorkMapLocation.setOnClickListener(this);

        mSaveBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("设置常用地址");
//        if (StringUtils.isEmpty(LocationData.getInstance().getWorkPoiName())) {
//            mInputWorkAddressEdtView.setText(LocationData.getInstance().getWorkPoiName());
//        }
//        if (StringUtils.isEmpty(LocationData.getInstance().getHomePoiName())) {
//            mInputHomeAddressEdtView.setText(LocationData.getInstance().getHomePoiName());
//        }
        refreshView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            case R.id.id_select_home_my_location:
                //家庭地址--当前定位
                setAddressTypeBean(AppConst.HOME_ADDRESS_SET_INDEX);
                //刷新界面
                refreshView();
                break;
            case R.id.id_select_home_map_location:
                //家庭地址--地图选择
                MapUtil.sendAMapAddressView(0);
                break;
            case R.id.id_select_work_my_location:
                //公司地址--当前定位
                setAddressTypeBean(AppConst.COMPONY_ADDRESS_SET_INDEX);
                //刷新界面
                refreshView();
                break;
            case R.id.id_select_work_map_location:
                //公司地址--地图选择
                MapUtil.sendAMapAddressView(1);
                break;
            case R.id.id_tv_save:
                //
//                setAddressTypeBean(AppConst.COMPONY_ADDRESS_SET_INDEX);
//                //
//                setAddressTypeBean(AppConst.HOME_ADDRESS_SET_INDEX);

                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 设置地址实体
     *
     * @param addressType addressType
     */
    private void setAddressTypeBean(int addressType) {
        switch (addressType) {
            //公司地址
            case AppConst.COMPONY_ADDRESS_SET_INDEX:
                LocationData.getInstance().setHasWorkAddress(true);
                LocationData.getInstance().setWorkPoiName(LocationData.getInstance().getWorkPoiName());
                LocationData.getInstance().setWorkAddressName(LocationData.getInstance().getWorkAddressName());
                LocationData.getInstance().setWorkLatitude(LocationData.getInstance().getLatitude());
                LocationData.getInstance().setWorkLongitude(LocationData.getInstance().getLongitude());
                MapUtil.sendAMapAddressSave(2);
//                CommonToast.showToast("设置为公司成功");
                break;
            case AppConst.HOME_ADDRESS_SET_INDEX:
                LocationData.getInstance().setHasHomeAddress(true);
                LocationData.getInstance().setHomePoiName(LocationData.getInstance().getHomePoiName());
                LocationData.getInstance().setHomeAddressName(LocationData.getInstance().getHomeAddressName());
                LocationData.getInstance().setHomeLatitude(LocationData.getInstance().getLatitude());
                LocationData.getInstance().setHomeLongitude(LocationData.getInstance().getLongitude());
                MapUtil.sendAMapAddressSave(1);
//                CommonToast.showToast("设置为家成功");
                break;
            default:
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == AppConst.REQUEST_RESULT_CODE && resultCode == 200 && data != null) {
//            //选择地点成功
//            String address = data.getStringExtra("select_address_info");
//            double latitude = data.getDoubleExtra("select_address_latitude", 0d);
//            double longitude = data.getDoubleExtra("select_address_longitude", 0d);
//            if (latitude != 0 && longitude != 0) {
//                //设置实体
//                setAddressTypeBean(address, latitude, longitude);
//                //刷新界面
//                refreshView();
//            }
//        }
//    }

    /**
     * 刷新页面
     */
    private void refreshView() {
        SitechDevLog.i(AppConst.TAG, "SetAddressActivity==>LocationData=getWorkAddressName=" + LocationData.getInstance().getWorkAddressName()
                + " ***LocationData=getWorkPoiName=" + LocationData.getInstance().getWorkPoiName() + "**===");
        SitechDevLog.i(AppConst.TAG, "SetAddressActivity==>LocationData=getHomeAddressName=" + LocationData.getInstance().getHomeAddressName()
                + " ***LocationData=getHomePoiName=" + LocationData.getInstance().getHomePoiName() + "**===");
        if (mInputWorkAddressEdtView != null) {
            if (!StringUtils.isEmpty(LocationData.getInstance().getWorkAddressName()) && !StringUtils.isEmpty(LocationData.getInstance().getWorkPoiName())) {
                mInputWorkAddressEdtView.setText(String.format("%s(%s)", LocationData.getInstance().getWorkPoiName(), LocationData.getInstance().getWorkAddressName()));
            } else if (StringUtils.isEmpty(LocationData.getInstance().getWorkAddressName()) && !StringUtils.isEmpty(LocationData.getInstance().getWorkPoiName())) {
                mInputWorkAddressEdtView.setText(LocationData.getInstance().getWorkPoiName());
            } else if (!StringUtils.isEmpty(LocationData.getInstance().getWorkAddressName()) && StringUtils.isEmpty(LocationData.getInstance().getWorkPoiName())) {
                mInputWorkAddressEdtView.setText(LocationData.getInstance().getWorkAddressName());
            } else {
                mInputWorkAddressEdtView.setText("");
            }
        }
        if (mInputHomeAddressEdtView != null) {
            if (!StringUtils.isEmpty(LocationData.getInstance().getHomeAddressName()) && !StringUtils.isEmpty(LocationData.getInstance().getHomePoiName())) {
                mInputHomeAddressEdtView.setText(String.format("%s(%s)", LocationData.getInstance().getHomePoiName(), LocationData.getInstance().getHomeAddressName()));
            } else if (StringUtils.isEmpty(LocationData.getInstance().getHomeAddressName()) && !StringUtils.isEmpty(LocationData.getInstance().getHomePoiName())) {
                mInputHomeAddressEdtView.setText(LocationData.getInstance().getHomePoiName());
            } else if (!StringUtils.isEmpty(LocationData.getInstance().getHomeAddressName()) && StringUtils.isEmpty(LocationData.getInstance().getHomePoiName())) {
                mInputHomeAddressEdtView.setText(LocationData.getInstance().getHomeAddressName());
            } else {
                mInputHomeAddressEdtView.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }

    /**
     * edittext失去焦点，并隐藏键盘
     */
    public void hideSoftInput() {
        mInputWorkAddressEdtView.setFocusable(false);//设置输入框不可聚焦,即失去焦点和光标
        mInputHomeAddressEdtView.setFocusable(false);//设置输入框不可聚焦,即失去焦点和光标
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(mInputWorkAddressEdtView.getWindowToken(), 0);//隐藏输入法
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapEvent(MapEvent event) {
        SitechDevLog.i(AppConst.TAG, "SetAddressActivity===onMapEvent==" + event.getEventKey());
        switch (event.getEventKey()) {
            case MapEvent.EVENT_MAP_HOME_WORK_ADDRESS_RESULT:
                refreshView();
                break;
            default:
                break;
        }
    }
}
