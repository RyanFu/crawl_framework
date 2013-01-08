package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CNTVChannelExtrator  extends  CNTVExtractBase{

	public Boolean GetChannels(String strHtml, String strCharset, String strRefUrl, java.util.List<CNTVChannelInfo> channelTable) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);
		try {
			if (doc != null) {
				Elements areas = doc	.select("div.md_left a.channel");
				for (Element src : areas) {
					CNTVChannelInfo item = new CNTVChannelInfo();
					item.setUrlMark(src.attr("rel")) ;//
					item.setName(src.ownText().trim());
					item.setRefUrl(strRefUrl);		 
					channelTable.add(item);
				}
			} else {
				bRet = false;
			}
		} catch (Exception e) {
			 bRet = false;
		//	e.printStackTrace();
		}
		return bRet;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		CNTVChannelExtrator channelExtrator = new CNTVChannelExtrator();
		String[] charset = new String[1];
		String strRootUrl = "http://tv.cntv.cn/epg?channel=cctvxiqu";
		byte[] byHtml = channelExtrator.Download(strRootUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<CNTVChannelInfo> channelTable = new ArrayList<CNTVChannelInfo>();
		if (channelExtrator.GetChannels(strHtml, charset[0],strRootUrl, channelTable)) {
			Iterator<CNTVChannelInfo> itr = channelTable.iterator();			
			System.out.println(channelTable.toString());		 
			System.out.println("channels:");
			while (itr.hasNext()) {
				CNTVChannelInfo channelInfo = itr.next();
				System.out.println(channelInfo.getName() + " : " + channelInfo.getUrlMark());
			}
		} else {
			System.out.println("channelExtrator.GetChannels err");
		}
	}

}
