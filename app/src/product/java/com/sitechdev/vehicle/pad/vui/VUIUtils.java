package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;

import com.amap.api.services.core.PoiItem;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.UserWords;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.manager.KuwoManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.phone.PhoneBtManager;
import com.sitechdev.vehicle.pad.vui.bean.SpeakableSyncData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhubaoqiang
 * @date 2019/8/21
 */
public class VUIUtils {
    public static void callPhone(String phoneUumber) {
        PhoneBtManager.getInstance().diaTo(phoneUumber);
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
    /**
     * 打开第三方APP搜索歌曲并播放
     *
     */
    public static void playRandomMusic() {
        VoiceSourceManager.getInstance().changeAnother(VoiceSourceManager.VOICE);
    }

    /**
     * @param  context - 词典名称，{ "userword", "contact" }。
     *
     * @param  lexiconContent - 词典内容，联系人列表：以换行符"\n"分隔的词语字符串，如"张三，李四，王五"; 用户热词：满足一定格式的json字符串，格式如下：
     *                        {
     *   "userword": [
     *     {
     *       "name": "default",
     *       "words": [
     *         "默认词条1",
     *         "默认词条2"
     *       ]
     *     },
     *     {
     *       "name": "词表名称1",
     *       "words": [
     *         "词条1的第一个词",
     *         "词条1的第二个词"
     *       ]
     *     },
     *     {
     *       "name": "词表名称2",
     *       "words": [
     *         "词条2的第一个词",
     *         "词条2的第二个词"
     *       ]
     *     }
     *   ]
     * }
     */
    public static void updataUserWordsContact(Context context, Map<String, ArrayList<String>> lexiconContent) {
        UserWords userWords = new UserWords();
        Iterator<String> key = lexiconContent.keySet().iterator();
        while (key.hasNext()) {
            String keyStr = key.next();
            ArrayList<String> values = lexiconContent.get(keyStr);
            userWords.putWords(keyStr, values);
        }
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createRecognizer(context, i -> {
            if(i != ErrorCode.SUCCESS){
                SitechDevLog.i("updataUserWordsContact onInit not success");
            }
        });
        /**
         *  updateLexicon (词典名称,词典内容)
         *  UserWords toString 固定词典名称 userword
         *
         */
        if(null != speechRecognizer)
        speechRecognizer.updateLexicon("userword", userWords.toString(), (s, speechError) -> {
            if(null != speechError) {
                SitechDevLog.i("updataUserWordsContact onInit not success, code:"+speechError.getErrorCode()+" des:"+speechError.getErrorDescription());
            }
        });
    }

}
