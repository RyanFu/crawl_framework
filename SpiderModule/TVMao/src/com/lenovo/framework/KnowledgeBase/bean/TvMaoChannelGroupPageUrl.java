package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoChannelGroupPageUrl {

	private String url;
	private String absoluteUrl;
	private String channelGroupName;//
	private int    level;
	private String relUrl;
	private long findTime;
	private String title;	
	private String areaName;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAbsoluteUrl() {
		return absoluteUrl;
	}
	public void setAbsoluteUrl(String absoluteUrl) {
		this.absoluteUrl = absoluteUrl;
	}
	public String getChannelGroupName() {
		return channelGroupName;
	}
	public void setChannelGroupName(String channelGroupName) {
		this.channelGroupName = channelGroupName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getRelUrl() {
		return relUrl;
	}
	public void setRelUrl(String relUrl) {
		this.relUrl = relUrl;
	}
	public long getFindTime() {
		return findTime;
	}
	public void setFindTime(long findTime) {
		this.findTime = findTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Override
	public String toString() {
		return "TvMaoChannelGroupPageUrl [url=" + url + ", absoluteUrl="
				+ absoluteUrl + ", channelGroupName=" + channelGroupName
				+ ", level=" + level + ", relUrl=" + relUrl + ", findTime="
				+ findTime + ", title=" + title + ", areaName=" + areaName
				+ "]";
	}
 
	
	
}