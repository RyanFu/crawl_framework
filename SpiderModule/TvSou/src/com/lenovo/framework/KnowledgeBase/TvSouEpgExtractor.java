package com.lenovo.framework.KnowledgeBase;

import java.util.ArrayList;
 
import java.util.List;
 
import java.util.UUID;
import java.util.regex.*;
import java.io.UnsupportedEncodingException; 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
 
import org.jsoup.select.Elements;

 
 
import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouEpgInfo;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouEpgExtractor extends  TvSouBaseExtractor{
	
	public Boolean getEpgInfo(String html, String charset, TvSouChannelPageUrl channelPageUrl,	 List<TvSouEpgInfo> epgList) {
		Boolean bRet = true;		
		Document doc = Jsoup.parse(html);
		Elements rows = doc.select("div#con>div#con2>div[id^=PMT]");
		for (Element row : rows) {
			TvSouEpgInfo item = new TvSouEpgInfo();
			item.setUuid(UUID.randomUUID().toString());
			item.setYmdTime(channelPageUrl.getYearMonthDay());
			item.setLevel(channelPageUrl.getLevel() + 1);
			item.setRelUrl(channelPageUrl.getAbsoluteUrl());
			item.setFindTime(System.currentTimeMillis());
			item.setAreaName(channelPageUrl.getAreaName());
			item.setChannelName(channelPageUrl.getChannelName());
			item.setChannelGroupName(channelPageUrl.getChannelGroupName());
			item.setDayName(channelPageUrl.getWeekDay());
			//start time
			Element el = row.select("div#e1>b>font").first();
			if (el != null){
				item.setStartTime(el.ownText().trim());
				item.setStandardTime(item.getYmdTime() +" "+ item.getStartTime() + ":00");
			}			
			//Epg info
			doWithRowEpgItem(row, channelPageUrl, item);
			//extract type
			extractType(item);
			//extract 集  总集 季
			extractNumInfo(item);
			if (!item.getProgramName().equals("")){		
				epgList.add(item);
			}
		}	 
		return bRet;
	}	
	
	private void doWithRowEpgItem(Element row, TvSouChannelPageUrl channelPageUrl, TvSouEpgInfo item) {
		Element el = row.select("div#e2").first();
		if (el == null) return;
		
		Boolean bFindBR = false;
		Elements es = el.getAllElements();				
		for (Element e : es) {
			String tagName = e.tagName();			
			String ownText =e.ownText().trim();
		//	String outerHtml =e.outerHtml();
			//String text= e.text();
			//String html=e.html();
			if (tagName.equalsIgnoreCase("div")){
				if (!ownText.isEmpty()){
					if (bFindBR == false){
						item.setProgramName(ownText);
					}else{
						item.setDescription(ownText);
					}					
				}
			}else if (tagName.equalsIgnoreCase("a")){
				if (item.getProgramName().isEmpty()){
					item.setProgramName(e.ownText());
					item.setProgramFirstPageUrl(mergeUrl(item.getRelUrl(), e.attr("href")));
				}else{
					//剧情 http://jq.tvsou.com/introhtml/282/index_28297.htm 
					//第5集剧情http://jq.tvsou.com/introhtml/745/11_74576_1.htm
					// 3集视频点播 4集在线看   5集全集观看
					//http://tvsou.tv/htm/tvinfo_5011_4_1.htm
					//在线观看 视频点播 在线看  全集观看
					//http://www.tvsou.tv/htm/detail_11813.htm
					//http://www.tvsou.tv/htm/detail_30943.htm					
					if (ownText.indexOf("剧情") != -1) {
						item.setDramaTitle(ownText);
						item.setDramaUrl(e.attr("href"));
						item.setDramaAbsoluteUrl(mergeUrl(item.getRelUrl(), item.getDramaUrl()));
					} else if (ownText.indexOf("点播") != -1 || ownText.indexOf("看") != -1) {
						item.setPlayAddrTitle(ownText);
						item.setPlayAddrUrl(e.attr("href"));
						item.setPlayAddrAbsoluteUrl(mergeUrl(item.getRelUrl(), item.getPlayAddrUrl()));
					} 				
				}
			}else if (tagName.equalsIgnoreCase("br")){
				bFindBR = true;
			}
		}			
	}
	private void extractType(TvSouEpgInfo item){	
		//programName维度
		String programName = item.getProgramName();
		if (programName.indexOf("电视剧") != -1){
			item.setType("电视剧");
			Pattern p = Pattern.compile(".*电视剧:(.*?)$");
			Matcher matcher = p.matcher(programName);
			if(matcher.find()){
				programName = matcher.group(1).trim();							
			}
		}else if (programName.indexOf("片:") != -1){			
			Pattern p = Pattern.compile("(^[^:]{1,2}片):(.*?)$");
			Matcher matcher = p.matcher(programName);
			if(matcher.find()){
				item.setType(matcher.group(1).trim());
				programName = matcher.group(2).trim();				
			}
		}		
		//电视剧 other feature
		if (item.getType().isEmpty()){
			//20:47	战火西北狼25 第25集剧情
			//第48集剧情 48集视频点播
			//FIXME: 此方法是否能使用 ： 如果有 48集剧情
			Pattern p = Pattern.compile("\\d{1,3}集剧情");
			Matcher matcher = p.matcher(item.getDramaTitle());
			if(matcher.find()){
				item.setType("电视剧");			
			}
		}		
		//剧情  电影
		if (item.getType().isEmpty()){
			if (item.getDramaTitle()!=null && item.getDramaTitle().equals("剧情")){
				item.setType("电影");		
			}				
		}
		//兜底
		if (item.getType().isEmpty()){
			item.setType("栏目");
		}		
		//过滤
		programName = fliterProgramName(  programName);		
		item.setProgramName(programName);
	}
	private String fliterProgramName(String programName){
		programName = programName.replace("《普》", "");	
		programName = programName.replaceAll("(\\(|（)(普通话|复播|转播|重播|重|直播|直|首播|首|精選集|重播昨日|RP|普|付费|海外版|立体声|晚间版|高清版|重播版|日间版|双语|复播|重上周|雙語|雙語發音|录播)(\\)|）)", "").trim();		
		programName = programName.replaceAll("(复播|重播|重|直播|直|首播|首|精選集|重播昨日)$", "").trim();		
		return programName;
	}
	private void extractNumInfo(TvSouEpgInfo item){	 
		String programName = item.getProgramName();					 
		//滤掉时间		
		programName = programName.replaceAll("\\d{1,2}/\\d{1,2}/\\d{2,4}|\\d{2,4}/\\d{1,2}/\\d{1,2}|\\d{1,2}-\\d{1,2}-\\d{2,4}|\\d{2,4}-\\d{1,2}-\\d{1,2}", "").replace("()", "").trim();	
		//23:00		每日游报120925
		String  yearMonthDayOri = item.getYmdTime();
		yearMonthDayOri = yearMonthDayOri.replace("-", "").substring(2);
		programName = programName.replace(yearMonthDayOri, "").replace("（）","").replace("()", "").trim();		
	 		
		Pattern p = Pattern.compile("第(.*?)(季|部)");
		Matcher matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("第(.*?)(季|部)", "").replace("（）","").replace("()", "").trim();		
			item.setSession(matcher.group(1).trim());									 		
		} 		
		//4、5、6、7     (4.5.6.7.8.9.10)   
		p = Pattern.compile("(\\d+(、\\d+){1,}|\\d+(\\.\\d+){1,})");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\d+(、\\d+){1,}|\\d+(\\.\\d+){1,})", "").replace("（）","").replace("()", "").trim();
			item.setCurrentSet(matcher.group(1).trim());					
		}
		//网球王子(SEASON 4) -EPISODE 25 
		p = Pattern.compile("(SEASON|season)\\s*(\\d+)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(SEASON|season)\\s*(\\d+)", "").replace("（）","").replace("()", "").trim();		
			item.setSession(matcher.group(2).trim());		
		}else{
			//噬血真爱s 5:12
			p = Pattern.compile("(S|s)\\s?(\\d+)(:|：)(\\d+)$");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("(S|s)\\s?(\\d+)(:|：)(\\d+)$", "").replace("（）","").replace("()", "").trim();		
				item.setSession(matcher.group(2).trim());	
				item.setCurrentSet(matcher.group(4).trim());	
			}
		}
		//21:00		【欢乐八点档】加菲猫III #339-340 剧情		
		p = Pattern.compile("#(\\d+(-|―){1,2}\\d+)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("#(\\d+)(-|―){1,2}(\\d+)", "").replace("（）","").replace("()", "").trim();				
			item.setCurrentSet(matcher.group(1).trim());	
		}
		
		//grandpa in my pocket s2 #9    (s11) #29	.		
		p = Pattern.compile("[\\(（]?s(\\d+).*?#(\\d+)[\\)）]?", Pattern.CASE_INSENSITIVE);
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replace(matcher.group(0),"").trim();		
			item.setSession(matcher.group(1).trim());
			item.setCurrentSet(matcher.group(2).trim());
		}
		//集
		//09:20		上午剧:精彩电视剧（2集） 
		p = Pattern.compile("(\\(|（)\\s*(\\d+)(集|輯)(）|\\))");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\(|（)\\s*(\\d+)(集|輯)(）|\\))", "").replace("（）","").replace("()", "").trim();		
			item.setCurrentSet(matcher.group(2).trim());
		}		
		p = Pattern.compile("第(\\d+)(集|輯)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("第(\\d+)(集|輯)", "").replace("（）","").replace("()", "").trim();		
			item.setCurrentSet(matcher.group(1).trim());
		}else{
			//			10:45		师说 第07-007集
			p = Pattern.compile("第(\\d+)-(\\d+)(集|輯)");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("第(\\d+)-(\\d+)(集|輯)", "").replace("（）","").replace("()", "").trim();		
				item.setSession(matcher.group(1).trim());	
				item.setCurrentSet(matcher.group(2).trim());			
			}else{
				p = Pattern.compile("第(.*?)(集|輯)");
				matcher = p.matcher(programName);
				if(matcher.find()){
					programName = programName.replaceAll("第(.*?)(集|輯)", "").replace("（）","").replace("()", "").trim();				
					item.setCurrentSet(matcher.group(1).trim());
				}else{
					//07:53		隋唐英雄传（44——48） 第48集剧情
					p = Pattern.compile("(\\（|\\()(\\d+[―-]{1,3}\\d+)(\\）|\\))");
					matcher = p.matcher(programName);
					if(matcher.find()){
						programName = programName.replaceAll("(\\（|\\()(\\d+[―-]{1,3}\\d+)(\\）|\\))", "").replace("（）"," ").replace("()", "").trim();		
						item.setCurrentSet(matcher.group(2).trim());			
					}else{
						p = Pattern.compile("(\\d+)-(\\d+)$");
						matcher = p.matcher(programName);
						if(matcher.find()){
							programName = programName.replaceAll("(\\d+)-(\\d+)$", "").replace("（）"," ").replace("()", "").trim();		
							item.setSession(matcher.group(1).trim());			
							item.setCurrentSet(matcher.group(2).trim());			
						}
					}
				}				
			}
		}
		//19:20 中国京剧音配像精粹:折子戏选编(10)1 
		p = Pattern.compile("\\((\\d+)\\)\\s?(\\d+)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("\\((\\d+)\\)\\s?(\\d+)", "").replace("（）","").replace("()", "").trim();			
			item.setTotalSet(matcher.group(1).trim());	
			item.setCurrentSet(matcher.group(2).trim());
		}else{
			//14:40			凤凰典藏剧场:壹周．立波秀(1/6) 
			p = Pattern.compile("(\\d+)/(\\d+)");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("(\\d+)/(\\d+)", "").replace("（）","").replace("()", "").trim();		
				item.setCurrentSet(matcher.group(1).trim());
				item.setTotalSet(matcher.group(2).trim());					
			}
		}				
		//23:30		古井贡酒特约之锵锵三人行(3729)   K1（17）
		p = Pattern.compile("(\\(|（)(\\d+)(\\)|）)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\(|（)(\\d+)(\\)|）)", "").replace("（）","").replace("()", "").trim();		
			item.setCurrentSet(matcher.group(2).trim());
		}else{
		//2:00		primetime news episode 1 剧情
			p = Pattern.compile("-*(episode|EPISODE|#)\\s*?(\\d+)");//,  Pattern.CASE_INSENSITIVE);
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("-*(episode|EPISODE|#)\\s*?(\\d+)", "").replace("（）","").replace("()", "").trim();		
				item.setCurrentSet(matcher.group(2).trim());
			}
		}
		
		//   IIII5  康熙來了IV 全民最大黨II 真心英雄：法证先锋 ii 剧情	
		p = Pattern.compile("(\\(|（)(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})(\\)|）)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\(|（)(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})(\\)|）)", " ").replace("（）","").replace("()", "").trim();		
			item.setSession(matcher.group(2).trim());			
		}else{
			p = Pattern.compile("[^a-zA-Z]+(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$", " ").replace("（）","").replace("()", "").trim();		
				item.setSession(matcher.group(1).trim());			
			}
		}
		
		//  法证先锋 36  4位时 排除 19xx   20xx		
		if (item.getCurrentSet() == null || item.getCurrentSet().isEmpty()){
			p = Pattern.compile("\\D+(\\d{1,4})$");
			matcher = p.matcher(programName);
			if(matcher.find()){
				String result = matcher.group(1).trim();
				if (result.length() == 4 && (result.startsWith("19") || result.startsWith("20"))){							
				}else{
					programName = programName.replaceAll("(\\d{1,4})$", "").trim();			
					item.setCurrentSet(result);
				}
			}			
		}
		//上中下		
		p = Pattern.compile("(\\(|（)([上中下一二三四五六七八九十]+)(\\)|）)$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\(|（)([上中下一二三四五六七八九十]+)(\\)|）)$", "").replace("（）","").replace("()", "").trim();		
			item.setCurrentSet(item.getCurrentSet()==null ? matcher.group(2).trim() : item.getCurrentSet()+ matcher.group(2).trim());			
		}else{		
			p = Pattern.compile("(\\(|（)([上中下]?)(\\)|）)");
			matcher = p.matcher(programName);
			if(matcher.find()){			
				programName = programName.replaceAll("(\\(|（)([上中下]?)(\\)|）)", "").replace("（）","").replace("()", "").trim();		
				item.setCurrentSet(item.getCurrentSet()==null ? matcher.group(2).trim() : item.getCurrentSet()+ matcher.group(2).trim());			
			}
		}
		//2012西雅图DOTA2邀请赛（竞技天堂1958A） 			
		p = Pattern.compile("(\\(|（).*?(\\d+[a-zA-Z]?)\\s*(\\)|）)$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("(\\(|（).*?(\\d+[a-zA-Z]?)\\s*(\\)|）)$", "").replace("（）","").replace("()", "").trim();		
			if (isNumeric(matcher.group(2).trim())){
				if (item.getCurrentSet().equals("")){
					item.setCurrentSet(matcher.group(2).trim());	
				}else{
					item.setSession(matcher.group(2).trim());			
				}		
			}else{
				item.setCurrentSet(item.getCurrentSet()==null ? matcher.group(2).trim() : item.getCurrentSet()+ matcher.group(2).trim());	
			}
		}	
		//最后
		p = Pattern.compile("第(.{1,4})期");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll("第(.{1,4})期", "").replace("()", "").trim();		
			if (item.getCurrentSet().equals("")){
				item.setCurrentSet(matcher.group(1).trim());	
			}else{
				item.setSession(matcher.group(1).trim());			
			}									 		
		}		
		item.setProgramName(programName);
	}
	
	private boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {		
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		TvSouChannelPageUrl channelPageUrl =new TvSouChannelPageUrl();			 
		channelPageUrl.setUrl("/programys/TV_82/Channel_393/W5.htm");
		channelPageUrl.setRelUrl("http://epg.tvsou.com/program/TV_82/Channel_393/W5.htm");
		channelPageUrl.setAreaName("黑龙江");
		channelPageUrl.setChannelName("黑龙江电视台");
		channelPageUrl.setLevel(2);		 
		channelPageUrl.setFindTime(System.currentTimeMillis());	 
		channelPageUrl.setChannelGroupName("黑龙江电视台");
		channelPageUrl.setAbsoluteUrl("http://epg.tvsou.com/program/TV_300/Channel_1322/W6.htm");	
		//channelPageUrl.setAbsoluteUrl("http://epg.tvsou.com/programjw/TV_48/Channel_108/W3.htm");
		channelPageUrl.setWeekDay("5");
		channelPageUrl.setYearMonthDay("2012-09-24");		
		TvSouEpgExtractor extractor = new TvSouEpgExtractor();		 
		List<TvSouEpgInfo> EpgList = new ArrayList<TvSouEpgInfo>();		
		String[] charset = new String[1];		 
		byte[] byHtml = extractor.download(channelPageUrl.getAbsoluteUrl(), charset);
		String html = null;
		try {
			charset[0] = "gb2312";
			html = new String(byHtml,  charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		if (extractor.getEpgInfo(html, charset[0], channelPageUrl, EpgList)){
			for (TvSouEpgInfo item : EpgList){
				System.out.println(item.toString());
			}
		}
	}
}
