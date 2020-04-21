package com.sitechdev.vehicle.pad.module.member.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户反馈上传的实体类
 *
 * @author bijingshuai
 * @date 2019/8/21
 */
public class FeedBackCommitBean implements Serializable {
    private Long userId;
    private byte feedbackType;
    private int platformId;
    private String content;
    private List<String> imgFile;

    public FeedBackCommitBean() {
    }

    public FeedBackCommitBean(Long userId, byte feedbackType, int platformId, String content, List<String> imgFile) {
        this.userId = userId;
        this.feedbackType = feedbackType;
        this.platformId = platformId;
        this.content = content;
        this.imgFile = imgFile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(byte feedbackType) {
        this.feedbackType = feedbackType;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgFile() {
        return imgFile;
    }

    public void setImgFile(List<String> imgFile) {
        this.imgFile = imgFile;
    }
}
