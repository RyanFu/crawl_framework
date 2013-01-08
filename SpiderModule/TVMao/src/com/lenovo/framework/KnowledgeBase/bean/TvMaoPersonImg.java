package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoPersonImg   implements Cloneable{ 
	private String personImgId;	
	private String imgUrl;	 
	private String imgName;	
	private String imgType;//0:缩略图；1：原图；
	private String imgDesc;
	private String personId;
	private String sourceUrl;//
	public String getPersonImgId() {
		return personImgId;
	}
	public void setPersonImgId(String personImgId) {
		this.personImgId = personImgId;
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
	public String getImgDesc() {
		return imgDesc;
	}
	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	@Override
	public String toString() {
		return "TvMaoPersonImg [personImgId=" + personImgId + ", imgUrl="
				+ imgUrl + ", imgName=" + imgName + ", imgType=" + imgType
				+ ", imgDesc=" + imgDesc + ", personId=" + personId
				+ ", sourceUrl=" + sourceUrl + "]";
	}
	
	@Override
	public Object clone() {
		TvMaoPersonImg o = null;
		try {
			o = (TvMaoPersonImg) super.clone();
		} catch (CloneNotSupportedException e) {
			//e.printStackTrace();
			o = null;
		}
		return o;
	}
}
