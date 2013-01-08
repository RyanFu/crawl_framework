package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoSatelliteChannelExtractor extends TvMaoBaseExtractor {
	
	public Boolean getChannelPage(String strHtml, String strCharset, String parentUrl, List<TvMaoChannelPageUrl> channelPageList) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);
		// self info
		try {	
			Elements ELs = doc.select("div.page-content div.chlsnav ul li");
			for (Element src : ELs) {
				TvMaoChannelPageUrl item = new TvMaoChannelPageUrl();				
				Element bEL = src.select("b").first();
				if (bEL != null) {
					item.setChannelName ( bEL.ownText());
					item.setUrl (parentUrl);				
				}else{
					Element aEL = src.select("a").first();
					if (aEL != null) {
						item.setUrl(aEL.attr("href"));
						item.setChannelName(aEL.ownText());						
					}else{
						continue;
					}
				}						
				item.setLevel (2);
				item.setRelUrl (parentUrl);
				item.setFindTime ( System.currentTimeMillis());
				item.setAbsoluteUrl ( mergeUrl(item.getRelUrl(), item.getUrl()));	
				item.setChannelGroupName ("卫视");
				item.setAreaName ("");
				channelPageList.add(item);
			}
		}catch (Exception e) {	
			bRet = false;
		}
		return bRet;
	}
	public static void main(String[] args) {
		TvMaoSatelliteChannelExtractor extractor = new TvMaoSatelliteChannelExtractor();
		 String parentUrl ="http://www.tvmao.com/program_satellite/AHTV1-w3.html";
		List<TvMaoChannelPageUrl> channelPageList =  new ArrayList<TvMaoChannelPageUrl>();
		String[] charset = new String[1];		 
		byte[] byHtml = extractor.download(parentUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (extractor.getChannelPage(strHtml, charset[0], parentUrl, channelPageList)){
	    	for (TvMaoChannelPageUrl item:channelPageList ){
	    		System.out.println(item.getChannelName() +" : "+ item.getAbsoluteUrl());
	    	}
	    }
	}	
}
