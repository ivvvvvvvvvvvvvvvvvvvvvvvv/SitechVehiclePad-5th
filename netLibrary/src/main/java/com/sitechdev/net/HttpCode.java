package com.sitechdev.net;

public class HttpCode {
    /**
     * 200,成功。
     * 202,成功，等待回执。
     * 400,业务异常（看业务码）。
     * 500,系统异常。
     * 401,身份异常。
     * 403,身份过期。
     * 406,参数错误。
     * <p>
     * 业务码
     * 10008001=登录身份不能为空
     * <p>
     * 10008002=登录身份验证失败
     * <p>
     * 15001003=参数格式不正确
     * <p>
     * 15009001=系统异常
     * <p>
     * 10008001=登录身份不能为空
     * 10008002=登录身份验证失败
     * 15001003=参数格式不正确
     * <p>
     * 15004001=违章查询失败
     * 15004002=暂不提供该城市的查询
     * 15004003=车辆识别号有误
     * 15004004=发动机号有误
     * 15004005=车牌号有误
     */
    public static final String HTTP_OK = "200";
    public static final String HTTP_EMPTY = "201";
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_INTERNAL_SERVER_ERROR = "500";
    public static final String HTTP_FORBIDDEN = "403";
    public static final String LOGIN_AUTHENTICATION_FAILED_NUM = "10008002";


    /**
     * 单点登录被踢出状态码
     */
    public static final String HTTP_UNAUTHORIZED = "401";
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;

}
