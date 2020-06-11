package com.sitechdev.vehicle.pad.module.video;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/11
 * </pre>
 */
public class VideoManager {
    private static String[] proj_video = new String[]{
            MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE};

    public static List<VideoInfo> getLocalVideos(Context context) {

        List<VideoInfo> infos = new ArrayList<>();
        StringBuilder selection = new StringBuilder();
        selection.append("0=0 and ")
                .append(MediaStore.Video.Media.TITLE)
//                .append("!='' and ")
//                .append(MediaStore.Audio.Media.SIZE)
//                .append(">").append(FILTER_SIZE)
//                .append(" and ")
//                .append(MediaStore.Audio.Media.DURATION)
//                .append(">").append(FILTER_DURATION)
        ;
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj_video, selection.toString(),
                null, "");
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                VideoInfo info = new VideoInfo();
                info.id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                info.title = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Video.Media.TITLE));
                String filePath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Video.Media.DATA));
                info.data = filePath;
                info.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
                info.size = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media.SIZE));
                info.islocal = true;
                info.sort = Pinyin.toPinyin(info.title.charAt(0)).substring(0, 1).toUpperCase();
                infos.add(info);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return infos;
    }
}
