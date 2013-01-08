package com.lenovo.framework.KnowledgeBase.bean;

public class TvSouEpgInfo  implements Cloneable{ 
	private String uuid="";
	private String standardTime="";
	private String startTime="";
	private String status="";	
	private String type="";
	private String currentSet="";
	private String totalSet="";
	private String programName="";
	private String programFirstPageUrl="";
	private String programFirstPageAbsoluteUrl=""; 
	private String resolutionRatio="";	 
	private int    level=0;
	private String relUrl="";
	private long findTime=0;
	private String dramaUrl="";
	private String dramaTitle="";
	private String dramaAbsoluteUrl="";
	private String dramaPhotoUrl="";
	private String dramaPhotoTitle="";
	private String dramaPhotoAbsoluteUrl="";
	private String playerListUrl="";
	private String playerListTitle="";
	private String playerListAbsoluteUrl="";	
	private String playAddrUrl="";
	private String playAddrTitle="";
	private String playAddrAbsoluteUrl="";	
	private String ymdTime=""; 
	private String dayName=""; 
	private String weekName="";
	private String channelName="";
	private String channelGroupName="";
	private String areaName="";	
	private String session="";	 
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
	
	private String score;
	@Override
	public Object clone() {
		TvSouEpgInfo o = null;
		try {
			o = (TvSouEpgInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			//e.printStackTrace();
			o = null;
		}
		return o;
	}

	 

	@Override
	public String toString() {
		return "EpgInfo [uuid=" + uuid + ", standardTime=" + standardTime
				+ ", startTime=" + startTime + ", status=" + status + ", type="
				+ type + ", currentSet=" + currentSet + ", totalSet="
				+ totalSet + ", programName=" + programName
				+ ", programFirstPageUrl=" + programFirstPageUrl
				+ ", programFirstPageAbsoluteUrl="
				+ programFirstPageAbsoluteUrl + ", resolutionRatio="
				+ resolutionRatio + ", level=" + level + ", relUrl=" + relUrl
				+ ", findTime=" + findTime + ", dramaUrl=" + dramaUrl
				+ ", dramaTitle=" + dramaTitle + ", dramaAbsoluteUrl="
				+ dramaAbsoluteUrl + ", dramaPhotoUrl=" + dramaPhotoUrl
				+ ", dramaPhotoTitle=" + dramaPhotoTitle
				+ ", dramaPhotoAbsoluteUrl=" + dramaPhotoAbsoluteUrl
				+ ", playerListUrl=" + playerListUrl + ", playerListTitle="
				+ playerListTitle + ", playerListAbsoluteUrl="
				+ playerListAbsoluteUrl + ", playAddrUrl=" + playAddrUrl
				+ ", playAddrTitle=" + playAddrTitle + ", playAddrAbsoluteUrl="
				+ playAddrAbsoluteUrl + ", ymdTime=" + ymdTime + ", dayName="
				+ dayName + ", weekName=" + weekName + ", channelName="
				+ channelName + ", channelGroupName=" + channelGroupName
				+ ", areaName=" + areaName + ", session=" + session
				+ ", directors=" + directors + ", writers=" + writers
				+ ", leadingRoles=" + leadingRoles + ", programArea="
				+ programArea + ", lang=" + lang + ", year=" + year
				+ ", firstPlayChannel=" + firstPlayChannel + ", presenters="
				+ presenters + ", episodeTotal=" + episodeTotal
				+ ", description=" + description + ", tags=" + tags
				+ ", score=" + score + "]";
	}



	public String getScore() {
		return score;
	}



	public void setScore(String score) {
		this.score = score;
	}



	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getStandardTime() {
		return standardTime;
	}
	public void setStandardTime(String standardTime) {
		this.standardTime = standardTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCurrentSet() {
		return currentSet;
	}
	public void setCurrentSet(String currentSet) {
		this.currentSet = currentSet;
	}
	public String getTotalSet() {
		return totalSet;
	}
	public void setTotalSet(String totalSet) {
		this.totalSet = totalSet;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getProgramFirstPageUrl() {
		return programFirstPageUrl;
	}
	public void setProgramFirstPageUrl(String programFirstPageUrl) {
		this.programFirstPageUrl = programFirstPageUrl;
	}
	public String getProgramFirstPageAbsoluteUrl() {
		return programFirstPageAbsoluteUrl;
	}
	public void setProgramFirstPageAbsoluteUrl(String programFirstPageAbsoluteUrl) {
		this.programFirstPageAbsoluteUrl = programFirstPageAbsoluteUrl;
	}
	public String getResolutionRatio() {
		return resolutionRatio;
	}
	public void setResolutionRatio(String resolutionRatio) {
		this.resolutionRatio = resolutionRatio;
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
	public String getDramaUrl() {
		return dramaUrl;
	}
	public void setDramaUrl(String dramaUrl) {
		this.dramaUrl = dramaUrl;
	}
	public String getDramaTitle() {
		return dramaTitle;
	}
	public void setDramaTitle(String dramaTitle) {
		this.dramaTitle = dramaTitle;
	}
	public String getDramaAbsoluteUrl() {
		return dramaAbsoluteUrl;
	}
	public void setDramaAbsoluteUrl(String dramaAbsoluteUrl) {
		this.dramaAbsoluteUrl = dramaAbsoluteUrl;
	}
	public String getDramaPhotoUrl() {
		return dramaPhotoUrl;
	}
	public void setDramaPhotoUrl(String dramaPhotoUrl) {
		this.dramaPhotoUrl = dramaPhotoUrl;
	}
	public String getDramaPhotoTitle() {
		return dramaPhotoTitle;
	}
	public void setDramaPhotoTitle(String dramaPhotoTitle) {
		this.dramaPhotoTitle = dramaPhotoTitle;
	}
	public String getDramaPhotoAbsoluteUrl() {
		return dramaPhotoAbsoluteUrl;
	}
	public void setDramaPhotoAbsoluteUrl(String dramaPhotoAbsoluteUrl) {
		this.dramaPhotoAbsoluteUrl = dramaPhotoAbsoluteUrl;
	}
	public String getPlayerListUrl() {
		return playerListUrl;
	}
	public void setPlayerListUrl(String playerListUrl) {
		this.playerListUrl = playerListUrl;
	}
	public String getPlayerListTitle() {
		return playerListTitle;
	}
	public void setPlayerListTitle(String playerListTitle) {
		this.playerListTitle = playerListTitle;
	}
	public String getPlayerListAbsoluteUrl() {
		return playerListAbsoluteUrl;
	}
	public void setPlayerListAbsoluteUrl(String playerListAbsoluteUrl) {
		this.playerListAbsoluteUrl = playerListAbsoluteUrl;
	}
	public String getPlayAddrUrl() {
		return playAddrUrl;
	}
	public void setPlayAddrUrl(String playAddrUrl) {
		this.playAddrUrl = playAddrUrl;
	}
	public String getPlayAddrTitle() {
		return playAddrTitle;
	}
	public void setPlayAddrTitle(String playAddrTitle) {
		this.playAddrTitle = playAddrTitle;
	}
	public String getPlayAddrAbsoluteUrl() {
		return playAddrAbsoluteUrl;
	}
	public void setPlayAddrAbsoluteUrl(String playAddrAbsoluteUrl) {
		this.playAddrAbsoluteUrl = playAddrAbsoluteUrl;
	}
	public String getYmdTime() {
		return ymdTime;
	}
	public void setYmdTime(String ymdTime) {
		this.ymdTime = ymdTime;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String ddayName) {
		this.dayName = ddayName;
	}
	public String getWeekName() {
		return weekName;
	}
	public void setWeekName(String weekName) {
		this.weekName = weekName;
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
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
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
	
	 
	 
 
}
