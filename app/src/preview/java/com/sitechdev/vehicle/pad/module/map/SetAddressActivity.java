package com.sitechdev.vehicle.pad.module.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：PoiSearchActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/13 0013 16:32
 * 修改时间：
 * 备注：
 */
public class SetAddressActivity extends BaseActivity {
    private EditText mInputAddressEdtView = null;
    private LinearLayout mSelectMyLocation = null, mSelectMapLocation = null;

    private int currentAddressSetType = AppConst.HOME_ADDRESS_SET_INDEX;
    /**
     * 输入法管理器
     */
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_set_address;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mInputAddressEdtView = findViewById(R.id.id_input_set_address);
        mSelectMyLocation = findViewById(R.id.id_select_my_location);
        mSelectMapLocation = findViewById(R.id.id_select_map_location);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolBarView.setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSelectMyLocation.setOnClickListener(this);
        mSelectMapLocation.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Bundle mBundle = getBundle();
        if (mBundle == null) {
            return;
        }
        currentAddressSetType = mBundle.getInt(AppConst.ADDRESS_SET_TYPE);
        switch (currentAddressSetType) {
            case AppConst.COMPONY_ADDRESS_SET_INDEX:
                mToolBarView.setMainTitle(getString(R.string.string_set_address_title, "公司地址"));
                if (StringUtils.isEmpty(LocationData.getInstance().getWorkPoiName())) {
                    mInputAddressEdtView.setHint(String.format("请输入公司地址"));
                } else {
                    mInputAddressEdtView.setText(LocationData.getInstance().getWorkPoiName());
                }
                break;
            case AppConst.HOME_ADDRESS_SET_INDEX:
                mToolBarView.setMainTitle(getString(R.string.string_set_address_title, "家庭地址"));
                if (StringUtils.isEmpty(LocationData.getInstance().getHomePoiName())) {
                    mInputAddressEdtView.setHint(String.format("请输入家庭地址"));
                } else {
                    mInputAddressEdtView.setText(LocationData.getInstance().getHomePoiName());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_select_my_location:
                setAddressTypeBean(LocationData.getInstance().getFormatAddress(), LocationData.getInstance().getLatitude(), LocationData.getInstance().getLongitude());
                //刷新界面
                refreshView();
                break;
            case R.id.id_select_map_location:
                Intent mIntent = new Intent(this, MapActivity.class);
                mIntent.putExtra(AppConst.JUMP_MAP_TYPE, AppConst.JUMP_MAP_TYPE_SELECT_MAP_SET_ADDRESS);
                mIntent.putExtra(AppConst.ADDRESS_SET_TYPE, currentAddressSetType);
                startActivityForResult(mIntent, AppConst.REQUEST_RESULT_CODE);

                CommonToast.showToast("在地图上长按选择地址");
                break;
            default:
                break;
        }
    }

    /**
     * 设置地址实体
     *
     * @param address   address
     * @param latitude  latitude
     * @param longitude longitude
     */
    private void setAddressTypeBean(String address, double latitude, double longitude) {
        switch (currentAddressSetType) {
            case AppConst.COMPONY_ADDRESS_SET_INDEX:
                LocationData.getInstance().setHasWorkAddress(true);
                LocationData.getInstance().setWorkPoiName(address);
                LocationData.getInstance().setWorkAddressName(address);
                LocationData.getInstance().setWorkLatitude(latitude);
                LocationData.getInstance().setWorkLongitude(longitude);
                MapUtil.sendAMapAddressSave(2);
                CommonToast.showToast("设置为公司成功");
                break;
            case AppConst.HOME_ADDRESS_SET_INDEX:
                LocationData.getInstance().setHasHomeAddress(true);
                LocationData.getInstance().setHomePoiName(address);
                LocationData.getInstance().setHomeAddressName(address);
                LocationData.getInstance().setHomeLatitude(latitude);
                LocationData.getInstance().setHomeLongitude(longitude);
                MapUtil.sendAMapAddressSave(1);
                CommonToast.showToast("设置为家成功");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConst.REQUEST_RESULT_CODE && resultCode == 200 && data != null) {
            //选择地点成功
            String address = data.getStringExtra("select_address_info");
            double latitude = data.getDoubleExtra("select_address_latitude", 0d);
            double longitude = data.getDoubleExtra("select_address_longitude", 0d);
            if (latitude != 0 && longitude != 0) {
                //设置实体
                setAddressTypeBean(address, latitude, longitude);
                //刷新界面
                refreshView();
            }
        }
    }

    /**
     * 刷新页面
     */
    private void refreshView() {
        switch (currentAddressSetType) {
            case AppConst.COMPONY_ADDRESS_SET_INDEX:
                mToolBarView.setMainTitle(getString(R.string.string_set_address_title, "公司地址"));
                mInputAddressEdtView.setText(LocationData.getInstance().getWorkPoiName());
                break;
            case AppConst.HOME_ADDRESS_SET_INDEX:
                mToolBarView.setMainTitle(getString(R.string.string_set_address_title, "家庭地址"));
                mInputAddressEdtView.setText(LocationData.getInstance().getHomePoiName());
                break;
            default:
                break;
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
        mInputAddressEdtView.setFocusable(false);//设置输入框不可聚焦,即失去焦点和光标
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(mInputAddressEdtView.getWindowToken(), 0);//隐藏输入法
        }
    }
}
