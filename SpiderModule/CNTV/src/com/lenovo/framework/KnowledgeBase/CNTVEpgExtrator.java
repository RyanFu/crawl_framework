package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CNTVEpgExtrator  extends  CNTVExtractBase{	
	
	public Boolean GetEPGInfo(String strHtml, String strCharset,CNTVChannelInfo channelInfo,String strEpgUrl, java.util.List<CNTVEpgInfo> EPGInfos) {			
		Boolean bRet = true;
		try{
			Document doc = Jsoup.parse(strHtml);
			Elements srcs = doc.select("div.content_c dd");
			for (Element src : srcs) {
				CNTVEpgInfo item = new CNTVEpgInfo();
				item.setStrUUID(UUID.randomUUID().toString()) ;
				item.setStrRelUrl(strEpgUrl);
				Elements childs = src.children();
				if (childs.size() == 0){
					List<Node> nodeList = src.childNodes();
					if (nodeList.size() ==1){
						if (nodeList.get(0).nodeName().equals("#text")){
							item.setStrProgramName(nodeList.get(0).outerHtml().trim());
						}
					}
				}else{
					for (Element child : childs) {
						if (childs.get(0).nodeName().equals("a")){
							item.setStrPlayAddrUrl(child.attr("href"));
							String strTmp = child.text().trim();
							if (strTmp.indexOf("回看") ==-1){// && strTmp.indexOf("直播")==-1){
								if (strTmp.equals("直播") == false){
									item.setStrProgramName(strTmp);
								}
							}
						}
					}
				}
				if (item.getStrProgramName().isEmpty() == false){
					item.setStrProgramName(item.getStrProgramName().replaceAll("(\\(|（)(转播|重播|重|直播|直|首播|首|精選集|重播昨日|RP|普|付费|海外版|立体声|晚间版|高清版|重播版|日间版|双语|复播|重上周|雙語|雙語發音|录播|3D)(\\)|）)", "").trim()) ;
					String strTmpName = item.getStrProgramName().replaceAll("(重播|重|直播|直|首播|首|精選集|重播昨日)$", "").trim();
					if (!strTmpName.isEmpty()){					
						Pattern p = Pattern.compile("\\d{2}:\\d{2}.+");
						Matcher matcher = p.matcher(strTmpName);
						if(matcher.find()){
							item.setStrProgramName(strTmpName) ;		
						}						
					}
					//抽取EPGInfo信息
					ExtratEpgInfo(item, channelInfo,strEpgUrl);
					item.setStrChannelName(channelInfo.getName());				
					EPGInfos.add(item);
				}
			}
		}catch(Exception e){
			bRet=false;
	//		e.printStackTrace();
		}
		return bRet;
	}
	
	private void ExtratEpgInfo(CNTVEpgInfo item,CNTVChannelInfo channelInfo,String strEpgUrl) {
		/*
		20:57 电视剧：前情提要《长白山下我的家》26/29
		11:08 电视剧：薛平贵与王宝钏46/48
		01:58 电视剧：安娜莉亚（第二部）12
		04:59 电视剧：天仙配32/35
		13:22 意难忘（第二季）45/50
		08:44 公共宣传时段1-1
		22:58 电视剧：三国演义第一部群雄逐鹿4.孟德献刀4/84 
		00:02 电视剧：三国演义第一部群雄逐鹿2.十常侍乱政2/84
		09:45 跟我学：裴永杰教京剧1
		07:36 九州大戏台：吕剧电影李二嫂改嫁2/2
		15:37 CCTV空中剧院：京剧现代戏智取威虎山（国家京剧院）2/2
		19:30 电视剧：家N次方21、22 
		00:10 电视剧：江南锄奸5、6、7 
		12:10 电视剧：决战南京4、5、6、7 
		00:41 电视剧:魔界之龙珠(32、33)
		09:01 电视剧：天堂秀（13-16）
		01:05 电视剧：五号特工组(4.5.6.7.8.9.10)
		08:00 电视剧：重案六组四(33.34.35.36)
		01:10 电视剧(法)：红衣坊（法）31/32
		22:00 晚间新闻
		19:55 身边的感动580
		04:30 译制片：白发魔女传2
		02:23 故事片：镖行天下之桃花劫
		09:29 光影星播客2
		09:36 电影报道
		10:30 动画大放映--动画连连看85
		04:30 亲情树（精编版）（阿）17/26
		10:00 综合新闻（阿）(直播)
		09:00 新闻直播间：焦点新闻播报
		00:15 面对面（重播）
		04:40 红军东征（高清版）12/30
		00:00 环球直播室(16/9/12) 
		10:05 鲁豫有约:说出你的故事(1894) 
		15:20 박민화의 행복한 오후 
		21:05 이밤을 함께 합니다 
		00:00 2012/2013西班牙足球甲级联赛-第4轮（奥萨苏纳-马洛卡）
		10:35 实况录像-2012年男篮亚洲杯小组赛中国队—黎巴嫩队（录播）
		08:33 2012年体坛风云会-林书豪
		12:30 现场直播:2012年男篮亚洲杯-小组赛（乌兹别克斯坦队-中国国奥队）
		21:00 足球之夜-2012中超战报-24 
		01:51 星光大道2/3
		06:56 暑期七天乐大讲堂3：2/3
		05:00 整点新闻(英)（直播）
		05:00 译制片：航海王：珍兽岛的乔巴王国		
		
		09:08 高三数学：函数与方程思想（二） 
		10:12 试卷分析课：初三政治：中考全解析（二）如何提高中考总复习的效率 
        13:00 寰宇视野：伟大的卫国战争Ⅱ6 
		*/
		String date="";
		Pattern pDate = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
		Matcher matcherDate = pDate.matcher(strEpgUrl);
		if(matcherDate.find()){
			date = matcherDate.group(1).trim();		
		}

		//String programName="00:27 电视剧：天涯明月刀21/40";
		String programName = item.getStrProgramName();
		Pattern p = Pattern.compile("^(\\d{2}:\\d{2})\\s+.*?");
		Matcher matcher = p.matcher(programName);
		if(matcher.find()){
			item.setStrStartTime(matcher.group(1).trim());
			item.setStrStandardTime(date + " " +  item.getStrStartTime() + ":00");
			programName = programName.replaceAll("^(\\d{2}:\\d{2})", "").trim();	
			item.setStrProgramName(programName);	 
		}else{
			 p = Pattern.compile("^(\\d{2}:\\d{2})");
			 matcher = p.matcher(programName);
			if(matcher.find()){
				item.setStrStartTime(matcher.group(1).trim());
				item.setStrStandardTime(date + " " +  item.getStrStartTime() + ":00");
				programName = programName.replaceAll("^(\\d{2}:\\d{2})", "").trim();	
				item.setStrProgramName(programName);	 
			}
		}
		//通过	programName获得 strtype strSession  strCurrentSet strTotalSet strProgramName
		if (programName.indexOf("电视剧") != -1) {
			item.setStrType("电视剧");			
			GetDramaInfo(item, programName);
		} else if (programName.indexOf("片:") != -1 || programName.indexOf("片：") != -1  || programName.indexOf("电影：") != -1) {
			item.setStrType("电影");			
			GetMovieInfo(item, programName);
		}else{
			item.setStrType("栏目");
			GetTVColumnInfo(item, programName);			
			//FIXME: currentset 和 totalset totalset>10非空时认为是电视剧
			if (item.getStrCurrentSet().isEmpty() == false && item.getStrTotalSet().isEmpty() == false){
				int nTotalSet = Integer.parseInt( item.getStrTotalSet());
				if (nTotalSet >10)
				item.setStrType("电视剧");
			}
		}			
		item.setStrProgramName(item.getStrProgramName().replaceAll("(\\(|（).?(\\)|）)$", "").trim()) ;	
	}

	private void GetDramaInfo(CNTVEpgInfo item, String programName) {
		String programNameTmp =programName.replaceAll("电视剧.*?(:|：)", "").trim();
		if (!programNameTmp.isEmpty())
			programName = programNameTmp;
		programName = programName.replaceAll("\\d{1,2}/\\d{1,2}/\\d{1,4}", "").replace("（）","").replace("()", "").trim();
		// 1/2   (1/2)  （1/2）
		Pattern p = Pattern.compile("(.*?)(\\d+/\\d+|\\(\\d+/\\d+\\)|（\\d+/\\d+）)$");
		Matcher matcher = p.matcher(programName);
		if(matcher.find()){
			programName = matcher.group(1).trim();
			String strSetInfo = matcher.group(2).replaceAll("（|）|\\(|\\)","").trim();
			String[] strPosInfos = strSetInfo.split("/");
			if (strPosInfos.length == 2){
				item.setStrCurrentSet(strPosInfos[0]);
				item.setStrTotalSet(strPosInfos[1]);
			}else{
				item.setStrCurrentSet("");
				item.setStrTotalSet("");
			}						
		}
		//4、5、6、7     (4.5.6.7.8.9.10)  （13-16）  （）()  
		p = Pattern.compile("(.*?)(\\d+(、\\d+){1,}|\\d+(\\.\\d+){1,}|(\\d+-\\d+))");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replace(matcher.group(2).trim(), "").replace("第集","").replace("（）","").replace("()", "").trim();
			item.setStrCurrentSet(matcher.group(2).trim());					
		}
		// (1922) （45）
		p = Pattern.compile("(.*?)(\\(|（)(\\d+)(\\)|）)");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(\\(|（)(\\d+)(\\)|）)", "").replace("（）","").replace("()", "").trim();
			item.setStrCurrentSet(matcher.group(3).trim());
		}
		
		// )12 -123 ）12  IIII5
		p = Pattern.compile("(.*?)(\\)|-|）|Ⅰ|Ⅱ|I{1,5})(\\d+)$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll(matcher.group(3).trim()+"$", "").trim();
			item.setStrCurrentSet(matcher.group(3).trim());					
		}		
				
		//（第二季）（第二部）
		p = Pattern.compile("第(.+)(部|季)");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replace(matcher.group(0), "").replace("（）","").replace("()", "").trim();
			item.setStrSession(matcher.group(1).trim());					
		}
		
		//（一）  （二）  （上）
		//	programName = "高一生物：细胞代谢串讲课（四二中）";
		p = Pattern.compile("(（|\\()([一二三四五六七八九十零]+)(）|\\))$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()([一二三四五六七八九十零]+)(）|\\))$", "").replace("（）","").replace("()", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}else{
				item.setStrSession(matcher.group(2).trim());
			}
		}	

		//（上） 集
		p = Pattern.compile("(（|\\()([上中下]+)(）|\\))$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()([上中下]+)(）|\\))$", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}
		}
		//回看 00:31 电视剧：螳螂（27上）
		p = Pattern.compile("(（|\\()(\\d+\\s?[上中下]+)(）|\\))$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()(\\d+\\s?[上中下]+)(）|\\))$", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}
		}
/*
		p = Pattern.compile("(.*?)(Ⅰ|Ⅱ|I{1,5})$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll(matcher.group(2).trim()+"$", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}else{
				item.setStrSession(matcher.group(2).trim());
			}
		}	*/				
	//   IIII5  康熙來了IV 全民最大黨II 真心英雄：法证先锋 ii 剧情	
			p = Pattern.compile("(\\(|（)(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})(\\)|）)");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll("(\\(|（)(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})(\\)|）)", " ").replace("（）","").replace("()", "").trim();		
			//	item.setStrSession(matcher.group(2).trim());		
				if (item.getStrCurrentSet().isEmpty()){
					item.setStrCurrentSet(matcher.group(2).trim());
				}else{
					item.setStrSession(matcher.group(2).trim());
				}
			}else{
				p = Pattern.compile("[^a-zA-Z]+(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$");
				matcher = p.matcher(programName);
				if(matcher.find()){
					programName = programName.replaceAll("(Ⅰ|Ⅴ|IV|Ⅱ|I{1,5}|i{1,5})\\s*$", " ").replace("（）","").replace("()", "").trim();		
			//		item.setStrSession(matcher.group(1).trim());		
					if (item.getStrCurrentSet().isEmpty()){
						item.setStrCurrentSet(matcher.group(1).trim());
					}else{
						item.setStrSession(matcher.group(1).trim());
					}
				}
			}
			
			
		//过滤尾部     ：:  -
		programName =programName.replaceAll("(#|-|:|：)$", "").trim();		
		//当集数 和 季都是空时，判断最后是否为数字，以做集数
		//fixme： 当前只要3位数字，防止 什么什么1945之类的名字		
		if (item.getStrCurrentSet().isEmpty() || item.getStrSession().isEmpty()){
			p = Pattern.compile("(\\D+)(\\d{1,3})$");
			matcher = p.matcher(programName);
			if(matcher.find()){
				programName = programName.replaceAll(matcher.group(2).trim()+"$", "").trim();		
				if (item.getStrCurrentSet().isEmpty() ){
					item.setStrCurrentSet(matcher.group(2).trim());
				}else if ( item.getStrSession().isEmpty()){
					item.setStrCurrentSet(matcher.group(2).trim());
				}
			}	
		}
		item.setStrProgramName(programName);
	}

	private void GetMovieInfo(CNTVEpgInfo item, String programName) {		
		programName = programName.replaceAll("\\d{1,2}/\\d{1,2}/\\d{1,4}", "").replace("()", "").trim();		
		//修正strtype		 
		String[] strInfos = programName.split(":|：");
		if (strInfos.length == 2){					 
			item.setStrType( strInfos[0].trim());
			programName = programName.replaceAll(item.getStrType() + "(:|：)", "").trim();		
		}else if(strInfos.length > 2){
			for (int i=0;i<strInfos.length;i++){
				if (strInfos[i].indexOf("片") !=-1){
					item.setStrType( strInfos[i].trim());
					programName = programName.replaceAll(item.getStrType() + "(:|：)", "").trim();	
				}
			}
		}
		//（一）  （二）  （上）
		//	programName = "高一生物：细胞代谢串讲课（四二中）";
		Pattern p = Pattern.compile("(（|\\()([一二三四五六七八九十零上中下]+)(）|\\))$");
		Matcher matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()([一二三四五六七八九十零上中下]+)(）|\\))$", "").replace("（）","").replace("()", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}else{
				item.setStrSession(matcher.group(2).trim());
			}
		}				
		
		// 1/2   (1/2)  （1/2）
		p = Pattern.compile("(.*?)(\\d+/\\d+|\\(\\d+/\\d+\\)|（\\d+/\\d+）)$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = matcher.group(1).trim();
			String strSetInfo = matcher.group(2).replaceAll("（|）|\\(|\\)","").trim();
			String[] strPosInfos = strSetInfo.split("/");
			if (strPosInfos.length == 2){
				item.setStrCurrentSet(strPosInfos[0]);
				item.setStrTotalSet(strPosInfos[1]);
			}else{
				item.setStrCurrentSet("");
				item.setStrTotalSet("");
			}						
		}
		// (1922) （45）
		p = Pattern.compile("(.*?)(\\(|（)(\\d+)(\\)|）)");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(\\(|（)(\\d+)(\\)|）)", "").replace("（）","").replace("()", "").trim();
			item.setStrCurrentSet(matcher.group(3).trim());
		}	
		
		item.setStrProgramName(programName);
	}

	private void GetTVColumnInfo(CNTVEpgInfo item, String programName) {		
		programName = programName.replaceAll("\\d{1,2}/\\d{1,2}/\\d{1,4}", "").replace("（）","").replace("()", "").trim();		
		// 1/2   (1/2)  （1/2）
		Pattern p = Pattern.compile("(.*?)(\\d+/\\d+|\\(\\d+/\\d+\\)|（\\d+/\\d+）)$");
		Matcher matcher = p.matcher(programName);
		if(matcher.find()){
			programName = matcher.group(1).trim();
			String strSetInfo = matcher.group(2).replaceAll("（|）|\\(|\\)","").trim();
			String[] strPosInfos = strSetInfo.split("/");
			if (strPosInfos.length == 2){
				item.setStrCurrentSet(strPosInfos[0]);
				item.setStrTotalSet(strPosInfos[1]);
			}else{
				item.setStrCurrentSet("");
				item.setStrTotalSet("");
			}						
		}
		//4、5、6、7     (4.5.6.7.8.9.10)  （13-16）  
		p = Pattern.compile("(.*?)(\\d+(、\\d+){1,}|\\d+(\\.\\d+){1,}|(\\d+-\\d+))");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replace(matcher.group(2).trim(), "").replace("（）","").replace("()", "").trim();
			item.setStrCurrentSet(matcher.group(2).trim());
		}
		// (1922) （45）
		p = Pattern.compile("(.*?)(\\(|（)(\\d+)(\\)|）)");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(\\(|（)(\\d+)(\\)|）)", "").replace("（）","").replace("()", "").trim();
			item.setStrCurrentSet(matcher.group(3).trim());
		}	
		
	//	programName="12:00 寰宇视野：伟大的卫国战I争IIII5";
		// )12 -123 ）12  IIII5
		p = Pattern.compile("(.*?)(\\)|-|）|Ⅰ|Ⅱ|I{1,5})(\\d+)$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll(matcher.group(3).trim() + "$",	"").trim();
			item.setStrCurrentSet(matcher.group(3).trim());
		}
		// （第二季）（第二部）
		p = Pattern.compile("第(.+)(部|季)");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replace(matcher.group(0), "").replace("（）","").replace("()", "").trim();
			item.setStrSession(matcher.group(1).trim());
		}
		
		//（一）  （二）  季
	//	programName = "高一生物：细胞代谢串讲课（四二中）";
		p = Pattern.compile("(（|\\()([一二三四五六七八九十零]+)(）|\\))$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()([一二三四五六七八九十零]+)(）|\\))$", "").replace("（）","").replace("()", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}else{
				item.setStrSession(matcher.group(2).trim());
			}
		}
		
		//（上） 集
		p = Pattern.compile("(（|\\()([上中下]+)(）|\\))$");
		matcher = p.matcher(programName);
		if (matcher.find()) {
			programName = programName.replaceAll("(（|\\()([上中下]+)(）|\\))$", "").replace("（）","").replace("()", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}
		}
		
		p = Pattern.compile("(.*?)(Ⅰ|Ⅱ|I{1,5})$");
		matcher = p.matcher(programName);
		if(matcher.find()){
			programName = programName.replaceAll(matcher.group(2).trim()+"$", "").trim();
			if (item.getStrCurrentSet().isEmpty()){
				item.setStrCurrentSet(matcher.group(2).trim());
			}else{
				item.setStrSession(matcher.group(2).trim());
			}
		}	
		//过滤尾部     ：:  -
		programName =programName.replaceAll("(#|-|:|：)$", "").trim();		
		//当集数 和 季都是空时，判断最后是否为数字，以做集数
		//fixme： 当前只要3位数字，防止 什么什么1945之类的名字	
		//排除  20:06 今晚8：00 
		if (item.getStrCurrentSet().isEmpty() || item.getStrSession().isEmpty()){
			p = Pattern.compile("\\d{1,2}(：|:)\\d{2}$");
			matcher = p.matcher(programName);
			if (matcher.find()){
				
			}else{
				p = Pattern.compile("(\\D+)(\\d{1,3})$");
				matcher = p.matcher(programName);
				if(matcher.find()){
					programName = programName.replaceAll(matcher.group(2).trim()+"$", "").trim();		
					if (item.getStrCurrentSet().isEmpty() ){
						item.setStrCurrentSet(matcher.group(2).trim());
					}else if ( item.getStrSession().isEmpty()){
						item.setStrCurrentSet(matcher.group(2).trim());
					}
				}	
			}
		}
		item.setStrProgramName(programName);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CNTVEpgExtrator EPGInfoExtrator = new CNTVEpgExtrator();
		String[] charset = new String[1];
		String strUrl = "http://tv.cntv.cn/index.php?action=epg-list&date=2012-09-22&channel=shandong";
		byte[] byHtml = EPGInfoExtrator.Download(strUrl , charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CNTVChannelInfo channelInfo = new CNTVChannelInfo ();
		List<CNTVEpgInfo> EPGInfos = new ArrayList<CNTVEpgInfo>();
		if (EPGInfoExtrator.GetEPGInfo(strHtml, charset[0],channelInfo, strUrl , EPGInfos)) {
		/*	Iterator<ChannelInfo> itr = channelTable.iterator();
			
			System.out.println(channelTable.toString());
		 
			System.out.println("channels:");
			while (itr.hasNext()) {
				ChannelInfo channelInfo = itr.next();
				System.out.println(channelInfo.getName() + " : " + channelInfo.getUrlMark());
			}*/
		} else {
			System.out.println("EPGInfoExtrator.GetEPGInfos err");
		}
	}

}
