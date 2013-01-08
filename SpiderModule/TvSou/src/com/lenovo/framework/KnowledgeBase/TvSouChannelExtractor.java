package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouChannelExtractor extends  TvSouBaseExtractor {
	  
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
	public Boolean getChannelPage(String html, String charset, TvSouChannelGroupPageUrl channelGroupPageUrl, List<TvSouChannelPageUrl> channelPageList) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(html);			
		try {		
			//self
			Element el = doc.select("div#con>div#con1>div.listmenu2>div>font").first();
			if (el !=null){
				TvSouChannelPageUrl item = new TvSouChannelPageUrl();	
				item.setChannelName(el.ownText());
				item.setUrl(channelGroupPageUrl.getAbsoluteUrl());
				item.setLevel(channelGroupPageUrl.getLevel() + 1);
				item.setRelUrl( channelGroupPageUrl.getAbsoluteUrl());
				item.setFindTime(System.currentTimeMillis());
				item.setAbsoluteUrl( mergeUrl(item.getRelUrl(), item.getUrl()));
				item.setChannelGroupName( channelGroupPageUrl.getChannelGroupName());
				item.setAreaName (channelGroupPageUrl.getAreaName());
				channelPageList.add(item);
			}				
			// others
			Elements els = doc.select("div#con>div#con1>div.listmenu2>div>a.blue2");
			for (Element src : els) {
				TvSouChannelPageUrl item = new TvSouChannelPageUrl();	
				item.setChannelName(src.ownText());
				item.setUrl(src.attr("href"));
				item.setLevel(channelGroupPageUrl.getLevel() + 1);
				item.setRelUrl( channelGroupPageUrl.getAbsoluteUrl());
				item.setFindTime(System.currentTimeMillis());
				item.setAbsoluteUrl( mergeUrl(item.getRelUrl(), item.getUrl()));
				item.setChannelGroupName( channelGroupPageUrl.getChannelGroupName());
				item.setAreaName (channelGroupPageUrl.getAreaName());			
				channelPageList.add(item);
			}
			//extend time info
			extendTimeInfo(doc, channelGroupPageUrl, channelPageList);			
		} catch (Exception e) {	
			bRet = false;
		}
		return bRet;
	}
	 
	private void extendTimeInfo(Document doc, TvSouChannelGroupPageUrl channelGroupPageUrl, List<TvSouChannelPageUrl> channelPageList) throws Exception{
		if (channelPageList.isEmpty()) return;
		int year=0;
		int month=0;
		int day=0;
		int weekDay=0;
		Pattern p = Pattern.compile("/W(\\d{1,})\\.htm$", Pattern.DOTALL  | Pattern.CASE_INSENSITIVE); 
		Matcher matcher = p.matcher(channelGroupPageUrl.getAbsoluteUrl());
		if(matcher.find()){
			weekDay = Integer.parseInt(matcher.group(1).trim());
		}
		Element el = doc.select("div#con>div#con2>table").first().select("td").last();
		if (el != null){			
			p = Pattern.compile("(\\d{4})年(\\d{1,2})月(\\d{1,2})日", Pattern.DOTALL|Pattern.CASE_INSENSITIVE); 
			matcher = p.matcher(el.ownText().trim());
			if(matcher.find()){
				year =Integer.parseInt(matcher.group(1).trim());
				month =Integer.parseInt(matcher.group(2).trim());
				day =Integer.parseInt(matcher.group(3).trim());
			}
		}		
		if (year == 0 || month ==0 || day ==0 ||  weekDay == 0 ){
			   throw new Exception(TvSouChannelExtractor.class+" :: ExtendTimeInfo empty");
		}		
		List<TvSouChannelPageUrl>newList = new ArrayList<TvSouChannelPageUrl>();		
		Calendar calendar =  Calendar.getInstance();
		calendar.set(year, month-1, day);		
		Calendar calendarBak =  (Calendar)calendar.clone();		 
	 	String yearMonthDay = dateFormat.format(calendarBak.getTime());
	 	String strWeekDay = Integer.toString(weekDay);
		for (TvSouChannelPageUrl item : channelPageList){			
			item.setYearMonthDay(yearMonthDay);
			item.setWeekDay(strWeekDay);
			String url =item.getAbsoluteUrl();
			for (int i=1; i<=7; i++){
				if (i != weekDay){					
					TvSouChannelPageUrl newItem = (TvSouChannelPageUrl) item.clone();
					newItem.setUrl( url.replaceAll("(/W)\\d{1}(\\.htm$)", "$1"+ Integer.toString(i)+"$2"));
					newItem.setRelUrl(item.getAbsoluteUrl());
					newItem.setAbsoluteUrl( mergeUrl(newItem.getRelUrl(), newItem.getUrl()));
					newItem.setWeekDay(Integer.toString(i));					
					calendar.add(Calendar.HOUR, 24*(i-weekDay));
					String yearMonthDayTmp = dateFormat.format(calendar.getTime());					
					newItem.setYearMonthDay(yearMonthDayTmp);
					newList.add(newItem);
					calendar  =  (Calendar)calendarBak.clone();		 
				}
			}			
		}
		channelPageList.addAll(newList);
	}
	
	public static void main(String[] args) {		
		TvSouChannelGroupPageUrl channelGroupPageUrl =new TvSouChannelGroupPageUrl();			 
		channelGroupPageUrl.setUrl("/programys/TV_1/Channel_1/W1.htm");
		channelGroupPageUrl.setRelUrl("http://epg.tvsou.com/program/TV_12/Channel_34/W3.htm");
		channelGroupPageUrl.setAreaName("黑龙江");
		channelGroupPageUrl.setLevel(2);		 
		channelGroupPageUrl.setFindTime(System.currentTimeMillis());
		channelGroupPageUrl.setTitle("黑龙江电视台");
		channelGroupPageUrl.setChannelGroupName("黑龙江电视台");
		channelGroupPageUrl.setAbsoluteUrl("http://epg.tvsou.com/program/TV_82/Channel_393/W5.htm");		
		TvSouChannelExtractor extractor = new TvSouChannelExtractor();
		List<TvSouChannelPageUrl> channelPageList =  new ArrayList<TvSouChannelPageUrl>();
		String[] charset = new String[1];		 
		byte[] byHtml = extractor.download(channelGroupPageUrl.getAbsoluteUrl(), charset);
		String html = null;
		try {
			charset[0] = "gb2312";
			html = new String(byHtml,  charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	   if (extractor.getChannelPage(html, charset[0], channelGroupPageUrl, channelPageList)){
			for (TvSouChannelPageUrl item : channelPageList){
				System.out.println(item.getChannelName() + " : " + item.getAbsoluteUrl() + " in " + item.getChannelGroupName());
			}
	   }
	}
	
}
