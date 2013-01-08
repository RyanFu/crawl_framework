package com.lenovo.framework.KnowledgeBase;

public class CNTVEpgInfo {
	private String strUUID="";
	private String strStandardTime="";
	private String strStartTime="";
	private String strStatus="";	
	private String strType="";
	private String strCurrentSet="";
	private String strTotalSet="";
	private String strProgramName="";
	private String strProgramFirstPageUrl="";
	private String strProgramFirstPageAbsoluteUrl=""; 
	private String strResolutionRatio="";	 
	private int     nLevel=0;
	private String strRelUrl="";
	private long  lFindTime=0;
	private String strDramaUrl="";
	private String strDramaTitle="";
	private String strDramaAbsoluteUrl="";
	private String strDramaPhotoUrl="";
	private String strDramaPhotoTitle="";
	private String strDramaPhotoAbsoluteUrl="";
	private String strPlayerListUrl="";
	private String strPlayerListTitle="";
	private String strPlayerListAbsoluteUrl="";	
	private String strPlayAddrUrl="";
	private String strPlayAddrTitle="";
	private String strPlayAddrAbsoluteUrl="";
	private String strYmdTime=""; 
	private String strDayName=""; 
	private String strWeekName="";
	private String strChannelName="";
	private String strChannelGroupName="";
	private String strAreaName="";	
	private String strSession="";
	 
	@Override
	public String toString() {
		return "CEPGInfo [strUUID=" + strUUID + ", strStandardTime="
				+ strStandardTime + ", strStartTime=" + strStartTime
				+ ", strStatus=" + strStatus + ", strType=" + strType
				+ ", strCurrentSet=" + strCurrentSet + ", strTotalSet="
				+ strTotalSet + ", strProgramName=" + strProgramName
				+ ", strProgramFirstPageUrl=" + strProgramFirstPageUrl
				+ ", strProgramFirstPageAbsoluteUrl="
				+ strProgramFirstPageAbsoluteUrl + ", strResolutionRatio="
				+ strResolutionRatio + ", nLevel=" + nLevel + ", strRelUrl="
				+ strRelUrl + ", lFindTime=" + lFindTime + ", strDramaUrl="
				+ strDramaUrl + ", strDramaTitle=" + strDramaTitle
				+ ", strDramaAbsoluteUrl=" + strDramaAbsoluteUrl
				+ ", strDramaPhotoUrl=" + strDramaPhotoUrl
				+ ", strDramaPhotoTitle=" + strDramaPhotoTitle
				+ ", strDramaPhotoAbsoluteUrl=" + strDramaPhotoAbsoluteUrl
				+ ", strPlayerListUrl=" + strPlayerListUrl
				+ ", strPlayerListTitle=" + strPlayerListTitle
				+ ", strPlayerListAbsoluteUrl=" + strPlayerListAbsoluteUrl
				+ ", strPlayAddrUrl=" + strPlayAddrUrl + ", strPlayAddrTitle="
				+ strPlayAddrTitle + ", strPlayAddrAbsoluteUrl="
				+ strPlayAddrAbsoluteUrl + ", strYmdTime=" + strYmdTime
				+ ", strDayName=" + strDayName + ", strWeekName=" + strWeekName
				+ ", strChannelName=" + strChannelName
				+ ", strChannelGroupName=" + strChannelGroupName
				+ ", strAreaName=" + strAreaName + ", strSession=" + strSession
				+ "]";
	}
	
	public String getStrSession() {
		return strSession;
	}

	public void setStrSession(String strSession) {
		this.strSession = strSession;
	}

	public String getStrUUID() {
		return strUUID;
	}
	public void setStrUUID(String strUUID) {
		this.strUUID = strUUID;
	}
	public String getStrStandardTime() {
		return strStandardTime;
	}
	public void setStrStandardTime(String strStandardTime) {
		this.strStandardTime = strStandardTime;
	}
	public String getStrStartTime() {
		return strStartTime;
	}
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrType() {
		return strType;
	}
	public void setStrType(String strType) {
		this.strType = strType;
	}
	public String getStrCurrentSet() {
		return strCurrentSet;
	}
	public void setStrCurrentSet(String strCurrentSet) {
		this.strCurrentSet = strCurrentSet;
	}
	public String getStrTotalSet() {
		return strTotalSet;
	}
	public void setStrTotalSet(String strTotalSet) {
		this.strTotalSet = strTotalSet;
	}
	public String getStrProgramName() {
		return strProgramName;
	}
	public void setStrProgramName(String strProgramName) {
		this.strProgramName = strProgramName;
	}
	public String getStrProgramFirstPageUrl() {
		return strProgramFirstPageUrl;
	}
	public void setStrProgramFirstPageUrl(String strProgramFirstPageUrl) {
		this.strProgramFirstPageUrl = strProgramFirstPageUrl;
	}
	public String getStrProgramFirstPageAbsoluteUrl() {
		return strProgramFirstPageAbsoluteUrl;
	}
	public void setStrProgramFirstPageAbsoluteUrl(
			String strProgramFirstPageAbsoluteUrl) {
		this.strProgramFirstPageAbsoluteUrl = strProgramFirstPageAbsoluteUrl;
	}
	public String getStrResolutionRatio() {
		return strResolutionRatio;
	}
	public void setStrResolutionRatio(String strResolutionRatio) {
		this.strResolutionRatio = strResolutionRatio;
	}
	public int getNLevel() {
		return nLevel;
	}
	public void setNLevel(int nLevel) {
		this.nLevel = nLevel;
	}
	public String getStrRelUrl() {
		return strRelUrl;
	}
	public void setStrRelUrl(String strRelUrl) {
		this.strRelUrl = strRelUrl;
	}
	public long getLFindTime() {
		return lFindTime;
	}
	public void setLFindTime(long lFindTime) {
		this.lFindTime = lFindTime;
	}
	public String getStrDramaUrl() {
		return strDramaUrl;
	}
	public void setStrDramaUrl(String strDramaUrl) {
		this.strDramaUrl = strDramaUrl;
	}
	public String getStrDramaTitle() {
		return strDramaTitle;
	}
	public void setStrDramaTitle(String strDramaTitle) {
		this.strDramaTitle = strDramaTitle;
	}
	public String getStrDramaAbsoluteUrl() {
		return strDramaAbsoluteUrl;
	}
	public void setStrDramaAbsoluteUrl(String strDramaAbsoluteUrl) {
		this.strDramaAbsoluteUrl = strDramaAbsoluteUrl;
	}
	public String getStrDramaPhotoUrl() {
		return strDramaPhotoUrl;
	}
	public void setStrDramaPhotoUrl(String strDramaPhotoUrl) {
		this.strDramaPhotoUrl = strDramaPhotoUrl;
	}
	public String getStrDramaPhotoTitle() {
		return strDramaPhotoTitle;
	}
	public void setStrDramaPhotoTitle(String strDramaPhotoTitle) {
		this.strDramaPhotoTitle = strDramaPhotoTitle;
	}
	public String getStrDramaPhotoAbsoluteUrl() {
		return strDramaPhotoAbsoluteUrl;
	}
	public void setStrDramaPhotoAbsoluteUrl(String strDramaPhotoAbsoluteUrl) {
		this.strDramaPhotoAbsoluteUrl = strDramaPhotoAbsoluteUrl;
	}
	public String getStrPlayerListUrl() {
		return strPlayerListUrl;
	}
	public void setStrPlayerListUrl(String strPlayerListUrl) {
		this.strPlayerListUrl = strPlayerListUrl;
	}
	public String getStrPlayerListTitle() {
		return strPlayerListTitle;
	}
	public void setStrPlayerListTitle(String strPlayerListTitle) {
		this.strPlayerListTitle = strPlayerListTitle;
	}
	public String getStrPlayerListAbsoluteUrl() {
		return strPlayerListAbsoluteUrl;
	}
	public void setStrPlayerListAbsoluteUrl(String strPlayerListAbsoluteUrl) {
		this.strPlayerListAbsoluteUrl = strPlayerListAbsoluteUrl;
	}
	public String getStrPlayAddrUrl() {
		return strPlayAddrUrl;
	}
	public void setStrPlayAddrUrl(String strPlayAddrUrl) {
		this.strPlayAddrUrl = strPlayAddrUrl;
	}
	public String getStrPlayAddrTitle() {
		return strPlayAddrTitle;
	}
	public void setStrPlayAddrTitle(String strPlayAddrTitle) {
		this.strPlayAddrTitle = strPlayAddrTitle;
	}
	public String getStrPlayAddrAbsoluteUrl() {
		return strPlayAddrAbsoluteUrl;
	}
	public void setStrPlayAddrAbsoluteUrl(String strPlayAddrAbsoluteUrl) {
		this.strPlayAddrAbsoluteUrl = strPlayAddrAbsoluteUrl;
	}
	public String getStrYmdTime() {
		return strYmdTime;
	}
	public void setStrYmdTime(String strYmdTime) {
		this.strYmdTime = strYmdTime;
	}
	public String getStrDayName() {
		return strDayName;
	}
	public void setStrDayName(String strDayName) {
		this.strDayName = strDayName;
	}
	public String getStrWeekName() {
		return strWeekName;
	}
	public void setStrWeekName(String strWeekName) {
		this.strWeekName = strWeekName;
	}
	public String getStrChannelName() {
		return strChannelName;
	}
	public void setStrChannelName(String strChannelName) {
		this.strChannelName = strChannelName;
	}
	public String getStrChannelGroupName() {
		return strChannelGroupName;
	}
	public void setStrChannelGroupName(String strChannelGroupName) {
		this.strChannelGroupName = strChannelGroupName;
	}
	public String getStrAreaName() {
		return strAreaName;
	}
	public void setStrAreaName(String strAreaName) {
		this.strAreaName = strAreaName;
	}
 
}
