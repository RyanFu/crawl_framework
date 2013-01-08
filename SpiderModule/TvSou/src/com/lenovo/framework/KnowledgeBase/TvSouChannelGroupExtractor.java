package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvSouAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvSouChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouChannelGroupExtractor extends  TvSouBaseExtractor {
	
	public Boolean getChannelGroupPage(String html, String charset, TvSouAreaPageUrl areaPageUrl,  List<TvSouChannelGroupPageUrl> channelGroupPageList) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(html);		
		Elements els = doc.select("div#con>div#con1>div.listmenu>a.blue2");
		for (Element src : els) {
			TvSouChannelGroupPageUrl item = new TvSouChannelGroupPageUrl();
			item.setUrl(src.attr("href").trim());
			item.setChannelGroupName( src.ownText());
			item.setLevel(areaPageUrl.getLevel()+1);
			item.setRelUrl(areaPageUrl.getAbsoluteUrl());
			item.setFindTime(System.currentTimeMillis());
			item.setAbsoluteUrl(mergeUrl(item.getRelUrl() , item.getUrl()));				
			item.setAreaName(areaPageUrl.getAreaName());		
			channelGroupPageList.add(item);
		}							 
		return bRet;
	}
	
	public static void main(String[] args) {
		TvSouAreaPageUrl areaPageUrl = new TvSouAreaPageUrl();
		areaPageUrl.setUrl("http://epg.tvsou.com/programys/TV_1/Channel_1/W1.htm");
		areaPageUrl.setRelUrl("http://epg.tvsou.com/program/TV_15/Channel_37/W3.htm");
		areaPageUrl.setAreaName("CCTV");
		areaPageUrl.setLevel(1);		 
		areaPageUrl.setFindTime(System.currentTimeMillis());
		areaPageUrl.setTitle("CCTV");
		areaPageUrl.setAbsoluteUrl("http://epg.tvsou.com/program/TV_12/Channel_34/W3.htm");
 
		TvSouChannelGroupExtractor extractor = new TvSouChannelGroupExtractor();
		List<TvSouChannelGroupPageUrl> channelGroupPageList =new ArrayList<TvSouChannelGroupPageUrl>();		
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(areaPageUrl.getAbsoluteUrl(), charset);
		String html = null;
		try {
			charset[0] = "gb2312";
			html = new String(byHtml,  charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor. getChannelGroupPage(html,  charset[0],   areaPageUrl,   channelGroupPageList) ){
			for (TvSouChannelGroupPageUrl item : channelGroupPageList){
				System.out.println(item.getChannelGroupName() + " : " + item.getAbsoluteUrl() + " in " + item.getAreaName());
			}
		}
	} 
	
}
