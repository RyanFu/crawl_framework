package com.lenovo.framework.KnowledgeBase;

 
import java.io.UnsupportedEncodingException;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
 

 
import com.lenovo.framework.KnowledgeBase.bean.TvSouEpgInfo;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouTvFirstPageUrlExtractor  extends  TvSouBaseExtractor{ 
	
	public  Boolean getFirstPageUrl(String html, String charset, TvSouEpgInfo epgInfo) {
		Boolean bRet = false;			
		Document doc = Jsoup.parse(html);
		try {
			if (doc != null) {
				Element el = doc	.select("td.m1_menu1").first();
				if (el != null){
					Element elA = el	.select("a").first();
					if (elA != null){
						epgInfo.setProgramFirstPageUrl(elA.attr("href").trim());	
						epgInfo.setProgramFirstPageAbsoluteUrl(mergeUrl(epgInfo.getDramaAbsoluteUrl(), epgInfo.getProgramFirstPageUrl()));	
						bRet = true;		
					}
				}
			} 
		} catch (Exception e) {
			bRet = false;
		}
		return bRet;
	}
	
	public static void main(String[] args) {
		TvSouTvFirstPageUrlExtractor extractor = new TvSouTvFirstPageUrlExtractor();
		String rootUrl = "http://jq.tvsou.com/introhtml/744/11_74461_8.htm";	 
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(rootUrl, charset);
		String html = null;
		try {
			charset[0] = "gb2312";
			html = new String(byHtml,  charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}			
		TvSouEpgInfo epgInfo = new TvSouEpgInfo();
		epgInfo.setDramaAbsoluteUrl("http://jq.tvsou.com/introhtml/744/11_74461_8.htm");
		if (extractor.getFirstPageUrl(html, charset[0], epgInfo)){			 
			System.out.println(epgInfo.toString());		 
		}	
	}

}
