package com.netease.channel.service.impl;

import com.google.common.collect.Sets;
import com.netease.channel.dao.UserDao;
import com.netease.channel.entity.Role;
import com.netease.channel.entity.User;
import com.netease.channel.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by qmgeng on 16/4/1.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }

    @Override
    public List<Role> getRoleList() {
        return userDao.getRoleList();
    }

    @Override
    public Role getRoleByName(String role) {
        return userDao.getRoleByName(role);
    }

    @Override
    public Set<String> getPermissionsByEmail(String email) {
        User user = getUserByEmail(email);
        return getPermissionsByUser(user);
    }

    @Override
    public Set<String> getPermissionsByUser(User user) {
        Set<String> setPermission = Sets.newHashSet();
        for (String rolename : user.getSetRoles()) {
            Role role = getRoleByName(rolename);
            for (String permission : role.getSetPermissions()) {
                setPermission.add(permission);
            }
        }
        return setPermission;
    }

    @Override
    public void addRole(Role role) {
        userDao.addRole(role);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }
}
