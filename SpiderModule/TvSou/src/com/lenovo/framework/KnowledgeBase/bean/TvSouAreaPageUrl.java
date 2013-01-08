package com.lenovo.framework.KnowledgeBase.bean;

public class TvSouAreaPageUrl {
	
	private String url;//
	private String areaName;
	private int    level;
	private String relUrl;
	private long findTime;
	private String title;
	private String absoluteUrl;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {	 
		this.url = url;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	public String getAbsoluteUrl() {
		return absoluteUrl;
	}
	public void setAbsoluteUrl(String absoluteUrl) {
		this.absoluteUrl = absoluteUrl;
	}
	@Override
	public String toString() {
		return "AreaPageUrl [url=" + url + ", areaName=" + areaName
				+ ", level=" + level + ", relUrl=" + relUrl + ", findTime="
				+ findTime + ", title=" + title + ", absoluteUrl="
				+ absoluteUrl + "]";
	}
	 

}
