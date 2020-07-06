package com.sitechdev.vehicle.pad.module.setting.teddy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：HZ_SitechDOS
 * 类名称：TeddyConstants
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：18-6-13 下午8:17
 * 修改时间：
 * 备注：讯飞语音 相关常量定义
 */
public class TeddyConstants {
    /**
     * LOG-TAG
     */
    public static final String TAG_TEDDY = "TeddyVoice";
    /**
     * 默认唤醒词
     */
    private static final String[] MVW_DEFAULT_KEYWORDS = {"你好Teddy", "Teddy,Teddy", "Hello,Teddy", "Hi,Teddy"};
    public static final List<String> MVW_DEFAULT_KEYWORDS_LIST = Collections.unmodifiableList(Arrays.asList(MVW_DEFAULT_KEYWORDS));
    /**
     * 自定义唤醒词
     */
    private static final String[] MVW_DIY_KEYWORDS = {
            //商城
            "立即购买", "选择收货地址", "查看地址", "选择地址", "修改地址", "修改收货地址", "提交订单", "下单",
            "立即支付", "用积分", "不使用积分", "不用积分", "上一页", "下一页",
            //选择类型
            "第一个", "第二个", "第三个", "第四个", "第五个", "第六个", "第七个", "第八个", "第九个", "第十个",
            "第十一个", "第十二个", "第十三个", "第十四个", "第十五个", "第十六个", "第十七个", "第十八个", "第十九个", "第二十个",
            "支付宝", "微信", "一网通", "银行卡", "取消订单", "确定", "不取消", "再想想"
            //, "返回"
            //歌曲，音视频
//            "上一首", "下一首", "暂停播放", "继续播放",
//            //收音机
//            "搜索电台", "下一电台", "上一电台",
//            //空调
//            "打开空调", "关闭空调", "最大制冷", "空调制冷", "空调制热", "空调吹面", "空调吹脚", "吹面吹脚", "内循环", "外循环",
//            "前除霜", "前除雾", "后除霜", "后除雾", "风太大", "风太小", "太热", "太冷",// "热死",  "冷死", "冻死",
//            //CMD
//            "声音太大", "声音大", "声音小", "声音太小", "声太大", "声太小", "静音", "解除静音",// "吵死", "太吵",
//            //MAP
//            "缩小地图", "放大地图", "取消导航"
    };
    public static final List<String> MVW_DIY_KEYWORDS_LIST = Collections.unmodifiableList(Arrays.asList(MVW_DIY_KEYWORDS));
    /**
     * 默认发音人--true=男声、false=女声
     */
    public static final boolean DEFAULT_SEX_SPEAKER = false;

    /**
     * 自定义语义识别本地词典--app类型
     */
    private static final String[] SR_DICT_APPS_ARRAY = {"订单列表", "首页", "商城", "精品商城", "回到导航", "导航页面", "音乐列表", "音乐", "车辆控制",
            "网络", "网络电台", "饭聊", "前除霜", "后除霜", "前除雾", "后除雾", "车头方向", "喜马拉雅", "酷我音乐", "语音", "热点",
            "增大地图", "carlife", "wifi", "图库", "天气", "消息盒子", "解除静音", "静音解除", "听伴", "考拉", "在线电台"};
    public static final List<String> SR_DICT_APPS_ARRAY_LIST = Collections.unmodifiableList(Arrays.asList(SR_DICT_APPS_ARRAY));
    /**
     * 自定义语义识别本地词典--CMD类型
     */
    private static final String[] SR_DICT_CMDS_ARRAY = {"呼叫", "收藏为家", "收藏为公司", "静音",
            "向上搜台", "向下搜台", "播放", "解除静音", "3d地图", "2d地图", "车头方向"};
    public static final List<String> SR_DICT_CMDS_ARRAY_LIST = Collections.unmodifiableList(Arrays.asList(SR_DICT_CMDS_ARRAY));

    /**
     * 自动唤醒开关--开启
     */
    public static final String TEDDY_AUTOMVW_OPEN = "true";
    /**
     * 自动唤醒开关--关闭
     */
    public static final String TEDDY_AUTOMVW_CLOSE = "false";
    /**
     * 发音人性别
     */
    public static final String TEDDY_SEX_IS_MALE = "TEDDY_SEX_IS_MALE";
    /**
     * 自动唤醒开关--配置key
     */
    public static final String TEDDY_SPKEY_MVW_AUTOOPEN = "TEDDY_SPKEY_MVW_AUTOOPEN";
    /**
     * 自定义唤醒词--配置key
     */
    public static final String TEDDY_SPKEY_MVW_KEYWORDS = "TEDDY_SPKEY_MVW_KEYWORDS";

    /**
     * 持续对话开关--配置key
     */
    public static final String TEDDY_SPKEY_ONGOING_TALK = "TEDDY_SPKEY_ONGOING_TALK";
    /**
     * 自定义发声人--配置key
     */
    public static final String TEDDY_SPKEY_TTS_SPEAKER = "TEDDY_SPKEY_TTS_SPEAKER";
    /**
     * 自定义发声人--配置key
     */
    public static final String TEDDY_CONFIG_TTS_SPEAKER = "TEDDY_CONFIG_TTS_SPEAKER";
    /**
     * 自定义播报语速--配置key
     */
    public static final String TEDDY_CONFIG_TTS_SPEED = "TEDDY_CONFIG_TTS_SPEED";
    /**
    /**
     * 自定义开机问候语--配置key
     */
    public static final String TEDDY_SPKEY_TTS_WELCOME = "TEDDY_SPKEY_TTS_WELCOME";
    /**
     * Teddy图标--是否显示图标--默认为true，即显示图标
     */
    public static final String TEDDY_ICON_SHOW = "TEDDY_ICON_SHOW";
    /**
     * 语音增益值---目前只能调整到63了。64以上就完全不行了
     */
    public static final int TEDDY_VOICE_DAC_DEFAULT_VALUE = 55;
    /**
     * 列表数据最大的数量
     */
    public static final int TEDDY_MAP_LIST_COUNT = 5;


    /**
     * 接收语音资源准备OK广播
     */
    public static final String TEDDY_RECEIVER_RESDIR_OK = "hazens.update.infos";
    /**
     * 地图事件--发送tbox信号强度给导航
     */
    public static final String RECEIVER_TBOX_RSSI_NAVI = "com.sitechdev.send.tbox";
    /**
     * 一般地图操作类型
     */
    public static final int NAVIMAPTYPE_NORMAL = 10000;
    /**
     * 获取当前位置信息
     */
    public static final int NAVIMAPTYPE_CURRENTLOCATION = 10001;
    /**
     * 设置途经点
     */
    public static final int NAVIMAPTYPE_SETMIDDLELOCATION = 10002;
    /**
     * 执行从当前点导航到A点操作(包含途经点)，启动导航并执行导航操作
     */
    public static final int NAVIMAPTYPE_NAVI = 10003;
    /**
     * 导航路线数量
     */
    public static final int NAVIMAPTYPE_NAVI_ROUTE_COUNT = 10004;
    /**
     * 选择第几条导航路线
     */
    public static final int NAVIMAPTYPE_NAVI_ROUTE_INDEX = 10005;
    public static final String NAVI_MAP_SEARCH_BY_KEY = "https://restapi.amap.com/v3/place/text";
    public static final String NAVI_MAP_SEARCH_POI_BY_AROUND = "https://restapi.amap.com/v3/place/around";

    /**
     * 根据关键字获取POI联想信息列表--Get
     * districtid	string	城市名称	不填或为0则搜索全国
     * keyword	string	搜索关键字	必填
     * pageindex	int	当前页码	不填则不分页
     * pagesize	int	每页最大条数	大于0
     * userid	string	用户名	必填
     * password	string	密码	必填
     * location	string	坐标	Location=38.76623,116.43213
     * Location=lat<纬度>,lng<经度> 指定坐标搜索时候为必填，其他时候不填
     */
    public static final String NAVI_MAP_SEARCH_POI_SUGGESTION_BY_KEYWORDS_URL = "http://dnxf.ritu.cn:8088/XFDNService/PlaceSuggestionAPI";


    /**
     * 根据关键字获取POI检索信息列表--Get
     * districtid	string	城市名称	不填或为0则搜索全国
     * keyword	string	搜索关键字	必填
     * pageindex	int	当前页码	不填则不分页
     * pagesize	int	每页最大条数	大于0
     * userid	string	用户名	必填
     * password	string	密码	必填
     * location	string	坐标	Location=38.76623,116.43213
     * Location=lat<纬度>,lng<经度> 指定坐标搜索时候为必填，其他时候不填
     * radius	int	搜索范围	 指定坐标进行范围搜索时候为必填，其他时候不填则执行关键字检索
     * retrieval_way	string	检索方式（1.POI联想；其他,POI关键字检索）	不填默认POI关键字检索
     */
    public static final String NAVI_MAP_SEARCH_POI_BY_KEYWORDS_URL = "http://dnxf.ritu.cn:8088/XFDNService/PlaceAPI";

    /**
     * 选择场景--地图导航
     */
    public static final String TEDDY_SCENE_SELECT_MAP = "SCENE_MAP";
    /**
     * 选择场景--电话
     */
    public static final String TEDDY_SCENE_SELECT_PHONE = "SCENE_PHONE";
    /**
     * 选择场景--股票
     */
    public static final String TEDDY_SCENE_SELECT_STOCK = "SCENE_STOCK";
    /**
     * 选择场景--商城
     */
    public static final String TEDDY_SCENE_SELECT_MALL = "SCENE_MALL";
    /**
     * 第三方APP包名--酷我音乐
     */
    public static final String THIRDAPP_PACKAGENAME_KUWO = "cn.kuwo.kwmusiccar";
    public static final String THIRDAPP_PACKAGENAME_KUWO_MAIN = "cn.kuwo.kwmusiccar.MainActivity";
    /**
     * 第三方APP包名--喜马拉雅
     */
    public static final String THIRDAPP_PACKAGENAME_XIMALAYA = "com.ximalaya.ting.android.car";
    public static final String THIRDAPP_PACKAGENAME_XIMALAYA_MAIN = "com.ximalaya.ting.android.car.activity.MainActivity";
    /**
     * 第三方APP包名--百度CarLife
     */
    public static final String THIRDAPP_PACKAGENAME_BD_CARLIFE = "com.baidu.carlifevehicle";
    /**
     * SP是否允许语音唤醒-key
     */
    public static final String SP_KEY_VOICE_BY_MVW_SWITCH = "SITECH_SP_KEY_VOICE_BY_MVW_SWITCH";
    /**
     * SP是否允许开机问候语-key
     */
    public static final String SP_KEY_WELCOME_SWITCH = "SITECH_SP_KEY_VOICE_SWITCH";
    /**
     * 讯飞技能 标记  有声小说
     */
    public static final String XF_SKILL_TAG_NOVEL = "novel";
    /**
     * 讯飞技能 标记  故事
     */
    public static final String XF_SKILL_TAG_STORY = "story";
    /**
     * 讯飞技能 标记  有声书
     */
    public static final String XF_SKILL_TAG_AUDIOBOOK = "audioBook";
    /**
     * 讯飞技能 标记 评书
     */
    public static final String XF_SKILL_TAG_STORYTELLING = "storyTelling";
    /**
     *  儿歌
     */
    public static final String XF_SKILL_TAG_NURSERYRHYME = "AIUI.nurseryRhyme";
    /**
     * 讯飞技能 标记  戏曲
     */
    public static final String XF_SKILL_TAG_DRAMA = "drama";
    /**
     * 相声
     */
    public static final String XF_SKILL_TAG_CROSSTALK = "crossTalk";
}
