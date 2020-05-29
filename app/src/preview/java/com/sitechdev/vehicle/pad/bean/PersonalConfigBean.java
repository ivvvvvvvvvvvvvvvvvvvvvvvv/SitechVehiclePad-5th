package com.sitechdev.vehicle.pad.bean;

/**
 * 项目名称：HZ_SitechDOS
 * 类名称：PersonalConfigBean
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2018/11/26 0026 21:12
 * 修改时间：
 * 备注：
 */
public class PersonalConfigBean {

    /**
     * code : 200
     * message :
     * data : {"welcome":"欢迎您","sex":"男","ac_time":30,"fault_switch":0}
     */

    private String code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * welcome : 欢迎您
         * sex : 男
         * ac_time : 30
         * fault_switch : 0
         */
        private String welcome;
        private String sex;
        private int ac_time;
        private int fault_switch;
        public int delMqttRes;
        private String dev_password;

        public String getWelcome() {
            return welcome;
        }

        public void setWelcome(String welcome) {
            this.welcome = welcome;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAc_time() {
            return ac_time;
        }

        public void setAc_time(int ac_time) {
            this.ac_time = ac_time;
        }

        /**
         * 故障报警开关
         *
         * @return 0关闭报警，1开启报警
         */
        public int getFault_switch() {
            return fault_switch;
        }

        public void setFault_switch(int fault_switch) {
            this.fault_switch = fault_switch;
        }

        public String getDev_password() {
            return dev_password;
        }

        public void setDev_password(String dev_password) {
            this.dev_password = dev_password;
        }
    }
}
