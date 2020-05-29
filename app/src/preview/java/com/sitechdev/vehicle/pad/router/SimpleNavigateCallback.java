package com.sitechdev.vehicle.pad.router;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.sitechdev.vehicle.lib.util.SitechDevLog;

/**
 * 路由监听
 *
 * @author liuhe
 * @date 2019/03/23
 */
public class SimpleNavigateCallback implements NavigationCallback {

    private static final String TAG = SimpleNavigateCallback.class.getSimpleName();

    @Override
    public void onFound(Postcard postcard) {
        SitechDevLog.d(TAG, "router找到了~");
    }

    @Override
    public void onLost(Postcard postcard) {
        SitechDevLog.d(TAG, "router丢失~");
    }

    @Override
    public void onArrival(Postcard postcard) {
        SitechDevLog.d(TAG, "router跳转完成~");
    }

    @Override
    public void onInterrupt(Postcard postcard) {
        SitechDevLog.d(TAG, "router被拦截~");
    }
}
