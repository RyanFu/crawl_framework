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

 
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpisodeStory;
 
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoEpisodeStoryExtractor extends  TvMaoBaseExtractor{

	public Boolean getEpisodeStory(String strHtml, String strCharset,	TvMaoEpisodeStory episodeStory,  List<TvMaoEpisodeStory> episodeStoryList) {
		Boolean ret = true;			
		try {
			Document doc = Jsoup.parse(strHtml,episodeStory.getMainUrl());	 			
			if (episodeStory.getType().equals("电视剧")){			
				 getDramaInfo( doc ,  episodeStory ,episodeStoryList);
			}else if (episodeStory.getType().equals("电影")){
				// getMovieInfo( doc ,  item);
			}else{//缺省就是 栏目
				getTVColumnInfo( doc ,  episodeStory ,episodeStoryList);
			}	
		} catch (Exception e) {
			ret=false; 
		}		
		return ret;
	}
	
	private void getDramaInfo(Document doc, TvMaoEpisodeStory episodeStory,  List<TvMaoEpisodeStory> episodeStoryList) {	
		Elements divELs = doc.select("div.page-content article.mt6 div");
		for (Element divEL : divELs) {
			Element h2EL = divEL.select("h2").first();
			if (h2EL != null){
				TvMaoEpisodeStory item = new TvMaoEpisodeStory();
				item.setMainUrl(episodeStory.getMainUrl());
				item.setParentId(episodeStory.getParentId());
				item.setType(episodeStory.getType());	
				item.setParentSourceId(episodeStory.getParentSourceId());
				item.setProgramName(episodeStory.getProgramName());	
				item.setTitleName(episodeStory.getTitleName());		
				//集
				Pattern p = Pattern.compile("(\\d+)集");
				Matcher matcher = p.matcher( h2EL.text());
				if(matcher.find()){				 
					item.setEpisode(matcher.group(1).trim());					
					//sourceid			
					item.setSourceId(item.getParentSourceId() +"_"+ matcher.group(1).trim());
					//对sourceid md5 当作ContentUuid
					item.setContentUuid(myUuid(item.getSourceId().getBytes()));		
				}
				Element nextDiv = h2EL.nextElementSibling();
				if (nextDiv!=null ){
					String description = nextDiv.text();
					item.setDescription(description);
				}
				episodeStoryList.add(item);
			}		
		}
	}
	private void getTVColumnInfo(Document doc, TvMaoEpisodeStory episodeStory,  List<TvMaoEpisodeStory> episodeStoryList) {	
		Elements divELs = doc.select("div.page-content div.article-wrap div.section-wrap section div.guidelist div.glist div.desc2");
		for (Element divEL : divELs) {
			Element el = divEL.select("a").first();
			if (el != null){
				TvMaoEpisodeStory item = new TvMaoEpisodeStory();
				item.setMainUrl(el.absUrl("href"));//
				item.setParentId(episodeStory.getParentId());
				item.setType(episodeStory.getType());	
				item.setParentSourceId(episodeStory.getParentSourceId());
				item.setProgramName(episodeStory.getProgramName());	
				item.setTitleName(episodeStory.getTitleName());		//不同于电视剧  				
				episodeStoryList.add(item);
			}		
		}
	}

	public static void main(String[] args) {
		TvMaoEpisodeStoryExtractor extractor = new TvMaoEpisodeStoryExtractor();
		TvMaoEpisodeStory episodeStory = new TvMaoEpisodeStory();
		episodeStory.setMainUrl("http://www.tvmao.com/drama/VyxvUQ==/episode/2");
		episodeStory.setType("电视剧");
		
	//	episodeStory.setMainUrl("http://www.tvmao.com/tvcolumn/ME4o/guides");
		//http://www.tvmao.com/tvcolumn/VCxqWQ==/guides
	//	episodeStory.setType("栏目");
		episodeStory.setMainUrl("http://www.tvmao.com/tvcolumn/VCxqWQ==/guides");		
	 	episodeStory.setType("栏目");
		
		episodeStory.setParentId("setParentId");
		episodeStory.setParentSourceId("setParentSourceId");
		 List<TvMaoEpisodeStory> episodeStoryList = new ArrayList<TvMaoEpisodeStory>();
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(episodeStory.getMainUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getEpisodeStory(strHtml, charset[0], episodeStory, episodeStoryList)){
			System.out.println(episodeStory.toString());
		}
	}
}
