package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

 
import com.lenovo.framework.KnowledgeBase.bean.TvMaoPersonImg;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoActorImgExtrator extends  TvMaoBaseExtractor{
	public Boolean getActorImg(String strHtml, String strCharset,	TvMaoPersonImg personImg, List<TvMaoPersonImg> personImgList) {
			Boolean ret = true;			
			try {
				Document doc = Jsoup.parse(strHtml, personImg.getSourceUrl());	 			
				// big
				Element bigImgEl = doc
						.select("div.page-content div.article-wrap div.section-wrap section div.jz a img[itemprop=contentURL]")
						.first();
				if (bigImgEl != null) {
					TvMaoPersonImg item = (TvMaoPersonImg) personImg.clone();
					item.setImgType("1");//0:缩略图；1：原图；
					item.setImgUrl(bigImgEl.absUrl("src"));
					//item.setImgName(myUuid(getScoreId(item.getSourceUrl()).getBytes())+".jpg");
					//item.setImgName(bigImgEl.attr("alt"));
					item.setImgDesc("");
					item.setPersonId(personImg.getPersonId());
					String personImgId = item.getImgUrl() +item.getPersonId();
					item.setPersonImgId(myUuid(personImgId.getBytes()));		
				//	item.setPersonImgId(UUID.randomUUID().toString());
					personImgList.add(item);
				}
				// small
				Element liEL = doc
						.select("div.page-content div.article-wrap div.section-wrap section div.ml60 ul.picsblock li:not(.navnp)")
						.first();
				if (liEL != null) {
					Element aEL = liEL.select("a").first();
					if (aEL != null) {
						// String a1 = aEL.absUrl("href");
						Element imgEL = aEL.select("img").first();
						if (imgEL != null) {
							TvMaoPersonImg item = (TvMaoPersonImg) personImg.clone();
							item.setImgType("0");//0:缩略图；1：原图；
							item.setImgUrl(imgEL.absUrl("src"));
						//	item.setImgName(myUuid(getScoreId(item.getSourceUrl()).getBytes())+".jpg");
							//item.setImgName(imgEL.attr("alt"));
							item.setImgDesc("");
							item.setPersonId(personImg.getPersonId());
							String personImgId = item.getImgUrl() +item.getPersonId();
							item.setPersonImgId(myUuid(personImgId.getBytes()));		
						//	item.setPersonImgId(UUID.randomUUID().toString());
							personImgList.add(item);
						}
					}
				}
			} catch (Exception e) {
				ret=false; 
			}		
		return ret;
	}
	
	public static void main(String[] args) {
		TvMaoActorImgExtrator extractor = new TvMaoActorImgExtrator();
		TvMaoPersonImg personImg = new TvMaoPersonImg();
		personImg.setSourceUrl("http://www.tvmao.com/star/dQ==/pictures/4");		 
		personImg.setPersonId("pid");		 
		 List<TvMaoPersonImg> personImgList= new ArrayList<TvMaoPersonImg>();		
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(personImg.getSourceUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getActorImg(strHtml, charset[0], personImg, personImgList)){
			for (TvMaoPersonImg item :personImgList){
				System.out.println(item.toString());
			}
			 
		}
	}
}
