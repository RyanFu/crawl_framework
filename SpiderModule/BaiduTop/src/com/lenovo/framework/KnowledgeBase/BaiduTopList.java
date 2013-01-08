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
 * @Description : 处理 BaiduTop 列表
 */
public class BaiduTopList implements IHandle {

    private static List<String> crawlUrls;
    static {
        crawlUrls = new ArrayList<String>();
        crawlUrls.add("http://top.baidu.com/buzz.php?p=movie");
        crawlUrls.add("http://top.baidu.com/buzz.php?p=tv");
        crawlUrls.add("http://top.baidu.com/buzz.php?p=dianshi");
        crawlUrls.add("http://top.baidu.com/buzz.php?p=katong");
    }

    @Override
    public List<IMessage> handle(IMessage in, IBridge bridge) {
        String content = null;
        if (null == in || (content = in.GetBody()) == null) return null;
        if (null == bridge) return null;
        Document document = Jsoup.parse(content);
        List<Category> categorys = this.extractCategory(document);
        for (String url : crawlUrls) {
            Category category = this.checkUrl(url, categorys);
            IMessage message = MsgFactory.getDefaultDownMsg(bridge,
                    BaiduTop.module, url, "com.lenovo.framework.KnowledgeBase.BaiduTopItems");
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
        Elements els = document.select("li[onclick~=(?i)openWin]");
        for (Element el : els) {
            String url = this.getCategoryUrl(el.outerHtml());
            if (null == url) continue;
				url = url.trim();
            Category category = new Category(url, el.text());
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
        String regex = "openWin\\(this,'(.+)'\\)";
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
        DefaultDownloader BaiduDown = new DefaultDownloader();
        Document document = BaiduDown.getDocument("http://top.baidu.com/category.php?p=yule");
        BaiduTopList BaiduTopList = new BaiduTopList();
        List<Category> categorys = BaiduTopList.extractCategory(document);
        for (Category category : categorys) {
            System.out.println(category);
        }
    }

}
