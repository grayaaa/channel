package com.netease.channel.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 常量区
 */
public class Const {

    public static final String LOGIN_USER = "login_user";

    public static final String CHANNEL = "channel";

    public static final Map<String, String> settlementMap = new HashMap<String, String>() {
        {
            put("PV", "");
            put("UV", "");
            put("注册数", "channel_register.register_number");
            put("登陆人数", "");
            put("充值金额", "channel_recharge.recharge_money");
        }
    };
    //页面请求参数
    public static String PARAM_PAGE_NO = "pageNo";
    public static String PARAM_PAGE_SIZE = "pageSize";
    public static String PARAM_PAGE_DATE = "pageDate";
    public static String PARAM_PAGE_START_DATE = "pageStartDate";
    public static String PARAM_PAGE_END_DATE = "pageEndDate";
    //渠道id
    public static String PARAM_CHANNEL_ID = "channelId";
    //渠道名称
    public static String PARAM_CHANNEL_NAME = "channelName";
    //结算方式
    public static String PARAM_CHANNEL_SETTLEMENT_PATTERN = "settlementPattern";
    //渠道来源
    public static String PARAM_CHANNEL_SOURCE = "source";
    //合作方名称
    public static String PARAM_CHANNEL_PARTER_NAME = "parterName";
    //结算指标
    public static String PARAM_CHANNEL_SETTLEMENT_INDICATOR = "settlementIndicator";
    //折扣率
    public static String PARAM_CHANNEL_DISCOUNT = "discount";
    //上线时间
    public static String PARAM_CHANNEL_ONLINE_DATE = "onlineDate";
    //下线时间
    public static String PARAM_CHANNEL_OFFLINE_DATE = "offlineDate";
    //人工评级
    public static String PARAM_CHANNEL_ARTIFICIAL_RATING = "artificialRating";
    //修改时间
    public static String PARAM_CHANNEL_UPDATE_TIME = "lastUpdateTime";
    //通行证/用户名称
    public static String PARAM_USERNAME = "userName";
    //用户权限
    public static String PARAM_ROLE = "role";
    //提交人
    public static String PARAM_SUBMIT_PERSON = "submitPerson";
    //提交时间
    public static String PARAM_SUBMIT_TIME = "submitTime";
    public static int PAGE_NO = 1;
    public static int PAGE_SIZE = 20;
    //请求错误码
    public static enum ErrorCode {
        SUCCESS(1),
        FAILED(0),
        CAPTCHA(-1),
        CAPTCHA_WRONG(-2),
        WORLD_FORBIDDEN(2),
        ILLEGAL_ARGUMENT(400),
        FORBIDDEN(403),
        SERVER_ERROR(501);

        private int value;

        private ErrorCode(int value) {
            this.value = value;
        }

        public int intValue() {
            return this.value;
        }
    }

}
