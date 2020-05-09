package com.sitechdev.vehicle.pad.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 全部应用中的工具类
 *
 * @author bijingshuai
 * @date 2019/4/12
 */
public class AllModuleUtils {

    private static final String FILE_NAME = "modulename/main_all_app.json";

    //将Assets文件夹下的json数据变成字符串
    public static String getAssetsJson2String(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(FILE_NAME)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    /**
     * 将json中的appIcon转化成资源id
     *
     * @param appIcon
     * @return
     */
    public static int getSrcId(int appIcon) {
        int resId = 0;// R.drawable.all_tab_info;
//        switch (appIcon) {
//            case 1:             //本地音乐
//                resId = R.drawable.all_tab_usb;
//                break;
//            case 2:             //收音机
//                resId = R.drawable.all_tab_fm;
//                break;
//            case 3:             //听伴
//                resId = R.drawable.all_tab_kaola
//                ;
//                break;
//            case 4:             //电话
//                resId = R.drawable.all_tab_phone;
//                break;
//            case 5:             //车辆控制
//                resId = R.drawable.all_tab_car;
//                break;
//            case 6:             //车主服务
//                resId = R.drawable.all_tab_car_user;
//                break;
//            case 7:             //空调控制
//                resId = R.drawable.all_tab_air;
//                break;
//            case 8:             //商城
//                resId = R.drawable.all_tab_shop;
//                break;
//            case 9:             //消息盒子
//                resId = R.drawable.all_tab_message;
//                break;
//            case 10:            //天气
//                resId = R.drawable.all_tab_weather;
//                break;
//            case 11:            //设置
//                resId = R.drawable.all_tab_setting;
//                break;
//            case 12:            //个人中心
//                resId = R.drawable.all_tab_info;
//                break;
//            case 13:            //视频
//                resId = R.drawable.all_tab_video;
//                break;
//            case 14:            //酷我音乐
//                resId = R.drawable.all_tab_kuwo;
//                break;
//            case 15:            //地图导航
//                resId = R.drawable.all_tab_map;
//                break;
//            case 16:            //百度CarLife
//                resId = R.drawable.all_tab_carlife;
//                break;
//            case 17:            //个人中心
//                resId = R.drawable.all_tab_info;
//                break;
//            case 18:            //智能家居
//                resId = R.drawable.all_tab_home;
//                break;
//			case 19:            //饭聊
//                resId = R.drawable.all_tab_funchat;
//                break;
//        }
        return resId;
    }
}
