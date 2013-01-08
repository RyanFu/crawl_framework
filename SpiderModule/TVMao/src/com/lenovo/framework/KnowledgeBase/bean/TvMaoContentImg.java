package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoContentImg   implements Cloneable{ 
	private String contentImgId;	
	private String imgUrl;	 
	private String imgName;	
	private String imgType;//'0未知 1：海报缩略图；2：海报正常图片；3：剧照缩略图；4：剧照正常图；
 
	private String contentUuid;
	private String fromType;////内部计算用 "DRAMA_PICTURE" "MOVIE_PICTURE" "MOVIE_POSTER"
	private String mainUrl;//
	public String getContentImgId() {
		return contentImgId;
	}
	public void setContentImgId(String contentImgId) {
		this.contentImgId = contentImgId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public String getContentUuid() {
		return contentUuid;
	}
	public void setContentUuid(String contentUuid) {
		this.contentUuid = contentUuid;
	}
	public String getFromType() {
		return fromType;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	 
	public String getMainUrl() {
		return mainUrl;
	}
	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}
	@Override
	public String toString() {
		return "TvMaoContentImg [contentImgId=" + contentImgId + ", imgUrl="
				+ imgUrl + ", imgName=" + imgName + ", imgType=" + imgType
				+ ", contentUuid=" + contentUuid + ", fromType=" + fromType
				+ ", mainUrl=" + mainUrl + "]";
	}
	@Override
	public Object clone() {
		TvMaoContentImg o = null;
		try {
			o = (TvMaoContentImg) super.clone();
		} catch (CloneNotSupportedException e) {
			//e.printStackTrace();
			o = null;
		}
		return o;
	}
 
}