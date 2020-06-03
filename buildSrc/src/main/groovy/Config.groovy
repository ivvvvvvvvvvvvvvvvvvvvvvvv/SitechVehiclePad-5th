import org.gradle.api.JavaVersion

class Config {
    static applicationId = 'com.sitechdev.vehicle.pad'

    static compileSdkVersion = 28
    static targetSdkVersion = 28
    static minSdkVersion = 21
    static appVersionCode = 100
    static appVersionName = '1.0.0'
    static sourceCompatibilityVersion = JavaVersion.VERSION_1_8
    static targetCompatibilityVersion = JavaVersion.VERSION_1_8

    static signInfo = [
            storeKey     : '../key/sitechdev_key.jks',
            storePassword: 'Sitech!@Dev~#',
            keyAlias     : 'Sitech',
            keyPassword  : 'Sitech~#Dev!@'
    ]

    static depConfig = [
            plugin             : [
                    gradle: "com.android.tools.build:gradle:3.6.3",
            ],
            android_support    : [
                    junit       : 'junit:junit:4.12',
                    espresso    : 'com.android.support.test.espresso:espresso-core:3.0.2',
                    testRunner  : 'com.android.support.test:runner:1.0.2',
                    appcompatV7 : 'com.android.support:appcompat-v7:28.0.0',
                    recyclerview: 'com.android.support:recyclerview-v7:28.0.0',
                    annotations : 'com.android.support:support-annotations:28.0.0',
                    constraint  : 'com.android.support.constraint:constraint-layout:1.1.3',
                    multidex    : 'com.android.support:multidex:1.0.3',
                    design      : 'com.android.support:design:28.0.0',
            ],
            google             : [
                    gson : 'com.google.code.gson:gson:2.8.6',
                    zxing: 'com.google.zxing:core:3.4.0',
            ],
            okgo               : [
                    okgo    : 'com.lzy.net:okgo:3.0.4',
                    okserver: 'com.lzy.net:okserver:2.0.5',
            ],
            okhttp             : [
                    Okhttp_version              : 'com.squareup.okhttp3:okhttp:3.12.10',
                    Okio_version                : 'com.squareup.okio:okio:1.17.5',
                    logging_interceptor_version : 'com.squareup.okhttp3:logging-interceptor:3.12.10',
                    okhttp_urlconnection_version: 'com.squareup.okhttp3:okhttp-urlconnection:3.12.10',
            ],
            amap               : [
                    //3D地图so及jar
                    amap        : 'com.amap.api:3dmap:latest.integration',
                    //搜索功能
                    amapLocation: 'com.amap.api:location:latest.integration',
                    //搜索功能
                    amapSearch  : 'com.amap.api:search:latest.integration',
            ],
            glide              : [
                    glide_version   : 'com.github.bumptech.glide:glide:4.9.0',
                    glideIntegration: 'com.github.bumptech.glide:okhttp3-integration:4.9.0',
                    glideCompiler   : 'com.github.bumptech.glide:compiler:4.9.0',
            ],
            butterknife        : [
                    butterknife        : 'com.jakewharton:butterknife:8.8.1',
                    butterknifeCompiler: 'com.jakewharton:butterknife-compiler:8.8.1',
            ],
            eventbus           : [
                    eventbus         : 'org.greenrobot:eventbus:3.2.0',
                    eventbusProcessor: 'org.greenrobot:eventbus-annotation-processor:3.2.0',
            ],
            //kaola-tingban
            tingban            : 'com.kaolafm:open-sdk:1.5.0',
            //下拉刷新组件
            smartrefresh       : 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5.1',
            tkrefreshlayout    : 'com.lcodecorex:tkrefreshlayout:1.0.7',
            //rxjava--rxandroid
            rxjava             : 'io.reactivex.rxjava2:rxjava:2.1.3',
            rxandroid          : 'io.reactivex.rxjava2:rxandroid:2.0.1',
            //sitech maven
            dataStatistics     : 'com.sitechdev.vehicle:datastatistics:1.0.10',
            //androidUtilCode
            utilCode           : 'com.blankj:utilcode:1.29.0',
            FlycoTabLayout     : 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar',
            //pingyin
            tinypinyin         : 'com.github.promeg:tinypinyin:2.0.3',
            tinypinyin_compiler: 'com.github.promeg:tinypinyin-lexicons-android-cncity:2.0.3',
            //腾讯相关组件================
            tencent            : [
                    //腾讯-bugly
                    bugly_version          : 'com.tencent.bugly:crashreport:latest.release',
                    bugly_nativecrashreport: 'com.tencent.bugly:nativecrashreport:latest.release',
                    //腾讯-MMKV
                    mmkv                   : 'com.tencent:mmkv:1.1.1',
                    //腾讯-xlog
                    xlog                   : 'com.tencent.mars:mars-xlog:1.2.3',
            ],
            //阿里相关组件================
            ali                : [
                    //arouter
                    arouter_api     : 'com.alibaba:arouter-api:1.5.0',
                    arouter_compiler: 'com.alibaba:arouter-compiler:1.2.2',
                    arouter_register: 'com.alibaba:arouter-register:1.0.2',
            ],
            //AOP相关组件================
            aop                : [
                    //aop
                    aspectjx : 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10',
                    aspectjrt: 'org.aspectj:aspectjrt:1.9.5',
            ],
            //换肤组件
            skin_support       : [
                    skin_support                  : 'skin.support:skin-support:3.1.4',                   // skin-support 基础控件支持
                    skin_support_design           : 'skin.support:skin-support-design:3.1.4',            // skin-support-design material design 控件支持[可选]
                    skin_support_cardview         : 'skin.support:skin-support-cardview:3.1.4',                 // skin-support-cardview CardView 控件支持[可选]
                    skin_support_constraint_layout: 'skin.support:skin-support-constraint-layout:3.1.4', // skin-support-constraint-layout ConstraintLayout 控件支持[可选]
            ],
            //滚动动画的文字效果
            rollingText        : 'com.github.YvesCheung:RollingText:1.2.6'
    ]
}