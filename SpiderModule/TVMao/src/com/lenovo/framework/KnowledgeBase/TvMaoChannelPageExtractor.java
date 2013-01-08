package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelGroupPageUrl;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoChannelPageExtractor extends  TvMaoBaseExtractor {	

	public Boolean getChannelPage(String strHtml, String strCharset, TvMaoChannelGroupPageUrl channelGroupPageUrl, List<TvMaoChannelPageUrl> channelPageUrls) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);		
		try {
			Elements ELs = doc.select("body>div.clear>div.chlsnav>ul>li");
			for (Element src : ELs) {
				TvMaoChannelPageUrl item = new TvMaoChannelPageUrl();				
				Element nameElement = src.select("b").first();
				if (nameElement != null) {
					item.setChannelName ( nameElement.ownText());
					item.setUrl (channelGroupPageUrl.getAbsoluteUrl());
					Element otherElement = nameElement.select("span.gray").first();
					if (otherElement!=null){
						item.setChannelName(item.getChannelName() +otherElement.ownText());
					}
				}else{
					Element urlElement = src.select("a").first();
					if (urlElement != null) {
						item.setUrl(urlElement.attr("href"));
						item.setChannelName(urlElement.ownText());						
						Element otherElement = src.select("span.gray").first();
						if (otherElement!=null){
							item.setChannelName(item.getChannelName()+otherElement.ownText());
						}
					}else{
						continue;
					}
				}						
				item.setLevel ( channelGroupPageUrl.getLevel() + 1);
				item.setRelUrl ( channelGroupPageUrl.getAbsoluteUrl());
				item.setFindTime ( System.currentTimeMillis());
				item.setAbsoluteUrl ( mergeUrl(item.getRelUrl(), item.getUrl()));	
				item.setChannelGroupName ( channelGroupPageUrl.getChannelGroupName());
				item.setAreaName ( channelGroupPageUrl.getAreaName());
				channelPageUrls.add(item);
			}
		} catch (Exception e) {	
			bRet = false;
		}
		return bRet;
	}
	 
	public static void main(String[] args) {
		TvMaoChannelPageExtractor extractor = new TvMaoChannelPageExtractor();
		TvMaoChannelGroupPageUrl channelGroupPageUrl =new TvMaoChannelGroupPageUrl();
		java.util.List<TvMaoChannelPageUrl> channelPageUrls =  new ArrayList<TvMaoChannelPageUrl>();
		String[] charset = new String[1];
		channelGroupPageUrl.setAbsoluteUrl("http://www.tvmao.com/program/CCTV");
		byte[] byHtml = extractor.download(channelGroupPageUrl.getAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    extractor.getChannelPage(strHtml, charset[0], channelGroupPageUrl, channelPageUrls);		 
	}
}
