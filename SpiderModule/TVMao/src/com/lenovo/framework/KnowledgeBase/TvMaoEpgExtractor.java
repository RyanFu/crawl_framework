package com.lenovo.framework.KnowledgeBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.io.UnsupportedEncodingException; 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoTimeUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoEpgExtractor extends  TvMaoBaseExtractor{		
	 
	public Boolean getEpgInfo(String strHtml, String strCharset,TvMaoTimeUrl timePageUrl, List<TvMaoEpgInfo> EpgInfos) {
		EpgInfos.clear();
		Boolean bRet = true;
		Map<String, TvMaoEpgInfo> mapProgram2Type = new HashMap<String, TvMaoEpgInfo>();
		Document doc = Jsoup.parse(strHtml);
		String strYmdTime = "";
		try {
			Element ymdEL = doc.select("div.page-content div.pgmain div.mt10 b").first();
			if (ymdEL != null) {
				strYmdTime = ymdEL.ownText().trim();
				String[] infos = strYmdTime.split(" ");
				if (infos.length == 2){
					strYmdTime=infos[0];
				}
			}
		} catch (Exception e) {			
		}		
		Elements rows = doc.select("body>div.clear>div.pgmain ul#pgrow>li");
		for (Element row : rows) {
			TvMaoEpgInfo item = new TvMaoEpgInfo();
			//oriProgramName		
			{
				String oriProgramNameTmp = "";		 
				List<Node> nodes = row.childNodes();
				for (Node onlyNextEL : nodes){				 
					if (onlyNextEL!=null){							
						if (onlyNextEL.nodeName().equals("#text")){						
							oriProgramNameTmp += onlyNextEL.outerHtml();		
						}else if (onlyNextEL.nodeName().equals("a")){				
							List<Node> secChilds = onlyNextEL.childNodes();
							for (Node secChild : secChilds){
								if (secChild.nodeName().equals("#text")){			
									if (secChild.outerHtml().indexOf("剧情" ) !=-1 
											|| secChild.outerHtml().indexOf("剧照" ) !=-1 
											|| secChild.outerHtml().indexOf("演员表" ) !=-1 
											|| secChild.outerHtml().indexOf("在线观看" ) !=-1 
											||secChild.outerHtml().indexOf("在线看" ) !=-1){
										break;
									}else{
										oriProgramNameTmp+= secChild.outerHtml();		
									}
								}
							}
						}else if (onlyNextEL.nodeName().equals("div")){				
							break;
						}
					}
				}
				//回看 直播中 直播				
				oriProgramNameTmp=oriProgramNameTmp.replaceAll("^\\s*直播中", "");
				oriProgramNameTmp=oriProgramNameTmp.replaceAll("^\\s*直播", "");
				oriProgramNameTmp=oriProgramNameTmp.replaceAll("^\\s*回看", "").trim();						
				item.setOriprogramName(oriProgramNameTmp);			
			}
			//
			item.setYmdTime ( strYmdTime);
			item.setLevel (timePageUrl.getLevel() + 1);
			item.setRelUrl ( timePageUrl.getAbsoluteUrl());
			item.setFindTime ( System.currentTimeMillis());
			Element spanEl = row.select("span").first();
			if (spanEl != null) {
				item.setStartTime ( spanEl.ownText());
			}
			String strOwnText = row.ownText().trim();		
			if (strOwnText.indexOf("回看") != -1) {
				item.setStatus ( "回看");
				strOwnText=strOwnText.replace("回看", "");
			} else if (strOwnText.indexOf("直播") != -1) {
				item.setStatus("直播");
				strOwnText=strOwnText.replace("直播", "");
			} else {
				item.setStatus("");
			}		
			if (strOwnText.indexOf("高清版") != -1) {
				item.setResolutionRatio ("2");
				strOwnText=strOwnText.replace("(高清版)", "").replace("高清版", "");
			}else{
				item.setResolutionRatio ("0");
			}
			strOwnText=strOwnText.trim();	
			item.setProgramName( strOwnText);
			if (strOwnText.indexOf("电视剧") != -1) {
				item.setType( "电视剧");
				Pattern p = Pattern.compile(":(.*?)(\\d+|\\d+/\\d+)$");
				Matcher matcher = p.matcher(strOwnText);
				if(matcher.find()){
					item.setProgramName( matcher.group(1));
					String strSetInfo = matcher.group(2);		
					String[] strPosInfos = strSetInfo.split("/");
					if (strPosInfos.length == 2){
						item.setCurrentSet( strPosInfos[0]);
						item.setTotalSet( strPosInfos[1]);
					}else if (strPosInfos.length == 1){
						item.setCurrentSet (strPosInfos[0]);		
						item.setTotalSet("");
					}else{
						item.setCurrentSet("");
						item.setTotalSet("");
					}						
				}
			} else if (strOwnText.indexOf("片:") != -1) {
				Pattern p = Pattern.compile("(\\S*?)\\s*(\\S*?):(.*?)(\\d*|\\d*/\\d*)$"); 
				Matcher matcher = p.matcher(strOwnText);
				if(matcher.find()){
					item.setType( matcher.group(2));
					item.setProgramName( matcher.group(3));
					String strSetInfo = matcher.group(4);		
					String[] strPosInfos = strSetInfo.split("/");
					if (strPosInfos.length == 2){
						item.setCurrentSet( strPosInfos[0]);
						item.setTotalSet( strPosInfos[1]);
					}else if (strPosInfos.length == 1){
						item.setCurrentSet (strPosInfos[0]);		
						item.setTotalSet("");
					}else{
						item.setCurrentSet("");
						item.setTotalSet("");
					}						
				}
			} else {
				item.setType( "栏目");
				Pattern p = Pattern.compile("(.*?)(\\d+|\\d+/\\d+|\\(\\d+\\)|\\(\\d+/\\d+\\))$");
				Matcher matcher = p.matcher(strOwnText);
				if(matcher.find()){
					item.setProgramName( matcher.group(1));
					String strSetInfo = matcher.group(2).replace("(", "").replace(")", "");		
					String[] strPosInfos = strSetInfo.split("/");
					if (strPosInfos.length == 2){
						item.setCurrentSet( strPosInfos[0]);
						item.setTotalSet( strPosInfos[1]);					 
					}else if (strPosInfos.length == 1){
						item.setCurrentSet (strPosInfos[0]);		
						item.setTotalSet("");
					}else{
						item.setCurrentSet("");
						item.setTotalSet("");
					}						
				}
			}
			//取abs
			Element absEl = row.select("div.tvgd p").first();
			if (absEl != null){
				item.setAbs(absEl.ownText());
			}			
			// 取全部a
			Elements anchors = row.select("a");
			for (Element anchor : anchors) {
				String strAnchorText = anchor.ownText();
				if (strAnchorText.indexOf("剧情") != -1) {
					item.setDramaUrl ( anchor.attr("href"));
					item.setDramaTitle ( anchor.attr("title"));
					item.setDramaAbsoluteUrl (  mergeUrl(item.getRelUrl() , item.getDramaUrl()));
					//修正
					item.setType( "电视剧");
				} else if (strAnchorText.indexOf("剧照") != -1) {
					item.setDramaPhotoUrl ( anchor.attr("href"));
					item.setDramaPhotoTitle ( anchor.attr("title"));
					item.setDramaPhotoAbsoluteUrl (  mergeUrl(item.getRelUrl() , item.getDramaPhotoUrl()));
					//修正
					item.setType( "电视剧");
				} else if (strAnchorText.indexOf("演员表") != -1) {
					item.setPlayerListUrl ( anchor.attr("href"));
					item.setPlayerListTitle ( anchor.attr("title"));
					item.setPlayerListAbsoluteUrl (  mergeUrl(item.getRelUrl() , item.getPlayerListUrl()));
				} else if (strAnchorText.indexOf("在线观看") !=-1 || strAnchorText.indexOf("在线看") != -1) {
					item.setPlayAddrUrl ( anchor.attr("href"));
					item.setPlayAddrTitle ( anchor.attr("title"));
					item.setPlayAddrAbsoluteUrl (  mergeUrl(item.getRelUrl() , item.getPlayAddrUrl()));
				} else if (strAnchorText.indexOf("集剧情") !=-1 || strAnchorText.indexOf("大结局") != -1) {
					
				} else {
					//12:45 百家讲坛 (唐宋八大家之苏轼(三))
					if (anchor.parent().tagName().equals("div")){
						continue;
					}
					// 剩下的就是节目信息
					item.setProgramFirstPageUrl ( anchor.attr("href"));
					item.setProgramName ( strAnchorText);
					item.setProgramFirstPageAbsoluteUrl (  mergeUrl(item.getRelUrl() , item.getProgramFirstPageUrl()));
					//patch (http://www.tvmao.com/xiaoi/program/CCTV-CCTV9-w3.html) 07:30
					Node onlyNextEL = anchor.nextSibling();
					if (onlyNextEL!=null){							
						if (onlyNextEL.nodeName().equals("#text")){						
							String strTmp = onlyNextEL.outerHtml().trim();						
							strTmp = strTmp.replaceAll("\\d{4}-\\d{2}-\\d{2}|\\d{2}/\\d{2}/\\d{4}|\\d{1,2}/\\d{1,2}/\\d{2}", "").replace("()", "").trim();		
							Pattern p = Pattern.compile("(第(.*?)季)");
							Matcher matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replaceAll("(第(.*?)季)", "").replace("(", "").replace(")", "");		
								item.setSession ( matcher.group(2));									 		
							}							
							//季
							//00:50 Fimbles (SR1) -Episode 13 剧照 演员表 
						//	strTmp = "Fimbles (SR 1) -Episode 13";
							p = Pattern.compile("(\\(|（)SR(\\d{1,2})(\\)|）)");
							matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replaceAll("(\\(|（)SR(\\d{1,2})(\\)|）)", "").trim();
								item.setSession ( matcher.group(2).trim());					
							}						
							//4、5、6、7     (4.5.6.7.8.9.10)  （13-16）  （）()  
							p = Pattern.compile("(.*?)(\\d+(、\\d+){1,}|\\d+(\\.\\d+){1,}|(\\d+-\\d+))");
							matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replace(matcher.group(2).trim(), "").replace("第集","").replace("（）","").replace("()", "").trim();
								item.setCurrentSet(matcher.group(2).trim());					
							}							
							p = Pattern.compile("\\s*S{1}\\s*?(\\d+)\\s+");
							matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replaceAll("\\s*S{1}\\s*?(\\d+)\\s+", "").trim();		
								item.setSession( matcher.group(1));									 		
							}										
							p = Pattern.compile("(\\d{1,3}-+\\d{1,3})");
							matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replaceAll("\\d{1,3}-+\\d{1,3}", "").replace("()", "").trim();		
								item.setCurrentSet ( matcher.group(1));									 		
							}							
							p = Pattern.compile("(\\d+|\\d+/\\d+|\\(\\d+\\)|\\(\\d+/\\d+\\))$");
							matcher = p.matcher(strTmp);
							if(matcher.find()){
								strTmp = strTmp.replaceAll("(\\d*|\\d*/\\d*|\\(\\d*\\)|\\(\\d*/\\d*\\))$", "");		
								String strSetInfo = matcher.group(1).replace("(", "").replace(")", "");		
								String[] strPosInfos = strSetInfo.split("/");
								if (strPosInfos.length == 2){
									item.setCurrentSet( strPosInfos[0]);
									item.setTotalSet( strPosInfos[1]);					 
								}else if (strPosInfos.length == 1){
									item.setCurrentSet (strPosInfos[0]);		
									item.setTotalSet("");
								}		
							}
							item.setProgramName( item.getProgramName()+strTmp);
						}
					}				
				}
			}
			//根据setProgramFirstPageAbsoluteUrl对type进行修正
			adjustType(item) ;
			adjustProgramName(item) ;			
			item.setDayName (timePageUrl.getDayName()); 
			item.setWeekName (timePageUrl.getWeekName());
			item.setChannelName (timePageUrl.getChannelName());
			item.setAreaName (timePageUrl.getAreaName());			
			item.setChannelGroupName(timePageUrl.getChannelGroupName());		
		
		//	item.setUuid (UUID.randomUUID().toString()); 
			String strYear = Integer.toString( Calendar.getInstance().get(Calendar.YEAR));
			item.setStandardTime ( strYear+"-"+item.getYmdTime() + " " +item.getStartTime()+":00");	
			String epgidSource = "TVMAO"+item.getOriprogramName() + item.getChannelName()+item.getStandardTime();
		//	item.setUuid (UUID.randomUUID().toString()); // `SOURCE`,`NAME`,`CHANNEL`,`BEGIN_TIME`) 
			item.setUuid(myUuid(epgidSource.getBytes()));		
			//下一步优化考虑 待验证
			if (item.getProgramFirstPageAbsoluteUrl() != null && item.getProgramFirstPageAbsoluteUrl().equals("")==false){
			//	item.setSourceID(getScoreId(item.getProgramFirstPageAbsoluteUrl()));
				String SourceIDTmp = getScoreId(item.getProgramFirstPageAbsoluteUrl());
				item.setContentUuid(myUuid(SourceIDTmp.getBytes()));		
			}
			//处理类型缓存（当前只处理电视剧，因为只有电视剧有同页上下关联）
			//14:30 亮剑(24) 剧情 剧照 演员表 在线观看
			//15:27 亮剑(25)			
			//TODO: 待加入详细信息处理 2012-10.24  只填入详细地址填入其中  Map<String, String> mapProgram2Type =》Map<String, CEPGInfo> mapProgram2Type 
			//需要传strProgramFirstPageAbsoluteUrl
			TvMaoEpgInfo cacheEpgInfo = mapProgram2Type.get(item.getProgramName());
			if (cacheEpgInfo == null){
				//处理
				//08:00 白天剧场:有爱就有家(10) 剧情 剧照 演员表 
				//09:00 白天剧场:有爱就有家(11)						
				String[] infos = item.getProgramName().split(":");
				if (infos.length > 0 &&  infos.length <= 2){
					for (int i=0;i<infos.length;i++){
						String strProgram = infos[i].trim();
						cacheEpgInfo = mapProgram2Type.get(strProgram);
						if (cacheEpgInfo == null){
							continue;
						}else{
							if (cacheEpgInfo.getProgramFirstPageAbsoluteUrl()!=null && cacheEpgInfo.getProgramFirstPageAbsoluteUrl().isEmpty()==false){
								item.setProgramName(strProgram);
								item.setType(cacheEpgInfo.getType());
								if (item.getContentUuid()==null || item.getContentUuid().isEmpty()){
									item.setContentUuid(cacheEpgInfo.getContentUuid());
								}						
								break;
							}
						}
					}
				} 				
				mapProgram2Type.put(item.getProgramName(), item);
			}else{
				if (cacheEpgInfo.getProgramFirstPageAbsoluteUrl()!=null && cacheEpgInfo.getProgramFirstPageAbsoluteUrl().isEmpty()==false){
					item.setType(cacheEpgInfo.getType());
					if (item.getContentUuid()==null || item.getContentUuid().isEmpty()){
						item.setContentUuid(cacheEpgInfo.getContentUuid());
					}				
				}
			}			
			
			if (item.getProgramName().equals("") || item.getProgramName().equals("午间节目") || item.getProgramName().equals("晚间节目")){				
			}else{	
				//下周星期六节目单
				Pattern p = Pattern.compile("星期.*节目单");
				Matcher matcher = p.matcher(item.getProgramName());
				if(matcher.find()==false){
					adjustCurrentSet(item);
					EpgInfos.add(item);
				}
			}
		}	 
		return bRet;
	}
	
	private void adjustCurrentSet(TvMaoEpgInfo item) {	
		//处理 00:25 旅行者12-82 剧照 演员表 
		if (!item.getCurrentSet().isEmpty()){
			String infos[] = item.getCurrentSet().split("-");
			if (infos.length ==2){
				try{
					int i0= Integer.parseInt(infos[0]);
					int i1= Integer.parseInt(infos[1]);
					if (i1-i0 >10){
						//连续10集 可能性不大 ，认为i0 季
						item.setSession(String.valueOf(i0));
						item.setCurrentSet(String.valueOf(i1));
					}
				}catch (NumberFormatException e){					
				}
			}else{
				try{
					int currentSet = Integer.parseInt(item.getCurrentSet());
					//set > totalset
					if (!item.getTotalSet().isEmpty()){
						int totalSet = Integer.parseInt(item.getTotalSet());						
						if (currentSet > totalSet){
							item.setCurrentSet(String.valueOf(totalSet));
							item.setTotalSet(String.valueOf(currentSet));
						}
					}
				}catch (NumberFormatException e){					
				}
			}
		}
	}
	
	private void adjustType(TvMaoEpgInfo item) {		
		/*EpgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/drama/Zy4qHTA=");//电视剧
		EpgInfo.setType("电视剧");
	//EpgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/movie/U1MwcA==");//电影
	//	EpgInfo.setType("电影");
		//EpgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/tvcolumn/M2tVVQ==");//栏目
	//	 EpgInfo.setType("栏目");*/
		String programFirstPageAbsoluteUrl = item.getProgramFirstPageAbsoluteUrl();
		if (programFirstPageAbsoluteUrl.indexOf("/drama/") !=-1){
			item.setType("电视剧");
		}else if (programFirstPageAbsoluteUrl.indexOf("/movie/") !=-1){
			item.setType("电影");
		}else if (programFirstPageAbsoluteUrl.indexOf("/tvcolumn/") !=-1){
			item.setType("栏目");
		}		
	}
	
	private void adjustProgramName(	TvMaoEpgInfo item) {		
		//大陸尋奇1478《普》 美國邊境保衛戰:打擊偷渡 《普》(按#切換5.1聲道)
			item.setProgramName ( item.getProgramName().replace("《普》", ""));
			//item.strProgramName ="fdas(直a）"; 
			item.setProgramName ( item.getProgramName().replaceAll("(\\(|（)(转播|重播|重|直播|直|首播|首|精選集|重播昨日|RP|普|付费|海外版|立体声|晚间版|高清版|重播版|日间版|双语|复播|重上周|雙語|雙語發音|录播)(\\)|）)", "").trim());		
			item.setProgramName ( item.getProgramName().replaceAll("(重播|重|直播|直|首播|首|精選集|重播昨日)$", "").trim());		 
			//处理   尋寶雙星4 -45 彈球熱  待验证  當舖之星7 -115 古怪科學
			Pattern p = Pattern.compile("(\\d+)\\s+-(\\d+)");
			Matcher matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(\\d+)\\s+-(\\d+)", "").replace("()", "").trim());		
				item.setSession( matcher.group(1).trim());		
				item.setCurrentSet (matcher.group(2).trim());
			}
			item.setProgramName ( item.getProgramName().replaceAll("(\\(完\\)|《護》|《普》|最後.{1,2}集|最后.{1,2}集|《普》|-|\\(.*?版\\)|\\(普\\)|\\(大結局\\)|\\(\\D+/\\D+\\))", "").replace("()", "").trim());//點滴是親情 34《普》
			
			//首播剧场43集电视剧许茂和他的女儿们(24) 
			p = Pattern.compile("(\\d+)集\\s{0,1}电视剧");
			matcher = p.matcher(item.getProgramName());
			if (matcher.find()) {
				String strProgramName = item.getProgramName();
				strProgramName = strProgramName.replaceAll( matcher.group(0), " ").replace("（）","").replace("()", "").trim();
				item.setProgramName ( strProgramName);				
				item.setTotalSet( matcher.group(1).trim());		
			}	
			
			//（上） 集 档案:百年公交(下)		
			p = Pattern.compile("(（|\\()([上中下]+)(）|\\))");
			matcher = p.matcher(item.getProgramName());
			if (matcher.find()) {
				item.setProgramName ( item.getProgramName().replaceAll("(（|\\()([上中下]+)(）|\\))", "").replace("（）","").replace("()", "").trim());				
					item.setCurrentSet ( matcher.group(2).trim());		
			}			
			 p = Pattern.compile("第(.*?)季");
			 matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("第(.*?)季", "").replace("()", "").trim());		
				item.setSession( matcher.group(1));									 		
			} else {
				p = Pattern.compile("第(.*?)部");
				matcher = p.matcher(item.getProgramName());
				if (matcher.find()) {
					item.setProgramName ( item.getProgramName().replaceAll("第(.*?)部", "").replace("()", "").trim());
					item.setSession( matcher.group(1));
				}
			}			
			p = Pattern.compile("第(.*?)集");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("第(.*?)集", "").replace("()", "").trim());		
				item.setCurrentSet( matcher.group(1));									 		
			}			
			p = Pattern.compile("第(.*?)輯");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("第(.*?)輯", "").replace("()", "").trim());		
				item.setCurrentSet ( matcher.group(1));									 		
			}	
			//08:25 电视剧(6集连播) 
			p = Pattern.compile("(\\(|（)(\\d{1,3}集.{1,2}播)(\\)|）)");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(\\(|（)(\\d{1,3}集.{1,2}播)(\\)|）)", "").replace("()", "").trim());		
				item.setCurrentSet ( matcher.group(1).trim());									 		
			}			
			//08:19 《雍正王朝》23集预告 
			p = Pattern.compile("(\\d{1,3})集");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(\\d{1,3})集", "").replace("()", "").trim());		
				item.setCurrentSet ( matcher.group(1).trim());								 		
			}			
			//处理  白雲階梯 (30) 22《普》  待验证
			p = Pattern.compile("\\((\\d+)\\)\\s*(\\d+)");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("\\((\\d+)\\)\\s*(\\d+)", "").replace("()", "").trim());		
				item.setTotalSet ( matcher.group(1).trim());				
				item.setCurrentSet ( matcher.group(2).trim());		
			}					
			//07:00 英國特警隊 4:7 《普》 
			p = Pattern.compile("\\s?(\\d+:\\d+)$");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("\\s?(\\d+:\\d+)$", "").trim());					
				String[] strPosInfos = matcher.group(1).trim().split(":");
				if (strPosInfos.length == 2){
					item.setSession( strPosInfos[0]);
					item.setCurrentSet( strPosInfos[1]);
				}	
			}	
			p = Pattern.compile("\\((S|SEASON)*?\\s*?(\\d+)\\)");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("\\((S|SEASON)*?\\s*?(\\d+)\\)", "").replace("()", "").trim());		
				if (item.getCurrentSet().equals("")){
					item.setCurrentSet ( matcher.group(2));						
				}else{
					item.setSession(matcher.group(2));	
				}
			}
			//集 set1/settotal
			p = Pattern.compile("(\\d+/\\d+|\\(\\d+\\)|\\(\\d+/\\d+\\))");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(\\d+/\\d+|\\(\\d+\\)|\\(\\d+/\\d+\\))", "").trim());		
				String strSetInfo = matcher.group(1).replace("(", "").replace(")", "").trim();		
				String[] strPosInfos = strSetInfo.split("/");
				if (strPosInfos.length == 2){
					item.setCurrentSet ( strPosInfos[0]);
					item.setTotalSet( strPosInfos[1]);
				}else if (strPosInfos.length == 1){
					item.setCurrentSet ( strPosInfos[0]);		
			//		item.strTotalSet ="";
				}		
			}
			//处理 #12
			p = Pattern.compile("#\\s*?(\\d{1,3})");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("#\\s*?(\\d{1,3})", " ").trim());		
				item.setCurrentSet(matcher.group(1));									 		
			}			
			//处理00:00 尋寶雙星4 -65 傑克歸來  http://www.tvmao.com/xiaoi/program/AETN-HISTORY-w9.html
			p = Pattern.compile("(-\\s*?)(\\d{1,3})(\\D*)");
			matcher = p.matcher(	item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(-\\s*?)(\\d{1,3})(\\D*)", matcher.group(1)+matcher.group(3)).trim());		
				item.setCurrentSet( matcher.group(2));									 		
			}				
			// 判断最后一个字符
			if (item.getProgramName().isEmpty() == false) {
				char lastChar = item.getProgramName().charAt(item.getProgramName().length() - 1);
				if (lastChar == ':' || lastChar=='-' ) {
					item.setProgramName ( item.getProgramName().substring(0,item.getProgramName().length() - 1));
				} else if (lastChar =='之') {
					item.setProgramName ( item.getProgramName().substring(0,item.getProgramName().length() - 1));
				}
			}
			
			p = Pattern.compile("\\D+(\\d{1,3})$");
			matcher = p.matcher(	item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll(matcher.group(1)+"$", "").trim());		
				if (item.getCurrentSet().equals("")){
					item.setCurrentSet ( matcher.group(1));
				}else if (item.getSession().equals("")){
					item.setSession ( matcher.group(1));
				}
			}			
			//   IIII5  康熙來了IV 全民最大黨II  V
			p = Pattern.compile("(\\(|（)\\s*([ⅠVIⅡi]{1,5})\\s*(\\)|）)");
			matcher = p.matcher(item.getProgramName());
			if(matcher.find()){
				item.setProgramName ( item.getProgramName().replaceAll("(\\(|（)\\s*([ⅠVIⅡi]{1,5})\\s*(\\)|）)", " ").trim());
				if (item.getCurrentSet().equals("")){				
					item.setCurrentSet ( matcher.group(2));
				}else if (item.getSession().equals("")){				
					item.setSession ( matcher.group(2));
				}	
			}else{
				p = Pattern.compile("[^a-zA-Z]+(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$");
				matcher = p.matcher(item.getProgramName());
				if(matcher.find()){
					item.setProgramName ( item.getProgramName().replaceAll("(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$", " ").replace("（）","").replace("()", "").trim());					
					if (item.getCurrentSet().equals("")){
						item.setCurrentSet ( matcher.group(1));
					}else if (item.getSession().equals("")){
						item.setSession ( matcher.group(1));
					}	
				}			 
			}
			//（一）  （二）
			//	日文(一)
			p = Pattern.compile("(（|\\()([一二三四五六七八九十零]+)(）|\\))");
			matcher = p.matcher(item.getProgramName());
			if (matcher.find()) {
				item.setProgramName ( item.getProgramName().replaceAll("(（|\\()([一二三四五六七八九十零]+)(）|\\))", "").replace("（）","").replace("()", "").trim());
				if (item.getCurrentSet().isEmpty()){
					item.setCurrentSet(matcher.group(2).trim());
				}else{
					item.setSession(matcher.group(2).trim());
				}
			}				
			//过滤关键字
			//	String [] blackKeywords = {"回看","直播中","电视剧","电影"};
			item.setProgramName ( item.getProgramName().replaceAll("^(回看|直播中|电视剧:|电影:)", "").trim()); //TODO: http://www.tvmao.com/xiaoi/program/HEFEITV-HEFEITV5-w1.html MV问题 
			item.setProgramName ( item.getProgramName().replaceAll("^(:|：)", "").trim());
			item.setProgramName ( item.getProgramName().replaceAll("(EPISODE|episode|Episode)$", "").trim());
			item.setProgramName ( item.getProgramName().replaceAll("(#|-|:|：|\\(.*?版\\)|\\(普\\))$", "").trim());		
			item.setProgramName ( item.getProgramName().replaceAll("(《|》)", " ").trim());					
		}
	
	public static void main(String[] args) {
		TvMaoEpgExtractor extractor = new TvMaoEpgExtractor();		
		TvMaoTimeUrl timePageUrl = new TvMaoTimeUrl();
		List<TvMaoEpgInfo> EpgInfos = new ArrayList<TvMaoEpgInfo>();
		String[] charset = new String[1];
	//	timePageUrl.setAbsoluteUrl( "http://www.tvmao.com/program/BTV-BTV4-w6.html");
		timePageUrl.setAbsoluteUrl( "http://www.tvmao.com/program/CCTV-CCTV10-w5.html");
		byte[] byHtml = extractor.download(timePageUrl.getAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 extractor.getEpgInfo(strHtml, charset[0], timePageUrl, EpgInfos);
	}
 
}
