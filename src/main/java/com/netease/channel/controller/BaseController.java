/**
 * @(#)BaseController.java, May 9, 2011.
 * <p/>
 * Copyright 2011 Netease, Inc. All rights reserved. NETEASE PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.netease.channel.controller;

import com.netease.channel.util.LoginUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mengyan
 */
@RequestMapping("/")
public class BaseController extends MultiActionController {

    protected static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    protected static final String ERROR_VIEW = "error";
    protected static final String SUC_RES = "{\"res\":success}";
    protected static final String FAIL_RES = "{\"res\":error}";
    protected static final String ERROR_MSG = "errorMsg";
    protected static final String LOGIN_VIEW = "login";
    protected static final Logger LOG = Logger.getLogger(BaseController.class);

    protected void successRes(HttpServletResponse respose) {
        respose.setContentType(JSON_CONTENT_TYPE);
        try {
            respose.getWriter().write(SUC_RES);
            respose.getWriter().close();
        } catch (IOException e) {
            LOG.info("Controller response error");
        }
    }

    protected void successRes(HttpServletResponse respose, String res) {
        respose.setContentType(JSON_CONTENT_TYPE);
        try {
            respose.getWriter().write(res);
            respose.getWriter().close();
        } catch (IOException e) {
            LOG.info("Controller response error");
        }
    }

    protected void errorRes(HttpServletResponse respose) {
        respose.setContentType(JSON_CONTENT_TYPE);
        try {
            respose.getWriter().write(FAIL_RES);
            respose.getWriter().close();
        } catch (IOException e) {
            LOG.info("Controller response error");
        }
    }

    protected ModelAndView getView(String name) {
        if (StringUtils.isEmpty(name)) {
            return getErrorView();
        } else {
            return new ModelAndView(name);
        }
    }

    protected ModelAndView getView(String name, Map<String, Object> map) {
        if (StringUtils.isEmpty(name)) {
            return getErrorView();
        } else if (map == null) {
            return new ModelAndView(name);
        }
        return new ModelAndView(name, map);
    }

    protected ModelAndView getErrorView() {
        return new ModelAndView(ERROR_VIEW);
    }

    protected ModelAndView getErrorView(String msg) {
        Map<String, String> map = new HashMap<String, String>(1);
        map.put(ERROR_MSG, msg);
        return new ModelAndView(ERROR_VIEW, map);
    }

    protected String paramCheck(String str) {
        return ((str != null) ? str.trim() : null);
    }

    @RequestMapping("/unAuth")
    public ModelAndView unAuth(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("unAuth");
        StringBuilder message = new StringBuilder();
        message.append("用户 ")
                // .append(LoginUtil.getLoginStatus(request))
                .append(" 没有访问权限，").append("如需获得本系统的访问权限，请联系孟彦").append(" <a href=\"/\">回到首页</a> ");
        return getErrorView(message.toString());

    }

    @RequestMapping("/unLogin.do")
    public ModelAndView unLogin(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("unLogin");
        StringBuilder message = new StringBuilder();
        message.append("用户 ")
                // .append(LoginUtil.getLoginStatus(request))
                .append(" 网易内部认证系统 验证失败，")
                // .append("如需获得本系统的访问权限，请联系孟彦")
                .append(" <a href=\"/\">回到首页</a> ");
        return getErrorView(message.toString());

    }

    public ModelAndView sysError(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder message = new StringBuilder();
        message.append("系统异常， 请联系孟彦").append(" <a href=\"/\">回到首页</a> ");
        return getErrorView(message.toString());

    }


    public String getUserName(HttpServletRequest request, HttpServletResponse respose) {
        String userName = (String) request.getAttribute(LoginUtil.USER_NAME);
        LOG.debug("UserName is " + userName);
        return userName;
    }

    // 通过openid获取中文名称
    public String getChineseName(HttpServletRequest request, HttpServletResponse respose) {
        String chineseName = (String) request.getSession().getAttribute(LoginUtil.CHINESEUSERNAME);
        LOG.debug("chineseName is " + chineseName);
        return chineseName;
    }

}
