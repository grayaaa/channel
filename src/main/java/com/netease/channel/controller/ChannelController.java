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
@RequestMapping("/channel")
public class ChannelController {

    private static final Log logger = LogFactory.getLog(ChannelController.class);

    @Resource
    GroupDao groupDao;


    @RequestMapping(value = "/getChannelDetail.do")
    public String channelDetail(HttpServletRequest request, HttpServletResponse response) {
        return "channel/getChannelDetail";
    }


}
