package com.sitechdev.vehicle.pad.module.map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
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
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.event.PoiEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapAnimUtil;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.map.util.MapVariants;
import com.sitechdev.vehicle.pad.module.map.util.PoiOverlay;
import com.sitechdev.vehicle.pad.util.PermissionHelper;
import com.sitechdev.vehicle.pad.view.CommonToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapActivity
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/12 0012 14:57
 * 修改时间：
 * 备注：
 */
@BindEventBus
public class MapActivity extends BaseActivity implements View.OnClickListener,
        AMap.OnMapClickListener, AMap.OnMapLongClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener {

    private MapView mMapView = null;
    private AMap mAMap = null;

    private static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    private static final int STROKE_COLOR = Color.argb(0, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(0, 0, 0, 180);

    private List<String> requestPermissionList = null;
    private MyLocationStyle myLocationStyle = null;

    Marker marker;
    private MarkerOptions markerOption = null, currentLocMarkerOption = null;

    private RelativeLayout flowRelativeLayout = null;
    private ImageView flowBtn = null, flowBtnBg = null;
    private TextView markerTitleView = null, markerDistanceView = null, markerDesView = null;

    private AppCompatTextView comView = null;
    private ImageView mBackView = null, mCloseView = null, mToCurrentLocView = null;

    private boolean isMapSelectAddress = false;
    private int currentSetType = -1;
    private TextView mSetAddressView = null;

    /**
     * 缩放地图级别
     */
    private int zoomMapIndex = 17, markerZoomIndex = 16, currentZoomMapIndex = zoomMapIndex;

    private boolean isSuccessLocation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDarkStatusBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.id_main_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        comView = findViewById(R.id.id_title_view);
        comView.setVisibility(View.GONE);
        comView.setOnClickListener(this);
        mBackView = findViewById(R.id.id_img_back);
        mBackView.setOnClickListener(this);
///        mBackView=findViewById(R.id.id_img_back);
///        mBackView.setOnClickListener(this);


        flowRelativeLayout = findViewById(R.id.id_main_flow);
        flowBtn = findViewById(R.id.id_iv_navi);

        markerTitleView = findViewById(R.id.id_marker_title);
        markerDistanceView = findViewById(R.id.id_marker_distance);
        markerDesView = findViewById(R.id.id_marker_desc);
//        flowBtnBg = findViewById(R.id.id_iv_navi_bg);
        mSetAddressView = findViewById(R.id.id_set_address);

        mToCurrentLocView = findViewById(R.id.id_img_location);
        mToCurrentLocView.setOnClickListener(this);

        LinearLayout mAddMapView = findViewById(R.id.id_linear_add);
        mAddMapView.setOnClickListener(this);

        LinearLayout mSubMapView = findViewById(R.id.id_linear_sub);
        mSubMapView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        MapVariants.hasSelectListScene = false;
        initMap();
        initLocationMap();
        initParams();
    }

    private void initParams() {
        isMapSelectAddress = false;
        Bundle mBundle = getBundle();
        if (mBundle == null) {
            return;
        }
        //展示poi类型
//        mIntent3.putExtra(AppConst.JUMP_MAP_TYPE, AppConst.MAP_POI_SHOW_TYPE);
//        //poi点内容--天安门
//        mIntent3.putExtra(AppConst.JUMP_MAP_DATA, (String) event.getEventValue());
        if (mBundle.containsKey(AppConst.JUMP_MAP_TYPE)) {
            String jumpType = mBundle.getString(AppConst.JUMP_MAP_TYPE);
            if (AppConst.MAP_POI_SHOW_TYPE.equalsIgnoreCase(jumpType)) {
                //展示POI
                String poiKey = mBundle.getString(AppConst.JUMP_MAP_DATA);
                if (StringUtils.isEmpty(poiKey)) {
                    return;
                }

                //开始搜索POI
                MapUtil.startQueryPoiInfo(this, poiKey, 5, 1, this);
            } else if (AppConst.JUMP_MAP_TYPE_SELECT_MAP_SET_ADDRESS.equalsIgnoreCase(jumpType)) {
                isMapSelectAddress = true;
                currentSetType = mBundle.getInt(AppConst.ADDRESS_SET_TYPE);
            }
        }
    }

    /**
     * 检测GPS、位置权限是否开启
     */
    public boolean showGPSContacts(Activity activity) {
        try {
            LocationManager lm = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean checkPermission() {
        if (!showGPSContacts(this)) {
            CommonToast.makeText(this, "系统检测到未开启GPS定位服务,请开启");
            return false;
        }
        //请求权限
        requestPermissionList = PermissionHelper.getNeedRequestPermissionNameList(this, LOCATIONGPS);
        if (requestPermissionList == null || requestPermissionList.isEmpty()) {
            return false;
        } else {
            PermissionHelper.requestPermissons(this, requestPermissionList, AppConst.PERMISSION_REQ_CODE);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConst.PERMISSION_REQ_CODE:
                if (grantResults.length != requestPermissionList.size()) {
                    return;
                }
                boolean isRequestPermissionList = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isRequestPermissionList = true;
                        break;
                    }
                }
                if (!isRequestPermissionList) {
                    //权限全都允许
                    initData();
                } else {
                    checkPermission();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setInfoWindowAdapter(this);
            mAMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
            mAMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
            mAMap.setOnMarkerClickListener(this);

            UiSettings mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setZoomControlsEnabled(false);//false=不显示高德提供的缩放按钮
            mUiSettings.setMyLocationButtonEnabled(false); //false=不显示高德提供的默认定位按钮
            mUiSettings.setRotateGesturesEnabled(true);//false= 旋转手势
            mUiSettings.setTiltGesturesEnabled(true);//false=倾斜手势
            //比例尺控件（最大比例是1：10m,最小比例是1：1000Km），位于地图右下角
            //控制比例尺控件是否显示,true=显示
            mUiSettings.setScaleControlsEnabled(true);
            //设置是否以地图中心点缩放
            mUiSettings.setGestureScaleByMapCenter(true);
//            //设置指南针是否可见。
//            mUiSettings.setCompassEnabled(true);

//            mAMap.showBuildings(false); //关闭3d楼块
            //设置是否打开交通路况图层。
            mAMap.setTrafficEnabled(true);
            //设置是否显示室内地图，默认不显示。
            mAMap.showIndoorMap(true);
            //缩放级别 3-20
            mAMap.animateCamera(CameraUpdateFactory.zoomTo(zoomMapIndex));

            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    SitechDevLog.i(AppConst.TAG, "zoom 级别为：====>" + cameraPosition.zoom);
                    currentZoomMapIndex = (int) cameraPosition.zoom;
                }
            });
            //mAMap.setOnMyLocationChangeListener(this);
        }
        mAMap.setOnMapClickListener(latLng -> {
            //单击地图空白处
            resetFrameLayout(false);
        });
    }

    /**
     * 设置一些amap的定位属性
     */
    private void initLocationMap() {
//        mAMap.setLocationSource(this);// 设置定位监听
        setupLocationStyle();
        //false不会再定位// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMyLocationEnabled(false);
    }

    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ico_current_location));
//        // 自定义精度范围的圆形边框颜色
//        myLocationStyle.strokeColor(STROKE_COLOR);
//        //自定义精度范围的圆形边框宽度
//        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(FILL_COLOR);
        //连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.interval(BuildConfig.DEBUG ? 5000 : 30000);
        // 将自定义的 myLocationStyle 对象添加到地图上
//        mAMap.setMyLocationStyle(myLocationStyle);
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，
        // 设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
//        myLocationStyle.showMyLocation(false);
        //已经有定位数据，移动到定位位置
        SitechDevLog.i(AppConst.TAG, "是否有定位数据，====>");
        if (LocationData.getInstance().getaMapLocation() != null) {
            SitechDevLog.i(AppConst.TAG, "已经有定位数据，移动到定位位置：====>");
            gotoCurrentLocation();
            //定位成功
            isSuccessLocation = true;
        }
    }

    /**
     * 展示当前定位图标
     */
    private void getCurrentLocationMarkerOption() {
        currentLocMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_current_location))
                .position(new LatLng(LocationData.getInstance().getLatitude(), LocationData.getInstance().getLongitude()))
                .title(LocationData.getInstance().getFormatAddress())
                .snippet(LocationData.getInstance().getProvinceName()
                        + "-" + LocationData.getInstance().getCityName()
                        + "-" + LocationData.getInstance().getDistrictName()
                        + "-" + LocationData.getInstance().getaMapLocation().getStreet()
                        + (StringUtils.isEmpty(LocationData.getInstance().getaMapLocation().getStreetNum()) ?
                        " " : ("-" + LocationData.getInstance().getaMapLocation().getStreetNum())))
                .draggable(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isMapSelectAddress = false;
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_title_view:
                //进入poi搜索页面
                Intent mIntent = new Intent(this, PoiSearchActivity.class);
                startActivityForResult(mIntent, AppConst.REQUEST_RESULT_CODE);
                break;
            case R.id.id_img_back:
                //返回、关闭
                finish();
                break;
            case R.id.id_img_location:
                resetFrameLayout(false);
                gotoCurrentLocation();
                break;
            case R.id.id_linear_add:
                resetMapZoom(true);
                break;
            case R.id.id_linear_sub:
                resetMapZoom(false);
                break;
            default:
                break;
        }
    }

    /**
     * 改变地图缩放级别
     *
     * @param zoomMapIndex true=增加一个缩放级别，false=降低一个缩放级别
     */
    private void resetMapZoom(boolean zoomMapIndex) {
        if (zoomMapIndex) {
            currentZoomMapIndex = (currentZoomMapIndex >= 19) ? 19 : currentZoomMapIndex + 1;
        } else {
            currentZoomMapIndex = (currentZoomMapIndex <= 3) ? 3 : currentZoomMapIndex - 1;
        }
        //缩放
        mAMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomMapIndex));
    }

    /**
     * 关闭浮层View
     *
     * @param isVisibility true=展示浮层，false=关闭浮层
     */
    private void resetFrameLayout(boolean isVisibility) {
        if (isVisibility) {
            if (flowRelativeLayout != null && !flowRelativeLayout.isShown()) {
                flowRelativeLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (flowRelativeLayout != null && flowRelativeLayout.isShown()) {
                flowRelativeLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 返回当前定位位置
     */
    private void gotoCurrentLocation() {
//        if (LocationData.getInstance().getLocation() == null) {
//            return;
//        }
        getCurrentLocationMarkerOption();
        //生成新marker
        addmarker2Map(currentLocMarkerOption, false);
        //定位经纬度
        LatLng latLng = new LatLng(LocationData.getInstance().getLatitude(), LocationData.getInstance().getLongitude());
        //移动视图
        changeLocCamera(latLng, zoomMapIndex, 0, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConst.REQUEST_RESULT_CODE) {
            //绘制poi的点到地图上
            if (data == null) {
                return;
            }
            PoiItem item = data.getParcelableExtra(AppConst.POI_DATA);
            startBreatheAnimation(item);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        clickMarker(marker);
        return true;
    }

    /**
     * mark点击事件
     *
     * @param marker
     */
    private void clickMarker(Marker marker) {
        //播放动画
        marker.setAnimation(MapAnimUtil.getJumpAnimation(this, mAMap, marker.getPosition()));
        marker.startAnimation();
        //展示底部view
        showMarkerInfoView(marker);
        //移动marker视图至中心
        changeLocCamera(marker.getPosition(), markerZoomIndex, 0, 0);
    }

    /**
     * 设置公司或家庭地址成功的回调
     *
     * @param marker 当前焦点marker
     * @return
     */
    private void setAddressTypeSuccess(Marker marker) {
        //直接返回setAddress页面
        Intent mIntent = new Intent();
        mIntent.putExtra("select_address_info", marker.getTitle());
        mIntent.putExtra("select_address_latitude", marker.getPosition().latitude);
        mIntent.putExtra("select_address_longitude", marker.getPosition().longitude);
        setResult(200, mIntent);
        isMapSelectAddress = false;
        finish();
    }

    /**
     * 打开marker信息视图
     *
     * @param marker
     */
    private void showMarkerInfoView(Marker marker) {
        resetFrameLayout(true);
        if (markerTitleView != null) {
            markerTitleView.setText(marker.getTitle());
        }
        if (markerDistanceView != null && LocationData.getInstance().getaMapLocation() != null) {
            markerDistanceView.setText(MapUtil.getDistance(LocationData.getInstance().getLatitude(),
                    LocationData.getInstance().getLongitude(),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
            ));
        }
        if (markerDesView != null) {
            markerDesView.setText(marker.getSnippet());
        }
//        if (flowBtnBg != null) {
//            flowBtnBg.startAnimation(AppUtil.getAnimationSet());
//        }
        flowBtn.setVisibility(isMapSelectAddress ? View.GONE : View.VISIBLE);
        mSetAddressView.setVisibility(isMapSelectAddress ? View.VISIBLE : View.GONE);

        if (isMapSelectAddress) {
            switch (currentSetType) {
                case AppConst.COMPONY_ADDRESS_SET_INDEX:
                    mSetAddressView.setText("设置为公司地址");
                    break;
                case AppConst.HOME_ADDRESS_SET_INDEX:
                    mSetAddressView.setText("设置为家庭地址");
                    break;
                default:
                    break;
            }
            mSetAddressView.setOnClickListener(v -> setAddressTypeSuccess(marker));
        } else {
            if (flowBtn != null) {
                flowBtn.setOnClickListener(v -> MapUtil.showNaviSelectClientDialog(MapActivity.this, marker.getPosition(), marker.getTitle()));
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类。
        SitechDevLog.i(AppConst.TAG, "MapActivity==搜索到的结果为====>" + poiResult.getPois().toString());
        if (poiResult != null && poiResult.getPois() != null && !poiResult.getPois().isEmpty()) {
            if (MapVariants.mPoiList != null) {
                MapVariants.mPoiList.clear();
            }
            MapVariants.mPoiList = poiResult.getPois();
            if (MapVariants.mPoiList.size() > 0) {
                MapVariants.hasSelectListScene = true;
                //合成tts语音播报
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_TTS_PLAY_TEXT, "为您找到" + MapVariants.mPoiList.size() + "个结果"));
                //将poi变为marker，绘制在地图上
                PoiOverlay poiOverlay = new PoiOverlay(this, mAMap, MapVariants.mPoiList);
                poiOverlay.removeFromMap();
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 当前定位的marker
     *
     * @param markerOption markOption
     * @param anim         true=加一个跳动动画
     */
    private void addmarker2Map(MarkerOptions markerOption, boolean anim) {
        mAMap.clear();
        marker = mAMap.addMarker(markerOption);
        marker.setClickable(true);

        if (anim) {
            marker.setAnimation(MapAnimUtil.getJumpAnimation(this, mAMap, marker.getPosition()));
            marker.startAnimation();
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        if (isMapSelectAddress) {
            //反地理编码查询--根据经纬度查询当前地点信息
            MapUtil.regeoAddressQuery(this, latLng, new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    SitechDevLog.i(AppConst.TAG, "定位信息==>" + regeocodeResult.getRegeocodeAddress().getFormatAddress());
                    try {
                        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed))
                                .position(new LatLng(latLng.latitude, latLng.longitude))
                                .title(regeocodeResult.getRegeocodeAddress().getFormatAddress())
                                .snippet(regeocodeResult.getRegeocodeAddress().getProvince()
                                        + "-" + regeocodeResult.getRegeocodeAddress().getCity()
                                        + "-" + regeocodeResult.getRegeocodeAddress().getDistrict()
                                        + "-" + regeocodeResult.getRegeocodeAddress().getTownship()
                                        + "-" + regeocodeResult.getRegeocodeAddress().getStreetNumber().getStreet()
                                        + regeocodeResult.getRegeocodeAddress().getStreetNumber().getNumber() + "号")
                                .draggable(true);
                        // 往地图上添加marker
                        addmarker2Map(markerOption, true);
                        //在中心位置显示
                        changeLocCamera(latLng, markerZoomIndex, 0, 0);
                        //
                        clickMarker(marker);
                    } catch (Exception e) {
                        SitechDevLog.exception(e);
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        }
    }

    /**
     * 添加一个标记到地图上
     *
     * @param latLng
     */
    private void addMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_order))
                .position(latLng)
                .draggable(true);
        addmarker2Map(markerOption, false);
    }

    /**
     * 高德marker呼吸动画
     */
    private void startBreatheAnimation(PoiItem poiItem) {
        if (poiItem == null) {
            return;
        }
        LatLonPoint latlngPoint = poiItem.getLatLonPoint();
        if (latlngPoint == null) {
            return;
        }
        // 呼吸动画
        //清除地图上已有的marker
        mAMap.clear();
        LatLng poiLatLng = new LatLng(latlngPoint.getLatitude(), latlngPoint.getLongitude());
        //生成新marker
        Marker breatheMarker = mAMap.addMarker(new MarkerOptions().position(poiLatLng).zIndex(1)
                .title(poiItem.getTitle())
                .snippet(poiItem.getSnippet())
                .anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_map_marker_circle_64)));
        // 中心的marker
        Marker breatheMarker_center = mAMap.addMarker(new MarkerOptions().position(poiLatLng).zIndex(2)
                .title(poiItem.getTitle())
                .snippet(poiItem.getSnippet())
                .anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_map_marker_circle_64)));
        showMarkerInfoView(breatheMarker_center);
        // 动画执行完成后，默认会保持到最后一帧的状态
        breatheMarker.setAnimation(MapAnimUtil.getWaterRippleSet());
        breatheMarker.startAnimation();
        //移动视图
        changeLocCamera(poiLatLng, markerZoomIndex, 0, 0);
    }

    /**
     * latLng 显示在地图中心
     *
     * @param latLng    点的经纬度
     * @param zoomIndex 缩放级别 3-20
     * @param tilt      俯仰角0°~45°（垂直与地图时为0）
     * @param bearing   偏航角 0~360° (正北方为0)
     */
    private void changeLocCamera(LatLng latLng, int zoomIndex, int tilt, int bearing) {
        currentZoomMapIndex = zoomIndex;
        //改变视图中心焦点
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(latLng, zoomIndex, tilt, bearing)));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(PoiEvent poiEvent) {
        SitechDevLog.i(AppConst.TAG, this + "==地图语音消息==" + poiEvent.toString());
        if (!MapUtil.isMapActivityFront()) {
            //当前不是地图页面，不做处理，交由相应页面的事件响应处理
            SitechDevLog.i(AppConst.TAG, this + " == " + poiEvent.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
            return;
        }
        switch (poiEvent.getEventKey()) {
            case PoiEvent.EVENT_QUERY_POI_KEYWORD:
                //调用PoiActivity,搜索poi
                Intent mIntent = new Intent();
                //因POI的返回规则,先打开地图页面，再通过地图跳转POI搜索
                mIntent.setClass(this, PoiSearchActivity.class);
                mIntent.putExtra(AppConst.POI_QUERY_KEYWORD, poiEvent.getEventValue());
                ThreadUtils.runOnUIThread(() -> {
                    startActivityForResult(mIntent, AppConst.REQUEST_RESULT_CODE);
                });
                break;
            case PoiEvent.EVENT_QUERY_NEARBY_POI_KEYWORD:
                //调用PoiActivity,搜索poi
                Intent mIntent2 = new Intent();
                //因POI的返回规则,先打开地图页面，再通过地图跳转POI搜索
                mIntent2.setClass(this, PoiSearchActivity.class);
                mIntent2.putExtra(AppConst.POI_QUERY_KEYWORD, poiEvent.getEventValue());
                ThreadUtils.runOnUIThread(() -> {
                    startActivityForResult(mIntent2, AppConst.REQUEST_RESULT_CODE);
                });
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMapEvent(MapEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==地图语音消息==" + event.toString());
        if (!MapUtil.isMapActivityFront()) {
            //当前不是地图页面，不做处理，交由相应页面的事件响应处理
            SitechDevLog.i(AppConst.TAG, this + " == " + event.toString() + " ==不做处理 ");
            return;
        }
        switch (event.getEventKey()) {
            //定位成功
            case MapEvent.EVENT_LOCATION_SUCCESS:
                if (!isSuccessLocation) {
                    //还未定位成功
                    //已经有定位数据，移动到定位位置
                    if (LocationData.getInstance().getaMapLocation() != null) {
                        gotoCurrentLocation();
                        //定位成功
                        isSuccessLocation = true;
                    }
                }
                break;
            //展示DPI
            case MapEvent.EVENT_OPEN_MAP_SHOW_POI:
                //展示POI
                String poiKey = (String) event.getEventValue();
                if (StringUtils.isEmpty(poiKey)) {
                    return;
                }

                //开始搜索POI
                MapUtil.startQueryPoiInfo(this, poiKey, 10, 1, this);
                break;
            default:
                break;
        }
    }
}
