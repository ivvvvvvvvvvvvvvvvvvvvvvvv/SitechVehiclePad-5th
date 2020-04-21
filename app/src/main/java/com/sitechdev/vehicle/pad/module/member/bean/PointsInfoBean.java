package com.sitechdev.vehicle.pad.module.member.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Sitech Eugene Tuladhar
 * 2019-4-20
 */

public class PointsInfoBean implements Serializable {

    /**
     * "code": "200",
     * "message": "",
     * "data":
     */
    public String code;
    public String message;
    public PointsDataBean data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public PointsDataBean getData() {
        return data;
    }

    /**
     * "total":30,
     * "list":{
     * [
     * {
     * "createTime": 1524792738000,
     * "integral": 30,  //积分数，负数为退回积分
     * "integralId": 51114,  //数据唯一标识
     * "integralType": 1,  //类型，类型0=注册1=签到2=预约3=预订4=抽奖5=预订退款。。。
     * "integralTypeId": "0",  //类型标识，根据类型保存对应标识，如注册存0，预约预订存订单号，抽奖存抽奖活动号，退款存订单号或抽奖活动号
     * "remark": "签到赠送积分",  //说明
     * "userId": "100670010038"
     * },
     * {
     * "createTime": 1524672956000,
     * "integral": 30,
     * "integralId": 51085,
     * "integralType": 1,
     * "integralTypeId": "0",
     * "remark": "签到赠送积分",
     * "userId": "100670010038"
     * }
     * ]
     * }
     */

    public static class PointsDataBean {

        private String total;
        private List<PointsListBean> list;

        public String getTotal() {
            return total;
        }

        public List<PointsListBean> getPointsList() {
            return list;
        }

        /**
         * "createTime": 1524792738000,
         * "integral": 30,  //积分数，负数为退回积分
         * "integralId": 51114,  //数据唯一标识
         * "integralType": 1,  //类型，类型0=注册1=签到2=预约3=预订4=抽奖5=预订退款。。。
         * "integralTypeId": "0",  //类型标识，根据类型保存对应标识，如注册存0，预约预订存订单号，抽奖存抽奖活动号，退款存订单号或抽奖活动号
         * "remark": "签到赠送积分",  //说明
         * "userId": "100670010038"
         */

        public static class PointsListBean {

            private String remark;
            private String createTime;
            private String integral;

            public String getCreateTime() {
                return createTime;
            }

            public String getRemark() {
                return remark;
            }

            public String getIntegral() {
                return integral;
            }
        }

    }
}
