package com.netease.channel.controller;

import com.netease.channel.dao.mysql.GroupDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/manage")
public class ChannelManageController {

    private static final Log logger = LogFactory.getLog(ChannelManageController.class);

    @Resource
    GroupDao groupDao;


    @RequestMapping(value = "/getGroups.do")
    public String getGroups(HttpServletRequest request, HttpServletResponse response) {
        return "manage/getGroups";
    }


}
