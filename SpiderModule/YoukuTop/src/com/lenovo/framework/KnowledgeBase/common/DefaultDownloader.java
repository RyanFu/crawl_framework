package com.lenovo.framework.KnowledgeBase.common;

import org.jsoup.nodes.Document;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-11 下午3:15:29
 * @version : V1.0
 */
public class DefaultDownloader {

    private static Downloader downloader;
    static {
        downloader = new Downloader();
        downloader.setProxy("10.99.20.30", "8080");
    }

    public Document getDocument(String url) {
        Document document = downloader.download(url);

        return document;
    }
}
