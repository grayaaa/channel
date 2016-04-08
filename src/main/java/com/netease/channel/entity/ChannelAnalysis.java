package com.netease.channel.entity;

import java.io.Serializable;
import java.util.Date;

public class ChannelAnalysis implements Serializable {

    //下线
    public static final int STATUS_DISABLED = 1;
    //以上线
    public static final int STATUS_ENABLE = 0;
    private static final long serialVersionUID = 7536449367500922853L;
    private int id;
    private String channelName;
    private String parterName;
    private String source;
    private int status;
    //结算模式
    private String settlementPattern;
    //结算指标
    private String settlementIndicator;
    //折扣
    private float discount;
    //人工评级
    private int artificialRating;
    //在线时长
    private int onlineTime;
    //上线日期
    private Date onlineDate;
    //下线日期
    private Date offlineDate;
    //最后修改日期
    private Date lastUpdateTime;
    private String registerNum;
    private String chargeNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getParterName() {
        return parterName;
    }

    public void setParterName(String parterName) {
        this.parterName = parterName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSettlementPattern() {
        return settlementPattern;
    }

    public void setSettlementPattern(String settlementPattern) {
        this.settlementPattern = settlementPattern;
    }

    public String getSettlementIndicator() {
        return settlementIndicator;
    }

    public void setSettlementIndicator(String settlementIndicator) {
        this.settlementIndicator = settlementIndicator;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getArtificialRating() {
        return artificialRating;
    }

    public void setArtificialRating(int artificialRating) {
        this.artificialRating = artificialRating;
    }

    public int getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(Date onlineDate) {
        this.onlineDate = onlineDate;
    }

    public Date getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(Date offlineDate) {
        this.offlineDate = offlineDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public String getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(String chargeNum) {
        this.chargeNum = chargeNum;
    }

    public String toJson() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("channelName:" + getChannelName() + ",");
        buffer.append("parterName:" + getParterName() + ",");
        buffer.append("settlementPattern:" + getSettlementPattern() + ",");
        buffer.append("settlementIndicator:" + getSettlementIndicator() + ",");
        buffer.append("discount:" + getDiscount() + ",");
        buffer.append("onlineDate:" + getOnlineDate() + ",");
        buffer.append("offlineDate:" + getOfflineDate() + ",");
        buffer.append("artificialRating:" + getArtificialRating() + ",");
        return "";
    }


}
