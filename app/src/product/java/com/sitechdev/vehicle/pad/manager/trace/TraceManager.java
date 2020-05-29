package com.sitechdev.vehicle.pad.manager.trace;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sitechdev.vehicle.lib.util.CollectionUtils;
import com.sitechdev.vehicle.lib.util.ObjectUtils;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.xtev.trace.TraceClient;
import com.xtev.trace.event.BaseEvent;
import com.xtev.trace.event.NormalEvent;
import com.xtev.trace.event.VoiceEvent;
import com.xtev.trace.utils.TraceUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 埋点管理类
 *
 * @author liuhe
 * @date 2019/06/27
 */
public class TraceManager {

    private VoiceEvent voiceEvent;

    private void TrackManger() {
    }

    private static final class Holder {
        private static final TraceManager instance = new TraceManager();
    }

    public static TraceManager getInstance() {
        return Holder.instance;
    }

    public void setUserId(String userId) {
        TraceClient.setUserId(userId);
    }

    /**
     * 点击埋点事件提交
     *
     * @param pa    页面标识
     * @param point 埋点id
     */
    public void traceClick(Class pa, String point) {
        trace(pa, point, StatisticsEventType.CLICK);
    }

    /**
     * 点击埋点事件提交
     *
     * @param pa    页面标识
     * @param point 埋点id
     * @param evj   业务相关内容
     */
    public void traceClick(Class pa, String point, HashMap evj) {
        trace(pa, point, StatisticsEventType.CLICK, evj);
    }

    /**
     * 页面进入或者退出
     *
     * @param clazz      页面名称
     * @param pageStatus 0 TraceClient.PAGE_IN；1 TraceClient.PAGE_OUT
     */
    public void tracePage(Class clazz, int pageStatus) {
        TraceClient.tracePage(clazz, pageStatus);
    }

    /**
     * 埋点事件提交
     *
     * @param pa                  页面标识
     * @param statisticsEventType 埋点类型
     * @param point               埋点id
     */
    public void trace(@NonNull Class pa, @NonNull String point, StatisticsEventType statisticsEventType) {
        trace(pa, point, statisticsEventType, null);
    }

    /**
     * 埋点事件提交
     *
     * @param pa                  页面标识
     * @param point               埋点id
     * @param statisticsEventType 埋点类型
     * @param evj                 业务相关内容
     */
    public void trace(@NonNull Class pa, @NonNull String point, StatisticsEventType statisticsEventType, HashMap<String, String> evj) {
        CommonTrace commonTrace = new CommonTrace();
        commonTrace.pa = pa;
        commonTrace.evj = evj;
        if (null == statisticsEventType) {
            commonTrace.ev = StatisticsEventType.CLICK.getName();
        } else {
            if (TraceUtils.isPageEvent(statisticsEventType.getName())){
                commonTrace.ev = statisticsEventType.getName() + pa.getSimpleName();
            }else {
                commonTrace.ev = statisticsEventType.getName() + point;
            }
        }
        commonTrace.point = point;
        trace(commonTrace);
    }

    /**
     * 埋点事件提交
     *
     * @param trace 埋点事件
     */
    public void trace(CommonTrace trace) {
        if (null == trace) {
            return;
        }
        NormalEvent.Builder builder = new NormalEvent.Builder();
        if (!ObjectUtils.isEmpty(trace.pa)) {
            builder.pa(trace.pa);
        }
        if (!StringUtils.isEmpty(trace.ev)) {
            builder.ev(trace.ev);
        }
        if (!StringUtils.isEmpty(trace.point)) {
            builder.point(trace.point);
        }
        if (!CollectionUtils.isEmpty(trace.evj)) {
            for (Map.Entry<String, String> entry : trace.evj.entrySet()) {
                builder.evj(entry.getKey(), entry.getValue());
            }
        }
        if (trace.quick) {
            builder.commitQuickly();
        } else {
            builder.commit();
        }
    }

    /**
     * 直接埋点事件
     */
    public void traceEvent(BaseEvent normalEvent) {
        if (null != normalEvent) {
            TraceClient.traceEvent(normalEvent);
        }
    }

    /**
     * 快速埋点事件
     */
    public void traceEventQuick(BaseEvent normalEvent) {
        if (null != normalEvent) {
            TraceClient.traceEventQuickly(normalEvent);
        }
    }

    /**
     * {"operation":"PLAY","focus":"music","artist":"刘德华",
     * "normal_text":"我想听刘德华的歌"}
     *
     * @param json
     */
    public void traceVoiceIn(JSONObject json) {
        if (null == json) {
            return;
        }
        if (null != voiceEvent && TextUtils.isEmpty(voiceEvent.getResponsecontent())) {
            VoiceEvent temp = voiceEvent;
            TraceClient.traceEvent(temp);
        }
        voiceEvent = new VoiceEvent();
        voiceEvent.setVerbalTrick(json.optString("normal_text"));
        voiceEvent.setFunctionPoint(json.optString("focus"));
        voiceEvent.setIntention(json.optString("operation"));
        voiceEvent.setEvent(voiceEvent.getIntention() + voiceEvent.getFunctionPoint());
        voiceEvent.setOriginaljson(json.toString());
        if (TextUtils.isEmpty(voiceEvent.getIntention()) &&
                TextUtils.isEmpty(voiceEvent.getFunctionPoint())) {
            voiceEvent.setSuccessFlag(1);
        } else {
            voiceEvent.setSuccessFlag(0);
        }
    }

    public void traceVoiceOut(String out) {
        if (null != voiceEvent) {
            voiceEvent.setResponsecontent(out);
            VoiceEvent temp = voiceEvent;
            TraceClient.traceEvent(temp);
            voiceEvent = null;
        }
    }
}
