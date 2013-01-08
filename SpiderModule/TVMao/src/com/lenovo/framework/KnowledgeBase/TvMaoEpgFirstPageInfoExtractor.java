package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoContentImg;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgInfo;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoEpgFirstPageInfoExtractor extends  TvMaoBaseExtractor{
	public enum TaskUriName {
		DETAIL, // 详细介绍
		ACTOR,
		REVIEW,//长评论
		TVCOLUMN_DETAIL, // 栏目介绍
		TVCOLUMN_ACTOR, // 主持人
		TVCOLUMN_EPISODE, // 分集剧情 /guides
		DRAMA_EPISODE, // 分集剧情
		DRAMA_ACTOR, // 演员表
		DRAMA_PICTURE, // 剧照
		DRAMA_GUIDES, // 每期预告  (分集剧情)
		DRAMA_DETAIL, // 详细介绍
		MOVIE_DETAIL, // "详细介绍
		MOVIE_ACTOR, // 演员表
		MOVIE_PICTURE, // 剧照"
		MOVIE_POSTER// 海报
	}
	public final static  Map<TaskUriName, String> taskUriMap = new HashMap<TaskUriName, String>();
    static{
    	taskUriMap.put(TaskUriName.DETAIL, "/detail");    
    	taskUriMap.put(TaskUriName.ACTOR, "/actors");    
    	taskUriMap.put(TaskUriName.REVIEW, "/reviews");    
    	taskUriMap.put(TaskUriName.TVCOLUMN_DETAIL, "/detail");    
    	taskUriMap.put(TaskUriName.TVCOLUMN_ACTOR, "/actors");       
    	taskUriMap.put(TaskUriName.TVCOLUMN_EPISODE, "/guides");    
    	taskUriMap.put(TaskUriName.DRAMA_EPISODE, "/episode");   
    	taskUriMap.put(TaskUriName.DRAMA_ACTOR, "/actors");   
    	taskUriMap.put(TaskUriName.DRAMA_PICTURE, "/pictures");   
    	taskUriMap.put(TaskUriName.DRAMA_DETAIL, "/detail");      	
    	taskUriMap.put(TaskUriName.DRAMA_GUIDES, "/guides");     
    	taskUriMap.put(TaskUriName.MOVIE_DETAIL, "/detail");   
    	taskUriMap.put(TaskUriName.MOVIE_ACTOR, "/actors");   
    	taskUriMap.put(TaskUriName.MOVIE_PICTURE, "/pictures");       	
    	taskUriMap.put(TaskUriName.MOVIE_POSTER, "/posters");       	
    }
    
	public Boolean getEpgFirstPageInfo(String strHtml, String strCharset, TvMaoEpgInfo epgInfo, List<TvMaoEpgFirstPageInfo> epgFirstPageInfos
			,  List<TvMaoContentImg> contentImgList) {
		Boolean bRet = true;		
		Document doc = Jsoup.parse(strHtml);
		TvMaoEpgFirstPageInfo item = new TvMaoEpgFirstPageInfo(epgInfo);
		if (epgInfo.getType().equals("电视剧")){			
			 getDramaInfo( doc ,  item);
			 getDramaImgInfo( doc, item, contentImgList);
		}else if (epgInfo.getType().equals("电影")){
			 getMovieInfo( doc ,  item);
			 getMovieImgInfo( doc, item, contentImgList);
		}else{//缺省就是 栏目
			 getTVColumnInfo( doc ,  item);
			 getTVColumnImgInfo( doc, item, contentImgList);
		}		
		//sourceid			
		item.setSourceID(getScoreId(item.getProgramFirstPageAbsoluteUrl()));
		//对sourceid md5 当作ContentUuid (优化考虑：移动到epgextrator)
	//	item.setContentUuid(myUuid(item.getSourceID().getBytes()));			
		//title
		Element titleEL  = doc.select("span[itemprop=name]").first();
		if (titleEL != null){
			if (titleEL.ownText().trim().isEmpty() == false){
				item.setTitleName(titleEL.ownText());
				item.setProgramName(item.getTitleName());
			}
		}		
		epgFirstPageInfos.add(item);		
		return bRet;
	}
	
	private void getTVColumnImgInfo(Document doc , TvMaoEpgFirstPageInfo epgInfo, List<TvMaoContentImg> contentImgList) {
		Element imgEl  = doc.select("div.page-content div#mainpic>img").first();
		if (imgEl != null){
			TvMaoContentImg item = new TvMaoContentImg();		
			item.setImgType("5");
			item.setImgUrl(imgEl.attr("src"));	
			item.setContentUuid(epgInfo.getContentUuid() );
			String contentImgIDSource = item.getContentUuid() + item.getImgUrl();
			item.setContentImgId(myUuid(contentImgIDSource.getBytes()));	
			contentImgList.add(item);
		}
	}
	private void getMovieImgInfo(Document doc , TvMaoEpgFirstPageInfo epgInfo, List<TvMaoContentImg> contentImgList) {
		 getDramaImgInfo( doc, epgInfo, contentImgList);
	}
	
	private void getDramaImgInfo(Document doc , TvMaoEpgFirstPageInfo epgInfo, List<TvMaoContentImg> contentImgList) {
		Element imgEl  = doc.select("div.page-content div.article-wrap div.section-wrap section div.clear div.abstract-wrap div.lt div#mainpic a img").first();
		if (imgEl != null){
			TvMaoContentImg item = new TvMaoContentImg();		
			item.setImgType("5");
			item.setImgUrl(imgEl.attr("src"));	
			item.setContentUuid(epgInfo.getContentUuid() );
			String contentImgIDSource = item.getContentUuid() + item.getImgUrl();
			item.setContentImgId(myUuid(contentImgIDSource.getBytes()));	
			contentImgList.add(item);
		}
	}
	
	public static String getUrlByNextTask( TvMaoEpgFirstPageInfo item, TaskUriName taskName){
		//外部判断类型
		String url="";
		if (item.getProgramFirstPageAbsoluteUrl()!=null &&item.getProgramFirstPageAbsoluteUrl().isEmpty()==false){
			String uriPart = taskUriMap.get(taskName);		 
			if (uriPart != null && uriPart.isEmpty()==false){					
				url = item.getProgramFirstPageAbsoluteUrl() + uriPart; 
			}		 		
		}else{
		//TODO: 如果getProgramFirstPageAbsoluteUrl empty 考虑使用其他url进行处理
		}
		return url;
	}
	
	private void getDramaInfo(Document doc , TvMaoEpgFirstPageInfo item) {
		Element article  = doc.select("body>div.page-content>div.article-wrap>div.section-wrap>section>article").first();
		if (article != null){
			item.setDescription(article.ownText());
		}
		getNumInfo( doc ,  item);		
		Elements trs = doc.select("body>div.page-content>div.article-wrap>div.section-wrap>section>div table tr");
		for (Element tr : trs) {			
			Elements tds = tr.select("td");
			for (Element td : tds) {		
				String strFieldName = td.ownText().trim();
				if (strFieldName.equals("集数：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Element aEL = nextTd.select("a").first();
						if (aEL !=null){
							item.setEpisodeTotal ( aEL.attr("content").replace("集","").trim());
							//FIXME: 需要调试确认
							//if (item.getTotalSet() == null || item.getTotalSet().isEmpty() || item.getTotalSet().equals( "0")){
							//	把原来 current和totalset小的那个当currentset
							if (!item.getCurrentSet().isEmpty()){
								try{
									int currentSet = Integer.parseInt(item.getCurrentSet());
									//set > totalset
									if (!item.getTotalSet().isEmpty()){
										int totalSet = Integer.parseInt(item.getTotalSet());						
										if (currentSet > totalSet){
											item.setCurrentSet(String.valueOf(totalSet));										
										}
									}
								}catch (NumberFormatException e){					
								}			
							}else{
								if (!item.getTotalSet().isEmpty()){
									item.setCurrentSet(item.getTotalSet());					
								}
							}
							item.setTotalSet(item.getEpisodeTotal());
							//}
						}
					}						
				}else if (strFieldName.equals("导演：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setDirectors (strTmp.substring(0,strTmp.length()-1));
						}
					}								
				}else if (strFieldName.equals("编剧：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){							
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setWriters (strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("主演：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){							
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setLeadingRoles ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("地区：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setProgramArea (strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("语言：")){						
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setLang ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("年份：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setYear ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("类别：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setTags(strTmp.substring(0,strTmp.length()-1));
						}
					}
				}
			}				 
		}					
	}
	 
	private void getMovieInfo(Document doc , TvMaoEpgFirstPageInfo item) {
		Element article  = doc.select("body>div.page-content>div.article-wrap>div.section-wrap>section>article").first();
		if (article != null){
			item.setDescription(article.ownText());		
		}	
		getNumInfo( doc ,  item);
		
		Elements trs = doc.select("body>div.page-content>div.article-wrap>div.section-wrap>section>div table tr");
		for (Element tr : trs) {			
			Elements tds = tr.select("td");
			for (Element td : tds) {		
				String strFieldName = td.ownText().trim();
				if (strFieldName.equals("集数：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Element aEL = nextTd.select("a").first();
						if (aEL !=null){
							item.setEpisodeTotal (aEL.attr("content").replace("集","").trim());
						}
					}						
				}else if (strFieldName.equals("导演：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
							//	strTmp+= child.ownText().trim()+"/";
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setDirectors( strTmp.substring(0,strTmp.length()-1));
						}
					}								
				}else if (strFieldName.equals("编剧：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setWriters( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("主演：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setLeadingRoles ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("地区：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setProgramArea( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("语言：")){						
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setLang ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("年份：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setYear ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}else if (strFieldName.equals("类别：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setTags ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}
			}				 
		}					
	}

	private void getTVColumnInfo(Document doc , TvMaoEpgFirstPageInfo item) {		 	
		getNumInfo( doc ,  item);
		Elements trs = doc.select("body>div.page-content>div.article-wrap>div.section-wrap>section>div table tr");
		for (Element tr : trs) {			
			Elements tds = tr.select("td");
			for (Element td : tds) {		
				String strFieldName = td.ownText().trim();
				if (strFieldName.equals("首播频道：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setFirstPlayChannel ( strTmp.substring(0,strTmp.length()-1));
						}
					}								
				}else if (strFieldName.equals("主持人：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								if (child.tagName().equals("a")){
									strTmp+= child.ownText().trim() +"-"+ getScoreId(child.attr("href").trim()) +"/";
								}else{
									strTmp+= child.ownText().trim()+"/";
								}
							}
						}
						if (strTmp.equals("") == false){
							item.setPresenters( strTmp.substring(0,strTmp.length()-1));
						}
					}			
				}else if (strFieldName.equals("类别：")){
					Element nextTd = td.nextElementSibling();
					if (nextTd != null){
						Elements childELs = nextTd.children();
						String strTmp = "";
						for (Element child : childELs) {		
							if (child.ownText().trim().indexOf("更多" ) ==-1 && child.ownText().trim().isEmpty() == false){
								strTmp+= child.ownText().trim()+"/";
							}
						}
						if (strTmp.equals("") == false){
							item.setTags ( strTmp.substring(0,strTmp.length()-1));
						}
					}
				}
			}				 
		}					
	}
	private void getNumInfo(Document doc , TvMaoEpgFirstPageInfo item){
		Element scoreEl  = doc.select("div.page-content span.score").first();
		if (scoreEl != null){						
			Float fScore = Float.parseFloat(scoreEl.text().replaceAll("\\s", "")) * 10;
			item.setScore(String.valueOf(fScore));
		}
		Element heatEL  = doc.select("div.page-content span.gray").first();
		if (heatEL != null){					
			int heatDegree = 0;
			Pattern p = Pattern.compile("(\\d+)(万)");
			Matcher matcher = p.matcher(heatEL.ownText().trim());
			if(matcher.find()){
				heatDegree = Integer.parseInt(matcher.group(1));
				if (matcher.group(2).equals("万")){
					heatDegree *= 10000;
				}
			}else{
				p = Pattern.compile("(\\d+)次");
				matcher = p.matcher(heatEL.ownText().trim());
				if(matcher.find()){
					heatDegree = Integer.parseInt(matcher.group(1));				
				}
			}
			item.setHeatDegree(String.valueOf(heatDegree));
		}		
	}

	public static void main(String[] args) {		 
		TvMaoEpgFirstPageInfoExtractor extractor = new TvMaoEpgFirstPageInfoExtractor();	 
		TvMaoEpgInfo epgInfo=new TvMaoEpgInfo ();		
	   List<TvMaoContentImg> contentImgList = new ArrayList<TvMaoContentImg>();
		 List<TvMaoEpgFirstPageInfo> epgFirstPageInfos = new ArrayList<TvMaoEpgFirstPageInfo>();		
		String[] charset = new String[1];
		epgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/drama/V1YsbQ==");//电视剧
		epgInfo.setType("电视剧");
		epgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/movie/KyweMGg=");//电影
		epgInfo.setType("电影");
	epgInfo.setProgramFirstPageAbsoluteUrl("http://www.tvmao.com/tvcolumn/M2tVVQ==");//栏目
	 epgInfo.setType("栏目");
		byte[] byHtml = extractor.download(epgInfo.getProgramFirstPageAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		 
		extractor. getEpgFirstPageInfo(strHtml,  charset[0],   epgInfo,   epgFirstPageInfos, contentImgList) ;
	}
}
