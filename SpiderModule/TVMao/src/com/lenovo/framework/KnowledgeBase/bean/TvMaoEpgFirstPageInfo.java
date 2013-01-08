package com.lenovo.framework.KnowledgeBase.bean;


public class TvMaoEpgFirstPageInfo extends TvMaoEpgInfo{	

	private String directors;
	private String writers;
	private String leadingRoles;
	private String programArea;
	private String lang;
	private String year;
	private 	String firstPlayChannel;
	private 	String presenters;
	private String episodeTotal;	
	private String description;	
	private String tags;	
	
	//private String contentUuid;
	private String score;
	private String heatDegree;
	private String sourceID;
	private String titleName;//title （raw_content）

	private String duration;//时长 电影有
	private String airDate;//上映时间 电影有
	//next extrator urls
	public TvMaoEpgFirstPageInfo() {
		
	}
	public TvMaoEpgFirstPageInfo(TvMaoEpgInfo EpgInfo) {
		super.setUuid ( EpgInfo.getUuid());
	 	super.setStandardTime ( EpgInfo.getStandardTime());
		super.setStartTime ( EpgInfo.getStartTime());
		super.setStatus ( EpgInfo.getStatus());
		super.setType ( EpgInfo.getType());
		super.setCurrentSet ( EpgInfo.getCurrentSet());
		super.setTotalSet (EpgInfo.getTotalSet());
		super.setProgramName ( EpgInfo.getProgramName());
		super.setProgramFirstPageUrl(EpgInfo.getProgramFirstPageUrl());
		super.setProgramFirstPageAbsoluteUrl (EpgInfo.getProgramFirstPageAbsoluteUrl());		
		super.setResolutionRatio  (EpgInfo.getResolutionRatio());
		super.setLevel (EpgInfo.getLevel());
		super.setRelUrl (EpgInfo.getRelUrl());
		super.setFindTime (EpgInfo.getFindTime());
		super.setDramaUrl (EpgInfo.getDramaUrl());
		super.setDramaTitle (EpgInfo.getDramaTitle());
		super.setDramaAbsoluteUrl (EpgInfo.getDramaAbsoluteUrl());		
		super.setDramaPhotoUrl (EpgInfo.getDramaPhotoUrl());
		super.setDramaPhotoTitle (EpgInfo.getDramaPhotoTitle());
		super.setDramaPhotoAbsoluteUrl (EpgInfo.getDramaPhotoAbsoluteUrl());
		super.setPlayerListUrl (EpgInfo.getPlayerListUrl());
		super.setPlayerListTitle (EpgInfo.getPlayerListTitle());
		super.setPlayerListAbsoluteUrl (EpgInfo.getPlayerListAbsoluteUrl());		
		super.setPlayAddrUrl (EpgInfo.getPlayAddrUrl());
		super.setPlayAddrTitle (EpgInfo.getPlayAddrTitle());
		super.setPlayAddrAbsoluteUrl (EpgInfo.getPlayAddrAbsoluteUrl());
		super.setYmdTime (EpgInfo.getYmdTime());
		super.setDayName (EpgInfo.getDayName());
		super.setWeekName (EpgInfo.getWeekName());
		super.setChannelName (EpgInfo.getChannelName());
		super.setChannelGroupName (EpgInfo.getChannelGroupName());
		super.setAreaName  (EpgInfo.getAreaName());
		super.setSession ( EpgInfo.getSession());
		super.setAbs ( EpgInfo.getAbs());
		super.setContentUuid(EpgInfo.getContentUuid());
		super.setOriprogramName(EpgInfo.getOriprogramName());
	}







	public String getDuration() {
		return duration;
	}







	public void setDuration(String duration) {
		this.duration = duration;
	}







	public String getAirDate() {
		return airDate;
	}







	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}







	public String getTitleName() {
		return titleName;
	}



	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}



	public String getSourceID() {
		return sourceID;
	}



	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}



	public String getHeatDegree() {
		return heatDegree;
	}



	public void setHeatDegree(String heatDegree) {
		this.heatDegree = heatDegree;
	}



	public String getDirectors() {
		return directors;
	}



	public void setDirectors(String directors) {
		this.directors = directors;
	}



	public String getWriters() {
		return writers;
	}



	public void setWriters(String writers) {
		this.writers = writers;
	}



	public String getLeadingRoles() {
		return leadingRoles;
	}



	public void setLeadingRoles(String leadingRoles) {
		this.leadingRoles = leadingRoles;
	}



	public String getProgramArea() {
		return programArea;
	}



	public void setProgramArea(String programArea) {
		this.programArea = programArea;
	}



	public String getLang() {
		return lang;
	}



	public void setLang(String lang) {
		this.lang = lang;
	}



	public String getYear() {
		return year;
	}



	public void setYear(String year) {
		this.year = year;
	}



	public String getFirstPlayChannel() {
		return firstPlayChannel;
	}



	public void setFirstPlayChannel(String firstPlayChannel) {
		this.firstPlayChannel = firstPlayChannel;
	}



	public String getPresenters() {
		return presenters;
	}



	public void setPresenters(String presenters) {
		this.presenters = presenters;
	}



	public String getEpisodeTotal() {
		return episodeTotal;
	}



	public void setEpisodeTotal(String episodeTotal) {
		this.episodeTotal = episodeTotal;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getTags() {
		return tags;
	}



	public void setTags(String tags) {
		this.tags = tags;
	}
 
	public String getScore() {
		return score;
	}



	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "TvMaoEpgFirstPageInfo [directors=" + directors + ", writers="
				+ writers + ", leadingRoles=" + leadingRoles + ", programArea="
				+ programArea + ", lang=" + lang + ", year=" + year
				+ ", firstPlayChannel=" + firstPlayChannel + ", presenters="
				+ presenters + ", episodeTotal=" + episodeTotal
				+ ", description=" + description + ", tags=" + tags
				+  ", score=" + score
				+ ", heatDegree=" + heatDegree 
				+ ", sourceID=" + sourceID 
				+ ", titleName=" +	titleName
				+ ", duration=" + duration 
				+ ", airDate=" +	airDate
				+ ", getContentUuid()=" +	getContentUuid()
				+ ", getUuid()=" + getUuid() + ", getStandardTime()="
				+ getStandardTime() + ", getStartTime()=" + getStartTime()
				+ ", getStatus()=" + getStatus() + ", getType()=" + getType()
				+ ", getCurrentSet()=" + getCurrentSet() + ", getTotalSet()="
				+ getTotalSet() + ", getProgramName()=" + getProgramName()
				+ ", getProgramFirstPageUrl()=" + getProgramFirstPageUrl()
				+ ", getProgramFirstPageAbsoluteUrl()="
				+ getProgramFirstPageAbsoluteUrl() + ", getResolutionRatio()="
				+ getResolutionRatio() + ", getLevel()=" + getLevel()
				+ ", getRelUrl()=" + getRelUrl() + ", getFindTime()="
				+ getFindTime() + ", getDramaUrl()=" + getDramaUrl()
				+ ", getDramaTitle()=" + getDramaTitle()
				+ ", getDramaAbsoluteUrl()=" + getDramaAbsoluteUrl()
				+ ", getDramaPhotoUrl()=" + getDramaPhotoUrl()
				+ ", getDramaPhotoTitle()=" + getDramaPhotoTitle()
				+ ", getDramaPhotoAbsoluteUrl()=" + getDramaPhotoAbsoluteUrl()
				+ ", getPlayerListUrl()=" + getPlayerListUrl()
				+ ", getPlayerListTitle()=" + getPlayerListTitle()
				+ ", getPlayerListAbsoluteUrl()=" + getPlayerListAbsoluteUrl()
				+ ", getPlayAddrUrl()=" + getPlayAddrUrl()
				+ ", getPlayAddrTitle()=" + getPlayAddrTitle()
				+ ", getPlayAddrAbsoluteUrl()=" + getPlayAddrAbsoluteUrl()
				+ ", getYmdTime()=" + getYmdTime() + ", getDayName()="
				+ getDayName() + ", getWeekName()=" + getWeekName()
				+ ", getChannelName()=" + getChannelName()
				+ ", getChannelGroupName()=" + getChannelGroupName()
				+ ", getAreaName()=" + getAreaName() + ", getSession()="
				+ getSession() + "]";
	}
	  
}
