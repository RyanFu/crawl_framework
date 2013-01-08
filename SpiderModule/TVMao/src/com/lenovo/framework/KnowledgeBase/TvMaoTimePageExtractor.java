package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoTimeUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoTimePageExtractor extends  TvMaoBaseExtractor{
	 
	public Boolean getTimePage(String strHtml, String strCharset,	TvMaoChannelPageUrl channelPageUrl , List<TvMaoTimeUrl> timePageUrls) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);
		// cur week
		String weekName = null;
		try {
			Element firstNavElement = doc.select("nav.theweek").first();
			Element WeekNameElement=null;
			if (firstNavElement  !=null){
				WeekNameElement = firstNavElement.select("span").first();
			}
			if (WeekNameElement != null  && firstNavElement!=null) {
				weekName = WeekNameElement.ownText();
				Elements dayElements = firstNavElement.select("a");
				for (Element src : dayElements) {
					TvMaoTimeUrl item = new TvMaoTimeUrl();
					item.setUrl( src.attr("href"));
					if (item.getUrl().equals("")) {
						item.setUrl( channelPageUrl.getAbsoluteUrl());
					}
					Element el = src.select("span").first();
					if (el != null){
						item.setDayName ( el.ownText());
					}else{
						continue;
					}
					item.setLevel ( channelPageUrl.getLevel() + 1);
					item.setRelUrl ( channelPageUrl.getAbsoluteUrl());
					item.setFindTime ( System.currentTimeMillis());
					item.setWeekName ( weekName);
					item.setTitle ( src.attr("title"));
					item.setAbsoluteUrl ( mergeUrl(item.getRelUrl(), item.getUrl()));
					item.setChannelName ( channelPageUrl.getChannelName());
					item.setAreaName ( channelPageUrl.getAreaName());
					item.setChannelGroupName(channelPageUrl.getChannelGroupName());
					timePageUrls.add(item);
				}
			}	
		} catch (Exception e) {
			bRet = false;//e.printStackTrace();
		}
		//nextweek
		try {
			Element firstNavElement = doc.select("nav.nextweek").first();
			Element WeekNameElement=null;
			if (firstNavElement  !=null){
				WeekNameElement = firstNavElement.select("span").first();
			}
			if (WeekNameElement != null  && firstNavElement!=null) {
				weekName = WeekNameElement.ownText();
				Elements dayElements = firstNavElement.select("a");
				for (Element src : dayElements) {
					TvMaoTimeUrl item = new TvMaoTimeUrl();
					item.setUrl( src.attr("href"));
					if (item.getUrl().equals("")) {
						item.setUrl( channelPageUrl.getAbsoluteUrl());
					}
					Element el = src.select("span").first();
					if (el != null){
						item.setDayName ( el.ownText());
					}else{
						continue;
					}
					item.setLevel ( channelPageUrl.getLevel() + 1);
					item.setRelUrl ( channelPageUrl.getAbsoluteUrl());
					item.setFindTime ( System.currentTimeMillis());
					item.setWeekName ( weekName);
					item.setTitle ( src.attr("title"));
					item.setAbsoluteUrl ( mergeUrl(item.getRelUrl(), item.getUrl()));
					item.setChannelName ( channelPageUrl.getChannelName());
					item.setAreaName ( channelPageUrl.getAreaName());
					item.setChannelGroupName(channelPageUrl.getChannelGroupName());
					timePageUrls.add(item);
				}
			}	
		} catch (Exception e) {		
		}
		return bRet;
	}	
	
	public static void main(String[] args) {
		TvMaoTimePageExtractor extractor = new TvMaoTimePageExtractor();
		TvMaoChannelPageUrl channelPageUrl =new TvMaoChannelPageUrl ();
		List<TvMaoTimeUrl> timePageUrls =new ArrayList<TvMaoTimeUrl>();		
		String[] charset = new String[1];
		channelPageUrl.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV7-w2.html");
		byte[] byHtml = extractor.download(channelPageUrl.getAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor. getTimePage(strHtml,  charset[0],   channelPageUrl,   timePageUrls) ){
			for (TvMaoTimeUrl item : timePageUrls){
				System.out.println(TvMaoTimePageExtractor.class + item.getChannelName()+" : "+item.getAbsoluteUrl());
			}
		}
	} 
}
