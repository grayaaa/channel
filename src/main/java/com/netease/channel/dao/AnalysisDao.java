package com.netease.channel.dao;

import com.netease.channel.entity.ChannelAnalysis;

/**
 * Created by qmgeng on 16/3/28.
 */
public interface AnalysisDao {
    public ChannelAnalysis getChannelDataById(int channelId);
}
