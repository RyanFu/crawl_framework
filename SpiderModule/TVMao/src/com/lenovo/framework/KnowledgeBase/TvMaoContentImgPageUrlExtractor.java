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

import com.lenovo.framework.KnowledgeBase.bean.TvMaoContentImg;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoEpgFirstPageInfo;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoContentImgPageUrlExtractor extends  TvMaoBaseExtractor{
	
	public Boolean getContentImgPageUrl(String strHtml, String strCharset,String parentUrl,  String fromType,
			TvMaoEpgFirstPageInfo epgFirstPageInfo,  List<TvMaoContentImg> contentImgList) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, parentUrl);	 			
			if (fromType.equals("DRAMA_PICTURE")){
				 getDramaPicture( doc , parentUrl, fromType,epgFirstPageInfo , contentImgList);
			}else if (fromType.equals("MOVIE_PICTURE")){
				 getMoviePicture( doc , parentUrl, fromType,epgFirstPageInfo , contentImgList);
		//	}else if (fromType.equals("MOVIE_POSTER")){
			//	 getMoviePoster( doc , parentUrl, fromType,epgFirstPageInfo , contentImgList);
			}else{//缺省就是 栏目
				// getTVColumnInfo( doc ,  item);
			}	
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	
	private void getDramaPicture(Document doc, String parentUrl, String fromType,TvMaoEpgFirstPageInfo epgFirstPageInfo ,
			List<TvMaoContentImg> contentImgList) {		 
		//张数
		int photoNum=0;
		Element numEl= doc.select("div.page-content div.article-wrap div.section-wrap section div.jz").first();
		if (numEl != null){			
			Pattern p = Pattern.compile("张数：(\\d+)");
			Matcher matcher = p.matcher(numEl.text());
			if(matcher.find()){				
				try{
					photoNum = Integer.parseInt(matcher.group(1));	
				}catch (Exception e){
					photoNum = 0;
				}
			}		
		}		
		if (photoNum ==0 ){
			Elements rows = doc.select("div.page-content div.article-wrap div.section-wrap section div.ml60 ul.picsblock li");
			for (Element row : rows) {
				Element el = row.select("a").first();
				if (el !=null){
					TvMaoContentImg item = new TvMaoContentImg();						
					item.setContentUuid(epgFirstPageInfo.getContentUuid());
					item.setFromType(fromType);
					item.setMainUrl(el.absUrl("href"));
					contentImgList.add(item);
				}
			}
		}else{
			for (int i=0; i<photoNum;i++) {
				//TODO:验证 只要10张
				if (i >3){
					break;
				}
				//
				TvMaoContentImg item = new TvMaoContentImg();						
				item.setContentUuid(epgFirstPageInfo.getContentUuid());
				item.setFromType(fromType);
				item.setMainUrl(parentUrl + "/" + String.valueOf(i));
				contentImgList.add(item);
			}
		}
		
	}
	
	private void getMoviePicture(Document doc, String parentUrl, String fromType, TvMaoEpgFirstPageInfo epgFirstPageInfo ,
			List<TvMaoContentImg> contentImgList) {	
		 getDramaPicture( doc , parentUrl, fromType,epgFirstPageInfo , contentImgList);
	}	 
 
	public static void main(String[] args) {
		TvMaoContentImgPageUrlExtractor extractor = new TvMaoContentImgPageUrlExtractor();
		 String parentUrl="http://www.tvmao.com/drama/Zy4qHTA=/pictures";	
		 TvMaoEpgFirstPageInfo epgFirstPageInfo = new TvMaoEpgFirstPageInfo ();
		 epgFirstPageInfo.setType("电视剧");
		 epgFirstPageInfo.setContentUuid("contentid");
		 String fromType = "DRAMA_PICTURE";//MOVIE_PICTURE   MOVIE_POSTER
		 
		// parentUrl="http://www.tvmao.com/star/dQ==/pictures";
		// fromType = "MOVIE_PICTURE";//MOVIE_PICTURE   MOVIE_POSTER		
		 
		 List<TvMaoContentImg> contentImgList= new ArrayList<TvMaoContentImg>();
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(parentUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getContentImgPageUrl(strHtml, charset[0], parentUrl,fromType,epgFirstPageInfo, contentImgList)){
			System.out.println(contentImgList.toString());
			for (TvMaoContentImg item: contentImgList){
				System.out.println(item.getMainUrl());
			}
		}
	}
	
}
