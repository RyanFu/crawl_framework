package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoActor;
import com.lenovo.framework.KnowledgeBase.bean.TvMaoPersonImg;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoActorExtractor  extends  TvMaoBaseExtractor{

	public Boolean getActorInfo(String strHtml, String strCharset, TvMaoActor actor,TvMaoPersonImg personImg) {
		Boolean ret = true;	
		try {
			Document doc = Jsoup.parse(strHtml, actor.getRefUrl());	 			
			Element dlEL = doc.select("div.page-content div.ometa fieldset dl").first();
			if (dlEL!=null){
				Elements els = dlEL.children();
				for (Element el :els){
					if (el.tagName().equals("dt")){
						Element ddEl = el.nextElementSibling();
						getPersonInfo( el , ddEl ,  actor);
					}				 
				}
			}
			getNumInfo( doc ,  actor);
			actor.setGender("0");
			//desc
			Element descEL = doc.select("div.page-content div.article-wrap div.section-wrap section article div").first();
			if (descEL != null){
				actor.setDescription(descEL.text());
			}
			//sourceid		//PERSON_ID	
			actor.setSourceID(getScoreId(actor.getRefUrl()));	
			String sid = "TVMAO" + actor.getSourceID();
			actor.setPersonID(myUuid(sid.getBytes()));		
			
			getActorImgInfo( doc , actor, personImg);
		} catch (Exception e) {
			ret=false; 
		}
		return ret;
	}
	private void getActorImgInfo(Document doc , TvMaoActor actor, TvMaoPersonImg item){
		Element imgEl = doc
				.select("div.page-content aside.related-aside div section.aside-section div a img[itemprop=image]")
				.first();
		if (imgEl != null) {			
			item.setImgType("5");//0:缩略图；1：原图；
			item.setImgUrl(imgEl.absUrl("src").replace("60x80.", "120x160."));		
			item.setImgDesc("");
			item.setPersonId(actor.getPersonID());
			item.setSourceUrl(actor.getRefUrl());
			String personImgId = item.getImgUrl() +item.getPersonId();
			item.setPersonImgId(myUuid(personImgId.getBytes()));	
		}
	}
	public void getPersonInfo(Element dtEl ,Element ddEl , TvMaoActor actor) {
		if (dtEl.text().equals("英文名")) {
			actor.setEnglishName(ddEl.text().trim());
		} else if (dtEl.text().equals("曾用名")) {
			actor.setAlias(ddEl.text().trim());
		} else if (dtEl.text().equals("国家或地区")) {
			actor.setCountry(ddEl.text().trim());
		} else if (dtEl.text().equals("生日")) {
			actor.setBirthday(ddEl.text().trim() + " 00:00:00");
		} else if (dtEl.text().equals("出生地")) {
			actor.setBirthLand(ddEl.text().trim());
		} else if (dtEl.text().equals("职业")) {
			actor.setProfession(ddEl.text().trim());
		} else if (dtEl.text().equals("星座")) {
			actor.setConstellation(ddEl.text().trim());
		} else if (dtEl.text().equals("血型")) {
			actor.setBloodType(ddEl.text().trim());	
		} else if (dtEl.text().equals("毕业院校")) {
			actor.setSchool(ddEl.text().trim());
		}
	}
	private void getNumInfo(Document doc , TvMaoActor actor){
		Element scoreEl  = doc.select("div.page-content span.score").first();
		if (scoreEl != null){						
			Float fScore = Float.parseFloat(scoreEl.text().replaceAll("\\s", "")) * 10;
			actor.setScore(String.valueOf(fScore));
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
			actor.setHeatDegree(String.valueOf(heatDegree));
		}		
	}

	public static void main(String[] args) {
		TvMaoActorExtractor extractor = new TvMaoActorExtractor();		
		TvMaoActor actor = new TvMaoActor();
		TvMaoPersonImg personImg=new TvMaoPersonImg();
		actor.setRefUrl("http://www.tvmao.com/star/JDhS");
		actor.setMainUrl(actor.getRefUrl()+"/detail");
		actor.setName("hanliang");
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(actor.getMainUrl(), charset);
		String html = null;
		try {
			html = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (extractor.getActorInfo(html, charset[0], actor,personImg)) {
			System.out.println(actor.toString());
		}
	}
}
