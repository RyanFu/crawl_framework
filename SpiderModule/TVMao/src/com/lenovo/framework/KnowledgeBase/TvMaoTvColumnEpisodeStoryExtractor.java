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

import com.lenovo.framework.KnowledgeBase.bean.TvMaoActor;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpisodeStory;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoTvColumnEpisodeStoryExtractor  extends  TvMaoBaseExtractor{

	public Boolean getTvColumnEpisodeStory(String strHtml, String strCharset,	TvMaoEpisodeStory episodeStory, List<TvMaoActor> guestList) {
		Boolean ret = true;			
		try {
			Document doc = Jsoup.parse(strHtml,episodeStory.getMainUrl());	 
			//集20121011
			Element el = doc.select("div.page-content div.clear h1.lt").first();
			if(el!=null){
				Pattern p = Pattern.compile("第(\\d+)-(\\d+)-(\\d+)期");
				Matcher matcher = p.matcher( el.ownText());
				if(matcher.find()){				 
					episodeStory.setEpisode(matcher.group(1).trim() + matcher.group(2).trim() + matcher.group(3).trim());					
					//sourceid			
					episodeStory.setSourceId(episodeStory.getParentSourceId() +"_"+ episodeStory.getEpisode());
					//对sourceid md5 当作ContentUuid
					episodeStory.setContentUuid(myUuid(episodeStory.getSourceId().getBytes()));		
				}
			}
			el = doc.select("div.page-content div.article-wrap div.section-wrap section div.article div.glist div.desc2").first();
			if(el!=null){
				String description = el.text();
				episodeStory.setDescription(description);
			}			
			//嘉宾
			String guestNameList = "";
			Elements rows = doc.select("div.page-content div.article-wrap div.section-wrap section div.article div.glist div.desc2 p a[href^=/star]");
			for (Element row : rows){				
					TvMaoActor item = new TvMaoActor();						
					item.setName(row.ownText());				 
					item.setMainUrl(row.absUrl("href"));	
					item.setRefUrl(item.getMainUrl());
					String sourceIDTmp = getScoreId(item.getMainUrl());
					item.setSourceID(sourceIDTmp);				
					guestList.add(item);					
					guestNameList += item.getName()+"-"+item.getSourceID()+"/";
			}
			if (guestNameList.length()>0){
				char x =guestNameList.charAt(guestNameList.length()-1);
				if (x=='/'){
					guestNameList = guestNameList.substring(0,guestNameList.length()-1);
				}		
			}			
			episodeStory.setGuest(guestNameList);
		} catch (Exception e) {
			ret=false; 
		}		
		return ret;
	}
	
	public static void main(String[] args) {
		TvMaoTvColumnEpisodeStoryExtractor extractor = new TvMaoTvColumnEpisodeStoryExtractor();
		List<TvMaoActor> guestList = new ArrayList<TvMaoActor> ();
		TvMaoEpisodeStory episodeStory = new TvMaoEpisodeStory();
		episodeStory.setMainUrl("http://www.tvmao.com/tvcolumn/ME4o/guide/Ii1iLDI=");
			
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(episodeStory.getMainUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getTvColumnEpisodeStory(strHtml, charset[0], episodeStory, guestList)){
			System.out.println(episodeStory.toString());
		}
	}
	
}
