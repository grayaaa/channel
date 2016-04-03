/**
 * @(#)CubeAuthenticator.java, Jun 20, 2011.
 * <p/>
 * Copyright 2011 Netease, Inc. All rights reserved. NETEASE PROPRIETARY/CONFIDENTIAL. Use
 * is subject to license terms.
 */
package com.netease.channel.util;

import com.netease.channel.security.UrsToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author mengyan
 */
public class AuthenticatorFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(AuthenticatorFilter.class);
    private String realm = "";

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
            ServletException {
        LOG.debug("login Filter!!!");
        login("qmgeng1@126.com");
        try {
                    arg2.doFilter(arg0, arg1);
                } catch (Exception e) {
                    LOG.error(e);
                }
                return;


//        HttpServletRequest request = (HttpServletRequest) arg0;
//        HttpServletResponse response = (HttpServletResponse) arg1;
//        String assoc_handle = (String) request.getSession().getAttribute(LoginUtil.ASSOC_HANDLE);
//        String mac_key = (String) request.getSession().getAttribute(LoginUtil.MAC_KEY);
//        String passport = LoginUtil.getSessionLoginStatus(request);
//
//        String originurl =
//                request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
//
//        try {
//            if (StringUtils.isEmpty(assoc_handle) || StringUtils.isEmpty(mac_key)) {
//                LOG.debug("first");
//                OpenidAuth consumer = new OpenidAuth();
//                Map<String, String> asso = consumer.association();
//                String redirecturl = consumer.authentication(originurl, realm, asso.get(LoginUtil.ASSOC_HANDLE));
//
//                request.getSession().setAttribute(LoginUtil.ASSOC_HANDLE, asso.get(LoginUtil.ASSOC_HANDLE));
//                request.getSession().setAttribute(LoginUtil.MAC_KEY, asso.get(LoginUtil.MAC_KEY));
//                // request.getSession().setMaxInactiveInterval(Integer.parseInt(asso.get(LoginUtil.EXPIRES_IN))-10); //
//                // session超时
//                response.sendRedirect(redirecturl);
//                return;
//            } else if (StringUtils.isEmpty(passport)) {
//                OpenidAuth consumer = new OpenidAuth();
//                Map<String, String> dataMap = consumer.check_signature(originurl, assoc_handle, mac_key);
//                if (dataMap != null && !StringUtils.isEmpty(dataMap.get("email"))
//                        && !StringUtils.isEmpty(dataMap.get("returnto"))) {
//                    request.getSession().setAttribute(LoginUtil.SESSION_USER_NAME, dataMap.get("email"));
//                    request.getSession().setAttribute(LoginUtil.CHINESEUSERNAME, dataMap.get("fullname"));
//                    response.sendRedirect(dataMap.get("returnto"));
//                    return;
//                }
//                // 验证失败重新开始
//                LOG.error("验证失败：url=" + originurl);
//                request.getSession().setAttribute(LoginUtil.ASSOC_HANDLE, null);
//                request.getSession().setAttribute(LoginUtil.MAC_KEY, null);
//            } else {
//                request.setAttribute(LoginUtil.USER_NAME, passport);
//                try {
//                    arg2.doFilter(arg0, arg1);
//                } catch (Exception e) {
//                    LOG.error(e);
//                }
//                return;
//            }
//        } catch (Exception e) {
//            request.getSession().setAttribute(LoginUtil.ASSOC_HANDLE, null);
//            request.getSession().setAttribute(LoginUtil.MAC_KEY, null);
//            request.getSession().setAttribute(LoginUtil.SESSION_USER_NAME, null);
//            LOG.error("验证过程出现异常: ", e);
//            response.sendRedirect("/unLogin.do");
//        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        String temp = arg0.getInitParameter(LoginUtil.LOGIN_URL);
        if (!StringUtils.isEmpty(temp)) {
            realm = temp;
        }
        LOG.info("Login user " + realm);

        String home = arg0.getServletContext().getRealPath("WEB-INF");
        LOG.info("Home directory=" + home);
        System.setProperty("native.home", home);

    }

    private void login(String email) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UrsToken(email));
    }

}
