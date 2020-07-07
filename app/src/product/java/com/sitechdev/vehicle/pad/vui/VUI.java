package com.sitechdev.vehicle.pad.vui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.lib.event.AppEvent;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.event.PoiEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.KuwoManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.module.map.SetAddressActivity;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.music.MusicMainActivity;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConfig;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.util.AudioUtil;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;
import com.sitechdev.vehicle.pad.window.view.PersonLoginWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Random;

import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_AUDIOBOOK;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_CROSSTALK;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_DRAMA;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_NOVEL;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_NURSERYRHYME;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_STORY;
import static com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants.XF_SKILL_TAG_STORYTELLING;

/**
 * @author zhubaoqiang
 * @date 2019/8/12
 */
public class VUI implements VUIWindow.OnWindowHideListener {

    private Context context;
    private static final String TAG = VoiceConstants.TEDDY_TAG;
    private WakeupEngine mWakeupEngine;
    private AIUIEngine mAIUIEngine;
    private TTS mTTS;
    private int mAIUIState = -1;
    private boolean isWakeupTTS = false;
    //    private WeakReference<Activity> topActivity;
    private String mSyncSid;
    private String mSyncSid1;
    //    private OnVolumeChangeListener onVolumeChangeListener;
    //    private VUIDialogFragment vuiDialog;
    private VUIWindow vuiWindow;
    private boolean vadBos = false;
    private boolean isInTTS = false;
    private boolean isUIHide = false;
    private LinkedList<Activity> activities;
    private JSONArray calls;
    private boolean shutdown = false;

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            log("WakeuperListener onResult");
            if (!TeddyConfig.getAutoMVWStatus()) {
                VUI.log("onVoiceWakeup-->不允许通过语音唤醒");
                return;
            }
            EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_MVW_SUCCESS, VoiceConstants.TTS_RESPONSE_DEFAULT_TEXT));
//            onVoiceWakeup();
        }

        @Override
        public void onError(SpeechError error) {
            log("WakeuperListener onError" +
                    error.getErrorCode() + "-----" +
                    error.getErrorDescription());
        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:

                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
//            log("WakeuperListener onVolumeChanged=========" + volume);
        }
    };

    /**
     * 唤醒
     */
    private void onVoiceWakeup() {
        onVoiceWakeup("");
    }

    /**
     * 唤醒
     */
    private void onVoiceWakeup(String ttsText) {
        if (!TeddyWindowManager.getInstance().isViewShow()) {
            TeddyWindowManager.getInstance().show();
        }
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (AIUIConstant.STATE_READY != mAIUIState) {
            //TODO
            VUI.log("AIUIAgent -> NOT READY");
            return;
        }
        if (null == vuiWindow) {
            vuiWindow = VUIWindow.getInstance();
            vuiWindow.setOnWindowHideListener(VUI.this::onWindowHide);
        }
        VoiceSourceManager.getInstance().pause(VoiceSourceManager.CONTENT);
        mWakeupEngine.stop();
        mAIUIEngine.ttsStart(StringUtils.isEmpty(ttsText) ? VoiceConstants.TTS_RESPONSE_DEFAULT_TEXT : ttsText);
        isWakeupTTS = true;
        shutdown = false;
    }

    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    log("已连接服务器");
                    break;
                case AIUIConstant.EVENT_SERVER_DISCONNECTED:
                    log("与服务器断连");
                    break;
                case AIUIConstant.EVENT_PRE_SLEEP:
                    log("EVENT_PRE_SLEEP");
                    break;
                case AIUIConstant.EVENT_SLEEP:
                    log("EVENT_SLEEP");
                    break;
                case AIUIConstant.EVENT_WAKEUP:
                    log("进入识别状态");
                    break;
                case AIUIConstant.EVENT_RESULT:
                    log("EVENT_RESULT");
                    handleEventResult(event);
                    break;
                case AIUIConstant.EVENT_ERROR:
                    log("错误: " + event.arg1 + "\n" + event.info);
//                    CommonToast.showToast("语音功能暂不可用");
//                    if (null != vuiWindow) {
//                        vuiWindow.hide();
//                    }
                    shutdown = true;
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_OVER));
                    break;
                case AIUIConstant.EVENT_VAD:
                    log("EVENT_VAD");
                    handleEventVad(event);
                    break;
                case AIUIConstant.EVENT_START_RECORD:
                    log("已开始录音");
                    vadBos = false;
//                    if (null != vuiWindow.getCurrentHolder()) {
//                        if (vuiWindow.getCurrentHolder() instanceof ContactsHolder) {
//                            break;
//                        }
//                    }
//                    vuiWindow.show();
//                    vuiWindow.showText(context.getResources().getString(R.string.vui_welcome_text));
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_START_SR));
                    break;
                case AIUIConstant.EVENT_STOP_RECORD:
                    log("已停止录音");
                    break;
                case AIUIConstant.EVENT_STATE:    // 状态事件EVENT_VAD
                    log("EVENT_STATE");
                    handleEventState(event);
                    break;
                case AIUIConstant.EVENT_CMD_RETURN:
                    log("EVENT_CMD_RETURN");
                    handleEventCmdDReturn(event);
                    break;
                case AIUIConstant.EVENT_TTS:
                    log("EVENT_TTS");
                    handleTTSEvent(event);
                    break;
                default:
                    break;
            }
        }

    };

    private void handleEventCmdDReturn(AIUIEvent event) {
        if (AIUIConstant.CMD_SYNC == event.arg1) {    // 数据同步的返回
            int dtype = event.data.getInt("sync_dtype", -1);
            int retCode = event.arg2;

            switch (dtype) {
                case AIUIConstant.SYNC_DATA_SCHEMA: {
                    if (AIUIConstant.SUCCESS == retCode) {
                        // 上传成功，记录上传会话的sid，以用于查询数据打包状态
                        // 注：上传成功并不表示数据打包成功，打包成功与否应以同步状态查询结果为准，数据只有打包成功后才能正常使用
                        mSyncSid = event.data.getString("sid");
                        // 获取上传调用时设置的自定义tag
                        String tag = event.data.getString("tag");
                        log("上传成功，sid=" + mSyncSid + "，tag=" + tag);
                        mAIUIEngine.syncQuery(mSyncSid);
                    } else {
                        mSyncSid = "";
                        log("上传失败，错误码：" + retCode);
                    }
                }
                break;
                case AIUIConstant.SYNC_DATA_SPEAKABLE:
                    if (AIUIConstant.SUCCESS == retCode) {
                        // 上传成功，记录上传会话的sid，以用于查询数据打包状态
                        // 注：上传成功并不表示数据打包成功，打包成功与否应以同步状态查询结果为准，数据只有打包成功后才能正常使用
                        mSyncSid1 = event.data.getString("sid");
                        // 获取上传调用时设置的自定义tag
                        String tag = event.data.getString("tag");
                        log("上传成功，sid=" + mSyncSid + "，tag=" + tag);
                        mAIUIEngine.syncQuery(mSyncSid1);
                    } else {
                        mSyncSid1 = "";
                        log("上传失败，错误码：" + retCode);
                    }
                    break;
            }
        } else if (AIUIConstant.CMD_QUERY_SYNC_STATUS == event.arg1) {    // 数据同步状态查询的返回
            // 获取同步类型
            int syncType = event.data.getInt("sync_dtype", -1);
            if (AIUIConstant.SYNC_DATA_QUERY == syncType) {
                // 若是同步数据查询，则获取查询结果，结果中error字段为0则表示上传数据打包成功，否则为错误码
                String result = event.data.getString("result");
                JSONObject jsResult = null;
                try {
                    jsResult = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != jsResult) {
                    int error = jsResult.optInt("error", -1);
                    if (error != 0) {
                        mAIUIEngine.syncQuery(mSyncSid);
                    }
                }
                log("CMD_QUERY_SYNC_STATUS" + result);
            }
        }
    }

    private void handleEventState(AIUIEvent event) {
        mAIUIState = event.arg1;
        if (AIUIConstant.STATE_IDLE == mAIUIState) {
            // 闲置状态，AIUI未开启
            log("STATE_IDLE");
        } else if (AIUIConstant.STATE_READY == mAIUIState) {
            // AIUI已就绪，等待唤醒
            log("STATE_READY");
            if (!vadBos) {
                if (null != vuiWindow) {
                    mAIUIEngine.stopRecord();
                    vuiWindow.hide();
                }
            }
        } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
            // AIUI工作中，可进行交互
            log("STATE_WORKING");
        }
    }

    private void handleEventVad(AIUIEvent event) {
        if (AIUIConstant.VAD_BOS == event.arg1) {
            log("找到vad_bos");
            vadBos = true;
        } else if (AIUIConstant.VAD_EOS == event.arg1) {
            log("找到vad_eos");
        } else if (AIUIConstant.VAD_VOL == event.arg1) {
//            log("找到VAD_VOL====" + event.arg2);
            if (!isTeddyWorking()) {
                return;
            }
            if (null != vuiWindow) {
                vuiWindow.onVolumeChanged(event.arg2);
            }
            int volumeInt = (int) (Math.random() * 30);
            EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_ING_VOLUME, volumeInt));
        } else {
        }
    }

    private void handleEventResult(AIUIEvent event) {
        SitechDevLog.i(VoiceConstants.TEDDY_TAG, "event.info===" + event.info);
        try {
            JSONObject bizParamJson = new JSONObject(event.info);
            JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
            JSONObject params = data.getJSONObject("params");
            JSONObject content = data.getJSONArray("content").getJSONObject(0);
            String sub = params.optString("sub");
            switch (sub) {
                case "iat":
                    handleIat(event, content);
                    break;
                case "nlp":
                    handleNLP(event, content);
                    break;
                case "tts":
//                    handleTTS(event, content);
                    break;
                default:
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log("StackTrace = " + e.getLocalizedMessage());
        }
    }

    private void handleTTSEvent(AIUIEvent event) {
        switch (event.arg1) {
            case AIUIConstant.TTS_SPEAK_BEGIN:
                log("开始播放");
                isInTTS = true;
                if (vuiWindow != null) {
                    vuiWindow.clearText();
                    vuiWindow.appendText(event.arg2, event.info);
                }
                break;

            case AIUIConstant.TTS_SPEAK_PROGRESS:
                if (vuiWindow != null) {
                    vuiWindow.appendText(event.arg2, event.info);
                }
                break;

            case AIUIConstant.TTS_SPEAK_PAUSED:
                log("暂停播放");
                break;

            case AIUIConstant.TTS_SPEAK_RESUMED:
                log("恢复播放");
                break;

            case AIUIConstant.TTS_SPEAK_COMPLETED:
                isInTTS = false;
                log("播放完成==>" + mAIUIState + "====>shutdown==" + shutdown);
                if (shutdown) {
                    isWakeupTTS = false;
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_SR_OVER));
                    shutdown = false;
                    return;
                } else {
                    EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_TTS_PLAYIING, ""));
                    if (AIUIConstant.STATE_READY == mAIUIState) {
                        isWakeupTTS = false;
                        mAIUIEngine.startRecord();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void handleIat(AIUIEvent event, JSONObject content) throws Exception {
        if (content.has("cnt_id")) {
            String cnt_id = content.optString("cnt_id");
            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");
            if (TextUtils.isEmpty(cntStr)) {
                return;
            }
            JSONObject cntJson = new JSONObject(cntStr);
            if (null != cntJson) {
                JSONObject textJson = cntJson.optJSONObject("text");
                if (null != textJson) {
                    JSONArray ws = textJson.optJSONArray("ws");
                    if (null != ws) {
                        int len = ws.length();
                        if (len > 0) {
                            StringBuilder res = new StringBuilder();
                            for (int i = 0; i < len; i++) {
                                JSONObject index = ws.optJSONObject(i);
                                JSONArray cw = index.optJSONArray("cw");
                                if (null != cw && cw.length() > 0) {
                                    res.append(cw.optJSONObject(0).optString("w"));
                                }
                            }
                            if (res.length() > 0) {
                                vuiWindow.showText(res.toString());
                                EventBusUtils.postEvent(new VoiceEvent(
                                        VoiceEvent.EVENT_VOICE_SR_SUCCESS,
                                        res.toString()));
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleTTS(AIUIEvent event, JSONObject content) throws JSONException {
        if (mTTS.isInit()) {
            mTTS.handleTTS(event, content);
        }
    }

    private void handleNLP(AIUIEvent event, JSONObject content) throws Exception {
        log(new StringBuilder().append(" handleNLP = ").append(content.toString()).toString());
        if (content.has("cnt_id")) {
            String cnt_id = content.optString("cnt_id");
            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");
            if (TextUtils.isEmpty(cntStr)) {
                return;
            }

            JSONObject cntJson = new JSONObject(cntStr);
            // 解析得到语义结果
            JSONObject intent = cntJson.optJSONObject("intent");
//              应答码	说明
//              "rc"
//              0	操作成功
//              1	输入异常
//              2	系统内部异常
//              3	业务操作失败，没搜索到结果或信源异常
//              4	文本没有匹配的技能场景，技能不理解或不能处理该文本
            int rc = intent.optInt("rc", -1);
            log(intent.toString());
            if (0 == rc || 3 == rc) {
                String service = intent.optString("service");
                JSONArray semantics = intent.optJSONArray("semantic");
                if (TextUtils.equals("SITECHAI.sitechVoiceCmd", service)) {
                    String index = null;
                    if (null != semantics && semantics.length() > 0) {
                        JSONObject semantic = semantics.optJSONObject(0);
                        switch (semantic.optString("intent")) {
                            case "closeVoice":
                                //关闭语音
                                shutAndTTS("好的");
                                break;
                        }
                    }
                } else if (TextUtils.equals("SITECHAI.sitechList", service)) {
                    String index = null;
                    if (null != semantics && semantics.length() > 0) {
                        JSONObject semantic = semantics.optJSONObject(0);
                        switch (semantic.optString("intent")) {
                            case "listSelect":
                                JSONArray slots = semantic.optJSONArray("slots");
                                int len = slots.length();
                                if (null != slots && len > 0) {
                                    JSONObject slot = slots.optJSONObject(0);
                                    if (null != slot) {
                                        index = slot.optString("value");
                                    }
                                }
                                break;
                        }
                    }
                    if (MapUtil.isSelectPoiScene()) {
                        if (!TextUtils.isEmpty(index))
                            EventBusUtils.postEvent(new PoiEvent(
                                    PoiEvent.EVENT_QUERY_POI_INDEX,
                                    index));
                        return;
                    } else {
                        if (vuiWindow.getCurrentHolder() instanceof ContactsHolder) {
                            ContactsHolder holder = vuiWindow.getCurrentHolder();
                            if (null != holder.getContacts()) {
                                if (holder.getContacts().size() > 0) {
                                    int i = Integer.valueOf(index);
                                    if (holder.getContacts().size() >= i) {
                                        i--;
                                        String phoneNumber = holder.getContacts().get(i).getPhoneNumber();
                                        if (!TextUtils.isEmpty(phoneNumber)) {
                                            vuiWindow.hide();
                                            VUIUtils.callPhone(phoneNumber);
                                        }
                                    } else {
                                        shutAndTTS("找不到您要的联系人");
                                    }
                                    return;
                                }
                            }
                        }
//                    shutAndTTS("Teddy正在努力学习中，敬请期待");
                    }
                } else if (TextUtils.equals("SITECHAI.AIradio", service)) {
                    if (null != semantics && semantics.length() > 0) {
                        JSONObject semantic = semantics.optJSONObject(0);
                        String s = semantic.optString("intent").toLowerCase();
                        //新特速报
                        if ("AIradio_news".toLowerCase().equals(s)) {
                            playKaoLa(semantic, 1);
                            //少儿读物
                        } else if ("AIradio_kid".toLowerCase().equals(s)) {
                            playKaoLa(semantic, 2);
                            //车嗨娱乐
                        } else if ("AIradio_joke".toLowerCase().equals(s)) {
                            playKaoLa(semantic, 3);
                            //生活一点通
                        } else if ("AIradio_life".toLowerCase().equals(s)) {
                            playKaoLa(semantic, 4);
                            //随机音频
                        } else if ("AIradio_random".toLowerCase().equals(s)) {
                            gotoPlay(new Random().nextInt(4), new Random().nextInt(6));
                            //                            case "AIradio_broadcast":
//                                analysisBroadcast(semantic);
//                                break;
                        } else {
                            vuiAnr();
                        }
                        return;
                    }
                    vuiAnr();
                } else if (TextUtils.equals("musicX", service)) {
                    musicXServiceLogic(service, semantics);
                } else if (TextUtils.equals(XF_SKILL_TAG_CROSSTALK, service) ||
                        TextUtils.equals(XF_SKILL_TAG_DRAMA, service) ||
                        TextUtils.equals(XF_SKILL_TAG_NURSERYRHYME, service) ||
                        TextUtils.equals(XF_SKILL_TAG_STORYTELLING, service) ||
                        TextUtils.equals(XF_SKILL_TAG_AUDIOBOOK, service) ||
                        TextUtils.equals(XF_SKILL_TAG_STORY, service) ||
                        TextUtils.equals(XF_SKILL_TAG_NOVEL, service)) {
                    audioSkillServiceLogic(service, semantics);
                } else {
                    if (TextUtils.equals("mapU", service)) {
                        if (null != semantics && semantics.length() > 0) {
                            JSONObject semantic = semantics.optJSONObject(0);
                            switch (semantic.optString("intent")) {
                                case "LOCATE":
//                        vuiAnr();
                                    return;
                                case "QUERY":
                                    JSONArray slots = semantic.optJSONArray("slots");
                                    int len = slots.length();
                                    if (null != slots && len > 0) {
                                        for (int i = 0; i < len; i++) {
                                            JSONObject object = slots.optJSONObject(i);
                                            if (null != object) {
                                                if (TextUtils.equals(object.optString("name"),
                                                        "endLoc.ori_loc")) {
                                                    String value = object.optString("value");
                                                    if (!TextUtils.isEmpty(value)) {
                                                        if (TextUtils.equals(value, "家")) {
                                                            if (!LocationData.getInstance().isHasHomeAddress()) {
                                                                Intent goHome = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                                                                goHome.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.HOME_ADDRESS_SET_INDEX);
                                                                if (AppVariants.currentActivity != null) {
                                                                    AppVariants.currentActivity.startActivity(goHome);
                                                                }
                                                                shutAndTTS("请先设置家的地址");
                                                                return;
                                                            } else {
                                                                //导航回家
                                                                vuiWindow.hide();
                                                                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_HOME));
                                                                return;
                                                            }
                                                        } else if (TextUtils.equals(value, "公司")) {
                                                            if (!LocationData.getInstance().isHasWorkAddress()) {
                                                                Intent goCompony = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                                                                goCompony.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.COMPONY_ADDRESS_SET_INDEX);
                                                                if (AppVariants.currentActivity != null) {
                                                                    AppVariants.currentActivity.startActivity(goCompony);
                                                                }
                                                                shutAndTTS("请先设置公司的地址");
                                                                return;
                                                            } else {
                                                                //导航回公司
                                                                vuiWindow.hide();
                                                                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_START_NAVI_COMPONY));
                                                                return;
                                                            }
                                                        } else {
//                                                            vuiWindow.hide();
                                                            EventBusUtils.postEvent(new PoiEvent(PoiEvent.EVENT_QUERY_POI_KEYWORD, value));
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case "OPEN_MAP":
                                    EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_OPEN_MAP));
                                    vuiWindow.hide();
                                    break;
                                case "CLOSE_MAP":
                                    vuiWindow.hide();
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else if (TextUtils.equals("weather", service)) {
//                        JSONObject data = intent.optJSONObject("data");
//                        if (null != data) {
//                            JSONArray semantics = intent.optJSONArray("semantic");
//                            if (null != semantics && semantics.length() > 0) {
//                                JSONObject semantic = semantics.optJSONObject(0);
//                                if (null != semantic) {
//                                    JSONArray slots = semantic.optJSONArray("slots");
//                                    if (null != slots && slots.length() > 0) {
//                                        JSONObject slot = slots.optJSONObject(0);
//                                        if (null != slot) {
//                                            JSONObject normValue = new JSONObject(slot.optString("normValue"));
//                                            if (null != normValue) {
//                                                String datetime = normValue.optString("datetime");
//                                                if (!TextUtils.isEmpty(datetime)) {
//                                                    JSONArray array = data.optJSONArray("result");
//                                                    if (null != array) {
//                                                        int len = array.length();
//                                                        if (len > 0) {
//                                                            for (int i = 0; i < len; i++) {
//                                                                JSONObject today = array.optJSONObject(i);
//                                                                if (TextUtils.equals(today.optString("date"), datetime)) {
//                                                                    if (null != vuiWindow) {
//                                                                        vuiWindow.showWeather(today);
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    } else if (TextUtils.equals("stock", service)) {
//                        JSONObject data = intent.optJSONObject("data");
//                        if (null != data) {
//                            JSONArray array = data.optJSONArray("result");
//                            if (null != array && array.length() > 0) {
//                                JSONObject stock = array.optJSONObject(0);
//                                if (null != vuiWindow) {
//                                    vuiWindow.showStock(stock);
//                                }
//                            }
//                        }
                    } else if (TextUtils.equals(VoiceConstants.VOICE_CUSTOM_SERVICE_NAVI, service)) {
                        if (null != semantics && semantics.length() > 0) {
                            JSONObject semantic = semantics.optJSONObject(0);
                            if (null != semantic) {
                                switch (semantic.optString("intent")) {
                                    //设置为家
                                    case VoiceConstants.VOICE_CUSTOM_INTENT_NAVI_SETHOME:
                                        EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_NAVI_SET_HOME_ADDR));
                                        return;
                                    //设置为公司
                                    case VoiceConstants.VOICE_CUSTOM_INTENT_NAVI_SETWORK:
                                        EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_NAVI_SET_WORK_ADDR));
                                        return;
                                    //开始导航
                                    case VoiceConstants.VOICE_CUSTOM_INTENT_START_NAVI:
//                                        EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_NAVI_SET_HOME_ADDR));
                                        return;
                                    //关闭导航
                                    case VoiceConstants.VOICE_CUSTOM_INTENT_CLOSE_NAVI:
                                        EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_CLOSE_NAVI));
                                        return;
                                    default:
                                        vuiAnr();
                                        break;

                                }
                            }
                        }
                    } else if (TextUtils.equals("telephone", service)) {
                        if (!SettingConfig.getInstance().isBtConnected()) {
                            shutAndTTS("当前蓝牙未连接，无法使用通讯录功能");
                            return;
                        }
                        JSONArray semanticss = intent.optJSONArray("semantic");
                        if (null != semanticss && semanticss.length() > 0) {
                            JSONObject semantic = semanticss.optJSONObject(0);
                            if (null != semantic) {
                                switch (semantic.optString("intent")) {
                                    case "DIAL":
                                        JSONObject data = intent.optJSONObject("data");
                                        if (null != data) {
                                            calls = data.optJSONArray("result");
                                            if (calls.length() > 1) {
                                                vuiWindow.showContacts(calls);
                                            }
                                        }
                                        break;
                                    case "INSTRUCTION":
                                        JSONArray slots = semantic.optJSONArray("slots");
                                        if (null != slots && slots.length() > 0) {
                                            JSONObject slot = slots.optJSONObject(0);
                                            if (null != slot) {
                                                switch (slot.optString("value")) {
                                                    case "CONFIRM":
                                                    case "SEQUENCE":
                                                        int index = 1;
                                                        int len = slots.length();
                                                        for (int i = 1; i < len; i++) {
                                                            JSONObject object = slots.optJSONObject(i);
                                                            if (null != object) {
                                                                String name = object.optString("name");
                                                                if (TextUtils.equals("posRank.offset", name)) {
                                                                    index = Integer.valueOf(object.optString("normValue"));
                                                                }
                                                            }
                                                        }
                                                        if (null != calls && calls.length() >= index) {
                                                            index--;
                                                            JSONObject call = calls.optJSONObject(index);
                                                            String phoneNumber = call.optString("phoneNumber");
                                                            if (!TextUtils.isEmpty(phoneNumber)) {
                                                                vuiWindow.hide();
                                                                VUIUtils.callPhone(phoneNumber);
                                                            }
                                                        } else {
                                                            shutAndTTS("找不到您要的联系人");
                                                        }
                                                        break;
                                                    case "QUIT":
                                                        shutAndTTS("好的");
                                                        break;
                                                    case "CONTACTS":
                                                        vuiWindow.hide();
                                                        VUIUtils.goContacts();
                                                        break;
                                                    default:
                                                        vuiAnr();
                                                        break;
                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    } else if (TextUtils.equals("SITECHAI.SitechControl", service)) {
                        if (null != semantics && semantics.length() > 0) {
                            JSONObject semantic = semantics.optJSONObject(0);
                            if (null != semantic) {
                                switch (semantic.optString("intent")) {
                                    case "cmdAction":
                                        String template = semantic.optString("template");
                                        if ("{sitechaction}{appname}".equals(template)
                                                || "{sitechaction}{pricecounter}".equals(template)
                                                || "{sitechaction}".equalsIgnoreCase(template)
                                                || "{sitechaction}{page}".equals(template)) {
                                            doSitechactionWithAppName(semantic);
                                        } else {
                                            vuiAnr();
                                        }
                                        break;
                                    default:
                                        vuiAnr();
                                        break;
                                }
                            }
                        }
                    } else {
                        //未处理的技能
//                        vuiAnr();
                    }
                    JSONObject answer = intent.optJSONObject("answer");
                    if (null != answer) {
                        String text = answer.optString("text");
                        if (!TextUtils.isEmpty(text)) {
                            shutAndTTS(text);
                        }
                    }
                }
            } else {
                //TODO
                log("rc = " + rc);
                vuiAnr();
            }
        }
    }

    private void doSitechactionWithAppName(JSONObject semantic) {
        JSONArray slots = semantic.optJSONArray("slots");
        if (null != slots) {
            int len = slots.length();
            String sitechaction = null;
            String appname = null;
            String page = null;
            String appnameTag = null;
            String priceCounter = null;
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    JSONObject slot = slots.optJSONObject(i);
                    if (null != slot) {
                        if (TextUtils.equals(slot.optString("name"), "sitechaction")) {
                            sitechaction = slot.optString("normValue");
                            continue;
                        }
                        if (TextUtils.equals(slot.optString("name"), "appname")) {
                            appname = slot.optString("value");
                            appnameTag = slot.optString("normValue");
                            continue;
                        }
                        if (TextUtils.equals(slot.optString("name"), "pricecounter")) {
                            priceCounter = slot.optString("normValue");
                            continue;
                        }
                    }
                }
                if ("sign".equalsIgnoreCase(sitechaction)) {
                    //个人中心，签到功能
                    log("个人中心，签到功能");
                    shutdown = true;
                    shut();
                    EventBusUtils.postEvent(new AppEvent(AppEvent.EB_MEMBER_SIGN));
                    return;
                }
                if (TextUtils.isEmpty(sitechaction) || (TextUtils.isEmpty(appname) && TextUtils.isEmpty(priceCounter))) {
                    vuiAnr();
                } else {
                    switch (sitechaction) {
                        case "start":
                            log("action start");
                            if (priceCounter.equals("action_count")) {
                                log("action 开始计价");
                                EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_TAXI_START_PRICE));
                                shutAndTTS("好的，已开始计价，请按照交通规则行驶保障司乘安全");
                                shut();
                            }
                            break;
                        case "stop":
                            log("action stop");
                            if (priceCounter.equals("action_count")) {
                                log("action 停止计价");
                                EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_TAXI_STOP_PRICE));
                                shutAndTTS("好的，已停止计价，请乘客确认订单并付款");
                                shut();
                            }
                            break;
                        case "open":
                            if (appnameTag.equals("priceCounter")) {
                                log("打开出行计价器");
                                EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_OPEN_TAXI_PAGE));
                                shutAndTTS("好的，已为您打开出行计价器");
                                shut();
                                break;
                            }
                            switch (appname) {
                                case "首页":
                                case "主页":
                                    if (null == AppVariants.currentActivity || !(AppVariants.currentActivity instanceof MainActivity)) {
//                                        Intent goMain = new Intent();
//                                        goMain.setClass(context, MainActivity.class);
//                                        context.startActivity(goMain);
                                        RouterUtils.getInstance().navigationHomePage(RouterConstants.HOME_MAIN);
                                        shut();
                                    } else {
                                        shutAndTTS("您当前已在首页");
                                    }
                                    break;
                                case "皮肤设置":
                                case "主题设置":
                                case "设置":
                                    log("正在打开皮肤设置");
                                    EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_OPEN_SETTING_SKIN_PAGE));
                                    shutAndTTS("好的，已为您打开皮肤设置");
                                    shut();
                                    break;
                                case "控制面板":
                                case "控制中心":
                                    log("正在打开控制中心");
                                    EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_CONTROL_MENU, true));
                                    shut();
                                    break;
                                case "车辆状态":
                                case "车辆信息":
                                    log("正在打开车辆状态");
                                    EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_OPEN_CAR_STATUS_PAGE));
                                    shutAndTTS("好的，已为您打开车辆状态");
                                    shut();
                                    break;
                                case "个人中心":
                                    if (LoginUtils.isLogin()) {
                                        log("正在打开个人中心");
                                        EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_OPEN_MEMBER_INFO_PAGE));
                                        shutAndTTS("好的，已为您打开个人中心");
                                        shut();
                                    } else {
                                        PersonLoginWindow.getInstance().showWnd(() -> EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_OPEN_MEMBER_INFO_PAGE)));
                                        shutAndTTS("我还没有权限打开个人中心，请您登录授权。");
                                        shut();
                                    }
                                    break;
                                case "我的积分":
                                    if (LoginUtils.isLogin()) {
                                        log("正在打开我的积分");
                                        EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_MY_POINT_PAGE));
                                        shutAndTTS("好的，已为您打开我的积分");
                                        shut();
                                    } else {
                                        PersonLoginWindow.getInstance().showWnd(() -> EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_MY_POINT_PAGE)));
                                        shutAndTTS("我还没有权限打开我的积分，请您登录授权。");
                                        shut();
                                    }
                                    break;
                                case "本地音乐":
                                    if (null == AppVariants.currentActivity || !(AppVariants.currentActivity instanceof MusicMainActivity)) {
                                        Intent goMusicMain = new Intent();
                                        goMusicMain.setClass(context, MusicMainActivity.class);
                                        goMusicMain.putExtra("index", 1);
                                        context.startActivity(goMusicMain);
                                        shut();
                                    } else {
                                        shutAndTTS("您当前已在本地音乐");
                                    }
                                    break;
                                case "QQ音乐":
                                    AppUtil.goOtherActivity(context, "QQ音乐", "com.tencent.qqmusiccar",
                                            "com.tencent.qqmusiccar.app.activity.AppStarterActivity");
                                    shut();
                                    break;
                                case "天气":
                                    shutAndTTS("好的，正在为您打开天气");
                                    shut();
                                    RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_WEATHER);
                                    break;
                                case "听伴":
                                    shutAndTTS("好的，正在为您打开听伴");
                                    shut();
                                    RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE_MAIN);
                                    break;
                                case "全部应用":
                                case "应用列表":
                                    shutAndTTS("好的，正在为您打开全部应用");
                                    shut();
                                    RouterUtils.getInstance().navigation(RouterConstants.SETTING_APP_LIST);
                                    break;
                                case "网络音乐":
                                case "酷我音乐":
                                case "酷我":
                                    KuwoManager.getInstance().startKuwoApp(true);
                                    break;
                                case "导航":
                                case "LED表情管理":
                                case "表情管理":
                                case "视频":
                                case "电话":
                                case "饭聊":
                                case "商城":
                                    shutAndTTS("努力开发中，尽请期待");
                                    break;
                                default:
                                    vuiAnr();
                                    break;
                            }
                            break;
                        case "close":
                            switch (appname) {
                                case "本地音乐":
                                    MusicManager.getInstance().stop(new MusicManager.CallBack<String>() {
                                        @Override
                                        public void onCallBack(int code, String s) {

                                        }
                                    });
                                    if (null == AppVariants.currentActivity || AppVariants.currentActivity instanceof MusicMainActivity) {
                                        AppVariants.currentActivity.finish();
                                    }
                                    shut();
                                    break;
                                case "控制中心":
                                case "控制面板":
                                    log("正在关闭控制中心");
                                    shut();
                                    EventBusUtils.postEvent(new WindowEvent(WindowEvent.EVENT_WINDOW_CONTROL_MENU, false));
                                    break;
                                case "网络音乐":
                                case "酷我音乐":
                                case "酷我":
                                    KuwoManager.getInstance().exitKuwoApp();
                                    shutAndTTS("已为您关闭");
                                    break;
                                default:
                                    vuiAnr();
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            } else {
                vuiAnr();
            }
        } else {
            vuiAnr();
        }
    }

    private void vuiAnr() {
        shutAndTTS(context.getResources().getString(
                R.string.vui_anr_text));
    }

    public void shutAndTTS(@NotNull String tts) {
        shutAndTTS(true, tts);
    }

    public void shutAndTTS(boolean shutdown, @NotNull String tts) {
        this.shutdown = shutdown;
        if (!TextUtils.isEmpty(tts)) {
            mAIUIEngine.ttsStart(tts);
            EventBusUtils.postEvent(new VoiceEvent(
                    VoiceEvent.EVENT_VOICE_TTS_PLAYIING,
                    tts));
        } else {
            shut();
        }
    }

    public void shut() {
        vuiWindow.hide();
    }

    private VUI() {
        context = AppApplication.getContext();
        registerActivityLifecycleCallbacks();
        mWakeupEngine = WakeupEngine.getInstance(mWakeuperListener);
        mAIUIEngine = AIUIEngine.getInstance(mAIUIListener);
        mTTS = TTS.getInstance(mAIUIListener);
        EventBusUtils.register(this);
    }

    private void registerActivityLifecycleCallbacks() {
        activities = new LinkedList<>();
        ((Application) context).registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        log("onActivityResumed -> " + activity.getLocalClassName());
//                        topActivity = new WeakReference<Activity>(activity);
                        activities.add(activity);
                        if (isUIHide) {
                            start();
                        }
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        log("onActivityStopped -> " + activity.getLocalClassName());
                        activities.remove(activity);
//                        if (activities.size() == 0){
//                            isUIHide = true;
//                            stop();
//                        }
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
//                        if (null != topActivity && null != topActivity.get()){
//                            if (topActivity.get() == activity){
//                                topActivity = null;
//                            }
//                        }
                    }
                });
    }

    public void start() {
        isUIHide = false;
        if (null != mAIUIEngine) {
            if (AIUIConstant.STATE_IDLE == mAIUIState) {
                mAIUIEngine.start();
            }
        }
        if (null != mWakeupEngine) {
            int ret = mWakeupEngine.startListening();
            log("VUI start wakeup -> " + ret);
        } else {
            log("VUI WakeupEngine -> null");
        }
    }

    /**
     * 应用没有UI显示的时候
     */
    public void stop() {
        isUIHide = true;
        if (null != vuiWindow) {
            vuiWindow.hide();
        }
        if (null != mWakeupEngine) {
            if (mWakeupEngine.isListening()) {
                mWakeupEngine.stop();
            }
        }
        if (null != mAIUIEngine) {
            mAIUIEngine.stop();
        }
    }

    @Override
    public void onWindowHide() {
        log("VUI onWindowHide -> onWindowHide()");
        if (isInTTS) {
            mAIUIEngine.ttsCancel();
            mTTS.stop();
        }
        mAIUIEngine.stopRecord();
        if (AIUIConstant.STATE_WORKING == mAIUIState) {
            mAIUIEngine.resetWakeup();
        }
        if (null != mWakeupEngine && !mWakeupEngine.isListening() && !isUIHide) {
            mWakeupEngine.startListening();
        }
        isInTTS = false;
        isWakeupTTS = false;
        VoiceSourceManager.getInstance().resume(VoiceSourceManager.CONTENT);
    }

    public boolean isTeddyWorking() {
        boolean isWorking = isWakeupTTS || (AIUIConstant.STATE_WORKING == mAIUIState) || isInTTS;
        SitechDevLog.i(TAG, "isTeddyWorking====" + isWorking);
        return isWorking;
    }

    private void syncContacts() {
        if (BtGlobalRef.contacts.size()>0) {
            StringBuilder contacts = new StringBuilder();
            JSONObject contact = new JSONObject();
            for (Contact con:BtGlobalRef.contacts){
                String displayName = con.getName();
                String number = con.getPhoneNumber();
                if (!TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(number)) {
                    try {
                        contact.put("name", displayName.trim());
                        contact.put("phoneNumber", number.trim());
                        contacts.append(contact.toString()).append("\r\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (contacts.length() > 0) {
                String data = contacts.toString();
                syncContacts(data);
            }
        }
    }

    /**
     * 上传词典，动态实体
     *
     * @param data
     */
    private void syncContacts(String data) {
        if (StringUtils.isEmpty(data)) {
            return;
        }
        try {
            String dataBase64 = Base64.encodeToString(data.getBytes("utf-8"), Base64.NO_WRAP);
            JSONObject syncSchemaJson = new JSONObject();
            JSONObject dataParamJson = new JSONObject();
            // 设置id_name为uid，即用户级个性化资源
            // 个性化资源使用方法可参见http://doc.xfyun.cn/aiui_mobile/的用户个性化章节
            dataParamJson.put("id_name", "uid");
            // 设置res_name为联系人
            dataParamJson.put("res_name", "IFLYTEK.telephone_contact");
            syncSchemaJson.put("param", dataParamJson);
            syncSchemaJson.put("data", dataBase64);
            // 传入的数据一定要为utf-8编码
            byte[] syncData = syncSchemaJson.toString().getBytes("utf-8");
            // 给该次同步加上自定义tag，在返回结果中可通过tag将结果和调用对应起来
            JSONObject paramJson = new JSONObject();
            paramJson.put("tag", "sync-tag");
            mAIUIEngine.syncData(paramJson.toString(), syncData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VUI getInstance() {
        return VUIHolder.INSTANCE;
    }

    private static class VUIHolder {
        private static final VUI INSTANCE = new VUI();
    }

    public static void log(String log) {
        int segmentSize = 3 * 1024;
        long length = log.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.i(TAG, log);
        } else {
            while (log.length() > segmentSize) {// 循环分段打印日志
                String logContent = log.substring(0, segmentSize);
                log = log.replace(logContent, "");
                Log.i(TAG, "-------------------" + logContent);
            }
            Log.i(TAG, "-------------------" + log);// 打印剩余日志
        }
    }

    private void toKaolaPage(String appname, int index, int deepIndex) {
        gotoPlay(index, deepIndex);
    }

    public interface OnVolumeChangeListener {
        void onVolumeChange(int value);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(VoiceEvent event) {
        switch (event.getEventKey()) {
            case VoiceEvent.EVENT_VOICE_MVW_SUCCESS:
                ThreadUtils.runOnUIThread(() -> {
                    if (event.getEventValue() != null) {
                        onVoiceWakeup((String) event.getEventValue());
                    } else {
                        onVoiceWakeup();
                    }
                });
                break;
            case VoiceEvent.EVENT_VOICE_TTS_PLAY_TEXT:
                if (event.getEventValue() != null && mAIUIEngine != null) {
                    shutAndTTS((String) event.getEventValue());
                }
                break;
            case VoiceEvent.EVENT_VOICE_UPLOAD:
                Object data = event.getEventValue();
                if (data != null) {
                    AIUIMessage message = (AIUIMessage) data;
                    if (mAIUIEngine != null) {
                        mAIUIEngine.syncSpeakerData(message);
                        try {
                            JSONObject mPersParams = new JSONObject();
                            mPersParams.put("uid", "");
                            mAIUIEngine.setPersParams(mPersParams);
                        } catch (Exception e) {
                            SitechDevLog.exception(e);
                        }
                    }
                }
                break;
            case VoiceEvent.EVENT_VOICE_SR_OVER:
                if (isInTTS) {
                    shutdown = true;
                }
                onWindowHide();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onTeddyEvent(TeddyEvent event) {
        switch (event.getEventKey()){
            case TeddyEvent.EB_TEDDY_EVENT_SR_CONTACT_DICT:{
                syncContacts();
            }
            break;
        }
    }


    /**
     * 上传词典，动态实体
     *
     * @param resName
     * @param data
     */
    private void syncIflytekObject(String resName, String data) {
        if (StringUtils.isEmpty(data)) {
            return;
        }
        try {
            String dataBase64 = Base64.encodeToString(data.getBytes("utf-8"), Base64.NO_WRAP);
            JSONObject syncSchemaJson = new JSONObject();
            JSONObject dataParamJson = new JSONObject();
            // 设置id_name为uid，即用户级个性化资源
            // 个性化资源使用方法可参见http://doc.xfyun.cn/aiui_mobile/的用户个性化章节
            dataParamJson.put("id_name", "uid");
            dataParamJson.put("id_value", "");
            // 设置res_name为联系人
            dataParamJson.put("res_name", resName);
            syncSchemaJson.put("param", dataParamJson);
            syncSchemaJson.put("data", dataBase64);
            // 传入的数据一定要为utf-8编码
            byte[] syncData = syncSchemaJson.toString().getBytes("utf-8");
            // 给该次同步加上自定义tag，在返回结果中可通过tag将结果和调用对应起来
//            mAIUIEngine.syncData("", syncData);
//            mAIUIEngine.syncSpeakerData("", syncData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analysisBroadcast(JSONObject semantic) {
        String template = semantic.optString("template");
        JSONArray slots = semantic.optJSONArray("slots");
        if (null != slots && slots.length() > 0) {
            for (int i = 0; i < slots.length(); i++) {
                JSONObject slot = slots.optJSONObject(i);
                String name = slot.optString("name");
                if ("broadcast_keyword".equals(slot.optString("normValue"))) {
                    String va = slot.optString("value");

                }
            }
        }
    }

    private void playKaoLa(JSONObject semantic, int index) {
        playKaoLa(semantic, index, false);
    }

    private void playKaoLa(JSONObject semantic, int index, boolean autoRandomPlay) {
        boolean doFinally = true;
        String defaultPlayTTS = "";
        if (index == 1) {
            defaultPlayTTS = "正在为您打开新特速报";
        } else if (index == 2) {
            defaultPlayTTS = "正在为您打开少儿读物";
        } else if (index == 3) {
            defaultPlayTTS = "正在为您打开车嗨娱乐";
        } else if (index == 4) {
            defaultPlayTTS = "正在为您打开生活一点通";
        }
        try {
            String template = semantic.optString("template");
            boolean autoPlay = template.contains("播")
                    || template.contains("听")
                    || template.contains("讲")
                    || template.contains("来个")
                    || template.contains("来段");
            JSONArray slots = semantic.optJSONArray("slots");
            if (null != slots && slots.length() > 0) {
                for (int i = 0; i < slots.length(); i++) {
                    JSONObject slot = slots.optJSONObject(i);
                    String name = slot.optString("name");
                    if (name.equals("AI_joke")
                            || name.equals("AI_news")
                            || name.equals("AI_life")
                            || name.equals("AI_kid")) {
                        if ("specific_item".equals(slot.optString("normValue"))) {
                            boolean result = getAIRadioItem(slot.optString("value"));
                            doFinally = !result;
                        } else if (null != slot && "main_classify".equals(slot.optString("normValue"))) {
                            shutAndTTS(defaultPlayTTS);
                            gotoPlay(index, autoPlay || autoRandomPlay ? new Random().nextInt(index == 2 ? 5 : 6) : -1);//index == 2 ? 5 : 6 少儿读物  英语儿歌 音源缺失
                            doFinally = false;
                        }
                        return;
                    } else {
                        continue;
                    }
                }
            }
        } finally {
            if (doFinally) {
                shutAndTTS(defaultPlayTTS);
                gotoPlay(index, -1);
            }
        }
    }

    private void gotoPlay(int index, int subIndex) {
        KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(
                context, index, subIndex);
        vuiWindow.hide();
        shut();
    }

    private boolean getAIRadioItem(String name) {
        int index = -1;
        int deepIndex = -1;
        switch (name) {
            case "新闻电台":
                index = 1;
                deepIndex = 0;
                break;
            case "热点快讯":
                index = 1;
                deepIndex = 1;
                break;
            case "环球时政":
                index = 1;
                deepIndex = 2;
                break;
            case "汽车电台":
                index = 1;
                deepIndex = 3;
                break;
            case "财经快讯":
                index = 1;
                deepIndex = 4;
                break;
            case "娱乐":
                index = 1;
                deepIndex = 5;
                break;
            case "搞定熊孩子":
                index = 2;
                deepIndex = 0;
                break;
            case "童谣儿歌":
            case "儿歌童谣":
            case "儿歌":
                index = 2;
                deepIndex = 1;
                break;
            case "萌娃故事汇":
                index = 2;
                deepIndex = 2;
                break;
            case "跟我学英语":
                index = 2;
                deepIndex = 3;
                break;
            case "诗词歌赋":
                index = 2;
                deepIndex = 4;
                break;
            case "英语儿歌":
            case "英文儿歌":
            case "儿童英语儿歌故事":
                index = 2;
                deepIndex = 5;
                break;
            case "相声电台":
            case "相声频道":
                index = 3;
                deepIndex = 0;
                break;
            case "爆笑段子":
                index = 3;
                deepIndex = 1;
                break;
            case "闲聊脱口秀":
                index = 3;
                deepIndex = 2;
                break;
            case "搞笑电台":
                index = 3;
                deepIndex = 3;
                break;
            case "堵车不堵心":
                index = 3;
                deepIndex = 4;
                break;
            case "情景喜剧":
                index = 3;
                deepIndex = 5;
                break;
            case "生活百科":
                index = 4;
                deepIndex = 0;
                break;
            case "健康保鲜剂":
                index = 4;
                deepIndex = 1;
                break;
            case "美食家":
                index = 4;
                deepIndex = 2;
                break;
            case "旅行家":
                index = 4;
                deepIndex = 3;
                break;
            case "运动健身":
                index = 4;
                deepIndex = 4;
                break;
            case "情感故事":
                index = 4;
                deepIndex = 5;
                break;
        }
        if (index >= 0) {
            //intent to kaola
            toKaolaPage(name, index, deepIndex);
            return true;
        }
        return false;
    }

    private void doMusicControl(JSONObject semantic) {
        JSONArray slots = semantic.optJSONArray("slots");
        int len = slots.length();
        if (null != slots && len > 0) {
            JSONObject slot = slots.optJSONObject(0);
            if (null != slot) {
                switch (slot.optString("value")) {
                    case "past":
                        VoiceSourceManager.getInstance().pre(VoiceSourceManager.VOICE);
                        break;
                    case "next":
                        VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                        break;
                    case "replay":
                    case "play":
                        VoiceSourceManager.getInstance().resume(VoiceSourceManager.VOICE);
                        break;
                    case "pause":
                        VoiceSourceManager.getInstance().pause(VoiceSourceManager.VOICE);
                        break;
                    case "close":
                        VoiceSourceManager.getInstance().pause(VoiceSourceManager.VOICE);
                        EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_AUDIO_STOP));
                        break;
                    case "volume_minus":
                        //声音减小
                        AudioUtil.onKeyDownVolume(AudioManager.STREAM_MUSIC);
                        shutAndTTS("已为您减小音量");
                        return;
                    case "volume_plus":
                        //声音增大
                        AudioUtil.onKeyUpVolume(AudioManager.STREAM_MUSIC);
                        shutAndTTS("已为您增加音量");
                        return;
                    default:
                        shutdown = true;
                        vuiAnr();
                        break;
                }
            }
        }
    }

    private void audioSkillServiceLogic(String service, JSONArray semantics) {
        if (null != semantics && semantics.length() > 0) {
            JSONObject semantic = semantics.optJSONObject(0);
            String intent = semantic.optString("intent").toLowerCase();
            JSONArray slots = semantic.optJSONArray("slots");
            String artistOrActor = "";
            String audioName = "";
            boolean isAudioBookIntent = "QUERY_ACTOR".toLowerCase().equals(intent) || "QUERY_CATEGORY".toLowerCase().equals(intent) || "NAME_AUTHOR".toLowerCase().equals(intent) || "QUERY_NAME".toLowerCase().equals(intent) || "QUERY_AUTHOR".toLowerCase().equals(intent) || "NAME_ACTOR".toLowerCase().equals(intent) || "QUERY_AUDIOBOOK".toLowerCase().equals(intent) || "NAME_CHAPTER".toLowerCase().equals(intent);
            boolean isNovelIntent = "QUERY".toLowerCase().equals(intent);
            boolean isDramaIntent = "QUERY".toLowerCase().equals(intent);
            boolean isCrossTalkIntent = "QUERY".toLowerCase().equals(intent);
            boolean isStoryTellingIntent = "QUERY".toLowerCase().equals(intent);
            boolean isStoryIntent = "QUERY".toLowerCase().equals(intent) || "RANDOM_QUERY".toLowerCase().equals(intent) || "QUERY_CATEGORY".toLowerCase().equals(intent);
            if (isAudioBookIntent || isStoryIntent || isNovelIntent || isDramaIntent || isCrossTalkIntent || isStoryTellingIntent) {
                if (XF_SKILL_TAG_AUDIOBOOK.equals(service)) {
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject slot = slots.optJSONObject(i);
                        String name = slot.optString("name");
                        String value = slot.optString("value");
                        if ("artist".equals(name) ||
                                "actor".equals(name) ||
                                "author".equals(name)) {
                            artistOrActor = value;
                        }
                        if ("bookname".equals(name) ||
                                "category".equals(name) ||
                                "ambookname".equals(name)) {
                            audioName = audioName + value;
                        }
                    }
                } else {
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject slot = slots.optJSONObject(i);
                        String name = slot.optString("name");
                        String value = slot.optString("value");
                        if ("artist".equals(name) ||//drama storyTelling nurseryRhyme
                                "actor".equals(name) || //crossTalk
                                "author".equals(name)) {//novel story
                            artistOrActor = value;
                        }
                        if ("name".equals(name) || //drama novel crossTalk  storyTelling story
                                "category".equals(name) ||//drama  novel crossTalk
                                "selections".equals(name) ||//drama
                                "song".equals(name) ||//  nurseryRhyme
                                "category".equals(name) ||//story
                                "album".equals(name)  //nurseryRhyme
                        ) {
                            audioName = audioName + value;
                        }
                    }
                }
                if (!TextUtils.isEmpty(audioName) || !TextUtils.isEmpty(artistOrActor)) {
                    // 按照解析结果搜索
                    //听伴搜索
                    RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                            .withString("queryString", audioName + " " + artistOrActor)
                            .navigation();
                    log("queryString = " + audioName + " " + artistOrActor);
                    shutAndTTS("");
                } else {
                    // slots 为空
                    //儿歌中 随机播放解析成  音频的随机播放
                    if (intent.contains("random")) {
                        //随机播放一首歌   本地 蓝牙 酷我 情况 播放下一首
                        if (VoiceSourceManager.getInstance().getMusicSource() != VoiceSourceManager.KAOLA) {
                            if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.LOCAL_MUSIC) {
                                if (VUIUtils.isUdiskExist()) {
                                    VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                                    shutAndTTS("");
                                    return;
                                }
                            } else if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.BT_MUSIC) {
                                if (SettingConfig.getInstance().isBtConnected()) {
                                    VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                                    shutAndTTS("");
                                    return;
                                }
                            } else if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.KUWO_MUSIC) {
                                VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                                shutAndTTS("");
                                return;
                            }
                        }
                        //考拉情况 或者未开始播放情况    启用播放音频逻辑（ 本地 蓝牙 酷我顺序）
                        if (VUIUtils.isUdiskExist()) {
                            RouterUtils.getInstance().navigation(RouterConstants.FRAGMENT_LOCAL_MUSIC);
                        } else if (SettingConfig.getInstance().isBtConnected()) {
                            RouterUtils.getInstance().getPostcard(RouterConstants.FRAGMENT_LOCAL_MUSIC)
                                    .withInt("index", 1)
                                    .navigation();
                        } else {
                            VUIUtils.openThirdAppByMusic("", "");
                        }
                        shutAndTTS("");
                    } else {
                        if (XF_SKILL_TAG_NOVEL.equals(service)) {
                            audioName = "小说";
                        } else if (XF_SKILL_TAG_NURSERYRHYME.equals(service)) {
                            audioName = "儿歌";
                        } else if (XF_SKILL_TAG_AUDIOBOOK.equals(service)) {
                            audioName = "有声书";
                        } else if (XF_SKILL_TAG_CROSSTALK.equals(service)) {
                            audioName = "相声";
                        } else if (XF_SKILL_TAG_DRAMA.equals(service)) {
                            audioName = "戏曲";
                        } else if (XF_SKILL_TAG_STORY.equals(service)) {
                            audioName = "故事";
                        } else if (XF_SKILL_TAG_STORYTELLING.equals(service)) {
                            audioName = "评书";
                        }
                        if (!TextUtils.isEmpty(audioName)) {
                            //听伴搜索
                            RouterUtils.getInstance().getPostcard(RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
                                    .withString("queryString", audioName + " " + artistOrActor)
                                    .navigation();
                            log("queryString = " + audioName + " " + artistOrActor);
                            shutAndTTS("");
                        }
                    }
                }
            }
        }
    }

    private void musicXServiceLogic(String service, JSONArray semantics) {
        if (null != semantics && semantics.length() > 0) {
            JSONObject semantic = semantics.optJSONObject(0);
            String intent = semantic.optString("intent").toLowerCase();
            if ("INSTRUCTION".toLowerCase().equals(intent)) {
                doMusicControl(semantic);
            } else {/**PLAY  点歌   RANDOM_SEARCH  随机放一首、放首歌、换首歌  ADVICE 询问某位歌手有哪些歌曲*/
                JSONArray slots = semantic.optJSONArray("slots");
                String artistOrActor = "";
                String audioName = "";
                for (int i = 0; i < slots.length(); i++) {
                    JSONObject slot = slots.optJSONObject(i);
                    String name = slot.optString("name");
                    String value = slot.optString("value");
                    if (name.equals("artist")) {
                        artistOrActor = value;
                    }
                    if ("song".equals(name) || "source".equals(name) || "album".equals(name)) {
                        audioName = audioName + " " + value;
                    }
                }
                if (TextUtils.isEmpty(audioName) && TextUtils.isEmpty(artistOrActor)) {
                    // slots 为空  根据service做意图判断
                    //随机播放一首歌   本地 蓝牙 酷我 情况 播放下一首
                    if (VoiceSourceManager.getInstance().getMusicSource() != VoiceSourceManager.KAOLA) {
                        if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.LOCAL_MUSIC) {
                            if (VUIUtils.isUdiskExist()) {
                                VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                                shutAndTTS("");
                                return;
                            }
                        } else if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.BT_MUSIC) {
                            if (SettingConfig.getInstance().isBtConnected()) {
                                VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                                shutAndTTS("");
                                return;
                            }
                        } else if (VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.KUWO_MUSIC) {
                            VoiceSourceManager.getInstance().next(VoiceSourceManager.VOICE);
                            shutAndTTS("");
                            return;
                        }
                    }
                    //考拉情况 或者未开始播放情况    启用播放音频逻辑（ 本地 蓝牙 酷我顺序）
                    if (VUIUtils.isUdiskExist()) {
                        RouterUtils.getInstance().navigation(RouterConstants.FRAGMENT_LOCAL_MUSIC);
                    } else if (SettingConfig.getInstance().isBtConnected()) {
                        RouterUtils.getInstance().getPostcard(RouterConstants.FRAGMENT_LOCAL_MUSIC)
                                .withInt("index", 1)
                                .navigation();
                    } else {
                        VUIUtils.openThirdAppByMusic("", "");
                    }
                    shutAndTTS("");
                } else {
                    VUIUtils.openThirdAppByMusic(audioName, artistOrActor);
                    log("music search = " + audioName + " " + artistOrActor);
                    shutAndTTS("");
                }
            }
        }
    }
}
