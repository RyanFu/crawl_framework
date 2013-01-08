package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoChannelPageUrl {
	private String url;//
	private String channelName;
	private String channelGroupName;
	private int    level;
	private String relUrl;
	private long findTime;	
	private String absoluteUrl;	
	private String areaName;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
	public String getAbsoluteUrl() {
		return absoluteUrl;
	}
	public void setAbsoluteUrl(String absoluteUrl) {
		this.absoluteUrl = absoluteUrl;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Override
	public String toString() {
		return "TvMaoChannelPageUrl [url=" + url + ", channelName="
				+ channelName + ", channelGroupName=" + channelGroupName
				+ ", level=" + level + ", relUrl=" + relUrl + ", findTime="
				+ findTime + ", absoluteUrl=" + absoluteUrl + ", areaName="
				+ areaName + "]";
	}
	 
 
 

	 
	
}
