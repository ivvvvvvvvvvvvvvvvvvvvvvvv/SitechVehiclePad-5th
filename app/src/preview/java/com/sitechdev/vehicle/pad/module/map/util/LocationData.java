package com.sitechdev.vehicle.pad.module.map.util;

import com.amap.api.location.AMapLocation;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：LocationData
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/19 0019 11:39
 * 修改时间：
 * 备注：
 */
public class LocationData {

    private String formatAddress = "";
//    private Location mLocation = null;

    private double latitude = 0d;
    private double longitude = 0d;

    private String provinceName = "";
    private String cityName = "";
    private String districtName = "";

//    private HomeAddressBean homeAddressBean = null;
//    private ComponyAddressBean componyAddressBean = null;

    private AMapLocation aMapLocation = null;

    private boolean hasHomeAddress = false;
    private String homePoiName = "";
    private String homeAddressName = "";
    private double homeLatitude = 0d;
    private double homeLongitude = 0d;

    private boolean hasWorkAddress = false;
    private String workPoiName = "";
    private String workAddressName = "";
    private double workLatitude = 0d;
    private double workLongitude = 0d;

    private LocationData() {
    }

    private static class singleLocation {
        private static final LocationData SINGLE = new LocationData();
    }

    public static LocationData getInstance() {
        return singleLocation.SINGLE;
    }

    public String getFormatAddress() {
        return formatAddress;
    }

    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

//    public Location getLocation() {
//        return mLocation;
//    }
//
//    public void setLocation(Location mLocation) {
//        this.mLocation = mLocation;
//    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

//    public HomeAddressBean getHomeAddressBean() {
//        if (homeAddressBean == null) {
//            homeAddressBean = (HomeAddressBean) SPUtils.get(AppApplication.getContext(), AppConst.SP_KEY_HOME_ADDRESS);
//        }
//        return homeAddressBean;
//    }
//
//    public void setHomeAddressBean(HomeAddressBean homeAddressBean) {
//        this.homeAddressBean = homeAddressBean;
//    }
//
//    public ComponyAddressBean getComponyAddressBean() {
//        if (componyAddressBean == null) {
//            componyAddressBean = (ComponyAddressBean) SPUtils.get(AppApplication.getContext(), AppConst.SP_KEY_COMPONY_ADDRESS);
//        }
//        return componyAddressBean;
//    }
//
//    public void setComponyAddressBean(ComponyAddressBean componyAddressBean) {
//        this.componyAddressBean = componyAddressBean;
//    }

    public String getHomeAddressName() {
        return homeAddressName;
    }

    public void setHomeAddressName(String homeAddressName) {
        this.homeAddressName = homeAddressName;
    }

    public double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public boolean isHasHomeAddress() {
        return hasHomeAddress;
    }

    public void setHasHomeAddress(boolean hasHomeAddress) {
        this.hasHomeAddress = hasHomeAddress;
    }

    public String getHomePoiName() {
        return homePoiName;
    }

    public void setHomePoiName(String homePoiName) {
        this.homePoiName = homePoiName;
    }

    public String getWorkPoiName() {
        return workPoiName;
    }

    public void setWorkPoiName(String workPoiName) {
        this.workPoiName = workPoiName;
    }

    public boolean isHasWorkAddress() {
        return hasWorkAddress;
    }

    public void setHasWorkAddress(boolean hasWorkAddress) {
        this.hasWorkAddress = hasWorkAddress;
    }

    public String getWorkAddressName() {
        return workAddressName;
    }

    public void setWorkAddressName(String workAddressName) {
        this.workAddressName = workAddressName;
    }

    public double getWorkLatitude() {
        return workLatitude;
    }

    public void setWorkLatitude(double workLatitude) {
        this.workLatitude = workLatitude;
    }

    public double getWorkLongitude() {
        return workLongitude;
    }

    public void setWorkLongitude(double workLongitude) {
        this.workLongitude = workLongitude;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }
}
