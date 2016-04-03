package com.netease.channel.dao.impl;

import com.netease.channel.dao.AnalysisDao;
import com.netease.channel.entity.ChannelAnalysis;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qmgeng on 16/3/28.
 */
@Repository
public class AnalysisDaoImpl implements AnalysisDao {

    private static final String namespace = "analysis";

    @Resource
    private SqlSession sqlSession;

    @Override
    public ChannelAnalysis getChannelDataById(int channelId) {
        Map<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("channelId", channelId);
        return sqlSession.selectOne(namespace + ".getChannelDataById", queryMap);
    }
}
