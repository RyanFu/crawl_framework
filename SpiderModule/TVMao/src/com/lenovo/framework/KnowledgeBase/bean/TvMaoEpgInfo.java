package com.lenovo.framework.KnowledgeBase.bean;

public class TvMaoEpgInfo {
	private String uuid="";
	private String standardTime="";
	private String startTime="";
	private String status="";	
	private String  type="";
	private String  currentSet="";
	private String  totalSet="";
	private String oriProgramName="";//原始epg item信息
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
	
	private String abs="";
	private String contentUuid;
	 
	public String getOriProgramName() {
		return oriProgramName;
	}

	public void setOriProgramName(String oriProgramName) {
		this.oriProgramName = oriProgramName;
	}

	public String getOriprogramName() {
		return oriProgramName;
	}

	public void setOriprogramName(String oriprogramName) {
		this.oriProgramName = oriprogramName;
	}

	public String getContentUuid() {
		return contentUuid;
	}

	public void setContentUuid(String contentUuid) {
		this.contentUuid = contentUuid;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
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

	public void setDayName(String dayName) {
		this.dayName = dayName;
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

	@Override
	public String toString() {
		return "TvMaoEpgInfo [uuid=" + uuid + ", standardTime=" + standardTime
				+ ", startTime=" + startTime + ", status=" + status + ", type="
				+ type + ", currentSet=" + currentSet + ", totalSet="
				+ totalSet + ", oriProgramName=" + oriProgramName
				+ ", programName=" + programName + ", programFirstPageUrl="
				+ programFirstPageUrl + ", programFirstPageAbsoluteUrl="
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
				+ ", areaName=" + areaName + ", session=" + session + ", abs="
				+ abs + ", contentUuid=" + contentUuid + "]";
	}

	 
	 
 
}
