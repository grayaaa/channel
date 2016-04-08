package com.netease.channel.controller;

import com.netease.channel.dao.GroupDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    private static final Log logger = LogFactory.getLog(HomeController.class);

    @Resource
    GroupDao groupDao;

    @RequestMapping(value = "/dashboard.do")
    public String dashboard(
            HttpServletRequest request,
            HttpServletResponse response,
            ModelMap mm) throws Exception {
        mm.put("dashboard", "推广渠道管理平台");
        return "dashboard";
    }

    @RequestMapping(value = "/")
    public String home(HttpServletRequest request, HttpServletResponse response) {

        return "home";
    }

    @RequestMapping(value = "/channelStastic/getChannelRealReportList.do")
    public String test1(HttpServletRequest request, HttpServletResponse response) {
        return "channel/getChannelRealReportList";
    }

    @RequestMapping(value = "/error-404.do")
    public String error404(HttpServletRequest request,
                           HttpServletResponse response, ModelMap mm) throws Exception {
        return "page/404";
    }

    @RequestMapping(value = "/error-502.do")
    public String error502(ModelMap mm) {
        mm.put("errorMsg", "502");
        return "page/error";
    }
}
