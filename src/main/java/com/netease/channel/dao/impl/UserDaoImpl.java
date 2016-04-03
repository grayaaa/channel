package com.netease.channel.dao.impl;

import com.netease.channel.dao.UserDao;
import com.netease.channel.entity.Role;
import com.netease.channel.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qmgeng on 16/4/1.
 */
@Repository
public class UserDaoImpl implements UserDao {
    private static final String namespace = "user";

    @Resource
    private SqlSession sqlSession;

    @Override
    public User getUserByEmail(String email) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        return sqlSession.selectOne(namespace + ".getUserByEmail", params);
    }

    @Override
    public List<User> getUserList() {
        return sqlSession.selectList(namespace + ".getUserList");
    }

    @Override
    public Role getRoleByName(String role) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("role", role);
        return sqlSession.selectOne(namespace + ".getRoleByName", params);
    }

    @Override
    public List<Role> getRoleList() {
        return sqlSession.selectList(namespace + ".getRoleList");
    }

    @Override
    public void addRole(Role role) {
        sqlSession.insert(namespace + ".addRole", role);
    }

    @Override
    public void addUser(User user) {
        sqlSession.insert(namespace + ".addUser", user);
    }
}
