package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpisodeStory;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoEpisodeStoryPageUrlExtractor extends  TvMaoBaseExtractor{

	public Boolean getEpisodeStoryPageUrl(String strHtml, String strCharset,	String parentUrl, TvMaoEpgFirstPageInfo epgFirstPageInfo ,  List<TvMaoEpisodeStory> episodeStoryPageUrlList) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, parentUrl);	 			
			if (epgFirstPageInfo.getType().equals("电视剧")){			
				 getDramaInfo( doc , parentUrl, epgFirstPageInfo , episodeStoryPageUrlList);
			}else if (epgFirstPageInfo.getType().equals("电影")){
				// getMovieInfo( doc ,  item);
			}else{//缺省就是 栏目
				 getTVColumnInfo( doc , parentUrl, epgFirstPageInfo , episodeStoryPageUrlList);
			}	
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	
	private void getDramaInfo(Document doc, String parentUrl, TvMaoEpgFirstPageInfo epgFirstPageInfo ,  List<TvMaoEpisodeStory> episodeStoryPageUrlList) {	
		 Elements rows = doc.select("div.page-content div.article-wrap div.section-wrap section div.mt6 a");
			for (Element row : rows) {
				TvMaoEpisodeStory item = new TvMaoEpisodeStory();				
				item.setMainUrl( row.absUrl("href"));				
				item.setParentId(epgFirstPageInfo.getContentUuid());
				item.setType(epgFirstPageInfo.getType());	
				item.setParentSourceId(epgFirstPageInfo.getSourceID());	
				item.setProgramName(epgFirstPageInfo.getProgramName());	
				item.setTitleName(epgFirstPageInfo.getTitleName());					
				episodeStoryPageUrlList.add(item);
			}		 
	}
	
	private void getTVColumnInfo(Document doc, String parentUrl, TvMaoEpgFirstPageInfo epgFirstPageInfo ,  List<TvMaoEpisodeStory> episodeStoryPageUrlList) {	
		 	int num=0;
			Element numEl=  doc.select("div.page-content div.article-wrap div.section-wrap section div.guidelist div.page span.sum").first();
			if (numEl != null){			
				Pattern p = Pattern.compile("共(\\d+)页");
				Matcher matcher = p.matcher(numEl.text());
				if(matcher.find()){				
					try{
						num = Integer.parseInt(matcher.group(1));	
					}catch (Exception e){
						num = 0;
					}
				}		
			}		
			if (num == 0){
				TvMaoEpisodeStory item = new TvMaoEpisodeStory();				
				item.setMainUrl(parentUrl);				
				item.setParentId(epgFirstPageInfo.getContentUuid());
				item.setType(epgFirstPageInfo.getType());	
				item.setParentSourceId(epgFirstPageInfo.getSourceID());	
				item.setProgramName(epgFirstPageInfo.getProgramName());	
				item.setTitleName(epgFirstPageInfo.getTitleName());					
				episodeStoryPageUrlList.add(item);	
			}else{
				for (int i=0; i<num;i++) {
					TvMaoEpisodeStory item = new TvMaoEpisodeStory();				
					item.setMainUrl(parentUrl+ "/" + i*20);				
					item.setParentId(epgFirstPageInfo.getContentUuid());
					item.setType(epgFirstPageInfo.getType());	
					item.setParentSourceId(epgFirstPageInfo.getSourceID());	
					item.setProgramName(epgFirstPageInfo.getProgramName());	
					item.setTitleName(epgFirstPageInfo.getTitleName());					
					episodeStoryPageUrlList.add(item);				
				}
			}
	}
	
	public static void main(String[] args) {
		TvMaoEpisodeStoryPageUrlExtractor extractor = new TvMaoEpisodeStoryPageUrlExtractor();
		 String parentUrl="http://www.tvmao.com/drama/VFctbg==/episode";	
		 TvMaoEpgFirstPageInfo epgFirstPageInfo = new TvMaoEpgFirstPageInfo ();
		 epgFirstPageInfo.setType("电视剧");
		 
		 //http://www.tvmao.com/tvcolumn/VCxqWQ==/guides
		 parentUrl="http://www.tvmao.com/tvcolumn/dQ==/guides";			 
		parentUrl="http://www.tvmao.com/tvcolumn/Xh0=/guides";
		 epgFirstPageInfo.setType("栏目");
		 
		 List<TvMaoEpisodeStory> episodeStoryPageUrlList = new ArrayList<TvMaoEpisodeStory>();
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(parentUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getEpisodeStoryPageUrl(strHtml, charset[0], parentUrl,epgFirstPageInfo, episodeStoryPageUrlList)){
			System.out.println(episodeStoryPageUrlList.toString());
			for (TvMaoEpisodeStory item: episodeStoryPageUrlList){
				System.out.println(item.getMainUrl());
			}
		}
	}
	
}
