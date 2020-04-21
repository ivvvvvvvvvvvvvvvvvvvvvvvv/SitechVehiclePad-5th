package com.sitechdev.vehicle.pad.module.music.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhubaoqiang
 * @date 2019/8/26
 */
public class IPCResult implements Parcelable {
    
    public int code;
    public String msg;
    public static final int SUCCESS = 0;

    public IPCResult() {
    }

    public IPCResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected IPCResult(Parcel in) {
        code = in.readInt();
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IPCResult> CREATOR = new Creator<IPCResult>() {
        @Override
        public IPCResult createFromParcel(Parcel in) {
            return new IPCResult(in);
        }

        @Override
        public IPCResult[] newArray(int size) {
            return new IPCResult[size];
        }
    };
}
