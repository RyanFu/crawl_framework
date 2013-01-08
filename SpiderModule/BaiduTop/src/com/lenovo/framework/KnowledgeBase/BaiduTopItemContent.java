package com.lenovo.framework.KnowledgeBase;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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

public class BaiduTopItemContent implements IHandle {

	@Override
	public List<IMessage> handle(IMessage in, IBridge bridge) {
		String content = null;
		if (null == in || (content = in.GetBody()) == null) {
			return null;
		}
		if (null == bridge)
			return null;
		
		String categoryName = in.GetHeader(HeadTag.URLNAME);
		String entryUrl = in.GetHeader(HeadTag.URL);
		Document document = Jsoup.parse(content);
		// parse item detail html
		
		RankingRaw rankingRaw = this.parseItem(document, entryUrl, categoryName);
		rankingRaw.setVersion(in.GetHeader("Version"));
		String fields[] = HeadTag.WEBRANKINGRAWFIELDS.split("/");

		StringBuilder notNullFields = new StringBuilder();
		IMessage message = MsgFactory.getDefaultDBMsg(bridge, UUID.randomUUID().toString(), HeadTag.WEBRANKINGRAW);
		// set item type
		String type = in.GetHeader(HeadTag.URLNAME);
		message.SetHeader(HeadTag.URLNAME, type);
		
		for (int i = 0; i < fields.length; i++) {
			if (null != rankingRaw.getFieldsValue(fields[i])) {
				message.SetHeader(HeadTag.FIELDPREFIX + fields[i], rankingRaw.getFieldsValue(fields[i]));
				notNullFields.append(fields[i]).append("/");
			}
		}
		
//		if (null==rankingRaw.getActor() || null==rankingRaw.getPresenter()) {
//			message = MsgFactory.getDefaultDownMsg(bridge,BaiduTop.module, entryUrl, "com.lenovo.framework.KnowledgeBase.BaiduTopItemView");
//		}
		
		String notNullFieldsString = notNullFields.substring(0, notNullFields.length() - 1).toString();
		message.SetHeader(HeadTag.FIELDS, notNullFieldsString);
		bridge.SendMsg(message);
		
		return null;
	}

	private RankingRaw parseItem(Document document, String entryUrl, String category) {
		if (null == document)
			return null;
		RankingRaw rankingRaw = new RankingRaw();
		// id
		String id = extractId(document);
		rankingRaw.setSourceId(id);
		// name
		Elements nameNode = document.select("h2[class=fl]");
		String name = "";
		if (null != nameNode && nameNode.size() > 0) {
			name = nameNode.first().text();
		}
		// description
		Element intro = document.select("div[class=intro]").first();
		String descRaw = intro.text();
		String desc = "";
		if (null != descRaw) {
			desc = descRaw.substring(0, descRaw.indexOf("["));
		}
		// 获取节目指数(今日搜索)
		String num = "";
		Elements searchCounts = document.select("span[class=td-search fl f-bold]");
		if (null != searchCounts) {
			num = searchCounts.first().text();
			num = num.substring(num.indexOf("：") + 1);
		}

		HashMap<String, String> attrs = detail(document);
		// 获取演员信息
		if (null != attrs.get("actors")) {
			rankingRaw.setActor(String.valueOf(attrs.get("actors")));
		}
		rankingRaw.setSource(BaiduTop.module);
		rankingRaw.setMainUrl(entryUrl);
		rankingRaw.setName(filter(name));
		rankingRaw.setPoint(num);
		// 获取导演信息
		if (null != attrs.get("directors")) {
			rankingRaw.setDirector(String.valueOf(attrs.get("directors")));
		}
		if (null != attrs.get("presenter")) {
			rankingRaw.setPresenter(String.valueOf(attrs.get("presenter")));
		}
		rankingRaw.setType(category);
		rankingRaw.setDescription(filter(desc));
		return rankingRaw;
	}
	/**
	 * 解析详细内容
	 * @param document
	 * @return
	 */
	private HashMap<String, String> detail(Document document) {
		HashMap<String, String> map = new HashMap<String, String>();
		Elements attrs = document.select("li > span");
		for (int i = 0; i < attrs.size() - 1; i++) {
			Element li = attrs.get(i);
			String titText = li.text().trim();
			String value = attrs.get(i + 1).text();
			
			try {
				if (titText.equals(new String("导演：".getBytes("UTF-8"),"UTF-8"))) {
					map.put("directors", filter(value));
				} else if (titText.equals(new String("主演：".getBytes("UTF-8"),"UTF-8"))) {
					map.put("actors", filter(value));
				} else if (titText.equals(new String("主持人：".getBytes("UTF-8"),"UTF-8"))) { 
					map.put("presenter", filter(value));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	/**
	 * 转码，用/分割字符串
	 * @param raw
	 * @return
	 */
	private String filter(String raw) {
		String fresh = raw;
		if (raw.contains("；")) {
			fresh = raw.replaceAll("；", "/");
		}
		if (raw.contains("，")) {
			fresh = raw.replaceAll("，", "/");
		}
		if (raw.contains("、")) {
			fresh = raw.replaceAll("、", "/");
		}
		try {
			return new String(fresh.getBytes("UTF-8"),"UTF-8");  
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//extract Baike url
	/*
	private String extractUrl(Document doc) {
		String html = doc.body().outerHtml();
        String regex = "http://baike.baidu.com/view/\\d+.htm";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        String ret = "";
        while (matcher.find()) {
            ret = matcher.group();
        }
        return ret;
    }
    */
	
	private String extractId(Document doc) {
		String html = doc.body().outerHtml();
        String regex = "http://baike.baidu.com/view/(\\d+).htm";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        String ret = "";
        while (matcher.find()) {
            ret = matcher.group(1);
        }
        return ret;
    }

	public static void main(String[] args) {
		DefaultDownloader baiduDown = new DefaultDownloader();
		String[] url = new String[] { "http://top.baidu.com/detail.php?b=4&w=%CE%D2%CA%C7%CC%D8%D6%D6%B1%F82", "电视剧" };
		
		Document document = baiduDown.getDocument(url[0]);
		BaiduTopItemContent baiduTopContent = new BaiduTopItemContent();
		RankingRaw rankingRaw = baiduTopContent.parseItem(document, url[0], url[1]);
		System.out.println(rankingRaw);
	}
}
