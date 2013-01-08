package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoEpisodeStory {
	private String contentUuid;
	private String parentId;	 
	private String sourceId;	
	private String mainUrl;
	private String episode;	
	private String description;
	
	private String programName;
	private String titleName;
	
	private String type;////内部计算用
	private String parentSourceId; //内部计算SOURCE_ID用
	
	private String guest;//嘉宾
 
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContentUuid() {
		return contentUuid;
	}
	public void setContentUuid(String contentUuid) {
		this.contentUuid = contentUuid;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
 
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getParentSourceId() {
		return parentSourceId;
	}
	public void setParentSourceId(String parentSourceId) {
		this.parentSourceId = parentSourceId;
	}
	public String getMainUrl() {
		return mainUrl;
	}
	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "TvMaoEpisodeStory [contentUuid=" + contentUuid + ", parentId="
				+ parentId + ", sourceId=" + sourceId + ", mainUrl=" + mainUrl
				+ ", episode=" + episode + ", description=" + description
				+ ", programName=" + programName + ", titleName=" + titleName
				+ ", type=" + type + ", parentSourceId=" + parentSourceId
				+ ", guest=" + guest + "]";
	}
	 
}
