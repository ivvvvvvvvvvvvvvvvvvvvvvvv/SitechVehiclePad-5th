package com.sitechdev.vehicle.pad.module.member.bean;

import java.io.Serializable;

public class MemberInfoBaseBean implements Serializable {
    /**
     * code : 200
     * data : {"memberInfo":{"clubId":1,"clubName":"北京新特车友会","level":2,"memberName":"正式会长","memberType":1,"userId":"105430000012"},"isMember":1}
     * message : success
     */

    private String code;
    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * 会员信息
         * memberInfo : {"clubId":1,"clubName":"北京新特车友会","level":3,"memberName":"金牌会长","memberType":1,"userId":"101340000013"}
         * isMember : 1
         */
        private MemberInfoBean memberInfo;
        /**
         * 是否车友会员: 1是0否
         */
        private int isMember;

        public MemberInfoBean getMemberInfo() {
            return memberInfo;
        }

        public void setMemberInfo(MemberInfoBean memberInfo) {
            this.memberInfo = memberInfo;
        }

        /**
         * true=是车友会会员
         *
         * @return true=是车友会会员
         */
        public boolean isMember() {
            return isMember == 1;
        }

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        public static class MemberInfoBean {
            /**
             * clubId : 1
             * clubName : 北京新特车友会
             * level : 2
             * memberName : 正式会长
             * memberType : 1
             * userId : 105430000012
             */

            private int clubId;
            /**
             * 车友会名称
             */
            private String clubName;
            /**
             * 会员级别:
             * 1：见习会长2正式会长3金牌会长、4王牌会长
             * 1见习会员2白银会员3黄金会员、4钻石会员
             */
            private int level;
            /**
             * 会员名称
             */
            private String memberName;
            /**
             * 会员类型:1会长2会员
             */
            private int memberType;
            private String userId;

            public int getClubId() {
                return clubId;
            }

            public void setClubId(int clubId) {
                this.clubId = clubId;
            }

            public String getClubName() {
                return clubName;
            }

            public void setClubName(String clubName) {
                this.clubName = clubName;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getMemberName() {
                return memberName;
            }

            public void setMemberName(String memberName) {
                this.memberName = memberName;
            }

            public int getMemberType() {
                return memberType;
            }

            public void setMemberType(int memberType) {
                this.memberType = memberType;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
