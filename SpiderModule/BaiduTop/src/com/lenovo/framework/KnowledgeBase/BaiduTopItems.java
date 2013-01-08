package com.lenovo.framework.KnowledgeBase;

import java.util.ArrayList;
import java.util.List;

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
 * @Description : 处理 BaiduTop 分类列表中的每个对象
 */
public class BaiduTopItems implements IHandle {

    @Override
    public List<IMessage> handle(IMessage in, IBridge bridge) {
        String content = null;
        if (null == in || (content = in.GetBody()) == null) return null;
        if (null == bridge) return null;
        Document document = Jsoup.parse(content);
        String type = in.GetHeader(HeadTag.URLNAME);
        bridge.LogInfo("type is :" + type);
        List<Category> categorys = this.extractCategory(document,type);
        //get 50 items eatch category
        for (Category item : categorys) {
        	String mainUrl = item.getUrl();
            IMessage message = MsgFactory.getDefaultDownMsg(bridge, BaiduTop.module, mainUrl, "com.lenovo.framework.KnowledgeBase.BaiduTopItemContent");
            message.SetHeader(HeadTag.URLNAME, item.getName());
            message.SetHeader("Version", in.GetHeader("Version"));
            bridge.SendMsg(message);
        }
        return null;
    }

    /**
     * 从指定的文档中， 抽取url
     * @param document
     * @param type
     * @return
     */
    private List<Category> extractCategory(Document document,String type) {
        if (null == document)
            return null;
        List<Category> categorys = new ArrayList<Category>();
        Elements els = document.select("td[class=key] >a");
        for (Element el : els) {
            String url = el.attr("href");
            if (null == url) continue;
            url = "http://top.baidu.com/" + url.trim();
            Category category = new Category(url, type);
            categorys.add(category);
        }
        return categorys;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DefaultDownloader BaiduDown = new DefaultDownloader();
        Document document = BaiduDown.getDocument("http://top.baidu.com/buzz.php?p=tv");
        BaiduTopItems BaiduTopList = new BaiduTopItems();
        List<Category> categorys = BaiduTopList.extractCategory(document,"电视剧");
        for (Category category : categorys) {
            System.out.println(category);
        }
    }

}
