package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoActor;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoActorWorksExtrator  extends  TvMaoBaseExtractor{
	public Boolean getActorWorks(String strHtml, String strCharset, TvMaoActor actor) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, actor.getMainUrl());	 			
			Elements els = doc.select("div.page-content section table.picsblock tbody em a");			 
			String works="";
			for (Element el :els){
				works += el.ownText().trim() + "/";
				//String url = el.absUrl("href");
			}
			if (works.isEmpty()==false){
				works = works.substring(0, works.length()-1);
			}
			actor.setWorks(works);	
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	public static void main(String[] args) {
		TvMaoActorWorksExtrator extractor = new TvMaoActorWorksExtrator();
		TvMaoActor actor = new TvMaoActor();
		actor.setRefUrl("http://www.tvmao.com/star/TyAy");
		actor.setMainUrl(actor.getRefUrl()+"/telemovie");
		actor.setName("hanliang");
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(actor.getMainUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor.getActorWorks(strHtml, charset[0], actor)) {
			System.out.println(actor.toString());
		}
	}
}
