package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

 
import com.lenovo.framework.KnowledgeBase.bean.TvSouEpgInfo;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouEpgFirstPageInfoExtractor extends  TvSouBaseExtractor{	
	
	public Boolean getEpgFirstPageInfo(String html, String charset, TvSouEpgInfo item) {
		Boolean bRet = true;
		try{
			Document doc = Jsoup.parse(html);		
			if (item.getType().equals("电视剧")){//item.getProgramFirstPageAbsoluteUrl()
				 getDramaInfo( doc ,  item);
			}else if (item.getType().equals("电影")){     //item.getDramaAbsoluteUrl()
				 getMovieInfo( doc ,  item);
			}else{                                                     //item.getProgramFirstPageAbsoluteUrl()
				 getTVColumnInfo( doc , item);
			}
		}catch (Exception e){
			bRet = false;
		}
		return bRet;
	}
	
	private void getDramaInfo(Document doc , TvSouEpgInfo item) {		
		Elements tables = doc.select("div#content2>div#con_l>div#con_l_r>table");
		if (tables==null || tables.isEmpty()) return;		
		//table1	 
		Elements tds = tables.get(0).select("td");
		Element td = null; 
		for (int i=0; i<tds.size(); ){
			td = tds.get(i++);
			if (td !=null && td.text().indexOf("导演：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setDirectors(td.text().trim());
				}
			}else if 	(td !=null && td.text().indexOf("地区：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setProgramArea(td.text().trim());
				}
			}else if 	(td !=null && td.text().indexOf("主演：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setLeadingRoles(td.text().trim());
				}
			}else if 	(td !=null && td.text().indexOf("类型：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setTags(td.text().trim());
				}
			}		 
		}	 
		//table2 
		if (item.getDescription() == null || item.getDescription().equals("")){
			if (tables.size() >= 2){		
				Element descTd = tables.get(1).select("td.m1_txt").first();
				if (descTd != null){
					item.setDescription(descTd.ownText());
				}
			}
		}	
		//score
	/*	Element tableScore = doc.select("div#content2>div#con_l>div#con_l_r>div#ratingdiv1").first();
		if (tableScore != null){
			Element tdNum1 = tableScore.select("td.m1_num1").first();  tableScore.select("td[class=m1_num1]")
			if (tdNum1 != null){
				String score = tdNum1.ownText().trim() ;
				Element tdNum2 = tableScore.select("td.m1_num2").first();
				if (tdNum2 != null){
					score += tdNum2.ownText().trim() ;
				}
				Element tdImgs = tableScore.select("td[valign=top]").first();
				if (tdImgs !=null){
					Elements imgs = tdImgs.select("img");
					score += "/" + String.valueOf(imgs.size());
				}
				item.setScore(score);
			}
		}*/
	}
	 
	private void getMovieInfo(Document doc , TvSouEpgInfo item) {
		getDramaInfo( doc ,  item) ;	
	}

	private void getTVColumnInfo(Document doc , TvSouEpgInfo item) {
		Element tbody = doc.select("tbody:matches(频道.*?主持.*?评分)").last();
		if (tbody==null) return;		
		//tbody	 
		Elements tds = tbody.select("td");
		Element td = null; 
		for (int i=0; i<tds.size(); ){
			td = tds.get(i++);
			if (td !=null && td.text().indexOf("频道：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setFirstPlayChannel(td.text().trim());
				}
			}else if 	(td !=null && td.text().indexOf("主持：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					item.setPresenters(td.text().trim());
				}
			}else if 	(td !=null && td.text().indexOf("评分：")!=-1){
				td =tds.get(i++);
				if (td !=null){
					//item.setLeadingRoles(td.text().trim());
					Elements totalStarEls = td.select("img");
					Elements scoreStarEls = td.select("img[src$=star.gif]");
					item.setScore(String.valueOf(scoreStarEls.size()) + "/" + String.valueOf(totalStarEls.size()));
				}
			}	 
		}
		//desc
		if (item.getDescription() == null || item.getDescription().equals("")){
			Element tableDesc = tbody.parent().nextElementSibling();
			if (tableDesc!=null){		
				Element descTd = tableDesc.select("td").first();
				if (descTd != null){
					item.setDescription(descTd.ownText()); 
				}
			}
		}	
		
	}
	
	public static void main(String[] args) {		
		TvSouEpgInfo epgInfo=new TvSouEpgInfo ();		
		epgInfo.setProgramFirstPageAbsoluteUrl("http://jq.tvsou.com/introhtml/853/index_85332.htm");//电视剧
		epgInfo.setType("电视剧");	
		//epgInfo.setProgramFirstPageAbsoluteUrl("http://jq.tvsou.com/introhtml/363/index_36332.htm");//电影
		//epgInfo.setType("电影");	
	//	epgInfo.setProgramFirstPageAbsoluteUrl("http://jq.tvsou.com/introhtml/645/index_64534.htm");//栏目
		//epgInfo.setType("栏目");		
		TvSouEpgFirstPageInfoExtractor extractor = new TvSouEpgFirstPageInfoExtractor();
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(epgInfo.getProgramFirstPageAbsoluteUrl(), charset);
		String strHtml = null;
		try {
			charset[0] = "gb2312";
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		 
		if (extractor. getEpgFirstPageInfo(strHtml,  charset[0],   epgInfo) ){
			System.out.println(epgInfo.toString());
		}
	}
	
}
