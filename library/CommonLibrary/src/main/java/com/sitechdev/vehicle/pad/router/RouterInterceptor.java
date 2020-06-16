package com.sitechdev.vehicle.pad.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.sitechdev.vehicle.lib.util.SitechDevLog;

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
            callback.onInterrupt(new RuntimeException("the currentPage is Showing"));      // 觉得有问题，中断路由流程
        } else {
            callback.onContinue(postcard);  // 处理完成，交还控制权
        }
    }

    @Override
    public void init(Context context) {
        SitechDevLog.i(TAG, "[RouterInterceptor--init()]==>");
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
    }
}
