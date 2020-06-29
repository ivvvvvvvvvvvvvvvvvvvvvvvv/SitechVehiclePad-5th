package com.sitechdev.vehicle.pad.model.phone;

public class CallLog {

    private int type;
    private String name;
    private String phoneNum;
    private String date = "";
    private String time = "";

    public CallLog(int type, String name, String phoneNum, String date, String time) {
        this.type = type;
        this.name = name;
        this.phoneNum = phoneNum;
        this.date = date;
        this.time = time;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNum;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }
}