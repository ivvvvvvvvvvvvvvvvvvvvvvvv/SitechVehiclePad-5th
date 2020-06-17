package com.sitechdev.vehicle.pad.module.video;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.sitechdev.vehicle.pad.module.video.service.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/11
 * </pre>
 */
public class VideoManager {
    /**
     * _id	主键。图片 id，从 1 开始自增
     * _data	图片绝对路径
     * _size	文件大小，单位为 byte
     * _display_name	文件名
     * mime_type	类似于 image/jpeg 的 MIME 类型
     * title	不带扩展名的文件名
     * date_added	添加到数据库的时间，单位秒
     * date_modified	文件最后修改时间，单位秒
     * description	 
     * picasa_id	用于 picasa 网络相册
     * isprivate	 
     * latitude	纬度，需要照片有 GPS 信息
     * longitude	经度，需要照片有 GPS 信息
     * datetaken	取自 EXIF 照片拍摄时间，若为空则等于文件修改时间，单位毫秒
     * orientation	取自 EXIF 旋转角度，在图库旋转图片也会改变此值
     * mini_thumb_magic	取小缩略图时生成的一个随机数，见 MediaThumbRequest
     * bucket_id	等于 path.toLowerCase.hashCode()，见 MediaProvider.computeBucketValues()
     * bucket_display_name	直接包含图片的文件夹就是该图片的 bucket，就是文件夹名
     *
     *
     *
     *thumbnails：缩略图:
     *
     *  _id	主键。缩略图 id，从 1 开始自增
     * _data	图片绝对路径
     * image_id	缩略图所对应图片的 id，依赖于 images 表 _id 字段，可建立外键
     * kind	缩略图类型，1 是大缩略图，2 基本不用，3 是微型缩略图但其信息不保存在数据库
     * width	缩略图宽度
     * height	缩略图高度
     */
    private static String keyThumbnails = "thumbnails";
    private static String[] proj_video = new String[]{
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
           };
    // "(SELECT " + MediaStore.Video.Thumbnails.DATA + " FROM " + MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI + " AS " + MediaStore.Video.Thumbnails.DATA + " WHERE " + MediaStore.Video.Thumbnails.VIDEO_ID
    //                    + "=" + MediaStore.Video.Media._ID + ") AS " + keyThumbnails

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
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj_video, "",
                null, "");
        Cursor cursorThumbnails ;
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
//                info.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
                info.size = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media.SIZE));
//                info.islocal = true;
//                info.sort = Pinyin.toPinyin(info.title.charAt(0)).substring(0, 1).toUpperCase();
                //查询缩略图表
                StringBuilder thumbnailsSelection = new StringBuilder();
                thumbnailsSelection
                        .append(MediaStore.Video.Thumbnails.VIDEO_ID)
                        .append("=")
                        .append(info.id);
                cursorThumbnails = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Video.Thumbnails.DATA}, thumbnailsSelection.toString(),
                        null, "");
                if (null != cursorThumbnails && cursorThumbnails.getCount() > 0) {
                    cursorThumbnails.moveToFirst();
                    info.thumbnails = cursorThumbnails.getString(cursorThumbnails
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                }
                infos.add(info);
            } while (cursor.moveToNext());
            cursor.close();
            if (null != cursorThumbnails) {
                cursorThumbnails.close();
            }
        }
        return infos;
    }
}
