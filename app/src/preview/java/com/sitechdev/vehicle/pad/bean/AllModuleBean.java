package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 全部应用中的实体
 *
 * @author bijingshuai
 * @date 2019/4/12
 */
public class AllModuleBean implements Serializable {

    private static final long serialVersionUID = -15L;
    public List<ModuleBean> apps;

    public class ModuleBean {
        /**
         * 应用名称
         */
        public String appId;
        /**
         * 应用名称
         */
        public int appIndex;
        /**
         * 应用名称
         */
        public String appName;
        /**
         * 应用icon
         */
        public int appIcon;
        /**
         * 跳转路径
         */
        public String appRoute;
        /**
         * 跳转标识
         * 0->跳转本地页面
         * 1->跳转三方应用，具体跳转页面根据appRoute判断
         * 2->window展示，eg:空调
         */
        public int jumpType;

        /**
         * 埋点id
         */
        public String eventId;
    }
}
