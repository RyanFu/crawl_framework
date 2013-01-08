package com.lenovo.framework.KnowledgeBase.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
 
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
 
public class TvSouBaseExtractor {
	
	public boolean writeStringToFile(String fileName, String content, String enc) {
		File file = new File(fileName);
		try {
			if (file.isFile()) {
				file.deleteOnExit();
				file = new File(file.getAbsolutePath());
			}
			OutputStreamWriter os = null;
			if (enc == null || enc.length() == 0) {
				os = new OutputStreamWriter(new FileOutputStream(file));
			} else {
				os = new OutputStreamWriter(new FileOutputStream(file), enc);
			}
			os.write(content);
			os.close();		
		} catch (Exception e) {
			e.printStackTrace();
		//	return false;
		}
		return true;
	}
	public byte[] download(String strUrl, String[] result) {
		byte[] body = null;
 
		try {
			HttpClient client = new HttpClient();
			client.getHostConfiguration().setProxy("10.99.60.91", 8080);
			GetMethod get = new GetMethod(strUrl);
			client.executeMethod(get);
			body = get.getResponseBody();
			result[0] = get.getResponseCharSet();
			get.releaseConnection();		
		} catch (Exception e) {
			e.printStackTrace();
		}    	
		try {
			String strHtml = new String(body, result[0]);
			writeStringToFile("d:\\tmp.html", strHtml, result[0]);
		} catch (UnsupportedEncodingException e) {				
		}			
	 
		return body;
	}

	public String mergeUrl(String strBaseUrl, String strRedirectUrl) {
		URL baseUrl;
		URL destUrl;
		try {
			baseUrl = new URL(strBaseUrl);
			destUrl = new URL(baseUrl, strRedirectUrl);
		} catch (MalformedURLException e1) {
			return "";
		}
		return destUrl.toString();
	}
}
