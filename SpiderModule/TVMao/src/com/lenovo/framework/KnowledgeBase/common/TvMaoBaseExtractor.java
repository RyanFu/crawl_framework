package com.lenovo.framework.KnowledgeBase.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class TvMaoBaseExtractor {
	
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
	//
	public  String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	private String digest(byte[] buf, String al) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(al);
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return byte2hex(md.digest(buf));
	}
	public  String md5(byte[] buf) {
		return digest(buf, "MD5");
	}	
	public  String sha1(byte[] buf) {
		return digest(buf, "SHA-1");
	}
	public  String myUuid(byte[] buf) {		 
		StringBuffer strSqlBuffer = new StringBuffer();
		strSqlBuffer.append(digest(buf, "MD5")) ;
		strSqlBuffer.insert(8, '-');
		strSqlBuffer.insert(13, '-');
		strSqlBuffer.insert(18, '-');
		strSqlBuffer.insert(23, '-');
		return strSqlBuffer.toString();
	}
	public String getScoreId(String strUrl) {
		String strPath = "";
		try {
			URL url = new URL(strUrl);
			strPath = url.getPath();
			// return byte2hex( url.getPath().getBytes());
		} catch (MalformedURLException e1) {
			return "";
		}
		return byte2hex(strPath.getBytes());
	/*	String[] info = strPath.split("/");
		if (info.length > 0) {			
		//	return byte2hex(info[info.length - 1].getBytes());
		} else {
			return "";
		}*/
	}
	//
	public static void main(String[] args){
		TvMaoBaseExtractor x = new TvMaoBaseExtractor();
		String [] result =new String[2];
		x.download("http://www.baidu.co1m", result);
	}
}
