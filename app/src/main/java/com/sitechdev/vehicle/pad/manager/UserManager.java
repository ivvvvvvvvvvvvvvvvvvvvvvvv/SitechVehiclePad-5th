package com.sitechdev.vehicle.pad.manager;

import android.util.Base64;

import com.sitechdev.net.GsonUtils;
import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.bean.UserBean;
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

    private UserBean mUser = null;
    private boolean hasCar = false;

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

    public UserBean getUser() {
        if (mUser == null) {
            mUser = GsonUtils.jsonToBean(getLocalUserInfo(), UserBean.class);
        }
        if (mUser == null) {
            mUser = new UserBean();
        }
        return mUser;
    }

    public String getUserId4RestoreTag() {
        String tag = getUser().getUserId();
        if (StringUtils.isEmpty(tag)) {
            tag = "temp";
        }
        return tag;
    }

    public String getUserToken() {
        if (mUser != null) {
            if (mUser.getCredential() != null && !StringUtils.isEmpty(mUser.getCredential().getAccessToken())) {
                return mUser.getCredential().getAccessToken();
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
            mUser = GsonUtils.jsonToBean(userInfo, UserBean.class);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        if (mUser == null) {
            mUser = new UserBean();
        }
    }

    public void saveUserInfo(String info) {
        try {
            //请求正确响应，内存对象赋值。
            setUserClass(info);

            //本地清单文件保存登录用户信息
            saveUserInfo();
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 保存用户个人信息
     */
    public void saveUserInfo() {
        saveUserInfo(getUser());
    }

    /**
     * 保存用户个人信息
     */
    public void saveUserInfo(UserBean user) {
        if (user == null) {
            return;
        }
        this.mUser = user;
        if (isExistUserToken()) {
            try {
                //请求正确响应，内存对象赋值。
                String userInfo = GsonUtils.toJson(user);
                //本地清单文件保存登录用户信息
                writeToShareSP(AppConst.KEY_USER, Base64.encodeToString(userInfo.getBytes(), Base64.DEFAULT));
            } catch (Exception e) {
                SitechDevLog.exception(e);
            }
        }
    }

    /**
     * 读取用户个人信息
     */
    public String getLocalUserInfo() {
        return new String(Base64.decode(SPUtils.getValue(AppApplication.getContext(), AppConst.KEY_USER, ""), Base64.DEFAULT));
    }

    public void logoutUser() {
        try {
//            CarManager.getInstance().setCarModelClazz(null);
            //清掉用户缓存
//            if (null != mUser) {
//                mUser.getCredential().setAccessToken("");
//                mUser.getCredential().setCmdTokenDTO(new CmdBean("", ""));
//            }
            mUser = new UserBean();
            //刷新用户信息
            removeUserInfoCache();
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    private void removeUserInfoCache() {
        SPUtils.putValue(AppApplication.getContext(), AppConst.KEY_USER, "");
        setLoginUserBean(null);
        setPointsSigninBean(null);
    }

    public void writeToShareSP(String key, String value) {
        SPUtils.putValue(AppApplication.getContext(), key, value);
    }

    public String getUserRelationID() {
        return userRelationID;
    }

    public void setUserRelationID(String userRelationID) {
        this.userRelationID = userRelationID;
    }

    public String getUserReferralCode() {
        return userReferralCode;
    }

    public void setUserReferralCode(String userReferralCode) {
        this.userReferralCode = userReferralCode;
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

    public LoginUserBean getLoginUserBean() {
        if (loginUserBean == null) {
            //查看是否有保存的数据
            loginUserBean = (LoginUserBean) SPUtils.get(AppApplication.getContext(), AppConst.SP_KEY_LOGIN_USER);
        }
        return loginUserBean;
    }

    public void setLoginUserBean(LoginUserBean loginUserBean) {
        this.loginUserBean = loginUserBean;
        //保存
        SPUtils.save(AppApplication.getContext(), AppConst.SP_KEY_LOGIN_USER, (loginUserBean == null) ? "" : loginUserBean);
    }
}
