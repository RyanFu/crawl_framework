package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
 

import com.lenovo.framework.KnowledgeBase.bean.TvMaoReviewInfo;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoReviewExtractor extends  TvMaoBaseExtractor{
	
	public Boolean getReviewInfo(String html, String charset, TvMaoReviewInfo item) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(html);	 			
			Element timeEl = doc.select("div.page-content div.article-wrap div.section-wrap section span[itemprop=dateCreated]").first();
			if (timeEl !=null){				
				item.setPubTime(timeEl.ownText()+ ":00");				 
			}		
			Element el = doc.select("div.page-content div.article-wrap div.section-wrap section div[itemprop=reviewBody]").first();
			if (el !=null){				
				item.setContent(el.text());				 
			}
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	
	public static void main(String[] args) {
		TvMaoReviewExtractor extractor = new TvMaoReviewExtractor();				
		 TvMaoReviewInfo item=new TvMaoReviewInfo();
		 item.setMailUrl("http://www.tvmao.com/drama/MRwuYiw=/review/9870");
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(item.getMailUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor.getReviewInfo(strHtml, charset[0], item)) {
		 
				System.out.println(item.toString());
		 
		} 		
	}
}
