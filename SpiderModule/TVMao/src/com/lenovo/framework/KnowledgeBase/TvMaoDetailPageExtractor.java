package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;

import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoDetailPageExtractor  extends  TvMaoBaseExtractor{		
	
	public Boolean getDetailIntro(String strHtml, String strCharset, TvMaoEpgFirstPageInfo item) {	 
		Boolean bRet = true;	
		try {
			Document doc = Jsoup.parse(strHtml);	 
			item.setLevel(item.getLevel()+1);
			if (item.getType().equals("电视剧")){			
				 getDramaInfo( doc ,  item);
			}else if (item.getType().equals("电影")){
				 getMovieInfo( doc ,  item);
			}else{//缺省就是 栏目
				 getTVColumnInfo( doc ,  item);
			}	
		} catch (Exception e) {
			//bRet=false; //可以失败
		}
		return bRet;
	}

	private void getDramaInfo(Document doc, TvMaoEpgFirstPageInfo item) {
		Element descEL = doc.select("div.page-content article div[itemprop=description]").first();
		if (descEL != null){
			item.setDescription(descEL.text());		
		}	
	}

	private void getMovieInfo(Document doc, TvMaoEpgFirstPageInfo item) {
		Element descEL = doc.select("div.page-content article div[itemprop=description]").first();
		if (descEL != null){
			item.setDescription(descEL.text());		
		}		 
		Element airDateEL = doc.select("div.page-content span.abstract td.td1 span[itemprop=datePublished]").first();
		if (airDateEL != null){		
			String str = airDateEL.ownText().trim();
			String infos[] = str.split(" ");
			if (infos.length ==2){
				item.setAirDate(str);		
			}else{
				item.setAirDate(str+" 00:00:00");		
			}
		}		
		Element durationEL = doc.select("div.page-content span.abstract span[itemprop=duration]").first();
		if (durationEL != null){			  
			String str = durationEL.ownText().trim();
			Pattern p = Pattern.compile("(\\d+)");
			Matcher matcher = p.matcher(str);
			if(matcher.find()){
				item.setDuration(matcher.group(1));		
			}			
		}		
		//tags(概要页无)
		Elements tds = doc.select("div.page-content span.abstract table.obj_meta td");
		for (Element td : tds) {			
			String strFieldName = td.ownText().trim();
			if (strFieldName.equals("类别：")){
				Element nextTd = td.nextElementSibling();
				if (nextTd != null){
					Elements childELs = nextTd.children();
					String strTmp = "";
					for (Element child : childELs) {		
						if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
							strTmp+= child.ownText().trim()+"/";
						}
					}
					if (strTmp.isEmpty() == false){
						item.setTags ( strTmp.substring(0,strTmp.length()-1));
					}
				}						
			}
		}
	}

	private void getTVColumnInfo(Document doc, TvMaoEpgFirstPageInfo item) {
		Element descEL = doc.select("div.page-content article div[itemprop=description]").first();
		if (descEL != null){
			item.setDescription(descEL.text());		
		}	
	}
	
	public static void main(String[] args) {
		TvMaoDetailPageExtractor extractor = new TvMaoDetailPageExtractor();	 
		TvMaoEpgFirstPageInfo item=new TvMaoEpgFirstPageInfo ();		 
		 
		String[] charset = new String[1];
		item.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/drama/cVhTNQ==");//电视剧
		item.setType("电视剧");
		//item.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/movie/MRwzZig=");//电影
		//item.setType("电影");
		//item.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/tvcolumn/M2tVVQ==");//栏目
		 //item.setType("栏目");
		byte[] byHtml = extractor.download(TvMaoEpgFirstPageInfoExtractor.getUrlByNextTask(item, TvMaoEpgFirstPageInfoExtractor.TaskUriName.DETAIL), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		 
		extractor. getDetailIntro(strHtml,  charset[0],   item) ;
	}

}
