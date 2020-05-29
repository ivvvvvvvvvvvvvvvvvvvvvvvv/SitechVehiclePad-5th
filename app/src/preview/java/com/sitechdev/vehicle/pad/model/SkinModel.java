package com.sitechdev.vehicle.pad.model;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：SkinModel
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/06 0006 19:13
 * 修改时间：
 * 备注：
 */
public enum SkinModel {
    /**
     * 默认皮肤
     */
    SKIN_DEFAULT("", "默认皮肤"),
    /**
     * 白橘色皮肤
     */
    SKIN_WHITE_ORANGE("whiteorange", "白橘色皮肤"),
    /**
     * 蓝橘色皮肤
     */
    SKIN_BLUE_ORANGE("blueorange", "蓝橘色皮肤");

    private String skinTag = "";
    private String skinDesc = "";

    private SkinModel(String tag, String desc) {
        skinTag = tag;
        skinDesc = desc;
    }

    public String getSkinTag() {
        return skinTag;
    }

    public String getSkinDesc() {
        return skinDesc;
    }
    // 根据value返回枚举类型,主要在switch中使用
    public static SkinModel getByName(String value) {
        for (SkinModel code : values()) {
            if (value.equalsIgnoreCase(code.getSkinTag())) {
                return code;
            }
        }
        return null;
    }

}
