package com.sitechdev.net;

/**
 * 环境切换配置
 */
public class EnvironmentConfig {

    public static String URL_ROOT_HOST = "https://cloud-api.sitechdev.com";
    public static String URL_MALL_HOST = "https://ecommerce-api.sitechdev.com";
    public static String URL_EXCHANGE_HOST = "https://exchange-api.sitechdev.com";
    public static String H5_HOST = "https://automobile.sitechdev.com";
    public static String BBS_HOST = "https://app-api.sitechdev.com";
    public static String RSA_PUBLIC_KEY = "";

    /**
     * 是否正式环境
     *
     * @param isRelease true=正式环境
     */
    public static void init(boolean isRelease) {
        isRelease = true;
        if (isRelease) {
            URL_ROOT_HOST = "https://cloud-api.sitechdev.com";
            URL_MALL_HOST = "https://ecommerce-api.sitechdev.com";
            URL_EXCHANGE_HOST = "https://exchange-api.sitechdev.com";
            BBS_HOST = "https://app-api.sitechdev.com";
            H5_HOST = "https://automobile.sitechdev.com";
            RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDuWrftTh0wJOzyLbfrMx5hUlN048IJ2qwmh0luvaXTeNJs1UN0rQjIES8TCQ3AhIUUm1LxPnGdVzNL0OUbK4GC71Q+eJ+/z5PlX6HH8MlJaJAh1onc8QIqmcuvtb1fU6VDfJSYX73U8DQtqPN0eVKbDM4YLXsRapqufrDIyzWYxQIDAQAB";
        } else {
            URL_ROOT_HOST = "https://qa-cloud-api.sitechdev.com";
            URL_MALL_HOST = "https://qa-ecommerce-api.sitechdev.com";
            URL_EXCHANGE_HOST = "https://exchange-api.sitechdev.com";
            BBS_HOST = "https://qa-app-api.sitechdev.com";
            H5_HOST = "http://test-automobile.sitechdev.com";
            RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDioWy+UD0bgYBjMyFvbECEESXeaDGzk3hg6Oj96PUQCsjZgvTkOez4AtLvQwoaA6O5eOGBoIEPuShfdoYbSutWwJRt+OYydG9LXZBVWk9Pfb3+o2Sx557YOt7ln1esYdq1IRcPiMGzs9FXKqYfREqYCqFrDZiRgmr9ftezeuri4QIDAQAB";
        }
    }
}