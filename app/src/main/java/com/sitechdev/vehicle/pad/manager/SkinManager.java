package com.sitechdev.vehicle.pad.manager;

import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.model.SkinModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import skin.support.utils.SkinPreference;

import static com.sitechdev.vehicle.pad.model.SkinModel.SKIN_BLUE_ORANGE;
import static com.sitechdev.vehicle.pad.model.SkinModel.SKIN_DEFAULT;
import static com.sitechdev.vehicle.pad.model.SkinModel.SKIN_WHITE_ORANGE;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：SkinManager
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/06 0006 14:19
 * 修改时间：
 * 备注：
 */
public class SkinManager {

    private SkinManager() {
        EventBusUtils.register(this);
    }

    private static final class Single {
        private static final SkinManager SINGLE = new SkinManager();
    }

    public static SkinManager getInstance() {
        return Single.SINGLE;
    }

    /**
     * 初始化换肤组件
     *
     * @param app AppApplication
     */
    public void initSkinManager(AppApplication app) {
        SkinCompatManager.withoutActivity(app)
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .setSkinAllActivityEnable(true)
                .loadSkin();
    }

    /**
     * 加载皮肤资源
     *
     * @param skinName
     */
    public void loadSkin(String skinName) {
        SkinCompatManager.getInstance().loadSkin(skinName, new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {
                SitechDevLog.i("SkinManager", "换肤事件监听：onStart===>");
            }

            @Override
            public void onSuccess() {
                SitechDevLog.i("SkinManager", "换肤事件监听：onSuccess===>" + SkinPreference.getInstance().getSkinName());
                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                ToastUtils.showShort("切换为： " + getSkinDesc(SkinPreference.getInstance().getSkinName()));
            }

            @Override
            public void onFailed(String errMsg) {
                SitechDevLog.i("SkinManager", "换肤事件监听：onFailed===>" + errMsg);
            }
        }, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAppEvent(AppEvent event) {
        switch (event.getEventKey()) {
            case AppEvent.EVENT_APP_CHANGE_SKIN:
                if (event.getEventValue() == null) {
                    return;
                }
                SkinModel skinModel = (SkinModel) event.getEventValue();
                if (skinModel == null) {
                    return;
                }
                SitechDevLog.i("SkinManager", "换肤事件监听：===>" + skinModel.getSkinDesc());
                if (!StringUtils.isEmpty(skinModel.getSkinTag())) {
                    loadSkin(skinModel.getSkinTag());
                }
                break;
            default:
                break;
        }
    }

    private String getSkinDesc(String skinName) {
        switch (SkinModel.getByName(skinName)) {
            case SKIN_WHITE_ORANGE:
                return SKIN_WHITE_ORANGE.getSkinDesc();
            case SKIN_BLUE_ORANGE:
                return SKIN_BLUE_ORANGE.getSkinDesc();
            case SKIN_DEFAULT:
            default:
                return SKIN_DEFAULT.getSkinDesc();
        }
    }
}
