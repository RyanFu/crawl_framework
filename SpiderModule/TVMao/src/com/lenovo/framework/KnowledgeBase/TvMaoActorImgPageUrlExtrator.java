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
import com.lenovo.framework.KnowledgeBase.bean.TvMaoPersonImg;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoActorImgPageUrlExtrator  extends  TvMaoBaseExtractor{
	public Boolean getActorImgPageUrl(String strHtml, String strCharset,TvMaoActor actor,  List<TvMaoPersonImg> personImgList) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, actor.getRefUrl());	 			
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
						TvMaoPersonImg item = new TvMaoPersonImg();						
						item.setPersonId(actor.getPersonID());					 
						item.setSourceUrl(el.absUrl("href"));
						personImgList.add(item);
					}
				}
			}else{
				for (int i=0; i<photoNum;i++) {
					//TODO:验证 只要10张
					if (i > 3){
						break;
					}
					//
					TvMaoPersonImg item = new TvMaoPersonImg();					
					item.setPersonId(actor.getPersonID());
					item.setSourceUrl(actor.getRefUrl() +  "/pictures/" + String.valueOf(i));				 
					personImgList.add(item);
				}
			}
		 
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	
	public static void main(String[] args) {
		TvMaoActorImgPageUrlExtrator extractor = new TvMaoActorImgPageUrlExtrator();	
		 TvMaoActor actor = new TvMaoActor();
		 actor.setRefUrl("http://www.tvmao.com/star/dQ==");
		 actor.setPersonID("personid");	
	//	 parentUrl="http://www.tvmao.com/star/dQ==/pictures";
	
		 List<TvMaoPersonImg> personImgList  =new ArrayList<TvMaoPersonImg> ();
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(actor.getRefUrl()+"/pictures", charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getActorImgPageUrl(strHtml, charset[0], actor, personImgList)){
			System.out.println(personImgList.toString());
			for (TvMaoPersonImg item: personImgList){
				System.out.println(item.getImgUrl());
			}
		}
	}
}
