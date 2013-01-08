package com.lenovo.framework.KnowledgeBase.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @Description : 排行指数信息
 * @author : ligang8@lenovo.com
 * @date : 2012-9-11 上午9:04:36
 * @version : V1.0
 */
public class RankingRaw implements Serializable {

    private static final long serialVersionUID = -3547128914602196010L;
    // 来源，例如youku
    private String source;
    // 节目的ID
    private String sourceId;
    // url
    private String mainUrl;
    // 名称
    private String name;
    // 指数
    private String point;
    // 第几季
    private String session;
    // 第几集或第几期
    private String episode;
    // 分类，例如电视剧、电影、综艺节目
    private String type;
    // 演员，斜线分割
    private String actor;
    // 导演，斜线分割
    private String director;
    // 主持人，斜线分割
    private String presenter;
    // 嘉宾，斜线分割
    private String guest;
    // 简介
    private String description;
    // 版本
    private String version;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;

    public RankingRaw() {
        this.updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .format(System.currentTimeMillis());
    }

    public RankingRaw(String source, String sourceId, String mainUrl,
            String name, String point) {
        super();
        this.source = source;
        this.sourceId = sourceId;
        this.mainUrl = mainUrl;
        this.name = name;
        this.point = point;
        this.updateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .format(System.currentTimeMillis());
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

    public String getFieldsValue(String field) {
        if (null == field)
            return null;
        if ("SOURCE".equals(field))
            return this.source;
        if ("SOURCE_ID".equals(field))
            return this.sourceId;
        if ("MAIN_URL".equals(field))
            return this.mainUrl;
        if ("NAME".equals(field))
            return this.name;
        if ("SESSION".equals(field))
            return this.session;
        if ("EPISODE".equals(field))
            return this.episode;
        if ("TYPE".equals(field))
            return this.type;
        if ("ACTOR".equals(field))
            return this.actor;
        if ("DIRECTOR".equals(field))
            return this.director;
        if ("PRESENTER".equals(field))
            return this.presenter;
        if ("GUEST".equals(field))
            return this.guest;
        if ("DESCRIPTION".equals(field))
            return this.description;
        if ("POINT".equals(field))
            return this.point;
        if ("VERSION".equals(field))
            return this.version;
        if ("CREATE_TIME".equals(field))
            return this.createTime;
        if ("UPDATE_TIME".equals(field))
            return this.updateTime;
        return null;
    }

    @Override
    public String toString() {
        return "RankingRaw [source=" + source + ", sourceId=" + sourceId
                + ", mainUrl=" + mainUrl + ", name=" + name + ", point="
                + point + ", session=" + session + ", episode=" + episode
                + ", type=" + type + ", actor=" + actor + ", director="
                + director + ", presenter=" + presenter + ", guest=" + guest
                + ", description=" + description + ", version=" + version 
                + ", createTime=" + createTime
                + ", updateTime=" + updateTime + "]";
    }

}
