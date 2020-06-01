package com.sitechdev.vehicle.pad.model.feedback;

import java.io.Serializable;

/**
 * @author cold
 * 上传反馈内容
 */
public class FeedbackCommitBean implements Serializable {
    /**
     * "code": "200",
     * "message": "",
     * "data":
     */
    public String code;
    public String data;
    public String message;

    @Override
    public String toString() {
        return "FeedbackCommitBean{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
