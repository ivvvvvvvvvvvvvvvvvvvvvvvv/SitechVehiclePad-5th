package com.sitechdev.vehicle.pad.module.member.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户反馈类型
 *
 * @author bijingshuai
 * @date 2019/8/21
 */
public class FeedBackTypeBean implements Serializable {
    private String code;
    private String message;
    private BackTypeBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BackTypeBean getData() {
        return data;
    }

    public void setData(BackTypeBean data) {
        this.data = data;
    }

    public class BackTypeBean {
        private List<TypeBean> typeList;
        private int id;

        public class TypeBean {
            private int id;
            private String name;
            private String type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public List<TypeBean> getTypeList() {
            return typeList;
        }

        public void setTypeList(List<TypeBean> typeList) {
            this.typeList = typeList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
