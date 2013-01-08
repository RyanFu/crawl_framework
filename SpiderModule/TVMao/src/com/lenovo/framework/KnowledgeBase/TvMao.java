package com.lenovo.framework.KnowledgeBase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

 

 
import com.lenovo.framework.*;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoActor;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoContentImg;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpisodeStory;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoPersonImg;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoReviewInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoTimeUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoUrlCache;

public class TvMao implements IModule {
	private IBridge bridge = null;
    private int sleepMs = 10;
    private Boolean isBreak = false;//调试用
    private Map<String, String> rawEpgFieldTable = new HashMap<String, String>();
    private Map<String, String> rawContentFieldTable = new HashMap<String, String>();
    private Map<String, String> rawEpisodeStoryFieldTable = new HashMap<String, String>();
    private Map<String, String> contentImgFieldTable = new HashMap<String, String>();
    private Map<String, String> actorFieldTable = new HashMap<String, String>();
    private Map<String, String> personImgFieldTable = new HashMap<String, String>();
    private Map<String, String> contentCommentFieldTable = new HashMap<String, String>();
    private Map<String, String> rawWebRankingFieldTable = new HashMap<String, String>();
    private String downPoolName="/queue/proxy_pool";
    private TvMaoUrlCache cache = new TvMaoUrlCache();
	
    public void RegisterSelf(IBridge bridge) {
        this.bridge = bridge;
        bridge.RegisterModule("TvMao", this);         
        bridge.LogInfo("TvMao:RegisterSelf");       
        init(); 
    }
        
    private void init(){
    	 //raw_epg
        rawEpgFieldTable.put("EPG_ID", "uuid");    
        rawEpgFieldTable.put("NAME", "oriProgramName");   //  rawEpgFieldTable.put("NAME", "programName");
        rawEpgFieldTable.put("TYPE", "type");
        rawEpgFieldTable.put("ACTOR", "leadingRoles");
        rawEpgFieldTable.put("DIRECTOR", "directors");
        rawEpgFieldTable.put("PRESENTER", "presenters");
        rawEpgFieldTable.put("WRITER", "writers");
        rawEpgFieldTable.put("PRODUCER", "");
        rawEpgFieldTable.put("GUEST", "");
        rawEpgFieldTable.put("SESSION", "session");
        rawEpgFieldTable.put("EPISODE", "currentSet");
        rawEpgFieldTable.put("EPISODE_TOTAL", "totalSet");        
        rawEpgFieldTable.put("CHANNEL", "channelName");
        rawEpgFieldTable.put("AREA", "areaName");
        rawEpgFieldTable.put("BEGIN_TIME", "standardTime");
        rawEpgFieldTable.put("DESCRIPTION", "abs");     
        rawEpgFieldTable.put("MAIN_URL", "relUrl");  
        rawEpgFieldTable.put("CONTENT_ID", "contentUuid");  
        //raw_content
        rawContentFieldTable.put("CONTENT_ID", "contentUuid");    
        rawContentFieldTable.put("PARENT_ID", "");      
        rawContentFieldTable.put("SOURCE_ID", "sourceID");
        rawContentFieldTable.put("MAIN_URL", "programFirstPageAbsoluteUrl");
        rawContentFieldTable.put("NAME", "programName");
        rawContentFieldTable.put("ALIAS", "");
        rawContentFieldTable.put("TYPE", "type");
        rawContentFieldTable.put("TITLE", "titleName");
        rawContentFieldTable.put("TAG", "tags");
        rawContentFieldTable.put("ACTOR", "leadingRoles");
        rawContentFieldTable.put("DIRECTOR", "directors");
        rawContentFieldTable.put("PRESENTER", "presenters");
        rawContentFieldTable.put("WRITER", "writers");
        rawContentFieldTable.put("PRODUCER", "");        
        rawContentFieldTable.put("GUEST", "");
        rawContentFieldTable.put("SESSION", "session");
        rawContentFieldTable.put("EPISODE", "currentSet");
        rawContentFieldTable.put("EPISODE_TOTAL", "totalSet");            
        rawContentFieldTable.put("PUBLISH_NUM", "");
        rawContentFieldTable.put("DURATION", "duration");
        rawContentFieldTable.put("GRADE", "score");
        rawContentFieldTable.put("AIR_DATE", "airDate");       
      //  rawContentFieldTable.put("HEAT_DEGREE", "heatDegree");   
        rawContentFieldTable.put("LANGUAGE", "lang");
        rawContentFieldTable.put("SOURCE_QUALITY", "resolutionRatio");
        rawContentFieldTable.put("DESCRIPTION", "description");
        
        //episode_story_content
        rawEpisodeStoryFieldTable.put("CONTENT_ID", "contentUuid");    
        rawEpisodeStoryFieldTable.put("PARENT_ID", "parentId");      
        rawEpisodeStoryFieldTable.put("SOURCE_ID", "sourceId");
        rawEpisodeStoryFieldTable.put("MAIN_URL", "mainUrl");
        rawEpisodeStoryFieldTable.put("NAME", "programName");        
        rawEpisodeStoryFieldTable.put("TYPE", "type");
        rawEpisodeStoryFieldTable.put("TITLE", "titleName");      
        rawEpisodeStoryFieldTable.put("EPISODE", "episode");
        rawEpisodeStoryFieldTable.put("DESCRIPTION", "description");        
        rawEpisodeStoryFieldTable.put("GUEST", "guest");  
       
        //content_img
        contentImgFieldTable.put("CONTENT_IMG_ID", "contentImgId");    
        contentImgFieldTable.put("CONTENT_ID", "contentUuid");      
        contentImgFieldTable.put("IMG_URL", "imgUrl");       
        contentImgFieldTable.put("IMG_NAME", "imgName");        
        contentImgFieldTable.put("IMG_TYPE", "imgType");    
        
        //raw_person
        actorFieldTable.put("PERSON_ID", "personID");          
        actorFieldTable.put("SOURCE_ID", "sourceID");
        actorFieldTable.put("MAIN_URL", "mainUrl");
        actorFieldTable.put("NAME", "name");        
        actorFieldTable.put("ENGLISH_NAME", "englishName"); 
        actorFieldTable.put("ALIAS", "alias");      
        actorFieldTable.put("GENDER", "gender");
        actorFieldTable.put("DESCRIPTION", "description");        
        actorFieldTable.put("BIRTHDAY", "birthday");      
        actorFieldTable.put("COUNTRY", "country");    
        actorFieldTable.put("NATION", "nation");      
        actorFieldTable.put("PROFESSION", "profession");        
        actorFieldTable.put("CONSTELLATION", "constellation");
        actorFieldTable.put("BLOOD_TYPE", "bloodType");      
        actorFieldTable.put("BIRTHLAND", "birthLand");    
        actorFieldTable.put("WORKS", "works");      
        actorFieldTable.put("SCHOOL", "school");
        actorFieldTable.put("GRADE", "score");
        actorFieldTable.put("COMMENT", "comment");      
        actorFieldTable.put("MICRO_SLOG", ""); 
       
        //person_img
        personImgFieldTable.put("PERSON_IMG_ID", "personImgId"); 
        personImgFieldTable.put("PERSON_ID", "personId"); 
        personImgFieldTable.put("SOURCE_URL", "sourceUrl");
      //  personImgFieldTable.put("IMG_NAME", "imgName"); 
        personImgFieldTable.put("IMG_URL", "imgUrl"); 
        personImgFieldTable.put("IMG_TYPE", "imgType"); 
        personImgFieldTable.put("IMG_DESC", ""); 
        
        //cotent_comment
        contentCommentFieldTable.put("COMMENT_ID", "commentID"); 
        contentCommentFieldTable.put("UID", "uid"); 
        contentCommentFieldTable.put("NAME", "name");
        contentCommentFieldTable.put("CONTENT_ID", "contentID"); 
        contentCommentFieldTable.put("SOURCE", "source"); 
        contentCommentFieldTable.put("SOURCE_ID", "sourceID"); 
        contentCommentFieldTable.put("TITLE", "title");
        contentCommentFieldTable.put("CONTENT", "content"); 
        contentCommentFieldTable.put("PUB_TIME", "pubTime"); 
        
        //RAW_WEB_RANKING    
        rawWebRankingFieldTable.put("SOURCE_ID", "sourceID");
        rawWebRankingFieldTable.put("MAIN_URL", "programFirstPageAbsoluteUrl");
        rawWebRankingFieldTable.put("NAME", "programName");      
        rawWebRankingFieldTable.put("TYPE", "type");       
        rawWebRankingFieldTable.put("ACTOR", "leadingRoles");
        rawWebRankingFieldTable.put("DIRECTOR", "directors");
        rawWebRankingFieldTable.put("PRESENTER", "presenters");
        rawWebRankingFieldTable.put("WRITER", "writers");
        rawWebRankingFieldTable.put("SESSION", "session");
        rawWebRankingFieldTable.put("EPISODE", "currentSet");   
        rawWebRankingFieldTable.put("POINT", "heatDegree");       
        rawWebRankingFieldTable.put("DESCRIPTION", "description");
    }
    
    private Boolean checkInput(IMessage msg){
    	Boolean ret = true;
    	String html = msg.GetBody();
    	int size=0;
    	if (html != null){
    		size = html.length();
    	}    	
    	if (size<300 && msg.GetHeader("TaskType").indexOf("List")==-1){
    		String dispatchCount = msg.GetHeader("DispatchCount");
    		int nDispatchCount = 1;
    		try{
    			nDispatchCount = Integer.parseInt(dispatchCount);
    			if (nDispatchCount < 0){
    				nDispatchCount = 1;
    			}else{
    				nDispatchCount++;
    			}
    		}catch (Exception e){    			
    		}
    		bridge.LogInfo("[java] TvMao:HandleTask(warning)="+msg.GetHeader("TaskType")+ " v:" + msg.GetHeader("Version") 
    						+" url:"+msg.GetHeader("Url") +" proxy:"+msg.GetHeader("ProxyAddr") +  " body: "+ html + " nDispatchCount:"+String.valueOf(nDispatchCount));      	
    		IMessage  newMsg =  this.bridge.CreateMsg();    		
    		int totalCnt = msg.HeaderSize();
    	    for (int i=0; i<totalCnt; i++){
    	    	String key = msg.GetHeaderName(i);
    	    	newMsg.SetHeader(key,  msg.GetHeader(key));     
    	    }
    	    newMsg.SetHeader("DispatchCount", String.valueOf(nDispatchCount));   
    	    newMsg.SetHeader("destination", downPoolName);   
    	    newMsg.SetHeader("MsgType", "10005");   
    	    newMsg.SetHeader("NextTaskType", msg.GetHeader("TaskType"));   
         	sendMsg(msg);          	
         	ret = false;
    	}else{
    		if (msg.GetHeader("TaskType").indexOf("List")==-1){
    			//下载类
	    		if (size < 1000){
	    			bridge.LogInfo("[java] TvMao:HandleTask="+msg.GetHeader("TaskType")+" url:"+msg.GetHeader("Url") +" proxy:"+msg.GetHeader("ProxyAddr")+ " v:" + msg.GetHeader("Version") + " s:"+ size +" c:"+html); 
	    		}else{
	    			bridge.LogInfo("[java] TvMao:HandleTask="+msg.GetHeader("TaskType")+" url:"+msg.GetHeader("Url")+ " v:" + msg.GetHeader("Version") + " s:"+ size); 
	    			//add url_tasktype cache
	    			if (cache!=null){
	    				cache.add(msg.GetHeader("Url") + msg.GetHeader("TaskType") );
	    			}
	    		}	    		
    		}
    	}
    	return ret;
    }
    public void HandleTask(IMessage msg) {        
    	if (checkInput( msg) == false){
    		return;
    	}    	
    	if (msg.GetHeader("TaskType").equals("ListChannelByMannal")) {    		
			DoMsg_ListChannelByMannal(msg);
		}else	if (msg.GetHeader("TaskType").equals("ListCCTV")) {			
			DoMsg_ListCCTV(msg);
		} else if (msg.GetHeader("TaskType").equals("ListSatelliteTV")) {			
			DoMsg_ListSatelliteTV(msg);
		} else if (msg.GetHeader("TaskType").equals("GetSatelliteChannel")) {
			DoMsg_GetSatelliteChannel(msg);		
		} else if (msg.GetHeader("TaskType").equals("ListRoot")) {		
			DoMsg_ListRoot(msg);
		} else if (msg.GetHeader("TaskType").equals("GetAreaPage")) {
			DoMsg_GetAreaPage(msg);
		} else if (msg.GetHeader("TaskType").equals("GetChannelGroupPage")) {
    		 DoMsg_GetChannelGroupPage(msg);
    	}else if (msg.GetHeader("TaskType").equals("GetChannelPage")) {
			 DoMsg_GetChannelPage( msg);
		}else if (msg.GetHeader("TaskType").equals("GetTimeUrlPage")) {
			DoMsg_GetTimeUrlPage( msg);
		}else if (msg.GetHeader("TaskType").equals("GetEPGInfo")) {
			DoMsg_GetEpgInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetProgramFirstPageInfo")) {
			DoMsg_GetProgramFirstPageInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetDetailIntroduce")) {
			DoMsg_GetDetailPage( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetEpisodeStoryPageUrl")) {
			DoMsg_GetEpisodeStoryPageUrl( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetEpisodeStory")) {
			DoMsg_GetEpisodeStory( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetContentImgPageUrl")) {
			DoMsg_GetContentImgPageUrl( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetContentImg")) {
			DoMsg_GetContentImg( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetActorPageUrl")) {
			DoMsg_GetActorPageUrl( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetActorInfo")) {
			DoMsg_GetActorInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetActorWorks")) {
			DoMsg_GetActorWorks( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetActorImgPageUrl")) {
			DoMsg_GetActorImgPageUrl( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetActorImg")) {
			DoMsg_GetActorImg( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetReviewBaseInfo")){
			DoMsg_GetReviewBaseInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetReviewInfo")){
			DoMsg_GetReviewInfo( msg);			
		}else if (msg.GetHeader("TaskType").equals("GetTvColumnEpisodeStory")){
			DoMsg_GetTvColumnEpisodeStory( msg);			
		}
   }
    
    private void writeEpgContent(Object info,String version){
    	if (info instanceof TvMaoEpgFirstPageInfo) {
    		bridge.LogDebug("writeEpgContent(TvMaoEpgFirstPageInfo):"+info.toString());
    	}else{
    		bridge.LogDebug("writeEpgContent(TvMaoEpgInfo):"+info.toString());
    	}		
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "RAW_EPG");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(rawEpgFieldTable,strFields,info,outMsg);
		strFields += "SOURCE";
		outMsg.SetHeader("Field_SOURCE", "TVMAO");
		strFields += "/VERSION";
		outMsg.SetHeader("Field_VERSION", version);
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);
		// 如果是TvMaoEpgFirstPageInfo，还需要写content
		if (info instanceof TvMaoEpgFirstPageInfo) {
			if (((TvMaoEpgFirstPageInfo) info).getProgramFirstPageAbsoluteUrl() != null 
					&& ((TvMaoEpgFirstPageInfo) info).getProgramFirstPageAbsoluteUrl().isEmpty()==false ){
				IMessage msg2 = this.bridge.CreateMsg();
				msg2.SetHeader("destination", "/queue/db_operation");
				msg2.SetHeader("Table", "RAW_CONTENT");
				msg2.SetHeader("DB", "RAW_DB");
				msg2.SetHeader("MsgType", "40001");
				strFields = "";
				strFields = fillDbMsg(rawContentFieldTable,strFields,info, msg2);
				strFields += "SOURCE";
				msg2.SetHeader("Field_SOURCE", "TVMAO");
				msg2.SetHeader("Fields", strFields);				
				sendMsg(msg2);				
				//RAW_WEB_RANKING
				IMessage msgRanking = this.bridge.CreateMsg();
				msgRanking.SetHeader("destination", "/queue/db_operation");
				msgRanking.SetHeader("Table", "RAW_WEB_RANKING");
				msgRanking.SetHeader("DB", "RAW_DB");
				msgRanking.SetHeader("MsgType", "40001");
				strFields = "";
				strFields = fillDbMsg(rawWebRankingFieldTable,strFields,info, msgRanking);
				strFields += "SOURCE";
				msgRanking.SetHeader("Field_SOURCE", "TVMAO");
				strFields += "/VERSION";
				msgRanking.SetHeader("Field_VERSION", version);
				msgRanking.SetHeader("Fields", strFields);				
				sendMsg(msgRanking);
				//				
			}else{
				bridge.LogDebug("TvMaoEpgFirstPageInfo data err:"+info.toString());
			}
		}
	}   
    private void writeEpisodeStory(Object info){    	 
    	bridge.LogDebug("writeEpisodeStory:"+info.toString());     
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "RAW_CONTENT");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(rawEpisodeStoryFieldTable, strFields, info, outMsg);
		strFields += "SOURCE";
		outMsg.SetHeader("Field_SOURCE", "TVMAO");
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);	 
	}  
    
    private void writeReviewInfo(Object info){    	 
    	bridge.LogDebug("writeReviewInfo:"+info.toString());     
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "CONTENT_COMMENT");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(contentCommentFieldTable, strFields, info, outMsg);
	//	strFields += "SOURCE";
		//outMsg.SetHeader("Field_SOURCE", "TVMAO");
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);	 
	}   
    private void writeContentImg(Object info){    	 
    	bridge.LogDebug("writeContentImg:"+info.toString());     
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "CONTENT_IMG");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(contentImgFieldTable, strFields, info, outMsg);
	//	strFields += "SOURCE";
		//outMsg.SetHeader("Field_SOURCE", "TVMAO");
		if (strFields.length()>0){
			char x =strFields.charAt(strFields.length()-1);
			if (x=='/'){
				strFields = strFields.substring(0,strFields.length()-1);
			}		
		}
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);
	} 
    
    private void writeActorImg(Object info){    	 
    	bridge.LogDebug("writeActorImg:"+info.toString());     
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "PERSON_IMG");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(personImgFieldTable, strFields, info, outMsg);
	//	strFields += "SOURCE";
		//outMsg.SetHeader("Field_SOURCE", "TVMAO");
		if (strFields.length()>0){
			char x =strFields.charAt(strFields.length()-1);
			if (x=='/'){
				strFields = strFields.substring(0,strFields.length()-1);
			}		
		}
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);
	} 
    
    private void writeActorInfo(Object info){    	 
    	bridge.LogDebug("writeActorInfo:"+info.toString());     
		IMessage outMsg = this.bridge.CreateMsg();
		outMsg.SetHeader("destination", "/queue/db_operation");
		outMsg.SetHeader("Table", "RAW_PERSON");
		outMsg.SetHeader("DB", "RAW_DB");
		outMsg.SetHeader("MsgType", "40001");
		String strFields = "";
		strFields = fillDbMsg(actorFieldTable, strFields, info, outMsg);
		strFields += "SOURCE";
		outMsg.SetHeader("Field_SOURCE", "TVMAO");
		if (strFields.length()>0){
			char x =strFields.charAt(strFields.length()-1);
			if (x=='/'){
				strFields = strFields.substring(0,strFields.length()-1);
			}		
		}
		outMsg.SetHeader("Fields", strFields);				
		sendMsg(outMsg);
	} 
    
    private String fillDbMsg(Map<String, String> feildTable, String strFields, Object info, IMessage outMsg){
    	String strTmpDebug ="";
    	
    	int mapsize = feildTable.size();
		Iterator it = feildTable.entrySet().iterator();
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
					
					strTmpDebug += "Field_" + key + ":" + strTmpResult;
				}
			}				
		}
		
		if (info instanceof TvMaoEpgFirstPageInfo) {
    		bridge.LogDebug("fillDbMsg(TvMaoEpgFirstPageInfo):"+strFields + "  ::  "+ strTmpDebug);
    	}else{
    		bridge.LogDebug("fillDbMsg(TvMaoEpgInfo):"+strFields + "  ::  "+ strTmpDebug);
    	}
		return strFields;
    }
 
    private Boolean SerializeMsg(Object model, IMessage outMsg) {
		Boolean bRet = true;
		try {
			Field[] field = model.getClass().getDeclaredFields(); 
			if (model instanceof TvMaoEpgFirstPageInfo) {
				Field[] supperField = model.getClass().getSuperclass().getDeclaredFields();					
				Field[] bydataTmp = new Field[field.length + supperField.length];
				System.arraycopy(field, 0, bydataTmp, 0, field.length);
				System.arraycopy(supperField, 0, bydataTmp, field.length, supperField.length);
				field = bydataTmp;	
			}
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
				//outMsg.SetHeader(name, strValue);			
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
			if (model instanceof TvMaoEpgFirstPageInfo) {
				Field[] supperField = model.getClass().getSuperclass().getDeclaredFields();					
				Field[] bydataTmp = new Field[field.length + supperField.length];
				System.arraycopy(field, 0, bydataTmp, 0, field.length);
				System.arraycopy(supperField, 0, bydataTmp, field.length, supperField.length);
				field = bydataTmp;	
			}
			for (int j = 0; j < field.length; j++) { 
				String name = field[j].getName(); 
				String nameOri = name;
				name = name.substring(0, 1).toUpperCase() + name.substring(1); // 
				String type = field[j].getType().toString(); // 
			//	String strValue = inMsg.GetHeader(name);
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
    
	private void MyWait(int sleepMs){
    	try {
			Thread.sleep(sleepMs);
		} catch (InterruptedException e) {				
		}
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

	private IMessage CreateDownloadMsg(String nextTaskType, String url) {
		IMessage outMsg = this.bridge.CreateMsg();		
		//check url_tasktype cache
		if (cache != null){
			if (cache.containKey(url + nextTaskType)){
				bridge.LogDebug(url + nextTaskType + " in cache");
				outMsg.SetHeader("TvMaoUsingInner", "not_down");
			}
		}
		//		
		outMsg.SetHeader("destination", downPoolName);
		outMsg.SetHeader("Module", "TvMao");
		outMsg.SetHeader("NextTaskType", nextTaskType);
		outMsg.SetHeader("Url", url);
		outMsg.SetHeader("MsgType", "10005");
		return outMsg;
	}
	private void sendMsg(IMessage msg){
		//outMsg.SetHeader("TvMaoUsingInner", "not_down");
		String value = msg.GetHeader("TvMaoUsingInner");
		if (value == null || value.equals("")){
			bridge.SendMsg(msg);
		}
	}
	private void DoMsg_ListCCTV(IMessage msg){	       
		TvMaoAreaPageUrl areaPageUrl=new TvMaoAreaPageUrl ();
		areaPageUrl.setAbsoluteUrl("http://www.tvmao.com/program/CCTV");
		areaPageUrl.setAreaName("CCTV");
		IMessage outMsg = CreateDownloadMsg("GetChannelGroupPage", areaPageUrl.getAbsoluteUrl());
		if ( SerializeMsg(areaPageUrl, outMsg) ){			
			outMsg.SetHeader("onlyCurrent",  "true");		
			outMsg.SetHeader("Version", msg.GetHeader("Version"));
			sendMsg(outMsg);
		}else{
			bridge.LogDebug("TvMao_DoMsg_ListCCTV_SerializeMsg_ERR : " + areaPageUrl.toString());
		}
	}
	
	private void DoMsg_ListSatelliteTV(IMessage msg){	       
	   	IMessage outMsg = CreateDownloadMsg( "GetSatelliteChannel", "http://www.tvmao.com/program_satellite/AHTV1-w3.html");		 
		outMsg.SetHeader("SeedUrl",  "http://www.tvmao.com/program_satellite/AHTV1-w3.html");		
		outMsg.SetHeader("Version", msg.GetHeader("Version"));
	    sendMsg(outMsg);  
	}

	private void DoMsg_GetSatelliteChannel(IMessage msg) {
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoSatelliteChannelExtractor extractor = new TvMaoSatelliteChannelExtractor();
		List<TvMaoChannelPageUrl> channelPageList = new ArrayList<TvMaoChannelPageUrl>();
		if (extractor.getChannelPage(strHtml, strCharset,msg.GetHeader("SeedUrl"), channelPageList)) {
			for (TvMaoChannelPageUrl channelPageUrl : channelPageList) {
				bridge.LogInfo(channelPageUrl.getChannelName() +" : "+ channelPageUrl.getAbsoluteUrl());
				IMessage outMsg = CreateDownloadMsg("GetTimeUrlPage",	channelPageUrl.getAbsoluteUrl());
				if (SerializeMsg(channelPageUrl, outMsg)) {
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				} else {
					bridge.LogDebug("TvMao_DoMsg_GetSatelliteChannel_SerializeMsg_ERR : "	+ channelPageUrl.toString());
				}			
				MyWait(sleepMs);
				if (isBreak) break;
			}
		}
		if (channelPageList.size() == 0) {
			bridge.LogInfo("TVMao_DoMsg_GetSatelliteChannel(0warning) size:0 "+ msg.GetHeader("SeedUrl"));
		} else {
			bridge.LogInfo("TVMao_DoMsg_GetSatelliteChannel size:"+ channelPageList.size() + " " + msg.GetHeader("SeedUrl"));
		}
	}

	private void DoMsg_ListRoot(IMessage msg){	       
	   	IMessage outMsg = CreateDownloadMsg( "GetAreaPage", "http://www.tvmao.com/xiaoi/");		 
		outMsg.SetHeader("SeedUrl",  "http://www.tvmao.com/xiaoi/");		
		outMsg.SetHeader("Version", msg.GetHeader("Version"));
	    sendMsg(outMsg);  
	}
	
	private void DoMsg_GetAreaPage(IMessage msg){
	  String strHtml = msg.GetBody();
      String strCharset = "utf-8";
		TvMaoAreaPageExtractor extractor = new TvMaoAreaPageExtractor();
		List<TvMaoAreaPageUrl> areaPagelist = new ArrayList<TvMaoAreaPageUrl>();
		if (extractor.getAreaPage(strHtml, strCharset,	msg.GetHeader("SeedUrl"), areaPagelist)) {
			Iterator<TvMaoAreaPageUrl> itr = areaPagelist.iterator();
			while (itr.hasNext()) {
				TvMaoAreaPageUrl areaPageUrl = itr.next();
				IMessage outMsg = CreateDownloadMsg( "GetChannelGroupPage", areaPageUrl.getAbsoluteUrl());
				if ( SerializeMsg(areaPageUrl, outMsg) ){				
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{
					bridge.LogDebug("TvMao_DoMsg_GetAreaPage_SerializeMsg_ERR : " + areaPageUrl.toString());
				}				
				MyWait(sleepMs);			
				if (isBreak) break;	  
			}
		}
		if (areaPagelist.size() == 0){	
			 bridge.LogInfo("TVMao_DoMsg_GetAreaPage(0warning) size:0 " + 	msg.GetHeader("SeedUrl"));
		}else{
			bridge.LogInfo("TVMao_DoMsg_GetAreaPage size:" + areaPagelist.size()+" "+	msg.GetHeader("SeedUrl"));
		}	
	}
	
	private void DoMsg_GetChannelGroupPage(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		
		TvMaoAreaPageUrl areaPageUrl = new TvMaoAreaPageUrl();
		if ( UnSerializeMsg( msg,  areaPageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetChannelGroupPage_UnSerializeMsg_ERR : "+msg.toString());		
			return;
		}
		Boolean onlyCurrent = false;
		String stronlyCurrent = msg.GetHeader("onlyCurrent");
		if (stronlyCurrent !=null &&stronlyCurrent.equals("true")){
			onlyCurrent = true;
		}
		List<TvMaoChannelGroupPageUrl> channelGroupPageList = new ArrayList<TvMaoChannelGroupPageUrl>();
		TvMaoChannelGroupExtractor extrator = new TvMaoChannelGroupExtractor();
		if (extrator.getChannelGroupPage(strHtml, strCharset,areaPageUrl, channelGroupPageList, onlyCurrent)) {
			Iterator<TvMaoChannelGroupPageUrl> itr = channelGroupPageList.iterator();
			while (itr.hasNext()) {
				TvMaoChannelGroupPageUrl channelGroupPageUrl = itr.next();			
				IMessage outMsg = CreateDownloadMsg( "GetChannelPage", channelGroupPageUrl.getAbsoluteUrl());
				if ( SerializeMsg(channelGroupPageUrl, outMsg) ){		
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{
					bridge.LogDebug("TvMao_DoMsg_GetChannelGroupPage_SerializeMsg_ERR : " + channelGroupPageUrl.toString());
				}						 
				MyWait(sleepMs);			
				if (isBreak) break;	  	 
			}
		}		
		bridge.LogDebug("TVMao_DoMsg_GetChannelGroupPage size:" + channelGroupPageList.size() 
				+" "+areaPageUrl.getAbsoluteUrl()+" : "+channelGroupPageList.toString());	 
	}
	
	private void DoMsg_GetChannelPage(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoChannelGroupPageUrl channelGroupPageUrl = new TvMaoChannelGroupPageUrl();		
		if ( UnSerializeMsg( msg,  channelGroupPageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetChannelPage_UnSerializeMsg_ERR : "+msg.toString());		
			return;
		}		
		List<TvMaoChannelPageUrl> channelPageList = new ArrayList<TvMaoChannelPageUrl>();
		TvMaoChannelPageExtractor extractor = new TvMaoChannelPageExtractor();
		if (extractor.getChannelPage(strHtml, strCharset,channelGroupPageUrl, channelPageList)) {
			Iterator<TvMaoChannelPageUrl> itr = channelPageList.iterator();
			while (itr.hasNext()) {
				 TvMaoChannelPageUrl channelPageUrl = itr.next();			
				 if (new ChannelGenerator().IsWhite(channelPageUrl.getAbsoluteUrl())){					
				 }else if (new ChannelGenerator().IsBlack(channelPageUrl.getAbsoluteUrl())){
					 continue;
				 }
				 bridge.LogDebug("TvMao_DoMsg_GetChannelPage_doWith : " + channelPageUrl.toString());
				IMessage outMsg = CreateDownloadMsg( "GetTimeUrlPage", channelPageUrl.getAbsoluteUrl());
				if ( SerializeMsg(channelPageUrl, outMsg) ){		
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{
					bridge.LogDebug("TvMao_DoMsg_GetChannelPage_SerializeMsg_ERR : " + channelPageUrl.toString());
				}		
				MyWait(sleepMs);			
				if (isBreak) break;	  	 	 
			}
		}		
		if (channelPageList.size() == 0){
			bridge.LogDebug("TVMao_DoMsg_GetChannelPage(0warning) size: 0 " + channelGroupPageUrl.getAbsoluteUrl() );
		}else{
			bridge.LogDebug("TVMao_DoMsg_GetChannelPage size:" +channelPageList.size()+" "+ channelGroupPageUrl.getAbsoluteUrl());
		}
	}
	private void DoMsg_ListChannelByMannal(IMessage msg){		
		Boolean downArea=true,  importantSatellite=true,  otherSatellite=true,  downGAT=true;
		final int DOWN_AREA_MASK=1;
		final int IMPORTANT_SATELLITE_MASK=2;
		final int OTHER_SATELLITE_MASK=4;
		final int DOWN_GAT_MASK=8;		
		String valueTmp = msg.GetHeader("DownPartMask");
		bridge.LogInfo("TvMao_DoMsg_ListChannelByMannal DownPartMask : " + valueTmp);
		if (valueTmp!=null && valueTmp.isEmpty()==false){
			int mask = Integer.parseInt(valueTmp);		
			if ((mask & DOWN_AREA_MASK) != DOWN_AREA_MASK){
				downArea = false;
			}			
			if ((mask & IMPORTANT_SATELLITE_MASK) != IMPORTANT_SATELLITE_MASK){
				importantSatellite = false;
			}			
			if ((mask & OTHER_SATELLITE_MASK) != OTHER_SATELLITE_MASK){
				otherSatellite = false;
			}			
			if ((mask & DOWN_GAT_MASK) != DOWN_GAT_MASK){
				downGAT = false;
			}
			bridge.LogInfo("TvMao_DoMsg_ListChannelByMannal DownPartMask analyse result : " 
						+ " downArea : " +downArea
						+ " importantSatellite : " +importantSatellite
						+ " otherSatellite : " +otherSatellite
						+ " downGAT : " + downGAT
					);			
		}
				
		List<TvMaoChannelPageUrl> channelPageList = new ArrayList<TvMaoChannelPageUrl>();		
		new ChannelGenerator().Init(channelPageList, downArea,  importantSatellite,  otherSatellite,  downGAT);
		for (TvMaoChannelPageUrl channelPageUrl : channelPageList ){		 
			bridge.LogDebug("TvMao_DoMsg_ListChannelByMannal  : " + channelPageUrl.toString());
			IMessage outMsg = CreateDownloadMsg( "GetTimeUrlPage", channelPageUrl.getAbsoluteUrl());
			if ( SerializeMsg(channelPageUrl, outMsg) ){	
				outMsg.SetHeader("Version", msg.GetHeader("Version"));
				sendMsg(outMsg);
			}else{
				bridge.LogDebug("TvMao_DoMsg_ListChannelByMannal_SerializeMsg_ERR : " + channelPageUrl.toString());
			}
		}		
	}
	
	private void DoMsg_GetTimeUrlPage(IMessage msg){
		String strHtml = msg.GetBody();		
		if (strHtml.length() < 100)
			bridge.LogDebug("TvSou_DoMsg_GetTimeUrlPage_html : "+strHtml);				
		String strCharset = "utf-8";		 
		TvMaoChannelPageUrl channelPageUrl = new TvMaoChannelPageUrl();	
		if ( UnSerializeMsg( msg,  channelPageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetTimeUrlPage_UnSerializeMsg_ERR : "+msg.toString());		
			return;
		}
		
		List<TvMaoTimeUrl> timePageList = new ArrayList<TvMaoTimeUrl>();
		TvMaoTimePageExtractor extractor= new TvMaoTimePageExtractor();
		if (extractor.getTimePage(strHtml,strCharset ,channelPageUrl, timePageList)){
			Iterator<TvMaoTimeUrl> itr = timePageList.iterator();
			while (itr.hasNext()) {
				TvMaoTimeUrl timePageUrl = itr.next();				
				IMessage outMsg = CreateDownloadMsg( "GetEPGInfo", timePageUrl.getAbsoluteUrl());
				if ( SerializeMsg(timePageUrl, outMsg) ){				
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{
					bridge.LogDebug("TvMao_DoMsg_GetTimeUrlPage_SerializeMsg_ERR : " + timePageUrl.toString());
				}		
				MyWait(sleepMs);			
				if (isBreak) break;	  	 	
			}
		}		
		if (timePageList.size() == 0){
			bridge.LogDebug("TVMao_DoMsg_GetTimeUrlPage(0warning)  size: 0 " + channelPageUrl.getAbsoluteUrl() );
		}else{
			bridge.LogDebug("TVMao_DoMsg_GetTimeUrlPage  size:" +timePageList.size() +" "+ channelPageUrl.getAbsoluteUrl());		 
		}
	}
	
	private void DoMsg_GetEpgInfo(IMessage msg){
		String html = msg.GetBody();
		String charset = "utf-8";
		TvMaoTimeUrl timePageUrl = new TvMaoTimeUrl();				
		if ( UnSerializeMsg( msg,  timePageUrl) == false){					
			bridge.LogDebug("TvSou_DoMsg_GetEPGInfo_UnSerializeMsg_ERR : "+msg.toString());		
			return;
		}		
		List<TvMaoEpgInfo> EpgInfoList = new ArrayList<TvMaoEpgInfo>();
		TvMaoEpgExtractor extrator = new TvMaoEpgExtractor();
		if (extrator.getEpgInfo(html, charset, timePageUrl,	EpgInfoList)) {
			Iterator<TvMaoEpgInfo> itr = EpgInfoList.iterator();
			while (itr.hasNext()) {
				TvMaoEpgInfo EpgInfo = itr.next();	
				if (EpgInfo.getProgramFirstPageAbsoluteUrl() == null || EpgInfo.getProgramFirstPageAbsoluteUrl().equals("")){
					writeEpgContent(EpgInfo, msg.GetHeader("Version"));					
				}else{				
					IMessage outMsg = CreateDownloadMsg( "GetProgramFirstPageInfo", EpgInfo.getProgramFirstPageAbsoluteUrl());
					if ( SerializeMsg(EpgInfo, outMsg) ){	
						outMsg.SetHeader("Version", msg.GetHeader("Version"));
						sendMsg(outMsg);
					}else{
						bridge.LogInfo("TVMao_DoMsg_GetEPGInfo_SerializeMsg_ERR : "+EpgInfo.toString());
					}
				}
			}
		}		
		if (EpgInfoList.size() == 0){
			bridge.LogDebug("TVMao_DoMsg_GetEPGInfo(0warning)  size: 0 " + timePageUrl.getAbsoluteUrl() );
		}else{
			bridge.LogDebug("TVMao_DoMsg_GetEPGInfo  size:" + EpgInfoList.size() + " " + timePageUrl.getAbsoluteUrl());
		}
	}
	
	private void DoMsg_GetProgramFirstPageInfo(IMessage msg){
		String html = msg.GetBody();
		String charset = "utf-8";
		TvMaoEpgInfo epgInfo = new TvMaoEpgInfo();
		if ( UnSerializeMsg( msg,  epgInfo) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{
			List<TvMaoEpgFirstPageInfo> epgFirstPageList = new ArrayList<TvMaoEpgFirstPageInfo>();
			 List<TvMaoContentImg> thumbContentImgList = new ArrayList<TvMaoContentImg>();
			TvMaoEpgFirstPageInfoExtractor extractor = new TvMaoEpgFirstPageInfoExtractor();
			if ( extractor.getEpgFirstPageInfo(html,charset, epgInfo, epgFirstPageList, thumbContentImgList) ){
				//write contentImgList
				for (TvMaoContentImg thumbContentImg : thumbContentImgList){
					 writeContentImg(thumbContentImg);
				}
				//
				Iterator<TvMaoEpgFirstPageInfo> itr = epgFirstPageList.iterator();
				while (itr.hasNext()) {
					TvMaoEpgFirstPageInfo epgFirstPageInfo = itr.next();					
					Boolean succeedSend = false;
					//主content		 	
					String nextTaskUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, TvMaoEpgFirstPageInfoExtractor.TaskUriName.DETAIL);
					if (nextTaskUrl.equals("")==false){
						IMessage outMsg = CreateDownloadMsg( "GetDetailIntroduce", nextTaskUrl);
						if ( SerializeMsg(epgFirstPageInfo, outMsg) ){	
							outMsg.SetHeader("Version", msg.GetHeader("Version"));
							sendMsg(outMsg);
							succeedSend = true;
						}else{										
							bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_GetDetailIntroduceSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
						}
					}
					if (succeedSend == false){
						//演员 (基本信息)
						String parentActorUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
								TvMaoEpgFirstPageInfoExtractor.TaskUriName.ACTOR);
						if (parentActorUrl.equals("")==false){						
							IMessage outMsg = CreateDownloadMsg( "GetActorPageUrl", parentActorUrl);
							if ( SerializeMsg(epgFirstPageInfo, outMsg) ){				
								outMsg.SetHeader("parentUrlForactorPageUrl", parentActorUrl);	
								outMsg.SetHeader("typeForactorPageUrl", epgFirstPageInfo.getType());	
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);	
								succeedSend = true;
							}else{										
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_GetActorPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}												
						}					
					}					
					if (succeedSend == false){
						writeEpgContent(epgFirstPageInfo, msg.GetHeader("Version"));
					}
					
					//分级剧情（只电视剧有）
					if (epgFirstPageInfo.getType().equals("电视剧")){
						String nextDramaEpisodeStoryEnterUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
								TvMaoEpgFirstPageInfoExtractor.TaskUriName.DRAMA_EPISODE);
						if (nextDramaEpisodeStoryEnterUrl.equals("")==false){
							IMessage outMsg = CreateDownloadMsg( "GetEpisodeStoryPageUrl", nextDramaEpisodeStoryEnterUrl);
							if ( SerializeMsg(epgFirstPageInfo, outMsg) ){		
								outMsg.SetHeader("nextDramaEpisodeStoryEnterUrl", nextDramaEpisodeStoryEnterUrl);			
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);
							}else{							
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_GetEpisodeStoryPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}
						}
					}else if (epgFirstPageInfo.getType().equals("栏目")){//2012-10-24 add 
						String nextTvColumnEpisodeStoryEnterUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
								TvMaoEpgFirstPageInfoExtractor.TaskUriName.TVCOLUMN_EPISODE);
						if (nextTvColumnEpisodeStoryEnterUrl.equals("")==false){
							IMessage outMsg = CreateDownloadMsg( "GetEpisodeStoryPageUrl", nextTvColumnEpisodeStoryEnterUrl);
							if ( SerializeMsg(epgFirstPageInfo, outMsg) ){		
								outMsg.SetHeader("nextDramaEpisodeStoryEnterUrl", nextTvColumnEpisodeStoryEnterUrl);			
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);
							}else{							
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_GetEpisodeStoryPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}
						}
					}					
					//分级剧情 END					
					//剧照，海报（：海报缩略图；2：海报正常图片；3：剧照缩略图；4：剧照正常图；），栏目没有
					if (epgFirstPageInfo.getType().equals("电视剧")){
						//	DRAMA_PICTURE					
						String parentUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
									TvMaoEpgFirstPageInfoExtractor.TaskUriName.DRAMA_PICTURE);
						if (parentUrl.equals("")==false){
							IMessage outMsg = CreateDownloadMsg( "GetContentImgPageUrl", parentUrl);
							if ( SerializeMsg(epgFirstPageInfo, outMsg) ){		
								outMsg.SetHeader("nextContentImgParentUrl", parentUrl);	
								outMsg.SetHeader("fromType", "DRAMA_PICTURE");	
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);
							}else{							
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_getContentImgPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}
						}
					}else if  (epgFirstPageInfo.getType().equals("电影")){
						//MOVIE_PICTURE									
						String parentUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
									TvMaoEpgFirstPageInfoExtractor.TaskUriName.MOVIE_PICTURE);
						if (parentUrl.equals("")==false){
							IMessage outMsg = CreateDownloadMsg( "GetContentImgPageUrl", parentUrl);
							if ( SerializeMsg(epgFirstPageInfo, outMsg) ){		
								outMsg.SetHeader("nextContentImgParentUrl", parentUrl);	
								outMsg.SetHeader("fromType", "MOVIE_PICTURE");	
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);
							}else{							
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_getContentImgPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}
						}
						//MOVIE_POSTER （不用提取子连接）
						parentUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
								TvMaoEpgFirstPageInfoExtractor.TaskUriName.MOVIE_POSTER);
						if (parentUrl.equals("")==false){
							TvMaoContentImg contentImg = new TvMaoContentImg();
							contentImg.setMainUrl(parentUrl);
							contentImg.setFromType("MOVIE_POSTER");
							contentImg.setContentUuid(epgFirstPageInfo.getContentUuid());	
							IMessage outMsg = CreateDownloadMsg( "GetContentImg", parentUrl);
							if ( SerializeMsg(contentImg, outMsg) ){
								outMsg.SetHeader("Version", msg.GetHeader("Version"));
								sendMsg(outMsg);
							}else{							
								bridge.LogInfo("TVMao_DoMsg_GetProgramFirstPageInfo_getContentImgSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
							}
						}
					}
					//剧照，海报 END						
					//长评论
					String reviewUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
							TvMaoEpgFirstPageInfoExtractor.TaskUriName.REVIEW);
					if (reviewUrl.equals("")==false){
						IMessage outMsg = CreateDownloadMsg( "GetReviewBaseInfo", reviewUrl);			
						outMsg.SetHeader("refUrl", epgFirstPageInfo.getProgramFirstPageAbsoluteUrl());		
						outMsg.SetHeader("contentID", epgFirstPageInfo.getContentUuid());		
						outMsg.SetHeader("Version", msg.GetHeader("Version"));		
						sendMsg(outMsg);
					}
					//长评论END					
				}
			}else{
				writeEpgContent(epgInfo, msg.GetHeader("Version"));
			}		
		}
	}
	
	private void DoMsg_GetDetailPage(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoEpgFirstPageInfo epgFirstPageInfo =  new TvMaoEpgFirstPageInfo();
		if ( UnSerializeMsg( msg,  epgFirstPageInfo) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetDetailPage_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{
			TvMaoDetailPageExtractor extractor = new TvMaoDetailPageExtractor();
			if (extractor. getDetailIntro(strHtml, strCharset, epgFirstPageInfo) ){				
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetDetailIntroduce_Extractor_ERR : "+ msg.toString());			
			}
			 
			//演员 (基本信息)
			String parentActorUrl = TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(epgFirstPageInfo, 
					TvMaoEpgFirstPageInfoExtractor.TaskUriName.ACTOR);
			if (parentActorUrl.equals("")==false){						
				IMessage outMsg = CreateDownloadMsg( "GetActorPageUrl", parentActorUrl);
				if ( SerializeMsg(epgFirstPageInfo, outMsg) ){				
					outMsg.SetHeader("parentUrlForactorPageUrl", parentActorUrl);	
					outMsg.SetHeader("typeForactorPageUrl", epgFirstPageInfo.getType());			
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);					
				}else{		
					writeEpgContent(epgFirstPageInfo, msg.GetHeader("Version"));
					bridge.LogInfo("TVMao_DoMsg_GetDetailPage_GetActorPageUrlSerializeMsg_ERR : "+ epgFirstPageInfo.toString());
				}												
			}else{
				writeEpgContent(epgFirstPageInfo, msg.GetHeader("Version"));
			}
			//writeEpgContent(epgFirstPageInfo, msg.GetHeader("Version"));
		}
	}
	private void DoMsg_GetEpisodeStoryPageUrl(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		String parentUrl = msg.GetHeader("nextDramaEpisodeStoryEnterUrl");
		TvMaoEpgFirstPageInfo epgFirstPageInfo =  new TvMaoEpgFirstPageInfo();
		if ( UnSerializeMsg( msg,  epgFirstPageInfo) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetEpisodeStoryPageUrl_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoEpisodeStoryPageUrlExtractor extractor = new TvMaoEpisodeStoryPageUrlExtractor();
			 List<TvMaoEpisodeStory> episodeStoryPageUrlList = new ArrayList<TvMaoEpisodeStory>();
			if (extractor. getEpisodeStoryPageUrl(strHtml, strCharset,parentUrl, epgFirstPageInfo, episodeStoryPageUrlList) ){		
				for (TvMaoEpisodeStory item: episodeStoryPageUrlList){
					IMessage outMsg = CreateDownloadMsg( "GetEpisodeStory", item.getMainUrl());
					if ( SerializeMsg(item, outMsg) ){
						outMsg.SetHeader("Version", msg.GetHeader("Version"));
						sendMsg(outMsg);
					}else{							
						bridge.LogInfo("TVMao_DoMsg_GetEpisodeStoryPageUrl_GetEpisodeStorySerializeMsg_ERR : "+ item.toString());
					}
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetEpisodeStoryPageUrl_Extractor_ERR : "+ msg.toString());			
			}
			if (episodeStoryPageUrlList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetEpisodeStoryPageUrl(0warning)  size: 0 " +parentUrl );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetEpisodeStoryPageUrl  size:" + episodeStoryPageUrlList.size() + " " + parentUrl + episodeStoryPageUrlList.toString());
			}
		}	
	}
	
	private void DoMsg_GetEpisodeStory(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoEpisodeStory episodeStory = new TvMaoEpisodeStory();
		if ( UnSerializeMsg( msg,  episodeStory) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetEpisodeStory_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoEpisodeStoryExtractor extractor = new TvMaoEpisodeStoryExtractor();
			 List<TvMaoEpisodeStory> episodeStoryList = new ArrayList<TvMaoEpisodeStory>();
			if (extractor. getEpisodeStory(strHtml, strCharset, episodeStory, episodeStoryList) ){		
				for (TvMaoEpisodeStory item: episodeStoryList){
					if (item.getType().equals("栏目")){
						IMessage outMsg = CreateDownloadMsg( "GetTvColumnEpisodeStory", item.getMainUrl());
						if ( SerializeMsg(item, outMsg) ){
							outMsg.SetHeader("Version", msg.GetHeader("Version"));
							sendMsg(outMsg);
						}else{							
							bridge.LogInfo("TVMao_DoMsg_GetEpisodeStoryPageUrl_GetTvColumnEpisodeStorySerializeMsg_ERR : "+ item.toString());
						}
					}else{
						writeEpisodeStory(item);
					}
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetEpisodeStory_Extractor_ERR : "+ msg.toString());			
			}
			if (episodeStoryList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetEpisodeStory(0warning)  size: 0 " +episodeStory.getMainUrl() );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetEpisodeStory  size:" + episodeStoryList.size() + " " + episodeStory.getMainUrl()  + episodeStoryList.toString());
			}
		}	
	}

	private void DoMsg_GetTvColumnEpisodeStory(IMessage msg){
		String html = msg.GetBody();
		String charset = "utf-8";
		TvMaoEpisodeStory episodeStory = new TvMaoEpisodeStory();
		if ( UnSerializeMsg( msg,  episodeStory) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetTvColumnEpisodeStory_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{		
			TvMaoTvColumnEpisodeStoryExtractor extractor = new TvMaoTvColumnEpisodeStoryExtractor();
			List<TvMaoActor> guestList = new ArrayList<TvMaoActor> ();
			if ( extractor.getTvColumnEpisodeStory(html, charset, episodeStory, guestList)){
				 //episodeStory => raw-content
				writeEpisodeStory(episodeStory);
				//guestList -> actorinfo
				for (TvMaoActor item:guestList){
					if (item.getRefUrl()!=null && item.getRefUrl().isEmpty() == false){//for 有url的演员名称
						//基本信息
						IMessage outMsg = CreateDownloadMsg( "GetActorInfo", item.getRefUrl()+"/detail");
						if ( SerializeMsg(item, outMsg) ){
							outMsg.SetHeader("Version", msg.GetHeader("Version"));
							sendMsg(outMsg);
						}else{							
							bridge.LogInfo("TVMao_DoMsg_GetTvColumnEpisodeStorySerializeMsg_ERR : "+ item.toString());
						}	
					}
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetTvColumnEpisodeStory_Extractor_ERR : "+ episodeStory.toString());			
			}
		}	
	}
	
	private void DoMsg_GetContentImgPageUrl(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		String parentUrl = msg.GetHeader("nextContentImgParentUrl");
		String fromType = msg.GetHeader("fromType");
		TvMaoEpgFirstPageInfo epgFirstPageInfo =  new TvMaoEpgFirstPageInfo();
		if ( UnSerializeMsg( msg,  epgFirstPageInfo) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetContentImgPageUrl_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoContentImgPageUrlExtractor extractor = new TvMaoContentImgPageUrlExtractor();
			 List<TvMaoContentImg> contentImgList= new ArrayList<TvMaoContentImg>();
			if (extractor. getContentImgPageUrl(strHtml, strCharset,parentUrl,fromType, epgFirstPageInfo, contentImgList) ){		
				for (TvMaoContentImg item: contentImgList){
					IMessage outMsg = CreateDownloadMsg( "GetContentImg", item.getMainUrl());
					if ( SerializeMsg(item, outMsg) ){
						outMsg.SetHeader("Version", msg.GetHeader("Version"));
						sendMsg(outMsg);
					}else{							
						bridge.LogInfo("TVMao_DoMsg_GetContentImgSerializeMsg_ERR : "+ item.toString());
					}
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetContentImgPageUrl_Extractor_ERR : "+ parentUrl);			
			}
			if (contentImgList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetContentImgPageUrl(0warning)  size: 0 " +parentUrl );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetContentImgPageUrl  size:" + contentImgList.size() + " " + parentUrl + contentImgList.toString());
			}
		}	
	}
	
	private void DoMsg_GetContentImg(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoContentImg contentImg = new TvMaoContentImg();
		if ( UnSerializeMsg( msg,  contentImg) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetContentImg_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoContentImgExtractor extractor = new TvMaoContentImgExtractor();
			 List<TvMaoContentImg> contentImgList = new ArrayList<TvMaoContentImg>();		
			if (extractor. getContentImg(strHtml, strCharset, contentImg, contentImgList) ){		
				for (TvMaoContentImg item: contentImgList){
					 writeContentImg(item);
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetContentImg_Extractor_ERR : "+ msg.toString());			
			}
			if (contentImgList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetContentImg(0warning)  size: 0 " +contentImg.getMainUrl() );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetContentImg  size:" + contentImgList.size() + " " + contentImg.getMainUrl()  + contentImgList.toString());
			}
		}	
	}
	
	private void DoMsg_GetActorPageUrl(IMessage msg){		
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		TvMaoEpgFirstPageInfo epgFirstPageInfo =  new TvMaoEpgFirstPageInfo();
		if ( UnSerializeMsg( msg,  epgFirstPageInfo) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetActorPageUrl_UnSerializeMsg_ERR : "+ msg.toString());	
			return;
		}
		String parentUrl = msg.GetHeader("parentUrlForactorPageUrl");
		String type = msg.GetHeader("typeForactorPageUrl");
		TvMaoActorPageUrlExtractor extractor = new TvMaoActorPageUrlExtractor();
		List<TvMaoActor> acotrList= new ArrayList<TvMaoActor>();
		if (extractor. getActorPageUrl(strHtml, strCharset,parentUrl, type,  acotrList, epgFirstPageInfo) ){		
			for (TvMaoActor item: acotrList){
				if (item.getRefUrl()!=null && item.getRefUrl().isEmpty() == false){//for 有url的演员名称
					//基本信息
					IMessage outMsg = CreateDownloadMsg( "GetActorInfo", item.getRefUrl()+"/detail");
					if ( SerializeMsg(item, outMsg) ){
						outMsg.SetHeader("Version", msg.GetHeader("Version"));
						sendMsg(outMsg);
					}else{							
						bridge.LogInfo("TVMao_DoMsg_GetActorPageUrlSerializeMsg_ERR : "+ item.toString());
					}	
				}//else{
				//	writeActorInfo(item);				 
			//	}
			}
		}else{
			bridge.LogInfo("TVMao_DoMsg_GetActorPageUrl_Extractor_ERR : "+ parentUrl);			
		}
		writeEpgContent(epgFirstPageInfo, msg.GetHeader("Version"));
		if (acotrList.size() == 0){
			bridge.LogDebug("TVMao_DoMsg_GetActorPageUrl(0warning)  size: 0 " +parentUrl );
		}else{
			bridge.LogDebug("TVMao_DoMsg_GetActorPageUrl  size:" + acotrList.size() + " " + parentUrl + acotrList.toString());
		}		 
	}
	private void DoMsg_GetActorInfo(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoActor actor = new TvMaoActor();
		if ( UnSerializeMsg( msg,  actor) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetActorInfo_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoActorExtractor extractor = new TvMaoActorExtractor();		
			TvMaoPersonImg personImg=new TvMaoPersonImg();
			if (extractor. getActorInfo(strHtml, strCharset, actor, personImg) ){				
				IMessage outMsg = CreateDownloadMsg( "GetActorWorks", actor.getRefUrl()+"/telemovie");
				if ( SerializeMsg(actor, outMsg) ){
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{							
					bridge.LogInfo("TVMao_DoMsg_GetActorInfoSerializeMsg_ERR : "+ actor.toString());
				}
				//照片
				IMessage outMsg2 = CreateDownloadMsg( "GetActorImgPageUrl", actor.getRefUrl()+"/pictures");
				if ( SerializeMsg(actor, outMsg2) ){
					outMsg2.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg2);
				}else{							
					bridge.LogInfo("TVMao_DoMsg_GetActorImgPageUrlSerializeMsg_ERR : "+ actor.toString());
				}
				//write personImg
				writeActorImg(personImg);
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetActorInfo_Extractor_ERR : "+ msg.toString());			
			}			 
			bridge.LogDebug("TVMao_DoMsg_GetActorInfo:" + actor.toString());			 
		}	
	}
	private void DoMsg_GetActorWorks(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoActor actor = new TvMaoActor();
		if ( UnSerializeMsg( msg,  actor) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetActorWorks_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoActorWorksExtrator extractor = new TvMaoActorWorksExtrator();			
			if (extractor. getActorWorks(strHtml, strCharset, actor) ){						 
				writeActorInfo(actor);				 
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetActorWorks_Extractor_ERR : "+ msg.toString());			
			}			 
			bridge.LogDebug("TVMao_DoMsg_GetActorWorks:" + actor.toString());			 
		}	
	}
	
	private void DoMsg_GetActorImgPageUrl(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";		
		 TvMaoActor actor = new TvMaoActor();		 
		if ( UnSerializeMsg( msg,  actor) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetActorImgPageUrl_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoActorImgPageUrlExtrator extractor = new TvMaoActorImgPageUrlExtrator();	
			List<TvMaoPersonImg> personImgList  =new ArrayList<TvMaoPersonImg> ();
			if (extractor. getActorImgPageUrl(strHtml, strCharset, actor, personImgList) ){		
				for (TvMaoPersonImg item: personImgList){
					IMessage outMsg = CreateDownloadMsg( "GetActorImg", item.getSourceUrl());
					if ( SerializeMsg(item, outMsg) ){
						outMsg.SetHeader("Version", msg.GetHeader("Version"));
						sendMsg(outMsg);
					}else{							
						bridge.LogInfo("TVMao_DoMsg_GetActorImgPageUrlSerializeMsg_ERR : "+ item.toString());
					}
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetActorImgPageUrl_Extractor_ERR : "+ actor.getRefUrl());			
			}
			if (personImgList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetActorImgPageUrl(0warning)  size: 0 " +actor.getRefUrl() );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetActorImgPageUrl size:" + personImgList.size() + " " + actor.getRefUrl() + personImgList.toString());
			}
		}	
	}
	private void DoMsg_GetActorImg(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";
		TvMaoPersonImg personImg = new TvMaoPersonImg();
		if ( UnSerializeMsg( msg,  personImg) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetActorImg_UnSerializeMsg_ERR : "+ msg.toString());			
		}else{			
			TvMaoActorImgExtrator extractor = new TvMaoActorImgExtrator();
			List<TvMaoPersonImg> personImgList  =new ArrayList<TvMaoPersonImg> ();
			if (extractor. getActorImg(strHtml, strCharset, personImg, personImgList) ){		
				for (TvMaoPersonImg item: personImgList){
					 writeActorImg(item);
				}
			}else{
				bridge.LogInfo("TVMao_DoMsg_GetActorImg_Extractor_ERR : "+ msg.toString());			
			}
			if (personImgList.size() == 0){
				bridge.LogDebug("TVMao_DoMsg_GetActorImg(0warning)  size: 0 " +personImgList.size() );
			}else{
				bridge.LogDebug("TVMao_DoMsg_GetActorImg  size:" + personImgList.size() + " " + personImg.getSourceUrl()  + personImgList.toString());
			}
		}	
	}
	
	private void DoMsg_GetReviewBaseInfo(IMessage msg){
		String strHtml = msg.GetBody();
		String strCharset = "utf-8";			
		 String refUrl=msg.GetHeader("refUrl");	 
		 String contentID=msg.GetHeader("contentID");
		 List<TvMaoReviewInfo> reviewList = new ArrayList<TvMaoReviewInfo>();
		if (new TvMaoReviewBaseExtractor(). getReviewBaseInfo(strHtml, strCharset, refUrl,contentID, reviewList) ){		
			for (TvMaoReviewInfo item: reviewList){
				IMessage outMsg = CreateDownloadMsg( "GetReviewInfo", item.getMailUrl());
				if ( SerializeMsg(item, outMsg) ){
					outMsg.SetHeader("Version", msg.GetHeader("Version"));
					sendMsg(outMsg);
				}else{							
					bridge.LogInfo("TVMao_DoMsg_GetReviewBaseInfoSerializeMsg_ERR : "+ item.toString());
				}
			}
		}else{
			bridge.LogInfo("TVMao_DoMsg_GetReviewBaseInfo_Extractor_ERR : "+ msg.toString());			
		}
		if (reviewList.size() == 0){
			bridge.LogDebug("TVMao_DoMsg_GetReviewBaseInfo(0warning)  size: 0 " +reviewList.size() );
		}else{
			bridge.LogDebug("TVMao_DoMsg_GetReviewBaseInfo  size:" + reviewList.size() + " " +refUrl + reviewList.toString());
		}
	 
	}
	
	private void DoMsg_GetReviewInfo(IMessage msg){
		String html = msg.GetBody();
		String charset = "utf-8";			
		TvMaoReviewInfo item = new TvMaoReviewInfo();
		if ( UnSerializeMsg( msg,  item) == false){					
			bridge.LogInfo("TVMao_DoMsg_GetReviewInfo_UnSerializeMsg_ERR : "+ msg.toString());		
			return;
		}	 
		if (new TvMaoReviewExtractor(). getReviewInfo(html, charset, item) ){
			writeReviewInfo(item);		 
		}else{
			bridge.LogInfo("TVMao_DoMsg_GetReviewInfo_Extractor_ERR : "+ msg.toString());			
		}
	}
	
    
}
