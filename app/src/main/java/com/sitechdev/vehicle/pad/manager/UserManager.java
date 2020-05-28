package com.sitechdev.vehicle.pad.manager;

import android.util.Base64;

import com.blankj.utilcode.util.EncodeUtils;
import com.sitechdev.net.GsonUtils;
import com.sitechdev.vehicle.lib.util.ParamsUtil;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;
import com.sitechdev.vehicle.pad.module.member.bean.PointsSigninBean;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：UserManager
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 20:48
 * 修改时间：
 * 备注：
 */
public class UserManager {

    /**
     * 第三方登录授权接口处理后返回的relationId，如：微信code授权接口，返回的relationId，使用该id与微信用户进行关联绑定
     */
    private String userRelationID = "";
    /**
     * 推荐识别代码，记录推荐人id、推荐码、第三方导流识别码、微信小程序二维码的场景值等
     */
    private String userReferralCode = "";

    private PointsSigninBean pointsSigninBean = null;

    private LoginUserBean loginUserBean = null;

    private UserManager() {
    }

    private static class Single {
        private static final UserManager singleUserManager = new UserManager();
    }

    public static UserManager getInstance() {
        return Single.singleUserManager;
    }

    /**
     * 获取userBean对象
     *
     * @deprecated 请使用  {@link getLoginUserBean}
     */
    public LoginUserBean getUser() {
        return getLoginUserBean();
    }

    public LoginUserBean getLoginUserBean() {
        if (loginUserBean == null) {
            loginUserBean = GsonUtils.jsonToBean(getLocalUserInfo(), LoginUserBean.class);
        }
        if (loginUserBean == null) {
            loginUserBean = new LoginUserBean();
        }
        return loginUserBean;
    }

    public String getUserToken() {
        if (loginUserBean != null) {
            if (loginUserBean.getCredential() != null && !StringUtils.isEmpty(loginUserBean.getCredential().getAccessToken())) {
                return loginUserBean.getCredential().getAccessToken();
            }
        }
        return "";
    }

    /**
     * 是否存在Token。
     *
     * @return true=存在
     */
    public boolean isExistUserToken() {
        return !StringUtils.isEmpty(getUserToken());
    }


    public void setUserClass(String userInfo) {
        try {
            loginUserBean = GsonUtils.jsonToBean(userInfo, LoginUserBean.class);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        if (loginUserBean == null) {
            loginUserBean = new LoginUserBean();
        }
    }

    /**
     * 保存用户个人信息
     */
    public void saveUserInfo(String info) {
        try {
            //请求正确响应，内存对象赋值。
            setUserClass(info);

            //本地清单文件保存登录用户信息
            setLoginUserBean(loginUserBean);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 保存用户个人信息
     */
    public void saveUserInfo(LoginUserBean user) {
        if (user == null) {
            return;
        }
        this.loginUserBean = user;
        if (isExistUserToken()) {
            try {
                setLoginUserBean(loginUserBean);
            } catch (Exception e) {
                SitechDevLog.exception(e);
            }
        }
    }

    /**
     * 读取用户个人信息
     */
    public String getLocalUserInfo() {
        String userStr = ParamsUtil.getStringData(AppConst.SP_KEY_LOGIN_USER);
        SitechDevLog.i("Login", "userStr====>" + userStr);
        return userStr;
    }

    /**
     * 退出登录
     */
    public void logoutUser() {
        removeUserInfoCache();
    }

    private void removeUserInfoCache() {
        ParamsUtil.setData(AppConst.KEY_USER, "");
        setLoginUserBean(null);
        setPointsSigninBean(null);
    }

    public void setLoginUserBean(LoginUserBean loginUserBean) {
        this.loginUserBean = loginUserBean;
        if (loginUserBean != null) {
            String userStr = GsonUtils.toJson(loginUserBean);
            SitechDevLog.i("Login", "setLoginUserBean userStr====>" + userStr);
            //保存
            ParamsUtil.setData(AppConst.SP_KEY_LOGIN_USER, userStr);
        } else {
            ParamsUtil.removeValue(AppConst.SP_KEY_LOGIN_USER);
        }
    }

    public PointsSigninBean getPointsSigninBean() {
        return pointsSigninBean;
    }

    public void setPointsSigninBean(PointsSigninBean pointsSigninBean) {
        this.pointsSigninBean = pointsSigninBean;
    }

    /**
     * 判断用户今天是否已经签到
     *
     * @return true=已签到
     */
    public boolean isUserSignToday() {
        if (this.pointsSigninBean == null) {
            return false;
        }
        return "-1".equals(pointsSigninBean.data.getIntegral());
    }
}
