package com.lenovo.framework.KnowledgeBase;
 

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoReviewInfo;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoReviewBaseExtractor extends  TvMaoBaseExtractor{

	public Boolean getReviewBaseInfo(String html, String charset, String refUrl, String contentID, List<TvMaoReviewInfo> reviewList) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(html, refUrl);	 			
			Elements els = doc.select("div.page-content div.article-wrap div.section-wrap section div#revlst_t.dg>div>div.c2");			 
			for (Element el :els){
				Boolean hasErr = true;
				TvMaoReviewInfo item = new TvMaoReviewInfo();
				Element reviewEl = el.select("a[itemprop=url]").first();
				if (reviewEl !=null){
					String title = reviewEl.ownText();
					String url = reviewEl.absUrl("href");
					if (url !=null && title!=null && url.isEmpty()==false && title.isEmpty()==false){
						hasErr = false;
						item.setMailUrl(url);
						item.setTitle(title);
					}
				}				
				//user
				Element usrEl = el.select("a.user").first();
				if (usrEl !=null){
					item.setUid(usrEl.attr("userid"));
					item.setName(usrEl.ownText());
				}			
				if (!hasErr){
					item.setSource("TVMAO");
					item.setSourceID(getScoreId(item.getMailUrl()));
					String uuidSource = item.getSource()+getScoreId(item.getMailUrl());
					item.setCommentID(myUuid(uuidSource.getBytes()));
					item.setContentID(contentID);
					reviewList.add(item);//差	private String content;		private String pubTime;
				}
			}
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	
	public static void main(String[] args) {
		TvMaoReviewBaseExtractor extractor = new TvMaoReviewBaseExtractor();
		 String refUrl="http://www.tvmao.com/tvcolumn/dA==/reviews";
		  refUrl="http://www.tvmao.com/movie/LiIwZig=/reviews";
		 String contentID="contentid";
		 List<TvMaoReviewInfo> reviewList = new ArrayList<TvMaoReviewInfo>();	
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(refUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor.getReviewBaseInfo(strHtml, charset[0],refUrl,contentID, reviewList)) {
			for (TvMaoReviewInfo item:reviewList){
				System.out.println(item.toString());
			}
		} 		
	}
}
