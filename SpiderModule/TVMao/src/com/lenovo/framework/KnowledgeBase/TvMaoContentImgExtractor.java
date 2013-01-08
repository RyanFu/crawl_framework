package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoContentImg;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoContentImgExtractor extends  TvMaoBaseExtractor{

	public Boolean getContentImg(String strHtml, String strCharset,	TvMaoContentImg contentImg, 
					List<TvMaoContentImg> contentImgList) {
		Boolean ret = true;			
		try {
			Document doc = Jsoup.parse(strHtml, contentImg.getMainUrl());	 			
			if (contentImg.getFromType().equals("DRAMA_PICTURE")){			
				getDramaPicture( doc ,  contentImg ,contentImgList);
			}else if  (contentImg.getFromType().equals("MOVIE_PICTURE")){			
				getMoviePicture( doc ,  contentImg ,contentImgList);
			}else if  (contentImg.getFromType().equals("MOVIE_POSTER")){			
				getMoviePoster( doc ,  contentImg ,contentImgList);
			}	
		} catch (Exception e) {
			ret=false; 
		}		
		return ret;
	}
	////'0未知 1：海报缩略图；2：海报正常图片；3：剧照缩略图；4：剧照正常图；
	private void getDramaPicture(Document doc,TvMaoContentImg contentImg,  
			List<TvMaoContentImg> contentImgList) {	
		getMoviePicture( doc,  contentImg,	contentImgList);		
	}

	private void getMoviePicture(Document doc, TvMaoContentImg contentImg,
			List<TvMaoContentImg> contentImgList) {
		// big
		Element bigImgEl = doc
				.select("div.page-content div.article-wrap div.section-wrap section div.jz a img[itemprop=contentURL]")
				.first();
		if (bigImgEl != null) {
			TvMaoContentImg item = (TvMaoContentImg) contentImg.clone();
			item.setImgType("4");
			item.setImgUrl(bigImgEl.absUrl("src"));
		//	item.setImgName(myUuid(getScoreId(contentImg.getMainUrl()).getBytes())+".jpg");
		//	item.setImgName(bigImgEl.attr("alt"));
			String contentImgIDSource = item.getContentUuid() + item.getImgUrl();
			item.setContentImgId(myUuid(contentImgIDSource.getBytes()));		
	//		item.setContentImgId(UUID.randomUUID().toString());
			contentImgList.add(item);
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
					TvMaoContentImg item = (TvMaoContentImg) contentImg.clone();
					item.setImgType("3");
					item.setImgUrl(imgEL.absUrl("src"));
				//	item.setImgName(myUuid(getScoreId(contentImg.getMainUrl()).getBytes())+".jpg");
				//	item.setImgName(imgEL.attr("alt"));
					String contentImgIDSource = item.getContentUuid() + item.getImgUrl();
					item.setContentImgId(myUuid(contentImgIDSource.getBytes()));	
					//item.setContentImgId(UUID.randomUUID().toString());
					contentImgList.add(item);
				}
			}
		}
	}
	private void getMoviePoster(Document doc,TvMaoContentImg contentImg,  
			List<TvMaoContentImg> contentImgList) {	
		//取张数
		Element numEL = doc.select("div.page-content div.article-wrap div.section-wrap section span#poster_num").first();
		if (numEL !=null){			
			String numString = numEL.ownText().trim();
			if (numString ==null || numString.isEmpty() || numString.equals("0")){
				return;
			}
		}
		//big
		Element bigImgEl = doc.select("div.page-content div.article-wrap div.section-wrap section div.poster>a>img").first();
		if (bigImgEl !=null){			
			TvMaoContentImg item = (TvMaoContentImg)contentImg.clone();
			item.setImgType("2");
			item.setImgUrl(bigImgEl.absUrl("src"));
	//		item.setImgName(myUuid(getScoreId(contentImg.getMainUrl()).getBytes())+".jpg");
	//		item.setImgName(bigImgEl.attr("alt"));
			String contentImgIDSource = item.getContentUuid() + item.getImgUrl();
			item.setContentImgId(myUuid(contentImgIDSource.getBytes()));	
		//	item.setContentImgId(UUID.randomUUID().toString());		
			contentImgList.add(item);
		}
	}
	
	public static void main(String[] args) {
		TvMaoContentImgExtractor extractor = new TvMaoContentImgExtractor();
		TvMaoContentImg contentImg = new TvMaoContentImg();
		contentImg.setMainUrl("http://www.tvmao.com/drama/Zy4qHTA=/pictures");
		contentImg.setFromType("DRAMA_PICTURE");
		contentImg.setContentUuid("contentUuid");		 
		
	//	contentImg.setMainUrl("http://www.tvmao.com/movie/Zi4sIi8=/posters");
		//	contentImg.setFromType("MOVIE_PICTURE");
		
		contentImg.setMainUrl("http://www.tvmao.com/movie/Ii9jLyw=/posters");	
		contentImg.setFromType("MOVIE_POSTER");
		
		 List<TvMaoContentImg> contentImgDbList = new ArrayList<TvMaoContentImg>();		
		String[] charset = new String[1];	
		byte[] byHtml = extractor.download(contentImg.getMainUrl(), charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ( extractor.getContentImg(strHtml, charset[0], contentImg, contentImgDbList)){
			for (TvMaoContentImg item :contentImgDbList){
				System.out.println(item.toString());
			}
			 
		}
	}
}
