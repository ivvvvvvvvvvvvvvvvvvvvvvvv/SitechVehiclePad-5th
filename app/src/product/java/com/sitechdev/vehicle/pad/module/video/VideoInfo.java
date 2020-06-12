package com.sitechdev.vehicle.pad.module.video;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class VideoInfo implements Parcelable {

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SIZE = "size";
    public static final String KEY_DATA = "data";
    public static final String KEY_THUMBNAILS = "thumbnails";

    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ARTIST_ID = "artist_id";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_LRC = "lrc";
    public static final String KEY_ISLOCAL = "islocal";
    public static final String KEY_SORT = "sort";

    /**
     * 数据库中的_id
     */
    public long id = -1;
    public String title;
    public String data;
    public String thumbnails;
    public int size;



    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {

        @Override
        public VideoInfo createFromParcel(Parcel source) {
            VideoInfo videoInfo = new VideoInfo();
            Bundle bundle = new Bundle();
            bundle = source.readBundle();
            videoInfo.id = bundle.getLong(KEY_ID);
            videoInfo.title = bundle.getString(KEY_TITLE);
            videoInfo.thumbnails = bundle.getString(KEY_THUMBNAILS);
//            videoInfo.artist = bundle.getString(KEY_ARTIST);
//            videoInfo.artistId = bundle.getLong(KEY_ARTIST_ID);
            videoInfo.data = bundle.getString(KEY_DATA);
//            videoInfo.folder = bundle.getString(KEY_FOLDER);
            videoInfo.size = bundle.getInt(KEY_SIZE);
//            videoInfo.lrc = bundle.getString(KEY_LRC);
//            videoInfo.islocal = bundle.getBoolean(KEY_ISLOCAL);
//            videoInfo.sort = bundle.getString(KEY_SORT);
            return videoInfo;
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, id);
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_THUMBNAILS, thumbnails);
//        bundle.putString(KEY_ARTIST, artist);
//        bundle.putLong(KEY_ARTIST_ID, artistId);
        bundle.putString(KEY_DATA, data);
//        bundle.putString(KEY_FOLDER, folder);
        bundle.putInt(KEY_SIZE, size);
//        bundle.putString(KEY_LRC, lrc);
//        bundle.putBoolean(KEY_ISLOCAL, islocal);
//        bundle.putString(KEY_SORT, sort);
        dest.writeBundle(bundle);
    }

}