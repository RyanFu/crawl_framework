package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoActor {
	private String personID;
	private String source;
	private String sourceID;
	private String mainUrl;
	private String name;
	private String englishName;
	private String alias;
	private String gender;//'0未知 1男 2女',
	private String description;
	private String birthday;
	private String country;//'国籍',
	private String nation;//民族
	private String profession;//职业
	private String constellation;//星座
	private String bloodType;
	private String birthLand;
	private String works;
	private String school;
	private String score;//grade '评分，百分制',
	private String comment;//'短评',
	private String microSlogUrl;//微博地址
	
	private String refUrl;
	private String heatDegree;
	
	public String getHeatDegree() {
		return heatDegree;
	}
	public void setHeatDegree(String heatDegree) {
		this.heatDegree = heatDegree;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
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
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getBirthLand() {
		return birthLand;
	}
	public void setBirthLand(String birthLand) {
		this.birthLand = birthLand;
	}
	public String getWorks() {
		return works;
	}
	public void setWorks(String works) {
		this.works = works;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getMicroSlogUrl() {
		return microSlogUrl;
	}
	public void setMicroSlogUrl(String microSlogUrl) {
		this.microSlogUrl = microSlogUrl;
	}
	@Override
	public String toString() {
		return "TvMaoActor [personID=" + personID + ", source=" + source
				+ ", sourceID=" + sourceID + ", mainUrl=" + mainUrl + ", name="
				+ name + ", englishName=" + englishName + ", alias=" + alias
				+ ", gender=" + gender + ", description=" + description
				+ ", birthday=" + birthday + ", country=" + country
				+ ", nation=" + nation + ", profession=" + profession
				+ ", constellation=" + constellation + ", bloodType="
				+ bloodType + ", birthLand=" + birthLand + ", works=" + works
				+ ", school=" + school + ", score=" + score + ", comment="
				+ comment + ", microSlogUrl=" + microSlogUrl + ", refUrl="
				+ refUrl + ", heatDegree=" + heatDegree + "]";
	}
	 
	
}
