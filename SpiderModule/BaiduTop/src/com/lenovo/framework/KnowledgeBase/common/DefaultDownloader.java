package com.lenovo.framework.KnowledgeBase.common;

import org.jsoup.nodes.Document;

public class DefaultDownloader {

    private static Downloader downloader;
    static {
        downloader = new Downloader();
//      downloader.setProxy("10.99.20.30", "8080");
        downloader.setProxy("10.99.60.91", "8080");
    }

    public Document getDocument(String url) {
        Document document = downloader.download(url);

        return document;
    }
}
