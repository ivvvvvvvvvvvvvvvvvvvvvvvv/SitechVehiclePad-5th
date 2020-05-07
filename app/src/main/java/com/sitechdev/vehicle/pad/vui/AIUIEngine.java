package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author zhubaoqiang
 * @date 2019/8/12
 */
public class AIUIEngine {

    private Context context;
    private static AIUIListener aiuiListener;

    private AIUIAgent mAIUIAgent = null;

    private String ttsPeopleParams = "";

    private AIUIEngine() {
        context = AppApplication.getContext();
        createAgent();
    }

    private void createAgent() {
        if (null == mAIUIAgent) {
            mAIUIAgent = AIUIAgent.createAgent(context, getAIUIParams(),
                    aiuiListener);
        }
        if (null == mAIUIAgent) {
            VUI.log("创建AIUIAgent失败！");
        } else {
            VUI.log("AIUIAgent已创建");
        }
    }

    private void destoryAgent() {
        if (null != mAIUIAgent) {
            mAIUIAgent.destroy();
            mAIUIAgent = null;
        }
    }

    public void startRecord() {
        startRecord(null, "sample_rate=16000,data_type=audio," +
                "pers_param={\"uid\":\"\"},tag=audio-tag");
    }

    public void startRecord(byte[] data) {
        startRecord(data, "sample_rate=16000,data_type=audio," +
                "pers_param={\"uid\":\"\"},tag=audio-tag");
    }

    public void startRecord(String text) {
        try {
            startRecord(text.getBytes("utf-8"),
                    "data_type=text,tag=text-tag");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
            return;
        }
        VUI.log("AIUIAgent -> do stopRecord");
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage stopRecord = new AIUIMessage(
                AIUIConstant.CMD_STOP_RECORD, 0, 0,
                params, null);
        mAIUIAgent.sendMessage(stopRecord);
    }

    public void ttsStart(String ttsStr) {
        if (TextUtils.isEmpty(ttsStr.trim())) {
            return;
        }
        VUI.log("AIUIAgent -> 播报内容：" + ttsStr);
        try {
            tts(AIUIConstant.START, getTtsPeopleParams(), ttsStr.getBytes("utf-8"));
//            TtsConfig.getInstance().startTts(ttsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTtsPeopleParams() {
        if (!TextUtils.isEmpty(ttsPeopleParams)) {
            return ttsPeopleParams;
        }
        ttsPeopleParams = getXiaoyanVoice();
        return ttsPeopleParams;
    }

    private String getXiaofengVoice() {
        StringBuilder params = new StringBuilder();
        params.append("vcn=xiaofeng");
        params.append(",speed=48");
        params.append(",pitch=50");
        params.append(",volume=50");
        params.append(",ent=tts");
        return params.toString();
    }

    private String getXiaoyanVoice() {
        StringBuilder params = new StringBuilder();
        params.append("vcn=xiaoyan");
        params.append(",speed=48");
        params.append(",pitch=50");
        params.append(",volume=50");
        params.append(",ent=tts");
        return params.toString();
    }

    public void ttsCancel() {
        tts(AIUIConstant.CANCEL);
    }

    public void ttsPause() {
        tts(AIUIConstant.PAUSE);
    }

    public void ttsResume() {
        tts(AIUIConstant.RESUME);
    }

    public void resetWakeup() {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
            return;
        }
        AIUIMessage restWakeup = new AIUIMessage(AIUIConstant.CMD_RESET_WAKEUP,
                0, 0, null, null);
        mAIUIAgent.sendMessage(restWakeup);
    }

    private void tts(int cmd) {
        tts(cmd, null, null);
    }

    public void syncData(String param, byte[] data) {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
            return;
        }
        if (null != data) {
            // 用schema数据同步上传联系人
            // 注：数据同步请在连接服务器之后进行，否则可能失败
            AIUIMessage syncAthena = new AIUIMessage(AIUIConstant.CMD_SYNC,
                    AIUIConstant.SYNC_DATA_SCHEMA, 0, param, data);
            mAIUIAgent.sendMessage(syncAthena);
        }
    }

    public void syncSpeakerData(AIUIMessage aiMessage) {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
            return;
        }
        if (null != aiMessage) {
            SitechDevLog.i(AppConst.TAG, "syncSpeakerData====>aiMessage");
            // 注：数据同步请在连接服务器之后进行，否则可能失败
            mAIUIAgent.sendMessage(aiMessage);
        }
    }

    public void syncQuery(String mSyncSid) {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
        }
        if (TextUtils.isEmpty(mSyncSid)) {
            VUI.log("sid 为空");
            return;
        }
        try {
            // 构造查询json字符串，填入同步schema数据返回的sid
            JSONObject queryJson = new JSONObject();
            queryJson.put("sid", mSyncSid);
            // 发送同步数据状态查询消息，设置arg1为schema数据类型，params为查询字符串
            AIUIMessage syncQuery = new AIUIMessage(AIUIConstant.CMD_QUERY_SYNC_STATUS,
                    AIUIConstant.SYNC_DATA_SCHEMA, 0, queryJson.toString(), null);
            mAIUIAgent.sendMessage(syncQuery);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
        }
        AIUIMessage startCmd = new AIUIMessage(AIUIConstant.CMD_START,
                0, 0, null, null);
        mAIUIAgent.sendMessage(startCmd);
    }

    public void stop() {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
        }
        AIUIMessage stopCmd = new AIUIMessage(AIUIConstant.CMD_STOP,
                0, 0, null, null);
        mAIUIAgent.sendMessage(stopCmd);
    }

    private void tts(int cmd, String params, byte[] data) {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
        }
        if (cmd < 0 || cmd > 4) {
            return;
        }
        AIUIMessage ttsCmd = new AIUIMessage(AIUIConstant.CMD_TTS,
                cmd, 0, params, data);
        mAIUIAgent.sendMessage(ttsCmd);
    }


    public void destory() {
        destoryAgent();
    }

    private void startRecord(byte[] data, String params) {
        if (null == mAIUIAgent) {
            VUI.log("AIUIAgent -> null");
            return;
        }
        VUI.log("AIUIAgent -> do startRecord");
        {
            AIUIMessage wakeupMsg = new AIUIMessage(
                    AIUIConstant.CMD_WAKEUP,
                    0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }
        AIUIMessage startRecord = new AIUIMessage(
                AIUIConstant.CMD_START_RECORD,
                0, 0, params, data);
        mAIUIAgent.sendMessage(startRecord);
    }

    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = context.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            params = new String(buffer);
            JSONObject paramsJson = new JSONObject(params);
            params = paramsJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static AIUIEngine getInstance(AIUIListener listener) {
        aiuiListener = listener;
        return NLPEngineHolder.INSTANCE;
    }

    private static class NLPEngineHolder {
        private static final AIUIEngine INSTANCE = new AIUIEngine();
    }

    /**
     * 设置个性化(动态实体和所见即可说)生效参数
     *
     * @param persParams
     */
    public void setPersParams(JSONObject persParams) {
        try {
            //参考文档动态实体生效使用一节
            JSONObject params = new JSONObject();
            JSONObject audioParams = new JSONObject();
            audioParams.put("pers_param", persParams.toString());
            params.put("audioparams", audioParams);

//            sendMessage();
            AIUIMessage message = new AIUIMessage(AIUIConstant.CMD_SET_PARAMS, 0, 0, params.toString(), null);
            //确保AIUI处于唤醒状态
//            if (mCurrentState != AIUIConstant.STATE_WORKING && !isWakeUpEnable) {
//                mAgent.sendMessage(new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null));
//            }

            mAIUIAgent.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
