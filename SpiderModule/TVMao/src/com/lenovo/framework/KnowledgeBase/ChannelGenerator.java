package com.lenovo.framework.KnowledgeBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

 


import com.lenovo.framework.KnowledgeBase.bean.TvMaoChannelPageUrl;
/*
'%天下足球%';       CCTV-5
'%今日关注%';       中文国际频道（CCTV-4）
'%中国梦想秀%';    浙江卫视
'%谁能百里挑一%'; 东方卫视
'%动物世界%';       中央电视台
'%8090%';            湖南卫视 
'%百变大咖秀%'      湖南卫视

 * */
public class ChannelGenerator {	  
	public final static  Map<String, String> whiteChannelMap = new HashMap<String, String>();
	public final static  Map<String, String> blackChannelMap = new HashMap<String, String>();
    static{
    	whiteChannelMap.put("http://www.tvmao.com/program/BTV-BTV2-w2.html", "1");    
    	whiteChannelMap.put("http://www.tvmao.com/program/BTV-BTV4-w2.html", "1");  
    	whiteChannelMap.put("http://www.tvmao.com/program/BTV-BTV6-w2.html", "1");  
    	whiteChannelMap.put("http://www.tvmao.com/program/BTV-BTV7-w2.html", "1");  
    	
    	//blackChannelMap.put("http://www.tvmao.com/program/BTV-BTV7-w2.html", "1");  
    }
  
	 /* private Boolean isDownArea = false;//抓 地方
	  private Boolean isImportantSatellite = true;//抓 重点卫视
	  private Boolean isOtherSatellite = false;//抓 其他卫视
	  private Boolean isDownGAT = false;//抓 港澳台*/
	public void Init(List<TvMaoChannelPageUrl> channelPageList, Boolean isDownArea, 
			Boolean isImportantSatellite, Boolean isOtherSatellite, Boolean isDownGAT ){		
		TvMaoChannelPageUrl item = new TvMaoChannelPageUrl();	
		if (isDownArea){			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("浙江卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/ZJTV-ZJTV1-w3.html");
			item.setChannelGroupName("浙江电视台");
			item.setAreaName("浙江");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("东方卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SHHAI-DONGFANG1-w3.html");
			item.setChannelGroupName("上海电视台");
			item.setAreaName("上海");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
		 
		/*	item = new TvMaoChannelPageUrl();
			item.setChannelName("湖南卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HUNANTV-HUNANTV1-w3.html");
			item.setChannelGroupName("湖南电视台");
			item.setAreaName("湖南");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);*/
			
		 	item = new TvMaoChannelPageUrl();
			item.setChannelName("CCTV-5体育频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/CCTV-CCTV5-w1.html");
			item.setChannelGroupName("中央电视台");
			item.setAreaName("CCTV");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			item = new TvMaoChannelPageUrl();
			item.setChannelName("CCTV-4中文国际频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/CCTV-CCTV4-w1.html");
			item.setChannelGroupName("中央电视台");
			item.setAreaName("CCTV");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			item = new TvMaoChannelPageUrl();
			item.setChannelName("CCTV-1综合频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/CCTV-CCTV1-w1.html");
			item.setChannelGroupName("中央电视台");
			item.setAreaName("CCTV");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("凤凰卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/PHOENIX");
			item.setChannelGroupName("凤凰卫视电视台");
			item.setAreaName("香港");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item); 
	/*		//地方
			item = new TvMaoChannelPageUrl();
			item.setChannelName("BTV纪实");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV12-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV12-w5.html");
			channelPageList.add(item);
			 
		  item = new TvMaoChannelPageUrl();
			item.setChannelName("山东教育台");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SDETV");
			item.setChannelGroupName("山东教育台");			
			item.setAreaName("山东");
			item.setAbsoluteUrl("http://www.tvmao.com/program/SDETV");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京体育频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV6-w2.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV6-w2.html");
			channelPageList.add(item);
	 	
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京科教频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV3-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV3-w5.html");
			channelPageList.add(item);
		 
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京财经频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV5-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV5-w5.html");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京青少频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV8-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV8-w5.html");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京新闻频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV9-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV9-w5.html");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("BTV卡酷少儿");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV10-w5.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV10-w5.html");
			channelPageList.add(item);
	
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京文艺频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV2-w2.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV2-w2.html");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京影视频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV4-w2.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV4-w2.html");
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京生活频道");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV7-w2.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl("http://www.tvmao.com/program/BTV-BTV7-w2.html");
			channelPageList.add(item);	*/
		}
		if (isImportantSatellite){
			//重点卫视		
			item = new TvMaoChannelPageUrl();
			item.setChannelName("山东卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SDTV-SDTV1-w3.html");
			item.setChannelGroupName("山东电视台");
			item.setAreaName("山东");
			item.setAbsoluteUrl("http://www.tvmao.com/program/SDTV-SDTV1-w3.html");
			channelPageList.add(item);
			
		    item = new TvMaoChannelPageUrl();
			item.setChannelName("安徽卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/AHTV-AHTV1-w3.html");
			item.setChannelGroupName("安徽电视台");
			item.setAreaName("安徽");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
					
			item = new TvMaoChannelPageUrl();
			item.setChannelName("浙江卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/ZJTV-ZJTV1-w3.html");
			item.setChannelGroupName("浙江电视台");
			item.setAreaName("浙江");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("江苏卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/JSTV-JSTV1-w3.html");
			item.setChannelGroupName("江苏电视台");
			item.setAreaName("江苏");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
				
			item = new TvMaoChannelPageUrl();
			item.setChannelName("湖南卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HUNANTV-HUNANTV1-w3.html");
			item.setChannelGroupName("湖南电视台");
			item.setAreaName("湖南");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
		
			item = new TvMaoChannelPageUrl();
			item.setChannelName("东南卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/FJTV-FJTV2-w3.html");
			item.setChannelGroupName("福建电视台");
			item.setAreaName("福建");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("天津卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/TJTV-TJTV1-w3.html");
			item.setChannelGroupName("天津电视台");
			item.setAreaName("天津");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("北京卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/BTV-BTV1-w3.html");
			item.setChannelGroupName("北京电视台");
			item.setAreaName("北京");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("东方卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SHHAI-DONGFANG1-w3.html");
			item.setChannelGroupName("上海电视台");
			item.setAreaName("上海");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("凤凰卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/PHOENIX");
			item.setChannelGroupName("凤凰卫视电视台");
			item.setAreaName("香港");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
		}
		 
		if (isOtherSatellite){
			//其他卫视
			item = new TvMaoChannelPageUrl();
			item.setChannelName("重庆卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/CCQTV-CCQTV1-w3.html");
			item.setChannelGroupName("重庆电视台");
			item.setAreaName("重庆");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("广东卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/GDTV-GDTV1-w3.html");
			item.setChannelGroupName("广东电视台");
			item.setAreaName("广东");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("广西卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/GUANXI-GUANXI1-w3.html");
			item.setChannelGroupName("广西电视台");
			item.setAreaName("广西");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("旅游卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/TCTC");
			item.setChannelGroupName("旅游卫视");
			item.setAreaName("海南");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
		
			item = new TvMaoChannelPageUrl();
			item.setChannelName("江西卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/JXTV-JXTV1-w3.html");
			item.setChannelGroupName("旅游电视台");
			item.setAreaName("江西");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("山东卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SDTV-SDTV1-w3.html");
			item.setChannelGroupName("山东电视台");
			item.setAreaName("山东");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("山西卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SXTV-SXTV1-w3.html");
			item.setChannelGroupName("山西电视台");
			item.setAreaName("山西");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);		
	
			item = new TvMaoChannelPageUrl();
			item.setChannelName("陕西卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SHXITV-SHXITV1-w3.html");
			item.setChannelGroupName("陕西电视台");
			item.setAreaName("陕西");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("辽宁卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/LNTV-LNTV1-w3.html");
			item.setChannelGroupName("辽宁电视台");
			item.setAreaName("辽宁");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("吉林卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/JILIN-JILIN1-w3.html");
			item.setChannelGroupName("吉林电视台");
			item.setAreaName("吉林");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("黑龙江卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HLJTV-HLJTV1-w3.html");
			item.setChannelGroupName("黑龙江电视台");
			item.setAreaName("黑龙江");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("湖北卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HUBEI-HUBEI1-w3.html");
			item.setChannelGroupName("湖北电视台");
			item.setAreaName("湖北");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("河南卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HNTV-HNTV1-w3.html");
			item.setChannelGroupName("河南电视台");
			item.setAreaName("河南");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("河北卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/HEBEI-HEBEI1-w3.html");
			item.setChannelGroupName("河北电视台");
			item.setAreaName("河北");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("四川卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/SCTV-SCTV1-w3.html");
			item.setChannelGroupName("四川电视台");
			item.setAreaName("四川");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("云南卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/YNTV-YNTV1-w3.html");
			item.setChannelGroupName("云南电视台");
			item.setAreaName("云南");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("贵州卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/GUIZOUTV-GUIZOUTV1-w3.html");
			item.setChannelGroupName("贵州电视台");
			item.setAreaName("贵州");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("新疆卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/XJTV-XJTV1-w3.html");
			item.setChannelGroupName("新疆电视台");
			item.setAreaName("新疆");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("西藏卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/XIZANGTV-XIZANGTV2-w3.html");
			item.setChannelGroupName("西藏电视台");
			item.setAreaName("西藏");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("青海卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/QHTV-QHTV1-w3.html");
			item.setChannelGroupName("青海电视台");
			item.setAreaName("青海");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("甘肃卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/GSTV-GSTV1-w3.html");
			item.setChannelGroupName("甘肃电视台");
			item.setAreaName("甘肃");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("宁夏卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/NXTV-NXTV1-w3.html");
			item.setChannelGroupName("宁夏电视台");
			item.setAreaName("宁夏");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
			
			item = new TvMaoChannelPageUrl();
			item.setChannelName("内蒙卫视");
			item.setLevel(3);
			item.setRelUrl("http://www.tvmao.com/program/NMGTV-NMGTV1-w3.html");
			item.setChannelGroupName("内蒙古电视台");
			item.setAreaName("内蒙");
			item.setAbsoluteUrl(item.getRelUrl());
			channelPageList.add(item);
		}
		if (isDownGAT){
		//港澳台	
		}
	}
	//默认通过
	public Boolean IsWhite(String url){
		Boolean ret = false;
		if ( whiteChannelMap.containsKey(url)){
			 ret = true;
		}	 
		return ret;
	}
	//默认通过
	public Boolean IsBlack(String url){
		Boolean ret = false;
		if ( blackChannelMap.containsKey(url)){
			 ret = true;
		}	 
		return ret;
	}
}
