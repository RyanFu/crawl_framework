package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoReviewInfo {
	private String commentID;//source + sourceid
	private String uid;
	private String name;
	private String contentID;
	private String source;
	private String sourceID;
	private String title;
	private String content;
	private String pubTime;
	
	private String mailUrl;
	//reviewId=66&objectId=89122&flag=t
	/*private String reviewID;
	private String objectID;
	private String flag;
	private String postUrl;//http://www.tvmao.com/servlet/getreview?reviewId=9832&objectId=128060&flag=t*/
	
	public String getCommentID() {
		return commentID;
	}
	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentID() {
		return contentID;
	}
	public void setContentID(String conentID) {
		this.contentID = conentID;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceID() {
		return sourceID;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	 
	public String getMailUrl() {
		return mailUrl;
	}
	public void setMailUrl(String mailUrl) {
		this.mailUrl = mailUrl;
	}
	@Override
	public String toString() {
		return "TvMaoReviewInfo [commentID=" + commentID + ", uid=" + uid
				+ ", name=" + name + ", contentID=" + contentID + ", source="
				+ source + ", sourceID=" + sourceID + ", title=" + title
				+ ", content=" + content + ", pubTime=" + pubTime
				+ ", mailUrl=" + mailUrl + "]";
	}
 
	
}
