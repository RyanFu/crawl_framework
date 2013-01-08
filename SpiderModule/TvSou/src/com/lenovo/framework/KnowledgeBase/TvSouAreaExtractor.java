package com.lenovo.framework.KnowledgeBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

import com.lenovo.framework.KnowledgeBase.bean.TvSouAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvSouBaseExtractor;

public class TvSouAreaExtractor extends  TvSouBaseExtractor{ 

	public  Boolean getAreaPage(String html, String charset, String parantUrl, List<TvSouAreaPageUrl> areaPagelist) {
		Boolean bRet = true;		
		//cctv特殊处理
		TvSouAreaPageUrl self = new TvSouAreaPageUrl();
		self.setUrl(parantUrl);
		self.setRelUrl(parantUrl);
		self.setAreaName("CCTV");
		self.setLevel(1);		 
		self.setFindTime(System.currentTimeMillis());
		self.setTitle("CCTV");
		self.setAbsoluteUrl(parantUrl);
		areaPagelist.add(self);
		Document doc = Jsoup.parse(html);		
		//Element  parant = doc.select("center").first();
		//parant = parant.append(html);
		try {
			if (doc != null) {
				Elements areas = doc	.select("td.listtop a");
				for (Element src : areas) {
					/*生活 : http://epg.tvsou.com/programds/84_84_W3.htm
					房产 : http://www.tvsou.com/newsfl/fc.htm
					内蒙古 : /program/TV_34/Channel_56/W3.htm */
					Pattern p = Pattern.compile("^/.*?\\.htm$"); 
					Matcher matcher = p.matcher(src.attr("href").trim());
					if(matcher.find()){
						TvSouAreaPageUrl item = new TvSouAreaPageUrl();
						item.setUrl( src.attr("href"));			
						item.setAreaName( src.ownText());
						item.setLevel(1);		
						item.setRelUrl(parantUrl);
						item.setFindTime(System.currentTimeMillis());
						item.setTitle(src.attr("title"));
						item.setAbsoluteUrl(mergeUrl(item.getRelUrl(), item.getUrl()));					
						areaPagelist.add(item);						
					}
				}
			} 
		} catch (Exception e) {
			bRet=false;
		}
		return bRet;
	}
	
	public static void main(String[] args) {
		TvSouAreaExtractor extractor = new TvSouAreaExtractor();
		String rootUrl = "http://epg.tvsou.com/head_area.js";
		String parantUrl ="http://epg.tvsou.com/programys/TV_1/Channel_1/W1.htm";
		List<TvSouAreaPageUrl> areaPagelist = new  ArrayList<TvSouAreaPageUrl>();		
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(rootUrl, charset);
		String html = null;
		try {
			charset[0] = "gb2312";
			html = new String(byHtml,  charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		Context cx = ContextFactory.getGlobal().enterContext();	
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(Context.VERSION_1_5);
		Global global = Main.getGlobal();
		global.init(cx);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			Main.setOut(ps);
			html = html.replace("document.writeln", "print");
			Main.setIn(new ByteArrayInputStream(html.getBytes()));
			Main.processSource(cx, null);			
			html = baos.toString("UTF-8");	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Context.exit();
		}	
		if (	extractor.getAreaPage(html, charset[0], parantUrl, areaPagelist)){
			for (TvSouAreaPageUrl item : areaPagelist){
				System.out.println(item.getAreaName() + " : " + item.getAbsoluteUrl());
			}
		}		
	}	
	
}
