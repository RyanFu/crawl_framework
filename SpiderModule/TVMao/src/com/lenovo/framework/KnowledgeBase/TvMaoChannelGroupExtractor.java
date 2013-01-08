package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoChannelGroupExtractor extends  TvMaoBaseExtractor {	

	public Boolean getChannelGroupPage(String strHtml, String strCharset, TvMaoAreaPageUrl areaPageUrl, List<TvMaoChannelGroupPageUrl> channelGroupPageUrls, Boolean onlyCurrent) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);		
		Elements selfInfo = doc.select("body>div.clear>div.chlsnav>div.pbar>b");
		for (Element src : selfInfo) {
			TvMaoChannelGroupPageUrl item = new TvMaoChannelGroupPageUrl();
			item.setUrl(  areaPageUrl.getAbsoluteUrl());
			item.setChannelGroupName( src.ownText());
			item.setLevel ( areaPageUrl.getLevel() + 1);
			item.setRelUrl( areaPageUrl.getAbsoluteUrl());
			item.setFindTime (System.currentTimeMillis());
			item.setAbsoluteUrl (mergeUrl(item.getRelUrl() , item.getUrl()));				
			item.setAreaName (areaPageUrl.getAreaName());		
			channelGroupPageUrls.add(item);
		}	
		if (onlyCurrent == true){
			return bRet;
		}			
		Elements areas = doc.select("body>div.clear>div.chlsnav> a");
		for (Element src : areas) {
			TvMaoChannelGroupPageUrl item = new TvMaoChannelGroupPageUrl();
			item.setUrl(src.attr("href"));//
			item.setLevel ( areaPageUrl.getLevel() + 1);
			item.setRelUrl (  areaPageUrl.getAbsoluteUrl());
			item.setFindTime ( System.currentTimeMillis());
			item.setAbsoluteUrl( mergeUrl(item.getRelUrl() , item.getUrl()));						
			Element nameElement = src.select("div>b").first();
			if (nameElement != null) {
				item.setChannelGroupName( nameElement.ownText());
			}
			item.setAreaName(areaPageUrl.getAreaName());		
			channelGroupPageUrls.add(item);
		}				 
		return bRet;
	}
	
	public static void main(String[] args) {
		TvMaoChannelGroupExtractor extractor = new TvMaoChannelGroupExtractor();
		TvMaoAreaPageUrl areaPageUrl=new TvMaoAreaPageUrl ();
		java.util.List<TvMaoChannelGroupPageUrl> channelGroupPageUrls =new ArrayList<TvMaoChannelGroupPageUrl>();		
		String[] charset = new String[1];
		areaPageUrl.setAbsoluteUrl("http://www.tvmao.com/program/CCTV");
		byte[] byHtml = extractor.download(areaPageUrl.getAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		extractor. getChannelGroupPage(strHtml,  charset[0],   areaPageUrl,   channelGroupPageUrls,true) ; 
	} 
}
