package com.sitechdev.vehicle.pad.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.lib.event.AppEvent;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：RouterInterceptor
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/09 0009 15:00
 * 修改时间：
 * 备注：
 */
@Interceptor(priority = 8, name = "处理跳转过程中的路由逻辑")
public class RouterInterceptor implements IInterceptor {
    private static final String TAG = "Router";

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        SitechDevLog.i(TAG, "[RouterInterceptor--process()]==>");
        if (RouterUtils.getInstance().isCurrentPage(postcard)) {
            // 觉得有问题，中断路由流程
            callback.onInterrupt(new RuntimeException("the currentPage is Showing"));
        } else {
            //当前要跳转的新Path
            String jumpNewPath = postcard.getPath();
            //取出新path的页面级别,发出跳转事件
            JSONObject pathObject = getPathJsonObject(jumpNewPath);
            String level = pathObject.optString("level");
            if (IRouterConstants.LEVEL_THIRD.equalsIgnoreCase("/" + level)) {
                //是三级页面，才发事件
                EventBusUtils.postEvent(new AppEvent(AppEvent.EB_ACTIVITY_START_EVENT, pathObject.toString()));
            }
            // 处理完成，交还控制权
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        SitechDevLog.i(TAG, "[RouterInterceptor--init()]==>");
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
    }

    private JSONObject getPathJsonObject(String path) {
        JSONObject pathObject = new JSONObject();
        try {
            //切割字符串
            String[] pathSplits = path.split("/");
            //数组组成格式为：【无用，module_name,level_name,group_name,path&param】
            if (pathSplits != null && pathSplits.length > 0) {
                pathObject.put("module", pathSplits[1]);
                pathObject.put("level", pathSplits[2]);
                pathObject.put("group", pathSplits[3]);
                pathObject.put("realPath", pathSplits[4]);
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return pathObject;
    }
}
