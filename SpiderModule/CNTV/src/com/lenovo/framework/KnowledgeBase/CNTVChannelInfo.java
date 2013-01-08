package com.lenovo.framework.KnowledgeBase;

public class CNTVChannelInfo {
	private String name="";
	private String urlMark="";
	private String refUrl="";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlMark() {
		return urlMark;
	}
	public void setUrlMark(String urlMark) {
		this.urlMark = urlMark;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	
	@Override
	public String toString() {
		return "ChannelInfo [name=" + name + ", urlMark=" + urlMark
				+ ", refUrl=" + refUrl + "]";
	}
}
