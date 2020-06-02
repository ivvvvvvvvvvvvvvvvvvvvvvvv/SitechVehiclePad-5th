package com.sitechdev.vehicle.pad.module.apps.model;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.sitechdev.vehicle.lib.util.ParamsUtil;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;

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
    /**
     * 移动item动画结束状态
     */
    public static final String KEY_SAVE_MENU_DATA = "KEY_SAVE_MENU_DATA";

    /**
     * 获取保存的菜单数据
     *
     * @return 菜单数据
     */
    public static String getSavedMenuData() {
        try {
            //从持久化保存的数据中获取
            String localMenuData = ParamsUtil.getStringData(KEY_SAVE_MENU_DATA);
//            SitechDevLog.w(AppConst.TAG_APP, "读取的最新的菜单数据为=====" + localMenuData);
            return localMenuData;
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return "";
    }

    /**
     * 保存最新的菜单数据
     *
     * @return 菜单数据
     */
    public static void saveNewMenuData() {
        Gson mGson = new Gson();
        String menuNewJson = mGson.toJson(AppsMenuConfig.mAllMenuBean);
//        SitechDevLog.w(AppConst.TAG_APP, "最新的菜单数据为：" + menuNewJson);
        if (StringUtils.isEmpty(menuNewJson)) {
            SitechDevLog.w(AppConst.TAG_APP, "最新的菜单数据读取为空，无法保存");
            return;
        }

        //持久化保存
        ParamsUtil.setData(KEY_SAVE_MENU_DATA, menuNewJson);
    }

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
        int resId = R.drawable.icon_app_menu_navi;
        switch (appIcon) {
            case 1:             //本地音乐
                resId = R.drawable.icon_app_menu_navi;
                break;
            case 2:             //收音机
                resId = R.drawable.icon_app_menu_radio;
                break;
            case 3:             //本地音乐
                resId = R.drawable.icon_app_menu_local_music;
                break;
            case 4:             //网络音乐
                resId = R.drawable.icon_app_menu_kuwo;
                break;
            case 5:             //听伴FM
                resId = R.drawable.icon_app_menu_kaola;
                break;
            case 6:             //LED表情管理
                resId = R.drawable.icon_app_menu_led;
                break;
            case 7:             //视频
                resId = R.drawable.icon_app_menu_video;
                break;
            case 8:             //天气
                resId = R.drawable.icon_app_menu_weather;
                break;
            case 9:             //电话
                resId = R.drawable.icon_app_menu_phone;
                break;
            case 10:            //个人中心
                resId = R.drawable.icon_app_menu_member;
                break;
            case 11:            //饭聊
                resId = R.drawable.icon_app_menu_funchat;
                break;
            case 12:            //出行计价器
                resId = R.drawable.icon_app_menu_taxi;
                break;
            case 13:            //商城
                resId = R.drawable.icon_app_menu_mall;
                break;
            case 14:            //设置
                resId = R.drawable.icon_app_menu_setting;
                break;
            default:
                break;
        }
        return resId;
    }
}