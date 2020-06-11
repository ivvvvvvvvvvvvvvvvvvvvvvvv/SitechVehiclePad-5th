package com.sitechdev.vehicle.pad.module.setting;

/**
 * @author zhubaoqiang
 * @date 2019/9/23
 */

public class TTSConfig {
    private String show;
    private String value;
    private int id;

    public TTSConfig() {
    }

    public TTSConfig(String show, String value, int id) {
        this.show = show;
        this.value = value;
        this.id = id;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "TTSConfig{" +
                "show='" + show + '\'' +
                ", value='" + value + '\'' +
                ", id=" + id +
                '}';
    }
}
