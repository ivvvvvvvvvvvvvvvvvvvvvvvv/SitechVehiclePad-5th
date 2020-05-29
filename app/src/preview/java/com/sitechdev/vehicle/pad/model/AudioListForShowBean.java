package com.sitechdev.vehicle.pad.model;

import java.io.Serializable;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/6
 * </pre>
 */
public class AudioListForShowBean implements AudioListBaseBean, Serializable {
    private int img;
    private String title;
    private String subTitle;

    public void setImg(int img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String getImageUrl() {
        return "";
    }

    @Override
    public int getImageResource() {
        return img;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubTitle() {
        return subTitle;
    }
}
