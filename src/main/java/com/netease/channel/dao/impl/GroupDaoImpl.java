package com.netease.channel.dao.impl;

import com.netease.channel.dao.GroupDao;
import com.netease.channel.entity.ChannelGroup;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class GroupDaoImpl implements GroupDao {

    private static final String namespace = "group";

    @Resource
    private SqlSession sqlSession;


    @Override
    public int getNextID(String tableName) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("tableName", tableName);
        return sqlSession.selectOne(namespace + ".getNextId", queryMap);
    }

    @Override
    public ChannelGroup getGroupByID(int groupId) {
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("groupId", groupId);
        return sqlSession.selectOne(namespace + ".getGroupById", queryMap);
    }
}


