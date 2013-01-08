package com.lenovo.framework.KnowledgeBase.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-11 下午3:12:29
 * @version : V1.0
 */
public class Downloader {

    private Integer timeoutMillis = 1000 * 10;

    /**
     * 
     * @param ip
     * @param port
     */
    public void setProxy(String ip, String port) {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", ip);
        System.getProperties().put("proxyPort", port);
    }

    /**
     * 
     * @param timeoutMillis
     */
    public void setTimeoutMillis(Integer timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * downloader url
     * 
     * @param url
     * @return
     */
    public Document download(String url) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), this.timeoutMillis);
            document = Jsoup.parse(document.outerHtml(), url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException sctError) {
            sctError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return document;
    }
}
