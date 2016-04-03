/**
 * @(#)LoginUtil.java, Jun 21, 2011.
 * <p/>
 * Copyright 2011 Netease, Inc. All rights reserved. NETEASE PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.netease.channel.util;

import com.netease.commons.util.http.ntescode;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mengyan
 */
public class LoginUtil {

    public static final int PERSISTENTTIME = 3 * 30 * 24 * 3600;// 目前是三个月
    public static final String AUTH_MODE = "auth_mode";
    public static final String AUTH_MODE_ONLINE = "ONLINE";
    public static final String LOGIN_URL = "login_url";
    public static final String USER_NAME = "user_name";
    public static final String SESSION_USER_NAME = "session_username";
    public static final String CHINESEUSERNAME = "chinese_username";
    public static final String PRODUCT_AUTH = "product_auth";
    public final static String CORP_MAIL = "@corp.netease.com";
    public static final String ASSOC_HANDLE = "assoc_handle";
    public static final String MAC_KEY = "mac_key";
    public static final String EXPIRES_IN = "expires_in";
    // 登录状态，cms、corp
    public final static String login_status = "login_status";
    public static String mode = AUTH_MODE_ONLINE;

    /**
     * 从cookie里面获取用户登录信息(旧,用通行证的时候的)
     *
     * @param request
     * @return
     */
    public static String getLoginStatus(final HttpServletRequest request) {
        Cookie ntesCookie = null;
        String username = null;
        String encodedUserName = null;
        Cookie[] cookies = request.getCookies();
        // 获取的 cookies 不为 NULL 并且的个数大于 0 个
        if (cookies != null && cookies.length > 0) {
            // 遍历 cookies
            for (Cookie cooky : cookies) {
                // cookies 名
                String cname = cooky.getName();
                // 如果 cookies 名是 NTES_SESS
                if ("NTES_SESS".equals(cname)) {
                    ntesCookie = cooky;
                    // cookies 的值，即被加密的用户名
                    encodedUserName = ntesCookie.getValue();
                    // 初始化通行证类
                    ntescode ntes = new ntescode();
                    // 验证被加密的用户名是否登录状态
                    int ret = ntes.validate_cookie(encodedUserName.getBytes(), 8, PERSISTENTTIME, true);
                    // 验证成功，即该通行证用户处于登录状态
                    if (ret >= 0) {
                        username = new String(ntes.ssn);
                        if (username.indexOf("@") <= 0) {
                            username += "@163.com";
                        }
                    } else {
                        username = "";
                    }
                }
            }
            // 如果是NTES_SESS没有的话，需要再去判断passport，如果有那么，去验证，
            // 如果返回正确，那么认为是自动登录用户，如果没有，还要判断pinfo字段，
            // 如果是1，那么认为出现异常丢失cookie的情况这样需要修改pinfo字段中相应的状态位为2
            String encodedPassport;
            if (StringUtils.isEmpty(username)) {
                try {
                    for (Cookie cooky : cookies) {
                        if ("NTES_PASSPORT".equals(cooky.getName())) {
                            ntesCookie = cooky;
                            // cookies 的值，即被加密的用户名
                            encodedPassport = ntesCookie.getValue();
                            // 初始化通行证类
                            ntescode ntes = new ntescode();
                            // 验证被加密的用户名是否登录状态,如果ret>0那么，去判断pinfo相应状态位，
                            // 如果为1，那么认为是自动登录,false表示不是重新写cookie
                            int ret = ntes.validate_persistent_cookie(encodedPassport.getBytes(), 8, PERSISTENTTIME, true);
                            if (ret >= 0) {
                                username = new String(ntes.ssn);
                                if (username.indexOf("@") <= 0) {
                                    username += "@163.com";
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        return username;
    }

    public static String getSessionLoginStatus(final HttpServletRequest request) {
        return (String) request.getSession().getAttribute(SESSION_USER_NAME);
    }

}
