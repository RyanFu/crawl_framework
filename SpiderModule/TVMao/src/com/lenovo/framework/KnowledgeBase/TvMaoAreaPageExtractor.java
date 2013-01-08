package com.lenovo.framework.KnowledgeBase;

//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.KnowledgeBase.bean.TvMaoAreaPageUrl;
import com.lenovo.framework.KnowledgeBase.common.TvMaoBaseExtractor;

public class TvMaoAreaPageExtractor extends  TvMaoBaseExtractor{ 

	public Boolean getAreaPage(String strHtml, String charset, String strParantUrl, List<TvMaoAreaPageUrl> areaPagelist) {
		Boolean bRet = true;
		Document doc = Jsoup.parse(strHtml);
		try {
			if (doc != null) {
				Elements areas = doc	.select("body>div.clear>table>tbody>tr>td>a");
				for (Element src : areas) {
					TvMaoAreaPageUrl item = new TvMaoAreaPageUrl();
					item.setUrl( src.attr("href"));//
					item.setAreaName( src.ownText());
					item.setLevel( 2);
					item.setRelUrl( strParantUrl);
					item.setFindTime( System.currentTimeMillis());
					item.setTitle( src.attr("title"));
					item.setAbsoluteUrl ( mergeUrl(item.getRelUrl(), item.getUrl()));
					areaPagelist.add(item);
				}
			} else {
				bRet = false;
			}
		} catch (Exception e) {
			bRet=false;//e.printStackTrace();
		}
		return bRet;
	}	

	public static void main(String[] args) {	
		/*{
			TvMaoAreaPageExtractor extractor = new TvMaoAreaPageExtractor();
			String url ="http://img.tvmao.com/stills/drama/11/218/b/580x386_2.jpg";
			String[] charset = new String[1];
			byte[] byHtml = extractor.download(url, charset);
			RandomAccessFile file = null; 
			try
			{	
				file = new RandomAccessFile("d:/x.jpg", "rw");	
				file.write(byHtml);		
			}
			catch(java.io.IOException ex)
			{ 			
				ex.printStackTrace();			
			}finally{
				try {
					file.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
			}					
		}*/
		
		
		TvMaoAreaPageExtractor extractor = new TvMaoAreaPageExtractor();
		String strRootUrl = "http://www.tvmao.com/xiaoi/";
		java.util.List<TvMaoAreaPageUrl> areaPagelist = new  ArrayList<TvMaoAreaPageUrl>();		
		String[] charset = new String[1];
		byte[] byHtml = extractor.download(strRootUrl, charset);
		String strHtml = null;
		try {
			strHtml = new String(byHtml, charset[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		extractor.getAreaPage(strHtml, charset[0],strRootUrl,areaPagelist);
	}	
}
