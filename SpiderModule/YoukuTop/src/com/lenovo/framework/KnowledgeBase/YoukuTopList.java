package com.lenovo.framework.KnowledgeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;
import com.lenovo.framework.KnowledgeBase.bean.Category;
import com.lenovo.framework.KnowledgeBase.common.DefaultDownloader;
import com.lenovo.framework.KnowledgeBase.common.HeadTag;

/**
 * @Description : 处理 Youku 列表
 * @author : ligang8@lenovo.com
 * @date : 2012-9-7 下午1:50:21
 * @version : V1.0
 */
public class YoukuTopList implements IHandle {

    private static List<String> crawlUrls;
    static {
        crawlUrls = new ArrayList<String>();
        crawlUrls.add("http://index.youku.com/protop/0");
        crawlUrls.add("http://index.youku.com/protop/1");
        crawlUrls.add("http://index.youku.com/protop/2");
        crawlUrls.add("http://index.youku.com/protop/3");
    }

    @Override
    public List<IMessage> handle(IMessage in, IBridge bridge) {
        String content = null;
        if (null == in || (content = in.GetBody()) == null)
            return null;
        if (null == bridge)
            return null;
        Document document = Jsoup.parse(content);
        List<Category> categorys = this.extractCategory(document);
        for (String url : crawlUrls) {
            Category category = this.checkUrl(url, categorys);
            IMessage message = MsgFactory.getDefaultDownMsg(bridge,
                    YoukuTop.module, url,
                    "com.lenovo.framework.KnowledgeBase.YoukuTopContent");
            if (category != null) {
                message.SetHeader(HeadTag.URLNAME, category.getName());
            } else {
            	bridge.LogInfo("cann't find url [ " + url + " ] from first page");
                message.SetHeader(HeadTag.URLNAME, "unknow");
            }
            message.SetHeader("Version", in.GetHeader("Version"));
            bridge.SendMsg(message);
        }
        return null;
    }

    /**
     * 从指定的文档中， 抽取url及名称
     * 
     * @param document
     * @return
     */
    private List<Category> extractCategory(Document document) {
        if (null == document)
            return null;
        List<Category> categorys = new ArrayList<Category>();
        Elements els = document.select("a[onclick~=(?i)window.location.href]");
        for (Element el : els) {
            String url = this.getCategoryUrl(el.outerHtml());
            if (null == url)
                continue;
            url = url.trim();
            Category category = new Category(url, el.text());
//            bridge.LogInfo("add Category \t" + category);
            categorys.add(category);
        }
        return categorys;
    }

    /**
     * 提取url链接
     * 
     * @param elementStr
     * @return
     */
    private String getCategoryUrl(String elementStr) {
        String ret = null;
        if (null == elementStr) {
            return ret;
        }
        String regex = "encodeURIComponent\\('(.+)'\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(elementStr);
        while (matcher.find()) {
            ret = matcher.group(1);
        }
        return ret;
    }

    /**
     * 判断抓取的url，是否存在于队列中。
     * 
     * @param url
     * @param categorys
     * @return
     */
    private Category checkUrl(String url, List<Category> categorys) {
        for (Category category : categorys) {
            if (url.equals(category.getUrl()))
                return category;
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DefaultDownloader youkuDown = new DefaultDownloader();
        Document document = youkuDown
                .getDocument("http://index.youku.com/vr_top/cate_search.html");
        YoukuTopList youkuTopList = new YoukuTopList();
        List<Category> categorys = youkuTopList.extractCategory(document);
        for (Category category : categorys) {
            System.out.println(category);
        }
    }

}
