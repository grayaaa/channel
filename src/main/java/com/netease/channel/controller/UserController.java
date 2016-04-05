package com.netease.channel.controller;

import com.google.common.collect.Maps;
import com.netease.channel.entity.Role;
import com.netease.channel.entity.User;
import com.netease.channel.service.UserService;
import com.netease.channel.util.JSONResponseBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);

    @Resource
    private UserService userService;

    @RequiresPermissions("user:view")
    @RequestMapping(value = "/getUserList.do")
    public String getUserList(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
        List<User> listUser = userService.getUserList();
        mm.put("listUser", listUser);
        JSONResponseBuilder.outToJson(mm);
        return "user/getUserList";
    }

    @RequiresPermissions("user:add")
    @RequestMapping(value = "/addUser.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addUser(User user) {
        userService.addUser(user);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("msg", "true");
        return resultMap;
    }

    @RequiresPermissions("role:view")
    @RequestMapping(value = "/getRoleList.do")
    public String getRoleList(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
        logger.debug("start getRoleList");

        List<Role> listRole = userService.getRoleList();
        mm.put("listRole", listRole);
        JSONResponseBuilder.outToJson(mm);
        return "user/getRoleList";
    }

    @RequiresPermissions("role:add")
    @RequestMapping(value = "/addRole.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addRole(Role role) {
        userService.addRole(role);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("msg", "true");
        return resultMap;
    }

//    @RequestMapping(value = "/addRole.do", method = RequestMethod.POST)
//    public
//    String addRole(Role role, RedirectAttributes redirectAttributes) {
//        userService.addRole(role);
//        redirectAttributes.addFlashAttribute("msg", "true");
//        return "redirect:/user/getRoleList.do";
//    }

    @RequiresPermissions("user:del")
    @RequestMapping(value = "/deleteUser.do", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> deleteUser(String email) {
        userService.deleteUser(email);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("msg", "true");
        return resultMap;
    }

    @RequiresPermissions("role:del")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> deleteRole(String role) {
        userService.deleteRole(role);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("msg", "true");
        return resultMap;
    }
}
