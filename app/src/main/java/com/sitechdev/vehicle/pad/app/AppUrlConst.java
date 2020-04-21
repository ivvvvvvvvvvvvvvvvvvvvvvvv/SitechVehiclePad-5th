package com.sitechdev.vehicle.pad.app;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppUrlConst
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/13 0013 20:17
 * 修改时间：
 * 备注：
 */
public interface AppUrlConst {

    int HTTPRESPONSE_200 = 200;
    int HTTPRESPONSE_201 = 201;
    int HTTPRESPONSE_202 = 202;
    int HTTPRESPONSE_400 = 400;
    int HTTPRESPONSE_300 = 300;
    int HTTPRESPONSE_401 = 401;
    int HTTPRESPONSE_403 = 403;
    int HTTPRESPONSE_500 = 500;

    String HTTP_CODE_200 = "200";
    String HTTP_CODE_201 = "201";
    String HTTP_CODE_202 = "202";
    String HTTP_CODE_400 = "400";
    String HTTP_CODE_401 = "401";
    String HTTP_CODE_403 = "403";
    String HTTP_CODE_500 = "500";
    /**
     * 登录身份验证失败。需要请求refreshToken接口刷新用户Token
     */
    String ERRORCODE_NEED_REQUEST_TOKEN_403_8003 = "10008003";
    /**
     * 登录身份已失效，需要退出登录后，让用户重新登录。
     * 10008001，登录身份不能为空
     * 10008002，登录身份验证失败
     */
    String ERRORCODE_NEED_RELOGIN_401_8001 = "10008001";
    String ERRORCODE_NEED_RELOGIN_401_8002 = "10008002";

    String ERRORCODE_400_12002001 = "12002001";//数据未找到
    String ERRORCODE_400_12022007 = "12022007";//您不是车主无权对该车辆行使控制权
    String ERRORCODE_400_12032007 = "12032007";//未激活

    /**
     * 用户注册协议链接地址
     */
    String USER_AGREEMENT_URL = "/protocol/html/agreement.html?pid=001";
    /**
     * 用户隐私协议链接地址
     */
    String USER_PRIVACY_URL = "/protocol/html/agreement.html?pid=002";

    /**
     * 获取验证码
     */
    String URL_PHONEVALID = "/sitechid/v2/captcha/";
    /**
     * 手机验证码登录
     */
    String URL_USER_LOGIN = "/sitechid/v2/login";
    /**
     * 核对验证码
     */
    String URL_CHECK_VALIDATE = "/sitechid/v2/captcha/%s";

    /**
     * 刷新Token认证接口
     */
    String URL_REFRESHTOKEN = "/sitechid/v2/refreshtoken";
    /**
     * Personal car info
     * 尤金
     */
    public static final String URL_GET_PERSONALINFO = "/sitechid/v2/user/";
    /**
     * 获取登录身份的用户id
     */
    public static final String URL_GET_VALID_ID = "/sitechid/v2/id";
    public static final String URL_GET_CARINFO = "/vehicle/v2/control/list";
    public static final String URL_GET_POINTSINFO = "/user/integral/v2";
    public static final String URL_POST_POINTS = "/user/sign/v1";
    public static final String URL_GET_POINTSSTATUS = "/user/signStatus/v1";
    public static final String URL_POST_WORDCHAINS = "/applications/v1/games/wordchains";
    /**
     * 用户反馈信息提交
     */
    String URL_FEEDBACK_SUBMIT = "/activity/feedback/submit";
    /**
     * 用户反馈图片上传
     */
    String URL_FEEDBACK_IMAGE_UPLOAD = "/forum/v1/uploads";
    /**
     * 用户反馈类型获取
     */
    String URL_FEEDBACK_TYPELIST = "/activity/feedback/getTypeList";
    /**
     * 平台类型：1 - 商城 ；2 - 车联网
     */
    int HTTP_PLAT_TYPE = 2;


    /**
     * 用户积分
     */
    String GET_USER_CREDITS = "/user/myIntegral";

    /**
     * 天气接口
     */
    String GET_WEATHER_DATA = "/applications/v1/weather/coordinate";
}
