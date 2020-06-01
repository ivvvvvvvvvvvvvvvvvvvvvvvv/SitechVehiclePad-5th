package com.sitechdev.vehicle.pad.model.feedback;

import java.io.Serializable;
import java.util.List;

/**
 * {
 * "code": "200",
 * "data": {
 * "count": 1,
 * "feedbackList": [
 * {
 * "auditBy": "admin",
 * "auditStatus": 1,
 * "auditTime": 1565532268000,
 * "contentUrl": "https://cdn5.sitechdev.com/voice/2019/08/12/测试音频.mp3",  //语音url
 * "createBy": "",
 * "createTime": 1565341713000,      //创建时间
 * "id": 1,
 * "isDelete": 0,
 * "processRemark": "批准",
 * "updateBy": "",
 * "updateTime": null,
 * "userId": "104150000024",
 * "vehicleConfig": "DEV1 创睿版",
 * "version": "20180806",
 * "vin": "XTEVE000000A00007"
 * }
 * ]
 * },
 * "message": ""
 * }
 */

public class FeedbackHistoryBean implements Serializable {

    public String code;
    public String message;
    public FeedbackDataBean data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackDataBean getData() {
        return data;
    }

    public class FeedbackDataBean {
        public String getCount() {
            return count;
        }

        public List<FeedbackItemBean> getFeedbackList() {
            return feedbackList;
        }

        public String count;
        public List<FeedbackItemBean> feedbackList;

        public class FeedbackItemBean {

            @Override
            public String toString() {
                return "FeedbackItemBean{" +
                        "auditBy='" + auditBy + '\'' +
                        ", auditStatus='" + auditStatus + '\'' +
                        ", auditTime='" + auditTime + '\'' +
                        ", contentUrl='" + contentUrl + '\'' +
                        ", createBy='" + createBy + '\'' +
                        ", createTime='" + createTime + '\'' +
                        ", id='" + id + '\'' +
                        ", isDelete='" + isDelete + '\'' +
                        ", processRemark='" + processRemark + '\'' +
                        ", updateBy='" + updateBy + '\'' +
                        ", updateTime='" + updateTime + '\'' +
                        ", userId='" + userId + '\'' +
                        ", vehicleConfig='" + vehicleConfig + '\'' +
                        ", version='" + version + '\'' +
                        ", vin='" + vin + '\'' +
                        ", voiceLength='" + voiceLength + '\'' +
                        '}';
            }

            public String auditBy;
            public String auditStatus;
            public String auditTime;
            public String contentUrl;//语音url
            public String createBy;
            public String createTime;//创建时间
            public String id;
            public String isDelete;
            public String processRemark;
            public String updateBy;
            public String updateTime;
            public String userId;
            public String vehicleConfig;
            public String version;
            public String vin;
            public String voiceLength;

            public String getVoiceLength() {
                return voiceLength;
            }

            public String getAuditBy() {
                return auditBy;
            }

            public String getAuditStatus() {
                return auditStatus;
            }

            public String getAuditTime() {
                return auditTime;
            }

            public String getContentUrl() {
                return contentUrl;
            }

            public String getCreateBy() {
                return createBy;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getId() {
                return id;
            }

            public String getIsDelete() {
                return isDelete;
            }

            public String getProcessRemark() {
                return processRemark;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public String getUserId() {
                return userId;
            }

            public String getVehicleConfig() {
                return vehicleConfig;
            }

            public String getVersion() {
                return version;
            }

            public String getVin() {
                return vin;
            }
        }
    }

}
