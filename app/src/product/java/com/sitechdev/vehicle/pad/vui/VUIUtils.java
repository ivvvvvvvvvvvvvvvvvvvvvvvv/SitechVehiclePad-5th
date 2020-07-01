package com.sitechdev.vehicle.pad.vui;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;

import com.amap.api.services.core.PoiItem;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.manager.KuwoManager;
import com.sitechdev.vehicle.pad.vui.bean.SpeakableSyncData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhubaoqiang
 * @date 2019/8/21
 */
public class VUIUtils {
    public static void callPhone(String phoneUumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneUumber);
        intent.setData(data);
        AppApplication.getContext().startActivity(intent);
    }

    public static void goContacts() {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);

        intent.setData(ContactsContract.Contacts.CONTENT_URI);

        AppApplication.getContext().startActivity(intent);
    }

    public static String getDynamicNaviPoiObject(List<PoiItem> paramsList) {
        JSONObject poiObject = new JSONObject();
        StringBuilder poiSbu = new StringBuilder();
        try {
            for (int i = 0; i < paramsList.size(); i++) {
                PoiItem poiItem = paramsList.get(i);
                poiObject.put("naviPoiName", poiItem.getTitle());
                poiObject.put("naviPoiIndex", i);
                poiObject.put("naviPoiLat", poiItem.getLatLonPoint().getLatitude());
                poiObject.put("naviPoiLng", poiItem.getLatLonPoint().getLongitude());
                poiSbu.append(poiObject.toString()).append("\r\n");
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return poiSbu.toString();
    }

    private static SpeakableSyncData syncNaviData(List<PoiItem> paramsList) {
        // 构造所见即可说数据
        StringBuilder speakableData = new StringBuilder();
        JSONObject poiObject = new JSONObject();
        try {
            for (int i = 0; i < paramsList.size(); i++) {
                PoiItem poiItem = paramsList.get(i);
                poiObject.put("naviPoiName", poiItem.getTitle());
                poiObject.put("naviPoiIndex", i);
                poiObject.put("naviPoiLat", poiItem.getLatLonPoint().getLatitude());
                poiObject.put("naviPoiLng", poiItem.getLatLonPoint().getLongitude());
                speakableData.append(poiObject.toString()).append("\n");
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }

        SpeakableSyncData speakableSyncData = new SpeakableSyncData(
                "SITECHAI.SITECH_NAVI_INDEX", "SITECHAI.sitechdev_navi", speakableData.toString());

//        // 进行所见即可说同步操作
        return speakableSyncData;
//        // 设置pers_params 生效所见即可说，生效详情参考接入文档中动态实体生效使用一节
//        mMessageViewModel.putPersParam("uid", "");
//        // 生成提示语
//        mMessageViewModel.fakeAIUIResult(0,
//                "FOOBAR.DishSkill", "同步成功后通过 xxx怎么做 查询具体做法");
    }

    /**
     * 同步所见即可说
     *
     * @param paramsList 所见即可说数据
     */
    public static AIUIMessage syncSpeakableData(List<PoiItem> paramsList) {
        try {
            SpeakableSyncData data = syncNaviData(paramsList);

            JSONObject syncSpeakableJson = new JSONObject();

            //从所见即可说数据中根据key获取识别热词信息
            List<String> hotWords = new ArrayList<>();
            String[] dataItems = data.speakableData.split("\r?\n");
            for (String item : dataItems) {
                JSONObject dataItem = new JSONObject(item);
                Iterator<String> hotKeysIterator;
                if (data.masterKey == null) {
                    hotKeysIterator = dataItem.keys();
                } else {
                    List<String> hotKeys = new ArrayList<>();
                    hotKeys.add(data.masterKey);
                    hotKeys.add(data.subKeys);
                    hotKeysIterator = hotKeys.iterator();
                }

                while (hotKeysIterator.hasNext()) {
                    String hotKey = hotKeysIterator.next();
                    hotWords.add(dataItem.getString(hotKey));
                }
            }

            // 识别用户数据
            JSONObject iatUserDataJson = new JSONObject();
            iatUserDataJson.put("recHotWords", TextUtils.join("|", hotWords));
            iatUserDataJson.put("sceneInfo", new JSONObject());
            syncSpeakableJson.put("iat_user_data", iatUserDataJson);

            // 语义理解用户数据
            JSONObject nlpUserDataJson = new JSONObject();

            JSONArray resArray = new JSONArray();
            JSONObject resDataItem = new JSONObject();
            resDataItem.put("res_name", data.resName);
            resDataItem.put("data", Base64.encodeToString(
                    data.speakableData.getBytes(), Base64.NO_WRAP));
            resArray.put(resDataItem);

            nlpUserDataJson.put("res", resArray);
            nlpUserDataJson.put("skill_name", data.skillName);

            syncSpeakableJson.put("nlp_user_data", nlpUserDataJson);

            // 传入的数据一定要为utf-8编码
            byte[] syncData = syncSpeakableJson.toString().getBytes("utf-8");

            AIUIMessage syncAthenaMessage = new AIUIMessage(AIUIConstant.CMD_SYNC,
                    AIUIConstant.SYNC_DATA_SPEAKABLE, 0, "", syncData);
            return syncAthenaMessage;
//            sendMessage(syncAthenaMessage);
        } catch (Exception e) {
            //mAIUIRepo.fakeAIUIResult(0, Constant.SERVICE_SPEAKABLE, String.format("同步所见即可说数据出错 %s", e.getMessage()), null, null);
            SitechDevLog.exception(e);
            return null;
        }
    }

    /**
     * 打开第三方APP搜索歌曲并播放
     *
     * @param musicName 歌曲名称
     * @param singer    歌手名称
     */
    public static void openThirdAppByMusic(String musicName, String singer) {
        SitechDevLog.i(AppConst.TAG, "openThirdAppByMusic=====>musicName=" + musicName + "====>singer=" + singer);
//        EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_APP_BACKGROUD));
        KuwoManager.getInstance().playMusic(musicName, singer);
    }
}
