package com.sitechdev.vehicle.pad.bean;

import android.graphics.Bitmap;

import com.sitechdev.net.bean.BaseBean;

/**
 * @author 邵志
 * @version 2020/06/11 0011 18:34
 * @name SitechMusicBean
 * @description
 */
public class SitechMusicBean extends BaseBean {
    private String name = "";
    private String author = "";
    private double duration = 0d;
    /**
     * 播放状态，true=当前正在播放
     */
    private boolean playStatus = false;
    private Bitmap iconBitmap = null;
    private String iconBitmapUrl = "";
    /**
     * 总长度
     */
    private double totalLength = 0d;
    /**
     * 已播放长度
     */
    private double progressLength = 0d;

    @Override
    public String toString() {
        return "SitechMusicBean{" +
                "name='" + name +
                ", author='" + author +
                ", duration=" + duration +
                ", playStatus=" + playStatus +
                ", iconBitmap=" + iconBitmap +
                ", totalLength=" + totalLength +
                ", progressLength=" + progressLength +
                "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(boolean playStatus) {
        this.playStatus = playStatus;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    public String getIconBitmapUrl() {
        return iconBitmapUrl;
    }

    public void setIconBitmapUrl(String iconBitmapUrl) {
        this.iconBitmapUrl = iconBitmapUrl;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }

    public double getProgressLength() {
        return progressLength;
    }

    public void setProgressLength(double progressLength) {
        this.progressLength = progressLength;
    }
}
