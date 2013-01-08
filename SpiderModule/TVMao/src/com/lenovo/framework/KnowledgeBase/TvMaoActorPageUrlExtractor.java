package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoActor;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoActorPageUrlExtractor  extends  TvMaoBaseExtractor{
	public Boolean getActorPageUrl(String strHtml, String strCharset,String parentUrl,  String type,List<TvMaoActor> acotrList, TvMaoEpgFirstPageInfo epgFirstPageInfo) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, parentUrl);	 			
			if (type.equals("电视剧")){
				getDramaActorUrl( doc,  parentUrl, acotrList, epgFirstPageInfo) ;
			}else if (type.equals("电影")){
				getMovieActorUrl( doc,  parentUrl, acotrList, epgFirstPageInfo) ;	
			}else{//缺省就是 栏目
				getTVColumnActorUrl( doc,  parentUrl,  acotrList, epgFirstPageInfo) ;	
			}	
		} catch (Exception e) {
			ret=false; 
		}
		if (ret){
			adjustEpgActorInfo(acotrList, epgFirstPageInfo);
		}
		return ret;
	}
	public void adjustEpgActorInfo( List<TvMaoActor> acotrList, TvMaoEpgFirstPageInfo epgFirstPageInfo) {	
		String strActorTmp="";
		for (TvMaoActor item:acotrList ){
			if (item.getRefUrl()!=null && item.getRefUrl().isEmpty()==false){
				strActorTmp += item.getName()+"-"+item.getSourceID()+"/";
			}else{
				strActorTmp += item.getName() +"/";
			}
		}
		if (strActorTmp.length()>0){
			char x =strActorTmp.charAt(strActorTmp.length()-1);
			if (x=='/'){
				strActorTmp = strActorTmp.substring(0,strActorTmp.length()-1);
			}		
		}
		if (strActorTmp.isEmpty()==false){
			epgFirstPageInfo.setLeadingRoles(strActorTmp);
		}
	}
	public void getDramaActorUrl(Document doc, String parentUrl,  List<TvMaoActor> acotrList, TvMaoEpgFirstPageInfo epgFirstPageInfo) {	
		Element row = doc.select("div.page-content section article div.intro").first();	
		if (row != null){
			List<Node> nodes = row.childNodes();
			for (Node onlyNextEL : nodes){				 
				if (onlyNextEL!=null){							
					if (onlyNextEL.nodeName().equals("#text") && onlyNextEL.nodeName().trim().isEmpty()==false){					
						if (onlyNextEL.outerHtml().trim().startsWith("饰")==false  && onlyNextEL.outerHtml().trim().isEmpty()==false ){
							TvMaoActor item = new TvMaoActor();	
							int pos=onlyNextEL.outerHtml().indexOf("饰");
							if (pos > 0){
								item.setName(onlyNextEL.outerHtml().substring(0,pos));				 
						//		item.setMainUrl(onlyNextEL.absUrl("href"));
							//	item.setRefUrl(item.getMainUrl());
								String pidSource = getScoreId(item.getMainUrl());
								item.setSourceID(pidSource);	
							//	item.setPersonID(UUID.randomUUID().toString());								
								//由于没有下一步url 故不用进行下一步提取
								acotrList.add(item);		 
							}else{
								/*虎头要塞牺牲演员表
								李君峰
								王韦智
								曹卫宇*/								
								item.setName(onlyNextEL.outerHtml());				 
								//		item.setMainUrl(onlyNextEL.absUrl("href"));
									//	item.setRefUrl(item.getMainUrl());
								String pidSource = getScoreId(item.getMainUrl());
								item.setSourceID(pidSource);	
							//	item.setPersonID(UUID.randomUUID().toString());								
								//由于没有下一步url 故不用进行下一步提取
								acotrList.add(item);		 
							}
						}
					}else if (onlyNextEL.nodeName().equals("a")){			
						TvMaoActor item = new TvMaoActor();						
					//	item.setName(el.ownText());				 
						item.setMainUrl(onlyNextEL.absUrl("href"));
						item.setRefUrl(item.getMainUrl());
						String pidSource = getScoreId(item.getMainUrl());
						item.setSourceID(pidSource);	
				//		item.setPersonID(UUID.randomUUID().toString());		
						
						List<Node> secChilds = onlyNextEL.childNodes();
						for (Node secChild : secChilds){
							if (secChild.nodeName().equals("#text")){			
								item.setName(secChild.outerHtml());		
							}
						}
						acotrList.add(item);		 
					}else if (onlyNextEL.nodeName().equals("b")){				
						break;
					}
				}
			}
		}
	}
	public void getMovieActorUrl(Document doc, String parentUrl,   List<TvMaoActor> acotrList, TvMaoEpgFirstPageInfo epgFirstPageInfo) {
		Element row = doc.select("div.page-content div.article-wrap div.section-wrap section article").first();	
		if (row != null){
			List<Node> nodes = row.childNodes();
			for (Node onlyNextEL : nodes){				 
				if (onlyNextEL!=null){							
					if (onlyNextEL.nodeName().equals("#text") ){					
						if (onlyNextEL.outerHtml().trim().startsWith("饰")==false && onlyNextEL.outerHtml().trim().isEmpty()==false){
							TvMaoActor item = new TvMaoActor();	
							int pos=onlyNextEL.outerHtml().indexOf("饰");
							if (pos > 0){
								item.setName(onlyNextEL.outerHtml().substring(0,pos));				 
						//		item.setMainUrl(onlyNextEL.absUrl("href"));
							//	item.setRefUrl(item.getMainUrl());
								String pidSource = getScoreId(item.getMainUrl());
								item.setSourceID(pidSource);	
							//	item.setPersonID(UUID.randomUUID().toString());								
								//由于没有下一步url 故不用进行下一步提取
								acotrList.add(item);		 
							}else{
								/*虎头要塞牺牲演员表
								李君峰
								王韦智
								曹卫宇*/								
								item.setName(onlyNextEL.outerHtml());				 
								//		item.setMainUrl(onlyNextEL.absUrl("href"));
									//	item.setRefUrl(item.getMainUrl());
								String pidSource = getScoreId(item.getMainUrl());
								item.setSourceID(pidSource);	
							//	item.setPersonID(UUID.randomUUID().toString());								
								//由于没有下一步url 故不用进行下一步提取
								acotrList.add(item);		 
							}
						}
					}else if (onlyNextEL.nodeName().equals("a")){			
						TvMaoActor item = new TvMaoActor();						
					//	item.setName(el.ownText());				 
						item.setMainUrl(onlyNextEL.absUrl("href"));
						item.setRefUrl(item.getMainUrl());
						String pidSource = getScoreId(item.getMainUrl());
						item.setSourceID(pidSource);	
				//		item.setPersonID(UUID.randomUUID().toString());		
						
						List<Node> secChilds = onlyNextEL.childNodes();
						for (Node secChild : secChilds){
							if (secChild.nodeName().equals("#text")){			
								item.setName(secChild.outerHtml());		
							}
						}
						acotrList.add(item);		 
					}else if (onlyNextEL.nodeName().equals("b")){				
						break;
					}
				}
			}
		}
	}
	public void getTVColumnActorUrl(Document doc, String parentUrl,   List<TvMaoActor> acotrList, TvMaoEpgFirstPageInfo epgFirstPageInfo) {
		Elements rows = doc.select("div.page-content article ul.picsblock li em a[itemprop=actors]");
		for (Element el : rows) {			 
				TvMaoActor item = new TvMaoActor();						
				item.setName(el.ownText());				 
				item.setMainUrl(el.absUrl("href"));	
				item.setRefUrl(item.getMainUrl());
				String pidSource = getScoreId(item.getMainUrl());
				item.setSourceID(pidSource);			
				acotrList.add(item);	
		}
	}
	
	public static void main(String[] args) {
		TvMaoActorPageUrlExtractor extractor = new TvMaoActorPageUrlExtractor();
		 TvMaoEpgFirstPageInfo epgFirstPageInfo = new   TvMaoEpgFirstPageInfo ();
		 String parentUrl="http://www.tvmao.com/drama/YicwIC4=/actors";			 
		 String type = "电视剧"; 
	//	 parentUrl="http://www.tvmao.com/tvcolumn/ViA3/actors";
		// type = "栏目";		 
		// parentUrl=" http://www.tvmao.com/movie/YGBrIFtp/actors";
	//	 type = "电影";		
		 
		 List<TvMaoActor> acotrList= new ArrayList<TvMaoActor>();
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(parentUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getActorPageUrl(strHtml, charset[0], parentUrl, type, acotrList,epgFirstPageInfo)){			
			for (TvMaoActor item: acotrList){
				System.out.println(item.getName() + " : "+ item.getRefUrl());
			}
		}
	}
}
