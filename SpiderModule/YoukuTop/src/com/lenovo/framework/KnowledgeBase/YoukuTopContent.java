package com.lenovo.framework.KnowledgeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lenovo.framework.IBridge;
import com.lenovo.framework.IMessage;
import com.lenovo.framework.KnowledgeBase.bean.RankingRaw;
import com.lenovo.framework.KnowledgeBase.common.DefaultDownloader;
import com.lenovo.framework.KnowledgeBase.common.HeadTag;

/**
 * @Description :
 * @author : ligang8@lenovo.com
 * @date : 2012-9-7 下午3:30:53
 * @version : V1.0
 */
public class YoukuTopContent implements IHandle {

    private static List<String> actorUrls;
    static {
        actorUrls = new ArrayList<String>();
        actorUrls.add("http://index.youku.com/protop/0");
        actorUrls.add("http://index.youku.com/protop/2");
    }

    @Override
    public List<IMessage> handle(IMessage in, IBridge bridge) {
        String content = null;
        if (null == in || (content = in.GetBody()) == null)
            return null;
        if (null == bridge)
            return null;
        String categoryName = in.GetHeader(HeadTag.URLNAME);
        String entryUrl = in.GetHeader(HeadTag.URL);
        Document document = Jsoup.parse(content);
        // 获取推荐列表
        List<RankingRaw> rankingRaws = this.extractTop(document, entryUrl, categoryName);
        String fields[] = HeadTag.WEBRANKINGRAWFIELDS.split("/");
        for (RankingRaw rankingRaw : rankingRaws) {
        	rankingRaw.setVersion(in.GetHeader("Version"));
            StringBuilder notNullFields = new StringBuilder();
            IMessage message = MsgFactory.getDefaultDBMsg(bridge, UUID.randomUUID().toString(), HeadTag.WEBRANKINGRAW);
            for (int i = 0; i < fields.length; i++) {
                if (null != rankingRaw.getFieldsValue(fields[i])) {
                    message.SetHeader(HeadTag.FIELDPREFIX + fields[i], rankingRaw.getFieldsValue(fields[i]));
                    notNullFields.append(fields[i]).append("/");
                }
            }

            String notNullFieldsString = notNullFields.substring(0, notNullFields.length() - 1).toString();
            message.SetHeader(HeadTag.FIELDS, notNullFieldsString);
            bridge.SendMsg(message);
        }
        return null;
    }

    private List<RankingRaw> extractTop(Document document, String entryUrl, String categoryName) {
        if (null == document)
            return null;
        List<RankingRaw> topInfos = new ArrayList<RankingRaw>();
        Elements els = document.select("table > tbody > tr");
        for (Element el : els) {
            Elements childEls = el.children();

            // 获取节目指数
            Elements numElms = childEls.select("span[class=num]");
            String num = "";
            if (null != numElms) {
                num = numElms.first().text();
                if (null != num) {
                    num = num.replaceAll(",", "").replaceAll("，", "");
                }
            }
            // 获取节目名称
            String name = childEls.select("td[class=key] > a").attr("title").trim();

            String elHtml = el.outerHtml();
            // 获取节目url
            String mainUrl = this.extractorUrl(elHtml);
            // sourceId
            String sourceId = "";
            Integer strIndex = mainUrl.lastIndexOf("/");
            if (strIndex > 0)
                sourceId = mainUrl.substring(strIndex + 1);
            // 获取演员信息
            String actor = "";
            if (actorUrls.contains(entryUrl)) {
                actor = this.extractorActor(childEls);
            }

            RankingRaw rankingRaw = new RankingRaw();
            rankingRaw.setSource(YoukuTop.module);
            rankingRaw.setSourceId(sourceId);
            rankingRaw.setMainUrl(mainUrl);
            rankingRaw.setName(name);
            rankingRaw.setPoint(num);

            rankingRaw.setType(categoryName);
            rankingRaw.setActor(actor);

            topInfos.add(rankingRaw);

        }
        return topInfos;
    }

    /**
     * 抽取节目的url
     * 
     * @param elHtml
     * @return
     */
    private String extractorUrl(String elHtml) {
        String mainUrl = "";
        Pattern hrefPattern = Pattern.compile("encodeURIComponent\\('([^\\s]+)'\\)", Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE);
        Matcher m = hrefPattern.matcher(elHtml);
        if (m.groupCount() >= 1) {
            if (m.find()) {
                mainUrl = m.group(1);
            }
        }
        return mainUrl;
    }

    /**
     * 抽取节目的演员
     * 
     * @param childEls
     * @return
     */
    private String extractorActor(Elements childEls) {
        String ret = "";
        Elements actorElms = childEls.select("td[class=intro]");
        if (null != actorElms) {
            String actor = actorElms.first().text();
            // 规范演员名字的格式
            String actors[] = actor.split("/");
            for (int i = 0; i < actors.length; i++) {
                ret += "/" + actors[i].trim();
            }
        }
        ret = ret.startsWith("/") ? ret.substring(1) : "";
        System.out.println("actor\t" + ret);
        return ret;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DefaultDownloader youkuDown = new DefaultDownloader();
        String[] url = new String[] { "http://index.youku.com/protop/0", "电视剧" };
        // url = new String[] { "http://index.youku.com/protop/1", "综艺" };
        url = new String[] { "http://index.youku.com/protop/2", "电影" };
        // url = new String[] { "http://index.youku.com/protop/3", "动漫" };
        // url = new String[] { "http://index.youku.com/protop/4", "音乐" };
        // url = new String[] { "http://index.youku.com/protop/5", "游戏" };

        // url = new String[] { "http://index.youku.com/srankdetail/teleplay",
        // "搜索电视剧" };

        Document document = youkuDown.getDocument(url[0]);
        YoukuTopContent youkuTopContent = new YoukuTopContent();
        List<RankingRaw> rankingRaws = youkuTopContent.extractTop(document, url[0], url[1]);
        for (RankingRaw rankingRaw : rankingRaws) {
            System.out.println(rankingRaw);
        }
    }
}
