package com.sitechdev.vehicle.pad.model.phone;

public class Contact {

    private String abbr;
    private String name;
    private String phoneNum;
    private int type;

    public Contact(String abbr, String name, String phoneNum) {
        this.abbr = abbr;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public Contact(String abbr, String name, String phoneNum, int type) {
        this.abbr = abbr;
        this.name = name;
        this.phoneNum = phoneNum;
        this.type = type;
    }

    public Contact(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getAbbr() {
        return this.abbr;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNum;
    }

    public int getType() {
        return this.type;
    }
}