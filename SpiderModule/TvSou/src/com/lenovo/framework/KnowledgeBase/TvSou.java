package com.lenovo.framework.KnowledgeBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main; 
import com.lenovo.framework.*;
import com.lenovo.framework.KnowledgeBase.bean.TvSouAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouEpgInfo;
 

public class TvSou implements IModule {
	private IBridge bridge = null;
    private int sleepMs = 10;
    private Boolean isBreak = false;//调试用
    private Map<String, String> mapDBFieldToEPGFirstPage = new HashMap<String, String>();
    private String downPoolName="/queue/proxy_pool";
    
    public void RegisterSelf(IBridge bridge) {
        this.bridge = bridge;
         bridge.RegisterModule("TvSou", this);         
        bridge.LogInfo("TvSou:RegisterSelf");       
        mapDBFieldToEPGFirstPage.put("EPG_ID", "uuid");    
        mapDBFieldToEPGFirstPage.put("NAME", "programName");
        mapDBFieldToEPGFirstPage.put("TYPE", "type");
        mapDBFieldToEPGFirstPage.put("ACTOR", "leadingRoles");
        mapDBFieldToEPGFirstPage.put("DIRECTOR", "directors");
        mapDBFieldToEPGFirstPage.put("PRESENTER", "presenters");
        mapDBFieldToEPGFirstPage.put("WRITER", "writers");
        mapDBFieldToEPGFirstPage.put("PRODUCER", "");
        mapDBFieldToEPGFirstPage.put("GUEST", "");
        mapDBFieldToEPGFirstPage.put("SESSION", "session");
        mapDBFieldToEPGFirstPage.put("EPISODE", "currentSet");
        mapDBFieldToEPGFirstPage.put("EPISODE_TOTAL", "totalSet");        
        mapDBFieldToEPGFirstPage.put("CHANNEL", "channelName");
        mapDBFieldToEPGFirstPage.put("AREA", "areaName");
        mapDBFieldToEPGFirstPage.put("BEGIN_TIME", "standardTime");
        mapDBFieldToEPGFirstPage.put("DESCRIPTION", "description");
        mapDBFieldToEPGFirstPage.put("MAIN_URL", "relUrl"); 
    }
    
    public void HandleTask(IMessage msg) {
    	bridge.LogInfo("[java] TvSou:HandleTask = "+msg.GetHeader("TaskType"));      
        if(msg.GetHeader("TaskType").equals("ListRoot")) {            
        	DoMsg_ListRoot(msg);
        }else if(msg.GetHeader("TaskType").equals("GetAreaPage")) {         
        	DoMsg_GetAreaPage( msg);		
        }else if (msg.GetHeader("TaskType").equals("GetChannelGroupPage")) {
    		 DoMsg_GetChannelGroupPage(msg);
    	}else if (msg.GetHeader("TaskType").equals("GetChannelPage")) {
			 DoMsg_GetChannelPage( msg);
    	}else if (msg.GetHeader("TaskType").equals("GetEpgInfo")) {
			DoMsg_GetEpgInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetProgramFirstPageInfo")) {
			DoMsg_GetProgramFirstPageInfo( msg);			
		}else if  (msg.GetHeader("TaskType").equals("GetProgramFirstPageUrlForTV")) {
			DoMsg_GetProgramFirstPageUrlForTV( msg);	
		}
   }
	private void WriteDB(Object info){
		bridge.LogDebug("WriteDB:"+info.toString());
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");// 
		outMsg.SetHeader("Table", "RAW_EPG");//
		outMsg.SetHeader("DB", "RAW_DB");//
		outMsg.SetHeader("MsgType", "40001");// 
		String strFields = "";
		int mapsize = mapDBFieldToEPGFirstPage.size();
		Iterator it = mapDBFieldToEPGFirstPage.entrySet().iterator();
		for (int i = 0; i < mapsize; i++) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String strValueTmp = (String) entry.getValue();
			if (strValueTmp.equals("")) {
			//	outMsg.SetHeader("Field_" + key, "");
			} else {
			 	String strTmpResult = GetObjectStrFieldValue(strValueTmp,  info);
				if (strTmpResult.equals("")==false){
					outMsg.SetHeader( "Field_" + key, strTmpResult);
					strFields += key + "/";
				}
			}				
		}
		strFields += "SOURCE";		
		outMsg.SetHeader("Field_SOURCE", "TVSOU");//
		outMsg.SetHeader("Fields", strFields);				
		bridge.SendMsg(outMsg);	
	}  
	
    private Boolean SerializeMsg(Object model, IMessage outMsg) {
		Boolean bRet = true;
		try {
			Field[] field = model.getClass().getDeclaredFields(); 
			for (int j = 0; j < field.length; j++) { 
				String name = field[j].getName();
				String nameOri =name;
				name = name.substring(0, 1).toUpperCase() + name.substring(1); 
				String type = field[j].getType().toString(); 
				String strValue = "";
				if (type.equals("class java.lang.String")) { 
					Method m = model.getClass().getMethod("get" + name);
					String value = (String) m.invoke(model);
					if (value != null) {
						strValue = value;
					}
				} else if (type.equals("int")) {
					Method m = model.getClass().getMethod("get" + name);
					Integer value = (Integer) m.invoke(model);
					if (value != null) {
						strValue = Integer.toString(value);
					}
				} else if (type.equals("short")) {
					Method m = model.getClass().getMethod("get" + name);
					Short value = (Short) m.invoke(model);
					if (value != null) {
						strValue = Short.toString(value);
					}
				} else if (type.equals("double")) {
					Method m = model.getClass().getMethod("get" + name);
					Double value = (Double) m.invoke(model);
					if (value != null) {
						strValue = Double.toString(value);
					}
				} else if (type.equals("boolean")) {
					Method m = model.getClass().getMethod("get" + name);
					Boolean value = (Boolean) m.invoke(model);
					if (value != null) {
						strValue = Boolean.toString(value);
					}
				} else if (type.equals("long")) {
					Method m = model.getClass().getMethod("get" + name);
					Long value = (Long) m.invoke(model);
					if (value != null) {
						strValue = Long.toString(value);
					}
				}			
			//	bridge.LogInfo("***SerializeMsg***" + nameOri + " : " + strValue);
				outMsg.SetHeader(nameOri, strValue);				
			}
		} catch (Exception e) {
			bRet = false;
		//	e.printStackTrace();
		}
		return bRet;
	}
    
    private Boolean UnSerializeMsg( IMessage inMsg,  Object model) {
    	Boolean bRet=true;
    	try {
			Field[] field = model.getClass().getDeclaredFields(); 
			for (int j = 0; j < field.length; j++) { 
				String name = field[j].getName(); 
				String nameOri = name;
				name = name.substring(0, 1).toUpperCase() + name.substring(1); // 
				String type = field[j].getType().toString(); // 
				String strValue = inMsg.GetHeader(nameOri);
				if (type.equals("class java.lang.String")) { // 
					Method m = model.getClass().getMethod("set" + name, String.class);
					 m.invoke(model, strValue); //
				} else if (type.equals("int")) {
					Method m = model.getClass().getMethod("set" + name, int.class);					
					 m.invoke(model, Integer.parseInt(strValue));				 
				}else if (type.equals("long")) {
					Method m = model.getClass().getMethod("set" + name, long.class);
					m.invoke(model, Long.parseLong(strValue));					 
				}				
			}
		} catch (Exception e) {
			bRet = false;
			//e.printStackTrace();
		}
    	return bRet;
    }
    
    private String  GetObjectStrFieldValue(String strFieldName,  Object model) {
    	String value="";
		try {
		//	Field field = model.getClass().getField(strFieldName);
			strFieldName = strFieldName.substring(0, 1).toUpperCase()+ strFieldName.substring(1); 
			Method m = model.getClass().getMethod("get" + strFieldName);			 
			value = (String) m.invoke(model); //
			if (value == null){
				value = "";
			}
		} catch (Exception e) {
			value = "";
//			e.printStackTrace();
		}	 
    	return value;
    }   
       
	private void MyWait(int sleepMs){
    	try {
			Thread.sleep(sleepMs);
		} catch (InterruptedException e) {				
		}
    }
    private IMessage CreateDownloadMsg(String nextTaskType, String url){
    	   IMessage outMsg = this.bridge.CreateMsg();	      
	       outMsg.SetHeader("destination", downPoolName);	      
	       outMsg.SetHeader("Module", "TvSou");	    
	       outMsg.SetHeader("NextTaskType",nextTaskType );
	       outMsg.SetHeader("Url", url);
	       outMsg.SetHeader("MsgType", "10005");
	       outMsg.SetHeader("Charset", "gb2312");
	       return outMsg;
    }     
    
	private void DoMsg_ListRoot(IMessage msg){
		IMessage outMsg = CreateDownloadMsg( "GetAreaPage", "http://epg.tvsou.com/head_area.js");
		outMsg.SetHeader("ParantUrl", "http://epg.tvsou.com/programys/TV_1/Channel_1/W1.htm");	
		outMsg.SetHeader("SeedUrl",  "http://epg.tvsou.com/head_area.js");		
	    bridge.SendMsg(outMsg);  
	}	
	
	private void DoMsg_GetAreaPage(IMessage msg) {//throws IOException{
		String html = msg.GetBody();
		String charset = "gb2312";	
   		/*
		RandomAccessFile	m_writer = new RandomAccessFile("d:/x", "rw");		
    	 m_writer.writeUTF(html);
    	 m_writer.close();   
    	 */
		
		TvSouAreaExtractor extractor = new TvSouAreaExtractor();				
		/*
		String[] charset1 = new String[1];
		byte[] byHtml = extractor.Download(msg.GetHeader("SeedUrl") , charset1);		 
		try {
			charset1[0] = "gb2312";
			html = new String(byHtml,  charset1[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		*/		
		List<TvSouAreaPageUrl> areaPagelist = new ArrayList<TvSouAreaPageUrl>();
		Context cx = ContextFactory.getGlobal().enterContext();
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(Context.VERSION_1_5);
		Global global = Main.getGlobal();
		global.init(cx);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			Main.setOut(ps);
			html = html.replace("document.writeln", "print");
			Main.setIn(new ByteArrayInputStream(html.getBytes()));
			Main.processSource(cx, null);			
			html = baos.toString("gb2312");	
		} catch (Exception ex) {
			bridge.LogDebug("TvSou_DoMsg_DoMsg_GetAreaPage_ERR: " + 	msg.GetHeader("SeedUrl") + ex.toString());//ex.printStackTrace();
		} finally {
			Context.exit();
		}			
		bridge.LogInfo(msg.GetHeader("ParantUrl"));
		if (extractor.getAreaPage(html, charset,	msg.GetHeader("ParantUrl"), areaPagelist)) {
			for (TvSouAreaPageUrl areaPageUrl : areaPagelist){				
			//	bridge.LogInfo(areaPageUrl.getAreaName() + " : " + areaPageUrl.getAbsoluteUrl());					
				IMessage outMsg = CreateDownloadMsg( "GetChannelGroupPage", areaPageUrl.getAbsoluteUrl());
				if ( SerializeMsg(areaPageUrl, outMsg) ){					 
					bridge.SendMsg(outMsg);
				}else{
					bridge.LogDebug("TvSou_DoMsg_GetAreaPage_SerializeMsg_ERR : " + areaPageUrl.toString());
				}
				MyWait(sleepMs);			
				if (isBreak) break;	 
			}
		}		
		if (areaPagelist.size() == 0){	
			 bridge.LogDebug("TvSou_DoMsg_GetAreaPage(warning) size:0 " + 	msg.GetHeader("SeedUrl"));
		}else{
			bridge.LogDebug("TvSou_DoMsg_GetAreaPage size:" + areaPagelist.size()+" "+	msg.GetHeader("SeedUrl"));
		}	
	}
	
	private void DoMsg_GetChannelGroupPage(IMessage msg){
		String html = msg.GetBody();
		String charset = "gb2312";	
		TvSouAreaPageUrl areaPageUrl = new TvSouAreaPageUrl();
		if ( UnSerializeMsg( msg,  areaPageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetChannelGroupPage_UnSerializeMsg_ERR : "+msg.toString());			
		}else{
			//bridge.LogInfo("DoMsg_GetChannelGroupPage UnSerializeMsg:" + areaPageUrl.toString());
			List<TvSouChannelGroupPageUrl> channelGroupPageList =new ArrayList<TvSouChannelGroupPageUrl>();	
			TvSouChannelGroupExtractor extractor = new TvSouChannelGroupExtractor();
			if (extractor.getChannelGroupPage(html, charset, areaPageUrl, channelGroupPageList)) {
				for (TvSouChannelGroupPageUrl channelGroupPageUrl : channelGroupPageList){
					//bridge.LogInfo("DoMsg_GetChannelGroupPage:" + channelGroupPageUrl.toString());
				//	bridge.LogInfo(channelGroupPageUrl.getChannelGroupName() + " : " + channelGroupPageUrl.getAbsoluteUrl() + " in " + channelGroupPageUrl.getAreaName());
					IMessage outMsg = CreateDownloadMsg( "GetChannelPage", channelGroupPageUrl.getAbsoluteUrl());
					if ( SerializeMsg(channelGroupPageUrl, outMsg) ){							
						bridge.SendMsg(outMsg);
					}else{
						bridge.LogDebug("TvSou_DoMsg_GetChannelGroupPage_SerializeMsg_ERR : " + areaPageUrl.toString());
					}
					MyWait(sleepMs);			
					if (isBreak) break;	 
				}
			}	
			if (channelGroupPageList.size() == 0){
				bridge.LogDebug("TvSou_DoMsg_GetChannelGroupPage(0warning) size: 0 " +  areaPageUrl.getAbsoluteUrl());
			}else{
				bridge.LogDebug("TvSou_DoMsg_GetChannelGroupPage size:" + channelGroupPageList.size() + " " + areaPageUrl.getAbsoluteUrl());
			}
		}	
	}
	
	private void DoMsg_GetChannelPage(IMessage msg){
		String html = msg.GetBody();
		String charset = "gb2312";		 
		TvSouChannelGroupPageUrl channelGroupPageUrl = new TvSouChannelGroupPageUrl();
		if ( UnSerializeMsg( msg,  channelGroupPageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetChannelPage_UnSerializeMsg_ERR : "+msg.toString());			
		}else{		 
			List<TvSouChannelPageUrl> channelPageList =  new ArrayList<TvSouChannelPageUrl>();
			TvSouChannelExtractor extractor = new TvSouChannelExtractor();
			if (extractor.getChannelPage(html, charset , channelGroupPageUrl, channelPageList)) {
				for (TvSouChannelPageUrl channelPageUrl : channelPageList){
				//	bridge.LogInfo(channelPageUrl.getChannelGroupName() + " : " + channelPageUrl.getAbsoluteUrl() + " in " + channelPageUrl.getAreaName());
					IMessage outMsg = CreateDownloadMsg( "GetEpgInfo", channelPageUrl.getAbsoluteUrl());
					if ( SerializeMsg(channelPageUrl, outMsg) ){					 
						bridge.SendMsg(outMsg);
					}else{
						bridge.LogDebug("TvSou_DoMsg_GetChannelPage_SerializeMsg_ERR : " + channelPageUrl.toString());
					}					
					MyWait(sleepMs);			
					if (isBreak) break;	  	 
				}
			}		
			if (channelPageList.size() == 0){
				bridge.LogDebug("TvSou_DoMsg_GetChannelPage(0warning) size: 0 " + channelGroupPageUrl.getAbsoluteUrl() );
			}else{
				bridge.LogDebug("TvSou_DoMsg_GetChannelPage size:" + channelPageList.size() + " " + channelGroupPageUrl.getAbsoluteUrl() );
			} 
		}
	}
	
	private void DoMsg_GetEpgInfo(IMessage msg){
		String html = msg.GetBody();
		String charset = "gb2312";
		TvSouChannelPageUrl channelPageUrl =new TvSouChannelPageUrl();
		if ( UnSerializeMsg( msg,  channelPageUrl) == false){					
			bridge.LogInfo("TvSou_DoMsg_GetEpgInfo_UnSerializeMsg_ERR : "+msg.toString());			
		}else{	
			List<TvSouEpgInfo> EpgList = new ArrayList<TvSouEpgInfo>();		
			TvSouEpgExtractor extractor = new TvSouEpgExtractor();		 
			if (extractor.getEpgInfo(html, charset, channelPageUrl,	EpgList)) {
				for (TvSouEpgInfo epgInfo : EpgList){						
					String nextDownloadUrl = "";
					if (epgInfo.getType().equals("栏目")){//栏目
						if (epgInfo.getProgramFirstPageAbsoluteUrl()!=null && !epgInfo.getProgramFirstPageAbsoluteUrl().isEmpty()){						
							nextDownloadUrl = epgInfo.getProgramFirstPageAbsoluteUrl();
						}
					}else{//电影 电视剧
						if (epgInfo.getDramaAbsoluteUrl()!=null || !epgInfo.getDramaAbsoluteUrl().isEmpty()){							
							nextDownloadUrl = epgInfo.getDramaAbsoluteUrl();
						}
					}					
					if (nextDownloadUrl.isEmpty() ){
						WriteDB(epgInfo);
					}else{
						bridge.LogDebug("DoMsg_GetEpgInfo nextDownloadUrl : " + nextDownloadUrl);
						if (epgInfo.getType().equals("电视剧")){//需要多下一层提取 “本片概述 	” 页的url
							IMessage outMsg = CreateDownloadMsg("GetProgramFirstPageUrlForTV", nextDownloadUrl);
							if (SerializeMsg(epgInfo, outMsg)){					 
								bridge.SendMsg(outMsg);
							}else{
								bridge.LogDebug("TvSou_DoMsg_GetEpgInfo_ForTV_SerializeMsg_ERR : " + epgInfo.toString());
								WriteDB(epgInfo);
							}		
						}else{
							IMessage outMsg = CreateDownloadMsg("GetProgramFirstPageInfo", nextDownloadUrl);
							if (SerializeMsg(epgInfo, outMsg)){					 
								bridge.SendMsg(outMsg);
							}else{
								bridge.LogDebug("TvSou_DoMsg_GetEpgInfo_SerializeMsg_ERR : " + epgInfo.toString());
								WriteDB(epgInfo);
							}		
						}
					}
				}
			}		
			if (EpgList.size() == 0){
				bridge.LogDebug("TvSou_DoMsg_GetEPGInfo(0warning)  size: 0 " + channelPageUrl.getAbsoluteUrl() );
			}else{
				bridge.LogDebug("TvSou_DoMsg_GetEPGInfo  size:" + EpgList.size() + " " + channelPageUrl.getAbsoluteUrl());
			}
		}
	}
	private void DoMsg_GetProgramFirstPageUrlForTV(IMessage msg){
		String html = msg.GetBody();
		String charset = "gb2312";
		TvSouEpgInfo epgInfo = new TvSouEpgInfo();
		if ( UnSerializeMsg( msg,  epgInfo) == false){					
			bridge.LogInfo("TvSou_DoMsg_GetProgramFirstPageUrlForTV_UnSerializeMsg_ERR : " + msg.toString());			
		}else{			
			TvSouTvFirstPageUrlExtractor extractor = new TvSouTvFirstPageUrlExtractor();
			if ( extractor.getFirstPageUrl(html, charset, epgInfo) ){
				IMessage outMsg = CreateDownloadMsg("GetProgramFirstPageInfo", epgInfo.getProgramFirstPageAbsoluteUrl());
				if (SerializeMsg(epgInfo, outMsg)){					 
					bridge.SendMsg(outMsg);
				}else{
					bridge.LogDebug("TvSou_DoMsg_GetProgramFirstPageUrlForTV_ERR : " + epgInfo.toString());
					WriteDB(epgInfo);		
				}	
			}else{
				WriteDB(epgInfo);		
			}
		}
	}
	private void DoMsg_GetProgramFirstPageInfo(IMessage msg){
		String html = msg.GetBody();
		String charset = "gb2312";
		TvSouEpgInfo epgInfo = new TvSouEpgInfo();
		if ( UnSerializeMsg( msg,  epgInfo) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetProgramFirstPageInfo_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvSouEpgFirstPageInfoExtractor extractor = new TvSouEpgFirstPageInfoExtractor();
			if ( extractor.getEpgFirstPageInfo(html, charset, epgInfo) == false){				 		
				bridge.LogDebug("TvSou_DoMsg_GetProgramFirstPageInfo_Extract_ERR : "+ epgInfo.toString());	
			}			
			WriteDB(epgInfo);					
		}
	}
	
 
    
}
