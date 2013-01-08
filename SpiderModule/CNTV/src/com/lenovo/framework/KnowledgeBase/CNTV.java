package com.lenovo.framework.KnowledgeBase;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.lenovo.framework.*;

public class CNTV implements IModule {
	private IBridge bridge = null;
    private int nSleep = 10;
    private Boolean bBreak = false;
    private Map<String, String> m_mapDBFieldToEPGFirstPage = new HashMap<String, String>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
    private String downPoolName="/queue/proxy_pool";
    
    public void RegisterSelf(IBridge bridge) {
		this.bridge = bridge;
		bridge.RegisterModule("CNTV", this);
		bridge.LogInfo("CNTV:RegisterSelf");
        
        m_mapDBFieldToEPGFirstPage.put("EPG_ID", "strUUID");
     //   m_mapDBFieldToEPGFirstPage.put("SOURCE", "strRelUrl");
        m_mapDBFieldToEPGFirstPage.put("NAME", "strProgramName");
        m_mapDBFieldToEPGFirstPage.put("TYPE", "strType");
       /* m_mapDBFieldToEPGFirstPage.put("ACTOR", "strLeadingRoles");
        m_mapDBFieldToEPGFirstPage.put("DIRECTOR", "strDirectors");
        m_mapDBFieldToEPGFirstPage.put("PRESENTER", "strPresenters");
        m_mapDBFieldToEPGFirstPage.put("WRITER", "strWriters");
        m_mapDBFieldToEPGFirstPage.put("PRODUCER", "");
        m_mapDBFieldToEPGFirstPage.put("GUEST", "");*/
        m_mapDBFieldToEPGFirstPage.put("SESSION", "strSession");
        m_mapDBFieldToEPGFirstPage.put("EPISODE", "strCurrentSet");
        m_mapDBFieldToEPGFirstPage.put("EPISODE_TOTAL", "strTotalSet");        
        m_mapDBFieldToEPGFirstPage.put("CHANNEL", "strChannelName");
        m_mapDBFieldToEPGFirstPage.put("AREA", "strAreaName");
        m_mapDBFieldToEPGFirstPage.put("BEGIN_TIME", "strStandardTime");
      //  m_mapDBFieldToEPGFirstPage.put("DESCRIPTION", "strDescription");
        m_mapDBFieldToEPGFirstPage.put("MAIN_URL", "strRelUrl"); 
    }

	@Override
	public void HandleTask(IMessage msg) {
		bridge.LogInfo("[java] CNTV = " + msg.GetHeader("TaskType"));		
		if (msg.GetHeader("TaskType").equals("ListRoot")) {
			DoMsg_ListRoot(msg);
		} else if (msg.GetHeader("TaskType").equals("CNTV_GetChannelInfo")) {
			DoMsg_CNTV_GetChannelInfo(msg);
		} else if (msg.GetHeader("TaskType").equals("CNTV_GetEpgInfo")) {
			DoMsg_CNTV_GetEpgInfo(msg);
		}
	}
	
	private void DoMsg_ListRoot(IMessage msg){
		   IMessage outMsg = this.bridge.CreateMsg();
	       outMsg.SetHeader("SeedUrl", "http://tv.cntv.cn/epg?channel=cctvxiqu");	       
	       outMsg.SetHeader("destination", downPoolName);//
	       outMsg.SetHeader("Url", "http://tv.cntv.cn/epg?channel=cctvxiqu");//
	       outMsg.SetHeader("Module", "CNTV");//
	       outMsg.SetHeader("NextTaskType", "CNTV_GetChannelInfo");//
	       outMsg.SetHeader("MsgType", "10005");//
	       bridge.SendMsg(outMsg);  
	}

	private void DoMsg_CNTV_GetChannelInfo(IMessage msg) {
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		CNTVChannelExtrator channelExtrator = new CNTVChannelExtrator();
		List<CNTVChannelInfo> channelTable = new ArrayList<CNTVChannelInfo>();
		if (channelExtrator.GetChannels(strHtml, strCharset,msg.GetHeader("SeedUrl"), channelTable)) {			
			bridge.LogInfo(channelTable.toString());
			bridge.LogInfo("channels:");
			Iterator<CNTVChannelInfo> itr = channelTable.iterator();
			while (itr.hasNext()) {
				CNTVChannelInfo channelInfo = itr.next();
				bridge.LogInfo(channelInfo.getName() + " : "+ channelInfo.getUrlMark());
				java.util.List<String> urls = new ArrayList<String>();
				GenerateUrl( channelInfo,urls);	 
				bridge.LogInfo("DoMsg_CNTV_GetChannelInfo url:" + urls.toString());
				Iterator<String> itrUrl = urls.iterator();
				while  (itrUrl.hasNext()) {
					String url = itrUrl.next();				
					IMessage outMsg = this.bridge.CreateMsg();
					outMsg.SetHeader("Name", channelInfo.getName());
					outMsg.SetHeader("RefUrl", channelInfo.getRefUrl());
					outMsg.SetHeader("UrlMark", channelInfo.getUrlMark());	
					outMsg.SetHeader("EpgUrl", url);	
					outMsg.SetHeader("destination", downPoolName);
			 		outMsg.SetHeader("Url", url);
					outMsg.SetHeader("Module", "CNTV");
					outMsg.SetHeader("NextTaskType", "CNTV_GetEpgInfo");
					outMsg.SetHeader("MsgType", "10005");
					bridge.SendMsg(outMsg);
					try {
						Thread.sleep(nSleep);
					} catch (InterruptedException e) {					
					//	e.printStackTrace();
					}
					if (bBreak)
						break;
				}
				if (bBreak)
					break;			
				}			
		} else {
			bridge.LogInfo("DoMsg_CNTV_GetChannelInfo err");
		}
		
		if (channelTable.size() == 0){
			bridge.LogDebug("DoMsg_CNTV_GetChannelInfo(0warning) size:0" + " " + msg.GetHeader("SeedUrl") );
		}else{
			bridge.LogDebug("DoMsg_CNTV_GetChannelInfo size: " +channelTable.size() +" "+ msg.GetHeader("SeedUrl"));
		}
	}
	
	private void DoMsg_CNTV_GetEpgInfo(IMessage msg) {		
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		CNTVChannelInfo channelInfo = new CNTVChannelInfo();
		channelInfo.setName(msg.GetHeader("Name")) ;
		channelInfo.setRefUrl(msg.GetHeader("RefUrl"));
		channelInfo.setUrlMark(msg.GetHeader("UrlMark"));		 
		String strEpgUrl = msg.GetHeader("EpgUrl");
		
		List<CNTVEpgInfo> epgInfos = new ArrayList<CNTVEpgInfo>();	 
		CNTVEpgExtrator doExtrator = new CNTVEpgExtrator();
		if (doExtrator.GetEPGInfo(strHtml, strCharset, channelInfo, strEpgUrl, epgInfos) ) {
		 	Iterator<CNTVEpgInfo> itr = epgInfos.iterator();
			while (itr.hasNext()) {
				CNTVEpgInfo epgInfo = itr.next();
				bridge.LogInfo("DoMsg_CNTV_GetEpgInfo:"+epgInfo.toString());			
				IMessage outMsg = this.bridge.CreateMsg();
				outMsg.SetHeader("destination", "/queue/db_operation");
				outMsg.SetHeader("Table", "RAW_EPG");
				outMsg.SetHeader("DB", "RAW_DB");
				outMsg.SetHeader("MsgType", "40001");
				String strFields = "";
				int mapsize = m_mapDBFieldToEPGFirstPage.size();
				Iterator it = m_mapDBFieldToEPGFirstPage.entrySet().iterator();
				for (int i = 0; i < mapsize; i++) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = (String) entry.getKey();
					String strValueTmp = (String) entry.getValue();
					if (strValueTmp.equals("")) {
					//	outMsg.SetHeader("Field_" + key, "");
					} else {						 
						String strTmpResult = GetObjectStrFieldValue(strValueTmp,  epgInfo);
						if (strTmpResult.equals("")==false){
							outMsg.SetHeader( "Field_" + key, strTmpResult);
							strFields += key + "/";
						}
					}				
				}
				strFields += "SOURCE";		
				outMsg.SetHeader("Field_SOURCE", "CNTV");
				outMsg.SetHeader("Fields", strFields);				
				bridge.SendMsg(outMsg);					 			 
				try {
					Thread.sleep(nSleep);
				} catch (InterruptedException e) {				
				//	e.printStackTrace();
				}
				if (bBreak)
					break;				 
			}			 
		}					
		if (epgInfos.size() == 0){
			bridge.LogDebug("DoMsg_CNTV_GetEpgInfo(0warning) size:0" + " "+strEpgUrl );
		}else{
			bridge.LogDebug("DoMsg_CNTV_GetEpgInfo size: " +epgInfos.size() +" "+ strEpgUrl);
		}
	}
	
	private void GenerateUrl(CNTVChannelInfo channelInfo, java.util.List<String> urls){		
		//取当前日期前3天，及后7天的时间表		
	 	for (int i=-3; i<8; i++){
	 		Calendar calendar = Calendar.getInstance();
	 		calendar.add(Calendar.HOUR, 24*i);
	 		String strDateField = dateFormat.format(calendar.getTime());
	 		String strUrl = "http://tv.cntv.cn/index.php?action=epg-list&date="+strDateField+"&channel="+channelInfo.getUrlMark();
	 		urls.add(strUrl);
	 	}
	}
	  private String  GetObjectStrFieldValue(String strFieldName,  Object model) {
	    	String value="";
			try {
			//	Field field = model.getClass().getField(strFieldName);
				strFieldName = strFieldName.substring(0, 1).toUpperCase()+ strFieldName.substring(1); 
				Method m = model.getClass().getMethod("get" + strFieldName);			 
				value = (String) m.invoke(model); 
				if (value == null){
					value = "";
				}
			} catch (Exception e) {
				value = "";
			//	e.printStackTrace();
			}			 
	    	return value;
	    }
}
